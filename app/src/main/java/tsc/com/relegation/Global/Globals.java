package tsc.com.relegation.Global;

import android.app.Application;

/**
 * Created by Etienne on 2015-07-03.
 */
public class Globals extends Application {
    private int userID;
    private int gameID;
    private int groupID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}
