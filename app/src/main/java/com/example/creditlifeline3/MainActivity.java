package com.example.creditlifeline3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
//import android.support.v7.app.AppCompatActivity;


import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(MainActivity.this);

        // to load the EMI_Fragment by default
        loadFragments(new EMI_Fragment());
    }

    private boolean loadFragments(Fragment fragment){
        //here it loads the fragment which has been selected
        if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameContainer,fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //here it grabs which menu item has been clicked
        Fragment fragment = null;
        switch(menuItem.getItemId()){
            case R.id.navigation_emi :
                fragment = new EMI_Fragment();
                break;

            case R.id.navigation_sip :
                fragment = new SIP_Fragment();
                break;
        }

        return loadFragments(fragment);
    }
}
