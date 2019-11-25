package com.hexapixels.darklight.movaka;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by darklight on 6/1/18.
 */

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputPhone;
    private EditText inputAltPhone;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputRePassword;
    private EditText inputDeviceidA;
    private EditText inputDeviceidB;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFirstName = (EditText) findViewById(R.id.firstname);
        inputLastName = (EditText) findViewById(R.id.lastname);
        inputPhone = (EditText) findViewById(R.id.phoneno);
        inputAltPhone = (EditText) findViewById(R.id.altphoneno);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputRePassword = (EditText) findViewById(R.id.repassword);
        inputDeviceidA = (EditText) findViewById(R.id.deviceidp1);
        inputDeviceidB = (EditText) findViewById(R.id.deviceidp2);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    SwitchActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String firstname = inputFirstName.getText().toString().trim();
                String lastname = inputLastName.getText().toString().trim();
                String phoneno = inputPhone.getText().toString().trim();
                String altphoneno = inputAltPhone.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String repassword = inputRePassword.getText().toString().trim();
                String deviceid1 = inputDeviceidA.getText().toString().trim();
                String deviceid2 = inputDeviceidB.getText().toString().trim();
                //String deviceid = inputDeviceidA.getText().toString().trim() + inputDeviceidA.getText().toString().trim();


                if (!firstname.isEmpty() &&
                        !lastname.isEmpty() &&
                        !phoneno.isEmpty() &&
                        !altphoneno.isEmpty() &&
                        !email.isEmpty() &&
                        !password.isEmpty() &&
                        !repassword.isEmpty() &&
                        !deviceid1.isEmpty() &&
                        !deviceid2.isEmpty() )
                {
                    registerUser(firstname, lastname, phoneno, altphoneno, email, password, deviceid1, deviceid2);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String firstname,
                              final String lastname,
                              final String phoneno,
                              final String altphoneno,
                              final String email,
                              final String password,
                              final String deviceid1,
                              final String deviceid2) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //Toast.makeText(getApplicationContext(), "Respond frm srver", Toast.LENGTH_LONG).show();
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String firstname = user.getString("firstname");
                        String lastname = user.getString("lastname");
                        String phoneno = user.getString("phoneno");
                        String altphoneno = user.getString("altphoneno");
                        String email = user.getString("email");
                        //String password = user.getString("password");
                        String deviceid1 = user.getString("deviceid1");
                        String deviceid2 = user.getString("deviceid2");;
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        db.addUser(firstname, lastname, phoneno, altphoneno, email, deviceid1, deviceid2, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Er" + error.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("phoneno", phoneno);
                params.put("altphoneno", altphoneno);
                params.put("email", email);
                params.put("password", password);
                params.put("deviceid1", deviceid1);
                params.put("deviceid2", deviceid2);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}