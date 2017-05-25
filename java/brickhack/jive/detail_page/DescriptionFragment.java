package brickhack.jive.detail_page;


import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import brickhack.jive.R;
import ninjaman494.expandabletextview.ExpandableTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescriptionFragment extends Fragment {
    private static final String ARG_PARAM1 = "blurb";
    private static final String ARG_PARAM2 = "desp";

    private String blurb;
    private String desp;
    private TextView blurbView;
    private TextView despView;


    public DescriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param blurb Parameter 1.
     * @param desp Parameter 2.
     * @return A new instance of fragment DescriptionFragment.
     */
    public static DescriptionFragment newInstance(String blurb, String desp) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, blurb);
        args.putString(ARG_PARAM2, desp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            blurb = getArguments().getString(ARG_PARAM1);
            desp = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_description, container, false);
        final ExpandableTextView etv = (ExpandableTextView)root.findViewById(R.id.etv);
        blurbView = etv.getBlurbView();
        despView = etv.getDespView();
        ImageButton btn =  (ImageButton)root.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etv.toggle();
                if(etv.isExpanded()) {
                    ((ImageButton) v).setImageResource(R.drawable.ic_keyboard_arrow_up_grey_24dp);
                }
                else{
                    ((ImageButton) v).setImageResource(R.drawable.ic_keyboard_arrow_down_grey_24dp);
                }
            }
        });
        etv.setAnimSpeed(150);

        blurbView.setText(blurb);
        blurbView.setTextSize(16);
        blurbView.setTypeface(null, Typeface.BOLD);
        blurbView.setGravity(Gravity.CENTER);

        despView.setText(desp);
        despView.setTextSize(16);
        return root;
    }

}
