package com.example.rohithreddy.hkwikmint;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoPromotion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoPromotion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoPromotion extends Fragment {
    UserSessionManager session;
   // MainActivity mainscreen;
    public static int button =0;
    public static ArrayList<String>  rlist=null,rid=null ;
    private SQLiteDatabase db;
    AppCompatAutoCompleteTextView route;
    private Cursor d;
    String r10,r10a,datetime,phone;
    double longitude,latitude;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VideoPromotion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoPromotion.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoPromotion newInstance(String param1, String param2) {
        VideoPromotion fragment = new VideoPromotion();
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
        return inflater.inflate(R.layout.fragment_video_promotion, container, false);
    }@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onViewCreated(view, savedInstanceState);



        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);




        getActivity().setTitle("       PKWIKMINT");
        Button su,ro,sr;
        session = new UserSessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        phone = user.get(UserSessionManager.KEY_NAME);
        ro = view.findViewById(R.id.ro);
        sr = view.findViewById(R.id.sr);
        db=getActivity().openOrCreateDatabase("PKMDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS routelist(routeid VARCHAR," + "route VARCHAR," + "routenum VARCHAR);");
        d = db.rawQuery("SELECT * FROM routelist", null);
        //sr = view.findViewById(R.id.sr);
        su = view.findViewById(R.id.su);
        final TextView txtView;
        txtView=  view.findViewById(R.id.r_id);
        final ArrayList<String> routelist = new ArrayList<String>();
        final ArrayList<String> routeid = new ArrayList<String>();
        if (!(d.moveToFirst()) || d.getCount() == 0){
            Toast.makeText(getContext(), "Sync to get routes", Toast.LENGTH_LONG).show();
        }
        else {
            d.moveToFirst();
            while (d.isAfterLast() == false) {
                System.out.println(d.getColumnNames());

                int totalColumn = d.getColumnCount();
                for (int i = 0; i < totalColumn; i++) {
                    if (d.getColumnName(i) != null) {
                        try {
                            if (d.getString(i) != null) {
                                System.out.println("here i is"+i);
                                System.out.println(d.getColumnName(i));
                                System.out.println(d.getString(i));
                            } else {

                            }
                        } catch (Exception e) {

                        }
                    }
                    System.out.println("one step over");
                }
                String route=d.getString(1)+"("+d.getString(2)+")";
                //  String routerev=d.getString(2)+"("+d.getString(1)+")";
                routelist.add(route);
                // routelist.add(routerev);

                routeid.add(d.getString(0));
                d.moveToNext();
            }
            rlist=routelist;
            rid=routeid;

            d.close();
        }
        route =  view.findViewById(R.id.route);
        ArrayList<String> nll = new ArrayList<String>();
        if(rlist==null) {
            rlist = nll;
            rlist.add(0, "");
            //rlist.add(0, "No Routes present.. please sync routes");
            //rlist.add(0, "hahah");
            // rlist.add(1,"heheh");
        }



        db.execSQL(" CREATE TABLE IF NOT EXISTS rid(route VARCHAR)");
        d = db.rawQuery("SELECT * FROM rid", null);
        d.moveToFirst();
        if (!(d.moveToFirst()) || d.getCount() == 0){
            System.out.println("rid is empty....bruhh");
        }
        else {
            d.moveToFirst();
            System.out.println(d.getString(0));
            String str=d.getString(0);
            txtView.setText(str);
        }



        ArrayAdapter<String> routeadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, rlist);
        route.setAdapter(routeadapter);
        route.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence p, int start, int before, int count) {
                route.showDropDown();
            }
            @Override
            public void beforeTextChanged(CharSequence p, int start, int count, int after) {
                System.out.println("++++++change here");
                // routeadapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable p) {

            }
        });
        route.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                route.showDropDown();
                route.requestFocus();
                // routeadapter.notifyDataSetChanged();
                return false;
            }
        });
        route.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                r10 = String.valueOf(parent.getItemAtPosition(position));
                System.out.print("\nselect"+r10+"position"+position);
                //sp3.setTop(position);
                //   sp3.setSelection(position);
                System.out.print("\nset...");
                System.out.print("\nrlist..."+rlist);
                System.out.print("\nx..."+rlist.indexOf(r10));
                int x = rlist.indexOf(r10);
                int y=routelist.indexOf(r10);
                System.out.print("\n------>index is "+x);
                System.out.print("\nsettext..."+rlist.get(x));
                System.out.print("\nx..."+x);
                System.out.println("routelist....."+routelist);
                System.out.println("routeid....."+routeid);
                System.out.print("\ny..."+y);
                System.out.print("\nrouteid would be..."+routeid.get(y));
                System.out.print("\n\n\n");
                txtView.setText(rlist.get(x));
                db=getActivity().openOrCreateDatabase("PKMDB", Context.MODE_PRIVATE, null);
                db.execSQL(" CREATE TABLE IF NOT EXISTS rid(route VARCHAR)");
                db.execSQL("DELETE FROM rid  ");
                db.execSQL("INSERT INTO rid( route , routeid )" +" VALUES('"+  rlist.get(x) +"','"+ routeid.get(y)+"');");
                /*if (x>0)
                    r10a = rid.get(x);*/




            }


        });

        sr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button=1;
                Intent loginIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(loginIntent);
               // new onsubmit(getActivity());
                //new submitfeedback(getActivity());
            }
        });



        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button=3;
                Intent loginIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(loginIntent);



//                Intent playvideo = new Intent(getActivity(), playvideo.class);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//                datetime = sdf.format(new Date());
//
//                GPSTracker gps = new GPSTracker(getActivity());
//                if(gps.canGetLocation()){
//                    latitude = gps.getLatitude();
//                    longitude = gps.getLongitude();
//                    if(latitude == 0.0 ){
//                        Toast.makeText(getContext(), "Wait for location and try again",
//                                Toast.LENGTH_SHORT).show();
//
//                    }
//                    else {
//
//
//
//                        Bundle b = new Bundle();
//                        b.putString("start", datetime);
//                        b.putString("phone", phone);
//                        b.putDouble("lng",longitude );
//                        b.putDouble("lat", latitude);
//                        playvideo.putExtras(b);
//                        System.out.println(b);
//
//
//                        getActivity().startActivity(playvideo);
//                    }
//                }else{
//                    gps.showSettingsAlert();
//                }




            }
        });
        ro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button=4;
                Intent loginIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(loginIntent);


//                Fragment fragment = null;
//                fragment = new registeroutlet();
//                if (fragment != null) {
//                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, fragment);
//                    mainscreen.navItemIndex=3;
//                    ft.commit();
//                }
//
//                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);


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
