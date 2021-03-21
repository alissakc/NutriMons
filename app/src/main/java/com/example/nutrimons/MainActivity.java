package com.example.nutrimons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.example.nutrimons.database.AppDatabase;
import com.example.nutrimons.database.Token;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements DrawerController {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_registration, R.id.nav_addMeal, R.id.nav_exercise, R.id.nav_meal, R.id.nav_mealPlan, R.id.nav_nutrientInformation, R.id.nav_nutrientOverview, R.id.nav_profile, R.id.nav_scanBarcode, R.id.nav_tamagotchi, R.id.nav_tamagotchiShop, R.id.nav_water, R.id.nav_calendar)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());
        try
        {
            Token t = mDb.tokenDao().getToken();
            Log.d("tables initialized", String.valueOf(t.areTablesInitialized));
            if(t.areTablesInitialized == false)
            {
                NutrientTablesApi nta = new NutrientTablesApi(mDb);
                nta.Initialize(getAssets());
                t.areTablesInitialized = true;
                mDb.tokenDao().insert(t);
            }
        }
        catch(NullPointerException e) //fresh database
        {
            Log.d("fresh tables", "true");
            NutrientTablesApi nta = new NutrientTablesApi(mDb);
            nta.Initialize(getAssets());
        }
    }

    @Override
    protected void onStop () {
        super .onStop() ;
        startService( new Intent( this, NotificationService. class )) ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void setDrawer_Locked() {
        // locks navigation drawer
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toolbar.setNavigationIcon(null);
    }

    @Override
    public void setDrawer_UnLocked() {
        // unlocks navigation drawer
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}