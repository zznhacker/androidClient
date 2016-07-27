package com.example.zeningzhang.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends AppCompatActivity
        implements FirstFragment.OnFragmentInteractionListener, SecondFragment.OnFragmentInteractionListener,ThirdFragment.OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener {

    private String userName;
    public static final String PREFS_NAME = "LoginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        Bundle bundle = this.getIntent().getExtras();
//        userName = bundle.getString("username");
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        userName = settings.getString("username",null);
        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = FirstFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        TextView textView = (TextView) findViewById(R.id.userName);
        textView.setText(userName);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("logged");
            editor.commit();

            Log.d("zznmizzou", "Now log out and start the activity login");
            Intent intent = new Intent(HomeActivity.this,
                    LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass;

        if (id == R.id.icon1) {
            fragmentClass = FirstFragment.class;

        } else if (id == R.id.icon2) {
            showInputDialog();
            return true;
        } else if (id == R.id.icon3) {
            fragmentClass = ThirdFragment.class;
        }
        else
        {
            fragmentClass = FirstFragment.class;
        }


        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showInputDialog() {

        // get prompts.xml view

        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editName = (EditText) promptView.findViewById(R.id.enterItemName);
        final EditText editType = (EditText) promptView.findViewById(R.id.enterItemType);
        final EditText editDate = (EditText) promptView.findViewById(R.id.enterItemDate);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                        //save into db
                        AddItemTask addItemTask = new AddItemTask(editDate.getText().toString(),editType.getText().toString(),editName.getText().toString(),userName,"itemInput");
                        addItemTask.execute((Void)null);

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public class AddItemTask extends AsyncTask<Void, Void, Boolean>
    {
        private String itemDate;
        private String itemType;
        private String itemName;
        private String userName;
        private String status;



        public AddItemTask(String itemDate, String itemType, String itemName, String userName, String status) {
            this.itemDate = itemDate;
            this.itemType = itemType;
            this.itemName = itemName;
            this.userName = userName;
            this.status = status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean)
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = null;
                try {
                    fragment = (Fragment) FirstFragment.class.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
            else{

            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Map<String,String> encrypted = new HashMap<>();
            encrypted.put("itemName",itemName);
            encrypted.put("itemType",itemType);
            encrypted.put("itemDate",itemDate);
            Map<String,String> map = new HashMap<>();
            try {
                String info = RSA.runRSA(encrypted,"saveItems");
//                            map.put("itemName",editName.getText().toString());
//                            map.put("itemType",editType.getText().toString());
//                            map.put("itemDate",editDate.getText().toString());
                map.put("info",info);
                map.put("username",userName);
                map.put("status","itemInput");
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = HttpUtil.BASE_URL;
            try {
                String temp = HttpUtil.postRequest(url,map);
                Log.d("zznmizzou",temp);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
    }
}
