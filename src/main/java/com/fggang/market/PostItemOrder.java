package com.fggang.market;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fggang.datasave.items.Item;
import com.fggang.tools.HttpClientUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class PostItemOrder {
    public static String getStringItemOrder(Item item , int rank) {
        String rs = "你查询的物品是: " + item.item + "(" + item.url + ")\n" +
                "价格区间: 待更新\n";

        String result = getPostResult(item);

        if (result != null) {
            JSONObject jsonObject = JSON.parseObject(result);
            int num = jsonObject.getJSONObject("payload")
                    .getJSONArray("orders")
                    .size();

            orderSort(jsonObject);


            if (num >= 5) {
                rs = rs + "从" + num + "位卖家中捕获到top5卖家信息(测试版)\n";
            } else {
                rs = rs + "从" + num + "位卖家中捕获到top"+num+"卖家信息(测试版)\n";
            }

            int n = 0;
            String end = "";
            // 从JSONObject对象中获取属性值
            for (int i = 0; i < num; i++) {
                if(n == 5) break;
                JSONObject ls1 = jsonObject.getJSONObject("payload")
                        .getJSONArray("orders")
                        .getJSONObject(i);
                if (!ls1.getJSONObject("user").getString("status").equals("offline") && ls1.getString("order_type").equals("sell")) {
                    if(rank != -1 && ls1.getInteger("mod_rank") != rank) {
                        continue;
                    }
                    if(n == 0) {
                        end = "\n为您自动生成了最低价的购买私信: \n" +
                                "/w " + ls1.getJSONObject("user").getString("ingame_name") +
                                " Hi! I want to buy: " + item.url +
                                " barrel for " + ls1.getString("platinum") +
                                " platinum. (warframe.market)";
                    }
                    n++;
                    rs = rs + ls1.getJSONObject("user").getString("ingame_name")
                            + "  白金: " + ls1.getString("platinum")
                            + "  数量: " + ls1.getString("quantity");
                    rs = rs + "\n";

                }

            }

            if(end.equals("")) {
                return "该物品无人售卖";
            }

            rs = rs + end;

        } else {
            System.out.println("GET请求失败！");
        }
        return rs ;
    }
    public static String getImageItemOrder(Item item , int rank) {
        String result = getPostResult(item);

        String data = "";
        if (result != null) {
            JSONObject jsonObject = JSON.parseObject(result);
            int num = jsonObject.getJSONObject("payload")
                    .getJSONArray("orders")
                    .size();

            orderSort(jsonObject);
            int n = 0;
            String end = "";

            // 从JSONObject对象中获取属性值
            for (int i = 0; i < num; i++) {
                if(n == 5) break;
                JSONObject ls1 = jsonObject.getJSONObject("payload")
                        .getJSONArray("orders")
                        .getJSONObject(i);
                if (!ls1.getJSONObject("user").getString("status").equals("offline") && ls1.getString("order_type").equals("sell")) {
                    if(rank != -1 && ls1.getInteger("mod_rank") != rank) {
                        continue;
                    }
                    if(n == 0) {
                        end = "\n为您自动生成了最低价的购买私信: \n" +
                                "/w " + ls1.getJSONObject("user").getString("ingame_name") +
                                " Hi! I want to buy: " + item.url +
                                " barrel for " + ls1.getString("platinum") +
                                " platinum. (warframe.market)";
                    }
                    n++;
                    data = data + "<td>" + ls1.getJSONObject("user").getString("ingame_name") + "</td>\n" +
                            "        <td>" + ls1.getString("platinum") + "</td>\n" +
                            "        <td>" + ls1.getString("quantity") + "</td>";

                }

            }

            if(end.equals("")) {
                return "该物品无人售卖";
            }

        } else {
            System.out.println("GET请求失败！");
        }

        return data;
    }

    private static void orderSort(JSONObject jsonObject) {
        JSONArray orders = jsonObject.getJSONObject("payload")
                .getJSONArray("orders");


        orders.sort((o1, o2) -> {
            JSONObject jo1 = (JSONObject) o1;
            JSONObject jo2 = (JSONObject) o2;
            int p1 = jo1.getInteger("platinum");
            int p2 = jo2.getInteger("platinum");
            return Integer.compare(p1, p2);
        });
    }

    private static String getPostResult(Item item) {
        Map<String, String> head = new HashMap<>();

        head.put("accept", "application/json");
        head.put("Language", "zh-hans");
        head.put("Platform", "pc");
        String result;
        try {
            result = HttpClientUtils.get("https://api.warframe.market/v1/items/" + item.url + "/orders?include=item", null, head);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
