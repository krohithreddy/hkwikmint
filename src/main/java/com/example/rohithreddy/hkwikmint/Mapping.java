package com.example.rohithreddy.hkwikmint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Mapping.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Mapping#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mapping extends Fragment {

    EditText phonenum,outletname,username;
    String user ,phonenumber,outlet,datetime,longi=null,lati=null;
    double longitude,latitude;
    private SQLiteDatabase db;
    MainActivity mainscreen;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Mapping() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mapping.
     */
    // TODO: Rename and change types and number of parameters
    public static Mapping newInstance(String param1, String param2) {
        Mapping fragment = new Mapping();
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
        View view = inflater.inflate(R.layout.fragment_mapping, container, false);

        db = getActivity().openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS mapusers(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username VARCHAR," +
                "outletname VARCHAR," +
                "phonen VARCHAR," +
                "date VARCHAR," +
                "lat VARCHAR,lng VARCHAR,systemid VARCHAR);");

        final Button submit = (Button) view.findViewById(R.id.map);
        phonenum = (EditText) view.findViewById(R.id.phonenumber);
        username = (EditText) view.findViewById(R.id.outletname);
        outletname = (EditText) view.findViewById(R.id.fullname);
        phonenum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber = phonenum.getText().toString();
                user = username.getText().toString();
                user = user.toUpperCase();
                outlet = outletname.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH-mm-ss");
                datetime = sdf.format(new Date());
                GPSTracker gps = new GPSTracker(getActivity());
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                } else {
                    gps.showSettingsAlert();
                }
                if (latitude == 0.0) {
                    Toast.makeText(getContext(), "mapping wihtout location",
                            Toast.LENGTH_SHORT).show();
                }

                if (username.getText().toString().trim().length() == 0) {
                    username.setError("username cant be empty ");
                } else if (outletname.getText().toString().trim().length() == 0) {
                    outletname.setError("outletname cant be empty ");
                } else if (phonenum.getText().toString().trim().length() < 10) {
                    phonenum.setError("phone number should be 10 digits");
                } else {
                    longi = String.valueOf(longitude);
                    lati = String.valueOf(latitude);
                    db.execSQL("INSERT INTO mapusers( username ,outletname ,phonen ,date ,lat ,lng ,systemid )" + " VALUES('" + user + "','" + outlet + "','" + phonenumber + "','" + datetime + "','" + lati + "','" + longi + "','" + null + "');");
                    phonenum.setText("");
                    outletname.setText("");
                    username.setText("");
                    Fragment fragment = null;
                    fragment = new TabsMapping();
                    if (fragment != null) {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                       // mainscreen.navItemIndex = 4;
                        ft.commit();
                    }

                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);


                }


            }
        });
        return view;

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
