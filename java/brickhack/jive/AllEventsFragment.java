package brickhack.jive;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllEventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> names;
    private ArrayList<String> descriptions;
    private ArrayList<String> hours;


    public AllEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllEventsFragment newInstance(String param1, String param2) {
        AllEventsFragment fragment = new AllEventsFragment();
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
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);
        names = getNames();
        descriptions = getDates();
        hours = getHours();

        ListView eventsList = (ListView)view.findViewById(R.id.events_list);
        eventsList.setAdapter(new VocabAdapter(names,descriptions,hours,inflater,R.layout.event_item));
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView name = (TextView)view.findViewById(R.id.name);
                System.out.println("you clicked:"+name.getText());

                Intent intent = new Intent(getActivity(),EventDetailsActivity.class);
                intent.putExtra("name",name.getText());
                startActivity(intent);

            }
        });

        return view;


    }

    private ArrayList<String> getNames(){
        ArrayList<String> names = new ArrayList<>();
        names.add("BrickHack");
        names.add("SSE Meeting");
        names.add("Hockey Game");

        return names;
    }

    private ArrayList<String> getDates(){
        ArrayList<String> desp = new ArrayList<>();
        desp.add("2/11");
        desp.add("2/12");
        desp.add("2/13");

        return desp;
    }

    private ArrayList<String> getHours(){
        ArrayList<String> hours = new ArrayList<>();
        hours.add("11:00 AM - 5:30 PM");
        hours.add("7:00 PM - 9:00 PM");
        hours.add("6:00 PM - 10:00 PM");

        return hours;
    }
}

class VocabAdapter extends BaseAdapter {
    ArrayList<String> names, descriptions,hours;
    LayoutInflater inflater;
    int layoutResourceId;
    public VocabAdapter(ArrayList<String> names, ArrayList<String> descriptions,ArrayList<String> hours, LayoutInflater inflater,int layoutResourceId) {
        this.inflater = inflater;
        this.layoutResourceId = layoutResourceId;
        this.names = names;
        this.descriptions = descriptions;
        this.hours = hours;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView = inflater.inflate(layoutResourceId,parent,false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView description = (TextView) convertView.findViewById(R.id.date);
        TextView hour = (TextView)convertView.findViewById(R.id.hours);
        name.setText(names.get(position));
        description.setText(descriptions.get(position));
        hour.setText(hours.get(position));
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
        return names.size();
    }
}
