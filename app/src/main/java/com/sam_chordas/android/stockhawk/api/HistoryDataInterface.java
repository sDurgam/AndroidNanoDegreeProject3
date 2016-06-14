package com.sam_chordas.android.stockhawk.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by durga on 6/12/16.
 */
public interface HistoryDataInterface
{
    @GET("v1/public/yql??&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    //https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%3D'YHOO'%20and%20startDate%3D'2016-05-09'%20and%20endDate%3D'2016-06-08'&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=
    Call<Object> GetHistoricData(@Query("q") String q);
}
