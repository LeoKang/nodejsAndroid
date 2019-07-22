package com.jbedu.android_node;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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

public class MainActivity extends AppCompatActivity {
    TextView tvServerIP;
    EditText etServerIP, etID, etPW, etMsg;
    String m_ip = "192.168.10.141";
    String m_port = "3000";
    String strId, strPw;
    boolean flag_DBresult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvServerIP = findViewById(R.id.tvServerip);
        etServerIP = findViewById(R.id.etServerip);
        etID = findViewById(R.id.etId);
        etPW = findViewById(R.id.etPw);
        etMsg = findViewById(R.id.etMsg);

        etServerIP.setText(m_ip);

        Button btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                etMsg.setText("");
                strId = etID.getText().toString();
                strPw = etPW.getText().toString();
                Log.d("KSJ", "Button Clicked!");
                Log.d("KSJ", "ID : " + strId);
                Log.d("KSJ", "PW : " + strPw);
                new JSONTask().execute("http://" + m_ip + ":" + m_port + "/post");
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            etMsg.setText("Loading...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", strId);
                jsonObject.accumulate("pw", strPw);
                Log.d("KSJ", jsonObject.toString());

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type","application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();
                    Log.d("KSJ", con.toString());

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line;
                    while((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null) {
                        con.disconnect();
                    }
                    try{
                        if(reader != null) {
                            reader.close();
                        }
                    }catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            flag_DBresult = true;
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(!flag_DBresult || isCancelled()) {
                etMsg.setText(result);
            }
        }
    }
}
