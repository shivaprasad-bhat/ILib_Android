package com.example.prasad.ilib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Welcome_Page extends AppCompatActivity {


    public Button signup, login;
    public EditText user,pass;
    public String uname,ps,Url;

    String Key_Uname="user";
    String Key_Password="password";

    String jsonUrl;
    String un,crs,rno,b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome__page);
        onSignupClick();


        jsonUrl = "http://192.168.43.34/ilib/login.php";
        //jsonUrl = "http://192.168.4.4/ilib/login.php";
        login = (Button)findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = (EditText)findViewById(R.id.txtUserName);
                pass = (EditText)findViewById(R.id.txtPassword);

                uname=user.getText().toString();
                ps=pass.getText().toString();

                if(uname == null || ps == null)
                {
                    Toast.makeText(Welcome_Page.this, "Invalid User name or password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    StringRequest sr = new StringRequest(Request.Method.POST, jsonUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("resp", "onResponse: "+response);
                            int suc = 0;
                            try {
                                JSONObject j = new JSONObject(response);
                                un = j.getString("name");
                                crs = j.getString("course");
                                rno = j.getString("rno");
                                suc = j.getInt("success");
                                b1 = j.getString("book_1");
                                b2 = j.getString("book_2");



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(suc == 1)
                            {
                                Intent intent1 = new Intent(getApplicationContext(),Profile.class);
                                intent1.putExtra("usr",un);
                                intent1.putExtra("crs",crs);
                                intent1.putExtra("rno",rno);
                                startActivity(intent1);

                            }
                            else
                            {
                                user.setError("Invalid user name or password");
                                return;
                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("errr", "onErrorResponse: "+error.toString());
                        }
                    }) {
                        @Override
                        protected Map<String,String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(Key_Uname, uname);
                            params.put(Key_Password,ps);

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(sr);

                }




                
            }
        });


    }

    public void onSignupClick(){
        signup = (Button)findViewById(R.id.btnSignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);


            }
        });
    }


}
