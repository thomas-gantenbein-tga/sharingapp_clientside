package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling;

import android.support.annotation.Nullable;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public interface DataService {
    /**
     * deliversAllItems available for sharing
     */
    void deliverAllItems(DataListener listener);

    /**
     * Saves an item to a storage location as defined in the implementing class.
     */
    void saveItem(Item item, DataListener listener);

    /**
     * Called when the data is loaded and ready to be delivered.
     */
    void alertListener(@Nullable Item[] items, Status status, String message);

    /**
     * Find available items based on given criteria. Call alertListener to deliver the found items.
     */
    void findItems(DataListener listener, String... params);

    /**
     * Finds available items shared by the "logged in" user. Call alertListener to deliver the
     * found items.
     */
    void deliverUserItems(DataListener listener, String username);

    /**
     * Deletes an item with a given ID. Success/failure and message delivered in alertListener.
     */
    void deleteItem(DataListener listener, String itemId);

    /**
     * Will get an item from the server with a given Id with all properties null except the picture
     * content.
     */
    void deliverItemWithPictureOnly(DataListener listener, String itemId);
}
