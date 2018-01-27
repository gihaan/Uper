package com.example.gihan.uper.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gihan.uper.R;
import com.example.gihan.uper.fragment.WelcomeFragment;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        //TO PUT FRAGMENT ON ACTIVITY

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.welcome_activity, new WelcomeFragment())
                    .commit();
        }
    }
}
