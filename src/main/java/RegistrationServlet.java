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
public class RegistrationServlet extends HttpServlet {

    public static final String JASPER = "/auth/register.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("register".equals(action)) {
            String login = (String) req.getParameter("login");
            String password = (String) req.getParameter("password");
            boolean fail = false;
            if (password.length() < 3) {
                fail = true;
                req.setAttribute("shortPassword", true);
            }
            if (login.length()==0) {
                fail = true;
                req.setAttribute("emptyLogin", true);
            }
            List<UserAccount> accounts = Scope.getUserAccounts(req.getSession().getServletContext());
            for (UserAccount acc : accounts) {
                if (acc.getLogin().equals(login)) {
                    fail = true;
                    req.setAttribute("duplicateLogin", true);
                    break;
                }
            }
            if(!fail) {
                UserAccount newAccount = new UserAccount(login, password);
                accounts.add(newAccount);
                accounts = Scope.getUserAccounts(req.getSession().getServletContext());
                req.getSession().setAttribute("user", newAccount);
            }
            req.setAttribute("fail", fail);
        } //else it's "signup" from login.jsp
        req.getRequestDispatcher(JASPER).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JASPER).forward(req, resp);
    }
}
