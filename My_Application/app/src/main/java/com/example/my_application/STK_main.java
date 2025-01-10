package com.example.my_application;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class STK_main extends AppCompatActivity {
    public Spinner master;
    public static ArrayList<Note_class> note_classes = new ArrayList<>();
    public ListView list_panel;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC не поддерживается на этом устройстве", Toast.LENGTH_SHORT).show();
            return;
        }
        setContentView(R.layout.activity_stk_main);

        // Инициализация Spinner
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


//        reload_listView();
//        showInputDialog();
//        handleIntent(getIntent());
    }

    public void reload_listView() {
        custom_listView customListViewAdapter = new custom_listView(this, note_classes);
        list_panel = findViewById(R.id.list_panel);
        list_panel.setAdapter(customListViewAdapter);
    }

    private void showInputDialog() {
        EditText inputField = new EditText(this);
        inputField.setHint("Введите текст");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ввод текста");
        builder.setMessage("Введите текст в поле ниже:");
        builder.setView(inputField);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String inputText = inputField.getText().toString();
            Toast.makeText(STK_main.this, "Вы ввели: " + inputText, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            handleIntent(intent);
        }
    }

    private void handleIntent(Intent intent) {
        NdefMessage[] messages = getNdefMessages(intent);
        if (messages != null && messages.length > 0) {
            NdefRecord record = messages[0].getRecords()[0];
            String payload = new String(record.getPayload());

            // Защита от ошибок в substring
            if (payload.length() > 3) {
                note_classes.add(new Note_class(note_classes.size() + 1, payload.substring(3), 0, ""));
                reload_listView();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatch();
    }

    private void enableForegroundDispatch() {
        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        IntentFilter[] intentFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatch() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private NdefMessage[] getNdefMessages(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null && rawMessages.length > 0) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }
            return messages;
        }
        return null;
    }
}