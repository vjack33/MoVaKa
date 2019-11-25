package com.hexapixels.darklight.movaka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.hexapixels.darklight.movaka.AppConfig.URL_DATA;
import static com.hexapixels.darklight.movaka.AppConfig.URL_RELAY_STATUS;

public class SwitchActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String TAG = DetailedActivity.class.getSimpleName();
    private ImageView refreshButton1;
    TextView switchButton_1;
    TextView switchButton_2;
    TextView switchButton_3;
    TextView switchButton_4;

    Button button_R1On;
    Button button_R2On;
    Button button_R3On;
    Button button_R4On;
    Button button_R1Off;
    Button button_R2Off;
    Button button_R3Off;
    Button button_R4Off;


    //private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        // Progress dialog
        pDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        refreshButton1 = (ImageView) findViewById(R.id.refreshButton1);
        refreshButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refreshSwitchStatus();
            }
        });

        button_R1On = (Button) findViewById(R.id.buttonR1On);
        button_R2On = (Button) findViewById(R.id.buttonR2On);
        button_R3On = (Button) findViewById(R.id.buttonR3On);
        button_R4On = (Button) findViewById(R.id.buttonR4On);

        button_R1Off = (Button) findViewById(R.id.buttonR1Off);
        button_R2Off = (Button) findViewById(R.id.buttonR2Off);
        button_R3Off = (Button) findViewById(R.id.buttonR3Off);
        button_R4Off = (Button) findViewById(R.id.buttonR4Off);

        button_R1On.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://192.168.43.83/ledOn_1");
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            StringBuilder sb = new StringBuilder();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });

                thread.start();



                refreshSwitchStatus();
            }
        });

        button_R2On.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refreshSwitchStatus();
            }
        });

        button_R3On.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refreshSwitchStatus();
            }
        });

        button_R4On.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refreshSwitchStatus();
            }
        });



        checkSwitchStatus();

    }

    /*private void checkSwitchStatus() {

        switchButton_1 = (Button) findViewById(R.id.detailedTextView1);
        switchButton_2 = (Button) findViewById(R.id.detailedTextView2);
        switchButton_3 = (Button) findViewById(R.id.detailedTextView3);
        switchButton_4 = (Button) findViewById(R.id.detailedTextView4);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        pDialog.setMessage("Loading data ...");
        showDialog();

        getJSON( URL_DATA + "?deviceid=" + deviceid1);
    }*/

    private void checkSwitchStatus() {
        // Tag used to cancel the request
        switchButton_1 = (TextView) findViewById(R.id.textView_R1);
        switchButton_2 = (TextView) findViewById(R.id.textView_R2);
        switchButton_3 = (TextView) findViewById(R.id.textView_R3);
        switchButton_4 = (TextView) findViewById(R.id.textView_R4);

        //db = new SQLiteHandler(getApplicationContext());
        pDialog.setMessage("Loading data ...");
        showDialog();

        getJSON( URL_RELAY_STATUS);
    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    try {
                        hideDialog();
                        loadIntoTextView(s);
                    } catch (Exception e) {
                        Log.d("Error", e.toString());
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }

            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void  refreshSwitchStatus() {
        //Intent intent = new Intent(SwitchActivity.this, SwitchActivity.class);
        //startActivity(intent);
        //finish();

        checkSwitchStatus();
    }

    private void loadIntoTextView(String s) {
        //switchButton_1.setText(s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            String heroes1 = jsonObject.getString("relay_1");
            String heroes2 = jsonObject.getString("relay_2");
            String heroes3 = jsonObject.getString("relay_3");
            String heroes4 = jsonObject.getString("relay_4");
            switchButton_1.setText(heroes1);
            switchButton_2.setText(heroes2);
            switchButton_3.setText(heroes3);
            switchButton_4.setText(heroes4);


        }catch (JSONException err){
            Log.d("Error", err.toString());
        }

    }
}
