package lego_demo;

import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import framework.sdk.Message;
import framework.sdk.DbHandler;
import framework.sdk.Framework;
import framework.sdk.CustomAction;
import org.apache.ibatis.session.SqlSession;

public class Custom extends CustomAction {
        private HttpServletRequest httpServletRequest;
        private HttpServletResponse httpServletResponse;
        private DbHandler dbHandler;
        private HashMap<String, Object> parameter;

        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DbHandler dbHandler, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, dbHandler, parameter);
                this.httpServletRequest = httpServletRequest;
                this.httpServletResponse = httpServletResponse;
                this.dbHandler = dbHandler;
                this.parameter = parameter;
        }

        /**
         * 登录
         */
        public void login() {
                String name = (String) parameter.get("name");
                String password = (String) parameter.get("password");
                try {
                        if ((name.equalsIgnoreCase("admin")) && (password.equalsIgnoreCase("admin"))) {
                                HttpSession session = this.httpServletRequest.getSession();
                                session.setAttribute(Framework.USER_UUID, 1);
                                session.setAttribute(Framework.USER_ROLE, "demo_login,demo_delete,demo_modify,demo_modifyNameSexOk,demo_modifyNameSexErr,demo_keepBeauty");
                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                        } else {
                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "Name Or Password Is Error");
                        }
                } catch (Exception e) {
                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, e.toString());
                }
        }

        /**
         * 登出
         */
        public void logout() {
                try {
                        HttpSession session = this.httpServletRequest.getSession();
                        session.setAttribute(Framework.USER_UUID, null);
                        session.setAttribute(Framework.USER_ROLE, null);
                        session.removeAttribute(Framework.USER_UUID);
                        session.removeAttribute(Framework.USER_ROLE);
                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                } catch (Exception e) {
                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, e.toString());
                }
        }

        /**
         * 事务示例
         */
        public void keepBeauty() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(false);
                        try {
                                HashMap<String, Object> p = new HashMap<String, Object>();
                                p.put("id", this.parameter.get("id"));
                                p.put("age", this.parameter.get("age"));
                                int res = s.update("lego_demo.modify", p);
                                if (1 <= res) {
                                        List<HashMap<String, Object>> list = s.selectList("lego_demo.getInfo", this.parameter);
                                        if (0 < list.size()) {
                                                Iterator<HashMap<String, Object>> iter = list.iterator();
                                                while (iter.hasNext()) {
                                                        HashMap<String, Object> hm = iter.next();
                                                        Integer sex = (Integer) hm.get("sex");
                                                        Integer age = (Integer) hm.get("age");
                                                        if ((0 == sex) && (age > 18)) {
                                                                s.rollback();
                                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "Do Not Let The Beauty Go!");
                                                                return;
                                                        }
                                                        s.commit();
                                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                                                        return;
                                                }
                                        } else {
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "Get Info Error");
                                                return;
                                        }
                                } else {
                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "Modify Sex Error");
                                }
                        } catch (Exception e) {
                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
                        } finally {
                                s.close();
                        }
                } catch (Exception e) {
                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
                }
        }
}