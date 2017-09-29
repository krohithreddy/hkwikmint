package com.example.rohithreddy.hkwikmint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        System.out.println("0000000000000000000");
        // Set the adapter

            System.out.println("55555555555");
            final Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        mapuserList = new ArrayList<>();
        mapuserList.add(0,new mapuser(14,"rggdfgo","gndsgng","gndsgng","gndsgng","gndsgng"));
        mapuserList.add(0,new mapuser(12,"hhhhhhhh","hhhhhhh","hhhhhhh","hhh","gndsgng"));
        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(mapuserList, getActivity()));
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
