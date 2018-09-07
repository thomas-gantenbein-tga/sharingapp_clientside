package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataListener;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ServerDataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.helpers.Status;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class ShowArticleDetailActivity extends AppCompatActivity implements DataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Item item = (Item) intent.getSerializableExtra(ShowArticleListActivity.EXTRA_ITEM);
        TextView title = findViewById(R.id.showArticleDetailTitle);
        TextView category = findViewById(R.id.showArticleDetailCategory);
        TextView description = findViewById(R.id.showArticleDetailDescription);
        TextView address = findViewById(R.id.showArticleDetailAddress);
        TextView phone = findViewById(R.id.showArticleDetailPhone);

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
        DataService dataService = new ServerDataService(this);
        dataService.deliverItemWithPictureOnly(this, item.getItemId());
    }

    @Override
    public void receiveData(Item[] items, Status status, String message) {
        if (status == Status.SUCCESS && items[0].getPicture() != null && !items[0].getPicture().isEmpty()) {
            Bitmap bitmap = items[0].getPictureAsBitmap();
            ImageView imageView = findViewById(R.id.showArticleDetailPicture);
            imageView.setImageBitmap(bitmap);
        }
    }
}
