package chess;

/**
 * User: Alpen Ditrix
 * Date: 29.05.2014
 * Time: 11:42
 */
public class TurnInfo {
    public String getLog() {
        return log;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public String getError() {
        return error;
    }

    public TurnInfo(String log, boolean isCheck, String error) {

        this.log = log;
        this.isCheck = isCheck;
        this.error = error;
    }

    private String  log;
    private boolean isCheck;
    private String  error;


}
