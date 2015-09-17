package tsc.com.relegationupdated;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;

import tsc.com.relegationupdated.Global.Globals;
import tsc.com.relegationupdated.NetworkCalls.UserFunctions;


public class InitialActivity extends Activity {

    UserFunctions userFunctions;


    private static final String KEY_UID = "uid";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        /**
         * Dashboard Screen for the application
         * */
        // Check login status in database
        userFunctions = new UserFunctions();
        if (userFunctions.isUserLoggedIn(getApplicationContext())) {

            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            Globals g = (Globals) getApplication();

            HashMap<String,String> user = new HashMap<String,String>();
            user = db.getUserDetails();
            g.setUserID(Integer.parseInt(user.get(KEY_UID)));
            //g.setUserID(Integer.parseInt("1"));





            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        startActivity(intent);
        finish();
        // note we never called setContentView()
    }






}



