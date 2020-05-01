package com.mogli.coronavirustracker;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class LocationLoader extends AsyncTaskLoader {

    private String infectedCasesURL;
    private String deathsCasesURL;
    private String recoveredCasesURL;
    public LocationLoader(Context context , String infectedCasesURL,String deathsCasesURL){
        super(context);
        this.infectedCasesURL = infectedCasesURL;
        this.deathsCasesURL = deathsCasesURL;
    }

    public LocationLoader(Context context , String infectedCasesURL,String deathsCasesURL,String recoveredCasesURL){
        super(context);
        this.infectedCasesURL = infectedCasesURL;
        this.deathsCasesURL = deathsCasesURL;
        this.recoveredCasesURL = recoveredCasesURL;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Location> loadInBackground() {
        if(infectedCasesURL == null || deathsCasesURL == null){
            return  null;
        }
        return QueryUtils.fetchData(infectedCasesURL,deathsCasesURL);
    }
}
