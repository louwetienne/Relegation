package tsc.com.relegationupdated.DatabaseObjects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

//another class to handle item's id and userName
public class LeaderboardUser implements Parcelable {

    private int userID;
    private String userName;
    private String initials;
    private double score;
    private int leaderboardPosition;

    // constructor
    public LeaderboardUser(int userID, String userName, String initials, double score) {
        this.userID = userID;
        this.userName = userName;
        this.initials = initials;
        this.score = score;
    }

    // constructor
    public LeaderboardUser() {
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getLeaderboardPosition() {
        return leaderboardPosition;
    }

    public void setLeaderboardPosition(int leaderboardPosition) {
        this.leaderboardPosition = leaderboardPosition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("Parcel Leaderboard User", "writeToParcel..." + flags);

        dest.writeInt(userID);
        dest.writeString(userName);
        dest.writeString(initials);
        dest.writeDouble(score);
        dest.writeInt(leaderboardPosition);

    }

    public void readFromParcel(Parcel in) {
        Log.v("Parcel Leaderboard User", "ReadInParcel...");

        userID = in.readInt();
        userName = in.readString();
        initials = in.readString();
        score = in.readDouble();
        leaderboardPosition = in.readInt();
    }


}
