package brickhack.jive;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendingEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendingEventsFragment extends Fragment {
    private static final String ARG_PARAM1 = "EVENTS";
    private static final String ARG_PARAM2 = "DATES";
    private static final String ARG_PARAM3 = "HOURS";
    private static final String ARG_PARAM4 = "DESPS";

    private ArrayList<String> names;
    private ArrayList<String> dates;
    private ArrayList<String> hours;
    private ArrayList<String> desps;


    public AttendingEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AttendingEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendingEventsFragment newInstance(ArrayList names,ArrayList dates,ArrayList hours,ArrayList desps) {
        AttendingEventsFragment fragment = new AttendingEventsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, names);
        args.putStringArrayList(ARG_PARAM2, dates);
        args.putStringArrayList(ARG_PARAM3, hours);
        args.putStringArrayList(ARG_PARAM4, desps);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_attending_events, container, false);
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
                String key = ((MainActivity) getActivity()).requestKey(name.getText().toString());
                intent.putExtra("key",key);
                intent.putExtra("date",dates.get(i));
                intent.putExtra("hour",hours.get(i));
                intent.putExtra("desp",desps.get(i));

                double[] coords = ((MainActivity) getActivity()).requestCoords(key);
                intent.putExtra("coords",coords);
                startActivity(intent);

            }
        });
        System.out.println("onCreateView: "+names.size());
        return view;
    }

}
