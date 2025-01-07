package com.example.my_application;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class STK_main extends AppCompatActivity {
    public Spinner master;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stk_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        master = findViewById(R.id.master);
        List<String> items = new ArrayList<>();
        items.add("Максим");
        items.add("Виктор");
        items.add("Тимур");
        items.add("Руслан");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        master.setAdapter(adapter);
        showInputDialog();
    }


    private void showInputDialog() {
        // Создаем EditText для ввода текста
        EditText inputField = new EditText(this);
        inputField.setHint("Введите текст");

        // Создаем диалоговое окно
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ввод текста");
        builder.setMessage("Введите текст в поле ниже:");
        builder.setView(inputField);

        // Кнопка "OK"
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Получаем текст из EditText
                String inputText = inputField.getText().toString();
                // Показываем текст в Toast
                Toast.makeText(STK_main.this, "Вы ввели: " + inputText, Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка "Отмена"
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Закрываем диалог
                dialog.cancel();
            }
        });

        // Показываем диалог
        builder.show();
    }




    public void test(View v){
        Toast.makeText(STK_main.this, master.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }
}