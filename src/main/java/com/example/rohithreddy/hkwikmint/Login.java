package com.example.rohithreddy.hkwikmint;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    UserSessionManager session;
    private ProgressDialog pDialog;
    String x="nointernet"; String responseBody; String firstname="",fullname;
    Response response;
    EditText etname;
    TextView tvoutput;
    Button submit;
    public static String filename = "Valustoringfile";
    SharedPreferences SP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
         //       WindowManager.LayoutParams.FLAG_FULLSCREEN);

      //  Initialization();
      //  SP = getSharedPreferences(filename, 0);
      //  String getname = SP.getString("key1","");
      //  etname.setText(getname);
        session = new UserSessionManager(getApplicationContext());

        if(session.isUserLoggedIn()){
            String x,y,z;
            HashMap<String, String> user =  session.getUserDetails();
            x = user.get(UserSessionManager.KEY_NAME);
            y = user.get(UserSessionManager.KEY_PASS);
            z = user.get(UserSessionManager.KEY_FULLNAME);
            Intent loginIntent = new Intent(Login.this, MainActivity.class);
            session.createUserLoginSession(x, y,z);
            Login.this.startActivity(loginIntent);
        }
        final EditText phonenum = (EditText) findViewById(R.id.username);
        final EditText pin = (EditText) findViewById(R.id.password);
        final Button blogin = (Button) findViewById(R.id.login);
        phonenum.setFilters( new InputFilter[] { new InputFilter.LengthFilter(10)});
        blogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(phonenum.getText().toString().trim().length() < 10){
                    phonenum.setError("phone number should be 10 digits");
                }
                else if(pin.getText().toString().trim().length()< 6){
                    pin.setError("password should  be min  6 digits");
                }

                else {
                    final String name = phonenum.getText().toString();
                    final String pass = pin.getText().toString();
//                    switch (v.getId()) {
//                        case R.id.login:
//
//
//                            SharedPreferences.Editor editit = SP.edit();
//                            editit.putString("key1", name);
//                            editit.apply();
//                            break;

                 //   }
                    new AsyncTask<Void, Void, Void>()
                    {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            pDialog = new ProgressDialog(Login.this);
                            pDialog.setMessage("Please wait...");
                            pDialog.setCancelable(false);
                            pDialog.show();
                        }
                        @Override
                        protected Void doInBackground(Void... arg0)
                        {
                            OkHttpClient client = new OkHttpClient();
                            RequestBody formBody = new FormBody.Builder()
                                    .add("field1", name)
                                    .add("field2",pass)
                                    .build();
                            Request request = new Request.Builder()
                                    .url(getResources().getString(R.string.url_text)+"/verifyLogin")
                                    .post(formBody)
                                    .build();
                            try {
                                response = client.newCall(request).execute();
                                responseBody = response.body().string();
                                System.out.println("respone is -------------"+responseBody);
                                try {
                                    JSONObject jsonObj = new JSONObject(responseBody);
                                    firstname = jsonObj.getString("status");

                                    if (firstname.equals("success")) {
                                        fullname = jsonObj.getString("fullName");
                                    }
                                    /////////
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                x="internet";
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            if (x=="nointernet"){
                                Toast.makeText(getApplicationContext(),"internet problem or server down", Toast.LENGTH_LONG).show();
                            }
                            else if(firstname.equals("success")){
                                Intent loginIntent = new Intent(Login.this, MainActivity.class);
                                session.createUserLoginSession(name, pass,fullname);
                                Login.this.startActivity(loginIntent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"invalid phonenumber or pin", Toast.LENGTH_LONG).show();
                                pin.setText("");
                                //phonenum.setText("");
                                x="nointernet";
                            }
                        }
                    }.execute();
                }
            }
        });
        phonenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phonenum.setError(null);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void afterTextChanged(Editable s) {
                phonenum.setError(null);
                pin.setText("");
            }
        });
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence p, int start, int before, int count) {
                if(phonenum.getText().toString().trim().length() < 10){
                    phonenum.setError("phone number should be 10 digits");
                }
                pin.setError(null);
            }
            @Override
            public void beforeTextChanged(CharSequence p, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable p) {pin.setError(null);}
        });

    }
//    private void Initialization() {
//        // TODO Auto-generated method stub
//
//        etname = (EditText) findViewById(R.id.username);
//
//        submit = (Button) findViewById(R.id.login);
//
//        //tvoutput = (TextView) findViewById(R.id.tv_output);
//        //submit.setOnClickListener();
//
//    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}


