package tsc.com.relegationupdated;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import tsc.com.relegationupdated.Global.Globals;
import tsc.com.relegationupdated.NetworkCalls.FixtureFunctions;


public class ResultsList extends android.support.v4.app.Fragment {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private ResultListTask mResultListTask = null;

    private ArrayList<IndividualQuestion> resultList;

    private View mFixtureRecyclerView;
    private View mProgressView;
    private View mActionButton;
    //private ActionButton actionButton;

    private boolean mSearchCheck;
    //private ArrayList<IndividualQuestion> result;

    private RecyclerView recList;

    public ResultsList newInstance(){
        ResultsList mFragment = new ResultsList();
        Bundle mBundle = new Bundle();
        //mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        // Note: getValues() is a method in your ArrayAdaptor subclass
        //String[] values = mAdapter.getValues();
        //savedState.putStringArray("myKey", values);

        savedState.putParcelableArrayList("QuestionList", resultList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentActivity faActivity  = (FragmentActivity) super.getActivity();

        View rootView = inflater.inflate(R.layout.activity_fixtures_list, container, false);

        mFixtureRecyclerView = rootView.findViewById(R.id.cardList);
        mProgressView = rootView.findViewById(R.id.fixture_list_progress);
        recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        mActionButton = rootView.findViewById(R.id.action_button);
        mActionButton.setVisibility(View.GONE);
        //actionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        //actionButton.setVisibility(View.INVISIBLE);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            resultList = savedInstanceState.getParcelableArrayList("QuestionList");
        }

        if (resultList == null) {
            if (mResultListTask == null) {
                //FixtureAdapter ca = new FixtureAdapter();
                //recList.setAdapter(ca);
                requestFixtureList();
            }
        }
        else {
            ResultAdapter ca = new ResultAdapter(resultList);

            recList.setAdapter(ca);

        }

        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            resultList = savedInstanceState.getParcelableArrayList("QuestionList");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu, menu);
//
//        //Select search item
//        final MenuItem menuItem = menu.findItem(R.id.menu_search);
//        menuItem.setVisible(true);
//
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint(this.getString(R.string.search));
//
//        ((EditText) searchView.findViewById(R.id.search_src_text))
//                .setHintTextColor(getResources().getColor(R.color.nliveo_white));
//        searchView.setOnQueryTextListener(onQuerySearchView);
//
//        menu.findItem(R.id.menu_add).setVisible(true);
//
//        mSearchCheck = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<IndividualQuestion> createList(JSONArray resultListParam) {

        //result = new ArrayList<IndividualQuestion>();

