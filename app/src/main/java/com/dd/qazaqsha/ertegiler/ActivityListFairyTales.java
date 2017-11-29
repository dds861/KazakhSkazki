package com.dd.qazaqsha.ertegiler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class ActivityListFairyTales extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;

    SharedPreferences sPref;
    final String SAVED_TEXT = "selectedSkazkiCategoryPosition";
    final String SAVED_TEXT2 = "selectedSkazkiCategoryName";
    int selectedSkazkiCategoryPosition;
    String selectedSkazkiCategoryName;

    //Реклама Admob
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Восстанавливаем тему после лого
        setTheme(R.style.AppTheme);

        Log.i("autolog", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Реклама Admob
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();


        //получаем позицию выбранной категории
        selectedSkazkiCategoryPosition = getIntent().getIntExtra("selectedSkazkiCategoryPosition", -1);
        selectedSkazkiCategoryName = getIntent().getStringExtra("selectedSkazkiCategoryName");
        if (selectedSkazkiCategoryPosition == -1) {
            loadText();
        }

        //Logo and app name on action bar
        setLogoAndAppName(selectedSkazkiCategoryName);


        //получаем список категории на латинице
        List<String> stringListSkazki = databaseAccess.getNames("names_of_skazki_latin");

        //из списка категории на латинице выбраем ту что выбрали
        final String nameOfSelectedCategoryInLatin = stringListSkazki.get(selectedSkazkiCategoryPosition);


        //получаем все сказки из выбранной категории
        final List<String> skazkiList = databaseAccess.getNames(nameOfSelectedCategoryInLatin);

        listView = (ListView) findViewById(R.id.listview2);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, skazkiList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ActivityFairyTaleText.class);
                intent.putExtra("nameOfSelectedCategoryInLatin", nameOfSelectedCategoryInLatin);
                intent.putExtra("SelectedSkazkiCategoryPosition", selectedSkazkiCategoryPosition);
                intent.putExtra("SelectedSkazkaPosition", position);
                intent.putExtra("SelectedSkazkaName", skazkiList.get(position));
                startActivity(intent);
                saveText(selectedSkazkiCategoryPosition, selectedSkazkiCategoryName);
            }
        });
    }

    void saveText(int selectedSkazkiCategoryPosition, String selectedSkazkiCategoryName) {
        getPreferences(MODE_PRIVATE).edit().putInt(SAVED_TEXT, selectedSkazkiCategoryPosition).apply();
        getPreferences(MODE_PRIVATE).edit().putString(SAVED_TEXT2, selectedSkazkiCategoryName).apply();
    }

    void loadText() {
        selectedSkazkiCategoryPosition = getPreferences(MODE_PRIVATE).getInt(SAVED_TEXT, 0);
        selectedSkazkiCategoryName = getPreferences(MODE_PRIVATE).getString(SAVED_TEXT2, "");

    }

    //Logo and app name on action bar
    private void setLogoAndAppName(String selectedSkazkiCategoryName) {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_aldar_kose);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle(selectedSkazkiCategoryName);

    }
}
