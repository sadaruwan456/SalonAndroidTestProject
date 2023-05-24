package com.dilanka456.myprojectcustomer10.directionsLib;

import com.dilanka456.myprojectcustomer10.pojo.mapDistanceObj;
import com.dilanka456.myprojectcustomer10.pojo.mapTimeObj;

/**
 * Created by Vishal on 10/20/2018.
 */

public interface TaskLoadedCallback {
    void onTaskDone(Object... values);
    void onDistanceTaskDone(mapDistanceObj distance);
    void onTimeTaskDone(mapTimeObj time);
}
