import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Alpen Ditrix
 * Date: 20.05.2014
 * Time: 14:42
 */
public class GameServlet extends HttpServlet {


    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {req.getRequestDispatcher("/game.jsp").forward(req, resp);}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserAccount user = (UserAccount) (req.getSession().getAttribute("user"));
        if (user == null) {
            resp.sendRedirect("/chessonline/login");
        } else {
            fwd(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UserAccount user = (UserAccount) (req.getSession().getAttribute("user"));
        if (user == null) {
            resp.sendRedirect("/chessonline/login");
        } else {
            fwd(req, resp);
        }
    }

}