package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.Serializable;
import java.util.UUID;

public class Item implements Serializable, Comparable<Item> {
    private String ownerId;
    private UUID itemId;
    private String title;
    private String category;
    private String description;
    private String address;
    private String city;
    private String zipCode;
    private String telephoneNumber;
    private String picture;

    public String getPicture() {
        return picture;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public Item(String ownerId, String title, String category, String description,
                String address, String city, String zipCode, String telephoneNumber, String picture) {
        this.ownerId = ownerId;
        this.title = title;
        this.category = category;
        this.description = description;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.telephoneNumber = telephoneNumber;
        this.picture = picture;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getItemId() {
        return itemId.toString();
    }

    public Bitmap getPictureAsBitmap() {
        String encodedPictureString = this.getPicture();
        byte[] decodedPictureString = Base64.decode(encodedPictureString,0);
        return BitmapFactory.decodeByteArray(decodedPictureString, 0, decodedPictureString.length);
    }

    @Override
    public int compareTo(@NonNull Item item) {
        return this.getTitle().toLowerCase().compareTo(item.getTitle().toLowerCase());
    }
}
