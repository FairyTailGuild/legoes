package lego_storage;

import java.io.File;
import java.util.List;
import java.util.HashMap;
import java.util.Calendar;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import framework.sdk.Message;
import framework.sdk.DbHandler;
import framework.sdk.Framework;
import framework.sdk.CustomAction;
import library.database.DatabaseKit;
import library.string.CharacterString;
import org.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.commons.fileupload.FileItem;

public class Custom extends CustomAction {
        private HttpServletRequest httpServletRequest;
        private HttpServletResponse httpServletResponse;
        private HashMap<String, Object> parameter;
        private DbHandler dbHandler;
        private SqlSession sqlSession;

        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DbHandler dbHandler, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, dbHandler, parameter);
                this.httpServletRequest = httpServletRequest;
                this.httpServletResponse = httpServletResponse;
                this.parameter = parameter;
                this.dbHandler = dbHandler;
        }

        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, SqlSession sqlSession, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, sqlSession, parameter);
                this.httpServletRequest = httpServletRequest;
                this.httpServletResponse = httpServletResponse;
                this.parameter = parameter;
                this.sqlSession = sqlSession;
        }

        /**
         * （基于调用）上传文件临时
         * 
         * 参数列表所需参数：<br />
         * directory_uuid: 上传目录uuid<br />
         * attachment: 上传文件<br />
         * creator_uuid: 文件集群创建者的uuid<br />
         */
        public void c_uploadTemporaryFile() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(false);
                        try {
                                // [内部变量声明]
                                HashMap<String, Object> p = null;
                                List<HashMap<String, Object>> list = null;
                                int res = 0;
                                // [接收所需参数]
                                String directory_uuid = (String) this.parameter.get("directory_uuid");
                                String creator_uuid = (String) this.parameter.get("creator_uuid");
                                FileItem attachment = null; // 特殊情况，后面处理。
                                // [开始逻辑判断]
                                if ((!directory_uuid.equalsIgnoreCase("00000000000000000000000000000000")) && (!directory_uuid.equalsIgnoreCase("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"))) {
                                        // 如果directory_uuid不等于“附件目录”或“根目录”时，判断目录是否存在，且是用户创建。
                                        p = new HashMap<String, Object>();
                                        p.put("uuid", directory_uuid);
                                        p.put("creator_uuid", creator_uuid);
                                        list = s.selectList("lego_storage.selectDirectory", p);
                                        if (!DatabaseKit.hasData(list)) {
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "没有找到directory_uuid对应的目录。");
                                                return;
                                        }
                                }
                                // 生成uuid文件名
                                String fileUuidName = CharacterString.getUuidStr(true);
                                // 根据当前时间和临时文件的有效期设置临时文件的过期时间
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.HOUR, Config.TMP_UPLOAD_FILE_CYCLE);
                                // 从参数中获取文件对象（服务器从这一步开始接收文件）
                                attachment = (FileItem) parameter.get("attachment");
                                // 获取文件后缀
                                String suffix = attachment.getName().substring(attachment.getName().lastIndexOf("."));
                                // 将文件信息写入“文件集群”表
                                p = new HashMap<String, Object>();
                                p.put("uuid", CharacterString.getUuidStr(true));
                                p.put("name", fileUuidName);
                                p.put("suffix", suffix.substring(1));
                                p.put("expire_datetime", cal.getTime());
                                res = s.insert("lego_storage.insertCluster", p);
                                if (1 > res) {
                                        s.rollback();
                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "写入文件集群表出错");
                                        return;
                                }
                                // 获取文件前缀
                                String fileName = attachment.getName().substring(0, attachment.getName().indexOf("."));
                                if (fileName.length() > 32) {
                                        // 如果文件名长度大于32，则自动截取。
                                        fileName = fileName.substring(0, 32);
                                }
                                // 检查同级目录下是否有重名文件，如果有重名文件自动增加“_时间戳”。
                                p = new HashMap<String, Object>();
                                p.put("directory_uuid", directory_uuid);
                                p.put("name", fileName);
                                p.put("creator_uuid", creator_uuid);
                                list = s.selectList("lego_storage.selectFile", p);
                                if (DatabaseKit.hasData(list)) {
                                        fileName += "_" + CharacterString.getCurrentFormatDateTime("yyyyMMddHHmmssSSS");
                                }
                                // 将文件信息写入“文件”表
                                p = new HashMap<String, Object>();
                                p.put("uuid", CharacterString.getUuidStr(true));
                                p.put("directory_uuid", directory_uuid);
                                p.put("name", fileName);
                                p.put("cluster_name", fileUuidName);
                                p.put("suffix", suffix.substring(1));
                                p.put("file_size", String.valueOf(attachment.getSize()));
                                p.put("creator_uuid", creator_uuid);
                                res = s.insert("lego_storage.insertFile", p);
                                if (1 > res) {
                                        s.rollback();
                                        Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "写入文件表出错");
                                        return;
                                }
                                // 保存文件至本地
                                File f = new File(Config.UPLOAD_PATH + fileUuidName + suffix);
                                attachment.write(f);
                                s.commit();
                                JSONObject obj = new JSONObject();
                                obj.put("cluster_name", fileUuidName);
                                obj.put("file_name", fileUuidName + suffix);
                                obj.put("file_size", String.valueOf(attachment.getSize()));
                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, obj.toString());
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

        /**
         * （基于类内）保存为永久文件
         * 
         * @param s 执行SqlSession
         * @param cluster_name 文件集群名称
         * @param creator_uuid 文件集群创建者uuid
         * 
         * @return 1: 操作成功<br />
         *         -1: 文件集群不能为空<br />
         *         -2: 文件集群不存在<br />
         *         -3: 修改文件集群有效期出错<br />
         *         0: 操作异常<br />
         */
        public int inline_savePermanentFile(SqlSession s, String cluster_name, String creator_uuid) {
                try {
                        // [内部变量声明]
                        HashMap<String, Object> p = null;
                        List<HashMap<String, Object>> list = null;
                        // [开始逻辑判断]
                        if (null == cluster_name) {
                                // 文件集群不能为空
                                return -1;
                        }
                        p = new HashMap<String, Object>();
                        p.put("cluster_name", cluster_name);
                        p.put("creator_uuid", creator_uuid);
                        list = s.selectList("lego_storage.selectFile", p);
                        if (!DatabaseKit.hasData(list)) {
                                // 文件集群不存在
                                return -2;
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.YEAR, Config.UPLOAD_FILE_CYCLE);
                        p = new HashMap<String, Object>();
                        p.put("name", cluster_name);
                        p.put("creator_uuid", creator_uuid);
                        p.put("expire_datetime", cal.getTime());
                        int res = s.update("lego_storage.updateCluster", p);
                        if (1 > res) {
                                // 修改文件集群有效期出错
                                return -3;
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于事务）保存为永久文件
         * 
         * 参数列表所需参数：<br />
         * cluster_name 文件集群名称<br />
         * creator_uuid: 文件集群创建者的uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_savePermanentFile() {
                try {
                        // [接收所需参数]
                        String cluster_name = (String) this.parameter.get("cluster_name");
                        if (null == cluster_name) {
                                return 1;
                        }
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        if (1 != this.inline_savePermanentFile(this.sqlSession, cluster_name, creator_uuid)) {
                                return -1;
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于事务）保存为永久文件
         * 
         * 参数列表所需参数：<br />
         * cluster_list 文件集群集合<br />
         * creator_uuid: 文件集群创建者的uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_savePermanentFiles() {
                try {
                        // [接收所需参数]
                        String cluster_list = (String) this.parameter.get("cluster_list");
                        if (null == cluster_list) {
                                return 1;
                        }
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        String cluster_name[] = cluster_list.split(";");
                        for (int i = 0; i < cluster_name.length; i++) {
                                if (1 != this.inline_savePermanentFile(this.sqlSession, cluster_name[i], creator_uuid)) {
                                        return -1;
                                }
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于调用）保存为永久文件
         * 
         * 参数列表所需参数：<br />
         * cluster_name 文件集群名称<br />
         * creator_uuid 文件集群创建者的uuid<br />
         */
        public void c_savePermanentFile() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(false);
                        try {
                                // [内部变量声明]
                                int res = 0;
                                // [接收所需参数]
                                String cluster_name = (String) this.parameter.get("cluster_name");
                                String creator_uuid = (String) this.parameter.get("creator_uuid");
                                // [开始逻辑判断]
                                res = this.inline_savePermanentFile(s, cluster_name, creator_uuid);
                                switch (res) {
                                        // 操作成功
                                        case 1:
                                                s.commit();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                                                return;
                                        // 文件集群不存在
                                        case -1:
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "文件集群不存在");
                                                return;
                                        // 修改文件集群有效期出错
                                        case -2:
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "修改文件集群有效期出错");
                                                return;
                                        // 操作异常
                                        default:
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "操作异常");
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

        /**
         * （基于调用）保存为永久文件
         * 
         * 参数列表所需参数：<br />
         * cluster_list 文件集群集合<br />
         * creator_uuid 文件集群创建者的uuid<br />
         */
        public void c_savePermanentFiles() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(false);
                        try {
                                // [内部变量声明]
                                int res = 0;
                                // [接收所需参数]
                                String cluster_list = (String) this.parameter.get("cluster_list");
                                String creator_uuid = (String) this.parameter.get("creator_uuid");
                                // [开始逻辑判断]
                                String cluster_name[] = cluster_list.split(";");
                                for (int i = 0; i < cluster_name.length; i++) {
                                        res = this.inline_savePermanentFile(s, cluster_name[i], creator_uuid);
                                        if (1 == res) {
                                                // 操作成功
                                                continue;
                                        } else if (-1 == res) {
                                                // 文件集群不存在
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "文件集群不存在");
                                                return;
                                        } else if (-2 == res) {
                                                // 修改文件集群有效期出错
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "修改文件集群有效期出错");
                                                return;
                                        } else {
                                                // 操作异常
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "操作异常");
                                                return;
                                        }
                                }
                                s.commit();
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

        /**
         * 迭代遍历获取目录下包括的所有目录
         */
        // private void getSubDirectory(ArrayList<Object> list, Object parent_uuid) {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(true);
        // try {
        // HashMap<String, Object> p = new HashMap<String, Object>();
        // p.put("parent_uuid", parent_uuid);
        // List<HashMap<String, Object>> l = s.selectList("lego_storage.selectDirectory", p);
        // if (!DatabaseKit.hasData(l)) {
        // return;
        // }
        // Iterator<HashMap<String, Object>> iter = l.iterator();
        // while (iter.hasNext()) {
        // HashMap<String, Object> o = iter.next();
        // Object uuid = o.get("uuid");
        // list.add(uuid);
        // this.getSubDirectory(list, uuid);
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于类内）删除目录
         * 
         * @param s 执行SqlSession
         * @param uuid 目录编号
         * @param creator_uuid 目录创建者uuid
         * 
         * @return 1: 操作成功<br />
         *         -1: 删除目录或子目录失败<br />
         *         -2: 文件不存在<br />
         *         -3: 删除“文件表”中数据出错<br />
         *         -4: 更新“集群表”过期出错<br />
         *         0: 操作异常<br />
         */
        // public int inline_removeDirectory(SqlSession s, Object uuid, Object creator_uuid) {
        // try {
        // // 遍历获取当前目录下所有的子目录
        // ArrayList<Object> dirList = new ArrayList<Object>();
        // dirList.add(uuid);
        // this.getSubDirectory(dirList, uuid);
        // // 删除所有子目录及其下的文件
        // Iterator<Object> dirIter = dirList.iterator();
        // while (dirIter.hasNext()) {
        // Object obj = dirIter.next();
        // HashMap<String, Object> p = new HashMap<String, Object>();
        // p.put("uuid", obj);
        // p.put("creator_uuid", creator_uuid);
        // int res = s.delete("lego_storage.deleteDirectory", p);
        // if (1 > res) {
        // // 删除目录或子目录失败
        // return -1;
        // }
        // p = new HashMap<String, Object>();
        // p.put("directory_uuid", obj);
        // List<HashMap<String, Object>> list = s.selectList("lego_storage.selectFile", p);
        // if (DatabaseKit.hasData(list)) {
        // Iterator<HashMap<String, Object>> iter = list.iterator();
        // while (iter.hasNext()) {
        // HashMap<String, Object> hm = iter.next();
        // Object cluster_name = hm.get("cluster_name");
        // Object file_creator_uuid = hm.get("creator_uuid");
        // res = this.inline_removeFile(s, cluster_name, file_creator_uuid);
        // switch (res这里已经于2017-07-18修改了，重新做判断) {
        // // 操作成功（目录包含文件）
        // case 1:
        // return 1;
        // // 文件不存在
        // case -1:
        // return -2;
        // // 删除“文件表”中数据出错
        // case -2:
        // return -3;
        // // 更新“集群表”过期出错
        // case -3:
        // return -4;
        // // 操作异常
        // default:
        // return 0;
        // }
        // }
        // }
        // }
        // // 操作成功（目录不包含文件）
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于事务）删除目录
         * 
         * 参数列表所需参数：<br />
         * uuid: 目录编号<br />
         * creator_uuid: 目录创建者uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        // public int t_removeDirectory() {
        // try {
        // if (1 == this.inline_removeDirectory(this.sqlSession, this.parameter.get("uuid"), this.parameter.get("creator_uuid"))) {
        // return 1;
        // }
        // return -1;

        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于类内）移动目录
         * 
         * @param s 执行SqlSession
         * @param directory_uuid 目录uuid
         * @param parent_uuid 父目录uuid
         * @param creator_uuid 目录集群创建者uuid
         * 
         * @return 1: 操作成功<br />
         *         -1: 当前目录不存在<br />
         *         -2: 父目录不存在<br />
         *         -3: 修改目录的父目录出错<br />
         *         0: 操作异常<br />
         */
        // public int inline_moveDirectory(SqlSession s, Object directory_uuid, Object parent_uuid, Object creator_uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // HashMap<String, Object> hm = null;
        // List<HashMap<String, Object>> list = null;
        // int res = 0;
        // // 判断当前目录是否存在
        // p = new HashMap<String, Object>();
        // p.put("uuid", directory_uuid);
        // p.put("creator_uuid", creator_uuid);
        // list = s.selectList("lego_storage.selectDirectory", p);
        // if (!DatabaseKit.hasData(list)) {
        // // 当前目录不存在
        // return -1;
        // }
        // hm = list.iterator().next();
        // String dirName = (String) hm.get("name");
        // // 在父目录不是0（根目录）的情况下，判断父目录是否存在。
        // if (!((String) parent_uuid).equalsIgnoreCase("00000000000000000000000000000000")) {
        // p = new HashMap<String, Object>();
        // p.put("uuid", parent_uuid);
        // p.put("creator_uuid", creator_uuid);
        // list = s.selectList("lego_storage.selectDirectory", p);
        // if (!DatabaseKit.hasData(list)) {
        // // 父目录不存在
        // return -2;
        // }
        // }
        // // 判断父目录下是否有重名目录，如果有重名目录自动增加“_时间戳”后缀。
        // p = new HashMap<String, Object>();
        // p.put("parent_uuid", parent_uuid);
        // p.put("name", dirName);
        // p.put("creator_uuid", creator_uuid);
        // list = s.selectList("lego_storage.selectDirectory", p);
        // if (DatabaseKit.hasData(list)) {
        // dirName += "_" + CharacterString.getCurrentFormatDateTime("yyyyMMddHHmmssSSS");
        // }
        // // 修改目录的父目录
        // p = new HashMap<String, Object>();
        // p.put("uuid", directory_uuid);
        // p.put("name", dirName);
        // p.put("parent_uuid", parent_uuid);
        // res = s.update("lego_storage.updateDirectory", p);
        // if (1 > res) {
        // // 修改目录的父目录出错
        // return -3;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于事务）删除目录
         * 
         * 参数列表所需参数：<br />
         * s 执行SqlSession<br />
         * directory_uuid 目录uuid<br />
         * parent_uuid 父目录uuid<br />
         * creator_uuid 目录集群创建者uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        // public int t_moveDirectory() {
        // try {
        // if (1 == this.inline_moveDirectory(this.sqlSession, this.parameter.get("directory_uuid"), this.parameter.get("parent_uuid"), this.parameter.get("creator_uuid"))) {
        // return 1;
        // }
        // return -1;

        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于调用）移动目录
         * 
         * 参数列表所需参数：<br />
         * s 执行SqlSession<br />
         * directory_uuid 目录uuid<br />
         * parent_uuid 父目录uuid<br />
         * creator_uuid 目录集群创建者uuid<br />
         * 
         * ERROR_1: 当前目录不存在。<br />
         * ERROR_2: 父目录不存在。<br />
         * ERROR_3: 修改目录的父目录出错。<br />
         * ERROR_4: 操作异常。<br />
         */
        // public void c_moveDirectory() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_moveDirectory(s, this.parameter.get("directory_uuid"), this.parameter.get("parent_uuid"), this.parameter.get("creator_uuid"));
        // switch (res) {
        // // 操作成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 当前目录不存在
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 父目录不存在
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_2");
        // return;
        // // 修改目录的父目录出错
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
        // return;
        // // 操作异常
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_4");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于类内）移动文件
         * 
         * @param s 执行SqlSession
         * @param directory_uuid 目录uuid
         * @param cluster_name 文件集群名称
         * @param creator_uuid 文件集群创建者uuid
         * 
         * 
         * @return 1: 操作成功<br />
         *         -1: 目标目录不存在<br />
         *         -2: 文件不存在<br />
         *         -3: 修改文件目录出错<br />
         *         0: 操作异常<br />
         */
        // public int inline_moveFile(SqlSession s, Object directory_uuid, Object cluster_name, Object creator_uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // HashMap<String, Object> hm = null;
        // int res = 0;
        // // 判断目标目录是否存在
        // p = new HashMap<String, Object>();
        // p.put("uuid", directory_uuid);
        // p.put("creator_uuid", creator_uuid);
        // list = s.selectList("lego_storage.selectDirectory", p);
        // if (!DatabaseKit.hasData(list)) {
        // // 目标目录不存在
        // return -1;
        // }
        // // 判断文件是否存在
        // p = new HashMap<String, Object>();
        // p.put("cluster_name", cluster_name);
        // list = s.selectList("lego_storage.selectFile", p);
        // if (!DatabaseKit.hasData(list)) {
        // // 文件不存在
        // return -2;
        // }
        // hm = list.iterator().next();
        // String fileName = (String) hm.get("name");
        // String fileSuffix = (String) hm.get("suffix");
        // // 判断目标目录下是否有重名文件，如果有重名文件自动增加“_时间戳”。
        // p = new HashMap<String, Object>();
        // p.put("directory_uuid", directory_uuid);
        // p.put("name", fileName);
        // p.put("suffix", fileSuffix);
        // p.put("creator_uuid", creator_uuid);
        // list = s.selectList("lego_storage.selectFile", p);
        // if (DatabaseKit.hasData(list)) {
        // fileName += "_" + CharacterString.getCurrentFormatDateTime("yyyyMMddHHmmssSSS");
        // }
        // // 修改文件的目录
        // p = new HashMap<String, Object>();
        // p.put("cluster_name", cluster_name);
        // p.put("directory_uuid", directory_uuid);
        // p.put("creator_uuid", creator_uuid);
        // res = s.update("lego_storage.updateFile", p);
        // if (1 > res) {
        // // 修改文件目录出错
        // return -3;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于事务）删除文件
         * 
         * 参数列表所需参数：<br />
         * directory_uuid 目录uuid<br />
         * cluster_name 文件集群名称<br />
         * creator_uuid 文件集群创建者uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        // public int t_moveFile() {
        // try {
        // if (1 == this.inline_moveFile(this.sqlSession, this.parameter.get("directory_uuid"), this.parameter.get("cluster_name"), this.parameter.get("creator_uuid"))) {
        // return 1;
        // }
        // return -1;

        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于调用）移动文件
         * 
         * 参数列表所需参数：<br />
         * directory_uuid 目录uuid<br />
         * cluster_name 文件集群名称<br />
         * creator_uuid 文件集群创建者uuid<br />
         * 
         * ERROR_1: 目标目录不存在。<br />
         * ERROR_2: 文件不存在。<br />
         * ERROR_3: 修改文件目录出错。<br />
         * ERROR_4: 操作异常。<br />
         */
        // public void c_moveFile() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_moveFile(s, this.parameter.get("directory_uuid"), this.parameter.get("cluster_name"), this.parameter.get("creator_uuid"));
        // switch (res) {
        // // 操作成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 目标目录不存在
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 文件不存在
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_2");
        // return;
        // // 修改文件目录出错
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
        // return;
        // // 操作异常
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_4");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于类内）检查目录是否重名
         * 
         * @param s 执行SqlSession
         * @param parent_uuid 父目录uuid
         * @param ne_id 排除不计的目录编号
         * @param name 父目录名称
         * @param creator_uuid 文件集群创建者uuid
         * 
         * @return 1: 目录有重名<br />
         *         2: 目录没有重名<br />
         *         -1: 父目录不能为空<br />
         *         -2: 父目录编号不能小于0<br />
         *         0: 操作异常<br />
         */
        // public int inline_checkDirectoryNameExist(SqlSession s, Object parent_uuid, Object ne_uuid, Object name, Object creator_uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // if (null == parent_uuid) {
        // // 父目录不能为空
        // return -1;
        // }
        // if (((String) parent_uuid).equalsIgnoreCase("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) {
        // // 父目录不能是附件目录
        // return -2;
        // }
        // p = new HashMap<String, Object>();
        // p.put("parent_uuid", parent_uuid);
        // p.put("ne_uuid", ne_uuid);
        // p.put("name", name);
        // p.put("creator_uuid", creator_uuid);
        // list = s.selectList("lego_storage.selectDirectory", p);
        // if (DatabaseKit.hasData(list)) {
        // // 目录有重名
        // return 1;
        // }
        // // 目录没有重名
        // return 2;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // // 操作异常
        // return 0;
        // }
        // }

        /**
         * （基于类内）检查文件是否重名
         * 
         * @param s 执行SqlSession
         * @param cluster_name 文件集群名称
         * @param directory_uuid 目录uuid
         * @param name 文件名
         * @param creator_uuid 文件集群创建者uuid
         * 
         * @return 1: 文件无重名<br />
         *         2: 文件有重名<br />
         *         -1: 文件集群不能为空<br />
         *         -2: 目录不能为空<br />
         *         -3: 目录编号不能小于0<br />
         *         -4: 文件名称不能为空<br />
         *         -5: 文件集群不存在<br />
         *         0: 操作异常<br />
         */
        // public int inline_checkFileNameExist(SqlSession s, Object cluster_name, Object directory_uuid, Object name, Object creator_uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // HashMap<String, Object> hm = null;
        // List<HashMap<String, Object>> list = null;
        // if (null == cluster_name) {
        // // 文件集群不能为空
        // return -1;
        // }
        // if (null == directory_uuid) {
        // // 目录不能为空
        // return -2;
        // }
        // if (((String) directory_uuid).equalsIgnoreCase("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) {
        // // 目录编号不能是附件目录
        // return -3;
        // }
        // if (null == name) {
        // // 文件名称不能为空
        // return -4;
        // }
        // p = new HashMap<String, Object>();
        // p.put("cluster_name", cluster_name);
        // p.put("directory_uuid", directory_uuid);
        // p.put("creator_uuid", creator_uuid);
        // list = s.selectList("lego_storage.selectFile", p);
        // if (!DatabaseKit.hasData(list)) {
        // // 文件集群不存在
        // return -5;
        // }
        // hm = list.iterator().next();
        // p = new HashMap<String, Object>();
        // p.put("ne_cluster_name", cluster_name);
        // p.put("directory_uuid", directory_uuid);
        // p.put("name", name);
        // p.put("suffix", hm.get("suffix"));
        // p.put("creator_uuid", creator_uuid);
        // list = s.selectList("lego_storage.selectFile", p);
        // if (DatabaseKit.hasData(list)) {
        // // 文件有重名
        // return 2;
        // }
        // // 文件无重名
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // // 操作异常
        // return 0;
        // }
        // }

        /**
         * （基于类内）检查目录是否存在
         * 
         * @param s 执行SqlSession
         * @param uuid 目录uuid
         * @param creator_uuid 目录创建者uuid
         * 
         * @return 1: 目录存在<br />
         *         -1: 目录不能为空<br />
         *         -2: 目录不存在<br />
         *         0: 操作异常<br />
         */
        // public int inline_checkDirectoryExist(SqlSession s, Object uuid, Object creator_uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // if (null == uuid) {
        // // 目录不能为空
        // return -1;
        // }
        // if ((((String) uuid).equalsIgnoreCase("00000000000000000000000000000000")) || (((String) uuid).equalsIgnoreCase("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"))) {
        // // 目录不能是根目录或附件目录
        // return -1;
        // }
        // if (((String) uuid).equalsIgnoreCase("00000000000000000000000000000000")) {
        // // 父目录默认存在
        // return 1;
        // }
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // p.put("creator_uuid", creator_uuid);
        // list = s.selectList("lego_storage.selectDirectory", p);
        // if (!DatabaseKit.hasData(list)) {
        // // 目录不存在
        // return -2;
        // }
        // // 目录存在
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // // 操作异常
        // return 0;
        // }
        // }

        /**
         * （基于类内）检查文件是否存在
         * 
         * @param s 执行SqlSession
         * @param cluster_name 文件集群名称
         * @param directory_uuid 目录的uuid
         * @param status 文件状态
         * @param creator_uuid 文件集群创建者的uuid
         * 
         * @return 1: 文件集群存在且合法<br />
         *         -1: 文件集群名称不能为空<br />
         *         -2: 文件集群不存在<br />
         *         0: 操作异常<br />
         */
        public int inline_checkFileExist(SqlSession s, String cluster_name, String directory_uuid, Integer status, String creator_uuid) {
                try {
                        // [内部变量声明]
                        HashMap<String, Object> p = null;
                        List<HashMap<String, Object>> list = null;
                        // [开始逻辑判断]
                        if (null == cluster_name) {
                                // 文件集群名称不能为空
                                return -1;
                        }
                        p = new HashMap<String, Object>();
                        p.put("cluster_name", cluster_name);
                        p.put("directory_uuid", directory_uuid);
                        p.put("status", status);
                        p.put("creator_uuid", creator_uuid);
                        list = s.selectList("lego_storage.selectFile", p);
                        if (!DatabaseKit.hasData(list)) {
                                // 文件集群不存在
                                return -2;
                        }
                        // 文件集群存在且合法
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        // 操作异常
                        return 0;
                }
        }

        /**
         * （基于事务）检查目录名称在某个父目录下是否存在
         * 
         * 参数列表所需参数：<br />
         * parent_uuid: 父目录uuid<br />
         * ne_uuid: 排除不计目录编号<br />
         * name: 目录名称<br />
         * creator_uuid: 创建者uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        // public int t_checkDirectoryNameExist() {
        // try {
        // if (1 == this.inline_checkDirectoryNameExist(this.sqlSession, this.parameter.get("parent_uuid"), this.parameter.get("ne_uuid"), this.parameter.get("name"), this.parameter.get("creator_uuid"))) {
        // return 1;
        // }
        // return -1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于事务）检查目录名称在某个父目录下是否“不”存在
         * 
         * 参数列表所需参数：<br />
         * parent_uuid: 父目录uuid<br />
         * ne_uuid: 排除不计目录编号<br />
         * name: 目录名称<br />
         * creator_uuid: 创建者uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        // public int t_checkDirectoryNameNotExist() {
        // try {
        // if (2 == this.inline_checkDirectoryNameExist(this.sqlSession, this.parameter.get("parent_uuid"), this.parameter.get("ne_uuid"), this.parameter.get("name"), this.parameter.get("creator_uuid"))) {
        // return 1;
        // }
        // return -1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于事务）检查文件是否重名
         * 
         * 参数列表所需参数：<br />
         * cluster_name: 集群文件名称<br />
         * directory_uuid: 目录uuid<br />
         * name: 文件名<br />
         * creator_uuid: 创建者uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        // public int t_checkFileNameExist() {
        // try {
        // if (2 == this.inline_checkFileNameExist(this.sqlSession, this.parameter.get("cluster_name"), this.parameter.get("directory_uuid"), this.parameter.get("name"), this.parameter.get("creator_uuid"))) {
        // return 1;
        // }
        // return -1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于事务）检查文件是否“不”重名
         * 
         * 参数列表所需参数：<br />
         * cluster_name: 集群文件名称<br />
         * directory_uuid: 目录uuid<br />
         * name: 文件名<br />
         * creator_uuid: 创建者uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        // public int t_checkFileNameNotExist() {
        // try {
        // if (1 == this.inline_checkFileNameExist(this.sqlSession, this.parameter.get("cluster_name"), this.parameter.get("directory_uuid"), this.parameter.get("name"), this.parameter.get("creator_uuid"))) {
        // return 1;
        // }
        // return -1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于事务）检查目录是否存在
         * 
         * 参数列表所需参数：<br />
         * uuid: 目录编号<br />
         * creator_uuid: 目录创建者uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        // public int t_checkDirectoryExist() {
        // try {
        // if (1 == this.inline_checkDirectoryExist(this.sqlSession, this.parameter.get("uuid"), this.parameter.get("creator_uuid"))) {
        // return 1;
        // }
        // return -1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于事务）检查文件是否存在
         * 
         * 参数列表所需参数：<br />
         * cluster_name: 文件集群名称<br />
         * directory_uuid: 目录的uuid<br />
         * status: 文件状态<br />
         * creator_uuid: 文件集群创建者的uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_checkFileExist() {
                try {
                        // [接收所需参数]
                        String cluster_name = (String) this.parameter.get("cluster_name");
                        if (null == cluster_name) {
                                return 1;
                        }
                        String directory_uuid = (String) this.parameter.get("directory_uuid");
                        Integer status = (Integer) this.parameter.get("status");
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        if (1 != this.inline_checkFileExist(this.sqlSession, cluster_name, directory_uuid, status, creator_uuid)) {
                                return -1;
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于事务）检查文件是否存在
         * 
         * 参数列表所需参数：<br />
         * cluster_list: 文件集群集合<br />
         * directory_uuid: 目录的uuid<br />
         * status: 文件状态<br />
         * creator_uuid: 文件集群创建者的uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_checkFilesExist() {
                try {
                        // [接收所需参数]
                        String cluster_list = (String) this.parameter.get("cluster_list");
                        if (null == cluster_list) {
                                return 1;
                        }
                        String directory_uuid = (String) this.parameter.get("directory_uuid");
                        Integer status = (Integer) this.parameter.get("status");
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        String cluster_name[] = cluster_list.split(";");
                        for (int i = 0; i < cluster_name.length; i++) {
                                if (1 != this.inline_checkFileExist(this.sqlSession, cluster_name[i], directory_uuid, status, creator_uuid)) {
                                        return -1;
                                }
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于事务）检查文件是否“不”存在
         * 
         * 参数列表所需参数：<br />
         * cluster_name: 文件集群名称<br />
         * directory_uuid: 目录的uuid<br />
         * status: 文件状态<br />
         * creator_uuid: 文件集群创建者的uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_checkFileNotExist() {
                try {
                        // [接收所需参数]
                        String cluster_name = (String) this.parameter.get("cluster_name");
                        if (null == cluster_name) {
                                return 1;
                        }
                        String directory_uuid = (String) this.parameter.get("directory_uuid");
                        Integer status = (Integer) this.parameter.get("status");
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        if (-2 == this.inline_checkFileExist(this.sqlSession, cluster_name, directory_uuid, status, creator_uuid)) {
                                return 1;
                        }
                        return -1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于事务）检查文件是否“不”存在
         * 
         * 参数列表所需参数：<br />
         * cluster_list: 文件集群集合<br />
         * directory_uuid: 目录的uuid<br />
         * status: 文件状态<br />
         * creator_uuid: 文件集群创建者的uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_checkFilesNotExist() {
                try {
                        // [内部变量声明]
                        int res = 0;
                        // [接收所需参数]
                        String cluster_list = (String) this.parameter.get("cluster_list");
                        if (null == cluster_list) {
                                return 1;
                        }
                        String directory_uuid = (String) this.parameter.get("directory_uuid");
                        Integer status = (Integer) this.parameter.get("status");
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        String cluster_name[] = cluster_list.split(";");
                        for (int i = 0; i < cluster_name.length; i++) {
                                res = this.inline_checkFileExist(this.sqlSession, cluster_name[i], directory_uuid, status, creator_uuid);
                                if (-2 == res) {
                                        continue;
                                }
                                return -1;
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于调用）检查目录是否重名
         * 
         * 参数列表所需参数：<br />
         * parent_uuid: 父目录uuid<br />
         * ne_uuid: 排除不计目录编号<br />
         * name: 目录名<br />
         * creator_uuid: 创建者uuid<br />
         * 
         * ERROR_1: 目录没有重名。<br />
         * ERROR_2: 父目录不能为空。<br />
         * ERROR_3: 父目录编号不能小于0。<br />
         * ERROR_4: 操作异常。<br />
         */
        // public void c_checkDirectoryNameExist() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(true);
        // try {
        // int res = this.inline_checkDirectoryNameExist(s, this.parameter.get("parent_uuid"), this.parameter.get("ne_uuid"), this.parameter.get("name"), this.parameter.get("creator_uuid"));
        // switch (res) {
        // // 目录有重名
        // case 1:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 目录没有重名
        // case 2:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_1");
        // return;
        // // 父目录不能为空
        // case -1:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_2");
        // return;
        // // 父目录编号不能小于0
        // case -2:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_3");
        // return;
        // // 操作异常
        // default:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_4");
        // return;
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于调用）检查文件是否重名
         * 
         * 参数列表所需参数：<br />
         * cluster_name: 集群文件名称<br />
         * directory_uuid: 目录uuid<br />
         * name: 文件名<br />
         * creator_uuid: 创建者uuid<br />
         * 
         * ERROR_1: 文件无重名。<br />
         * ERROR_2: 文件集群不能为空。<br />
         * ERROR_3: 目录不能为空。<br />
         * ERROR_4: 目录编号不能小于0。<br />
         * ERROR_5: 文件名称不能为空。<br />
         * ERROR_6: 操作异常。<br />
         */
        // public void c_checkFileNameExist() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(true);
        // try {
        // int res = this.inline_checkFileNameExist(s, this.parameter.get("cluster_name"), this.parameter.get("directory_uuid"), this.parameter.get("name"), this.parameter.get("creator_uuid"));
        // switch (res) {
        // // 文件无重名
        // case 1:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 文件有重名
        // case 2:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 文件集群不能为空
        // case -1:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_2");
        // return;
        // // 目录不能为空
        // case -2:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_3");
        // return;
        // // 目录编号不能小于0
        // case -3:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_4");
        // return;
        // // 文件名称不能为空
        // case -4:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_5");
        // return;
        // // 文件集群不存在
        // case -5:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 操作异常
        // default:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_6");
        // return;
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于调用）检查目录是否存在
         * 
         * 参数列表所需参数：<br />
         * uuid: 目录编号<br />
         * creator_uuid: 创建者uuid<br />
         * 
         * ERROR_1: 目录不能为空。<br />
         * ERROR_2: 目录不存在。<br />
         * ERROR_3: 操作异常。<br />
         */
        // public void c_checkDirectoryExist() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(true);
        // try {
        // int res = this.inline_checkDirectoryExist(s, this.parameter.get("uuid"), this.parameter.get("creator_uuid"));
        // switch (res) {
        // // 目录合法
        // case 1:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 目录不能为空
        // case -1:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_1");
        // return;
        // // 目录不存在
        // case -2:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_2");
        // return;
        // // 操作异常
        // default:
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
        // return;
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于调用）检查文件是否存在
         * 
         * 参数列表所需参数：<br />
         * cluster_name: 文件集群名称<br />
         * directory_uuid: 目录的uuid<br />
         * status: 文件状态<br />
         * creator_uuid: 文件集群创建者的uuid<br />
         */
        public void c_checkFileExist() {
                try {
                        // [内部变量声明]
                        int res = 0;
                        // [接收所需参数]
                        String cluster_name = (String) this.parameter.get("cluster_name");
                        String directory_uuid = (String) this.parameter.get("directory_uuid");
                        Integer status = (Integer) this.parameter.get("status");
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        SqlSession s = this.dbHandler.getSqlSession(true);
                        try {
                                res = this.inline_checkFileExist(s, cluster_name, directory_uuid, status, creator_uuid);
                                switch (res) {
                                        // 文件集群存在且合法
                                        case 1:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                                                return;
                                        // 文件集群名称不能为空
                                        case -1:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "文件集群名称不能为空");
                                                return;
                                        // 文件集群不存在
                                        case -2:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "文件集群不存在");
                                                return;
                                        // 操作异常
                                        default:
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "操作异常");
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
         * （基于调用）检查文件是否存在
         * 
         * 参数列表所需参数：<br />
         * cluster_list: 文件集群集合<br />
         * directory_uuid: 目录的uuid<br />
         * status: 文件状态<br />
         * creator_uuid: 文件集群创建者的uuid<br />
         */
        public void c_checkFilesExist() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(true);
                        try {
                                // [内部变量声明]
                                int res = 0;
                                // [接收所需参数]
                                String cluster_list = (String) this.parameter.get("cluster_list");
                                String directory_uuid = (String) this.parameter.get("directory_uuid");
                                Integer status = (Integer) this.parameter.get("status");
                                String creator_uuid = (String) this.parameter.get("creator_uuid");
                                // [开始逻辑判断]
                                String cluster_name[] = cluster_list.split(";");
                                for (int i = 0; i < cluster_name.length; i++) {
                                        res = this.inline_checkFileExist(s, cluster_name[i], directory_uuid, status, creator_uuid);
                                        if (1 == res) {
                                                // 文件集群存在且合法
                                                continue;
                                        } else if (-1 == res) {
                                                // 文件集群名称不能为空
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "文件集群名称不能为空");
                                                return;
                                        } else if (-2 == res) {
                                                // 文件集群不存在
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "文件集群不存在");
                                                return;
                                        } else {
                                                // 操作异常
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "操作异常");
                                                return;
                                        }
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
         * （基于类内）删除文件
         * 
         * @param s 执行SqlSession
         * @param cluster_name 文件集群名称
         * @param creator_uuid 文件集群创建者的uuid
         * 
         * @return 1: 文件集群不存在，判断删除成功<br />
         *         2: 操作成功<br />
         *         -1: 删除“文件表”中数据出错<br />
         *         -2: 更新“集群表”过期出错<br />
         *         0: 操作异常<br />
         */
        public int inline_removeFile(SqlSession s, String cluster_name, String creator_uuid) {
                try {
                        // [内部变量声明]
                        HashMap<String, Object> p = null;
                        List<HashMap<String, Object>> list = null;
                        int res = 0;
                        // [开始逻辑判断]
                        // 判断“文件集群”是否存在，如果不存在，那么则判断删除成功。
                        if (1 != this.inline_checkFileExist(s, cluster_name, null, null, null)) {
                                // 文件集群不存在，判断删除成功。
                                return 1;
                        }
                        // 删除“文件表”中的数据
                        p = new HashMap<String, Object>();
                        p.put("cluster_name", cluster_name);
                        res = s.delete("lego_storage.deleteFile", p);
                        if (1 > res) {
                                // 删除“文件表”中数据出错
                                return -1;
                        }
                        // 判断“集群表”是否存在
                        p = new HashMap<String, Object>();
                        p.put("name", cluster_name);
                        list = s.selectList("lego_storage.selectCluster", p);
                        if (DatabaseKit.hasData(list)) {
                                // 修改“集群表”中数据的过期时间为当前时间
                                p = new HashMap<String, Object>();
                                p.put("name", cluster_name);
                                p.put("expire_datetime", new java.sql.Timestamp(System.currentTimeMillis()));
                                res = s.update("lego_storage.updateCluster", p);
                                if (1 > res) {
                                        // 更新“集群表”过期出错
                                        return -2;
                                }
                        }
                        return 2;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于事务）删除文件
         * 
         * 参数列表所需参数：<br />
         * cluster_name: 文件集群名称<br />
         * creator_uuid: 文件创建者的uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_removeFile() {
                try {
                        // [接收所需参数]
                        String cluster_name = (String) this.parameter.get("cluster_name");
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        if (0 >= this.inline_removeFile(this.sqlSession, cluster_name, creator_uuid)) {
                                return -1;
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于事务）删除文件
         * 
         * 参数列表所需参数：<br />
         * cluster_list: 文件集群集合<br />
         * creator_uuid: 文件创建者的uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_removeFiles() {
                try {
                        // [接收所需参数]
                        String cluster_list = (String) this.parameter.get("cluster_list");
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        String cluster_name[] = cluster_list.split(";");
                        for (int i = 0; i < cluster_name.length; i++) {
                                if (0 >= this.inline_removeFile(this.sqlSession, cluster_name[i], creator_uuid)) {
                                        return -1;
                                }
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于类内）修改附件
         * 
         * @param s 执行SqlSession
         * @parem idColumnName 文件集群所在数据的唯一列名（如：“uuid”）
         * @parem idColumnValue 文件集群所在数据的唯一列值（如：“uuid”对应的数据）
         * @parem selectNamespace 查询文件集群所在数据的名空间
         * @parem clusterListColumnName 文件集群所在表中的列名（当一个表中存有多个附件的时候，列名会有不同的情况）
         * @parem creator_uuid “新”集群文件创建者的uuid
         * 
         * @return 1: 数据的文件集群不存在，也算操作成功<br />
         *         2: 删除文件集群成功<br />
         *         -1: 没有找到uuid与selectNamespace的对应的数据<br />
         *         -2: 删除文件集群失败<br />
         *         0: 操作异常<br />
         */
        public int inline_removeAttachment(SqlSession s, String idColumnName, String idColumnValue, String selectNamespace, String clusterListColumnName, String creator_uuid) {
                try {
                        // [内部变量声明]
                        HashMap<String, Object> p = null;
                        HashMap<String, Object> hm = null;
                        List<HashMap<String, Object>> list = null;
                        // [开始逻辑判断]
                        // 获取文件所在的数据
                        p = new HashMap<String, Object>();
                        p.put(idColumnName, idColumnValue);
                        list = this.sqlSession.selectList(selectNamespace, p);
                        if (!DatabaseKit.hasData(list)) {
                                // 没有找到uuid与selectNamespace的对应的数据
                                return -1;
                        }
                        hm = list.iterator().next();
                        // 获取文件所在数据的文件集群名称
                        String cluster_list = (String) hm.get(clusterListColumnName);
                        if (null == cluster_list) {
                                // 数据的文件集群不存在，也算操作成功。
                                return 1;
                        }
                        String cluster_name[] = cluster_list.split(";");
                        for (int i = 0; i < cluster_name.length; i++) {
                                if (0 >= this.inline_removeFile(this.sqlSession, cluster_name[i], creator_uuid)) {
                                        // 删除文件集群失败
                                        return -2;
                                }
                        }
                        // 删除文件集群成功
                        return 2;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        // 操作异常
                        return 0;
                }
        }

        /**
         * （基于事务）删除附件
         * 
         * 参数列表所需参数：<br />
         * idColumnName: 文件集群所在数据的唯一列名（如：“uuid”）<br />
         * idColumnValue: 文件集群所在数据的唯一列值（如：“uuid”对应的数据）<br />
         * selectNamespace: 查询文件集群所在数据的名空间<br />
         * clusterListColumnName: 文件集群所在表中的列名（当一个表中存有多个附件的时候，列名会有不同的情况）<br />
         * creator_uuid: 文件创建者的uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_removeAttachment() {
                try {
                        // [接收所需参数]
                        String idColumnName = (String) this.parameter.get("idColumnName");
                        String idColumnValue = (String) this.parameter.get("idColumnValue");
                        String selectNamespace = (String) this.parameter.get("selectNamespace");
                        String clusterListColumnName = (String) this.parameter.get("clusterListColumnName");
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        if (0 >= this.inline_removeAttachment(this.sqlSession, idColumnName, idColumnValue, selectNamespace, clusterListColumnName, creator_uuid)) {
                                return -1;
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于调用）删除目录
         * 
         * 参数列表所需参数：<br />
         * uuid: 目录编号<br />
         * creator_uuid: 目录创建者uuid<br />
         * 
         * ERROR_1: 删除目录或子目录失败。<br />
         * ERROR_2: 文件不存在。<br />
         * ERROR_3: 删除“文件表”中数据出错。<br />
         * ERROR_4: 更新“集群表”过期出错。<br />
         * ERROR_5: 操作异常。<br />
         */
        // public void c_removeDirectory() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_removeDirectory(s, this.parameter.get("uuid"), this.parameter.get("creator_uuid"));
        // switch (res) {
        // // 操作成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 删除目录或子目录失败
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 文件不存在
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_2");
        // return;
        // // 删除“文件表”中数据出错
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
        // return;
        // // 更新“集群表”过期出错
        // case -4:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_4");
        // return;
        // // 操作异常
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_5");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于调用）删除文件
         * 
         * 参数列表所需参数：<br />
         * cluster_name: 文件集群名称<br />
         * creator_uuid: 文件创建者的uuid<br />
         */
        public void c_removeFile() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(false);
                        try {
                                // [内部变量声明]
                                int res = 0;
                                // [接收所需参数]
                                String cluster_name = (String) this.parameter.get("cluster_name");
                                String creator_uuid = (String) this.parameter.get("creator_uuid");
                                // [开始逻辑判断]
                                res = this.inline_removeFile(s, cluster_name, creator_uuid);
                                switch (res) {
                                        // 文件集群不存在，判断删除成功。
                                        case 1:
                                                s.commit();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                                                return;
                                        // 操作成功
                                        case 2:
                                                s.commit();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
                                                return;
                                        // 删除“文件表”中数据出错
                                        case -1:
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "删除“文件表”中数据出错");
                                                return;
                                        // 更新“集群表”过期出错
                                        case -2:
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "更新“集群表”过期出错");
                                                return;
                                        // 操作异常
                                        default:
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "操作异常");
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

        /**
         * （基于调用）删除文件
         * 
         * 参数列表所需参数：<br />
         * cluster_list: 文件集群集合<br />
         * creator_uuid: 文件创建者的uuid<br />
         */
        public void c_removeFiles() {
                try {
                        SqlSession s = this.dbHandler.getSqlSession(false);
                        try {
                                // [内部变量声明]
                                int res = 0;
                                // [接收所需参数]
                                String cluster_list = (String) this.parameter.get("cluster_list");
                                String creator_uuid = (String) this.parameter.get("creator_uuid");
                                // [开始逻辑判断]
                                String cluster_name[] = cluster_list.split(";");
                                for (int i = 0; i < cluster_name.length; i++) {
                                        res = this.inline_removeFile(s, cluster_name[i], creator_uuid);
                                        if (1 == res) {
                                                // 文件集群不存在，判断删除成功
                                                continue;
                                        } else if (2 == res) {
                                                // 操作成功
                                                continue;
                                        } else if (-1 == res) {
                                                // 删除“文件表”中数据出错
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "删除“文件表”中数据出错");
                                                return;
                                        } else if (-2 == res) {
                                                // 更新“集群表”过期出错
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "更新“集群表”过期出错");
                                                return;
                                        } else {
                                                // 操作异常
                                                s.rollback();
                                                Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "操作异常");
                                                return;
                                        }
                                }
                                s.commit();
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

        /**
         * （基于类内）修改附件
         * 
         * @param s 执行SqlSession
         * @parem idColumnName 文件集群所在数据的唯一列名（如：“uuid”）
         * @parem idColumnValue 文件集群所在数据的唯一列值（如：“uuid”对应的数据）
         * @parem selectNamespace 查询文件集群所在数据的名空间
         * @parem updateNamespace 更新文件集群所在数据的名空间
         * @parem clusterListColumnName 文件集群所在表中的列名（当一个表中存有多个附件的时候，列名会有不同的情况）
         * @parem newClusterList “新”文件集群集合
         * @parem directory_uuid “新”集群文件的目录uuid
         * @parem status “新”集群文件的状态
         * @parem creator_uuid “新”集群文件创建者的uuid
         * 
         * @return 1: 操作成功<br />
         *         -1: 没有找到uuid与selectNamespace的对应的数据<br />
         *         -2: 删除“旧”文件集群失败<br />
         *         -3: 清空“旧”文件集群数据失败<br />
         *         -4: 删除“旧”文件集群失败<br />
         *         -5: 清空“旧”集群文件数据失败<br />
         *         -6: “新”集群文件不合法<br />
         *         -7: 保存“新”集群文件为永久文件失败<br />
         *         -8: 更新集群数据为“新”文件集群失败<br />
         *         0: 操作异常<br />
         */
        public int inline_modifyAttachment(SqlSession s, String idColumnName, String idColumnValue, String selectNamespace, String updateNamespace, String clusterListColumnName, String newClusterList, String directory_uuid, Integer status, String creator_uuid) {
                try {
                        // [内部变量声明]
                        HashMap<String, Object> p = null;
                        HashMap<String, Object> hm = null;
                        List<HashMap<String, Object>> list = null;
                        int res = 0;
                        // [开始逻辑判断]
                        // 获取文件集群所在的数据
                        p = new HashMap<String, Object>();
                        p.put(idColumnName, idColumnValue);
                        list = this.sqlSession.selectList(selectNamespace, p);
                        if (!DatabaseKit.hasData(list)) {
                                // 没有找到uuid与selectNamespace的对应的数据
                                return -1;
                        }
                        hm = list.iterator().next();
                        // “旧”文件集群
                        String oldClusterList = (String) hm.get(clusterListColumnName);
                        // “新”文件集群
                        // 如果“旧”文件集群不为空，那么判断是否有效，无效则清空该项数据。
                        if (null != oldClusterList) {
                                if ((null == newClusterList)) {
                                        String oldClusterName[] = oldClusterList.split(";");
                                        for (int i = 0; i < oldClusterName.length; i++) {
                                                // 删除“旧”文件集群
                                                if (0 >= this.inline_removeFile(this.sqlSession, oldClusterName[i], null)) {
                                                        // 删除“旧”文件集群失败
                                                        return -2;
                                                }
                                                // 清空“旧”文件集群数据
                                                p = new HashMap<String, Object>();
                                                p.put(idColumnName, idColumnValue);
                                                p.put("sn_" + clusterListColumnName, "set_null");
                                                res = this.sqlSession.update(updateNamespace, p);
                                                if (1 > res) {
                                                        // 清空“旧”文件集群数据失败
                                                        return -3;
                                                }
                                        }
                                } else {
                                        String oldClusterName[] = oldClusterList.split(";");
                                        String newClusterName[] = newClusterList.split(";");
                                        for (int i = 0; i < oldClusterName.length; i++) {
                                                boolean hasSameFile = false;
                                                for (int j = 0; j < newClusterName.length; j++) {
                                                        if (oldClusterName[i].equalsIgnoreCase(newClusterName[j])) {
                                                                hasSameFile = true;
                                                                break;
                                                        }
                                                }
                                                if (hasSameFile) {
                                                        continue;
                                                }
                                                // 删除“旧”文件集群
                                                if (0 >= this.inline_removeFile(this.sqlSession, oldClusterName[i], null)) {
                                                        // 删除“旧”文件集群失败
                                                        return -4;
                                                }
                                                // 清空“旧”集群文件数据
                                                p = new HashMap<String, Object>();
                                                p.put(idColumnName, idColumnValue);
                                                p.put("sn_" + clusterListColumnName, "set_null");
                                                res = this.sqlSession.update(updateNamespace, p);
                                                if (1 > res) {
                                                        // 清空“旧”集群文件数据失败
                                                        return -5;
                                                }
                                        }
                                }
                        }
                        if (((null == oldClusterList) && (null == newClusterList)) || ((null != oldClusterList) && (null == newClusterList))) {
                                // （如果“旧”文件集群为空，且“新”文件集群为空，不做任何操作）
                                // （如果“旧”文件集群不为空，且“新”文件集群为空，执行清空“旧”文件集群的操作，上面已实现）
                        } else if (((null == oldClusterList) && (null != newClusterList)) || ((null != oldClusterList) && (null != newClusterList))) {
                                // （如果“旧”文件集群为空，且“新”文件集群不为空，判断“新”文件集群是否合法，然后更新数据）
                                // （如果“旧”文件集群不为空，且“新”文件集群不为空，且在两者不相等的情况下，判断“新”文件集群是否合法，执行删除“旧”文件集群的操作，并更新集群数据为“新”文件集群）
                                // 判断“新”集群文件是否合法
                                String newClusterName[] = newClusterList.split(";");
                                for (int i = 0; i < newClusterName.length; i++) {
                                        if (1 != this.inline_checkFileExist(this.sqlSession, newClusterName[i], directory_uuid, status, creator_uuid)) {
                                                // “新”集群文件不合法
                                                return -6;
                                        }
                                        if (1 != this.inline_savePermanentFile(this.sqlSession, newClusterName[i], creator_uuid)) {
                                                // 保存“新”集群文件为永久文件失败
                                                return -7;
                                        }
                                }
                                // 更新集群数据为“新”文件集群
                                p = new HashMap<String, Object>();
                                p.put(idColumnName, idColumnValue);
                                p.put(clusterListColumnName, newClusterList);
                                res = this.sqlSession.update(updateNamespace, p);
                                if (1 > res) {
                                        // 更新集群数据为“新”文件集群失败
                                        return -8;
                                }
                        }
                        // 操作成功
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        // 操作异常
                        return 0;
                }
        }

        /**
         * （基于事务）修改附件
         * 
         * 参数列表所需参数：<br />
         * idColumnName: 文件集群所在数据的唯一列名（如：“uuid”）<br />
         * idColumnValue: 文件集群所在数据的唯一列值（如：“uuid”对应的数据）<br />
         * selectNamespace: 查询文件集群所在数据的名空间<br />
         * updateNamespace: 更新文件集群所在数据的名空间<br />
         * clusterListColumnName: 文件集群所在表中的列名（当一个表中存有多个附件的时候，列名会有不同的情况）<br />
         * newClusterList: “新”文件集群集合<br />
         * directory_uuid: “新”集群文件的目录uuid<br />
         * status: “新”集群文件的状态<br />
         * creator_uuid: “新”集群文件创建者的uuid<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_modifyAttachment() {
                try {
                        // [接收所需参数]
                        String idColumnName = (String) this.parameter.get("idColumnName");
                        String idColumnValue = (String) this.parameter.get("idColumnValue");
                        String selectNamespace = (String) this.parameter.get("selectNamespace");
                        String updateNamespace = (String) this.parameter.get("updateNamespace");
                        String clusterListColumnName = (String) this.parameter.get("clusterListColumnName");
                        String newClusterList = (String) this.parameter.get("newClusterList");
                        String directory_uuid = (String) this.parameter.get("directory_uuid");
                        Integer status = (Integer) this.parameter.get("status");
                        String creator_uuid = (String) this.parameter.get("creator_uuid");
                        // [开始逻辑判断]
                        if (1 != this.inline_modifyAttachment(this.sqlSession, idColumnName, idColumnValue, selectNamespace, updateNamespace, clusterListColumnName, newClusterList, directory_uuid, status, creator_uuid)) {
                                return -1;
                        }
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }
}
