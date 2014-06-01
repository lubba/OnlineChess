import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * User: Alpen Ditrix
 * Date: 01.06.2014
 * Time: 22:17
 */
public class EmptyUrlRedirect extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String redirect;
        if(session != null) {
            UserAccount user = (UserAccount) session.getAttribute("user");
            if (user != null) {
                redirect = RedirUrl.INDEX;
            } else {
                redirect = RedirUrl.LOGIN;
            }
        } else {
            redirect = RedirUrl.LOGIN;
        }
        resp.sendRedirect(req.getContextPath()+redirect);
    }
}
