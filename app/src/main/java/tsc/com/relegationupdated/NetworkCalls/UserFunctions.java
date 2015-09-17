package tsc.com.relegationupdated.NetworkCalls;

/**
 * Created by etienne on 2014/10/07.
 */

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tsc.com.relegationupdated.DatabaseHandler;
import tsc.com.relegationupdated.JSONParser;

public class UserFunctions {

    private JSONParser jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/

    //WAMP database
//    private static String loginURL = "http://10.0.2.2/relegation/login_register.php";
//    private static String registerURL = "http://10.0.2.2/relegation/login_register.php";
//    private static String activeGroupGameURL = "http://10.0.2.2/relegation/active_group_game.php";
//    private static String activeGroupAndGamesURL = "http://10.0.2.2/relegation/active_groups_and_games.php";

    //Live database
    private static String loginURL = "http://notify.thesoftwareco.co.za/login_register.php";
    private static String registerURL = "http://notify.thesoftwareco.co.za/login_register.php";
    private static String activeGroupGameURL = "http://notify.thesoftwareco.co.za/active_group_game.php";
    private static String activeGroupAndGamesURL = "http://notify.thesoftwareco.co.za/active_groups_and_games.php";

    //private static String loginURL = "http://192.168.1.8/relegation/login_register.php";


    private static String login_tag = "login";
    private static String register_tag = "register";

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    /**
     * function to get all active games user is competing in.
     * @param userID
     * @param groupID
     * */
    public JSONObject activeGroupGamesByUserID(String userID, String groupID){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userID", userID));
        params.add(new BasicNameValuePair("groupid", groupID));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(activeGroupGameURL, params);
        // return json
        return json;
    }

    /**
     * function to get all active games user is competing in.
     * @param userID
     * */
    public JSONObject activeGroupGamesByUserID(String userID){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userid", userID));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(activeGroupAndGamesURL, params);
        // return json
        return json;
    }

    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getLoginRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }

    /**
     * Function active game
     * */
    public boolean isGameActive(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getActiveGamesRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }

    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}
