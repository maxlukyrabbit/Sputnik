package com.example.my_application;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Change_stage {
    public static String Change_stage(String id_deal, String id_stage){
        String result = "";
        if (id_deal.trim().length() != 5) {
            return "Некоректный номер сделки";
        }
        OkHttpClient client = new OkHttpClient();

        try {
            // URL и JSON-запрос
            String url = "https://sputniksystems.bitrix24.ru/rest/879/hpyrfpxem514tmr4/crm.deal.update.json";
            String json = "{\"id\": " + id_deal + ", \"fields\": {\"STAGE_ID\": \"" + id_stage + "\"}}";

            // Создание запроса POST
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // Выполнение запроса и получение ответа
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                // Преобразование ответа в строку
                String responseBody = response.body().string();
                result = "Успех";
            }

        } catch (IOException e) {
            result = "Ошибка";
        }

        return result;
    }

    public static String status(String id_deal) {
        String status = "";
        try {
            // URL для получения JSON
            String urlString = "https://sputniksystems.bitrix24.ru/rest/879/igjz3ay62dj3ppzo/crm.deal.get.json?id=" + id_deal;

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
                status = dealsArray.getString("STAGE_ID");


            } //else {
//                status = "Ошибка при выполнении запроса. Код ответа: " + responseCode;
//            }

            // Закрытие соединения
            connection.disconnect();
        } catch (Exception e) {
            //e.printStackTrace();
            //status = "Ошибка при выполнении";
        }
        return status;
    }

    public static String Accepted_warehouse(String id_deal) {
        String err = null;
        String result = null;
        String status = status(id_deal);

        if (Objects.equals(status, "C25:NEW")) { // Новая заявка
            result = (Change_stage(id_deal, "C25:EXECUTING"));
        } else if (Objects.equals(status, "C25:PREPARATION")) { // Проверка заявки
            result = (Change_stage(id_deal, "C25:EXECUTING"));
        } else if (Objects.equals(status, "C25:PREPAYMENT_INVOIC")) { // Новая заявка
            result = (Change_stage(id_deal, "C25:EXECUTING"));
        } else {
            result = "Завка находится в неподходящей стадии " + status;
        }
        return result;

    }

    public static String Done_sending(String id_deal) {
        String err = null;
        String result = null;
        String status = status(id_deal);

        if (!"C25:LOSE".equals(status) && !"C25:APOLOGY".equals(status) && !"C25:WON".equals(status)) { // Заявка отменена
            result = (Change_stage(id_deal, "C25:7"));

        }
        else {
            result = "Завка находится в неподходящей стадии " + status;
        }
        return result;
    }

    public static String Under_repair(String id_deal) {
        String err = null;
        String result = null;
        String status = status(id_deal);

        if (Objects.equals(status, "C25:EXECUTING")) { // Принята на склад
            result = (Change_stage(id_deal, "C25:2"));
        } else if (Objects.equals(status, "C25:1")) { // Отложено
            result = (Change_stage(id_deal, "C25:2"));
        } else if (Objects.equals(status, "C25:2")) { // В ремонте
            result = (Change_stage(id_deal, "C25:2"));
        } else if (Objects.equals(status, "C25:FINAL_INVOICE")) { // Выходной контроль
            result = (Change_stage(id_deal, "C25:2"));
        } else if (Objects.equals(status, "C25:7")) { // Готово к отправке
            result = (Change_stage(id_deal, "C25:2"));
        } else if (Objects.equals(status, "C25:UC_DMZGI5")) { // Паркинг
            result = (Change_stage(id_deal, "C25:2"));
        } else {
            result = "Завка находится в неподходящей стадии " + status;
        }
        return result;
    }
}