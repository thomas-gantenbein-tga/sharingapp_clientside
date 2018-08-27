package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public interface DataService {
    void deliverAllItems(DataListener listener);
    void saveItem(Item item, DataListener listener);
    void alertListener(Item[] items, Status status, String message);
    void findItems(DataListener listener, String... params);
}
