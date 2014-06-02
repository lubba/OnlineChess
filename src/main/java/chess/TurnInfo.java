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

    public TurnInfo(String log, boolean isCheck) {
        this.log = log;
        this.isCheck = isCheck;
    }

    private String  log;
    private boolean isCheck;

}
