package com.example.rohithreddy.hkwikmint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String mapvalue ="";

    MobileNumber MN;
        int draweropencount=0;
        boolean internet;
    private View navheader;
        TextView textinternet,textlocation;
        Settings Settings;
        Scan2 Scan2;
        Scan Scan;
     Barcode barcode;
    public static final int REQUEST_CODE = 100;
    String datetime = "Hello world!";
    UserSessionManager session;
    double latitude,longitude;    String Status;
   String longi,lati,phone,pass; private ProgressDialog pDialog;
    String responseBody, result1 = "", error = "", result2 = "";
    Response response;String x = "nointernet";
    SwitchCompat mSwitchShowSecure;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    CompoundButton.OnCheckedChangeListener switchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //  hideSoftKeyboard();
      //  final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        session = new UserSessionManager(MainActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        phone = user.get(UserSessionManager.KEY_NAME);
        pass = user.get(UserSessionManager.KEY_PASS);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

            navheader= navigationView.getHeaderView(0);
         textinternet = (TextView) navheader.findViewById(R.id.internet);
         textlocation = (TextView) navheader.findViewById(R.id.location);

        //////////
        //to check if drawer is opened or not
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {

            private float last = 0;

            @Override
            public void onDrawerSlide(View arg0, float arg1) {}

            @Override public void onDrawerStateChanged(int arg0) {}
            @Override public void onDrawerOpened(View arg0) {
                draweropencount++;
                internet=isNetworkAvailable();
//                System.out.println(internet);
//                System.out.println(draweropencount);
                if(internet==true){
                    System.out.println("yes internet..");
                    textinternet.setBackgroundColor(Color.parseColor("#00ff00"));
                }
                else{
                    System.out.println("no internet..");
                    textinternet.setBackgroundColor(Color.parseColor("#ff0000"));
                }

                ////for checking if location is on or not
                LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                boolean network_enabled = false;

                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch(Exception ex) {}

                if(!gps_enabled && !network_enabled) {
                    System.out.println("no location..");
                    textlocation.setBackgroundColor(Color.parseColor("#ff0000"));
                }
                else {
                    System.out.println("yes location..");
                    textlocation.setBackgroundColor(Color.parseColor("#00ff00"));
                }
                //////////


            }
            @Override public void onDrawerClosed(View arg0) {}

        });
        ////////////


         bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        disableShiftMode(bottomNavigationView);

        if(MN.button==1){
            Fragment fragment = null;
            MN.button=0;
            fragment = new StockAndSale();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        }
        else if(Settings.button==1){
            Fragment fragment = null;
            Settings.button=0;
            fragment = new ErrorcodeFragment();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        }
        else if(Scan2.button==1){
            Intent intent = getIntent();
            System.out.println("outside---------------"+intent.getStringExtra("barcode"));
            String code = intent.getStringExtra("barcode");
            Fragment fragment = null;
            Scan2.button=0;
            fragment = new Register();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        }
        else if(Scan.button==1){
            Intent intent = getIntent();
            System.out.println("outside---------------"+intent.getStringExtra("barcode"));
            String code = intent.getStringExtra("barcode");
            Fragment fragment = null;
            Scan.button=0;
            fragment = new StockAndSale();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        }
        else {
            Fragment fragment = null;
            fragment = new Home();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            bottomNavigationView.getMenu().findItem(R.id.action_item3).setChecked(true);
        }

//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
  //      layoutParams.setBehavior(new BottomNavigationViewBehavior());
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                Intent myIntent = new Intent(MainActivity.this, Scan.class);
                                MainActivity.this.startActivity(myIntent);
                                break;
                            case R.id.action_item2:
                                selectedFragment = new Attendance();
                                navigationView.getMenu().findItem(R.id.attendance).setChecked(true);
                                break;
                            case R.id.action_item3:
                                selectedFragment = new Home();
                                break;
                            case R.id.action_item4:
                                selectedFragment = new TabsMapping();
                                navigationView.getMenu().findItem(R.id.Mapping).setChecked(true);
                                break;
                            case R.id.action_item5:
                                selectedFragment = new VideoPromotion();
                                navigationView.getMenu().findItem(R.id.Promotion).setChecked(true);
                                break;
                        }
                        if (selectedFragment != null) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.content_frame, selectedFragment);
                            transaction.commit();
                        }
                        return true;
                    }
                });

