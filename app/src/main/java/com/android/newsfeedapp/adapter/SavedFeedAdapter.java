package com.android.newsfeedapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.newsfeedapp.Global;
import com.android.newsfeedapp.R;
import com.android.newsfeedapp.appinterface.RecyclerViewClickListener;
import com.android.newsfeedapp.model.NewsFeed;

import java.util.ArrayList;

public class SavedFeedAdapter extends RecyclerView.Adapter<SavedFeedAdapter.MyViewHolder> {
    private ArrayList listNewsFeed;
    private static RecyclerViewClickListener saveButtonListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView description;
        public ImageButton save;
        public LinearLayout itemRow;

        public MyViewHolder(View v) {
            super(v);
            save = (ImageButton) v.findViewById(R.id.iv_save);
            title = (TextView) v.findViewById(R.id.tv_title);
            description = (TextView) v.findViewById(R.id.tv_description);
            itemRow= (LinearLayout) v.findViewById(R.id.ll_item_row);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SavedFeedAdapter(ArrayList<NewsFeed> newsFeed, RecyclerViewClickListener itemClickListener) {
        listNewsFeed = newsFeed;
        saveButtonListener = itemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SavedFeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_feed_list_row, parent, false);
        MyViewHolder vh = new MyViewHolder(itemView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        NewsFeed newsFeed = (NewsFeed) listNewsFeed.get(position);
        holder.title.setText(newsFeed.getTitle());
        holder.description.setText(newsFeed.getAuthor());
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonListener.recyclerViewListClicked(view, position , Global.RECYCLERVIEW_ITEM_SAVE);
            }
        });

        holder.itemRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonListener.recyclerViewListClicked(view, position, Global.RECYCLERVIEW_ID_ROW);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listNewsFeed.size();
    }
}