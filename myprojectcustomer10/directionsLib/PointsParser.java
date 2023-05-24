package com.dilanka456.myprojectcustomer10.directionsLib;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.dilanka456.myprojectcustomer10.pojo.mapDistanceObj;
import com.dilanka456.myprojectcustomer10.pojo.mapTimeObj;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vishal on 10/20/2018.
 */

public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    TaskLoadedCallback taskCallback;
    String directionMode = "driving";

    private String duration="";
    private mapDistanceObj distance;
    private mapTimeObj timeObj;

    public PointsParser(FetchURL mContext, String directionMode) {
        this.taskCallback = (TaskLoadedCallback) mContext;
        this.directionMode = directionMode;
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("mylog", jsonData[0].toString());
            DataParser parser = new DataParser();
            Log.d("mylog", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            distance = parser.parseDistance(jObject);
            timeObj = parser.parseTime(jObject);
            Log.d("mylog", "Executing routes");
            Log.d("mylog", routes.toString());

//            //get Time
//            JSONObject durationObject = jObject.getJSONObject("duration");
//              duration = durationObject.getString("text");
            duration="0";

        } catch (Exception e) {
            Log.d("mylog", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;
        // Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();
            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);
            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }
            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            if (directionMode.equalsIgnoreCase("walking")) {
                lineOptions.width(10);
                lineOptions.color(Color.MAGENTA);
            } else {
                lineOptions.width(20);
                lineOptions.color(Color.BLUE);
            }
            Log.d("mylog", "onPostExecute lineoptions decoded");
        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            //mMap.addPolyline(lineOptions);
            taskCallback.onTaskDone(lineOptions);

        } else {
            Log.d("mylog", "without Polylines drawn");
        }
        if (distance != null) {
            //mMap.addPolyline(lineOptions);
            taskCallback.onDistanceTaskDone(distance);
        } else {
            Log.d("mylog", "Error : Distance not Found !");
        }
        if (timeObj != null) {
            //mMap.addPolyline(lineOptions);
            taskCallback.onTimeTaskDone(timeObj);
        } else {
            Log.d("mylog", "Error : Time not found !");
        }
    }
}
