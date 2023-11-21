package com.example.my_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class logs extends AppCompatActivity {

    private TextView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        log = findViewById(R.id.textView2);


        try (BufferedReader reader = new BufferedReader(new FileReader(getFilesDir() + "/logs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.append(line);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении данных из файла: " + e.getMessage());
        }
    }

    public void startMain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clearLogs(View v)
    {
        try {
            FileWriter writer = new FileWriter(getFilesDir() + "/logs.txt");
            writer.write("");
            writer.close();
            log.setText("");
        }
        catch (IOException e) {
            //System.out.println("Ошибка при записи данных в файл: " + e.getMessage());
        }
    }
}
