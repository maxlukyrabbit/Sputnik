package com.example.my_application;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class terminal_block {
    public static void terminal_block(String id_deal, String Availability){
        OkHttpClient client = new OkHttpClient();

        try {
            // URL и JSON-запрос
            String url = "https://sputniksystems.bitrix24.ru/rest/161/l93zfoeri1pzz656/crm.deal.update.json";
            String json = "{\"id\": " + id_deal + ", \"fields\": {\"UF_CRM_1693478607504\": \"" + Availability + "\"}}";

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
                // Handle the response here
            } else {
                // Handle unsuccessful response here
            }


        } catch (Exception e) {
            // Handle other exceptions here
            e.printStackTrace();
        }

    }
}
