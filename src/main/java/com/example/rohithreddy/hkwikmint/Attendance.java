package com.example.rohithreddy.hkwikmint;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import static android.os.SystemClock.elapsedRealtime;
import static com.example.rohithreddy.hkwikmint.R.id.Sync;
import static com.example.rohithreddy.hkwikmint.R.id.container;
import static com.google.android.gms.internal.zzagy.runOnUiThread;
import static java.lang.System.currentTimeMillis;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Attendance.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Attendance#newInstance} factory method to
 * create an instance of this fragment.
 */

//
    //
    //
    //

//TODO :reload this page if u want dynamic when the attendance is stopped or strted while on attendance fragment
    //
    //
    //
    //
public class Attendance extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    long MillisecondTime,Timenow, StartTime,EndTime,elapsedTime,totaltimewhenstartinsec,timenowinsec, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes,Hours, MilliSeconds;
    MainActivity mainactivity;
    String StartT,EndT;
    TextView hour,minutes,timer;
    Handler handler;
    SharedPreferences sharedpreferences;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Attendance() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Attendance.
     */
    // TODO: Rename and change types and number of parameters
    public static Attendance newInstance(String param1, String param2) {
        Attendance fragment = new Attendance();
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


        //////

        ////////
    }
    public long starttimer(){

        StartTime = SystemClock.elapsedRealtime();

        System.out.println("hiiiiStartTime here...."+StartTime);
        return StartTime;
    };
    public void stoptimer(){
        EndTime=SystemClock.elapsedRealtime();
        System.out.println("hiiiiStopTime here...."+EndTime);
    };

    ////https://stackoverflow.com/questions/24156926/oncreateview-method-gets-called-when-and-how-many-times-in-activity-life-cycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance, container, false);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart(){  //u can only use getView() after oncreate() and oncreateView() .. so writing my funciotn in onStart()..
        super.onStart();
        if(mainactivity.isattendancestarted==1) {
            Timenow=SystemClock.elapsedRealtime();
            System.out.println("Starttime---sucks.."+StartTime);//donno y but this is showing 0
//            System.out.println("hiiiiStartTime here...."+StartT);
            System.out.println(Timenow);


            SharedPreferences sharedpreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            long timestarted = sharedpreferences.getLong("StartTimeStr", 0);
            System.out.println("timestarted from sharedpreferences...."+timestarted);
            Chronometer simpleChronometer = (Chronometer) getView().findViewById(R.id.chronometer); // initiate a chronometer
            timenowinsec= (Timenow/1000);

            totaltimewhenstartinsec=(timestarted/1000); //startTime is whatever time you want to start the chronometer from. you might have stored it somwehere
            System.out.println(timenowinsec);
            System.out.println(totaltimewhenstartinsec);
            elapsedTime=timenowinsec-totaltimewhenstartinsec;
            System.out.println("elapsedTime in secs..."+elapsedTime);
//            simpleChronometer.setBase( SystemClock.elapsedRealtime() - (2880 * 60000 + 0 * 1000));//to start from 48hrs as base start
            simpleChronometer.setBase( SystemClock.elapsedRealtime() - elapsedTime*1000);
            simpleChronometer.start(); // start a chronometer
            simpleChronometer.setFormat("%s"); // set the format for a chronometer
        }
    }
    //    public void printDifference(Date startDate, Date endDate) {
//        //milliseconds
//        long different = endDate.getTime() - startDate.getTime();
//
//        System.out.println("startDate : " + startDate);
//        System.out.println("endDate : "+ endDate);
//        System.out.println("different : " + different);
//
//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;
//
//        System.out.printf(
//                "%d days, %d hours, %d minutes, %d seconds%n",
//                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
//    }
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