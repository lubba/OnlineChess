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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("signup".equals(action)) {
            req.getRequestDispatcher("/register").forward(req, resp);
        } else if ("signin".equals(action)) {
            String login = (String) req.getParameter("login");
            String password = (String) req.getParameter("password");
            boolean fail = true;
            List<UserAccount> accounts = Scope.getUserAccounts(getServletContext());
            for (UserAccount acc : accounts) { // must be remade for BIG SERVER
                if (acc.tryLogin(login,password)){
                    fail = false;
                    req.getSession().setAttribute("user", acc);
                    break;
                }
            }
            req.setAttribute("fail", fail);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
