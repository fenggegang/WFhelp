package com.fggang.worldstate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fggang.tools.HttpClientUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VoidTrader {

    public static void main(String[] args) {
        new VoidTrader().getVoidTrader();
    }

    public String getVoidTrader() {
        String returnS;
//        try {
//            returnS = getData();
//        } catch (IOException | URISyntaxException e) {
//            return "数据出错";
//        }

        returnS = "{\n" +
                "    \"id\": \"5d1e07a0a38e4a4fdd7cefca\",\n" +
                "    \"activation\": \"2023-05-19T13:00:00.000Z\",\n" +
                "    \"startString\": \"6d 23h 16m 20s\",\n" +
                "    \"expiry\": \"2023-05-21T13:00:00.000Z\",\n" +
                "    \"active\": false,\n" +
                "    \"character\": \"Baro Ki'Teer\",\n" +
                "    \"location\": \"Larunda 中继站 (水星)\",\n" +
                "    \"inventory\": [],\n" +
                "    \"psId\": \"5d1e07a0a38e4a4fdd7cefca0\",\n" +
                "    \"endString\": \"8d 23h 16m 20s\",\n" +
                "    \"initialStart\": \"1970-01-01T00:00:00.000Z\",\n" +
                "    \"schedule\": []\n" +
                "}";

        String result = "";

        JSONObject jsonObject = JSON.parseObject(returnS);
        String location = jsonObject.getString("location");
        String activation = jsonObject.getString("activation");
        String expiry = jsonObject.getString("expiry");

        boolean active = jsonObject.getBoolean("active");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        if(active) {
            result = "它已经回来了哦\uD83D\uDE00 \n";

        } else {
            LocalDateTime ldt = LocalDateTime.parse(activation, formatter);
            ldt = ldt.plusHours(8);

            LocalDateTime ldtnow = LocalDateTime.now();

            Duration duration = Duration.between(ldtnow, ldt);

            int day = (int) (duration.getSeconds() / 60 / 60 / 24);

            if(day > 9) {
                result = "他才刚刚出发 去探索虚空 \n或许他需要一段时间才回来\uD83D\uDC38 \n";
            } else if(day > 4) {
                result = "他已经出发了有一段时间 \n或许他需要应该快要回来了\uD83D\uDC38 \n";
            } else {
                result = "他告诉我已经在往回赶路了哦 \uD83D\uDC38 \n";
            }

        }

        return result;

    }


    private String getData() throws IOException, URISyntaxException {
        String result = HttpClientUtils.get("https://api.warframestat.us/pc/voidTrader?language=zh" , null , null);
        return result;
    }
}
