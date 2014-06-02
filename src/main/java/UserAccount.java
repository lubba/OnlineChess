import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    public static        int    idDispatcher = 1;
    private final String login;
    private final long   passwordHash;
    private final int    id;
    private       State  state;
    private List<UserAccount> playOffers = new ArrayList<UserAccount>();

    public UserAccount(String login, String password) {
        this.login = login;
        this.passwordHash = hash(password);
        state = State.NOT_PLAYING;
        id = idDispatcher++;

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

    public int getStateInt() {
        return state.ordinal();
    }

    public State getState() {return state;}

    public void setState(State state) {
        this.state = state;
    }

    public int getId() {

        return id;
    }

    public String getLogin() {
        return login;
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
        char[] chars = SALT.concat(str).concat(login).toCharArray();
        for (char c : chars) {
            hash = ((hash << 5) + hash) + c; //hash * 33 + c;
        }
        return hash;
    }

    public boolean tryLogin(@NotNull String login, @NotNull String password) {
        return login.equals(this.login) && comparePassword(password);
    }

    public boolean comparePassword(String password) {
        return (passwordHash == hash(password));
    }

    public String getOffersString() {
        return playOffers.toString();
    }

    public List<UserAccount> getOffers() {
        return playOffers;
    }

    public int getOffersAmount() {return playOffers.size();}
}
