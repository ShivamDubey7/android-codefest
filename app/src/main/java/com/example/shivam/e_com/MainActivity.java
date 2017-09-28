package com.example.shivam.e_com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class  MainActivity extends Activity {
    Button login,reg;
    EditText login_master,login_pass;
    String Login_master,Login_pass;
    TextView tv,tv2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button)findViewById(R.id.btlogin);
        reg = (Button)findViewById(R.id.btreg);
        login_master = (EditText)findViewById(R.id.etlmaster);
        login_pass = (EditText)findViewById(R.id.etlpass);
        tv = (TextView)findViewById(R.id.tvinfo);
        tv2=(TextView)findViewById(R.id.tvlogin);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            tv.setVisibility(View.INVISIBLE);
        }
        else{
            login.setEnabled(false);
            reg.setEnabled(false);
        }
    }
    public void registerUser(View view){
        startActivity(new Intent(this,addInfo.class));
    }
    public void loginUser(View view){
        Login_master = login_master.getText().toString();
        Login_pass = login_pass.getText().toString();

        BackgroundTask backgroundTask =new BackgroundTask();
        backgroundTask.execute(Login_master,Login_pass);
    }
    class BackgroundTask extends AsyncTask<String,Void,String> {
        String login_url;
        @Override
        protected void onPreExecute() {
            login_url= "https://shivam17061997.000webhostapp.com/login.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String Login_master,Login_pass;
            Login_master = args[0];
            Login_pass =args[1];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream  =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("login_master","UTF-8")+"="+URLEncoder.encode(Login_master,"UTF-8")+"&"+
                        URLEncoder.encode("login_pass","UTF-8")+"="+URLEncoder.encode(Login_pass,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                outputStream.flush();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response="";
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    response += line;
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
            Intent intent  =new Intent(MainActivity.this,Userspace.class);
            result = result.substring(1);

            if(!result.isEmpty()){
                intent.putExtra("info",result);
                startActivity(intent);
            }
            else{
                tv2.setText("Incorrect Credentials");
            }

        }

    }

}
