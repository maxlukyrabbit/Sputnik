package com.example.my_application;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Search_deal {
    public static String search_deal(String id_panel) {
        String id_deal = "";

        try {
            // URL для получения JSON
            String urlString = "https://sputniksystems.bitrix24.ru/rest/879/igjz3ay62dj3ppzo/crm.deal.list.json?FILTER[UF_CRM_1629819273209]=" + id_panel.trim();

            // Создание объекта URL
            URL url = new URL(urlString);

            // Создание объекта HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Получение ответа от сервера
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Чтение данных из входного потока
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Обработка полученного JSON
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray dealsArray = jsonObject.getJSONArray("result");
                JSONObject lastDeal = dealsArray.getJSONObject(dealsArray.length() - 1);
                id_deal = lastDeal.getString("ID");


            } else {
                id_deal = "Ошибка при выполнении запроса. Код ответа: " + responseCode;
            }

            // Закрытие соединения
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            id_deal = "Ошибка при выполнении";
        }
        return id_deal;
    }

}
