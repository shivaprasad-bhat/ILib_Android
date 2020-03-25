package com.example.prasad.ilib;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Registration
public class Register extends AppCompatActivity  {

    //Local variable Declerations
    String uname, course, passwd, cpasswd, regno, em;
    EditText name,roll,crs,email,pass,cpass;
    Button reg;
    String url;


    //Key values to be posted
    public static final String KEY_User = "name";
    public static final String KEY_Reg = "rno";
    public static final String KEY_Course = "course";
    public static final String KEY_Passwd = "password";
    public static final String KEY_email = "email";

    //On Create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Parse the text field values inserted into local variables
        pass = (EditText) findViewById(R.id.txtPass);
        cpass = (EditText) findViewById(R.id.txtCpass);

        name = (EditText) findViewById(R.id.txtName);
        email = (EditText) findViewById(R.id.txtEmail);
        roll = (EditText) findViewById(R.id.txtRoll);
        crs = (EditText) findViewById(R.id.txtCrs);

        reg = (Button) findViewById(R.id.btnRegister);

        //Set onClickListener to button R
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               Toast.makeText(Register.this, "Clicked", Toast.LENGTH_SHORT).show();
                uname = name.getText().toString();
                course = crs.getText().toString();
                regno = roll.getText().toString();
                passwd = pass.getText().toString();
                cpasswd = cpass.getText().toString();
                em = email.getText().toString();

                //url = "http://192.168.4.4/ilib/register.php";
                url = "http://192.168.43.34/ilib/register.php";

                //OnClickListener for Register Button
                if (passwd.equals(cpasswd)) {

                    StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("resp", "onResponse: " + response);
                            try {
                                JSONObject j = new JSONObject(response);
                                String suc = j.getString("success");
                                if (!suc.equals("OK")) {
                                    roll.setError("User Already Exists");
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            boolean vEm = validateEmail(em);

                            if (!vEm) {
                                Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(getApplicationContext(), Welcome_Page.class);
                                startActivity(intent2);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(KEY_User, uname);
                            params.put(KEY_Reg, regno);
                            params.put(KEY_Course, course);
                            params.put(KEY_Passwd, passwd);
                            params.put(KEY_email, em);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(sr);
                } else {
                    Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
                }

            }


        });

    }
    public boolean validateEmail(String email)
    {
        String e = email;
        String ePattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(ePattern);
        Matcher matcher = p.matcher(e);
        if(matcher.matches())
        {
            return false;
        }
        else {
            EditText eText = (EditText) findViewById(R.id.txtEmail);
            eText.setError("Invalid Email");
            return true;
        }
    }



    private class ErrREsponse implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
//        pd.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                // there is no connection
                alertDialog.setTitle("Connection Error");
                alertDialog.setMessage("No Connection Try again Later");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });


            } else if (error instanceof AuthFailureError) {

            } else if (error instanceof ServerError) {

            } else if (error instanceof NetworkError) {

            } else if (error instanceof ParseError) {

            }

            alertDialog.show();
        }
    }
}
