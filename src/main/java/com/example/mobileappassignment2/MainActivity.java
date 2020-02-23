package com.example.mobileappassignment2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.gson.Gson;

import com.example.mobileappassignment2.Adapter.FeedAdapter;
import com.example.mobileappassignment2.Common.HTTPDataHandler;
import com.example.mobileappassignment2.Object.RssObject;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RssObject rssObject;

    private final String RSS_link="https://www.gamblingsites.org/blog/esports/feed/";
    private final String RSS_to_Json_API=" https://api.rss2json.com/v1/api.json?rss_url=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Esports News");
        setSupportActionBar(toolbar);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadRSS();
    }

    private void loadRSS(){
        AsyncTask<String,String,String> loadRSSAsync = new AsyncTask<String, String, String>(){

            ProgressDialog mDialog=new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute(){
                mDialog.setMessage("Wait a moment...");
                mDialog.show();
            }

            @Override
            protected String doInBackground(String...params){
                String result;
                HTTPDataHandler http=new HTTPDataHandler();
                result=http.GetHTTPData(params[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s){
                mDialog.dismiss();
                rssObject=new Gson().fromJson(s,RssObject.class);
                FeedAdapter adapter=new FeedAdapter(rssObject,getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        };

        StringBuilder url_get_data=new StringBuilder(RSS_to_Json_API);
        url_get_data.append(RSS_link);
        loadRSSAsync.execute(url_get_data.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.menu_refresh)
            loadRSS();
        return true;
    }

}
