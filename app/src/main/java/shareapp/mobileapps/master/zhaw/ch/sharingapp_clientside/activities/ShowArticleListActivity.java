package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataListener;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ServerDataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.helpers.ItemviewAdapter;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.helpers.Status;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class ShowArticleListActivity extends AppCompatActivity implements DataListener {

    private ItemviewAdapter itemviewAdapter;
    private ProgressDialog pd;
    //following field makes sure that items are not loaded twice (in onCreate and onResume)
    private boolean onCreateWasExecutedBefore = false;
    public static final String EXTRA_ITEM = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onCreateWasExecutedBefore = true;

        DataService dataService = new ServerDataService(this);
        itemviewAdapter = new ItemviewAdapter(this.getBaseContext());

        pd = new ProgressDialog(this);
        pd.setTitle("Gegenstände werden geladen");
        pd.setMessage("Bitte warten.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        dataService.deliverAllItems(this);

        ListView listView = findViewById(R.id.showArticlelistListview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = (Item) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ShowArticleListActivity.this, ShowArticleDetailActivity.class);
                intent.putExtra(EXTRA_ITEM, item);
                startActivity(intent);
            }
        });
    }

    @Override
    public void receiveData(Item[] items, Status status, String message) {
        pd.cancel();
        if (status == Status.SUCCESS && items != null) {
            Arrays.sort(items);
            itemviewAdapter.setItemList(items);
            ListView listView = findViewById(R.id.showArticlelistListview);
            listView.setAdapter(itemviewAdapter);
        }
        if (status == Status.FAILURE){
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            Intent intent = new Intent( this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!onCreateWasExecutedBefore) {
            DataService dataService = new ServerDataService(this);
            dataService.deliverAllItems(this);
        }
        onCreateWasExecutedBefore = false;
    }
}
