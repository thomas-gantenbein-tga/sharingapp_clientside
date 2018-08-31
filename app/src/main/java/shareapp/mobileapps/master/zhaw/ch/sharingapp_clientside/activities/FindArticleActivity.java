package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataListener;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.Endpoint;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ServerDataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.Status;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class FindArticleActivity extends AppCompatActivity implements DataListener{

    public static final String EXTRA_ITEMS = "items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = findViewById(R.id.findArticleCategory);
        String[] r = getResources().getStringArray(R.array.articleCategoryWithEmpty);
        ArrayAdapter<String> adapter = new CustomArrayAdapter(this,
                android.R.layout.simple_spinner_item, r);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onFindButtonClick(View view) {
        String ownerId = String.format("ownerId=%s", ((TextView) findViewById(R.id.findArticleOwnerId)).getText().toString());
        String title = String.format("title=%s", ((TextView) findViewById(R.id.findArticleTitle)).getText().toString());
        Spinner spinner = findViewById(R.id.findArticleCategory);
        String category = String.format("category=%s", (String) spinner.getSelectedItem());
        String description= String.format("description=%s", ((TextView) findViewById(R.id.findArticleDescription)).getText().toString());
        String address = String.format("address=%s", ((TextView) findViewById(R.id.findArticleAddress)).getText().toString());
        String city = String.format("city=%s", ((TextView) findViewById(R.id.findArticleCity)).getText().toString());
        String zipCode = String.format("zipCode=%s", ((TextView) findViewById(R.id.findArticleZipCode)).getText().toString());
        String telephone = String.format("telephone=%s", ((TextView) findViewById(R.id.findArticleTelephone)).getText().toString());

        DataService dataService = new ServerDataService(this, Endpoint.LOCALHOST);
        dataService.findItems(this, ownerId, title, category, description, address, city, zipCode, telephone);
    }

    @Override
    public void receiveData(Item[] items, Status status, String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        if (status == Status.SUCCESS) {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra(EXTRA_ITEMS, items);
            startActivity(intent);
        } else if (status == Status.FAILURE){
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }
    }
}
