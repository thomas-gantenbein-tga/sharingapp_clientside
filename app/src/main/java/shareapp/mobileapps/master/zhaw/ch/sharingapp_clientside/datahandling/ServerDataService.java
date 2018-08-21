package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class ServerDataService implements DataService {
    private AppCompatActivity activity;
    private DataListener listener;
    private RequestQueue requestQueue;
    private Endpoint endpoint;
    private final Response.Listener<NetworkResponse> onResponse =
            new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    Gson gson = new Gson();
                    String stringResponse = NetworkResponseRequest.parseResponseToString(response);
                    Item[] itemList = gson.fromJson(stringResponse, Item[].class);
                    listener.receiveData(itemList);
                }
            };
    private final Response.ErrorListener onResponseError =
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("PostActivity", error.toString());
                }
            };

    public ServerDataService(AppCompatActivity activity, Endpoint endpoint) {
        this.activity = activity;
        this.endpoint = endpoint;
        requestQueue = Volley.newRequestQueue(activity);
    }

    @Override
    public void deliverAllItems(DataListener listener) {
        this.listener = listener;
        String urlString = new StringBuilder().append(endpoint.getUrlBasePath())
                .append("/").append("items").toString();
        Request<NetworkResponse> request = new NetworkResponseRequest(Request.Method.GET,
                urlString, null, onResponse, onResponseError);
        requestQueue.add(request);
    }

    @Override
    public void saveItem(Item item) {
        // if submitted item has an itemId, that itemId is ignored by the server
        // itemId is set by the server
        Gson gson = new Gson();
        String requestBody = gson.toJson(item, Item.class);
        Request<NetworkResponse> request = new NetworkResponseRequest(Request.Method.POST, "http://10.0.2.2:8080/items/add", requestBody, onResponse, onResponseError);
        requestQueue.add(request);
    }

}
