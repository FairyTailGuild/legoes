package lego_user;

import java.io.File;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.dom4j.Element;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import framework.sdk.Framework;

@SuppressWarnings("serial")
public class Config extends HttpServlet {
        /*
         * 账号冻结的失败重试次数（默认：5次）
         */
        public static int FROZEN_FAILED_RETRY_COUNT = 5;

        /*
         * 账户冻结时间（默认：10分钟）
         */
        public static long FROZEN_TIME = 1000 * 60 * 10;

        private void getInitConfig(String path) {
                try {
                        File file = new File(path);
                        SAXReader reader = new SAXReader();
                        Document doc = reader.read(file);
                        Element config = doc.getRootElement();
                        Element frozenFailedRetryCount = config.element("frozenFailedRetryCount");
                        Config.FROZEN_FAILED_RETRY_COUNT = Integer.parseInt(frozenFailedRetryCount.getText());
                        Element frozenTime = config.element("frozenTime");
                        Config.FROZEN_TIME = Long.parseLong(frozenTime.getText());
                } catch (Exception e) {
                        Framework.LOG.fatal(e.toString());
                }
        }

        @Override
        public void init(ServletConfig config) throws ServletException {
                super.init(config);
                String path = config.getInitParameter("path");
                this.getInitConfig(path);
        }

}