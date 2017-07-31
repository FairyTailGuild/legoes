package lego_cms;

import java.util.List;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import framework.sdk.Message;
import framework.sdk.DbHandler;
import framework.sdk.Framework;
import framework.sdk.CustomAction;
import library.database.DatabaseKit;
import org.apache.ibatis.session.SqlSession;

public class Custom extends CustomAction {
        private HttpServletRequest httpServletRequest;
        private HttpServletResponse httpServletResponse;
        private HashMap<String, Object> parameter;
        private DbHandler dbHandler;

        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DbHandler dbHandler, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, dbHandler, parameter);
                this.httpServletRequest = httpServletRequest;
                this.httpServletResponse = httpServletResponse;
                this.parameter = parameter;
                this.dbHandler = dbHandler;
        }

        /**
         * 添加内容<br />
         * ERROR_1: 没有查询到type_id对应的类别数据。<br />
         * ERROR_2: cluster_name_list中的某个集群编号没有找到。<br />
         * ERROR_3: 增加CMS内容数据时出错。<br />
         */
        public void addCmsContent() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(true);
                        try {
                                // 检查type_id是否存在表中
                                HashMap<String, Object> p = new HashMap<String, Object>();
                                p.put("id", this.parameter.get("type_id"));
                                List<HashMap<String, Object>> list = s.selectList("lego_cms.selectCmsType", p);
                                if (!DatabaseKit.hasData(list)) {
                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_1");
                                        return;
                                }
                                // 检查cluster_name_list中的每一个文件是否在文件表中
                                String cluster = (String) this.parameter.get("cluster_name_list");
                                if (null != cluster) {
                                        String arr[] = cluster.split(";");
                                        for (int i = 0; i < arr.length; i++) {
                                                p = new HashMap<String, Object>();
                                                p.put("cluster_name", arr[i]);
                                                list = s.selectList("lego_storage.selectFile", p);
                                                if (!DatabaseKit.hasData(list)) {
                                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_2");
                                                        return;
                                                }
                                        }
                                }
                                int res = s.insert("lego_cms.insertCmsContent", this.parameter);
                                if (1 > res) {
                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
                                        return;
                                }
                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
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
         * 修改内容<br />
         * ERROR_1: 没有查询到type_id对应的类别数据。<br />
         * ERROR_2: cluster_name_list中的某个集群编号没有找到。<br />
         * ERROR_3: 修改CMS内容数据时出错。<br />
         */
        public void modifyCmsContent() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(false);
                        try {
                                // 检查type_id是否存在表中
                                HashMap<String, Object> p = new HashMap<String, Object>();
                                p.put("id", this.parameter.get("type_id"));
                                List<HashMap<String, Object>> list = s.selectList("lego_cms.selectCmsType", p);
                                if (!DatabaseKit.hasData(list)) {
                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_1");
                                        return;
                                }
                                // 检查cluster_name_list中的每一个文件是否在文件表中
                                String cluster = (String) this.parameter.get("cluster_name_list");
                                if (null != cluster) {
                                        String arr[] = cluster.split(";");
                                        for (int i = 0; i < arr.length; i++) {
                                                p = new HashMap<String, Object>();
                                                p.put("cluster_name", arr[i]);
                                                if (!DatabaseKit.hasData(list)) {
                                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_2");
                                                        return;
                                                }
                                        }
                                }
                                int res = s.update("lego_cms.updateCmsContent", this.parameter);
                                if (1 > res) {
                                        s.rollback();
                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
                                        return;
                                }
                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                        } catch (Exception e) {
                                s.rollback();
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
}