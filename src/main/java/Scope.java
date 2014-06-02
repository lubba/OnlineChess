import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Alpen Ditrix
 * Date: 29.05.2014
 * Time: 11:25
 */
public class Scope {

    public static final    String a_ACCOUNTS   = "accounts";

    public static List<UserAccount> getUserAccounts(ServletContext context) {
        List<UserAccount> list = (List<UserAccount>) context.getAttribute(a_ACCOUNTS);
        if (list == null) {
            list = new ArrayList<UserAccount>(){{
                add(new UserAccount("admin", "iddqd"));
                add(new UserAccount("Alpen Ditrix", "qwedas"));
            }};

            context.setAttribute(a_ACCOUNTS, list);
        }
        return list;
    }
}
