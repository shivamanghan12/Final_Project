package com.android.newsfeedapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.Xml;
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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WebsterFragment extends Fragment implements RecyclerViewClickListener {

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

    private String newsFeedUrl = "https://www.dictionaryapi.com/api/v1/references/sd3/xml/";
    private String websterAPIKey = "?key=" + Global.WEBSTER_API_KEY;
    private static String searchQuery;


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

        listNewsFeed = appStorage.getWebsterListFromStorage();
        progressBar.setVisibility(View.VISIBLE);

        if (listNewsFeed == null) {

            //make an async request to get teh news feed
            try {
                new GetNewsFeed().execute(getEncodeApiUrl("Pasta"));
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

        if (itemId == Global.RECYCLERVIEW_ITEM_SAVE) {
            Toast.makeText(getContext(), "Definition Saved ", Toast.LENGTH_SHORT).show();
            appStorage.addWebsterToStorage(newsFeed);
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

            Log.d("appDebug", "Api Response --> " + result);

            listNewsFeed = parseXML(result);

            loadDataInListView();

            //store newsfeed list in storage
            appStorage.setWebsterListInStorage(listNewsFeed);

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
                .setMessage("Authors name : Heramb Bendale \n\n Activity Version : News Feed Activity \n\n Usage of this Activity\n\n Click on the search icon on the toolbar and enter the search criteria for the News feed, that you would like to read about. Once search criteria is submitted, the list will populate with feed entries relevant to your search criteria. Tap on the entry to load the article or click on save icon for later reading. \n\n All saved feed can be accessed under the Saved tab of this activity.")

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

    private String getEncodeApiUrl(String query) throws UnsupportedEncodingException {
        searchQuery = query;
        showSnackBar("Loading feed for search query : " + searchQuery);
        return newsFeedUrl + URLEncoder.encode(searchQuery, "UTF-8") + websterAPIKey;
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private ArrayList<NewsFeed> parseXML(String xmlData) {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = new ByteArrayInputStream(xmlData.getBytes());
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            return processParsing(parser);

        } catch (XmlPullParserException e) {

        } catch (IOException e) {
        }

        return null;
    }

    private ArrayList<NewsFeed> processParsing(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<NewsFeed> listWebsterDefs = new ArrayList<>();
        int eventType = parser.getEventType();
        NewsFeed newsFeed = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            String text = null;
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    Log.d("appDebug", "XmlPullParser.START_TAG --> parser.getName() --> " + tagname);

                    if (tagname.equalsIgnoreCase("dt")) {
                        // create a new instance of employee
                        Log.d("appDebug", "New Instance of Object");
                        newsFeed = new NewsFeed();
                    }
                    break;

                case XmlPullParser.TEXT:
//                    text = parser.getText();
                    if (newsFeed != null) {
                        Log.d("appDebug", "parser.getText() --> " + parser.getText());
                        newsFeed.setTitle(parser.getText().replace(":", ""));
                    }
                    break;

                case XmlPullParser.END_TAG:

                    if (tagname.equalsIgnoreCase("dt")) {
                        Log.d("appDebug", "XmlPullParser.END_TAG --> parser.getName() --> " + tagname);

                        if (newsFeed != null && newsFeed.getTitle() != null && !newsFeed.getTitle().isEmpty()) {

                            String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
                            Log.d("appDebug", "timestamp --> " + timestamp);

                            newsFeed.setId(timestamp);
                            newsFeed.setAuthor(searchQuery);
                            listWebsterDefs.add(newsFeed);
                            newsFeed = null;
                            Log.d("appDebug", "Object added to the list");
                        }
                    }
                    break;

                default:
                    break;
            }
            eventType = parser.next();
        }

        return listWebsterDefs;
    }

    private void printPlayers(ArrayList<NewsFeed> newsFeeds) {
        StringBuilder builder = new StringBuilder();

        for (NewsFeed player : newsFeeds) {
            builder.append(player.getId()).append("\n").
                    append(player.getTitle()).append("\n").
                    append(player.getAuthor()).append("\n\n");
        }

        Log.d("appDebug", builder.toString());
    }


}