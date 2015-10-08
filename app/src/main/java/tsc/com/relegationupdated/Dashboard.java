package tsc.com.relegationupdated;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tsc.com.relegationupdated.DatabaseObjects.LeaderboardUser;
import tsc.com.relegationupdated.DatabaseObjects.Question;
import tsc.com.relegationupdated.DatabaseObjects.QuestionUser;
import tsc.com.relegationupdated.Global.Globals;
import tsc.com.relegationupdated.NetworkCalls.DashboardFunctions;


public class Dashboard extends android.support.v4.app.Fragment implements OnChartValueSelectedListener {

    private FixtureListTask mAuthTask = null;

    private ArrayList<LeaderboardUser> leaderboardUsers;
    private ArrayList<QuestionUser> latestResultQuestionUsers;
    private ArrayList<QuestionUser> nextFixtureQuestionUsers;
    private ArrayList<Question> formGuideList;

    private Question lastestResultQuestion;
    private Question nextFixtureQuestion;


    private View mDashboardScrollView;
    private View mProgressView;

    private boolean mSearchCheck;

    private View rootView;

    protected BarChart mLeaderboardChart;
    protected PieChart mLatestResultChart;
    protected PieChart mNextFixtureChart;

    private TextView latestResultDescription;
    private TextView nextFixtureDescription;
    private TextView mCurrentPosition;
    private TextView mGameDetails;
    private ArrayList<String> latestResultCorrectUsers;
    private ArrayList<String> latestResultIncorrectUsers;
    private ArrayList<String> nextFixtureFirstOptionUsers;
    private ArrayList<String> nextFixtureSecondOptionUsers;

    public static final int[] RESULTS_COLORS = {
            Color.rgb(0, 127,14), Color.rgb(255, 24, 20)
    };

    public static final int[] FIXTURE_COLORS = {
            Color.rgb(102, 234,255), Color.rgb(10, 50, 255)
    };

    public Dashboard newInstance(String text){
        Dashboard mFragment = new Dashboard();
        Bundle mBundle = new Bundle();
        //mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub



        rootView = inflater.inflate(R.layout.activity_dashboard, container, false);

        mDashboardScrollView = rootView.findViewById(R.id.scroll_view);
        mProgressView = rootView.findViewById(R.id.dashboard_progress);

        mLeaderboardChart = (BarChart) rootView.findViewById(R.id.chart1);
        mLatestResultChart = (PieChart) rootView.findViewById(R.id.pieChart1);
        mNextFixtureChart = (PieChart) rootView.findViewById(R.id.pieChart2);

        latestResultDescription = (TextView) rootView.findViewById(R.id.latestResultDescription);
        nextFixtureDescription = (TextView) rootView.findViewById(R.id.nextFixtureDescription);
        mCurrentPosition = (TextView) rootView.findViewById(R.id.txtCurrentPosition);
        mGameDetails = (TextView) rootView.findViewById(R.id.txtGameDetails);
        latestResultCorrectUsers = new ArrayList<String>();
        latestResultIncorrectUsers = new ArrayList<String>();
        nextFixtureFirstOptionUsers = new ArrayList<String>();
        nextFixtureSecondOptionUsers = new ArrayList<String>();

        if (mAuthTask == null) {
            requestDashboardResults();
        }

        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return rootView;
    }

    private void generateLeaderboardBarGraph() {
        // enable the drawing of values
        mLeaderboardChart.setDrawYValues(true);

        mLeaderboardChart.setDrawValueAboveBar(true);

        mLeaderboardChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mLeaderboardChart.setMaxVisibleValueCount(60);

        // disable 3D
        mLeaderboardChart.set3DEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        mLeaderboardChart.setPinchZoom(true);

        mLeaderboardChart.setDrawGridBackground(false);
        mLeaderboardChart.setDrawHorizontalGrid(true);
        mLeaderboardChart.setDrawVerticalGrid(false);

        // sets the text size of the values inside the chart
        mLeaderboardChart.setValueTextSize(10f);

        //mLeaderboardChart.setYRange(-2.f,7f,false);
        mLeaderboardChart.setYRange((float) leaderboardUsers.get(leaderboardUsers.size() - 1).getScore() - 1, (float) leaderboardUsers.get(0).getScore() + 1, false);


        mLeaderboardChart.setDrawBorder(false);
        // mLeaderboardChart.setBorderPositions(new BorderPosition[] {BorderPosition.LEFT,
        // BorderPosition.RIGHT});

        //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XLabels xl = mLeaderboardChart.getXLabels();
        xl.setPosition(XLabels.XLabelPosition.BOTTOM);
        xl.setCenterXLabelText(true);
        //xl.setTypeface(tf);

        YLabels yl = mLeaderboardChart.getYLabels();
        //yl.setTypeface(tf);
        yl.setLabelCount(8);
        yl.setPosition(YLabels.YLabelPosition.BOTH_SIDED);

        //mLeaderboardChart.setValueTypeface(tf);

        setLeaderboardData();
        mLeaderboardChart.animateY(2500);
        mLeaderboardChart.invalidate();
    }

