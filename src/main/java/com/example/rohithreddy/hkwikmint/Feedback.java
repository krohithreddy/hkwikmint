package com.example.rohithreddy.hkwikmint;

import android.content.Context;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Feedback.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Feedback#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Feedback extends Fragment {


    String datetime = "Hello world!",datee="",routeid="";
    double latitude, longitude;
    String longi, lati, phone, pass,userid;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    UserSessionManager session;
    public static EditText ro1, ro2, ro3=null;
    String r1, r2, r3,rating,vidref;

    RatingBar ratingbar1;

    public static int button =0;
    private Cursor d;
    private SQLiteDatabase db;
    int id;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Feedback() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Feedback.
     */
    // TODO: Rename and change types and number of parameters
    public static Feedback newInstance(String param1, String param2) {
        Feedback fragment = new Feedback();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);



        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("      FEEDBACK FORM");
        ro1 = (EditText) view.findViewById(R.id.ro1);
        ro2 = (EditText) view.findViewById(R.id.ro2);
        ro3 = (EditText) view.findViewById(R.id.ro3);
        ratingbar1=(RatingBar) view.findViewById(R.id.ratingBar1);

        session = new UserSessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        phone = user.get(UserSessionManager.KEY_NAME);
        pass = user.get(UserSessionManager.KEY_PASS);


        if(session.isvideostarted()){
            session.stopvideo();
            System.out.println("Attendance is stopped by gp......");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            datetime = sdf.format(new Date());
            datee=datetime;
            datee=datee.replaceAll("[\\s\\-()]", "");
            vidref=null;
          //  vidref=user.get(UserSessionManager.KEY_ID)+'u'+ datee;
        }
        else {
            vidref=null;
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            //datetime = sdf.format(new Date());
            //vidref=user.get(UserSessionManager.KEY_FULLNAME)+'u'+ datetime;


            //////
            // userid=user.get(UserSessionManager.KEY_ID);
        }

        final Button submit = (Button) view.findViewById(R.id.submit);

        ro3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                r1 = ro1.getText().toString();
                r2 = ro2.getText().toString();
                r3 = ro3.getText().toString();
                rating=String.valueOf(ratingbar1.getRating());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                datetime = sdf.format(new Date());
                /*
                if(vidref==null){
                    datee=datetime;
                    datee=datee.replaceAll("[\\s\\-()]", "");           //doesn't work if u use string.replaceall().. u have to use sting=sting.replaceall()
                    System.out.println("dateeee...."+datee);
                    vidref=userid+'u'+ datee;
                }
                */
                GPSTracker gps = new GPSTracker(getActivity());
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                } else {
                    gps.showSettingsAlert();
                }
                if (r1.trim().length() == 0) {
                    ro1.setError("Fullname can't be empty ");
                }
                else if (r2.trim().length() == 0) {
                    ro2.setError("Age can't be empty ");
                }
                else if (r3.trim().length() < 10) {
                    ro3.setError("Phone number should be 10 digits");
                }

                else {
                    longi = String.valueOf(longitude);
                    lati = String.valueOf(latitude);
                    db=getActivity().openOrCreateDatabase("PKMDB", Context.MODE_PRIVATE, null);
                    db.execSQL(" CREATE TABLE IF NOT EXISTS feedbackform(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            " customerName VARCHAR, " +
                            " age VARCHAR, " +
                            " phoneNumber VARCHAR, " +
                            " timeStamp VARCHAR, " +
                            " taste VARCHAR, "+
                            " locationRef VARCHAR, "+
                            " videoRef VARCHAR, "+
                            " lat VARCHAR,lng VARCHAR);");

                    db.execSQL(" CREATE TABLE IF NOT EXISTS rid(route VARCHAR)");
                    d = db.rawQuery("SELECT * FROM rid", null);
                    d.moveToFirst();
                    if (!(d.moveToFirst()) || d.getCount() == 0){
                        System.out.println("rid is empty....bruhh");
                    }
                    else {
                        d.moveToFirst();
                        System.out.println(d.getString(0));
                        routeid=d.getString(1);
                    }


                    db.execSQL("INSERT INTO feedbackform( customerName ,age ,phoneNumber ,timeStamp,taste,locationRef,videoRef ,lat ,lng  )"+" VALUES('" + r1 + "','" + r2 + "','" + r3 + "','" + datetime + "','"+ rating +"','"+ routeid +"','"+ vidref +"','" + lati + "','" + longi + "');");
                    ro1.setText("");
                    ro2.setText("");
                    ro3.setText("");
                    ratingbar1.setRating(0F);


                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);


                    Intent intent = new Intent();
                    button=1;
                    intent.setClass(getActivity(), MainActivity.class);
                    startActivity(intent);
                }

            }


        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
