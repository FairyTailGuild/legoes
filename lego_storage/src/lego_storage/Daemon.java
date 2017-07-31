package lego_storage;

import java.io.File;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.sql.Timestamp;
import framework.sdk.DbHandler;
import framework.sdk.Framework;
import library.database.DatabaseKit;
import library.thread.Block;
import framework.sdk.DaemonAction;
import javax.servlet.ServletContext;
import org.apache.ibatis.session.SqlSession;

class StorageCluster {
        private String uuid;
        private String name;
        private String suffix;
        private java.sql.Timestamp create_datetime;
        private java.sql.Timestamp expire_datetime;

        public StorageCluster(String uuid, String name, String suffix, Timestamp create_datetime, Timestamp expire_datetime) {
                super();
                this.uuid = uuid;
                this.name = name;
                this.suffix = suffix;
                this.create_datetime = create_datetime;
                this.expire_datetime = expire_datetime;
        }

        public String getUuid() {
                return uuid;
        }

        public String getName() {
                return name;
        }

        public String getSuffix() {
                return suffix;
        }

        public java.sql.Timestamp getCreate_datetime() {
                return create_datetime;
        }

        public java.sql.Timestamp getExpire_datetime() {
                return expire_datetime;
        }
}

class ExecuteThread extends Thread {
        private DbHandler dbHandler;

        public ExecuteThread(DbHandler dbHandler) {
                this.dbHandler = dbHandler;
        }

        private void clearExpireFile(ArrayList<StorageCluster> list) {
                SqlSession s = this.dbHandler.getSqlSession(false);
                try {
                        Iterator<StorageCluster> iter = list.iterator();
                        while (iter.hasNext()) {
                                StorageCluster sc = iter.next();
                                // 检查文件表中是否存在
                                HashMap<String, Object> p = new HashMap<String, Object>();
                                p.put("cluster_name", sc.getName());
                                List<HashMap<String, Object>> fileList = s.selectList("lego_storage.selectFile", p);
                                if (DatabaseKit.hasData(fileList)) {
                                        // 如果存在，目录编号为FFF...的直接删除，目录编号大于等于0的，文件状态标记为2（已过期）。
                                        HashMap<String, Object> hm = fileList.iterator().next();
                                        String directory_uuid = (String) hm.get("directory_uuid");
                                        int res = 0;
                                        p = new HashMap<String, Object>();
                                        if (!directory_uuid.equalsIgnoreCase("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) {
                                                p.put("cluster_name", sc.getName());
                                                p.put("status", 2);
                                                res = s.update("lego_storage.updateFile", p);
                                        } else {
                                                p.put("cluster_name", sc.getName());
                                                res = s.delete("lego_storage.deleteFile", p);
                                        }
                                        if (1 != res) {
                                                Framework.LOG.error("Clear Expire File Error For Update File Status (" + sc.getName() + ")");
                                                continue;
                                        }
                                }
                                // 删除集群表中的记录
                                p = new HashMap<String, Object>();
                                p.put("name", sc.getName());
                                int res = s.delete("lego_storage.deleteCluster", p);
                                if (1 == res) {
                                        File f = new File(Config.UPLOAD_PATH + sc.getName() + "." + sc.getSuffix());
                                        if (f.exists()) {
                                                if (!f.delete()) {
                                                        Framework.LOG.error("Delete File Error[" + f.getAbsolutePath() + "]");
                                                }
                                        }
                                        s.commit();
                                } else {
                                        s.rollback();
                                        Framework.LOG.error("Clear Expire File Error. File Uuid Is " + sc.getUuid());
                                }
                        }
                } catch (Exception e) {
                        s.rollback();
                        Framework.LOG.warn(e.toString());
                } finally {
                        s.close();
                }
        }

        @Override
        public void run() {
                try {
                        Block.lock(Daemon.LOCK, Daemon.CONDITION);
                } catch (Exception e) {
                        Framework.LOG.error(e.toString());
                }
                for (;;) {
                        SqlSession s = this.dbHandler.getSqlSession(true);
                        ArrayList<StorageCluster> scList = new ArrayList<StorageCluster>();
                        try {
                                // 如果休眠设置在最后，出现异常时会频繁的执行操作。
                                Thread.sleep(Config.EXPIRE_CHECK_CYCLE);
                                HashMap<String, Object> p = new HashMap<String, Object>();
                                p.put("underExpireDatetime", new java.sql.Timestamp(System.currentTimeMillis()));
                                List<HashMap<String, Object>> list = s.selectList("lego_storage.selectCluster", p);
                                if (1 <= list.size()) {
                                        Iterator<HashMap<String, Object>> iter = list.iterator();
                                        while (iter.hasNext()) {
                                                HashMap<String, Object> m = iter.next();
                                                String uuid = (String) m.get("uuid");
                                                String name = (String) m.get("name");
                                                String suffix = (String) m.get("suffix");
                                                java.sql.Timestamp create_datetime = (java.sql.Timestamp) m.get("create_datetime");
                                                java.sql.Timestamp expire_datetime = (java.sql.Timestamp) m.get("expire_datetime");
                                                scList.add(new StorageCluster(uuid, name, suffix, create_datetime, expire_datetime));
                                        }
                                }
                        } catch (InterruptedException e) {
                                Framework.LOG.info("Storage Daemon Stop");
                                return;
                        } catch (Exception e) {
                                Framework.LOG.warn(e.toString());
                        } finally {
                                s.close();
                        }
                        this.clearExpireFile(scList);
                }
        }
}

public class Daemon extends DaemonAction {
        private DbHandler dbHandler;
        private ExecuteThread thread;
        public static Lock LOCK = new ReentrantLock();
        public static Condition CONDITION = LOCK.newCondition();

        public Daemon(ServletContext servletContext, DbHandler dbHandler) {
                super(servletContext, dbHandler);
                this.dbHandler = dbHandler;
                this.thread = new ExecuteThread(this.dbHandler);
                // 添加守护进程对象至守护进程列表，以便于释放资源。
                DaemonAction.DAEMON_THREAD_LIST.add(this.thread);
        }

        @Override
        public void run() {
                // run方法由Listener调用，所以这里不能加线程锁，否则整个应用会堵塞在Listener调用run方法的地方。
                this.thread.start();
                Framework.LOG.info("Storage Daemon Start");
        }
}