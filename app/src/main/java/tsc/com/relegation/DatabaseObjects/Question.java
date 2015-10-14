package tsc.com.relegation.DatabaseObjects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Date;

/**
 * Created by Etienne on 2015-04-15.
 */
public class Question implements Parcelable {

    private int questionID;
    private String questionDescription;
    private int correctAnswerID;
    private String correctAnswerDescription;
    private boolean isCompleted;
    private Date date;
    private String outcome;
    private int answerID1;
    private String answerDescription1;
    private int answerID2;
    private String answerDescription2;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("Parcel Question", "writeToParcel..." + flags);

        dest.writeInt(questionID);
        dest.writeString(questionDescription);
        dest.writeInt(correctAnswerID);
        dest.writeString(correctAnswerDescription);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
        dest.writeSerializable(date);
        dest.writeString(outcome);
        dest.writeInt(answerID1);
        dest.writeString(answerDescription1);
        dest.writeInt(answerID2);
        dest.writeString(answerDescription2);

    }

    public void readFromParcel(Parcel in) {
        Log.v("Parcel Question", "ReadInParcel...");

        questionID = in.readInt();
        questionDescription = in.readString();
        correctAnswerID = in.readInt();
        correctAnswerDescription = in.readString();
        isCompleted = in.readByte() != 0;
        date = (java.util.Date) in.readSerializable();
        outcome = in.readString();
        answerID1 = in.readInt();
        answerDescription1 = in.readString();
        answerID2 = in.readInt();
        answerDescription2 = in.readString();

    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public int getCorrectAnswerID() {
        return correctAnswerID;
    }

    public void setCorrectAnswerID(int correctAnswerID) {
        this.correctAnswerID = correctAnswerID;
    }

    public String getCorrectAnswerDescription() {
        return correctAnswerDescription;
    }

    public void setCorrectAnswerDescription(String correctAnswerDescription) {
        this.correctAnswerDescription = correctAnswerDescription;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public int getAnswerID1() {
        return answerID1;
    }

    public void setAnswerID1(int answerID1) {
        this.answerID1 = answerID1;
    }

    public String getAnswerDescription1() {
        return answerDescription1;
    }

    public void setAnswerDescription1(String answerDescription1) {
        this.answerDescription1 = answerDescription1;
    }

    public int getAnswerID2() {
        return answerID2;
    }

    public void setAnswerID2(int answerID2) {
        this.answerID2 = answerID2;
    }

    public String getAnswerDescription2() {
        return answerDescription2;
    }

    public void setAnswerDescription2(String answerDescription2) {
        this.answerDescription2 = answerDescription2;
    }
}
