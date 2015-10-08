/*
 * Copyright 2015 Rudson Lima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tsc.com.relegationupdated;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

//import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.Model.HelpLiveo;
import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import tsc.com.relegationupdated.DatabaseObjects.LeaderboardUser;
import tsc.com.relegationupdated.Global.Globals;
import tsc.com.relegationupdated.NetworkCalls.UserFunctions;

//public class MainActivity extends NavigationLiveo implements NavigationLiveoListener {
public class MainActivity extends NavigationLiveo implements OnItemClickListener {

    private static final String KEY_GROUPID = "groupid";
    private static final String KEY_GAMEID = "gameid";

    private GetGameGroupTask mAuthTask = null;

    public List<String> mListNameItem;
    UserFunctions userFunctions;
    private HelpLiveo mHelpLiveo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Dashboard Screen for the application
         * */
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){

            //TODO
            //Check if groupID is empty or null from local database, if it is, need to pull new one
            //using the new activeGroupGamesByUserID from UserFunctions, this will pull group and game id.
            //If group is not null and has a value in then I can use the function activeGroupGamesByUserID
            //with the user id and group id.

            //This is here as a temp, it will always pull from the live DB and get the latest games.

            mAuthTask = new GetGameGroupTask();
            mAuthTask.execute((Void) null);

//            try {
//                mAuthTask.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }

            if (userFunctions.isGameActive(getApplicationContext())) {

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                Globals g = (Globals) getApplication();

                // Get the current active game from the SQLlite database with groupID and gameID

                //TODO resolve the local DB to save game so it is not doing network calls all the time.

//                HashMap<String,Integer> game = new HashMap<String,Integer>();
//
//                game = db.getActiveGroupGame();
//
//                // If there are values for the groupID and gameID add it to the global
//                if ((game != null) && (game.size() > 0)) {
//                    if ((game.get(KEY_GROUPID) != null) && (game.get(KEY_GAMEID) != null)) {
//
//                        g.setGroupID(game.get(KEY_GROUPID));
//                        g.setGameID(game.get(KEY_GAMEID));
//                    }
//                }
//                // Otherwise if fields are null with no entries in the SQLlite database, fetch from the live database
//                else {
//                    //set this in dashboard, check if these are empty and retrieve them from live database
//                    //g.setGroupID(1);
//                    //g.setGameID(1);
//
//                    mAuthTask = new GetGameGroupTask();
//                    mAuthTask.execute((Void) null);
//                }
            }
            else {
                mAuthTask = new GetGameGroupTask();
                mAuthTask.execute((Void) null);
            }

        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            //finish();
        }
    }

    @Override
    public void onInt(Bundle savedInstanceState) {

        // User Information
        this.userName.setText("Jou Ma Se Picks");
        this.userEmail.setText("rudsonlive@gmail.com");
        this.userPhoto.setImageResource(R.drawable.siphiwe_tshabalala);
        this.userBackground.setImageResource(R.drawable.ic_user_background_first);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        // Item separator
        //mHelpLiveo.addSeparator();
        mHelpLiveo.add("Dashboard", R.drawable.ic_inbox_black_24dp);
        mHelpLiveo.add("Smack Talk", R.drawable.ic_send_black_24dp);
        mHelpLiveo.addSubHeader("+ Leaderboards"); //Item subHeader
        mHelpLiveo.add("Player Vs Player", R.drawable.ic_inbox_black_24dp);
        mHelpLiveo.add("Detailed", R.drawable.ic_inbox_black_24dp);
        mHelpLiveo.add("History", R.drawable.ic_inbox_black_24dp);
        mHelpLiveo.addSubHeader("+ Fixtures And Results"); //Item subHeader
        mHelpLiveo.add("Fixtures", R.drawable.ic_inbox_black_24dp);
        mHelpLiveo.add("Resuilts", R.drawable.ic_inbox_black_24dp);
        mHelpLiveo.addSubHeader("+ Admin"); //Item subHeader
        mHelpLiveo.add("Submit Fixtures", R.drawable.ic_inbox_black_24dp);
        mHelpLiveo.add("Submit Results", R.drawable.ic_inbox_black_24dp);
        mHelpLiveo.addSeparator();
        mHelpLiveo.add("Logout", R.drawable.ic_inbox_black_24dp);

        with(this).startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                .footerItem(R.string.settings, R.drawable.ic_delete_black_24dp)
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .build();

    }

    @Override
    //public void onItemClickNavigation(int position, int layoutContainerId) {
    public void onItemClick(int position) {

        switch (position) {

            case 13:
                Globals g = (Globals) getApplication();

                userFunctions.logoutUser(getApplicationContext());
                g.setUserID(0);
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                // Closing dashboard screen
                finish();
                break;
        }

        if (position == 0) {

            //FragmentManager mFragmentManager = getSupportFragmentManager();

            //Fragment mDashboardFragment = new Dashboard().newInstance(mListNameItem.get(position));

            FragmentManager mFragmentManager = getSupportFragmentManager();
            setTitle("Relegation");


//            for(int i = 0; i < mFragmentManager.getBackStackEntryCount(); ++i) {
//                mFragmentManager.popBackStack();
//            }


            Fragment mDashboardFragment = new Dashboard().newInstance(mHelpLiveo.get(position).getName());

            if (mDashboardFragment != null){
                mFragmentManager.beginTransaction().replace(R.id.container, mDashboardFragment).commit();
            }

        }
        else if (position == 7) {

            setTitle("Fixtures");

            FragmentManager mFragmentManager = getSupportFragmentManager();

            Fragment mFixtureList = new FixturesList().newInstance();

            String backStateName = mFixtureList.getClass().getName();

            if (mFixtureList != null){

                //boolean fragmentPopped = mFragmentManager.popBackStackImmediate(backStateName, 0);

                if (mFragmentManager.getBackStackEntryCount() == 0) {
                    FragmentTransaction ft = mFragmentManager.beginTransaction();

                    ft.replace(R.id.container, mFixtureList);
                    //ft.replace(((ViewGroup) (getView().getParent())).getId());
                    //ft.addToBackStack(backStateName);
                    ft.commit();
                }
                else {
                    for(int i = 0; i < mFragmentManager.getBackStackEntryCount(); ++i) {
                        mFragmentManager.popBackStack();
                    }
                    FragmentTransaction ft = mFragmentManager.beginTransaction();

                    ft.replace(R.id.container, mFixtureList);
                    //ft.addToBackStack(backStateName);
                    ft.commit();
                }

                //mFragmentManager.beginTransaction().replace(R.id.container, mFixtureList).commit();

            }
        }
        else if (position == 8) {

            setTitle("Results");

            FragmentManager mFragmentManager = getSupportFragmentManager();

            Fragment mResultList = new ResultsList().newInstance();

            String backStateName = mResultList.getClass().getName();

            if (mResultList != null){
                if (mFragmentManager.getBackStackEntryCount() == 0) {
                    FragmentTransaction ft = mFragmentManager.beginTransaction();

                    ft.replace(R.id.container, mResultList);
                    //ft.addToBackStack(backStateName);
                    ft.commit();
                }
                else {
                    for(int i = 0; i < mFragmentManager.getBackStackEntryCount(); ++i) {
                        mFragmentManager.popBackStack();
                    }
                    FragmentTransaction ft = mFragmentManager.beginTransaction();

                    ft.replace(R.id.container, mResultList);
                    //ft.addToBackStack(backStateName);
                    ft.commit();
                }
            }
        }
        else {

            //FragmentManager mFragmentManager = getSupportFragmentManager();

            //Fragment mFragment = new FragmentMain().newInstance(mListNameItem.get(position));

            FragmentManager mFragmentManager = getSupportFragmentManager();
            Fragment mFragment = new FragmentMain().newInstance(mHelpLiveo.get(position).getName());

            if (mFragment != null){
                mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
            }

        }
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {

            //hide the menu when the navigation is opens
//            switch (position) {
//                case 0:
//                    menu.findItem(R.id.menu_add).setVisible(!visible);
//                    menu.findItem(R.id.menu_search).setVisible(!visible);
//                    break;
//
//                case 1:
//                    menu.findItem(R.id.menu_add).setVisible(!visible);
//                    menu.findItem(R.id.menu_search).setVisible(!visible);
//                    break;
//            }
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //startActivity(new Intent(this, SettingsActivity.class));
            closeDrawer();
        }
    };




    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class GetGameGroupTask extends AsyncTask<Void, Void, Boolean> {

        //private final String mEmail;
        //private final String mPassword;

        UserFunctions userFunction = new UserFunctions();
        JSONObject json;



        GetGameGroupTask() {

        }




        @Override
        protected Boolean doInBackground(Void... params) {

            JSONArray groups = null;
            // TODO: attempt authentication against a network service.

            Globals g = (Globals) getApplication();

            //json = userFunction.loginUser(mEmail, mPassword);
            json = userFunction.activeGroupGamesByUserID(Integer.toString(g.getUserID()));

            Log.i("JSON", String.valueOf(json));

            // check for login response
            try {

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                groups = json.getJSONArray("GroupGames");

                for (int i = 0; i < groups.length(); i++) {


                    JSONObject group = groups.getJSONObject(i);

                    //mIndividualLeaderboardUser = new LeaderboardUser();

                    if (i == 0) {
                        if ((!group.isNull("groupid")) && (!group.isNull("gamesid"))) {
                            db.addGroup(group.getInt("groupid"),group.getInt("gamesid"),1);

                            if (!group.isNull("groupid"))
                                g.setGroupID(group.getInt("groupid"));
                            if (!group.isNull("gamesid"))
                                g.setGameID(group.getInt("gamesid"));
                        }
                    }
                    else {
                        if ((!group.isNull("groupid")) && (!group.isNull("gamesid"))) {
                            db.addGroup(group.getInt("groupid"), group.getInt("gamesid"), 0);
                        }
                    }


                }



                if (userFunctions.isGameActive(getApplicationContext())) {

                    // Get the current active game from the SQLlite database with groupID and gameID

                    //TODO resolve the local DB to save game so it is not doing network calls all the time.

//                    HashMap<String, Integer> game = new HashMap<String, Integer>();
//
//                    game = db.getActiveGroupGame();
//
//                    // If there are values for the groupID and gameID add it to the global
//                    if ((game != null) && (game.size() > 0)) {
//                        if ((game.get(KEY_GROUPID) != null) && (game.get(KEY_GAMEID) != null)) {
//
//                            g.setGroupID(game.get(KEY_GROUPID));
//                            g.setGameID(game.get(KEY_GAMEID));
//                        }
//                    }
                }




            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            catch (RuntimeException e) {
                e.printStackTrace();
                return false;
            }

            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(false);

            if (success) {
                finish();
                Log.e("GroupGame","Group Game details retrieved correctly");
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                Log.e("GroupGame","Group Game details not retrieved from database");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }


}
