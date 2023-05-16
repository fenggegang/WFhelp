package com.fggang.datasave.items;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fggang.tools.HttpClientUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemList {
    private static ArrayList<Item> items = new ArrayList<>();

    public static void itemRead() {

        int num_ls = items.size();

        for (int i = 0; i < num_ls; i++) {
            items.remove(0);
        }


        Map<String, String> head = new HashMap<>();
        head.put("accept", "application/json");
        head.put("Language", "zh-hans");

        String result;
        try {
            result = HttpClientUtils.get("https://api.warframe.market/v1/items", null, head);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        if (result != null) {
            JSONObject jsonObject = JSON.parseObject(result);
            int num = jsonObject.getJSONObject("payload")
                    .getJSONArray("items")
                    .size();

            // 从JSONObject对象中获取属性值
            for (int i = 0; i < num; i++) {
                JSONObject ls1 = jsonObject.getJSONObject("payload")
                        .getJSONArray("items")
                        .getJSONObject(i);
                items.add(new Item(ls1.getString("url_name") , ls1.getString("item_name")));
            }
            System.out.println("读入数据完成！");
        } else {
            System.out.println("GET请求失败！");
        }
    }

    public static ArrayList<Item> getItems() {
        return items;
    }
}
