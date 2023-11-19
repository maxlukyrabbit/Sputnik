package com.example.my_application;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private EditText editText, track_number1, track_number2;
    private TextView ID, Status;

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


        ID = findViewById(R.id.ID);
        Status = findViewById(R.id.Status);




        Button button1 = findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Change_stage("Accepted_warehouse");
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Change_stage("Done_sending");
            }
        });

        Button Repair = findViewById(R.id.Repair);
        Repair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Change_stage("Under_repair");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] messages = getNdefMessages(intent);
            if (messages != null && messages.length > 0) {
                NdefRecord record = messages[0].getRecords()[0];
                String payload = new String(record.getPayload());
                editText.setText(payload.substring(3));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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


    public void  startLogs(View v)
    {
        Intent intent = new Intent(this, logs.class);
        startActivity(intent);
    }

    public void Change_stage(String method){
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        IntentFilter[] intentFiltersArray = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)};
        nfcAdapter.enableForegroundDispatch(MainActivity.this, pendingIntent, intentFiltersArray, null);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String id_panal = editText.getText().toString();
                String in_track_number = track_number1.getText().toString();
                String out_track_number = track_number2.getText().toString();
                if (id_panal.trim().length() == 10) {
                    String id_deal = Search_deal.search_deal(id_panal);
                    if ("Accepted_warehouse".equals(method)) {
                        if (in_track_number.length() == 20)
                        {
                            return Change_stage.Accepted_warehouse(id_deal, in_track_number);
                        }
                        else
                        {
                            return "Введите корректный трек номер";
                        }

                    }

                    if ("Done_sending".equals(method)) {
                        if (out_track_number.length() == 20)
                        {
                            return Change_stage.Done_sending(id_deal, out_track_number);
                        }
                        else
                        {
                            return "Введите корректный трек номер";
                        }

                    }
                    if ("Under_repair".equals(method)) {
                        return Change_stage.Under_repair(id_deal);
                    }

                }
                return "Введите корректный номер панели ";
            }

            @Override
            protected void onPostExecute(String result) {
                ID.setText(editText.getText());
                if ("Успех".equals(result)){
                    Status.setText("Успех");
                }
                else{
                    Status.setText("Ошибка");
                }
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }




}



