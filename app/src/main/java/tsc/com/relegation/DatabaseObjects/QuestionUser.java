package tsc.com.relegation.DatabaseObjects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

//another class to handle item's id and userName
public class QuestionUser implements Parcelable {

    private int userID;
    private String userName;
    private String initials;
    private double score;
    private int answerID;
    private String answerDescription;

    // constructor
    public QuestionUser(int userID, String userName, String initials, double score, int answerID, String answerDescription) {
        this.userID = userID;
        this.userName = userName;
        this.initials = initials;
        this.score = score;
        this.answerID = answerID;
        this.answerDescription = answerDescription;
    }

    // constructor
    public QuestionUser() {
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

    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("Parcel Question User", "writeToParcel..." + flags);

        dest.writeInt(userID);
        dest.writeString(userName);
        dest.writeString(initials);
        dest.writeDouble(score);
        dest.writeInt(answerID);
        dest.writeString(answerDescription);

    }

    public void readFromParcel(Parcel in) {
        Log.v("Parcel Question User", "ReadInParcel...");

        userID = in.readInt();
        userName = in.readString();
        initials = in.readString();
        score = in.readDouble();
        answerID = in.readInt();
        answerDescription = in.readString();
    }


    public String getAnswerDescription() {
        return answerDescription;
    }

    public void setAnswerDescription(String answerDescription) {
        this.answerDescription = answerDescription;
    }
}
