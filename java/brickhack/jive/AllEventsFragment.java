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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "EVENTS";
    private static final String ARG_PARAM2 = "DATES";
    private static final String ARG_PARAM3 = "HOURS";
    private static final String ARG_PARAM4 = "DESPS";

    private ArrayList<String> names;
    private ArrayList<String> dates;
    private ArrayList<String> hours;
    private ArrayList<String> desps;

    public AllEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param names Parameter 1.
     * @param dates Parameter 2.
     * @return A new instance of fragment AllEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllEventsFragment newInstance(ArrayList names,ArrayList dates,ArrayList hours,ArrayList desps) {
        AllEventsFragment fragment = new AllEventsFragment();
        Bundle args = new Bundle();
        System.out.println("newInstance: "+names.size());
        args.putStringArrayList(ARG_PARAM1, names);
        args.putStringArrayList(ARG_PARAM2, dates);
        args.putStringArrayList(ARG_PARAM3, hours);
        args.putStringArrayList(ARG_PARAM4, desps);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            names = getArguments().getStringArrayList(ARG_PARAM1);
            dates = getArguments().getStringArrayList(ARG_PARAM2);
            hours = getArguments().getStringArrayList(ARG_PARAM3);
            desps = getArguments().getStringArrayList(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);
        /*names = getNames();
        dates = getDates();
        hours = getHours();*/

        ListView eventsList = (ListView)view.findViewById(R.id.events_list);
        System.out.println("beforeAdapter: "+names.size());
        eventsList.setAdapter(new VocabAdapter(names, dates,hours,inflater,R.layout.event_item));
        System.out.println("afterAdapter: "+names.size());
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView name = (TextView)view.findViewById(R.id.name);

                Intent intent = new Intent(getActivity(),EventDetailsActivity.class);
                intent.putExtra("name",names.get(i));
                String key = ((HomePageActivity) getActivity()).requestKey(name.getText().toString());
                intent.putExtra("key",key);
                intent.putExtra("date",dates.get(i));
                intent.putExtra("hour",hours.get(i));
                intent.putExtra("desp",desps.get(i));

                double[] coords = ((HomePageActivity) getActivity()).requestCoords(key);
                intent.putExtra("coords",coords);
                startActivity(intent);

            }
        });
        System.out.println("onCreateView: "+names.size());
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
    ArrayList<String> names, dates,hours;
    LayoutInflater inflater;
    int layoutResourceId;
    public VocabAdapter(ArrayList<String> names, ArrayList<String> dates,ArrayList<String> hours, LayoutInflater inflater,int layoutResourceId) {
        this.inflater = inflater;
        this.layoutResourceId = layoutResourceId;
        this.names = names;
        this.dates = dates;
        this.hours = hours;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        TextView name,description,hour;
        if(convertView==null){
            convertView = inflater.inflate(layoutResourceId,parent,false);
        }
        name = (TextView) convertView.findViewById(R.id.name);
        description = (TextView) convertView.findViewById(R.id.date);
        hour = (TextView)convertView.findViewById(R.id.hours);
        name.setText(names.get(position));
        description.setText(dates.get(position));
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
