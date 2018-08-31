package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataListener;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.Endpoint;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ServerDataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.Status;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class PseudoLoginActivity extends AppCompatActivity implements DataListener {

    public static final String EXTRA_ITEMS = "items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pseudo_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void receiveData(Item[] items, Status status, String message) {
        if (status == Status.SUCCESS ) {
            Intent intent = new Intent(this, DeleteItemOverviewActivity.class);
            intent.putExtra(EXTRA_ITEMS, items);
            startActivity(intent);
        } else {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }
    }

    public void getUserItems(View view) {
        DataService dataService = new ServerDataService(this, Endpoint.LOCALHOST);
        TextView userNameTextField = findViewById(R.id.pseudoLoginUsername);
        String userName = userNameTextField.getText().toString();
        dataService.deliverUserItems(this, userName);
    }
}
