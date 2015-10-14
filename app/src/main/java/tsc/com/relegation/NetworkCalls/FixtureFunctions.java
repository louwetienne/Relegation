package tsc.com.relegation.NetworkCalls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tsc.com.relegation.IndividualQuestion;
import tsc.com.relegation.JSONParser;

/**
 * Created by Etienne on 2015-03-15.
 */
public class FixtureFunctions {

    private JSONParser jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/

    //WAMP database
//    private static String fixtureListURL = "http://10.0.2.2/relegation/get_fixture_list.php";
//    private static String updateUserAnswersURL = "http://10.0.2.2/relegation/update_user_answers.php";

    //Live Database
    private static String fixtureListURL = "http://notify.thesoftwareco.co.za/get_fixture_list.php";
    private static String updateUserAnswersURL = "http://notify.thesoftwareco.co.za/update_user_answers.php";

    // constructor
    public FixtureFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * function make Login Request
     * @param userid
     * @param groupid
     * @param gameid
     * */
    public JSONObject getFixtureListForGroup(int userid, int groupid, int gameid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userid", Integer.toString(userid)));
        params.add(new BasicNameValuePair("groupid", Integer.toString(groupid)));
        params.add(new BasicNameValuePair("gameid", Integer.toString(gameid)));
        JSONObject json = jsonParser.getJSONFromUrl(fixtureListURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    public JSONObject insertUpdateUserAnswers(ArrayList<IndividualQuestion> insertUpdateList, int userID, boolean isFinalCommit) {

        int lockedIn;

        JSONObject jResult = new JSONObject();
        JSONArray jArray = new JSONArray();

        for (int i = 0; i < insertUpdateList.size(); i++) {
            JSONObject jGroup = new JSONObject();
            try {

                //lockedIn = (insertUpdateList.get(i).isLockedIn()) ? 1 : 0;
                lockedIn = isFinalCommit ? 1 : 0;

                jGroup.put("questionid", insertUpdateList.get(i).getItemID());
                jGroup.put("userid", userID);
                //jGroup.put("answerid", insertUpdateList.get(i).getUserAnswerID());
                if (insertUpdateList.get(i).isOptionSelected1()) {
                    jGroup.put("answerid", insertUpdateList.get(i).getOptionID1());
                }
                else if (insertUpdateList.get(i).isOptionSelected2()) {
                    jGroup.put("answerid", insertUpdateList.get(i).getOptionID2());
                }
                else {
                    jGroup.put("answerid", 0);
                }

                jGroup.put("locked_in", lockedIn);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // etcetera

            jArray.put(jGroup);
        }

        try {
            jResult.put("fixtures", jArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return jResult;

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("fixtures", jResult.toString()));


        JSONObject json = jsonParser.getJSONFromUrl(updateUserAnswersURL, params);

        return json;
    }

}
