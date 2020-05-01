 package com.mogli.coronavirustracker;

 import android.app.LoaderManager;
 import android.content.Context;
 import android.content.Intent;
 import android.content.Loader;

import android.icu.text.Edits;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

 import androidx.annotation.NonNull;
 import androidx.annotation.Nullable;
 import androidx.appcompat.app.AppCompatActivity;

 import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

 public class CoronaVirusTracker extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Location>> {

     private LocationAdapter adapter;
     private ListView listView;
     private ProgressBar progressBar;
     private TextView totalICases;
     private TextView totalDCases;
     private TextView totalRCases;

     private final static String casesInfectedURL =
             "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
     private final static String casesDeathsURL =
             "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
//     private final static String casesRecoveredURL =
//             "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Recovered.csv";


     private static final int LOCATION_LOADER_ID = 1;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coronavirustracker_activity);



        ArrayList<Location>locations = new ArrayList<>();

        adapter = new LocationAdapter(this,locations);
        adapter.clear();
        listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        totalICases = (TextView)findViewById(R.id.totalICases);
        totalDCases = (TextView)findViewById(R.id.totalDCases);
        totalRCases = (TextView)findViewById(R.id.totalRCases);

        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOCATION_LOADER_ID,null,this);


//        VirusAsyncTask task = new VirusAsyncTask();
//        task.execute(casesInfectedURL,casesDeathsURL,casesRecoveredURL);
    }

    @Override
    protected void onPause(){
         QueryUtils.resetStatic();
         super.onPause();
    }

    @Override
    protected void onResume() {
         QueryUtils.resetStatic();
        super.onResume();
    }

    @Override
    protected void onStop() {
         QueryUtils.resetStatic();
        super.onStop();
    }

     @Override
     public Loader<List<Location>> onCreateLoader(int id, @Nullable Bundle args) {
         return new LocationLoader(this,casesInfectedURL,casesDeathsURL);
     }

     @Override
     public void onLoadFinished(@NonNull Loader<List<Location>> loader, List<Location> data) {
         adapter.clear();
         adapter.addAll(data);
         totalICases.setText(Integer.toString(QueryUtils.getTotalInfected()));
         totalDCases.setText(Integer.toString(QueryUtils.getTotalD()));
         totalRCases.setText(Integer.toString(QueryUtils.getTotalRecovered()));
         progressBar.setVisibility(View.INVISIBLE);
     }

     @Override
     public void onLoaderReset(@NonNull Loader<List<Location>> loader) {
        adapter.clear();
        QueryUtils.resetStatic();
     }

     private class VirusAsyncTask extends AsyncTask<String,Void,List<Location>>{

        @Override
        protected List<Location> doInBackground(String... urls) {
//            Log.v("background","BACKGROUND!!");

            List<Location>locations = QueryUtils.fetchData(urls[0],urls[1],urls[2]);
            return locations;
        }
        @Override
        protected void onPostExecute(List<Location> data){
            adapter.clear();
            adapter.addAll(data);
//            long totalICases = QueryUtils.getTotalInfected();
            totalICases.setText(Integer.toString(QueryUtils.getTotalInfected()));
            totalDCases.setText(Integer.toString(QueryUtils.getTotalD()));
            totalRCases.setText(Integer.toString(QueryUtils.getTotalRecovered()));
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
