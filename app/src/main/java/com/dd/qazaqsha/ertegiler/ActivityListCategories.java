package com.dd.qazaqsha.ertegiler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

public class ActivityListCategories extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;
    private AdView mAdView;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Восстанавливаем тему после лого
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Реклама Admob
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);



        //Logo and app name on action bar
        setLogoAndAppName();

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        listView = (ListView) findViewById(R.id.listview1);

        final List<String> stringList = databaseAccess.getListSkazkiCategory();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ActivityListFairyTales.class);
                intent.putExtra("selectedSkazkiCategoryPosition", position);
                intent.putExtra("selectedSkazkiCategoryName", stringList.get(position));

                startActivity(intent);

            }
        });
    }

    //Logo and app name on action bar
    private void setLogoAndAppName() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_aldar_kose);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
}
