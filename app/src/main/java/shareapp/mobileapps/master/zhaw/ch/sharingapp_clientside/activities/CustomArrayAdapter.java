package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Used to create a custom spinner where the first element is hidden when the dropdown
 * appears.
 */
public class CustomArrayAdapter extends ArrayAdapter<String>{

    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View v = null;
        // If this is the initial dummy entry, make it hidden
        if (position == 0) {
            TextView tv = new TextView(getContext());
            tv.setHeight(0);
            tv.setVisibility(View.GONE);
            v = tv;
        }
        else {
            // Pass convertView as null to prevent reuse of special case views
            v = super.getDropDownView(position, null, parent);
        }
        // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
        parent.setVerticalScrollBarEnabled(false);
        return v;
    }
}
