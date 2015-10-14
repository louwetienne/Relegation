package tsc.com.relegation.DatabaseObjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Etienne on 2015-05-26.
 */
public class FormGuide implements Parcelable {

    private Question currentQuestion;
    private String correctAnswerDescription;


    //Constructor
    public FormGuide() {
        currentQuestion = new Question();
        correctAnswerDescription = "";
    }

    //Constructor
    public FormGuide(Question currentQuestion, String correctAnswerDescription) {
        this.currentQuestion = currentQuestion;
        this.correctAnswerDescription = correctAnswerDescription;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public String getCorrectAnswerDescription() {
        return correctAnswerDescription;
    }

    public void setCorrectAnswerDescription(String correctAnswerDescription) {
        this.correctAnswerDescription = correctAnswerDescription;
    }
}
