package com.studio1221.instagram_api_manager.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josong on 2017-10-27.
 */

public class WorkingDurationUtil {
    private static Map<Integer, Long> mapItem = new HashMap<>();

    public final static long getDifferentMillsFromLastCall(int id){
        long callMills = System.currentTimeMillis();
        if(mapItem.containsKey(id)){
            long lastCallMills = mapItem.get(id);
            mapItem.put(id, callMills);
            return callMills - lastCallMills;
        }else{
            mapItem.put(id, callMills);
            return 0;
        }
    }
}