    private void generateLatestResultPieChart() {
        // change the color of the center-hole
        //mLeaderboardChart.setHoleColor(Color.rgb(235, 235, 235));
        //mLatestResultChart.setHoleColorTransparent(true);

        //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //mLatestResultChart.setValueTypeface(tf);
        //mLatestResultChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        mLatestResultChart.setHoleRadius(40f);
        mLatestResultChart.setHoleColor((Color.rgb(235, 235, 235)));
        mLatestResultChart.setTransparentCircleRadius(40f);

        mLatestResultChart.setDescription("");

        mLatestResultChart.setDrawYValues(true);
        mLatestResultChart.setDrawCenterText(true);

        mLatestResultChart.setDrawHoleEnabled(true);

        mLatestResultChart.setRotationAngle(0);

        // draws the corresponding description value into the slice
        mLatestResultChart.setDrawXValues(false);

        // enable rotation of the chart by touch
        mLatestResultChart.setRotationEnabled(false);

        // display percentage values
        mLatestResultChart.setUsePercentValues(true);

        // add a selection listener
        mLatestResultChart.setOnChartValueSelectedListener(this);

        mLatestResultChart.setCenterText("Result");

        setLatestResultChartData();

        mLatestResultChart.animateXY(1500, 1500);
        mLatestResultChart.spin(2000, 0, 360);

        mLatestResultChart.setDrawLegend(true);

        Legend l = mLatestResultChart.getLegend();

        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);


        // change the color of the center-hole
//        mLeaderboardChart.setHoleColor(Color.rgb(235, 235, 235));
        //mLatestResultChart.setHoleColorTransparent(true);

