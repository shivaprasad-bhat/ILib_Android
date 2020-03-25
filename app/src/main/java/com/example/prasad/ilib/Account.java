package com.example.prasad.ilib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

public class Account extends AppCompatActivity {

    String rno,jsonUrl;
    String book1, book2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        rno = getIntent().getStringExtra("rno");

        int roll = Integer.parseInt(rno);
//        final TextView b1 = (TextView)findViewById(R.id.txtBook1);
//        final TextView b2 = (TextView)findViewById(R.id.txtBook2);




        jsonUrl = "http://192.168.43.34/ilib/getbook.php";
        //jsonUrl = "http://192.168.4.4/ilib/getbook.php";

        StringRequest sr = new StringRequest(Request.Method.POST, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Err", "onResponse: "+response);
                try {
                    JSONObject j = new JSONObject(response);
                    book1 = j.getString("book_1");
                    book2 = j.getString("book_2");

                    TextView b1 = (TextView)findViewById(R.id.txtBook1);
                    TextView b2 = (TextView)findViewById(R.id.txtBook2);
                    b1.setText(book1);
                    b2.setText(book2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERR", "onResponse: "+error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rno",rno);
                return params;



            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);


    }
}
