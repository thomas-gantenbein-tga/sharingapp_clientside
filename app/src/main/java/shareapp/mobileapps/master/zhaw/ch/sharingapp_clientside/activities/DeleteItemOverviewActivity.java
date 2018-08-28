package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ItemviewAdapter;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class DeleteItemOverviewActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ItemviewAdapter itemviewAdapter = new ItemviewAdapter(this.getBaseContext());
        ListView listView = findViewById(R.id.deleteItemsListview);

        Intent intent = getIntent();
        Item[] items = (Item[]) intent.getSerializableExtra(PseudoLoginActivity.EXTRA_ITEMS);
        itemviewAdapter.setItemList(items);
        listView.setAdapter(itemviewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = (Item) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(DeleteItemOverviewActivity.this, DeleteItemDetailActivity.class);
                intent.putExtra(EXTRA_ITEM, item);
                startActivity(intent);
            }
        });
    }

}
