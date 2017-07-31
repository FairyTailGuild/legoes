package lego_demo;

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
        public static String PROJECT_NAME = "defaultName";
        public static String PROJECT_AUTHOR = "defaultAuthor";

        public Config() {
        }

        private void getInitConfig(String path) {
                try {
                        File file = new File(path);
                        SAXReader reader = new SAXReader();
                        Document doc = reader.read(file);
                        Element config = doc.getRootElement();
                        Element projectName = config.element("projectName");
                        Config.PROJECT_NAME = projectName.getText();
                        Element projectAuthor = config.element("projectAuthor");
                        Config.PROJECT_AUTHOR = projectAuthor.getText();
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