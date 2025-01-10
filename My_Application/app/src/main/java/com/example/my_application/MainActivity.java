package com.example.my_application;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Charsets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private EditText editText, track_number1, track_number2;
    private TextView ID, Status, date_text;
    public Button button1, button2, Repair, To_check;
    public ImageButton calendar_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
        if (nfcAdapter == null) {
            Toast.makeText(MainActivity.this, "NFC is not available", Toast.LENGTH_LONG).show();
            return;
        }
        handleIntent(getIntent());
        editText = findViewById(R.id.panel);
        track_number1 = findViewById(R.id.track_number1);
        track_number2 = findViewById(R.id.track_number2);
        ID = findViewById(R.id.ID);
        Status = findViewById(R.id.Status);
        button1 = findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setBackgroundResource(R.drawable.rectangle_shape_false);
                button1.setEnabled(false);
                Change_stage("Accepted_warehouse");
            }
        });

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2.setBackgroundResource(R.drawable.rectangle_shape_false);
                button2.setEnabled(false);
                Change_stage("Done_sending");
            }
        });

        Repair = findViewById(R.id.Repair);
        Repair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Repair.setBackgroundResource(R.drawable.rectangle_shape_false);
                Repair.setEnabled(false);
                Change_stage("Under_repair");
            }
        });

        To_check = findViewById(R.id.To_check);
        To_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                To_check.setBackgroundResource(R.drawable.rectangle_shape_false);
                To_check.setEnabled(false);
                Change_stage("To_check");
            }
        });

        date_text = findViewById(R.id.date_text);
        calendar_button = findViewById(R.id.calendar_button);
        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override

                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = String.format("%02d.%02d.%d", dayOfMonth, (month + 1), year);
                                date_text.setText(selectedDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] messages = getNdefMessages(intent);
            if (messages != null && messages.length > 0) {
                NdefRecord record = messages[0].getRecords()[0];
                String payload = new String(record.getPayload());
                EditText panel = findViewById(R.id.panel);
                panel.setText(payload.substring(3));
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
        nfcAdapter.disableForegroundDispatch(this);
    }

    private NdefMessage[] getNdefMessages(Intent intent) {
        NdefMessage[] messages = null;
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null && rawMessages.length > 0) {
            messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }
        }
        return messages;
    }

    public void startLogs(View v) {
        Intent intent = new Intent(this, logs.class);
        startActivity(intent);
    }


    public void Change_stage(String method) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        IntentFilter[] intentFiltersArray = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)};
        nfcAdapter.enableForegroundDispatch(MainActivity.this, pendingIntent, intentFiltersArray, null);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                CheckBox checkBox = findViewById(R.id.Terminal_block);
                String id_panal = editText.getText().toString();
                String in_track_number = track_number1.getText().toString();
                String out_track_number = track_number2.getText().toString();
                if (id_panal.trim().length() == 10) {
                    String id_deal = Search_deal.search_deal(id_panal);

                    if (id_deal.trim().length() == 5) {

                        if ("Accepted_warehouse".equals(method)) {
                            if (in_track_number.length() != 0 && in_track_number.length() <= 20) {
                                return Change_stage.Accepted_warehouse(id_deal, in_track_number, id_panal, getApplicationContext(), true, checkBox.isChecked(), date_text.getText().toString());

                            } else if (in_track_number.length() == 0) {
                                return Change_stage.Accepted_warehouse(id_deal, in_track_number, id_panal, getApplicationContext(), false, checkBox.isChecked(), date_text.getText().toString());
                            } else {
                                logsTXT.LogsWriter(getApplicationContext(), "ошибка:", id_panal, "некорректный трек номер");
                                return "Введите корректный трек номер";
                            }
                        }

                        if ("Done_sending".equals(method)) {
                            if (out_track_number.length() != 0 && out_track_number.length() <= 20) {
                                return Change_stage.Done_sending(id_deal, out_track_number, id_panal, getApplicationContext(), true);
                            } else if (out_track_number.length() == 0) {
                                return Change_stage.Done_sending(id_deal, out_track_number, id_panal, getApplicationContext(), false);

                            } else {
                                logsTXT.LogsWriter(getApplicationContext(), "ошибка:", id_panal, "некорректный трек номер");
                                return "Введите корректный трек номер";
                            }

                        }
                        if ("Under_repair".equals(method)) {

                            return Change_stage.Under_repair(id_deal, id_panal, getApplicationContext());
                        }

                        if ("To_check".equals(method)) {
                            return Change_stage.To_check(id_deal, id_panal, getApplicationContext());
                        }

                    } else {
                        logsTXT.LogsWriter(getApplicationContext(), "ошибка:", id_panal, "некорректный номер сделки");
                        return "Некорректный номер сделки";
                    }
                }
                logsTXT.LogsWriter(getApplicationContext(), "ошибка:", id_panal, "некорректный номер панели");
                return "Введите корректный номер панели ";
            }

            @Override
            protected void onPostExecute(String result) {
                ID.setText(editText.getText());
                if ("Успех".equals(result)) {
                    Status.setText("Успех");
                } else {
                    Status.setText("Ошибка");
                }
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();

                button1.setBackgroundResource(R.drawable.rectangle_shape);
                button2.setBackgroundResource(R.drawable.rectangle_shape);
                Repair.setBackgroundResource(R.drawable.rectangle_shape);
                To_check.setBackgroundResource(R.drawable.rectangle_shape);

                button1.setEnabled(true);
                button2.setEnabled(true);
                Repair.setEnabled(true);
                To_check.setEnabled(true);
            }
        }.execute();
    }




    public void clean_date(View v){
        date_text.setText("");
    }

}

//UF_CRM_1693478607504 клемная
