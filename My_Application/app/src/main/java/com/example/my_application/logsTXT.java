package com.example.my_application;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
            File file = new File(context.getFilesDir(), "logs.txt");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();
            fis.close();

            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing data to file: " + e.getMessage());
        }
    }

    public static void track_number1(Context context, String track_number1) {
        try {
            File file = new File(context.getFilesDir(), "track_number1.txt");
            FileWriter writer = new FileWriter(file, false);

            writer.write(track_number1);

            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing data to file: " + e.getMessage());
        }
    }

    public static void track_number2(Context context, String track_number2) {
        try {
            File file = new File(context.getFilesDir(), "track_number2.txt");
            FileWriter writer = new FileWriter(file, false);
            writer.write(track_number2);

            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing data to file: " + e.getMessage());
        }
    }
}