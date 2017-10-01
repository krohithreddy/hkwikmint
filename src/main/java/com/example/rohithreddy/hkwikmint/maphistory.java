package com.example.rohithreddy.hkwikmint;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rohithreddy.hkwikmint.dummy.DummyContent;
import com.example.rohithreddy.hkwikmint.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class maphistory extends Fragment {
    private List<mapuser> mapuserList;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public maphistory() {
    }
    MainActivity mainscreen;
    Register userdata;
    MyItemRecyclerViewAdapter adapter;
    private Cursor c;

    private SQLiteDatabase db;
    public  void GetList(String searchword){
        mapuserList.clear();
        if(searchword=="all"||searchword.length()==0||searchword=="all1")
            c = db.rawQuery("SELECT * FROM mapusers  ", null);
        else{
            System.out.println(searchword);
            c = db.rawQuery("SELECT * FROM mapusers where (username like '%"+searchword+"%' or outletname like '%"+searchword+"%' or phonen like '%"+searchword+"%')", null);
            //Toast.makeText(getContext(),searchword, Toast.LENGTH_LONG).show();
        }
        if (!(c.moveToFirst()) || c.getCount() == 0){
            if(searchword=="all")
                System.out.println("its all");
            else
                adapter.notifyDataSetChanged();
            System.out.println("no results found");
        }
        else {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                System.out.println(c.getColumnNames());

                int totalColumn = c.getColumnCount();
                for (int i = 0; i < totalColumn; i++) {
                    if (c.getColumnName(i) != null) {
                        try {
                            if (c.getString(i) != null) {
                                System.out.println("here i is"+i);
                                System.out.println(c.getColumnName(i));
                                System.out.println(c.getString(i));
                            } else {
                                System.out.println("its else" + c.getColumnName(i));
                            }
                        } catch (Exception e) {

                        }
                    }
                    System.out.println("one step over");
                }
                mapuserList.add(0,new mapuser(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(5),c.getString(6)));
                System.out.println("mapuserlist---"+mapuserList);
                c.moveToNext();
            }
            if(searchword=="all")
                System.out.println("its all");
            else
                adapter.notifyDataSetChanged();
            c.close();
        }
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static maphistory newInstance(int columnCount) {
        maphistory fragment = new maphistory();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

            final Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        final EditText search = (EditText) view.findViewById(R.id.outletname);
        mapuserList = new ArrayList<>();
        mapuserList.clear();
        db=getActivity().openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mapuserList.clear();
                GetList("all1");
                swipeLayout.setRefreshing(false);
            }
        });
        GetList("all");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String x =search.getText().toString();
                System.out.println(x);
                GetList(x);
                System.out.println(x);
                // /search.setError(null);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       // mapuserList.add(0,new mapuser(14,"rggdfgo","gndsgng","gndsgng","gndsgng","gndsgng"));
       // mapuserList.add(0,new mapuser(12,"hhhhhhhh","hhhhhhh","hhhhhhh","hhh","gndsgng"));
        adapter =new MyItemRecyclerViewAdapter(mapuserList, getActivity());
        recyclerView.setAdapter(adapter);
        System.out.println("======================"+mapuserList);
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                mapuser user = mapuserList.get(position);
//                Toast.makeText(getContext(), mapuserList.get(position).getPhonenumber() + " is selected!", Toast.LENGTH_SHORT).show();
//                Intent loginIntent = new Intent(context, Scan.class);
//
//                context.startActivity(loginIntent);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

                return view;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
