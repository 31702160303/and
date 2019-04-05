package com.example.kk.wedontchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabsAccessorAdapter mtabsAccessorAdapter;
public String getusername;

    private String getuser;
    private String getpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar= findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("微信");
        mViewPager= findViewById(R.id.main_tabs_pager);
        SharedPreferences sharedPreferences=getSharedPreferences("config",0);
        getusername = sharedPreferences.getString("name","original");
        getuser=sharedPreferences.getString("user","original");
        getpassword=sharedPreferences.getString("password","original");
        mtabsAccessorAdapter=new TabsAccessorAdapter(getSupportFragmentManager(),getusername,getuser,getpassword);
        mViewPager.setAdapter(mtabsAccessorAdapter);
        mTabLayout=findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void sendToLoginActivity()
    {
        Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
    startActivity(loginIntent);
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.options_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.main_logout_option){
            SharedPreferences sp=getSharedPreferences("config",0);
            SharedPreferences.Editor edit = sp.edit();
            edit.remove("user");
            edit.remove("password");
            edit.commit();
            sendToLoginActivity();
        }
        if(item.getItemId()==R.id.main_settings_option){

        }
        if(item.getItemId()==R.id.main_find_friends_option){

        }


        return true;

    }
}
