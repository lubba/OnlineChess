package server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * User: Alpen Ditrix
 * Date: 02.06.2014
 * Time: 0:08
 */
public class LoginServlet extends HttpServlet {

    public static final String JASPER = "/auth/login.jsp";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {req.getRequestDispatcher(JASPER).forward(req, resp);}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fwd(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("signup".equals(action)) {
            resp.sendRedirect("/chessonline/register");
        } else if ("signin".equals(action)) {
            String login = (String) req.getParameter("login");
            String password = (String) req.getParameter("password");
            boolean fail = true;
            List<UserAccount> accounts = Scope.getUserAccounts(req.getSession().getServletContext());
            for (UserAccount acc : accounts) {
                if (acc.tryLogin(login, password)) {
                    fail = false;
                    req.getSession().setAttribute("user", acc);
                    break;
                }
            }
            req.setAttribute("fail", fail);
            fwd(req, resp);
        } else if ("logout".equals(action)) {
            req.getSession().invalidate();
            fwd(req, resp);
        }
    }
}
