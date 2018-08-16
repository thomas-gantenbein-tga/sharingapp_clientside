package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public interface DataListener {
    void receiveData(Item[] items);
}
