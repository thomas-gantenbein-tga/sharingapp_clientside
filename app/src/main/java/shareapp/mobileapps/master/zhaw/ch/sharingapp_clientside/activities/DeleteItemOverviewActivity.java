package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.helpers.ItemviewAdapter;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class DeleteItemOverviewActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "item";
    public static final int REQUEST_CODE = 1;
    private ItemviewAdapter itemviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item_overview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemviewAdapter = new ItemviewAdapter(this.getBaseContext());
        ListView listView = findViewById(R.id.deleteItemsListview);
        Intent intent = getIntent();
        Item[] items = (Item[]) intent.getSerializableExtra(PseudoLoginActivity.EXTRA_ITEMS);
        Arrays.sort(items);
        itemviewAdapter.setItemList(items);
        listView.setAdapter(itemviewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = (Item) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(DeleteItemOverviewActivity.this, DeleteItemDetailActivity.class);
                intent.putExtra(EXTRA_ITEM, item);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    /**
     * Makes sure that the item is deleted not only from the DataService, but from the listview
     * as well.
     * @param requestCode
     * @param resultCode: is set to Activity.Result_OK only when one of the items is deleted
     * @param data: contains the ID of the deleted item
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String itemId = data.getStringExtra(DeleteItemDetailActivity.ITEMID_EXTRA);
                itemviewAdapter.removeItem(itemId);
            }
        }
    }
}
