package com.example.my_application;

import android.content.Context;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class logsTXT {
    public static void LogsWriter(Context context, String Status, String ID, String Explanation) {
        LocalDate Date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Date = LocalDate.now();
        }
        String data = String.format("\n\n  %s %s %s %s\n\n", Date, Status, ID, Explanation);

//        String data = "";
        try {
            FileWriter writer = new FileWriter(context.getFilesDir() + "/logs.txt",true);
            writer.write(data + System.lineSeparator());
            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка при записи данных в файл: " + e.getMessage());
        }
    }
}
