package lego_crm;

import java.util.HashMap;
import framework.sdk.DbHandler;
import framework.sdk.CustomAction;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Custom extends CustomAction {
        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DbHandler dbHandler, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, dbHandler, parameter);
        }
}
