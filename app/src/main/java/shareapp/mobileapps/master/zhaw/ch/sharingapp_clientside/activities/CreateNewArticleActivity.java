package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class CreateNewArticleActivity extends AppCompatActivity implements DataListener {

    private ProgressDialog pd;
    private long timeAtSaveAction;
    private static final int MINIMUM_SAVE_DURATION_IN_MILLIS = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_article);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = findViewById(R.id.spinnerArticleCategory);
        String[] r = getResources().getStringArray(R.array.articleCategory);
        ArrayAdapter<String> adapter = new CustomArrayAdapter(this,
                android.R.layout.simple_spinner_item, r);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onCreateButtonClick(View view) {
        DataService dataService = new ServerDataService(this, Endpoint.LOCALHOST);
        String ownerId = ((TextView) findViewById(R.id.editTextArticleOwnerId)).getText().toString();
        String title = ((TextView) findViewById(R.id.editTextArticleTitle)).getText().toString();
        Spinner spinner = findViewById(R.id.spinnerArticleCategory);
        String category = (String) spinner.getSelectedItem();
        String description= ((TextView) findViewById(R.id.editTextArticleDescription)).getText().toString();
        String address = ((TextView) findViewById(R.id.editTextArticleAddress)).getText().toString();
        String city = ((TextView) findViewById(R.id.editTextArticleCity)).getText().toString();
        String zipCode = ((TextView) findViewById(R.id.editTextArticleZipCode)).getText().toString();
        String telephone = ((TextView) findViewById(R.id.editTextArticleTelephoneNumber)).getText().toString();

        Item item = new Item(ownerId, title, category, description, address,city, zipCode, telephone);
        pd = new ProgressDialog(this);
        pd.setTitle("Gegenstand wird geteilt");
        pd.setMessage("Bitte warten.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        timeAtSaveAction = System.currentTimeMillis();

        dataService.saveItem(item, this);

    }

    @Override
    public void receiveData(Item[] items, Status status, String message) {
        Context context = getApplicationContext();
        waitIfTooFast();
        int duration = Toast.LENGTH_LONG;
        pd.cancel();
        if (status == Status.SUCCESS) {
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            clearTextFields();
        } else if (status == Status.FAILURE){
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }
    }

    private void waitIfTooFast() {
        long saveDurationInMillis = System.currentTimeMillis() - timeAtSaveAction;
        if (saveDurationInMillis < MINIMUM_SAVE_DURATION_IN_MILLIS) {
            try {
                Thread.sleep(MINIMUM_SAVE_DURATION_IN_MILLIS - saveDurationInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearTextFields() {
        ((TextView) findViewById(R.id.editTextArticleOwnerId)).setText("");
        ((TextView) findViewById(R.id.editTextArticleTitle)).setText("");
        ((TextView) findViewById(R.id.editTextArticleDescription)).setText("");
        ((TextView) findViewById(R.id.editTextArticleAddress)).setText("");
        ((TextView) findViewById(R.id.editTextArticleCity)).setText("");
        ((TextView) findViewById(R.id.editTextArticleZipCode)).setText("");
        ((TextView) findViewById(R.id.editTextArticleTelephoneNumber)).setText("");
    }
}
