import chess.ChessGame;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * User: Alpen Ditrix
 * Date: 29.05.2014
 * Time: 11:25
 */
public class Scope {

    public static final    String a_NICKNAMES  = "id2nick";
    public static final    String a_GAMES      = "games";
    public static final    String s_ID         = "id";
    public static final    String a_GAME_QUEUE = "want2play";
    public static final    String s_REGISTERED = "registered";
    public static final    String a_ACCOUNTS   = "accounts";
    public static volatile int    idDispatcher = 0;

    public static Map<Integer, Integer> getColors(ServletContext context) {
        Map<Integer, Integer> map = (Map<Integer, Integer>) context.getAttribute("colors");
        if (map == null) {
            context.setAttribute("colorsStrings", new String[]{"White", "Black"});
            map = new HashMap<Integer, Integer>();
            context.setAttribute("colors", map);
        }
        return map;
    }

    public static Map<Integer, ChessGame> getGames(ServletContext context) {
        Map<Integer, ChessGame> map = (Map<Integer, ChessGame>) context.getAttribute(a_GAMES);
        if (map == null) {
            map = new HashMap<Integer, ChessGame>();
            context.setAttribute(a_GAMES, map);
        }
        return map;
    }

    public static Map<Integer, String> getNicknames(ServletContext context) {
        Map<Integer, String> map = (Map<Integer, String>) context.getAttribute(a_NICKNAMES);
        if (map == null) {
            map = new HashMap<Integer, String>();
            context.setAttribute(a_NICKNAMES, map);
        }
        return map;
    }

    public static int getSessionId(HttpSession session) {
        Integer id = (Integer) session.getAttribute(s_ID);
        if (id == null) {
            id = ++idDispatcher;
            session.setAttribute(s_ID, id);
        }
        return id;
    }

    public static List<Integer> getWantToPlayIDs(ServletContext context) {
        List<Integer> list = (List<Integer>) context.getAttribute(a_GAME_QUEUE);
        if (list == null) {
            list = new LinkedList<Integer>();
            context.setAttribute(a_GAME_QUEUE, list);
        }
        return list;
    }


    public static boolean tryRegister(int id, String nick, Map<Integer, String> nicknames, HttpSession session) {
        if (StringUtils.isEmpty(nick)) {
            return false;
        }
        nicknames.put(id, nick);
        List<Integer> wtp = getWantToPlayIDs(session.getServletContext());
        wtp.add(id);
        session.setAttribute(s_REGISTERED, true);
        return true;
    }

    public static List<UserAccount> getUserAccounts(ServletContext context) {
        List<UserAccount> list = (List<UserAccount>) context.getAttribute(a_ACCOUNTS);
        if (list == null) {
            list = new ArrayList<UserAccount>();
            context.setAttribute(a_ACCOUNTS, list);
        }
        return list;
    }
}
