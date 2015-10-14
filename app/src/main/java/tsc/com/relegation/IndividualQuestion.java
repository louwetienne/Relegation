package tsc.com.relegation;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//another class to handle item's id and name
public class IndividualQuestion implements Parcelable {

    private int itemID;
    private String name;
    private String description;
    private String optionDescription1;
    private int optionID1;
    private boolean optionSelected1;
    private String optionDescription2;
    private int optionID2;
    private boolean optionSelected2;
    private int userCreatedID;
    private int userAnswerID;
    private int correctAnswerID;
    private String category;
    private Timestamp fixtureStart;
    private Timestamp fixtureEnd;
    private boolean hasUserPicked;
    private boolean outcome;
    private boolean isCorrect;
    private boolean isLockedIn;
    private boolean LocalEdit;
    private double score;

    // constructor
    public IndividualQuestion(int itemID, String name, String description, String optionDescription1, int optionID1, boolean optionSelected1, String optionDescription2, int optionID2, boolean optionSelected2, int userAnswerID, int correctAnswerID) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.optionDescription1 = optionDescription1;
        this.optionID1 = optionID1;
        this.optionSelected1 = optionSelected1;
        this.optionDescription2 = optionDescription2;
        this.optionID2 = optionID2;
        this.optionSelected2 = optionSelected2;
        this.userAnswerID = userAnswerID;
        this.correctAnswerID = correctAnswerID;
    }

    // constructor
    public IndividualQuestion() {
    }


    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAnswer() {
        return getUserAnswerID();
    }

    public void setAnswer(int answer) {
        this.setUserAnswerID(answer);
    }

    public int getUserAnswerID() {
        return userAnswerID;
    }

    public void setUserAnswerID(int userAnswerID) {
        this.userAnswerID = userAnswerID;
    }

    public String getOptionDescription1() {
        return optionDescription1;
    }

    public void setOptionDescription1(String optionDescription1) {
        this.optionDescription1 = optionDescription1;
    }

    public int getOptionID1() {
        return optionID1;
    }

    public void setOptionID1(int optionID1) {
        this.optionID1 = optionID1;
    }

    public String getOptionDescription2() {
        return optionDescription2;
    }

    public void setOptionDescription2(String optionDescription2) {
        this.optionDescription2 = optionDescription2;
    }

    public int getOptionID2() {
        return optionID2;
    }

    public void setOptionID2(int optionID2) {
        this.optionID2 = optionID2;
    }

    public boolean isOptionSelected1() {
        return optionSelected1;
    }

    public void setOptionSelected1(boolean optionSelected1) {
        this.optionSelected1 = optionSelected1;
    }

    public boolean isOptionSelected2() {
        return optionSelected2;
    }

    public void setOptionSelected2(boolean optionSelected2) {
        this.optionSelected2 = optionSelected2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("Parcel Question", "writeToParcel..." + flags);

        dest.writeInt(itemID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(optionDescription1);
        dest.writeInt(optionID1);
        dest.writeByte((byte) (optionSelected1 ? 1 : 0));
        dest.writeString(optionDescription2);
        dest.writeInt(optionID2);
        dest.writeByte((byte) (optionSelected2 ? 1 : 0));
        dest.writeInt(userAnswerID);


        dest.writeInt (userCreatedID);
        dest.writeInt (userAnswerID);
        dest.writeInt(correctAnswerID);
        dest.writeString(category);
        //dest.writeSerializable(fixtureStart);
        String fixtureStartString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fixtureStart);
        dest.writeString(fixtureStartString);
        //dest.writeSerializable(fixtureEnd);
        String fixtureEndString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fixtureEnd);
        dest.writeString(fixtureEndString);
        dest.writeByte((byte) (hasUserPicked ? 1 : 0));
        dest.writeByte((byte) (outcome ? 1 : 0));
        dest.writeByte((byte) (isCorrect ? 1 : 0));
        dest.writeByte((byte) (isLockedIn ? 1 : 0));
        dest.writeByte((byte) (LocalEdit ? 1 : 0));
        dest.writeDouble(score);

    }

    public void readFromParcel(Parcel in) {
        Log.v("Parcel Question", "ReadInParcel...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date parsedTimeStamp = null;

        itemID = in.readInt();
        name = in.readString();
        description = in.readString();
        optionDescription1 = in.readString();
        optionID1 = in.readInt();
        optionSelected1 = in.readByte() != 0;
        optionDescription2 = in.readString();
        optionID2 = in.readInt();
        optionSelected2 = in.readByte() != 0;
        userAnswerID = in.readInt();


        userCreatedID = in.readInt();
        userAnswerID = in.readInt();
        correctAnswerID = in.readInt();
        category = in.readString();
        //fixtureStart = (Timestamp) in.readSerializable();
        //fixtureEnd = (Timestamp) in.readSerializable();
        String fixtureStartString = in.readString();
        String fixtureEndString = in.readString();

        try {
            parsedTimeStamp = dateFormat.parse(fixtureStartString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fixtureStart = new java.sql.Timestamp(parsedTimeStamp.getTime());

        try {
            parsedTimeStamp = dateFormat.parse(fixtureEndString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fixtureEnd = new java.sql.Timestamp(parsedTimeStamp.getTime());

        hasUserPicked = in.readByte() != 0;
        outcome = in.readByte() != 0;
        isCorrect = in.readByte() != 0;
        isLockedIn = in.readByte() != 0;
        LocalEdit = in.readByte() != 0;
        score = in.readDouble();

    }

    public int getCorrectAnswerID() {
        return correctAnswerID;
    }

    public void setCorrectAnswerID(int correctAnswerID) {
        this.correctAnswerID = correctAnswerID;
    }

    public int getUserCreatedID() {
        return userCreatedID;
    }

    public void setUserCreatedID(int userCreatedID) {
        this.userCreatedID = userCreatedID;
    }

    public Timestamp getFixtureStart() {
        return fixtureStart;
    }

    public void setFixtureStart(Timestamp fixtureStart) {
        this.fixtureStart = fixtureStart;
    }

    public Timestamp getFixtureEnd() {
        return fixtureEnd;
    }

    public void setFixtureEnd(Timestamp fixtureEnd) {
        this.fixtureEnd = fixtureEnd;
    }

    public boolean isHasUserPicked() {
        return hasUserPicked;
    }

    public void setHasUserPicked(boolean hasUserPicked) {
        this.hasUserPicked = hasUserPicked;
    }

    public boolean isOutcome() {
        return outcome;
    }

    public void setOutcome(boolean outcome) {
        this.outcome = outcome;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isLockedIn() {
        return isLockedIn;
    }

    public void setIsLockedIn(boolean isLockedIn) {
        this.isLockedIn = isLockedIn;
    }

    public boolean isLocalEdit() {
        return LocalEdit;
    }

    public void setLocalEdit(boolean localEdit) {
        this.LocalEdit = localEdit;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
