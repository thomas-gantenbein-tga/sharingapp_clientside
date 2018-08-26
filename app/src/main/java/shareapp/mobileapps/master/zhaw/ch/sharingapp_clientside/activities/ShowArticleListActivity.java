package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataListener;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.Endpoint;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ItemviewAdapter;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ServerDataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.Status;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class ShowArticleListActivity extends AppCompatActivity implements DataListener {

    private ItemviewAdapter itemviewAdapter;
    public static final String EXTRA_ITEM = "item";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article_list);

        DataService dataService = new ServerDataService(this, Endpoint.LOCALHOST);
        itemviewAdapter = new ItemviewAdapter(this.getBaseContext());
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
        itemviewAdapter.setItemList(items);
        ListView listView = findViewById(R.id.showArticlelistListview);
        listView.setAdapter(itemviewAdapter);
    }
}
