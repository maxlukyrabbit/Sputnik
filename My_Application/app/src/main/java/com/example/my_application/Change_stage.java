package com.example.my_application;

import android.content.Context;
import android.widget.EditText;

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


    private static EditText editText;

    public static String Change_stage(String id_deal, String id_stage){
        String result = "";
        if (id_deal.trim().length() != 5) {
            return "Некоректный номер сделки";
        }
        OkHttpClient client = new OkHttpClient();

        try {
            // URL и JSON-запрос
            String url = "https://sputniksystems.bitrix24.ru/rest/161/l93zfoeri1pzz656/crm.deal.update.json";
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
            String urlString = "https://sputniksystems.bitrix24.ru/rest/161/l93zfoeri1pzz656/crm.deal.get.json?id=" + id_deal;

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


            }
            else {
                status = "Ошибка при выполнении запроса. Код ответа: " + responseCode;
            }

            // Закрытие соединения
            connection.disconnect();
        } catch (Exception e) {
            //e.printStackTrace();
            status = "Ошибка при выполнении";
        }
        return status;
    }

    public static String Accepted_warehouse(String id_deal, String in_track_number, String id_panel, Context context) {

        String err = null;
        String result = null;
        String status = status(id_deal);

        if (Objects.equals(status, "C25:NEW") || Objects.equals(status, "C25:PREPARATION") || Objects.equals(status, "C25:PREPAYMENT_INVOIC")) {
            result = Change_stage(id_deal, "C25:EXECUTING");
            if ("Успех".equals(result))
            {
                result = change_track_number.change_in_track_number(id_deal, in_track_number);



                logsTXT.LogsWriter(context, "Успех:", id_panel, "принято на склад");
            }
        } else {

            result = "Завка находится в неподходящей стадии " + status;

            logsTXT.LogsWriter(context, "Ошибка", id_panel, "этап неподходящий.");

        }
        return result;

    }

    public static String Done_sending(String id_deal, String out_track_number, String id_panel, Context context) {
        String err = null;
        String result = null;
        String status = status(id_deal);

        if (!"C25:LOSE".equals(status) && !"C25:APOLOGY".equals(status) && !"C25:WON".equals(status)) { // Заявка отменена
            result = (Change_stage(id_deal, "C25:7"));
            if ("Успех".equals(result))
            {
                result = change_track_number.change_out_track_number(id_deal, out_track_number);
                logsTXT.LogsWriter(context, "Успех:", id_panel, "готово к отправке");
            }

        }
        else {
            result = "Завка находится в неподходящей стадии " + status;
            logsTXT.LogsWriter(context, "Ошибка:", id_panel, "этап неподходящий.");
        }
        return result;
    }

    public static String Under_repair(String id_deal, String id_panel, Context context) {
        String err = null;
        String result = null;
        String status = status(id_deal);

        if (Objects.equals(status, "C25:EXECUTING") || Objects.equals(status, "C25:1") || Objects.equals(status, "C25:2") || Objects.equals(status, "C25:FINAL_INVOICE") || Objects.equals(status, "C25:7") || Objects.equals(status, "C25:UC_DMZGI5")) {
            result = Change_stage(id_deal, "C25:2");
            logsTXT.LogsWriter(context, "Успех:", id_panel, "в ремонте");
        } else {
            result = "Завка находится в неподходящей стадии " + status;
            logsTXT.LogsWriter(context, "Ошибка:", id_panel, "этап неподходящий.");
        }

        return result;
    }
}