        //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //mLatestResultChart.setValueTypeface(tf);
        //mLatestResultChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
    }

    private void generateNextFixturePieChart() {
        Legend l;
        mNextFixtureChart.setHoleRadius(40f);
        mNextFixtureChart.setHoleColor((Color.rgb(235, 235, 235)));
        mNextFixtureChart.setTransparentCircleRadius(40f);

        mNextFixtureChart.setDescription("");

        mNextFixtureChart.setDrawYValues(true);
        mNextFixtureChart.setDrawCenterText(true);

        mNextFixtureChart.setDrawHoleEnabled(true);

        mNextFixtureChart.setRotationAngle(0);

        // draws the corresponding description value into the slice
        mNextFixtureChart.setDrawXValues(false);

        // enable rotation of the chart by touch
        mNextFixtureChart.setRotationEnabled(false);

        // display percentage values
        mNextFixtureChart.setUsePercentValues(true);

        // add a selection listener
        mNextFixtureChart.setOnChartValueSelectedListener(this);
        // mLeaderboardChart.setTouchEnabled(false);

        mNextFixtureChart.setCenterText("Fixture");

        setNextFixtureChartData();

        mNextFixtureChart.animateXY(1500, 1500);
        // mLeaderboardChart.spin(2000, 0, 360);

        l = mNextFixtureChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
    }

    private void generateFormGuide() {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Log.i("Lengths", String.valueOf(width));
        Log.i("Lengths", String.valueOf(height));


        final float scale = getActivity().getResources().getDisplayMetrics().density;
        int pixels = (int) (60 * scale + 0.5f);
        int leftMargin = (int) (10 * scale + 0.5f);

        Log.i("Lengths", String.valueOf(scale));
        Log.i("Lengths", String.valueOf(pixels));
        Log.i("Lengths", String.valueOf(leftMargin));



        final LinearLayout linearLayoutFormGuide = (LinearLayout) rootView.findViewById(R.id.linearLayoutFormGuide);

        int numberOfFormGuides = Math.min((int) (width / (pixels + leftMargin)),5);

        numberOfFormGuides = Math.min(numberOfFormGuides,formGuideList.size());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(leftMargin, 0, 0, 0);

        ImageView formGuideImage;


        for (int i = 0; i < numberOfFormGuides; i++) {
            formGuideImage = new ImageView(getActivity());
            formGuideImage.setLayoutParams(lp);
            TextDrawable drawable;

            drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(pixels)  // width in px
                    .height(pixels) // height in px
                    .endConfig()
                    .buildRound("", Color.rgb(255, 255, 255));

            if (formGuideList.get(i).getOutcome().toString().equals("Outcome")) {
                if (formGuideList.get(i).getCorrectAnswerDescription().toString().equals("Correct")) {
                    drawable = TextDrawable.builder()
                            .beginConfig()
                            .width(pixels)  // width in px
                            .height(pixels) // height in px
                            .endConfig()
                            .buildRound("W", Color.rgb(38, 127, 0));

                }
                else if (formGuideList.get(i).getCorrectAnswerDescription().toString().equals("Incorrect")) {
                    drawable = TextDrawable.builder()
                            .beginConfig()
                            .width(pixels)  // width in px
                            .height(pixels) // height in px
                            .endConfig()
                            .buildRound("L", Color.rgb(255, 24, 20));

                }
            }
            else if (formGuideList.get(i).getOutcome().toString().equals("Push")) {
                drawable = TextDrawable.builder()
                        .beginConfig()
                        .width(pixels)  // width in px
                        .height(pixels) // height in px
                        .endConfig()
                        .buildRound("P", Color.rgb(72, 0, 255));

            }

            formGuideImage.setImageDrawable(drawable);
            linearLayoutFormGuide.addView(formGuideImage);

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //return true;

        //inflater.inflate(R.menu.menu, menu);

        //Select search item
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


    private void setLeaderboardData() {

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        int userPosition = 0;

        //leaderboardUsers;

        Globals g = (Globals) getActivity().getApplication();

        for (int i = 0; i < leaderboardUsers.size(); i++) {

            if (leaderboardUsers.get(i).getUserID() == g.getUserID()) {
                //mCurrentPosition.setText(i);
                userPosition = i + 1;
            }
            xVals.add(leaderboardUsers.get(i % leaderboardUsers.size()).getInitials());
            yVals1.add(new BarEntry((float) leaderboardUsers.get(i % leaderboardUsers.size()).getScore(), i));
        }

//        for (int i = 0; i < count; i++) {
//            xVals.add(mUsers[i % count]);
//            yVals1.add(new BarEntry((float) mUserScore[i % count], i));
//        }



//        for (int i = 0; i < count; i++) {
//            float mult = (range + 1);
//            float val = (float) (Math.random() * mult);
//            yVals1.add(new BarEntry(val, i));
//        }

        int[] ColourBands;
        ColourBands = getColorBands(leaderboardUsers.size());

        if (userPosition > 0) {
            //mCurrentPosition.setText("2");
            mCurrentPosition.setText(getOrdinal(userPosition));
            mCurrentPosition.setTextColor(ColourBands[userPosition - 1]);
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet");
        set1.setBarSpacePercent(35f);
        set1.setColors(getColorBands(leaderboardUsers.size()));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        mLeaderboardChart.setData(data);
    }


    public static String getOrdinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }

    private void setLatestResultChartData() {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        latestResultDescription.setText(lastestResultQuestion.getQuestionDescription());

        if (lastestResultQuestion.getCorrectAnswerID() == lastestResultQuestion.getAnswerID1()) {
            xVals.add(lastestResultQuestion.getAnswerDescription1()); // Correct - Green
            xVals.add(lastestResultQuestion.getAnswerDescription2()); // Incorrect - Red
        }
        else {
            xVals.add(lastestResultQuestion.getAnswerDescription2()); // Correct - Green
            xVals.add(lastestResultQuestion.getAnswerDescription1()); // Incorrect - Red
        }
        for (int c : RESULTS_COLORS)
            colors.add(c);

        int correctAnswerCount = 0;
        int incorrectAnswerCount = 0;
        latestResultCorrectUsers.clear();
        latestResultIncorrectUsers.clear();


        for (int i = 0; i < latestResultQuestionUsers.size(); i++) {
            if (lastestResultQuestion.getCorrectAnswerID() == latestResultQuestionUsers.get(i).getAnswerID()) {
                correctAnswerCount++;
                latestResultCorrectUsers.add(latestResultQuestionUsers.get(i).getUserName());
            }
            else {
                incorrectAnswerCount++;
                latestResultIncorrectUsers.add(latestResultQuestionUsers.get(i).getUserName());
            }
        }

        yVals1.add(new Entry(correctAnswerCount, 0, new String("Results")));
        yVals1.add(new Entry(incorrectAnswerCount, 1, new String("Results")));

        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setSliceSpace(3f);

        // add a lot of colors

        set1.setColors(colors);
        //set1.setColors(getColorBands(10));

        if (xVals.get(0) != null) {
            PieData data = new PieData(xVals, set1);
            mLatestResultChart.setData(data);
        }

        // undo all highlights
        mLatestResultChart.highlightValues(null);

        mLatestResultChart.invalidate();
    }

    private void setNextFixtureChartData() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();


        nextFixtureDescription.setText(nextFixtureQuestion.getQuestionDescription());


        xVals.add(nextFixtureQuestion.getAnswerDescription1()); // Correct - Green
        xVals.add(nextFixtureQuestion.getAnswerDescription2()); // Incorrect - Red

        for (int c : FIXTURE_COLORS)
            colors.add(c);

        int firstOptionCount = 0;
        int secondOptionCount = 0;
        nextFixtureFirstOptionUsers.clear();
        nextFixtureSecondOptionUsers.clear();

        xVals.add(nextFixtureQuestion.getAnswerDescription1()); // Option 1
        xVals.add(nextFixtureQuestion.getAnswerDescription2()); // Option 2


        for (int i = 0; i < nextFixtureQuestionUsers.size(); i++) {
            if (nextFixtureQuestion.getAnswerID1() == nextFixtureQuestionUsers.get(i).getAnswerID()) {
                firstOptionCount++;
                nextFixtureFirstOptionUsers.add(nextFixtureQuestionUsers.get(i).getUserName());
            }
            else {
                secondOptionCount++;
                nextFixtureSecondOptionUsers.add(nextFixtureQuestionUsers.get(i).getUserName());
            }
        }




        if (firstOptionCount > 0) {
            yVals1.add(new Entry(firstOptionCount, 0, new String("Fixtures")));
        }
        if (secondOptionCount > 0) {
            yVals1.add(new Entry(secondOptionCount, 1, new String("Fixtures")));
        }


        //xVals.add("Rosberg");
        //xVals.add("Hamilton");

        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setSliceSpace(3f);

        // add a lot of colors

        set1.setColors(colors);

        PieData data = new PieData(xVals, set1);
        mNextFixtureChart.setData(data);

        // undo all highlights
        mNextFixtureChart.highlightValues(null);

        mNextFixtureChart.invalidate();

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

    @Override
    public void onValueSelected(Entry entry, int i) {

        if (entry == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + entry.getVal() + ", xIndex: " + entry.getXIndex()
                        + ", DataSet index: " + i + entry.getData().toString());



        if (entry.getData().equals(new String("Results"))) {
            // Get ListView object from xml
            final ListView listView = (ListView) rootView.findViewById(R.id.listView1);
            Log.i("VAL SELECTED", "Results Pie Chart");

            if (entry.getXIndex() == 0) { // Correct - Green

                // Define a new Adapter
                // First parameter - Context
                // Second parameter - Layout for the row
                // Third parameter - ID of the TextView to which the data is written
                // Forth - the Array of data

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.leaderboard_list_item, R.id.leaderboradTextName, latestResultCorrectUsers);


                // Assign adapter to ListView
                listView.setAdapter(adapter);


                listView.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });


                // ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition     = position;

                        // ListView Clicked item value
                        String  itemValue    = (String) listView.getItemAtPosition(position);

                        // Show Alert
                        Toast.makeText(getActivity(),
                                "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                                .show();

                    }

                });
            }
            else if (entry.getXIndex() == 1) { // Incorrect - Red

                // Define a new Adapter
                // First parameter - Context
                // Second parameter - Layout for the row
                // Third parameter - ID of the TextView to which the data is written
                // Forth - the Array of data

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.leaderboard_list_item, R.id.leaderboradTextName, latestResultIncorrectUsers);


                // Assign adapter to ListView
                listView.setAdapter(adapter);


                listView.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });


                // ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition     = position;

                        // ListView Clicked item value
                        String  itemValue    = (String) listView.getItemAtPosition(position);

                        // Show Alert
                        Toast.makeText(getActivity(),
                                "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                                .show();

                    }

                });

            }
        }

        else if (entry.getData().equals(new String("Fixtures"))) {
            // Get ListView object from xml
            final ListView listView = (ListView) rootView.findViewById(R.id.listView2);
            Log.i("VAL SELECTED", "Results Pie Chart");

            if (entry.getXIndex() == 0) {

                // Defined Array values to show in ListView
                String[] values2 = new String[] { "Henri Synders", "Etienne Louw","Pedrie Le Rouw", "Luke Taylor",
                        "Roelf Jacobs"};

                // Define a new Adapter
                // First parameter - Context
                // Second parameter - Layout for the row
                // Third parameter - ID of the TextView to which the data is written
                // Forth - the Array of data

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.leaderboard_list_item, R.id.leaderboradTextName, nextFixtureFirstOptionUsers);


                // Assign adapter to ListView
                listView.setAdapter(adapter);


                listView.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });


                // ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition     = position;

                        // ListView Clicked item value
                        String  itemValue    = (String) listView.getItemAtPosition(position);

                        // Show Alert
                        Toast.makeText(getActivity(),
                                "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                                .show();

                    }

                });
            }
            else if (entry.getXIndex() == 1) {

                // Defined Array values to show in ListView
                String[] values = new String[] { "Bryden Barker", "Heinrich Wessels", "Darryl Louw",
                        "Matthew Huber", "Shaun Bennet"};

                // Defined Array values to show in ListView
                String[] values2 = new String[] { "Henri Synders", "Etienne Louw","Pedrie Le Rouw", "Luke Taylor",
                        "Roelf Jacobs"};

                // Define a new Adapter
                // First parameter - Context
                // Second parameter - Layout for the row
                // Third parameter - ID of the TextView to which the data is written
                // Forth - the Array of data

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.leaderboard_list_item, R.id.leaderboradTextName, nextFixtureSecondOptionUsers);


                // Assign adapter to ListView
                listView.setAdapter(adapter);


                listView.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });


                // ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition     = position;

                        // ListView Clicked item value
                        String  itemValue    = (String) listView.getItemAtPosition(position);

                        // Show Alert
                        Toast.makeText(getActivity(),
                                "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                                .show();

                    }

                });

            }
        }

    }

    @Override
    public void onNothingSelected() {

    }

    public int[] getColorBands(int bands) {

        //List<Color> colorBands = new ArrayList<>(bands);

        int greenNumbers;
        int redNumbers;

        int count = 0;

        greenNumbers = (int) Math.ceil(bands * 0.70);
        redNumbers = (int) Math.floor(bands * 0.30);

        int[] colorBandsArray = new int[bands];
        for (int index = 0; index < greenNumbers; index++) {
            //colorBands.add(darken(192,255,140, (double) index / (double) bands));
            colorBandsArray[count] = (lighten(0,127,14, (double) index / (double) greenNumbers));
            count++;
        }
        for (int index = 0; index < redNumbers; index++) {
            //colorBands.add(darken(192,255,140, (double) index / (double) bands));
            colorBandsArray[count] = (darken(255,127,127, (double) index / (double) redNumbers));
            count++;
        }
        return colorBandsArray;

    }

    public static int darken(int redParam, int greenParam, int blueParam, double fraction) {


        int red = (int) Math.round(Math.max(0, redParam - 155 * fraction));
        int green = (int) Math.round(Math.max(0, greenParam - 155 * fraction));
        int blue = (int) Math.round(Math.max(0, blueParam - 155 * fraction));

        //int alpha = color.getAlpha();

        //return new Color(red, green, blue);
        return Color.rgb(red, green, blue);

    }

    public static int lighten(int redParam, int greenParam, int blueParam, double fraction) {


        int red = (int) Math.round(Math.min(redParam + 150 * fraction, 255));
        int green = (int) Math.round(Math.min(greenParam + 150 * fraction, 255));
        int blue = (int) Math.round(Math.min(blueParam + 150 * fraction, 255));

        //int alpha = color.getAlpha();

        //return new Color(red, green, blue);
        return Color.rgb(red, green, blue);

    }

    private void requestDashboardResults() {

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.

        Globals g = (Globals) getActivity().getApplication();

        leaderboardUsers = new ArrayList<LeaderboardUser>();
        showProgress(true);
        //mAuthTask = new FixtureListTask(1,1,1);
        mAuthTask = new FixtureListTask(g.getUserID(), g.getGroupID(), g.getGameID());
        mAuthTask.execute((Void) null);

    }



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

            mDashboardScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            mDashboardScrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mDashboardScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mDashboardScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private List<LeaderboardUser> createLeaderboardList(JSONArray leaderboardListParam) {

        //result = new ArrayList<IndividualQuestion>();

        leaderboardUsers = new ArrayList<LeaderboardUser>();
        try {

            int questionCounter = 1;
            LeaderboardUser mIndividualLeaderboardUser = new LeaderboardUser();

            for (int i = 0; i < leaderboardListParam.length(); i++) {


                JSONObject user = leaderboardListParam.getJSONObject(i);

                mIndividualLeaderboardUser = new LeaderboardUser();

                if (!user.isNull("userID"))
                    mIndividualLeaderboardUser.setUserID(user.getInt("userID"));
                if (!user.isNull("UserName"))
                    mIndividualLeaderboardUser.setUserName(user.getString("UserName"));
                if (!user.isNull("Initials"))
                    mIndividualLeaderboardUser.setInitials(user.getString("Initials"));
                if (!user.isNull("Score"))
                    mIndividualLeaderboardUser.setScore(Double.parseDouble(user.getString("Score")));

                mIndividualLeaderboardUser.setLeaderboardPosition(questionCounter);

                leaderboardUsers.add(mIndividualLeaderboardUser);

                questionCounter++;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return leaderboardUsers;
    }

    private void createLatestResult(JSONArray latestResultFixture, JSONArray latestResultUserList) {

        //result = new ArrayList<IndividualQuestion>();

        lastestResultQuestion = new Question();

        try {

            JSONObject question = latestResultFixture.getJSONObject(0);

            if (!question.isNull("QuestionID"))
                lastestResultQuestion.setQuestionID(question.getInt("QuestionID"));
            if (!question.isNull("QuestionDescription"))
                lastestResultQuestion.setQuestionDescription(question.getString("QuestionDescription"));
            if (!question.isNull("correct_answer_id"))
                lastestResultQuestion.setCorrectAnswerID(question.getInt("correct_answer_id"));
            if (!question.isNull("is_completed")) {
                if (question.getInt("is_completed") == 1)
                    lastestResultQuestion.setCompleted(true);
                else if (question.getInt("is_completed") == 0)
                    lastestResultQuestion.setCompleted(false);
            }

            if (!question.isNull("fixture_date")) {
                String FixtureDateString = question.getString("fixture_date");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date FixtureDate = sdf.parse(FixtureDateString);
                lastestResultQuestion.setDate(FixtureDate);
            }
            if (!question.isNull("answerid"))
                lastestResultQuestion.setCorrectAnswerID(question.getInt("answerid"));
            if (!question.isNull("Outcome"))
                lastestResultQuestion.setOutcome(question.getString("Outcome"));
            if (!question.isNull("CorrectAnswer"))
                lastestResultQuestion.setCorrectAnswerDescription(question.getString("CorrectAnswer"));
            if (!question.isNull("TestAnswerID"))
                lastestResultQuestion.setAnswerID1(question.getInt("TestAnswerID"));
            if (!question.isNull("TestAnswerDescription"))
                lastestResultQuestion.setAnswerDescription1(question.getString("TestAnswerDescription"));
            if (!question.isNull("TestAnswerID2"))
                lastestResultQuestion.setAnswerID2(question.getInt("TestAnswerID2"));
            if (!question.isNull("TestAnswerDescription2"))
                lastestResultQuestion.setAnswerDescription2(question.getString("TestAnswerDescription2"));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        latestResultQuestionUsers = new ArrayList<QuestionUser>();
        try {

            int questionCounter = 1;
            QuestionUser mIndividualQuestionUser = new QuestionUser();

            for (int i = 0; i < latestResultUserList.length(); i++) {


                JSONObject user = latestResultUserList.getJSONObject(i);

                mIndividualQuestionUser = new QuestionUser();

                if (!user.isNull("userID"))
                    mIndividualQuestionUser.setUserID(user.getInt("userID"));
                if (!user.isNull("name"))
                    mIndividualQuestionUser.setUserName(user.getString("name"));
                if (!user.isNull("Initials"))
                    mIndividualQuestionUser.setInitials(user.getString("Initials"));
                if (!user.isNull("Score"))
                    mIndividualQuestionUser.setScore(Double.parseDouble(user.getString("Score")));
                if (!user.isNull("AnswerID"))
                    mIndividualQuestionUser.setAnswerID(user.getInt("AnswerID"));
                if (!user.isNull("description"))
                    mIndividualQuestionUser.setAnswerDescription(user.getString("description"));

                latestResultQuestionUsers.add(mIndividualQuestionUser);

                questionCounter++;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return leaderboardUsers;
    }
    
    private void createNextFixture(JSONArray nextFixture, JSONArray nextFixtureUserList) {

        //result = new ArrayList<IndividualQuestion>();

        nextFixtureQuestion = new Question();

        try {

            JSONObject question = nextFixture.getJSONObject(0);

            if (!question.isNull("QuestionID"))
                nextFixtureQuestion.setQuestionID(question.getInt("QuestionID"));
            if (!question.isNull("QuestionDescription"))
                nextFixtureQuestion.setQuestionDescription(question.getString("QuestionDescription"));
            if (!question.isNull("correct_answer_id"))
                nextFixtureQuestion.setCorrectAnswerID(question.getInt("correct_answer_id"));
            if (!question.isNull("is_completed")) {
                if (question.getInt("is_completed") == 1)
                    nextFixtureQuestion.setCompleted(true);
                else if (question.getInt("is_completed") == 0)
                    nextFixtureQuestion.setCompleted(false);
            }

            if (!question.isNull("fixture_date")) {
                String FixtureDateString = question.getString("fixture_date");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date FixtureDate = sdf.parse(FixtureDateString);
                nextFixtureQuestion.setDate(FixtureDate);
            }
            if (!question.isNull("answerid"))
                nextFixtureQuestion.setCorrectAnswerID(question.getInt("answerid"));
            if (!question.isNull("Outcome"))
                nextFixtureQuestion.setOutcome(question.getString("Outcome"));
            if (!question.isNull("CorrectAnswer"))
                nextFixtureQuestion.setCorrectAnswerDescription(question.getString("CorrectAnswer"));
            if (!question.isNull("TestAnswerID"))
                nextFixtureQuestion.setAnswerID1(question.getInt("TestAnswerID"));
            if (!question.isNull("TestAnswerDescription"))
                nextFixtureQuestion.setAnswerDescription1(question.getString("TestAnswerDescription"));
            if (!question.isNull("TestAnswerID2"))
                nextFixtureQuestion.setAnswerID2(question.getInt("TestAnswerID2"));
            if (!question.isNull("TestAnswerDescription2"))
                nextFixtureQuestion.setAnswerDescription2(question.getString("TestAnswerDescription2"));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        nextFixtureQuestionUsers = new ArrayList<QuestionUser>();
        try {

            int questionCounter = 1;
            QuestionUser mIndividualQuestionUser = new QuestionUser();

            for (int i = 0; i < nextFixtureUserList.length(); i++) {


                JSONObject user = nextFixtureUserList.getJSONObject(i);

                mIndividualQuestionUser = new QuestionUser();

                if (!user.isNull("userID"))
                    mIndividualQuestionUser.setUserID(user.getInt("userID"));
                if (!user.isNull("name"))
                    mIndividualQuestionUser.setUserName(user.getString("name"));
                if (!user.isNull("Initials"))
                    mIndividualQuestionUser.setInitials(user.getString("Initials"));
                if (!user.isNull("Score"))
                    mIndividualQuestionUser.setScore(Double.parseDouble(user.getString("Score")));
                if (!user.isNull("AnswerID"))
                    mIndividualQuestionUser.setAnswerID(user.getInt("AnswerID"));
                if (!user.isNull("description"))
                    mIndividualQuestionUser.setAnswerDescription(user.getString("description"));

                nextFixtureQuestionUsers.add(mIndividualQuestionUser);

                questionCounter++;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return leaderboardUsers;
    }

    private void createFormGuide(JSONArray formGuide) {
        Question currentFormQuestion = new Question();

        try {
            formGuideList = new ArrayList<Question>();

            for (int i = 0; i < formGuide.length(); i++) {


                JSONObject question = formGuide.getJSONObject(i);

                currentFormQuestion = new Question();

                if (!question.isNull("QuestionID"))
                    currentFormQuestion.setQuestionID(question.getInt("QuestionID"));
                if (!question.isNull("QuestionDescription"))
                    currentFormQuestion.setQuestionDescription(question.getString("QuestionDescription"));
                if (!question.isNull("correct_answer_id"))
                    currentFormQuestion.setCorrectAnswerID(question.getInt("correct_answer_id"));
                if (!question.isNull("is_completed")) {
                    if (question.getInt("is_completed") == 1)
                        currentFormQuestion.setCompleted(true);
                    else if (question.getInt("is_completed") == 0)
                        currentFormQuestion.setCompleted(false);
                }

                if (!question.isNull("fixture_date")) {
                    String FixtureDateString = question.getString("fixture_date");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date FixtureDate = sdf.parse(FixtureDateString);
                    currentFormQuestion.setDate(FixtureDate);
                }
                if (!question.isNull("answerid"))
                    currentFormQuestion.setCorrectAnswerID(question.getInt("answerid"));
                if (!question.isNull("Outcome"))
                    currentFormQuestion.setOutcome(question.getString("Outcome"));
                if (!question.isNull("CorrectAnswer"))
                    currentFormQuestion.setCorrectAnswerDescription(question.getString("CorrectAnswer"));
                if (!question.isNull("TestAnswerID"))
                    currentFormQuestion.setAnswerID1(question.getInt("TestAnswerID"));
                if (!question.isNull("TestAnswerDescription"))
                    currentFormQuestion.setAnswerDescription1(question.getString("TestAnswerDescription"));
                if (!question.isNull("TestAnswerID2"))
                    currentFormQuestion.setAnswerID2(question.getInt("TestAnswerID2"));
                if (!question.isNull("TestAnswerDescription2"))
                    currentFormQuestion.setAnswerDescription2(question.getString("TestAnswerDescription2"));

                formGuideList.add(currentFormQuestion);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
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
        JSONArray leaderboard = null;
        JSONArray latestResult = null;
        JSONArray latestResultUserPicks = null;
        JSONArray upcomingFixture = null;
        JSONArray upcomingFixtureUserPicks = null;
        JSONArray formGuide = null;

        //FixtureFunctions fixtureFunction = new FixtureFunctions();
        DashboardFunctions dashboardFunction = new DashboardFunctions();
        JSONObject json;



        FixtureListTask(int userID, int groupID, int gameID) {
            mUserID = userID;
            mGroupID = groupID;
            mGameID = gameID;

        }




        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.



            //json = fixtureFunction.getFixtureListForGroup(mUserID,mGroupID,mGameID);
            json = dashboardFunction.getDashboard(mUserID,mGroupID,mGameID);

            Log.i("JSON", String.valueOf(json.length()));

            try {


                leaderboard = json.getJSONArray("leaderboard");
                latestResult = json.getJSONArray("latestResult");
                latestResultUserPicks = json.getJSONArray("latestResultUserPicks");
                upcomingFixture = json.getJSONArray("upcomingFixture");
                upcomingFixtureUserPicks = json.getJSONArray("upcomingFixtureUserPicks");
                formGuide = json.getJSONArray("formGuide");


                Log.i("JSON_Leaderboard",String.valueOf(leaderboard.length()));
                Log.i("JSON_LatestResult",String.valueOf(latestResult.length()));
                Log.i("JSON_LatestResultUser",String.valueOf(latestResultUserPicks.length()));
                Log.i("JSON_Fixture",String.valueOf(upcomingFixture.length()));
                Log.i("JSON_FixtureUser", String.valueOf(upcomingFixtureUserPicks.length()));
                Log.i("JSON_FormGuide", String.valueOf(formGuide.length()));

                createLeaderboardList(leaderboard);
                if ((latestResult.length() > 0) && (latestResultUserPicks.length() >0))
                    createLatestResult(latestResult, latestResultUserPicks);
                if ((upcomingFixture.length() > 0) && (upcomingFixtureUserPicks.length() > 0))
                    createNextFixture(upcomingFixture, upcomingFixtureUserPicks);
                createFormGuide(formGuide);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //finish();

                //actionButton.setVisibility(View.VISIBLE);

                //actionButton.setImageResource(R.drawable.ic_done_white);
                //actionButton.setButtonColor(getResources().getColor(R.color.fab_material_blue_500));
                //FixtureAdapter ca = new FixtureAdapter(leaderboardUsers);
                //recList.setAdapter(ca);

                if ((leaderboard.length() > 0))
                    generateLeaderboardBarGraph();

                if ((latestResult.length() > 0) && (latestResultUserPicks.length() >0))
                    generateLatestResultPieChart();

                if ((upcomingFixture.length() > 0) && (upcomingFixtureUserPicks.length() > 0))
                    generateNextFixturePieChart();

                generateFormGuide();

                Log.e("Login","Logged In True");
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                Log.e("Login","Logged In Error");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
