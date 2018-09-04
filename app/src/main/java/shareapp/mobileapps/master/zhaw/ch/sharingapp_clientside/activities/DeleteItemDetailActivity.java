package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataListener;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.Endpoint;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ServerDataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.Status;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Constants;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class DeleteItemDetailActivity extends AppCompatActivity implements DataListener{
    public static final String ITEMID_EXTRA = "result";
    private String itemId;
    private Button button;
    private ProgressBar progressBar;
    private static final int MINIMUM_DELETE_DURATION_IN_MILLIS = 1500;
    private long timeAtDeleteAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        DataService dataService = new ServerDataService(this, Endpoint.LOCALHOST);
        dataService.deliverItemWithPictureOnly(this, item.getItemId());
    }

    public void deleteItem(View view) {
        button = findViewById(R.id.deleteItemDetailButton);
        button.setEnabled(false);
        button.setText("");
        progressBar = findViewById(R.id.deleteItemDetailProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        DataService dataService = new ServerDataService(this, Endpoint.LOCALHOST);
        timeAtDeleteAction = System.currentTimeMillis();
        dataService.deleteItem(this, itemId);
    }


    @Override
    public void receiveData(Item[] items, Status status, String message) {
        if (status == Status.SUCCESS && items != null && items[0].getPicture() != null) {
            ImageView imageView = findViewById(R.id.deleteArticleDetailPicture);
            if (items[0].getPicture().isEmpty()) {
                imageView.setVisibility(View.GONE);
            } else {
                Bitmap bitmap = items[0].getPictureAsBitmap();
                imageView.setImageBitmap(bitmap);
            }
        }

        if (status == Status.SUCCESS && message.equals(Constants.itemDeleted)) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            waitIfTooFast();
            Intent returnIntent = new Intent();
            returnIntent.putExtra(ITEMID_EXTRA, itemId);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

        if (status == Status.FAILURE){
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }
    }

    private void waitIfTooFast() {
        long deleteDurationInMillis = System.currentTimeMillis() - timeAtDeleteAction;
        if (deleteDurationInMillis < MINIMUM_DELETE_DURATION_IN_MILLIS) {
            try {
                Thread.sleep(MINIMUM_DELETE_DURATION_IN_MILLIS - deleteDurationInMillis);
            } catch (InterruptedException e) {
                //ignore
            }
        }
    }
}
