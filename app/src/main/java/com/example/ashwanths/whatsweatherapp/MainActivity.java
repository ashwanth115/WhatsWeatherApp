package com.example.ashwanths.whatsweatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    public void find(View view){

        Log.i("city",editText.getText().toString());
        InputMethodManager m=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromInputMethod(editText.getWindowToken(),0);

        try {
            String encoded = URLEncoder.encode(editText.getText().toString(),"UTF-8");

            DownloaderTask downloaderTask = new DownloaderTask();
            downloaderTask.execute("http://samples.openweathermap.org/data/2.5/weather?q="+encoded+"&appid=376a4ef3516c43ef9fa719cd9eeade8b");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    public class DownloaderTask extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection httpURLConnection=null;
            String result="";

            try{
                url = new URL(strings[0]);

                httpURLConnection=(HttpURLConnection)url.openConnection();

                InputStream in = httpURLConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1)
                {
                    char a =(char)data;

                    result += a;

                    data = reader.read();


                }
                return result;


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

              //  String location = editText.getText().toString();
String main="";
                String description="";
                JSONObject jsonObject = new JSONObject(s);

                String json = jsonObject.getString("weather");

                JSONArray jsonArray = new JSONArray(json);

                for(int i=0;i<jsonArray.length();i++)
                {


                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    main=jsonObject1.getString("main");
                    description=jsonObject1.getString("description");

                    textView.setText(main+"\n"+description);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.display);

    }
}
