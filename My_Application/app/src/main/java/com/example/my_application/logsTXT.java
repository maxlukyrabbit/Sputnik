package com.example.my_application;

import android.content.Context;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;


public class logsTXT {
    public static void LogsWriter(Context context, String Status, String ID, String Explanation) {
        LocalDate Date = null;
        LocalTime Time = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Date = LocalDate.now();
            Time = LocalTime.now().withSecond(0).withNano(0);
        }
        String data = String.format("%s %s - %s %s %s%s", Date, Time, Status, ID, Explanation, System.lineSeparator());

        try {
            FileWriter writer = new FileWriter(context.getFilesDir() + "/logs.txt", true);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка при записи данных в файл: " + e.getMessage());
        }
    }
}
