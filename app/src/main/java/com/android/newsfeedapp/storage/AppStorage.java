package com.android.newsfeedapp.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.newsfeedapp.model.NewsFeed;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AppStorage {

    public static final String KEY_INITIAL_RUN_TIMESTAMP = "initialRunTimestamp";
    public static final String KEY_LIST_NEWSFEED = "keyListNewsFeed";
    public static final String KEY_LIST_NEWYORKARTICLES = "keyNewYorkArticles";
    public static final String KEY_LIST_NEWSFEED_SAVED = "keyListNewsFeedSaved";
    public static final String KEY_LIST_NEWYORK_SAVED = "keyListNewYorkArticlesSaved";
    public static final String KEY_LIST_WEBSTER = "keyListWebster";
    public static final String KEY_LIST_WEBSTER_SAVED = "keyListNewsFeedSaved";

    private static AppStorage instance = null;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    protected AppStorage(Context c) {

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(c);
    }

    public static AppStorage getInstance(Context ctx) {

        if (instance == null) {
            instance = new AppStorage(ctx);
        }

        return instance;
    }

    public String getStringValueByKey(String key) {
        return sharedPreference.getString(key, null);
    }

    public void setStringValueByKey(String key, String value) {
        editor = sharedPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public Object getObjectValueByKey(String key) {
        Gson gson = new Gson();
        String json = sharedPreference.getString(key, null);
        Object object = gson.fromJson(json, Object.class);

        return object;
    }

    public void setObjectValueByKey(String key, Object value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);

        editor = sharedPreference.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public boolean getBooleanValueByKey(String key) {
        return sharedPreference.getBoolean(key, false);
    }

    public void setBooleanValueByKey(String key, boolean value) {
        editor = sharedPreference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void clearStorage() {
        editor = sharedPreference.edit();
        editor.clear();
        editor.commit();
    }

    public long sizeOfSharedPrefs(){

        byte[] byteArray = null;

        Map<String, ?> allEntries = sharedPreference.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("Shared Pref Keys --> ", entry.getKey());
            try {
                byteArray = entry.getValue().toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return byteArray.length;
    }

    public void setNewsFeedListInStorage(ArrayList<NewsFeed> newsFeedList){

        editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(newsFeedList);
        editor.putString(KEY_LIST_NEWSFEED, json);
        editor.commit();
    }

    public ArrayList getNewsFeedListFromStorage(){

        Gson gson = new Gson();
        String json = sharedPreference.getString(KEY_LIST_NEWSFEED, null);

        Type type = new TypeToken<List<NewsFeed>>(){}.getType();
        ArrayList<NewsFeed> listNewsFeed = gson.fromJson(json, type);

        return  listNewsFeed;
    }

    public void setSavedNewsFeedListInStorage(ArrayList<NewsFeed> newYorkArticlesList){

        editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(newYorkArticlesList);
        editor.putString(KEY_LIST_NEWSFEED_SAVED, json);
        editor.commit();
    }

    public ArrayList getSavedNewsFeedListInStorage(){

        Gson gson = new Gson();
        String json = sharedPreference.getString(KEY_LIST_NEWSFEED_SAVED, null);

        Type type = new TypeToken<List<NewsFeed>>(){}.getType();
        ArrayList<NewsFeed> listNewsFeed = gson.fromJson(json, type);

        return  listNewsFeed;
    }
    
    public void addNewsFeedToStorage(NewsFeed newsFeed){
        
        ArrayList<NewsFeed> savedItemListInStorage= getSavedNewsFeedListInStorage();

        if(savedItemListInStorage == null){
            savedItemListInStorage = new ArrayList<NewsFeed>();
        }

        savedItemListInStorage.add(newsFeed);

        setSavedNewsFeedListInStorage(savedItemListInStorage);
    }

    public void deleteNewsFeedFromStorage(NewsFeed newsFeed){

        ArrayList<NewsFeed> savedItemListInStorage= getSavedNewsFeedListInStorage();
        Log.d("appDebug", "param newsFeed --> "+newsFeed.getId());

        for (NewsFeed feed: savedItemListInStorage) {
            Log.d("appDebug", "Get position --> "+savedItemListInStorage.indexOf(feed));
            Log.d("appDebug", "feed id --> "+feed.getId());


            if (newsFeed.getId().equalsIgnoreCase(feed.getId())){

                savedItemListInStorage.remove(savedItemListInStorage.indexOf(feed));
                Log.d("appDebug", "Match found --> Removed ");

                break;
            }
        }

        setSavedNewsFeedListInStorage(savedItemListInStorage);
    }

    public void setNewYorkArticlesListInStorage(ArrayList<NewsFeed> newYorkArticlesList){

        editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(newYorkArticlesList);
        editor.putString(KEY_LIST_NEWYORKARTICLES, json);
        editor.commit();
    }

    public ArrayList getNewYorkArticlesListFromStorage(){

        Gson gson = new Gson();
        String json = sharedPreference.getString(KEY_LIST_NEWYORKARTICLES, null);

        Type type = new TypeToken<List<NewsFeed>>(){}.getType();
        ArrayList<NewsFeed> listNewsFeed = gson.fromJson(json, type);

        return  listNewsFeed;
    }

    public void setSavedNewYorkArticlesListInStorage(ArrayList<NewsFeed> newYorkArticlesList){

        editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(newYorkArticlesList);
        editor.putString(KEY_LIST_NEWYORK_SAVED, json);
        editor.commit();
    }

    public ArrayList getSavedNewYorkArticlesListInStorage(){

        Gson gson = new Gson();
        String json = sharedPreference.getString(KEY_LIST_NEWYORK_SAVED, null);

        Type type = new TypeToken<List<NewsFeed>>(){}.getType();
        ArrayList<NewsFeed> listNewsFeed = gson.fromJson(json, type);

        return  listNewsFeed;
    }

    public void addNewYorkArticlesToStorage(NewsFeed newsFeed){

        ArrayList<NewsFeed> savedItemListInStorage= getSavedNewYorkArticlesListInStorage();

        if(savedItemListInStorage == null){
            savedItemListInStorage = new ArrayList<NewsFeed>();
        }

        savedItemListInStorage.add(newsFeed);

        setSavedNewYorkArticlesListInStorage(savedItemListInStorage);
    }

    public void deleteNewYorkArticlesFromStorage(NewsFeed newsFeed){

        ArrayList<NewsFeed> savedItemListInStorage= getSavedNewYorkArticlesListInStorage();
        Log.d("appDebug", "param newsFeed --> "+newsFeed.getId());

        for (NewsFeed feed: savedItemListInStorage) {
            Log.d("appDebug", "Get position --> "+savedItemListInStorage.indexOf(feed));
            Log.d("appDebug", "feed id --> "+feed.getId());


            if (newsFeed.getId().equalsIgnoreCase(feed.getId())){

                savedItemListInStorage.remove(savedItemListInStorage.indexOf(feed));
                Log.d("appDebug", "Match found --> Removed ");

                break;
            }
        }

        setSavedNewYorkArticlesListInStorage(savedItemListInStorage);
    }

    public void setWebsterListInStorage(ArrayList<NewsFeed> websterList){

        editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(websterList);
        editor.putString(KEY_LIST_WEBSTER, json);
        editor.commit();
    }

    public ArrayList getWebsterListFromStorage(){

        Gson gson = new Gson();
        String json = sharedPreference.getString(KEY_LIST_WEBSTER, null);

        Type type = new TypeToken<List<NewsFeed>>(){}.getType();
        ArrayList<NewsFeed> listNewsFeed = gson.fromJson(json, type);

        return  listNewsFeed;
    }

    public void setSavedWebsterInStorage(ArrayList<NewsFeed> websterList){

        editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(websterList);
        editor.putString(KEY_LIST_WEBSTER_SAVED, json);
        editor.commit();
    }

    public ArrayList getSavedWebsterInStorage(){

        Gson gson = new Gson();
        String json = sharedPreference.getString(KEY_LIST_WEBSTER_SAVED, null);

        Type type = new TypeToken<List<NewsFeed>>(){}.getType();
        ArrayList<NewsFeed> listNewsFeed = gson.fromJson(json, type);

        return  listNewsFeed;
    }

    public void addWebsterToStorage(NewsFeed newsFeed){

        ArrayList<NewsFeed> savedItemListInStorage= getSavedWebsterInStorage();

        if(savedItemListInStorage == null){
            savedItemListInStorage = new ArrayList<NewsFeed>();
        }

        savedItemListInStorage.add(newsFeed);

        setSavedWebsterInStorage(savedItemListInStorage);
    }

    public void deleteWebsterFromStorage(NewsFeed newsFeed){

        ArrayList<NewsFeed> savedItemListInStorage= getSavedWebsterInStorage();
        Log.d("appDebug", "param newsFeed --> "+newsFeed.getId());

        for (NewsFeed feed: savedItemListInStorage) {
            Log.d("appDebug", "Get position --> "+savedItemListInStorage.indexOf(feed));
            Log.d("appDebug", "feed id --> "+feed.getId());


            if (newsFeed.getId().equalsIgnoreCase(feed.getId())){

                savedItemListInStorage.remove(savedItemListInStorage.indexOf(feed));
                Log.d("appDebug", "Match found --> Removed ");

                break;
            }
        }

        setSavedWebsterInStorage(savedItemListInStorage);
    }
}