        resultList = new ArrayList<IndividualQuestion>();
        try {

            int questionCounter = 0;
            IndividualQuestion mIndividualQuestion = new IndividualQuestion();

            for (int i = 0; i < resultListParam.length(); i++) {

                questionCounter++;

                JSONObject fixture = resultListParam.getJSONObject(i);

                mIndividualQuestion = new IndividualQuestion();

                if (!fixture.isNull("questionID"))
                    mIndividualQuestion.setItemID(fixture.getInt("questionID"));
                mIndividualQuestion.setName("Result " + questionCounter);
                if (!fixture.isNull("Description"))
                    mIndividualQuestion.setDescription(fixture.getString("Description"));
                if (!fixture.isNull("created_by_user_id"))
                    mIndividualQuestion.setUserCreatedID(Integer.parseInt(fixture.getString("created_by_user_id")));
                if (!fixture.isNull("CorrectAnswerID"))
                    mIndividualQuestion.setCorrectAnswerID(Integer.parseInt(fixture.getString("CorrectAnswerID")));
                if (!fixture.isNull("userAnswerID"))
                    mIndividualQuestion.setUserAnswerID(Integer.parseInt(fixture.getString("userAnswerID")));
                if (!fixture.isNull("fixture_date_start"))
                    mIndividualQuestion.setFixtureStart(Timestamp.valueOf(fixture.getString("fixture_date_start")));
                else
                    mIndividualQuestion.setFixtureStart(Timestamp.valueOf("0000-00-00 00:00:00.00"));
                if (!fixture.isNull("fixture_date_end"))
                    mIndividualQuestion.setFixtureEnd(Timestamp.valueOf(fixture.getString("fixture_date_end")));
                else
                    mIndividualQuestion.setFixtureEnd(Timestamp.valueOf("0000-00-00 00:00:00.00"));
                if (!fixture.isNull("category"))
                    mIndividualQuestion.setCategory(fixture.getString("category"));
                if (!fixture.isNull("AnswerID1"))
                    mIndividualQuestion.setOptionID1(Integer.parseInt(fixture.getString("AnswerID1")));
                if (!fixture.isNull("AnswerDescription1"))
                    mIndividualQuestion.setOptionDescription1(fixture.getString("AnswerDescription1"));
                if (!fixture.isNull("AnswerID2"))
                    mIndividualQuestion.setOptionID2(Integer.parseInt(fixture.getString("AnswerID2")));
                if (!fixture.isNull("AnswerDescription2"))
                    mIndividualQuestion.setOptionDescription2(fixture.getString("AnswerDescription2"));

                if ((!fixture.isNull("AnswerID1")) && (!fixture.isNull("UserAnswerID"))) {
                    if (Integer.parseInt(fixture.getString("UserAnswerID")) == Integer.parseInt(fixture.getString("AnswerID1"))) {
                        mIndividualQuestion.setOptionSelected1(true);
                    }
                }
                if ((!fixture.isNull("AnswerID2")) && (!fixture.isNull("UserAnswerID"))) {
                    if (Integer.parseInt(fixture.getString("UserAnswerID")) == Integer.parseInt(fixture.getString("AnswerID2"))) {
                        mIndividualQuestion.setOptionSelected2(true);
                    }
                }
                if (!fixture.isNull("UserPicked")) {
                    if (fixture.getString("UserPicked").equalsIgnoreCase("Not Picked")) {
                        mIndividualQuestion.setHasUserPicked(false);
                    }
                    else if (fixture.getString("UserPicked").equalsIgnoreCase("Picked")) {
                        mIndividualQuestion.setHasUserPicked(true);
                        if (mIndividualQuestion.getUserAnswerID() == mIndividualQuestion.getOptionID1()) {
                            mIndividualQuestion.setOptionSelected1(true);
                            mIndividualQuestion.setOptionSelected2(false);
                        }
                        else if (mIndividualQuestion.getUserAnswerID() == mIndividualQuestion.getOptionID2()) {
                            mIndividualQuestion.setOptionSelected1(false);
                            mIndividualQuestion.setOptionSelected2(true);
                        }

                    }
                }

                if (!fixture.isNull("Outcome")) {
                    if (fixture.getString("Outcome").equalsIgnoreCase("Push")) {
                        mIndividualQuestion.setOutcome(false);
                    }
                    else if (fixture.getString("Outcome").equalsIgnoreCase("Outcome")) {
                        mIndividualQuestion.setOutcome(true);
                    }
                }

                if (!fixture.isNull("CorrectAnswer")) {
                    if (fixture.getString("CorrectAnswer").equalsIgnoreCase("Incorrect")) {
                        mIndividualQuestion.setIsCorrect(false);
                    }
                    else if (fixture.getString("CorrectAnswer").equalsIgnoreCase("Correct")) {
                        mIndividualQuestion.setIsCorrect(true);
                    }
                }

                if (!fixture.isNull("LockedIn")) {
                    if (fixture.getString("LockedIn").equalsIgnoreCase("Open")) {
                        mIndividualQuestion.setIsLockedIn(false);
                    }
                    else if (fixture.getString("LockedIn").equalsIgnoreCase("Locked")) {
                        mIndividualQuestion.setIsLockedIn(true);
                    }
                }

                if (!fixture.isNull("score"))
                    mIndividualQuestion.setScore(fixture.getDouble("score"));

                resultList.add(mIndividualQuestion);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static boolean stringToBool(String s) {
        if (s.equals("1"))
            return true;
        if (s.equals("0"))
            return false;
        throw new IllegalArgumentException(s+" is not a bool. Only 1 and 0 are.");
    }

    private void requestFixtureList() {

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.

        Globals g = (Globals) getActivity().getApplication();

        resultList = new ArrayList<IndividualQuestion>();
        showProgress(true);
        //mResultListTask = new FixtureListTask(1,1,2);
        mResultListTask = new ResultListTask(g.getUserID(),g.getGroupID(),g.getGameID());
        mResultListTask.execute((Void) null);

    }

    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (mSearchCheck){
                // implement your search here
            }
            return false;
        }
    };

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {



        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFixtureRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFixtureRecyclerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFixtureRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFixtureRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ResultListTask extends AsyncTask<Void, Void, Boolean> {

        private final int mUserID;
        private final int mGroupID;
        private final int mGameID;

        // contacts JSONArray
        JSONArray fixtures = null;

        FixtureFunctions fixtureFunction = new FixtureFunctions();
        JSONObject json;



        ResultListTask(int userID, int groupID, int gameID) {
            mUserID = userID;
            mGroupID = groupID;
            mGameID = gameID;

        }




        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.



            json = fixtureFunction.getFixtureListForGroup(mUserID,mGroupID,mGameID);

            Log.i("JSON", String.valueOf(json.length()));

            try {


                fixtures = json.getJSONArray("fixtures");


                Log.i("JSON",String.valueOf(fixtures.length()));

                createList(fixtures);



            } catch (JSONException e) {
                e.printStackTrace();
            }


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                //finish();

                ResultAdapter ca = new ResultAdapter(resultList);
                recList.setAdapter(ca);
                Log.e("Login","Logged In True");
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                Log.e("Login","Logged In Error");
            }
        }

        @Override
        protected void onCancelled() {
            mResultListTask = null;
            showProgress(false);
        }
    }


}
