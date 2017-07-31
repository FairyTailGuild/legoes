package lego_user;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.sql.Timestamp;
import library.io.InputOutput;
import library.database.DatabaseKit;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import framework.sdk.Message;
import framework.sdk.DbHandler;
import framework.sdk.Framework;
import framework.sdk.CustomAction;
import org.apache.ibatis.session.SqlSession;
import org.dom4j.Element;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class Custom extends CustomAction {
        private HttpServletRequest httpServletRequest;
        private HttpServletResponse httpServletResponse;
        private HashMap<String, Object> parameter;
        private JSONArray modulePermissionList;
        private DbHandler dbHandler;

        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DbHandler dbHandler, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, dbHandler, parameter);
                this.httpServletRequest = httpServletRequest;
                this.httpServletResponse = httpServletResponse;
                this.parameter = parameter;
                this.dbHandler = dbHandler;
                this.modulePermissionList = new JSONArray();
                this.initModulePermission();
        }

        /**
         * 初始化模块权限
         */
        private void initModulePermission() {
                try {
                        String modulePath = Framework.PROJECT_REAL_PATH + "WEB-INF/temp/module/";
                        ArrayList<String> list = InputOutput.getCurrentDirectoryFolderName(modulePath);
                        Iterator<String> iter = list.iterator();
                        while (iter.hasNext()) {
                                String modName = iter.next();
                                String path = InputOutput.regulatePath(modulePath) + modName + "/resource/config/dispatch.xml";
                                File file = new File(path);
                                SAXReader reader = new SAXReader();
                                Document doc = reader.read(file);
                                Element root = doc.getRootElement();
                                Element dispatch = root.element("dispatch");
                                Iterator<?> servletIter = dispatch.elements("servlet").iterator();
                                while (servletIter.hasNext()) {
                                        Element servletElement = (Element) servletIter.next();
                                        String permission = servletElement.elementText("permission");
                                        if (!permission.equalsIgnoreCase("none")) {
                                                JSONObject obj = new JSONObject();
                                                obj.put("permission", permission);
                                                this.modulePermissionList.put(obj);
                                        }
                                }
                        }
                } catch (Exception e) {
                        Framework.LOG.error(e.toString());
                }
        }

        /**
         * 登录成功后，保存用户登录信息
         * 
         * @param uuid 用户uuid
         * @param role 用户角色
         */
        private void saveUserSession(HashMap<String, Object> hm) {
                this.httpServletRequest.getSession().setAttribute(Framework.USER_UUID, (String) hm.get("uuid"));
                this.httpServletRequest.getSession().setAttribute(Framework.USER_ROLE, (String) hm.get("role"));
        }

        /**
         * 安全信息和密码登录
         * 
         * @param s 执行SqlSession
         * @param securityKey 安全key（用户名登录为“name”、手机号登录为“telephone_number”、Email登录为“email”）
         * @param securityValue 安全值（与securityKey对应的值）
         * @param password 密码
         * 
         * @return 1 登录成功<br />
         *         2 登录成功（账户解冻）<br />
         *         -1 账户不存在<br />
         *         -2 密码错误<br />
         *         -3 “失败重试计数”超过系统规定次数，账户冻结<br />
         *         -4 账户已冻结<br />
         *         -5 账户被锁定<br />
         *         -6 账户异常<br />
         *         -7 操作错误<br />
         *         0 未知错误<br />
         */
        public int inline_loginWithPassword(SqlSession s, String securityKey, String securityValue, String password) {
                try {
                        // [内部变量声明]
                        HashMap<String, Object> p = null;
                        HashMap<String, Object> hm = null;
                        List<HashMap<String, Object>> list = null;
                        int res = 0;
                        // [开始逻辑判断]
                        // 检查用户名是否已存在
                        p = new HashMap<String, Object>();
                        p.put((String) securityKey, securityValue);
                        p.put("password", password);
                        list = s.selectList("lego_user.selectUserSecurity", p);
                        if (!DatabaseKit.hasData(list)) {
                                // 如果没有找到数据，再次判断用户是否存在
                                this.parameter.remove("password");
                                list = s.selectList("lego_user.selectUserSecurity", this.parameter);
                                if (!DatabaseKit.hasData(list)) {
                                        // 账户不存在
                                        return -1;
                                }
                                hm = list.iterator().next();
                                if (2 == (Integer) hm.get("status")) {
                                        // 账户已冻结
                                        return -4;
                                } else if (3 == (Integer) hm.get("status")) {
                                        // 账户被锁定
                                        return -5;
                                }
                                // 账户存在，但密码错误。获取当前的“失败重试计数”，并且增加1次失败记录。
                                Integer count = ((Integer) hm.get("failed_retry_count")) + 1;
                                // 判断是否超过最大重试计数限制
                                res = Config.FROZEN_FAILED_RETRY_COUNT - count;
                                String uuid = ((String) hm.get("uuid"));
                                if (0 < res) {
                                        // 如果没有超过规定值，那么增加失败重试计数。并且给予密码错误的提示信息。
                                        p = new HashMap<String, Object>();
                                        p.put("uuid", uuid);
                                        p.put("failed_retry_count", count);
                                        res = s.update("lego_user.updateUserSecurity", p);
                                        if (1 >= res) {
                                                // 密码错误
                                                return -2;
                                        } else {
                                                // 增加“失败重试计数”失败。
                                                Framework.LOG.error("Increase Failed Retry Count Error");
                                                return -7;
                                        }
                                } else {
                                        // 如果已经超过规定值，执行以下操作：冻结账号；失败重试计数归零；返回账户冻结信息。
                                        p = new HashMap<String, Object>();
                                        p.put("uuid", uuid);
                                        p.put("failed_retry_count", 0);
                                        p.put("frozen_datetime", new Timestamp(System.currentTimeMillis()));
                                        p.put("status", 2);
                                        res = s.update("lego_user.updateUserSecurity", p);
                                        if (1 >= res) {
                                                // “失败重试计数”超过系统规定次数，账户冻结
                                                return -3;
                                        } else {
                                                // 冻结账户失败
                                                Framework.LOG.error("Frozen Account Error");
                                                return -7;
                                        }
                                }
                        } else {
                                // 登录检索正确，判断账户状态。
                                hm = list.iterator().next();
                                if (1 == (Integer) hm.get("status")) {
                                        // 正常
                                        this.saveUserSession(hm);
                                        return 1;
                                } else if (2 == (Integer) hm.get("status")) {
                                        // 冻结，判断是否冻结时间已过，如果过期解冻账户。
                                        Timestamp frozenDateTime = ((Timestamp) list.iterator().next().get("frozen_datetime"));
                                        if (frozenDateTime.getTime() + Config.FROZEN_TIME < new Date().getTime()) {
                                                // 解冻账号
                                                p = new HashMap<String, Object>();
                                                p.put("uuid", hm.get("uuid"));
                                                p.put("failed_retry_count", 0);
                                                p.put("sn_frozen_datetime", "set_null");
                                                p.put("status", 1);
                                                res = s.update("lego_user.updateUserSecurity", p);
                                                if (1 >= res) {
                                                        // 已过期，解冻账号，返回登录正确信息，保存用户编号至session。
                                                        this.saveUserSession(hm);
                                                        return 2;
                                                } else {
                                                        // 解冻账户失败。
                                                        Framework.LOG.error("Unfrozen Account Error");
                                                        return -7;
                                                }
                                        }
                                        // 账户已冻结
                                        return -4;
                                } else if (3 == (Integer) hm.get("status")) {
                                        // 账户被锁定
                                        return -5;
                                } else {
                                        // 账户异常
                                        Framework.LOG.error("Account Exception");
                                        return -6;
                                }
                        }
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * 用户名和密码登录<br />
         * 
         * 参数列表所需参数：<br />
         * securityKey: 安全key（用户名登录为“name”、手机号登录为“telephone_number”、Email登录为“email”）<br />
         * [***]: 安全key指定的值<br />
         * password: 密码<br />
         */
        public void c_loginWithSecurityKeyPassword() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(true);
                        try {
                                // [内部变量声明]
                                int res = 0;
                                // [接收所需参数]
                                String securityKey = (String) this.parameter.get("securityKey");
                                String securityValue = (String) this.parameter.get(securityKey);
                                String password = (String) this.parameter.get("password");
                                // [开始逻辑判断]
                                res = this.inline_loginWithPassword(s, securityKey, securityValue, password);
                                switch (res) {
                                        // 登录成功
                                        case 1:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                                                return;
                                        // 登录成功（账户解冻）
                                        case 2:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                                                return;
                                        // 账户不存在
                                        case -1:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "账户不存在");
                                                return;
                                        // 密码错误
                                        case -2:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "密码错误");
                                                return;
                                        // “失败重试计数”超过系统规定次数，账户冻结
                                        case -3:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "“失败重试计数”超过系统规定次数，账户冻结");
                                                return;
                                        // 账户已冻结
                                        case -4:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "账户已冻结");
                                                return;
                                        // 账户被锁定
                                        case -5:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "账户被锁定");
                                                return;
                                        // 账户异常
                                        case -6:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "账户异常");
                                                return;
                                        // 操作错误
                                        case -7:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "操作错误");
                                                return;
                                        // 未知错误
                                        default:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "未知错误");
                                                return;
                                }
                        } catch (Exception e) {
                                if (Framework.DEBUG_ENABLE) {
                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
                                } else {
                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
                                }
                                Framework.LOG.warn(e.toString());
                        } finally {
                                s.close();
                        }
                } catch (Exception e) {
                        if (Framework.DEBUG_ENABLE) {
                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
                        } else {
                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
                        }
                        Framework.LOG.warn(e.toString());
                }
        }

        /**
         * 退出
         */
        public void c_logout() {
                this.httpServletRequest.getSession().setAttribute(Framework.USER_UUID, null);
                this.httpServletRequest.getSession().setAttribute(Framework.USER_ROLE, null);
                this.httpServletRequest.getSession().removeAttribute(Framework.USER_UUID);
                this.httpServletRequest.getSession().removeAttribute(Framework.USER_ROLE);
                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        }

        /**
         * 获得模块权限
         */
        public void c_getModulePermission() {
                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, this.modulePermissionList.length(), this.modulePermissionList.toString());
        }
}