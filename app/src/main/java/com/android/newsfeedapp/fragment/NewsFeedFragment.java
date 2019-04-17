package com.android.newsfeedapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.android.newsfeedapp.adapter.FeedAdapter;
import com.android.newsfeedapp.appinterface.RecyclerViewClickListener;
import com.android.newsfeedapp.model.NewsFeed;
import com.android.newsfeedapp.storage.AppStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NewsFeedFragment extends Fragment implements RecyclerViewClickListener {

    private ArrayList listNewsFeed;
    private AppStorage appStorage;
    private View snackBarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

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

    private String newsFeedUrl = "http://webhose.io/filterWebContent?token=" + Global.NEWSFEED_TOKEN + "&format=json&sort=crawled&q=";


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

        listNewsFeed = appStorage.getNewsFeedListFromStorage();
        progressBar.setVisibility(View.VISIBLE);

        if (listNewsFeed == null) {

            //make an async request to get teh news feed
            try {
                new GetNewsFeed().execute(getEncodeApiUrl("Tesla"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.d("appDebug", "Loading feed from network call");

        } else {
            Log.d("appDebug", "Loading feed from storage");

            loadDataInListView();
        }
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
            Toast.makeText(getContext(), "Item Saved ", Toast.LENGTH_SHORT).show();
            appStorage.addNewsFeedToStorage(newsFeed);
            getActivity().recreate();
        }
    }

    public class GetNewsFeed extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... params) {
            Log.d("appDebug", "Loading data for new url --> " + params[0]);
            listNewsFeed = new ArrayList();

            try {
                return makeApiRequest(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {

            //Log.d("debug", "Api Response --> "+result);

            if (result != null) {
                try {

                    JSONObject jsonResponse = new JSONObject(result);

                    JSONArray feedArray = jsonResponse.getJSONArray("posts");

                    for (int i = 0; i < feedArray.length(); i++) {
                        JSONObject feed = feedArray.getJSONObject(i);
                        String uuid = feed.getString("uuid");
                        String title = feed.getString("title");
                        String url = feed.getString("url");
                        String author = feed.getString("author");

                        System.out.println("uuid -- > " + uuid);
                        System.out.println("title -- > " + title);
                        System.out.println("url -- > " + url);

                        if (title != null && author != null && !title.isEmpty() && !author.isEmpty()) {
                            listNewsFeed.add(new NewsFeed(uuid, title, author, url));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loadDataInListView();

                //store newsfeed list in storage
                appStorage.setNewsFeedListInStorage(listNewsFeed);
            } else {
                Toast.makeText(getContext(), "News Feed API Error ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadDataInListView() {
        progressBar.setVisibility(View.GONE);
        mAdapter = new FeedAdapter(listNewsFeed, imageButtonClickListener);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);

    }

    public static String makeApiRequest(String apiUrl) throws IOException {
//        System.out.println("Receiving New Moon Data from API");
//        String apiUrl = "https://api.usno.navy.mil/moon/phase?date=" + date + "&loc=" + URLEncoder.encode(location, "UTF-8") + "&nump=6";
        System.out.println(apiUrl);
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        //System.out.println(content);
        in.close();

        return content.toString();
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
        SearchView searchView = new SearchView(((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQuery) {
                Toast.makeText(getContext(), "Loading feeds for new search query : " + searchQuery, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);

                try {
                    new GetNewsFeed().execute(getEncodeApiUrl(searchQuery));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );
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
                .setMessage("Authors name : Amit Kumar \n\n Activity Version : News Feed Activity \n\n Usage of this Activity\n\n Click on the search icon on the toolbar and enter the search criteria for the News feed, that you would like to read about. Once search criteria is submitted, the list will populate with feed entries relevant to your search criteria. Tap on the entry to load the article or click on save icon for later reading. \n\n All saved feed can be accessed under the Saved tab of this activity.")

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

    private String getEncodeApiUrl(String searchQuery) throws UnsupportedEncodingException {
        showSnackBar("Loading feed for search query : " + searchQuery);
        return newsFeedUrl + URLEncoder.encode(searchQuery, "UTF-8");
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}