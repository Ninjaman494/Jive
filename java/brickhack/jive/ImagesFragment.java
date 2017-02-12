package brickhack.jive;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagesFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "IMAGES";

    private ArrayList<String> images;

    public ImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param images Parameter 1.
     * @return A new instance of fragment ImagesFragment.
     */
    public static ImagesFragment newInstance(ArrayList<String> images) {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            images = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        /*ViewPager viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ImageAdapter(images,null,R.layout.image_item));*/
       /* for(String i:images){
            LinearLayout l = (LinearLayout)view.findViewById(R.id.image_container);
            View v = inflater.inflate(R.layout.image_item,l,false);
            TextView t = (TextView)v.findViewById(R.id.image);
            t.setText(i);
            l.addView(v);
        }*/

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.image_container);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(new ImageAdapter(images,inflater,R.layout.image_item));
        return view;
    }

}

class ImageAdapter extends RecyclerView.Adapter {
    ArrayList<String> images;
    LayoutInflater inflater;
    int layoutResourceId;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public ImageAdapter(ArrayList<String> images,LayoutInflater inflater,int layoutResourceId) {
        this.inflater = inflater;
        this.layoutResourceId = layoutResourceId;
        this.images = images;
    }
/*

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        //System.out.println(view == object);
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater i = (LayoutInflater)container.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        View itemView = i.inflate(R.layout.image_item,null);

        TextView name = (TextView)itemView.findViewById(R.id.image);
        name.setText(images.get(position));
        ((ViewPager)container).addView(itemView,0);
        System.out.println(container.getChildAt(0));
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        TextView tv = (TextView)v.findViewById(R.id.image);

        RecyclerView.ViewHolder vh = new ViewHolder(tv);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).mTextView.setText(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
