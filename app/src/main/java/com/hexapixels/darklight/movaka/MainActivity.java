package com.hexapixels.darklight.movaka;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.hexapixels.darklight.movaka.AppConfig.URL_CHART_1;
import static com.hexapixels.darklight.movaka.AppConfig.URL_CHART_2;
//import static com.hexapixels.darklight.movaka.AppConfig.URL_jio;

/**
 * Created by darklight on 6/1/18.
 *
 */

public class MainActivity extends Activity {

    private TextView txtFirstName;
    private TextView txtLastName;
    private TextView txtDeviceid1;
    private TextView txtDeviceid2;
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button detailedButton;
    private ImageView refreshButton;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtFirstName = (TextView) findViewById(R.id.firstname);
        txtLastName = (TextView) findViewById(R.id.lastname);
        txtDeviceid1 = (TextView) findViewById(R.id.deviceid1);
        txtDeviceid2 = (TextView) findViewById(R.id.deviceid2);
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        detailedButton = (Button) findViewById(R.id.detailedButton);
        refreshButton = (ImageView) findViewById(R.id.refresh_button);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String firstname = user.get("firstname");
        String lastname = user.get("lastname");
        String deviceid1 = user.get("deviceid1");
        String deviceid2 = user.get("deviceid2");
        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtFirstName.setText(firstname);
        txtLastName.setText(lastname);
        txtDeviceid1.setText(deviceid1);
        txtDeviceid2.setText(deviceid2);
        txtName.setText(name);
        txtEmail.setText(email);

        int char1int = Integer.parseInt(deviceid1);
        int char2int = char1int + 1;
        String char1 = Integer.toString(char1int);
        String char2 = Integer.toString(char2int);


        WebView webView1 = (WebView) findViewById(R.id.webView1);
        webView1.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView1.getSettings().setSupportMultipleWindows(false);
        webView1.getSettings().setSupportZoom(false);
        webView1.setVerticalScrollBarEnabled(false);
        webView1.setHorizontalScrollBarEnabled(false);
        webView1.loadUrl(URL_CHART_1 + "?deviceid=" + deviceid1);

        WebView webView2 = (WebView) findViewById(R.id.webView2);
        webView2.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView2.getSettings().setSupportMultipleWindows(false);
        webView2.getSettings().setSupportZoom(false);
        webView2.setVerticalScrollBarEnabled(false);
        webView2.setHorizontalScrollBarEnabled(false);
        webView2.loadUrl(URL_CHART_2 + "?deviceid=" + deviceid1);
        //webView2.loadUrl(URL_jio);

        //Detailed Button functioning
        detailedButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                detailedUser();
            }
        });


        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refreshUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void  detailedUser() {
        Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
        startActivity(intent);
        finish();
    }

    private void  refreshUser() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void switchON(){

        finish();
    }

    private void switchOFF(){

        finish();
    }
}