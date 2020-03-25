package com.example.prasad.ilib;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApplyBook extends AppCompatActivity {

    EditText bkname,auth,edition;
    Button apply;
    String bookname,author,edit;
    ListView SearchListView;
    String searchQuery;
    ArrayList<SearchList> sList;
    android.widget.SearchView sView;
    public static final String KEY_BKName = "title";
    public static final String KEY_User = "user";
    public static final String KEY_rno = "rno";
    public static final String KEY_Author = "author";
    public static final String KEY_Edition = "edition";
    public static final String KEY_Date = "DOI";

    String user,rno;
    
//    String url = "http://192.168.4.4/ilib/apply.php";
    String url = "http://192.168.43.34/ilib/apply.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_book);
        apply = (Button)findViewById(R.id.buttonApply);
        sView = (android.widget.SearchView) findViewById(R.id.search_book);
        sView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() == 0) {
                    sList.clear();
                    SearchListView.setVisibility(View.GONE);
                }
                else
                    getSearch(newText);
                return true;
            }
        });
        SearchListView = (ListView) findViewById(R.id.SearchListView);
        SearchListView.setVisibility(View.GONE);


        sList = new ArrayList<>();
        user = this.getIntent().getStringExtra("username");
        rno = this.getIntent().getStringExtra("rollno");
        Log.d("Uname", "onCreate: "+user);
        Log.d("Rno","onCreate: "+rno);

        Toast.makeText(this, "User "+user, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Rno "+rno, Toast.LENGTH_SHORT).show();

//        Date d = new Date(Calendar.getInstance().getTime().toString());
//        final String date = d.toString();

        java.sql.Date d = new java.sql.Date(System.currentTimeMillis());
        final String date = d.toString();

        SearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bkname = (EditText)findViewById(R.id.txtBookName);
                auth = (EditText)findViewById(R.id.txtAuthor);
                edition = (EditText)findViewById(R.id.txtEdition);
                SearchList sitem = sList.get(position);
               // Toast.makeText(ApplyBook.this, "test", Toast.LENGTH_SHORT).show();
                bkname.setText(sitem.getTitle());
                auth.setText(sitem.getAuthor());
                edition.setText(sitem.getEdition());
                SearchListView.setVisibility(View.GONE);
            }
        });

        //Apply button casting and onClick event
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cast edit text values
                bkname = (EditText)findViewById(R.id.txtBookName);
                auth = (EditText)findViewById(R.id.txtAuthor);
                edition = (EditText)findViewById(R.id.txtEdition);



                //Assing the values of edit text
                bookname=bkname.getText().toString();
                author=auth.getText().toString();
                edit = edition.getText().toString();

                StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("resp", "onResponse: "+response);
                        Toast.makeText(ApplyBook.this, "Applied", Toast.LENGTH_SHORT).show();
                        bkname.setText(null);
                        auth.setText(null);
                        edition.setText(null);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ApplyBook.this, "Error While applying", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(KEY_User,user);
                        params.put(KEY_rno,rno);
                        params.put(KEY_BKName,bookname);
                        params.put(KEY_Author,author);
                        params.put(KEY_Edition,edit);
                        params.put(KEY_Date,date);



                        return params;

                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(sr);

            }
        });

    }

    public void getSearch(String query)
    {
        searchQuery = query;
        String url = "http://192.168.43.34/ilib/search_books.php";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resp", "onResponse: "+response);
                try {
                    sList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() > 0)
                    {
                        SearchListView.setVisibility(View.VISIBLE);
                    }
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        SearchList node = new SearchList(jsonObject.getString("Author"),jsonObject.getString("Edition"),jsonObject.getString("Title"));
                        sList.add(node);

                    }
                    SearchListView.setAdapter(new SearchListAdapter(sList,getApplicationContext()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(ApplyBook.this, "Applied", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ApplyBook.this, "Error ", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("qry",searchQuery);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }

    private class SearchList {
        String author, title, edition;

        public SearchList(String author, String edition, String title) {
            this.author = author;
            this.edition = edition;
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getEdition() {
            return edition;
        }

        public void setEdition(String edition) {
            this.edition = edition;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    private class SearchListAdapter extends BaseAdapter {

        ArrayList<SearchList> sList;
        Context ctx;
        public SearchListAdapter(ArrayList<SearchList> sList, Context applicationContext) {
            this.sList = sList;
            ctx = applicationContext;

        }

        @Override
        public int getCount() {
            return sList.size();
        }

        @Override
        public Object getItem(int position) {
            return sList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater lf = getLayoutInflater().from(ctx);
            View item = lf.inflate(R.layout.search_list_item,parent,false);
            SearchList listitem = sList.get(position);
            TextView e = (TextView) item.findViewById(R.id.textViewTitle);
            e.setText(listitem.getTitle());
            e = (TextView) item.findViewById(R.id.textViewAuthor);
            e.setText(listitem.getAuthor());
            e = (TextView) item.findViewById(R.id.textViewEdition);
            e.setText(listitem.getEdition());
            return item;
        }
    }
}
