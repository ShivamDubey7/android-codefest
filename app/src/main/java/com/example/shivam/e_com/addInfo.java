package com.example.shivam.e_com;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
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
import java.nio.Buffer;

public class addInfo extends AppCompatActivity {
    EditText name,email,phone,addr,pass;
    String Name,Email,Phone,Pass,Addr;
    TextView tvdisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        name = (EditText)findViewById(R.id.etname);
        email = (EditText)findViewById(R.id.etemail);
        pass = (EditText)findViewById(R.id.etpass);
        phone = (EditText)findViewById(R.id.etphone);
        addr= (EditText)findViewById(R.id.etaddr);
        tvdisplay=(TextView)findViewById(R.id.tvdisplay);

    }
    public void saveInfo(View view){
        Name = name.getText().toString();
        Email = email.getText().toString();
        Pass = pass.getText().toString();
        Phone = phone.getText().toString();
        Addr = addr.getText().toString();
        BackgroundTask backgroundTask =new BackgroundTask();
        backgroundTask.execute(Name,Email,Pass,Addr,Phone);
    }
    class BackgroundTask extends AsyncTask<String,Void,String> {
        String add_info_url;

        @Override
        protected void onPreExecute() {
            add_info_url = "https://shivam17061997.000webhostapp.com/add_info.php";

        }

        @Override
        protected String doInBackground(String... args) {
            String Name,Email,Phone,Addr,Pass;
            Name = args[0];
            Email =args[1];
            Pass = args[2];
            Addr = args[3];
            Phone = args[4];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream  =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(Name,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(Email,"UTF-8")+"&"+
                        URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(Pass,"UTF-8")+"&"+
                        URLEncoder.encode("addr","UTF-8")+"="+URLEncoder.encode(Addr,"UTF-8")+"&"+
                        URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(Phone,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                outputStream.flush();
                outputStream.close();
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
            result = result.substring(1);
            tvdisplay.setText(result);
        }

    }
}