//        //Manually displaying the first fragment - one time only
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, new Home());
//        transaction.commit();

    }


////////////
//check if internet is available or not
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
//////////



//////https://stackoverflow.com/questions/41352934/force-showing-icon-and-title-in-bottomnavigationview-support-android///
    //for bot navigation more than 3 items the title will be hidden bydefault so we need to overdo....

    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        System.out.println(menu.findItem(R.id.show_secure));
        mSwitchShowSecure = (SwitchCompat) menu.findItem(R.id.show_secure).getActionView().findViewById(R.id.switch_show_protected);
        switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(session.isattendancestarted()){
                    System.out.println("yesssss");
                    GiveAttendance("Off");

                } else {
                    System.out.println("nooooooo");
                    GiveAttendance("On");

                }
            }
        };
        if (session.isattendancestarted()) {
            mSwitchShowSecure.setOnCheckedChangeListener (null);
            mSwitchShowSecure.setChecked(true);
            mSwitchShowSecure.setOnCheckedChangeListener(switchListener);
        } else {


        }
        mSwitchShowSecure.setOnCheckedChangeListener (switchListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if(session.isattendancestarted()){
                session.stopattendance();
                GiveAttendance("Off");

            }
            session.logoutUser();
            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            MainActivity.this.startActivity(loginIntent);
            //repeatTask.cancel();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Scan) {
            Intent myIntent = new Intent(MainActivity.this, Scan.class);
           MainActivity.this.startActivity(myIntent);

        } else if (id == R.id.attendance) {
            Fragment fragment = null;
            fragment = new Attendance();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            bottomNavigationView.getMenu().findItem(R.id.action_item2).setChecked(true);


        } else if (id == R.id.Mapping) {
            Fragment fragment = null;
            fragment = new TabsMapping();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            bottomNavigationView.getMenu().findItem(R.id.action_item4).setChecked(true);
        }
        else if (id == R.id.Settings) {
            Fragment fragment = null;
            fragment = new Settings();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }


        } else if (id == R.id.Sync) {
            Fragment fragment = null;
            fragment = new Sync();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }

        } else if (id == R.id.Promotion) {
            Fragment fragment = null;
            fragment = new VideoPromotion();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            bottomNavigationView.getMenu().findItem(R.id.action_item5).setChecked(true);
        }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    public void GiveAttendance(String att){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH-mm-ss");
        datetime = sdf.format(new Date());
        GPSTracker gps = new GPSTracker(MainActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            if (latitude == 0.0) {
                Toast.makeText(getApplicationContext(), "wait for location and try again",
                        Toast.LENGTH_SHORT).show();
                if (att=="On") {
                    mSwitchShowSecure.setOnCheckedChangeListener (null);
                    mSwitchShowSecure.setChecked(false);
                    mSwitchShowSecure.setOnCheckedChangeListener(switchListener);

                } else {
                    mSwitchShowSecure.setOnCheckedChangeListener (null);
                    mSwitchShowSecure.setChecked(true);
                    mSwitchShowSecure.setOnCheckedChangeListener(switchListener);
                }
            } else {
                longi = String.valueOf(longitude);
                lati = String.valueOf(latitude);
                if (att=="On") {
                    //  System.out.print("\nhrerrrcbfvffc");
                    Status = "IN";

                    //verify=1;
                    // System.out.print("i am verifying "+verify+"  kkk");

                } else {
                    // System.out.print("\nfghjgfdszdfghjkhgcfxdcfgvh");
                    Status = "OUT";
                    //verify=0;
                }
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        // Showing progress dialog\
                        pDialog = new ProgressDialog(MainActivity.this);
                        pDialog.setMessage("Please wait...");
                        pDialog.setCancelable(false);
                        pDialog.show();

                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        result1 = "";
                        result2 = "";
                        OkHttpClient client = new OkHttpClient();
                        JSONObject cred = new JSONObject();
                        try {
                            cred.put("field1", phone);
                            cred.put("field2", pass);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject data = new JSONObject();
                        try {
                            data.put("phone", phone);
                            data.put("status", Status);
                            data.put("time", datetime);
                            data.put("lng", longi);
                            data.put("lat", lati);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject studentsObj = new JSONObject();
                        try {
                            studentsObj.put("credentials", cred);
                            studentsObj.put("data", data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String jsonStr = studentsObj.toString();
                        System.out.print(jsonStr);
                        RequestBody formBody = new FormBody.Builder()
                                .add("request", jsonStr)
                                .build();
                        Request request = new Request.Builder()
                                .url(getResources().getString(R.string.url_text)+"/attendance")
                                .post(formBody)
                                .build();

                        try {
                            // System.out.print("2222222222");
                            response = client.newCall(request).execute();
                            responseBody = response.body().string();
                            try {
                                System.out.println("output ..................." + responseBody);
                                JSONObject jsonObj = new JSONObject(responseBody);
                                if (Status.equals("IN")) {
                                    result1 = jsonObj.getString("status");
                                } else {
                                    result2 = jsonObj.getString("status");
                                }
                                if (result1.equals("failed"))
                                    error = jsonObj.getString("errorCode");
                                if (result2.equals("failed"))
                                    error = jsonObj.getString("errorCode");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("ghjkjhghjcvbnmbvcvbnbvc--------"+result1);
                            x = "internet";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }


                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        System.out.println(result1);

                        if (x == "nointernet") {
                            Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                            if (Status.equals("IN")) {
                                mSwitchShowSecure.setOnCheckedChangeListener (null);
                                mSwitchShowSecure.setChecked(false);
                                mSwitchShowSecure.setOnCheckedChangeListener(switchListener);

                                // mSwitchShowSecure.setText("ON");
                            } else {
                                mSwitchShowSecure.setOnCheckedChangeListener (null);
                                mSwitchShowSecure.setChecked(true);
                                mSwitchShowSecure.setOnCheckedChangeListener(switchListener);
                            }
                        } else if (result2.equals("success")) {
                            // db.execSQL("DELETE FROM lasttime1 WHERE phone=" + phone + " ");
                            //  repeatTask.cancel();
                            //System.out.print("\n hereeeeeeee");
                            //   mSwitchShowSecure.setChecked(true);
                            session.stopattendance();

                            Toast.makeText(getApplicationContext(), "recored off duty", Toast.LENGTH_LONG).show();
                        } else if (result1.equals("success")) {

                            // db.execSQL("DELETE FROM videodata WHERE phonen="+phone+" ");
                            System.out.print("starting session");
                            //  mSwitchShowSecure.setChecked(false);
                            session.startattendance();
                            System.out.print("starting session");
                            // bool = false;
                            Toast.makeText(getApplicationContext(), "recored on duty", Toast.LENGTH_LONG).show();
                            // new startrecording().execute();
                        } else if (result1.equals("failed")) {
                            if (Status.equals("IN")) {
                                mSwitchShowSecure.setOnCheckedChangeListener (null);
                                mSwitchShowSecure.setChecked(false);
                                mSwitchShowSecure.setOnCheckedChangeListener(switchListener);

                            } else {
                                mSwitchShowSecure.setOnCheckedChangeListener (null);
                                mSwitchShowSecure.setChecked(true);
                                mSwitchShowSecure.setOnCheckedChangeListener(switchListener);
                            }
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();


                        }
                        else {
                            if (Status.equals("IN")) {
                                System.out.println("###################");
                                mSwitchShowSecure.setOnCheckedChangeListener (null);
                                mSwitchShowSecure.setChecked(false);
                                mSwitchShowSecure.setOnCheckedChangeListener(switchListener);
                            } else {
                                mSwitchShowSecure.setOnCheckedChangeListener (null);
                                mSwitchShowSecure.setChecked(true);
                                mSwitchShowSecure.setOnCheckedChangeListener(switchListener);
                            }
                            Toast.makeText(getApplicationContext(), "server down try again later", Toast.LENGTH_LONG).show();
                            x = "nointernet";
                        }
                        result1="";error="";result2="";

                    }


                }.execute();
            }

        } else {
            gps.showSettingsAlert();

        }
      //  new onsubmit(MainActivity.this);
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                barcode = data.getParcelableExtra("barcode");
//                System.out.println("hjkhghjhgfcvbn====="+barcode.displayValue);
//                System.out.println("jhghjhghjhbvghbghbgh====="+barcode.displayValue.toString());
//
//            }
//
//        }
//        if(barcode!=null){
//            Fragment fragment = null;
//            fragment = new StockAndSale();
//            if (fragment != null) {
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, fragment);
//                ft.commit();
//            }
//        }
//
//            }
    }
