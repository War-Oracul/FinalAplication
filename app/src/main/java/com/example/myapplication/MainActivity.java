package com.example.myapplication;


import static com.example.myapplication.DataBaseContract.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    DataBase databaseHelper;
    SQLiteDatabase db;
    String login;
    private Menu menu;
    List<Item> list = new ArrayList<Item>();
    boolean languageState = true;
    int MyPosition;
    CustomListAdapter adapter;
    private final static String TAG = "MainActivity";
    List<Item> image_details;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        if(menu.getClass().getSimpleName()
                .equals("MenuBuilder")){
            try{
                Method m = menu.getClass()
                        .getDeclaredMethod (
                                "setOptionalIconsVisible",
                                Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            }
            catch(NoSuchMethodException e){
                System.err.println("onCreateOptionsMenu");
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
        MenuItem itm = menu.findItem(R.id.item1);
        itm.setTitle(login);
        this.menu = menu;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String msg = "";
        switch (item.getItemId()) {
            case R.id.item2:
                AddNewItem();
                break;
            case R.id.item3:
                DeleteItem();
                break;
            case R.id.item4:
                if (Locale.getDefault().getLanguage() == "ru") {
                    Locale locale = new Locale("en");
                    changeLocale(locale);
                }
                else
                {
                    Locale locale = new Locale("ru");
                    changeLocale(locale);
                }
                break;
            case R.id.item5:
                Intent exitIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(exitIntent);
                finish();
                break;
            case R.id.item6:
                Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");


        Bundle arguments = getIntent().getExtras();
        login = arguments.get("login").toString();

        databaseHelper = new DataBase(getApplicationContext());

       /* final List<Item>*/  image_details = getListData();
        ReadDataFromDataBase(image_details,login);

        final ListView listView = (ListView) findViewById(R.id.countriesList);
        adapter = new CustomListAdapter(this, image_details);
        listView.setAdapter(adapter);

        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                MyPosition = position;

            }
        });

        final Button button3 = (Button)findViewById(R.id.button3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, BackActivity.class);
                startActivity(i);
            }
        });

        final Button button2 = (Button)findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteItem();
            }
        });



        final Button button1 = (Button)findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewItem();
            }
        });

        final Button button0 = (Button)findViewById(R.id.button0);

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Locale.getDefault().getLanguage() == "ru") {
                    Locale locale = new Locale("en");
                    changeLocale(locale);
                }
                else
                {
                    Locale locale = new Locale("ru");
                    changeLocale(locale);
                }


            }
        });

    }

    @Override
    protected void onDestroy(){
        databaseHelper.close();
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        LoginActivity.isShowAlert = true;
        Log.d(TAG, "onStart");
    }
    @Override
    protected void onPause(){
        super.onPause();
        LoginActivity.isShowAlert = false;
        Log.d(TAG, "onPause");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    private  List<Item> getListData() {

        return list;
    }

    @SuppressWarnings("deprecation")
    private void changeLocale(Locale locale)
    {
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources()
                .updateConfiguration(configuration,
                        getBaseContext()
                                .getResources()
                                .getDisplayMetrics());
        setTitle(R.string.title_activity_main);

        Button bt = (Button) findViewById(R.id.button0);
        bt.setText(R.string.button_Language);

        bt = (Button) findViewById(R.id.button1);
        bt.setText(R.string.button_Add);

        bt = (Button) findViewById(R.id.button2);
        bt.setText(R.string.button_Remove);

        bt = (Button) findViewById(R.id.button3);
        bt.setText(R.string.button_Activity);

       /* bt = (Button) findViewById(R.id.button_Create);
        bt.setText(R.string.button_Create);

        bt = (Button) findViewById(R.id.button_Enter);
        bt.setText(R.string.button_Enter);

        EditText txt = (EditText) findViewById(R.id.loginPlace);
        txt.setHint(R.string.loginPlace);

        txt = (EditText) findViewById(R.id.passwordPlace);
        txt.setHint(R.string.passwordPlace);*/

        MenuItem itm = menu.findItem(R.id.item2);
        itm.setTitle(R.string.button_Add);

        itm = menu.findItem(R.id.item3);
        itm.setTitle(R.string.button_Remove);

        itm = menu.findItem(R.id.item4);
        itm.setTitle(R.string.button_Language);

        itm = menu.findItem(R.id.item5);
        itm.setTitle(R.string.m_exit);

        itm = menu.findItem(R.id.item6);
        itm.setTitle(R.string.m_execute);

    }

    public void AddNewItem(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        final EditText one = new EditText(this);
        final EditText two = new EditText(this);
        final EditText tree = new EditText(this);

        if (Locale.getDefault().getLanguage() == "ru") {
            alert.setTitle("Введите параметры журнала");
            one.setHint("Имя");
            two.setHint("Тип");
            tree.setHint("Издание");
        }
        else{
            alert.setTitle("Enter journal parameters");
            one.setHint("Name");
            two.setHint("Type");
            tree.setHint("Edition");
        }
        //in my example i use TYPE_CLASS_NUMBER for input only numbers
        one.setInputType(InputType.TYPE_CLASS_TEXT);
        two.setInputType(InputType.TYPE_CLASS_TEXT);
        tree.setInputType(InputType.TYPE_CLASS_TEXT);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(one);
        lay.addView(two);
        lay.addView(tree);
        alert.setView(lay);

        final String[] text = new String[3];

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable value = one.getText();
                text[0] = value.toString();

                value = two.getText();
                text[1] = value.toString();

                value = tree.getText();
                text[2] = value.toString();

                final ContentValues newValues = new ContentValues();
                newValues.put(DataEntry.T2_ATR2, login);
                newValues.put(DataEntry.T2_ATR3, text[0]);
                newValues.put(DataEntry.T2_ATR4, text[1]);
                newValues.put(DataEntry.T2_ATR5, text[2]);

                Thread insertThread = new Thread( // создаём новый поток
                        new Runnable() { // описываем объект Runnable в конструкторе
                            public void run() {
                                SQLiteDatabase dbw;
                                dbw = databaseHelper.getWritableDatabase();
                                dbw.insert(DataEntry.TABLE_NAME_2, null, newValues);
                                dbw.close();
                            }
                        }

                );
                insertThread.start();
                try {
                    insertThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                image_details.clear();
                ReadDataFromDataBase(image_details,login);
                //    image_details.add(new Item(text[0], text1[0], text2[0]));
                adapter.updateList(image_details);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton){}
        });
        alert.show();
    }

    private void DeleteItem(){
        Item i = image_details.get(MyPosition);
        final int mid = i.getId();
        Thread deleteThread = new Thread( // создаём новый поток
                new Runnable() { // описываем объект Runnable в конструкторе
                    public void run() {
                        SQLiteDatabase dbw;
                        dbw = databaseHelper.getWritableDatabase();
                        dbw.delete(DataEntry.TABLE_NAME_2,   DataEntry.T2_ATR1+" =" + mid, null);
                        dbw.close();
                    }
                }

        );
        deleteThread.start();
        try {
            deleteThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        image_details.clear();
        ReadDataFromDataBase(image_details,login);
        adapter.updateList(image_details);
    }
    public int ReadDataFromDataBase(List<Item> list, String login) {
        int count = 0;

        db = databaseHelper.getReadableDatabase();
        Cursor query = db.rawQuery("SELECT * FROM "+ DataEntry.TABLE_NAME_2 +" where "+DataEntry.T2_ATR2+"=?", new String[]{login});
        if(query.moveToFirst()){
            do {
                int id = query.getInt(0);
                String name = query.getString(2);
                String tip = query.getString(3);
                String edition = query.getString(4);
                list.add(new Item(name, tip, edition, id));
                count++;
            }   while(query.moveToNext());
        }
        query.close();
        db.close();
        return count;
    }

}