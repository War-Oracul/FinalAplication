package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class MessageReceiver extends BroadcastReceiver {
    private NotificationUtils mNotificationUtils;
    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationUtils = new NotificationUtils(context);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            ArrayList<String> numbers = new ArrayList<String>();

            for (int i=0; i<msgs.length; i++){ //пробегаемся по всем полученным сообщениям
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                String number = msgs[i].getOriginatingAddress(); //получаем номер отправителя
                if (number.equals("+79374465059") && (LoginActivity.isShowAlert == false)) {
                    String mes = "SMS сообщение с номера <"+ number+">";
                    Notification.Builder nb = mNotificationUtils.
                            getChannelNotification(context, "Уведомление от приложения",mes);

                    mNotificationUtils.getManager().notify(101, nb.build());
                }
            }
        }

    }

}

