package com.example.my_application;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.util.Log;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

public class TagUtil {
    private static final String TAG = TagUtil.class.getSimpleName();

    public static String readTagData(Tag tag) {
        StringBuilder data = new StringBuilder();

        // Получаем список доступных технологий для данного NFC тега
        String techList[] = tag.getTechList();
        for (String tech : techList) {
            // Читаем данные из соответствующей технологии
            if (tech.startsWith("android.nfc.tech.")) {
                try {
                    // Создаем класс для чтения данных из технологии
                    Class<?> techClass = Class.forName(tech);
                    Method method = techClass.getMethod("get", Tag.class);
                    Object techInstance = method.invoke(null, tag);

                    // Получаем данные технологии
                    if (techInstance instanceof Ndef) {
                        Ndef ndef = (Ndef) techInstance;
                        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
                        NdefRecord records[] = ndefMessage.getRecords();
                        for (NdefRecord record : records) {
                            byte[] payload = record.getPayload();
                            String text = new String(payload, Charset.forName("UTF-8"));
                            data.append(text);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error reading tag data", e);
                }
            }
        }

        return data.toString();
    }
}
