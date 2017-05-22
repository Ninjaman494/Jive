package brickhack.jive;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DTLFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DTLFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "date";
    private static final String ARG_PARAM2 = "time";
    private static final String ARG_PARAM3 = "location";

    // TODO: Rename and change types of parameters
    private String date;
    private String time;
    private String location;


    public DTLFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date Parameter 1.
     * @param time Parameter 2.
     * @return A new instance of fragment DTLFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DTLFragment newInstance(String date, String time, String location) {
        DTLFragment fragment = new DTLFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, date);
        args.putString(ARG_PARAM2, time);
        args.putString(ARG_PARAM3, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = getArguments().getString(ARG_PARAM1);
            time = getArguments().getString(ARG_PARAM2);
            location = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_dtl, container, false);
        ListView DTL_list = (ListView)view.findViewById(R.id.DTL_list);
        DTL_list.setAdapter(new DTLAdapter(date,time,location,"Rochester Institute of Technology",inflater,R.layout.dtl_item));
        return view;
    }
}

class DTLAdapter extends BaseAdapter {
    String date;
    String time;
    String location;
    String college;
    LayoutInflater inflater;
    int layoutResourceId;
    public DTLAdapter(String date,String time,String location,String college,LayoutInflater inflater,int layoutResourceId) {
        this.inflater = inflater;
        this.layoutResourceId = layoutResourceId;
        this.date = date;
        this.time = time;
        this.location = location;
        this.college = college;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView = inflater.inflate(layoutResourceId,parent,false);
        }

        ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
        TextView mainline = (TextView)convertView.findViewById(R.id.mainline);
        TextView subline = (TextView)convertView.findViewById(R.id.subline);
        if(position == 0) {
            icon.setImageResource(R.drawable.ic_event_black_24dp);
            mainline.setText(date);
            subline.setText(time);
        }
        if(position == 1){
            icon.setImageResource(R.drawable.ic_place_black_24dp);
            mainline.setText(location);
            subline.setText(college);
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount(){
        return 2;
    }
}
