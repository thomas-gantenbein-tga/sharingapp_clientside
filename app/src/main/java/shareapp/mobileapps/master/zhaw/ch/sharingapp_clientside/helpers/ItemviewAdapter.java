package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class ItemviewAdapter extends BaseAdapter {
    private Context context;

    private Item[] itemList;

    public ItemviewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemList.length;
    }

    @Override
    public Object getItem(int i) {
        return itemList[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.listview_item, null);
        }
        TextView title = vi.findViewById(R.id.showArticleListTitle);
        TextView category = vi.findViewById(R.id.showArticleListCategory);
        TextView description = vi.findViewById(R.id.showArticleListDescription);

        Item item = itemList[i];
        title.setText(item.getTitle());
        category.setText(item.getCategory());
        description.setText(item.getDescription());
        return vi;
    }

    public void setItemList(Item[] itemList) {
        this.itemList = itemList;
    }

    public void removeItem(String itemId) {
        List<Item> items = new ArrayList<>(Arrays.asList(itemList));
        for (Iterator<Item> iterator = items.iterator(); iterator.hasNext(); ) {
            Item item = iterator.next();
            if (item.getItemId().equals(itemId)) {
                iterator.remove();
                itemList = items.toArray(new Item[0]);
                this.notifyDataSetChanged();
                return;
            }
        }
    }
}
