package com.fggang.worldstate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fggang.tools.Durationx;
import com.fggang.tools.HttpClientUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CetusCycle {
    public String getcetusCycle() {
        String returnS;
        try {
            returnS = getData();
        } catch (IOException | URISyntaxException e) {
            return "数据出错";
        }

        String result = "";

        JSONObject jsonObject = JSON.parseObject(returnS);
        String activation = jsonObject.getString("activation");
        String expiry = jsonObject.getString("expiry");

        boolean isDay = jsonObject.getBoolean("isDay");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        if(isDay) {

            result = "阳光渐渐的温暖起来，洒在世界里抚摸着万物\n\n";

            LocalDateTime ldt = LocalDateTime.parse(activation, formatter);
            ldt = ldt.plusHours(8);

            LocalDateTime ldtnow = LocalDateTime.now();

            Duration duration = Duration.between(ldtnow , ldt);

            result += "剩余时间: " + new Durationx().toString(duration);

        } else {

            result = "月亮斜挂在天空，笑盈盈的，星星挤满了银河，眨巴着眼睛\n\n";

            LocalDateTime ldt = LocalDateTime.parse(expiry, formatter);
            ldt = ldt.plusHours(8);

            LocalDateTime ldtnow = LocalDateTime.now();

            Duration duration = Duration.between(ldt , ldtnow);

            result += "剩余时间: " + new Durationx().toString(duration);

        }

        return result;

    }


    private String getData() throws IOException, URISyntaxException {
        String result = HttpClientUtils.get("https://api.warframestat.us/pc/cetusCycle?language=zh" , null , null);
        return result;
    }
}
