package com.mogli.coronavirustracker;

import android.util.Log;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class QueryUtils {
    private QueryUtils(){

    }
    private static int totalInfected = 0;
    private static int totalD = 0;
    private static int totalRecovered = 0;

    public static int getTotalInfected() {
        return totalInfected;
    }

    public static int getTotalD() {
        return totalD;
    }

    public static int getTotalRecovered() {
        return totalRecovered;
    }

    public static void resetStatic(){
        totalInfected = 0;
        totalD = 0;
        totalRecovered = 0;
    }

    public static final String LOG_TAG = CoronaVirusTracker.class.getName();


    public static List<Location> fetchData(String infectedCasesURL, String deathsCasesURL, String recoveredCasesURL){
        URL iUrl = createURL(infectedCasesURL);
        URL dUrl = createURL(deathsCasesURL);
        URL rUrl = createURL(recoveredCasesURL);
        List<String[]> iDataResponse = null;
        List<String[]> dDataResponse = null;
        List<String[]> rDataResponse = null;
        try{
            iDataResponse = makeHttpRequest(iUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem making the HTTP request.", e);
        }
        try{
            dDataResponse = makeHttpRequest(dUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem making the HTTP request.", e);
        }
        try{
            rDataResponse = makeHttpRequest(rUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem making the HTTP request.", e);
        }
        List<Location> locations= extractFeaturesFromData(iDataResponse,dDataResponse,rDataResponse);
        return locations;
    }

    private static URL createURL(String stringURL){
        URL url = null;
        try{
            url = new URL(stringURL);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the URL",e);
        }
        return url;
    }

    private static List<String[]> makeHttpRequest(URL url) throws IOException {
        List<String[]> dataResponse = null;
        if(url == null){
            return  dataResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();


            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                dataResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error response code" + urlConnection.getResponseCode());
            }

        }catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving the COVID-19 csv results",e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return dataResponse;
    }

    private static List<String[]> readFromStream(InputStream inputStream) throws IOException {
        List<String[]>records = null;
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            CSVReader csvReader = new CSVReader(inputStreamReader);

            records = csvReader.readAll();
        }
        return records;
    }

    private static List<Location> extractFeaturesFromData(List<String[]> iDataResponse,List<String[]> dDataResponse,List<String[]> rDataResponse) {
        List<Location>locations = new ArrayList<>();
        if(iDataResponse!= null && dDataResponse!= null && rDataResponse!= null){
            Iterator<String[]>iIterator = iDataResponse.iterator();
            Iterator<String[]>dIterator = dDataResponse.iterator();
            Iterator<String[]>rIterator = rDataResponse.iterator();
            iIterator.next();
            dIterator.next();
            rIterator.next();
            while (iIterator.hasNext() && dIterator.hasNext() && rIterator.hasNext()){
                String[]iRecord = iIterator.next();
                String[]dRecord = dIterator.next();
                String[]rRecord = rIterator.next();
                String country = iRecord[1];
                String state = iRecord[0];
                int currentDayLocCases = Integer.parseInt(iRecord[iRecord.length - 1]);
                int prevDayLocCases = Integer.parseInt(iRecord[iRecord.length - 2]);
                int locD = Integer.parseInt(dRecord[dRecord.length - 1]);
                int locRecovered = Integer.parseInt(rRecord[rRecord.length - 1]);
                int diffFromprevDay = currentDayLocCases - prevDayLocCases;
                totalInfected += currentDayLocCases;
                totalD += locD;
                totalRecovered += locRecovered;
                Log.v("Values Check","Province: "+ state + "  Country: " + country + "  Ncases: " + currentDayLocCases);
                locations.add(new Location(country,state,currentDayLocCases,locD,locRecovered,diffFromprevDay));
            }
        }
        return  locations;
    }
}
