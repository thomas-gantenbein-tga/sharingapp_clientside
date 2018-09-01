package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoFile;
    private static final float MAX_IMAGE_SIZE = 1024;

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

    public void onCreateButtonClick(View view) throws IOException {
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
        String picture;
        if (currentPhotoFile != null) {
            picture = getPhotoAsString(currentPhotoFile);
        } else {
            picture = "";
        }

        Item item = new Item(ownerId, title, category, description, address,city, zipCode, telephone, picture);
        pd = new ProgressDialog(this);
        pd.setTitle("Gegenstand wird geteilt");
        pd.setMessage("Bitte warten.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        timeAtSaveAction = System.currentTimeMillis();

        dataService.saveItem(item, this);

    }

    private String getPhotoAsString(String currentPhotoFile) throws IOException {
        File imageFile = new File(currentPhotoFile);
        if (imageFile.exists()) {
            Bitmap scaledImage = scaleDown(BitmapFactory.decodeFile(currentPhotoFile), MAX_IMAGE_SIZE, true);
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            scaledImage.compress(Bitmap.CompressFormat.JPEG, 30, bmpStream);
            byte[] scaledImageAsBytes = bmpStream.toByteArray();
            return org.apache.commons.codec.binary.Base64.encodeBase64String(scaledImageAsBytes);
        }
        return "";
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

    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // ignore
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoFile);
            ImageView imageView = findViewById(R.id.createArticleImageView);
            imageView.setImageBitmap(bitmap);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoFile = image.getAbsolutePath();
        return image;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
