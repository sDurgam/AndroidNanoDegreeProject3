package com.sam_chordas.android.stockhawk.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.sam_chordas.android.stockhawk.ui.DisplayStockGraphActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by durga on 6/12/16.
 */
public class GetHistoricDataCall implements Callback<Object>
{
    Context mContext;
    String stockSymbol;

    public GetHistoricDataCall(Context ctx, String sym)
    {
        mContext = ctx;
        stockSymbol = sym;
    }

    public void FetchData()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // prepare call in Retrofit 2.0
        HistoryDataInterface historicDataAPI = retrofit.create(HistoryDataInterface.class);
        String q = "select * from yahoo.finance.historicaldata where symbol = \'" + stockSymbol + "\' and startDate = \'" + "2016-05-09" + "\' and endDate = \'" + "2016-06-08" + "\'";
        Call<Object> call = historicDataAPI.GetHistoricData(q);
        //q=use%20al&env=store://datatables.org/alltableswithkeys#h=select+*+from+yahoo.finance.historicaldata+where+symbol%3D'YHOO'+and+startDate%3D'2016-05-09'+and+endDate%3D'2016-06-08'
        //asynchronous call
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response)
    {
        if(response.body() != null)
        {
            String body = response.body().toString();
            LinkedTreeMap querymap;
            LinkedTreeMap resultsmap;
            ArrayList quotesList;
            //datastructure to save date and close values of each value of the selected stock
            TreeMap<String, Double> datePriceMap = new TreeMap<>();
            JSONObject stockObj;
            Date date;
            String dateStr;
            final SimpleDateFormat inputformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat outputformat = new SimpleDateFormat("EEE, dd MMM yyyy");
            if(response.body() instanceof LinkedTreeMap)
            {
                //new JSONObject(((ArrayList) ((LinkedTreeMap)((LinkedTreeMap)(((LinkedTreeMap)response.body()).get("query"))).get("results")).get("quote")).get(0).toString()).get("Date")
                if(((LinkedTreeMap)response.body()).containsKey("query") && (((LinkedTreeMap)response.body()).get("query") instanceof  LinkedTreeMap))
                {
                    querymap = (LinkedTreeMap)(((LinkedTreeMap)response.body()).get("query"));
                    if(querymap.containsKey("results") && querymap.get("results") instanceof LinkedTreeMap)
                    {
                        resultsmap = (LinkedTreeMap)querymap.get("results");
                        if(resultsmap != null && resultsmap.containsKey("quote") && resultsmap.get("quote") instanceof ArrayList)
                        {
                            quotesList = (ArrayList) resultsmap.get("quote");
                            for(int i =0; i < quotesList.size(); i++)
                            {
                                try
                                {
                                    stockObj = new JSONObject(quotesList.get(i).toString());
                                    try {
                                        date = inputformat.parse(stockObj.getString("Date"));
                                        dateStr = outputformat.format(date);

                                    } catch (java.text.ParseException e)
                                    {
                                        // TODO Auto-generated catch block
                                        dateStr = null;
                                        e.printStackTrace();
                                    }
                                    if(dateStr != null)
                                    {
                                        datePriceMap.put(dateStr, stockObj.getDouble("Close"));
                                    }
                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            //Display data in a chart
                            ((DisplayStockGraphActivity)mContext).openChart(datePriceMap);
                        }
                    }

                }
            }
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t)
    {

    }
}
