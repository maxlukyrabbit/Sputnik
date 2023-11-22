package com.example.my_application;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class change_track_number {
    public static String change_in_track_number(String id_deal, String in_track_number) {
        String result = "";
        if (id_deal.trim().length() != 5) {
            return "Некоректный номер сделки";
        }
        OkHttpClient client = new OkHttpClient();

        try {
            // URL и JSON-запрос
            String url = "https://sputniksystems.bitrix24.ru/rest/879/hpyrfpxem514tmr4/crm.deal.update.json";
            String json = "{\"id\": " + id_deal + ", \"fields\": {\"UF_CRM_1693478562036\": \"" + in_track_number + "\"}}";

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

    public static String change_out_track_number(String id_deal, String out_track_number) {
        String result = "";
        if (id_deal.trim().length() != 5) {
            return "Некоректный номер сделки";
        }
        OkHttpClient client = new OkHttpClient();

        try {
            // URL и JSON-запрос
            String url = "https://sputniksystems.bitrix24.ru/rest/161/l93zfoeri1pzz656/crm.deal.update.json";
            String json = "{\"id\": " + id_deal + ", \"fields\": {\"UF_CRM_1693292930596\": \"" + out_track_number + "\"}}";

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
