package com.example.rohithreddy.hkwikmint;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rohithreddy.hkwikmint.ErrorcodeFragment.OnListFragmentInteractionListener;
import com.example.rohithreddy.hkwikmint.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MapHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MapHistoryRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private List<mapuser> mapuserList;


    public MapHistoryRecyclerViewAdapter(List<mapuser> MapuserList, OnListFragmentInteractionListener listener) {
        mapuserList = MapuserList;
        System.out.println(MapuserList);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_maps_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.mItem = mValues.get(position);
        holder.outletname.setText(mapuserList.get(position).getOutletname());
        holder.username.setText(mapuserList.get(position).getUsername());
        holder.phonenum.setText(mapuserList.get(position).getPhonenumber());

        holder.mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    System.out.println(mapuserList.get(position).getPhonenumber());
                   // mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mapuserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View v;
        public final TextView outletname,username,phonenum;
        public final Button mapview;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            v = view;
             outletname=(TextView) v.findViewById(R.id.outletname);
             username=(TextView) v.findViewById(R.id.username);
             phonenum=(TextView) v.findViewById(R.id.phonenum);
             mapview=(Button) v.findViewById(R.id.mapview);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + outletname.getText() + "'";
        }
    }
}
