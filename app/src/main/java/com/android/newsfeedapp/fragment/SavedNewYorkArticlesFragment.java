package com.android.newsfeedapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.newsfeedapp.Global;
import com.android.newsfeedapp.R;
import com.android.newsfeedapp.activity.WebActivity;
import com.android.newsfeedapp.adapter.SavedFeedAdapter;
import com.android.newsfeedapp.appinterface.RecyclerViewClickListener;
import com.android.newsfeedapp.model.NewsFeed;
import com.android.newsfeedapp.storage.AppStorage;

import java.util.ArrayList;

public class SavedNewYorkArticlesFragment extends Fragment implements RecyclerViewClickListener {

    private ArrayList listNewsFeed;
    private AppStorage appStorage;
    private View snackBarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_news_feed, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_main);
        snackBarView = (View) view.findViewById(R.id.context_view);

        return view;
    }

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;
    private RecyclerViewClickListener imageButtonClickListener;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        appStorage = AppStorage.getInstance(getActivity());

        imageButtonClickListener = this;
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        loadDataInListView();

    }

    @Override
    public void recyclerViewListClicked(View v, int position, int itemId) {

        NewsFeed newsFeed = (NewsFeed) listNewsFeed.get(position);

        if (itemId == Global.RECYCLERVIEW_ID_ROW) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra(Global.INTENT_WEBVIEW_URL, newsFeed.getUrl());
            intent.putExtra(Global.INTENT_WEBVIEW_TITLE, newsFeed.getTitle());
            startActivity(intent);

        } else {
            Toast.makeText(getContext(), "Item Deleted ", Toast.LENGTH_SHORT).show();

            appStorage.deleteNewYorkArticlesFromStorage(newsFeed);
            loadDataInListView();

        }
    }

    private void loadDataInListView() {
        progressBar.setVisibility(View.VISIBLE);

        listNewsFeed = appStorage.getSavedNewYorkArticlesListInStorage();

        if (listNewsFeed != null) {

            Log.d("appDebug", "Loading feed from storage");
            mAdapter = new SavedFeedAdapter(listNewsFeed, imageButtonClickListener);
            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
        }
        progressBar.setVisibility(View.GONE);

    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_help:
                showAlertDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlertDialog() {

        new AlertDialog.Builder(getActivity())
                .setTitle("Help")
                .setMessage("Authors name : Shivam Anghan \n\n Activity Version : NewYork Articles Activity \n\n Usage of this Activity\n\n Click on the search icon on the toolbar and enter the search criteria for the News feed, that you would like to read about. Once search criteria is submitted, the list will populate with feed entries relevant to your search criteria. Tap on the entry to load the article or click on save icon for later reading. \n\n All saved feed can be accessed under the Saved tab of this activity.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}