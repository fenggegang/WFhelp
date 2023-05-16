package com.fggang.market;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fggang.tools.HttpClientUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ItemRank {
    public static int getItemMax(String itemUrl) {
        Map<String, String> head = new HashMap<>();

        head.put("accept", "application/json");
        head.put("Language", "zh-hans");
        head.put("Platform", "pc");


        String result;
        try {
            result = HttpClientUtils.get("https://api.warframe.market/v1/items/" + itemUrl, null, head);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }


        if (result != null) {
            JSONObject jsonObject = JSON.parseObject(result);


            JSONArray items = jsonObject.getJSONObject("payload")
                    .getJSONObject("item")
                    .getJSONArray("items_in_set");

            int num = items.size();

            if (num == 1) {

                Integer rank = items.getJSONObject(0).getInteger("mod_max_rank");

                return rank == null ? -1 : rank;

            }


        } else {
            System.out.println("GET请求失败！");
        }
        return  0;
    }
}
