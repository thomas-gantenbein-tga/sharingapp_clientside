package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public interface DataService {
    void deliverAllItems(DataListener listener);
    void saveItem(Item item);
}
