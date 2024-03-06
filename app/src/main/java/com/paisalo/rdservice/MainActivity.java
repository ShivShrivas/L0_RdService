package com.paisalo.rdservice;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.paisalo.rdservice.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    int AUTHENTICATION_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.responsedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("PID", binding.responsedata.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
            }
        });

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String responseXml = "<PidOptions ver=\"1.0\"><Opts env=\"P\" fCount=\"1\" fType=\"0\" iCount=\"0\" iType=\"\" pCount=\"0\" pType=\"\" format=\"0\" pidVer=\"2.0\" timeout=\"20000\"  wadh=\"\" posh=\"UNKNOWN\"/><Demo></Demo><CustOpts></CustOpts></PidOptions>";

                Intent intent = new Intent("in.gov.uidai.rdservice.fp.CAPTURE");
                intent.setPackage("com.scl.rdservice");
                Log.d("TAG", "onClick: "+responseXml);
                intent.putExtra("PID_OPTIONS", responseXml);
                startActivityForResult(intent, AUTHENTICATION_REQUEST);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if
        (requestCode == AUTHENTICATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                    String pidData = b.getString("PID_DATA"); // in this varaible you will get Pid data
                    String dnc = b.getString("DNC", ""); // you will get value in this variable when your finger

                    String dnr = b.getString("DNR", ""); // you will get value in this variable when
                    Log.d("TAG", "onActivityResult: "+pidData);
                    binding.responsedata.setText(pidData);
                 }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}