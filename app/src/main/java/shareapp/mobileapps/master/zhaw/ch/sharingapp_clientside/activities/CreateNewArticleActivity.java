package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.R;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.DataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.Endpoint;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling.ServerDataService;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class CreateNewArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_article);

        Spinner spinner = findViewById(R.id.spinnerArticleCategory);
        String[] r = getResources().getStringArray(R.array.articleCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, r) {
            // nicht unbedingt nötig, aber hübsch: Erster Eintrag verschwindet, wenn Dropdown aufgeht
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
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onCreateButtonClick(View view) {
        DataService dataService = new ServerDataService(this, Endpoint.LOCALHOST);
        //hier irgendwie an die Textfelder rankommen und daraus ein Item generieren
        //etwa so: ((TextView) findViewById(R.id.editTextArticleZipCode)).getText()
        Item item = new Item("tgantenbein", "Super Teil", "Haushalt", "Ein wirklich super Teil", "Wässerwiesenstrasse 67a","Winterthur", "8408", "111");
        dataService.saveItem(item);
    }

}
