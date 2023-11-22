package com.example.my_application;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetOutgoingIncomingTackNumber {
    public String getOutgoingIncomingTackNumber(String id_deal, String key) {
        String TackNumber = null;
        try {
            // URL для получения JSON
            String urlString = "https://sputniksystems.bitrix24.ru/rest/161/l93zfoeri1pzz656/crm.deal.get.json?id="+id_deal;

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
                JSONObject dealsArray = jsonObject.getJSONObject("result");
                TackNumber = dealsArray.getString(key);

            } else {
                TackNumber = "Ошибка при выполнении запроса. Код ответа: " + responseCode;
            }

            // Закрытие соединения
            connection.disconnect();
        } catch (Exception e) {
            //e.printStackTrace();
            TackNumber = "Ошибка при выполнении";
        }
        return TackNumber;
    }
}


//    Трек номер входящий = UF_CRM_1693478562036
//    Трек номер исходящий = UF_CRM_1693292930596