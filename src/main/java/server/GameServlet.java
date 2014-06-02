package server;

import chess.ChessGame;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

/**
 * User: Alpen Ditrix
 * Date: 20.05.2014
 * Time: 14:42
 */
public class GameServlet extends HttpServlet {

    private final Object gameCreationLocker = new Object();

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {req.getRequestDispatcher("/game.jsp").forward(req, resp);}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserAccount user = (UserAccount) (req.getSession().getAttribute("user"));
        if (user == null) {
            resp.sendRedirect("/chessonline/login");
        } else {
            synchronized (gameCreationLocker) {
                ChessGame game = user.getGame();
                if (game == null) {
                    UserAccount opponent = user.getOpponent();
                    int dice = new Random().nextInt()%2;
                    game = dice == 0 ? new ChessGame(user, opponent) : new ChessGame(opponent, user);
                    opponent.setGame(game);
                }
                user.setState(UserAccount.State.PLAYING);
            }
            fwd(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserAccount user = (UserAccount) (req.getSession().getAttribute("user"));
        if (user == null) {
            resp.sendRedirect("/chessonline/login");
        } else {
            String action = req.getParameter("action");
            if ("go".equals(action)) {
                String from = req.getParameter("from");
                String to = req.getParameter("to");
                try {
                    user.getGame().makeTurn(new ChessGame.Cell(from), new ChessGame.Cell(to));
                } catch (RuntimeException e) {
                    req.setAttribute("error", e.getMessage());
                }
            }
            fwd(req, resp);
        }
    }

    private void addErrorString(HttpServletRequest req, String error) {
        req.setAttribute("error", error.concat("\n").concat((String) req.getAttribute("error")));
    }

}