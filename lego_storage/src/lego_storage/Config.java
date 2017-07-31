package lego_storage;

import java.io.File;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.dom4j.Element;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import library.io.InputOutput;
import library.thread.Block;
import framework.sdk.Framework;

@SuppressWarnings("serial")
public class Config extends HttpServlet {
        /*
         * 上传路径
         */
        public static String UPLOAD_PATH = InputOutput.regulatePath(System.getProperty("user.dir")) + "/upload/";

        /*
         * 过期检查周期（默认：10分钟）
         */
        public static long EXPIRE_CHECK_CYCLE = 1000 * 60 * 10;

        /*
         * 临时上传有效时间（默认：1小时）
         */
        public static int TMP_UPLOAD_FILE_CYCLE = 1;

        /*
         * 上传有效时间（默认：100年）
         */
        public static int UPLOAD_FILE_CYCLE = 100;

        private void getInitConfig(String path) {
                try {
                        File file = new File(path);
                        SAXReader reader = new SAXReader();
                        Document doc = reader.read(file);
                        Element config = doc.getRootElement();
                        // 测试！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
                        // Element expireCheckCycle = config.element("expireCheckCycle");
                        // Config.EXPIRE_CHECK_CYCLE = Long.parseLong(expireCheckCycle.getText());
                        Config.EXPIRE_CHECK_CYCLE = 1000 * 30;
                        Element uploadPath = config.element("uploadPath");
                        Config.UPLOAD_PATH = uploadPath.getText();
                        /*
                         * 判断上传路径是否存在，如果不存在则创建。
                         */
                        File f = new File(Config.UPLOAD_PATH);
                        if (!f.exists()) {
                                f.mkdirs();
                        }
                        Config.UPLOAD_PATH = InputOutput.regulatePath(Config.UPLOAD_PATH);
                        Element tmpUploadFileCycle = config.element("tmpUploadFileCycle");
                        Config.TMP_UPLOAD_FILE_CYCLE = Integer.parseInt(tmpUploadFileCycle.getText());
                        Element uploadFileCycle = config.element("uploadFileCycle");
                        Config.UPLOAD_FILE_CYCLE = Integer.parseInt(uploadFileCycle.getText());
                } catch (Exception e) {
                        Framework.LOG.fatal(e.toString());
                }
        }

        @Override
        public void init(ServletConfig config) throws ServletException {
                super.init(config);
                String path = config.getInitParameter("path");
                this.getInitConfig(path);
                /*
                 * Daemon在Listener中启动，而Config在Servlet中启动（晚于前者），为了避免Daemon中读取Config数据出现错误，需要在Daemon增加一个线程锁，并当Config初始化结束后解锁。
                 */
                Block.unlock(Daemon.LOCK, Daemon.CONDITION);
        }

}