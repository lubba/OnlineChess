import chess.ChessGame;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class FindGameServlet extends HttpServlet {

    public final static Object locker        = new Object();
    public final static String ACTION_FIND   = "find";
    public final static String ACTION_RETRY  = "retry";
    public static final String ACTION_DELETE = "delete";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        ServletContext context = session.getServletContext();

        int id = Scope.getSessionId(session);
        Map<Integer, ChessGame> games = Scope.getGames(context);
        if (games.get(id) != null) {
            redirectToGame(req, resp);
        } else {
            redirectToStart(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        ServletContext context = session.getServletContext();

        int id = Scope.getSessionId(session);
        Map<Integer, String> nicknames = Scope.getNicknames(context);
        Map<Integer, ChessGame> games = Scope.getGames(context);

        String action = req.getParameter("action");
        if (ACTION_RETRY.equals(action)) {
            if (games.get(id) != null) {
                redirectToGame(req, resp);
            } else {
                List<Integer> wantToPlay = Scope.getWantToPlayIDs(context);
                int lucky = wantToPlay.size() - 1;
                synchronized (locker) {
                    if (wantToPlay.get(lucky) == id) {
                        lucky = (lucky - 1) % wantToPlay.size();
                        if (wantToPlay.get(lucky) != id) {
                            int friend = wantToPlay.remove(lucky);
                            createGame(id, friend, games, context);
                            redirectToGame(req, resp);
                        } else {
                            redirectToStart(req, resp);
                        }
                    } else {
                        int friend = wantToPlay.remove(lucky);
                        createGame(id, friend, games, context);
                        redirectToGame(req, resp);
                    }
                }
            }
        } else if (ACTION_FIND.equals(action)) {
            String nick = req.getParameter("nickname");
            if (!Scope.tryRegister(id, nick, nicknames, session)) {
                req.setAttribute("error", "Incorrect nickname");
            }
            redirectToStart(req, resp);
        } else {
            redirectToStart(req, resp);
        }
    }


    private void createGame(int id, int friend, Map<Integer, ChessGame> n2g, ServletContext context) {
        int dice = new Random().nextInt();
        ChessGame game;
        Map<Integer,Integer> colors = Scope.getColors(context);
        if (dice % 2 == 0) {
            game = new ChessGame(id, friend);
            colors.put(id, ChessGame.WHITE);
            colors.put(friend, ChessGame.BLACK);
        } else {
            game = new ChessGame(friend, id);
            colors.put(id, ChessGame.BLACK);
            colors.put(friend, ChessGame.WHITE);
        }
        n2g.put(id, game);
        n2g.put(friend, game);
        ChessGame g1 = n2g.get(Integer.parseInt("1"));
        ChessGame g2 = n2g.get(Integer.parseInt("2"));
    }

    private void redirectToGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/game");
        rd.forward(req, resp);
    }

    private void redirectToStart(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/start.jsp");
        rd.forward(req, resp);
    }
}
