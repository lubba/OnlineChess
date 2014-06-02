import chess.ChessGame;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class StartServlet extends HttpServlet {

    public static final String JASPER = "/start.jsp";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {req.getRequestDispatcher("/start.jsp").forward(req, resp);}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserAccount user = (UserAccount)(req.getSession().getAttribute("user"));
        if(user.getState() == UserAccount.State.AWAITING_GAME){
            resp.sendRedirect("/chessonline/game");
            return;
        } else if(user.getState() == UserAccount.State.SEARCHING) {
            resp.setIntHeader("Refresh",5);}
        fwd(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        UserAccount currentUser = (UserAccount)(req.getSession().getAttribute("user"));
        if(currentUser.getState() == UserAccount.State.AWAITING_GAME){
            resp.sendRedirect("/chessonline/game");
        } else if ("find".equals(action)) {
            currentUser.setState(UserAccount.State.SEARCHING);
            resp.setIntHeader("Refresh",5);
            fwd(req, resp);
        } else if ("play".equals(action)) {
            resp.setIntHeader("Refresh",5);
            int friendId = Integer.parseInt(req.getParameter("id"));
            List<UserAccount> users = Scope.getUserAccounts(req.getSession().getServletContext());
            UserAccount friend = new UserAccount("[NOT FOUND USER]", "");
            for (UserAccount u : users) {
                if (u.getId() != friendId) {
                    continue;
                } else {
                    friend = u;
                    break;
                }
            }
            if(friend.addOffer(currentUser)){
                resp.sendRedirect("/chessonline/game");
                return;
            }
            resp.setIntHeader("Refresh",5);
            req.setAttribute("friend", friend.getLogin().concat(".").concat(Integer.toString(friendId)));
            fwd(req, resp);
        }
    }

    private void createGame(int id, int friend, Map<Integer, ChessGame> n2g, ServletContext context) {
        int dice = new Random().nextInt();
        ChessGame game;
        Map<Integer, Integer> colors = Scope.getColors(context);
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
        fwd(req, resp);
    }
}
