package com.example.prasad.wirecamptask;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    Activity activity;
    Context context;
    private ArrayList<DataModel> mDataModel = new ArrayList<>();

    public RecyclerAdapter (Context mAct, Activity activity) {
        this.context = mAct;
        this.activity = activity;
        mLayoutInflater = LayoutInflater.from(mAct);
    }

    public void setRecyclerAdapter  (ArrayList<DataModel> mDataModel) {
        this.mDataModel = mDataModel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.recycler_items, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        final DataModel dataModel = mDataModel.get(i);

        myViewHolder.mTitle.setText(dataModel.getTitle());
        myViewHolder.mDescriptions.setText(dataModel.getDescriptions());
        System.out.println("dataaa"+dataModel.getThumbnail());
        if (dataModel.getThumbnail()== null )
        {
            myViewHolder.mImageView.setBackgroundResource(R.drawable.imgplace);
        }
        else {
            Picasso.with(context).load(dataModel.getThumbnail()).error(R.drawable.imgplace)
                    .into(myViewHolder.mImageView);
        }
        myViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                String url = "https://en.wikipedia.org/wiki/";
                String title = myViewHolder.mTitle.getText().toString();
                customTabsIntent.launchUrl(activity, Uri.parse(url+title));
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTitle, mDescriptions;
        ImageView mImageView;
        CardView mCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.title);
            mDescriptions = (TextView) itemView.findViewById(R.id.descriptions);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);

        }
    }
}
