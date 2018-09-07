package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public interface DataListener {
    /**
    Called by methods in DataService interface.
     */
    void receiveData(@Nullable Item[] items, @NonNull Status status, @NonNull String message);
}
