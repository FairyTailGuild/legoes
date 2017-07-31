package lego_chinaArea;

import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import framework.sdk.DbHandler;
import framework.sdk.CustomAction;

public class Custom extends CustomAction {
        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DbHandler dbHandler, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, dbHandler, parameter);
        }
}