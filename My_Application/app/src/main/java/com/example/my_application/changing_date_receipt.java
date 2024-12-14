package com.example.my_application;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class changing_date_receipt {

    public static String changing_date(String id_deal, String date) {
        String result = "";
        if (id_deal.trim().length() != 5) {
            return "Некоректный номер сделки";
        }
        OkHttpClient client = new OkHttpClient();

        try {
            // URL и JSON-запрос
            String url = "https://sputniksystems.bitrix24.ru/rest/879/hpyrfpxem514tmr4/crm.deal.update.json";
            String json = "{\"id\": " + id_deal + ", \"fields\": {\"UF_CRM_1635513856970\": \"" + date + "\"}}";

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




}
