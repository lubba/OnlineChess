import chess.ChessGame;
import chess.ChessGame.Cell;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * User: Alpen Ditrix
 * Date: 20.05.2014
 * Time: 14:42
 */
public class GameServlet extends HttpServlet {

    public static final String ACTION_GO = "GO";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        ServletContext context = session.getServletContext();
        redirectToJasper(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        ServletContext context = session.getServletContext();

        int id = Scope.getSessionId(session);

        String action = req.getParameter("action");
        if (ACTION_GO.equals(action)) {
            ChessGame game = Scope.getGames(context).get(id);
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            try {
                Cell c_from = new Cell(from);
                Cell c_to = new Cell(to);
                game.makeTurn(c_from, c_to);
            } catch (Exception e) {
                session.setAttribute("error", e);
            }
            redirectToJasper(req, resp);
        }


        redirectToJasper(req, resp);
    }

    private void redirectToJasper(HttpServletRequest req, HttpServletResponse resp) throws ServletException,                                                  IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/game.jsp");
        rd.forward(req, resp);
    }
}