package com.example.shivam.e_com;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

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

public class change extends AppCompatActivity {
    EditText name,addr;
    String nm,add,card;
    TextView chdisplay;
    Button done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        name = (EditText)findViewById(R.id.chname);
        addr = (EditText)findViewById(R.id.chaddr);
        card = (String)getIntent().getExtras().get("card");
        chdisplay = (TextView)findViewById(R.id.chdisplay);

    }
    public void done(View view){
        nm=name.getText().toString();
        add=addr.getText().toString();
        BackgroundTask backgroundTask =new BackgroundTask();
        backgroundTask.execute(nm,add,card);
    }

    class BackgroundTask extends AsyncTask<String,Void,String> {
        String ch_url;
        @Override
        protected void onPreExecute() {
            ch_url= "https://shivam17061997.000webhostapp.com/change.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String chname,chadd,crd;
            chname=args[0];
            chadd=args[1];
            crd=args[2];
            try {
                URL url = new URL(ch_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream  =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data_string = URLEncoder.encode("card","UTF-8")+"="+URLEncoder.encode(crd,"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(chname,"UTF-8")+"&"+
                        URLEncoder.encode("addr","UTF-8")+"="+URLEncoder.encode(chadd,"UTF-8");
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
            chdisplay.setText(result);
        }

    }
}
