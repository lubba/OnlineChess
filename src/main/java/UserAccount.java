import chess.ChessGame;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: Alpen Ditrix
 * Date: 01.06.2014
 * Time: 22:41
 */
public class UserAccount {
    public enum State {
        PLAYING,
        SEARCHING,
        NOT_PLAYING,
        AWAITING_GAME;
    }

    private static final String SALT         = "my name is Alice";
    private static       int    idDispatcher = 1;

    private final String            login;
    private final long              passwordHash;
    private final int               id;
    private final List<UserAccount> playOffers;
    private       State             state;
    private ChessGame game;

    public UserAccount(String login, String password) {
        this.login = login;
        this.passwordHash = hash(password);
        state = State.NOT_PLAYING;
        id = idDispatcher++;
        playOffers = new ArrayList<UserAccount>();
    }

    public boolean addOffer(UserAccount friend) {
        boolean connected = false;
        if (friend.getOffers().contains(this)) {
            state = State.AWAITING_GAME;
            friend.setState(State.AWAITING_GAME);

            playOffers.clear();
            friend.getOffers().clear();

            friend.getOffers().add(this);
            connected = true;
        }
        playOffers.add(friend);
        return connected;
    }

    public ChessGame getGame() {
        return game;
    }

    public int getId() {

        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<UserAccount> getOffers() {
        return playOffers;
    }

    public String getOffersString() {
        return playOffers.toString();
    }

    public int getOffersAmount() {
        return playOffers.size();
    }

    public State getState() {
        return state;
    }

    public int getStateInt() {
        return state.ordinal();
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean tryLogin(@NotNull String login, @NotNull String password) {
        return login.equalsIgnoreCase(this.login) && comparePassword(password);
    }

    private boolean comparePassword(String password) {
        return (passwordHash == hash(password));
    }

    /**
     * Default djb2 hash alghoritm
     *
     * @param str input string
     *
     * @return hash of input string
     */
    private long hash(String str) {
        long hash = 5381;
        char[] chars = SALT.concat(str).concat(login.toLowerCase(Locale.ENGLISH)).toCharArray();
        for (char c : chars) {
            hash = ((hash << 5) + hash) + c; //hash * 33 + c;
        }
        return hash;
    }

}
