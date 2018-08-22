package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataListener;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.Endpoint;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ItemviewAdapter;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ServerDataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class ShowArticleListActivity extends AppCompatActivity implements DataListener {

    private ItemviewAdapter itemviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article_list);

        DataService dataService = new ServerDataService(this, Endpoint.LOCALHOST);
        itemviewAdapter = new ItemviewAdapter(this.getBaseContext());
        dataService.deliverAllItems(this);


    }

    @Override
    public void receiveData(Item[] items) {
        itemviewAdapter.setItemList(items);
        ListView listView = findViewById(R.id.showArticlelistListview);
        listView.setAdapter(itemviewAdapter);
    }
}
