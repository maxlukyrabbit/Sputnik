package com.example.my_application;

import android.app.Activity;
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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Charsets;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private boolean isFirstTime = true;

    private NfcAdapter nfcAdapter;
    private EditText editText, track_number1, track_number2;
    private TextView ID, Status;
    public Button button1, button2, Repair, To_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
        if (nfcAdapter == null) {
            Toast.makeText(MainActivity.this, "NFC is not available", Toast.LENGTH_LONG).show();
            return;
        }

        editText = findViewById(R.id.editTextText);

        track_number1 = findViewById(R.id.track_number1);
        track_number2 = findViewById(R.id.track_number2);

//        String savedValue = MyPreferences.getEditTextValue(this);
//        track_number1.setText(savedValue);


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


    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if (isFirstTime) {
            processNdefIntent(intent);
            isFirstTime = false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processNdefIntent(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void processNdefIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            NdefMessage ndefMessage = (NdefMessage) rawMsgs[0];
            NdefRecord[] records = ndefMessage.getRecords();
            if (records.length > 0) {
                NdefRecord record = records[0];
                String payload = new String(record.getPayload());
                editText.setText(payload.substring(3)); // Установка полученных данных в EditText
            }
        }
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
                                return Change_stage.Accepted_warehouse(id_deal, in_track_number, id_panal, getApplicationContext(), true, checkBox.isChecked());

                            } else if (in_track_number.length() == 0) {
                                return Change_stage.Accepted_warehouse(id_deal, in_track_number, id_panal, getApplicationContext(), false, checkBox.isChecked());
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
//        button1.setVisibility(View.VISIBLE);
    }


}


//UF_CRM_1693478607504 клемная