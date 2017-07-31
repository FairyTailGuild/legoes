package lego_demo;

import java.util.List;
import java.util.HashMap;
import framework.sdk.DbHandler;
import framework.sdk.Framework;
import framework.sdk.DaemonAction;
import javax.servlet.ServletContext;
import org.apache.ibatis.session.SqlSession;

class ExecuteThread extends Thread {
        private DbHandler dbHandler;

        public ExecuteThread(DbHandler dbHandler) {
                this.dbHandler = dbHandler;
        }

        @Override
        public void run() {
                for (;;) {
                        SqlSession s = this.dbHandler.getSqlSession(true);
                        try {
                                Long count = 0L;
                                List<HashMap<String, Object>> list = s.selectList("lego_demo.getCount");
                                if (0 < list.size()) {
                                        HashMap<String, Object> hm = list.iterator().next();
                                        /*
                                         * 账户存在，但密码错误。获取当前的“失败重试计数”，并且增加1次失败记录。
                                         */
                                        count = ((Long) hm.get("count"));
                                }
                                Framework.LOG.info("[Daemon] Current has " + count + " human info in database");
                                Thread.sleep(1000 * 10);
                        } catch (Exception e) {
                                Framework.LOG.error(e.toString());
                        } finally {
                                s.close();
                        }
                }
        }
}

public class Daemon extends DaemonAction {
        // private DbHandler dbHandler;

        public Daemon(ServletContext servletContext, DbHandler dbHandler) {
                super(servletContext, dbHandler);
                // this.dbHandler = dbHandler;
        }

        @Override
        public void run() {
                // new ExecuteThread(this.dbHandler).start();
        }
}