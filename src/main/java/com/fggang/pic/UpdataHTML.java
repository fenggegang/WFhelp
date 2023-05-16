package com.fggang.pic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdataHTML {

    public void updateMarket(String itemName , String text) throws IOException {
        String template = "C:\\Users\\Administrator\\Desktop\\TestOcr\\historyTestOcr.html";
        String html = "C:\\Users\\Administrator\\Desktop\\TestOcr\\historypages.html";
        updata(template, html , itemName , text);
    }

    public void updateVoidTrader(String itemName , String text) throws IOException {
        String template = "C:\\Users\\Administrator\\Desktop\\TestOcr\\historyTestOcr.html";
        String html = "C:\\Users\\Administrator\\Desktop\\TestOcr\\historypages.html";
        updata(template, html , itemName , text);
    }


    private void updata(String template, String html , String itemName , String text) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(template)), "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            String htmlAsString = stringBuilder.toString();


            htmlAsString = htmlAsString.replaceAll("#数据#",text);
            htmlAsString = htmlAsString.replaceAll("#物品名#",itemName);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(html)), "UTF-8"));
            writer.write(htmlAsString);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
