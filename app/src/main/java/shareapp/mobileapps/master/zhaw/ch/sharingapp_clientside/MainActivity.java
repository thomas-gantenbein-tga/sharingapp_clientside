package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private final Response.Listener<NetworkResponse> onPostsLoaded =
            new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    Gson gson = new Gson();
                    String stringResponse = NetworkResponseRequest.parseResponseToString(response);
                    Item[] itemList = gson.fromJson(stringResponse, Item[].class);
                    if (itemList != null) {
                        Log.e("test", itemList[0].getOwnerId());
                    }
                    Log.e("testHttpCode", String.valueOf(response.statusCode));

                    /*
                    Better:
                    Type collectionType = new TypeToken<Collection<channelSearchEnum>>(){}.getType();
                    Collection<channelSearchEnum> enums = gson.fromJson(yourJson, collectionType);
                     */
                }


            };
    private final Response.ErrorListener onPostsError =
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("PostActivity", error.toString());
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        serverCall();
    }

    private void serverCall() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Request<NetworkResponse> request = new NetworkResponseRequest(Request.Method.GET, "http://10.0.2.2:8080/items", null, onPostsLoaded, onPostsError);
        requestQueue.add(request);
        // if submitted item has an itemId, that itemId is ignored by the server
        // itemId is set by the server
        Item item = new Item("tgantenbein", "Super Teil", "Haushalt", "Ein wirklich super Teil", "Wülflingerstr. 212","Winterthur", "8408", "111");
        Gson gson = new Gson();
        String requestBody = gson.toJson(item, Item.class);
        request = new NetworkResponseRequest(Request.Method.POST, "http://10.0.2.2:8080/items/add", requestBody, onPostsLoaded, onPostsError);
        requestQueue.add(request);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
