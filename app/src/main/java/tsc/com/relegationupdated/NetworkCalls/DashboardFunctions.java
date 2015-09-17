package tsc.com.relegationupdated.NetworkCalls;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tsc.com.relegationupdated.JSONParser;

/**
 * Created by Etienne on 2015-04-04.
 */
public class DashboardFunctions {
    private JSONParser jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/

    //WAMP database
    //private static String dashboardURL = "http://10.0.2.2/relegation/get_main_leaderboard.php";

    // Live Database
    private static String dashboardURL = "http://notify.thesoftwareco.co.za/get_main_leaderboard.php";

    // constructor
    public DashboardFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * function make Login Request
     * @param userid
     * @param groupid
     * @param gameid
     * */
    public JSONObject getDashboard(int userid, int groupid, int gameid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userid", Integer.toString(userid)));
        params.add(new BasicNameValuePair("groupid", Integer.toString(groupid)));
        params.add(new BasicNameValuePair("gameid", Integer.toString(gameid)));
        JSONObject json = jsonParser.getJSONFromUrl(dashboardURL, params);
        // return json
        Log.e("Dashboard JSON", json.toString());
        return json;
    }
}
