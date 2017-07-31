package lego_communication;

import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import framework.sdk.Message;
import framework.sdk.DbHandler;
import framework.sdk.Framework;
import framework.sdk.CustomAction;
import org.apache.ibatis.session.SqlSession;

public class Custom extends CustomAction {
        // private HttpServlet httpServlet;
        private HttpServletRequest httpServletRequest;
        private HttpServletResponse httpServletResponse;
        private HashMap<String, Object> parameter;
        private DbHandler dbHandler;
        private SqlSession sqlSession;

        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DbHandler dbHandler, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, dbHandler, parameter);
                // this.httpServlet = httpServlet;
                this.httpServletRequest = httpServletRequest;
                this.httpServletResponse = httpServletResponse;
                this.parameter = parameter;
                this.dbHandler = dbHandler;
        }

        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, SqlSession sqlSession, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, sqlSession, parameter);
                // this.httpServlet = httpServlet;
                this.httpServletRequest = httpServletRequest;
                this.httpServletResponse = httpServletResponse;
                this.parameter = parameter;
                this.sqlSession = sqlSession;
        }

        /**
         * （基于类内）添加消息
         * 
         * @param s 执行SqlSession
         * @param directory_uuid 附件所在上传目录uuid（附件所在目录uuid为FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF）
         * @param file_status 状态（1:正常 2:过期）
         * @param message_type 消息类型
         * @param title 标题
         * @param content 内容
         * @param content_type 内容类型
         * @param sender_uuid 发件人uuid
         * @param receiver_list 收件人编号集合
         * @param carbon_copy_list 抄送人编号集合
         * @param blind_carbon_copy_list 密送人编号集合
         * @param cluster_list 附件集群集合
         * @param read_status 读取状态
         * @return 1: 操作成功<br />
         *         -1: 附件不存在<br />
         *         -2: 添加消息出错<br />
         *         -3: 保存附件为正式文件出错<br />
         *         0: 操作异常<br />
         */
        public int inline_addMessage(SqlSession s, Object directory_uuid, Object file_status, Object message_type, Object title, Object content, Object content_type, Object sender_uuid, Object receiver_list, Object carbon_copy_list, Object blind_carbon_copy_list, Object cluster_list, Object read_status) {
                try {
                        int res = 0;
                        HashMap<String, Object> p = null;
                        // lego_storage.Custom c = new lego_storage.Custom(this.httpServlet, this.httpServletRequest, this.httpServletResponse, this.dbHandler, this.parameter);
                        // if (null != cluster_list) {
                        // String cluster_name[] = ((String) cluster_list).split(";");
                        // for (int i = 0; i < cluster_name.length; i++) {
                        // if (1 != c.inline_checkFileExist(s, cluster_name[i], directory_uuid, file_status, sender_uuid)) {
                        // // 附件不存在
                        // return -1;
                        // }
                        // }
                        // }
                        p = new HashMap<String, Object>();
                        p.put("message_type", message_type);
                        p.put("title", title);
                        p.put("content", content);
                        p.put("content_type", content_type);
                        p.put("sender_uuid", sender_uuid);
                        p.put("receiver_list", receiver_list);
                        p.put("carbon_copy_list", carbon_copy_list);
                        p.put("blind_carbon_copy_list", blind_carbon_copy_list);
                        p.put("cluster_list", cluster_list);
                        p.put("read_status", read_status);
                        res = s.insert("lego_communication.insertMessage", p);
                        if (1 > res) {
                                // 添加消息出错
                                return -2;
                        }
                        // if (null != cluster_list) {
                        // String cluster_name[] = ((String) cluster_list).split(";");
                        // for (int i = 0; i < cluster_name.length; i++) {
                        // if (1 != c.inline_saveFile(s, cluster_name[i], sender_uuid)) {
                        // // 保存附件为正式文件出错
                        // return -3;
                        // }
                        // }
                        // }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于事务）添加消息
         * 
         * 参数列表所需参数：<br />
         * directory_uuid 附件所在上传目录uuid（附件所在目录uuid为FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF）<br />
         * file_status 状态（1:正常 2:过期）<br />
         * message_type 消息类型<br />
         * title 标题<br />
         * content 内容<br />
         * content_type 内容类型<br />
         * sender_uuid 发件人uuid<br />
         * receiver_list 收件人编号集合<br />
         * carbon_copy_list 抄送人编号集合<br />
         * blind_carbon_copy_list 密送人编号集合<br />
         * cluster_list 附件集群集合<br />
         * read_status 读取状态<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_addMessage() {
                try {
                        if (1 != this.inline_addMessage(this.sqlSession, this.parameter.get("directory_uuid"), this.parameter.get("file_status"), this.parameter.get("message_type"), this.parameter.get("title"), this.parameter.get("content"), this.parameter.get("content_type"), this.parameter.get("creator_uuid"), this.parameter.get("receiver_list"), this.parameter.get("carbon_copy_list"), this.parameter.get("blind_carbon_copy_list"), this.parameter.get("cluster_list"), this.parameter.get("read_status"))) {
                                return -1;
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于调用）保存正式文件件
         * 
         * 参数列表所需参数：<br />
         * directory_uuid 附件所在上传目录uuid（附件所在目录标号为FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF）<br />
         * file_status 状态（1:正常 2:过期）<br />
         * message_type 消息类型<br />
         * title 标题<br />
         * content 内容<br />
         * content_type 内容类型<br />
         * sender_uuid 发件人uuid<br />
         * receiver_list 收件人编号集合<br />
         * carbon_copy_list 抄送人编号集合<br />
         * blind_carbon_copy_list 密送人编号集合<br />
         * cluster_list 附件集群集合<br />
         * read_status 读取状态<br />
         * 
         * ERROR_1: 附件不存在<br />
         * ERROR_2: 添加消息出错<br />
         * ERROR_3: 保存附件为正式文件出错<br />
         * ERROR_4: 操作异常<br />
         */
        public void c_addMessage() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(false);
                        try {
                                int res = 0;
                                res = this.inline_addMessage(s, this.parameter.get("directory_uuid"), this.parameter.get("file_status"), this.parameter.get("message_type"), this.parameter.get("title"), this.parameter.get("content"), this.parameter.get("content_type"), this.parameter.get("creator_uuid"), this.parameter.get("receiver_list"), this.parameter.get("carbon_copy_list"), this.parameter.get("blind_carbon_copy_list"), this.parameter.get("cluster_list"), this.parameter.get("read_status"));
                                switch (res) {
                                        // 操作成功
                                        case 1:
                                                s.commit();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                                                return;
                                        // 附件不存在
                                        case -1:
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
                                                return;
                                        // 添加消息出错
                                        case -2:
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_2");
                                                return;
                                        // 保存附件为正式文件出错
                                        case -3:
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
                                                return;
                                        // 操作异常
                                        default:
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_4");
                                                return;
                                }
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
