package tsc.com.relegation;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.FixtureViewHolder> {

    private List<IndividualQuestion> questionList;

    public ResultAdapter(List<IndividualQuestion> questionList) {
        this.questionList = questionList;
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

    @Override
    public void onBindViewHolder(final FixtureViewHolder fixtureViewHolder, int i) {

        final int pos = i;
        String scoreOutput;



        IndividualQuestion mIndividualQuestion = questionList.get(i);

        if (mIndividualQuestion.getScore() <= 0) {
            scoreOutput = String.valueOf(mIndividualQuestion.getScore());
        }
        else {
            scoreOutput = '+' + String.valueOf(mIndividualQuestion.getScore());
        }

        //fixtureViewHolder.mCardView.setEnabled(false);
        //fixtureViewHolder.mOption1Description.setEnabled(false);
        //fixtureViewHolder.mOption2Description.setEnabled(false);

        fixtureViewHolder.mQuestionName.setText(mIndividualQuestion.getName());
        fixtureViewHolder.mScore.setVisibility(View.VISIBLE);
        fixtureViewHolder.mScore.setText(scoreOutput);
        fixtureViewHolder.mFixtureDateTime.setText(mIndividualQuestion.getFixtureEnd().toString());
        fixtureViewHolder.mQuestionDescription.setText(mIndividualQuestion.getDescription());

        if ((mIndividualQuestion.isHasUserPicked()) && (mIndividualQuestion.isOutcome() && (mIndividualQuestion.isCorrect()))) {
            fixtureViewHolder.mQuestionName.setBackgroundColor(Color.parseColor("#ff0b6400"));
        }
        else if ((mIndividualQuestion.isHasUserPicked()) && (mIndividualQuestion.isOutcome() && (mIndividualQuestion.isCorrect() == false))) {
            fixtureViewHolder.mQuestionName.setBackgroundColor(Color.parseColor("#ff640d05"));
        }
        else {
            fixtureViewHolder.mQuestionName.setBackgroundColor(Color.parseColor("#ff646464"));
        }



        fixtureViewHolder.mOption1Description.setText(mIndividualQuestion.getOptionDescription1());
        fixtureViewHolder.mOption2Description.setText(mIndividualQuestion.getOptionDescription2());
        fixtureViewHolder.mOption1Description.setChecked(questionList.get(pos).isOptionSelected1());
        fixtureViewHolder.mOption2Description.setChecked(questionList.get(pos).isOptionSelected2());
        fixtureViewHolder.mOption1Description.setEnabled(false);
        fixtureViewHolder.mOption2Description.setEnabled(false);

//        if (questionList.get(pos).isLockedIn()) {
//            //fixtureViewHolder.mCardView.setEnabled(false);
//            fixtureViewHolder.mOption1Description.setEnabled(false);
//            fixtureViewHolder.mOption2Description.setEnabled(false);
//        }
//        else {
//            //fixtureViewHolder.mCardView.setEnabled(true);
//            fixtureViewHolder.mOption1Description.setEnabled(true);
//            fixtureViewHolder.mOption2Description.setEnabled(true);
//        }



        fixtureViewHolder.mOption1Description.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {


                if (buttonView.isPressed()) {
                    questionList.get(pos).setLocalEdit(true);

                    questionList.get(pos).setOptionSelected1(isChecked);
                    //questionList.get(pos).setOptionSelected2(false);

                    Log.w("CheckBox", "Option 1 " + pos + " " + isChecked);

                    if (questionList.get(pos).isOptionSelected2()) {
                        questionList.get(pos).setOptionSelected2(false);
                        notifyDataSetChanged();

                    }
                }

            }


        });

        fixtureViewHolder.mOption2Description.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {


                if (buttonView.isPressed()) {
                    questionList.get(pos).setLocalEdit(true);

                    questionList.get(pos).setOptionSelected2(isChecked);

                    Log.w("CheckBox", "Option 2 " + pos + " " + isChecked);

                    if (questionList.get(pos).isOptionSelected1()) {
                        questionList.get(pos).setOptionSelected1(false);
                        notifyDataSetChanged();

                    }
                }

            }
        });

        //fixtureViewHolder.mOption1Description.setChecked(questionList.get(pos).isOptionSelected1());
        //fixtureViewHolder.mOption2Description.setChecked(questionList.get(pos).isOptionSelected2());

    }

    @Override
    public FixtureViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_list_item, viewGroup, false);

        return new FixtureViewHolder(itemView);
    }

    public static class FixtureViewHolder extends RecyclerView.ViewHolder {

        protected TextView mQuestionName;
        protected TextView mQuestionDescription;
        protected CheckBox mOption1Description;
        protected CheckBox mOption2Description;
        protected CardView mCardView;
        protected TextView mScore;
        protected TextView mFixtureDateTime;

        public FixtureViewHolder(View v) {
            super(v);
            mQuestionName =  (TextView) v.findViewById(R.id.txtQuestionName);
            mQuestionDescription = (TextView)  v.findViewById(R.id.txtQuestionDescription);
            mOption1Description = (CheckBox)  v.findViewById(R.id.checkBoxAnswer1);
            mOption2Description = (CheckBox) v.findViewById(R.id.checkBoxAnswer2);
            mCardView = (CardView) v.findViewById(R.id.card_view_fixture);
            mScore =  (TextView) v.findViewById(R.id.txtScore);
            mFixtureDateTime = (TextView)  v.findViewById(R.id.txtFixtureDateTime);
        }
    }


}