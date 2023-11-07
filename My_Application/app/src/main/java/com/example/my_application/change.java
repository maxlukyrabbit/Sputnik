package com.example.my_application;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class change {
    public String change(String id) {
        String id1 = "";
        String stageId = "xzcfvghjk";
        try {
            String url = "https://sputniksystems.bitrix24.ru/rest/879/igjz3ay62dj3ppzo/crm.deal.list.json?FILTER[UF_CRM_1629819273209]=2121124652";

            URL apiUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            stageId = "888";

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();


                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                JSONObject jsonObject = new JSONObject(response.toString());

                JSONArray resultArray = jsonObject.getJSONArray("result");
                JSONObject resultObject = resultArray.getJSONObject(0);
                id1 = resultObject.getString("ID");

            } else {
                stageId = ("Error: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {


            String url = "https://sputniksystems.bitrix24.ru/rest/879/igjz3ay62dj3ppzo/crm.deal.get.json?id="+id1;


            URL apiUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                JSONObject jsonObject = new JSONObject(response.toString());

                JSONObject firstJson = jsonObject.getJSONObject("result");

                stageId = firstJson.getString("STAGE_ID");

                firstJson.put("STAGE_ID", "ПППП");


                stageId = firstJson.getString("STAGE_ID");



            } else {
                stageId = ("Error: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (stageId);
    }

}
