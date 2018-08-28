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

public class DeleteItemDetailActivity extends AppCompatActivity implements DataListener{
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Item item = (Item) intent.getSerializableExtra(DeleteItemOverviewActivity.EXTRA_ITEM);
        itemId = item.getItemId();
        TextView title = findViewById(R.id.deleteArticleDetailTitle);
        TextView category = findViewById(R.id.deleteArticleDetailCategory);
        TextView description = findViewById(R.id.deleteArticleDetailDescription);
        TextView address = findViewById(R.id.deleteArticleDetailAddress);
        TextView phone = findViewById(R.id.deleteArticleDetailPhone);

        title.setText(item.getTitle());
        category.setText(item.getCategory());
        description.setText(item.getDescription());
        String addressString = new StringBuilder()
                .append(item.getAddress())
                .append(", ")
                .append(item.getZipCode())
                .append(" ")
                .append(item.getCity())
                .toString();
        address.setText(addressString);
        phone.setText(item.getTelephoneNumber());

    }

    public void deleteItem(View view) {
        DataService dataService = new ServerDataService(this, Endpoint.LOCALHOST);
        dataService.deletItem(this, itemId);
    }


    @Override
    public void receiveData(Item[] items, Status status, String message) {
        if (status == Status.SUCCESS) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }
    }
}
