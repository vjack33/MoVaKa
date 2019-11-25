package com.hexapixels.darklight.movaka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static com.hexapixels.darklight.movaka.AppConfig.URL_DATA;

public class DetailedActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String TAG = DetailedActivity.class.getSimpleName();
    private ImageView refreshButton1;
    ListView listView1;
    ListView listView2;
    ListView listView3;
    ListView listView4;

    String deviceid1;
    String deviceid2;

    private SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        // Progress dialog
        pDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        refreshButton1 = (ImageView) findViewById(R.id.refresh_button1);

        checkDetailedData();
        refreshButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refreshUser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }


    private void checkDetailedData() {

        listView1 = (ListView) findViewById(R.id.detailedTextView1);
        listView2 = (ListView) findViewById(R.id.detailedTextView2);
        listView3 = (ListView) findViewById(R.id.detailedTextView3);
        listView4 = (ListView) findViewById(R.id.detailedTextView4);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        pDialog.setMessage("Loading data ...");
        showDialog();

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();


        String deviceid1 = user.get("deviceid1");
        String deviceid2 = user.get("deviceid2");

        getJSON( URL_DATA + "?deviceid=" + deviceid1);
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
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                try {
                    hideDialog();
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void loadIntoListView(String json) throws JSONException {

        JSONArray jsonArray1 = new JSONArray(json);
        JSONArray jsonArray2 = new JSONArray(json);
        JSONArray jsonArray3 = new JSONArray(json);
        JSONArray jsonArray4 = new JSONArray(json);

        String[] heroes1 = new String[jsonArray1.length()];
        String[] heroes2 = new String[jsonArray2.length()];
        String[] heroes3 = new String[jsonArray3.length()];
        String[] heroes4 = new String[jsonArray4.length()];

        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject obj1 = jsonArray1.getJSONObject(i);
            JSONObject obj2 = jsonArray2.getJSONObject(i);
            JSONObject obj3 = jsonArray3.getJSONObject(i);
            JSONObject obj4 = jsonArray4.getJSONObject(i);


            heroes1[i] = obj1.getString("time1");
            heroes2[i] = obj2.getString("bpm");
            heroes3[i] = obj3.getString("temperature");
            heroes4[i] = obj4.getString("date1");
        }

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes1);
        listView1.setAdapter(arrayAdapter1);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes2);
        listView2.setAdapter(arrayAdapter2);

        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes3);
        listView3.setAdapter(arrayAdapter3);

        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes4);
        listView4.setAdapter(arrayAdapter4);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void  refreshUser() {
        Intent intent = new Intent(DetailedActivity.this, DetailedActivity.class);
        startActivity(intent);
        finish();
    }

}

