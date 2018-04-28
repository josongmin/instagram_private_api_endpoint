package com.studio1221.instagram_api_manager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jo on 2017-11-16.
 */

public class InstaUsers {
    /**인스타에서 주는 카운트. 실제 수와 조금 다름*/
    public int countForDisplay = 0;
    public Map<String, InstaUser> mapUsers = new HashMap<>();
    public List<InstaUser> listUsers = new ArrayList<>();

    public boolean hasNextPage = false;
    public String endCursor;

}
