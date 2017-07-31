package lego_user;

import framework.sdk.DbHandler;
import framework.sdk.DaemonAction;
import javax.servlet.ServletContext;

public class Daemon extends DaemonAction {
        public Daemon(ServletContext servletContext, DbHandler dbHandler) {
                super(servletContext, dbHandler);
        }

        @Override
        public void run() {
        }
}