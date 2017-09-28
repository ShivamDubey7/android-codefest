package com.example.shivam.e_com;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class Userspace extends Activity {

    Button btcheck,bttran,btchange;
    TextView ustrans;
    String[] info;
    String card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userspace);
        btcheck = (Button)findViewById(R.id.btcheck);
        ustrans = (TextView)findViewById(R.id.ustrans);
        info = ((String)getIntent().getExtras().get("info")).split(",");
        TextView display = (TextView)findViewById(R.id.usdisplay);
        display.setText("Welcome " + info[0]);
    }
    public void checkB(View view){
        btcheck.setText(info[5]);
        btcheck.setEnabled(false);

    }

    public void showTrans(View view){

        BackgroundTask backgroundTask =new BackgroundTask();
        backgroundTask.execute(info[5]);

    }
    class BackgroundTask extends AsyncTask<String,Void,String> {
        String show_url;
        @Override
        protected void onPreExecute() {
            show_url= "https://shivam17061997.000webhostapp.com/show.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String crd;
            crd = args[0];
            try {
                URL url = new URL(show_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream  =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("card","UTF-8")+"="+URLEncoder.encode(crd,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                outputStream.flush();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response="";
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    response += line+"\n";
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String result) {
            ustrans.setText(result);
        }

    }

    public void changeInfo(View view){
        Intent intent  =new Intent(this,change.class);
        intent.putExtra("card",info[5]);
        startActivity(intent);

    }
}
