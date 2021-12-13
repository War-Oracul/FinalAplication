package com.example.myapplication;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {

    DataBase databaseHelper;
    SQLiteDatabase db;
    Cursor query;

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_LOGIN = "login";
    public static final String APP_PREFERENCES_PASS = "password";
    public static boolean isShowAlert = true;
    SharedPreferences mSettings;
    EditText editlogin;
    EditText editpassword;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        databaseHelper = new DataBase(getApplicationContext());

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        editlogin = findViewById(R.id.loginPlace);
        editpassword = findViewById(R.id.passwordPlace);
        editlogin.setText(mSettings.getString(APP_PREFERENCES_LOGIN, ""));
        editpassword.setText(mSettings.getString(APP_PREFERENCES_PASS, ""));

        /* Make sure, we have the permissions */
        requestSmsPermission();


        final Button button = (Button)findViewById(R.id.button_Enter);
        textView = findViewById(R.id.textView);


        // Кнопка войти
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strLogin = editlogin.getText().toString();
                String strPassword = editpassword.getText().toString();

                db = databaseHelper.getReadableDatabase();
                query = db.rawQuery("SELECT * FROM "+ DataBaseContract.UserEntry.TABLE_NAME_1 + " where "+DataBaseContract.UserEntry.T1_ATR2 +"=?", new String[]{strLogin});
                if (query.moveToNext()){
                    String checkPassword = query.getString(2);
                    if (strPassword.equals(checkPassword))
                    {
                        textView.setText("Ok");
                        db.close();
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_LOGIN, strLogin);
                        editor.putString(APP_PREFERENCES_PASS, strPassword);
                        editor.apply();
                        Intent user = new Intent(LoginActivity.this, MainActivity.class);
                        user.putExtra("login", strLogin);
                        startActivity(user);
                    }
                    else{
                        textView.setText("Неверный пароль");
                        db.close();
                    }
                }
                else{
                    textView.setText("Пользователя не существует");
                    db.close();
                }

            }
        });

        final Button button4 = (Button)findViewById(R.id.button_Create);
        //Кнопка регистрации
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strLogin = editlogin.getText().toString();
                final String strPassword = editpassword.getText().toString();
                db = databaseHelper.getReadableDatabase();
                query = db.rawQuery("SELECT * FROM "+ DataBaseContract.UserEntry.TABLE_NAME_1 + " where "+DataBaseContract.UserEntry.T1_ATR2 +"=?", new String[]{strLogin});
                if (query.moveToNext()) {
                    textView.setText("Логин занят");
                    db.close();
                }
                else if(strLogin.length() > 0) {
                    db.close();
                    if (strPassword.length() >=8) {

                        Thread regThread = new Thread( // создаём новый поток
                                new Runnable() { // описываем объект Runnable в конструкторе
                                    public void run() {
                                        regUser(strLogin,strPassword); // вызываем метод воспроизведения
                                    }
                                }

                        );
                        regThread.start();
                        try {
                            regThread.join();
                            textView.setText("Регистрация успешна выполните вход");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    else{
                        textView.setText("Слабый пароль");
                    }
                }
                else{
                    db.close();
                    textView.setText("Некорректный логин");
                }
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        isShowAlert = true;

        Bundle arguments = getIntent().getExtras();
        if (arguments!= null){
            String sms = arguments.get("sms").toString();
            textView.setText(sms);
        }

    }
    @Override
    protected void onPause(){
        super.onPause();
        isShowAlert = false;
    }
    @Override
    protected void onStop(){
        super.onStop();

    }
    @Override
    protected void onDestroy(){
        databaseHelper.close();
        super.onDestroy();
    }

    public void regUser(String login, String password){
        SQLiteDatabase dbInThread = databaseHelper.getWritableDatabase();
        ContentValues newUser = new ContentValues();
        newUser.put(DataBaseContract.UserEntry.T1_ATR2, login);
        newUser.put(DataBaseContract.UserEntry.T1_ATR3, password);
        dbInThread.insert(DataBaseContract.UserEntry.TABLE_NAME_1, null, newUser);
        dbInThread.close();
    }


    private void requestSmsPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
        }

}
