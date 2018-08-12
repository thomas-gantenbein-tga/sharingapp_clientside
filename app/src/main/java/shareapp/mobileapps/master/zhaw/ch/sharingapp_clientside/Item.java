package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside;

import java.util.UUID;

public class Item {
    private String ownerId;
    private UUID itemId;
    private String title;
    private String category;
    private String description;
    private String city;
    private String zipCode;
    private String telephoneNumber;

    public String getOwnerId() {
        return ownerId;
    }
}
