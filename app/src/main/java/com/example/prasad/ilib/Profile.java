package com.example.prasad.ilib;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;


public class Profile extends AppCompatActivity implements TabLayout.OnTabSelectedListener
{


    String uname,rno,crs,usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        usr=this.getIntent().getStringExtra("usr");
        TabLayout tb = (TabLayout)findViewById(R.id.tab);
        tb.addOnTabSelectedListener(this);
//        String jsonUrl = "http://192.168.43.34/ilib/login.php";

        uname = getIntent().getStringExtra("usr");
        rno = getIntent().getStringExtra("rno");
        crs = getIntent().getStringExtra("crs");



        TextView tv = (TextView)findViewById(R.id.txtUname);
        tv.setText(uname);


        tv = (TextView)findViewById(R.id.txtReg);
        tv.setText(rno);

        tv = (TextView)findViewById(R.id.txtCrs);
        tv.setText(crs);





    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition())
        {
            case 0 :
                        break;
            case 1 :
                Intent intapply = new Intent(getApplicationContext(),ApplyBook.class);
                intapply.putExtra("username",uname);
                intapply.putExtra("rollno",rno);

                startActivity(intapply);
                break;
            case 2 :
                Intent i2 = new Intent(getApplicationContext(),Account.class);
                i2.putExtra("rno",rno);
                startActivity(i2);
                        break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
//        switch (tab.getPosition())
//        {
//            case 0 :
//                break;
//            case 1 :
//                Intent intapply = new Intent(getApplicationContext(),ApplyBook.class);
//                startActivity(intapply);
//                break;
//            case 2 :
//                break;
//
//        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        switch (tab.getPosition())
        {
            case 0 :
                break;
            case 1 :
                Intent intapply = new Intent(getApplicationContext(),ApplyBook.class);
                intapply.putExtra("username",uname);
                intapply.putExtra("rollno",rno);
                startActivity(intapply);
                break;
            case 2 :
                Intent i2 = new Intent(getApplicationContext(),Account.class);
                i2.putExtra("rno",rno);
                startActivity(i2);
                break;
        }
    }
}
