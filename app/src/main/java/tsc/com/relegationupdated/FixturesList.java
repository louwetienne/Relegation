package tsc.com.relegationupdated;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.software.shell.fab.ActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import tsc.com.relegationupdated.Global.Globals;
import tsc.com.relegationupdated.NetworkCalls.FixtureFunctions;


public class FixturesList extends android.support.v4.app.Fragment {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private static String KEY_SUCCESS = "success";

    private FixtureListTask mFixtureListTask = null;
    private FixtureListSaveTask mFixtureListSaveTask = null;

    private ArrayList<IndividualQuestion> fixtureList;

    private View mFixtureRecyclerView;
    private View mProgressView;
    private ActionButton actionButton;

    private boolean mSearchCheck;
    //private ArrayList<IndividualQuestion> result;

    private RecyclerView recList;

    public FixturesList newInstance(){
        FixturesList mFragment = new FixturesList();
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

        savedState.putParcelableArrayList("QuestionList", fixtureList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        FragmentActivity faActivity  = (FragmentActivity) super.getActivity();

        View rootView = inflater.inflate(R.layout.activity_fixtures_list, container, false);

        mFixtureRecyclerView = rootView.findViewById(R.id.cardList);
        mProgressView = rootView.findViewById(R.id.fixture_list_progress);
        recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        actionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        actionButton.setVisibility(View.INVISIBLE);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            fixtureList = savedInstanceState.getParcelableArrayList("QuestionList");
        }

        if (fixtureList == null) {
            if (mFixtureListTask == null) {
                //FixtureAdapter ca = new FixtureAdapter();
                //recList.setAdapter(ca);
                requestFixtureList();
            }
        }
        else {
            FixtureAdapter ca = new FixtureAdapter(fixtureList);

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
            fixtureList = savedInstanceState.getParcelableArrayList("QuestionList");
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

    private List<IndividualQuestion> createList(JSONArray fixtureListParam) {

        //result = new ArrayList<IndividualQuestion>();

        fixtureList = new ArrayList<IndividualQuestion>();
        try {

            int questionCounter = 0;
            IndividualQuestion mIndividualQuestion = new IndividualQuestion();

            for (int i = 0; i < fixtureListParam.length(); i++) {

                questionCounter++;

                JSONObject fixture = fixtureListParam.getJSONObject(i);

                mIndividualQuestion = new IndividualQuestion();

                if (!fixture.isNull("QuestionID"))
                    mIndividualQuestion.setItemID(fixture.getInt("QuestionID"));
                mIndividualQuestion.setName("Question " + questionCounter);
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

                fixtureList.add(mIndividualQuestion);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fixtureList;
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

        fixtureList = new ArrayList<IndividualQuestion>();
        showProgress(true);
        //mFixtureListTask = new FixtureListTask(1,1,2);
        mFixtureListTask = new FixtureListTask(g.getUserID(),g.getGroupID(),g.getGameID());
        mFixtureListTask.execute((Void) null);

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
    public class FixtureListTask extends AsyncTask<Void, Void, Boolean> {

        private final int mUserID;
        private final int mGroupID;
        private final int mGameID;

        // contacts JSONArray
        JSONArray fixtures = null;

        FixtureFunctions fixtureFunction = new FixtureFunctions();
        JSONObject json;



        FixtureListTask(int userID, int groupID, int gameID) {
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
            mFixtureListSaveTask = null;
            showProgress(false);

            if (success) {
                //finish();

                actionButton.setVisibility(View.VISIBLE);

                actionButton.setImageResource(R.drawable.ic_done_white);
                actionButton.setButtonColor(getResources().getColor(R.color.fab_material_blue_500));
                actionButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click
                        new MaterialDialog.Builder(getActivity())

                                .title("Confirm Question Answers?")
                                .content("Would you like to Save these answers to make changes at a later stage, or would you like to Commit all changes now?")
                                .positiveText("Final Commit")
                                .negativeText("Save For Later")

                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        //Final Commit
                                        //just make sure the locked in is set to true.
                                        //call the FixtureListSaveTask class to execute scripts

                                        showProgress(true);
                                        mFixtureListSaveTask = new FixtureListSaveTask(true);
                                        //mFixtureListSaveTask.isFinalCommit = true;
                                        mFixtureListSaveTask.execute((Void) null);

                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        //Save answers for later, just make sure the locked in is set to false.
                                        //call the FixtureListSaveTask class to execute scripts

                                        showProgress(true);
                                        mFixtureListSaveTask = new FixtureListSaveTask(false);
                                        //mFixtureListSaveTask.isFinalCommit = false;
                                        mFixtureListSaveTask.execute((Void) null);
                                    }
                                })

                                .show();

                                }
                    });


                FixtureAdapter ca = new FixtureAdapter(fixtureList);
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
            mFixtureListTask = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class FixtureListSaveTask extends AsyncTask<Void, Void, Boolean> {


        private final ArrayList<IndividualQuestion> mFixtureList;
        private ArrayList<IndividualQuestion> fixtureDeleteList = new ArrayList<IndividualQuestion>();
        private ArrayList<IndividualQuestion> fixtureInsertUpdateList = new ArrayList<IndividualQuestion>();
        protected boolean isFinalCommit;

        FixtureFunctions fixtureFunction = new FixtureFunctions();
        JSONObject json;

        FixtureListSaveTask(boolean mFinalCommit) {

            isFinalCommit = mFinalCommit;

            mFixtureList = fixtureList;

            for (int i = 0; i < fixtureList.size(); i++) {
                if (fixtureList.get(i).isLockedIn() == false) {


                    if (isFinalCommit) {
                        if (fixtureList.get(i).isOptionSelected1() == false && fixtureList.get(i).isOptionSelected2() == false) {
                            //This question has been removed and needs to be deleted from the database
                            fixtureList.get(i).setUserAnswerID(0);
                            fixtureInsertUpdateList.add(fixtureList.get(i));

                        }
                        else {
                            //This is a update, need to add this question to update list
                            // The insert and update query can be done in one query.
                            fixtureInsertUpdateList.add(fixtureList.get(i));
                        }

                    }
                    else {


                        if (fixtureList.get(i).isLocalEdit()) {
                            if (fixtureList.get(i).isHasUserPicked()) {
                                if (fixtureList.get(i).isOptionSelected1() == false && fixtureList.get(i).isOptionSelected2() == false) {
                                    //This question has been removed and needs to be deleted from the database
                                    fixtureDeleteList.add(fixtureList.get(i));

                                } else {
                                    //This is a update, need to add this question to update list
                                    // The insert and update query can be done in one query.
                                    fixtureInsertUpdateList.add(fixtureList.get(i));
                                }
                            } else {
                                //This is an insert.
                                fixtureInsertUpdateList.add(fixtureList.get(i));
                            }
                        }

                    }

                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (fixtureInsertUpdateList.size() > 0) {

                Globals g = (Globals) getActivity().getApplication();

                json = fixtureFunction.insertUpdateUserAnswers(fixtureInsertUpdateList,g.getUserID(),isFinalCommit);

                // check for login response
                try {
                    Log.i("JSON", String.valueOf(json.length()));
                    if (json.getString(KEY_SUCCESS) != null) {
                        //loginErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if(res.equals("true")){

                            Toast.makeText(getActivity().getApplicationContext(), "Fixtures Saved Correctly", Toast.LENGTH_LONG).show();
                            Log.i("InsertUpdate", "Insert and Update completed Successfully");

                            return true;
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Error In Saving Fixtures", Toast.LENGTH_LONG).show();
                            Log.i("InsertUpdate","Error Insert and Update completed");

                            return false;
                            // Error in login
                            //loginErrorMsg.setText("Incorrect username/password");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                    return false;
                }

            }




            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success){
            mFixtureListSaveTask = null;
            showProgress(false);
            Log.i("FixtureListSaveTask", "Post Execute");
            requestFixtureList();


        }

        @Override
        protected void onCancelled() {
            mFixtureListSaveTask = null;
            showProgress(false);
            Log.i("FixtureListSaveTask", "Cancelled");
        }
    }


}
