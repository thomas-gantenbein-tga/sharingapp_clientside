package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling;

import android.support.v7.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

public class ServerDataService implements DataService {

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
                    alertListener(itemList, Status.SUCCESS, String.valueOf(response.statusCode));
                }
            };
    private final Response.ErrorListener onResponseError =
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    alertListener(null, Status.FAILURE, error.toString());
                }
            };

    public ServerDataService(AppCompatActivity activity, Endpoint endpoint) {
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
    public void saveItem(Item item, DataListener listener) {
        this.listener = listener;
        // if submitted item has an itemId, that itemId is ignored by the server
        // itemId is set by the server
        Gson gson = new Gson();
        String requestBody = gson.toJson(item, Item.class);
        String urlString = new StringBuilder()
                .append(endpoint.getUrlBasePath())
                .append("/items/add")
                .toString();
        Request<NetworkResponse> request = new NetworkResponseRequest(Request.Method.POST,
                urlString, requestBody, onResponse, onResponseError);
        requestQueue.add(request);
    }

    @Override
    public void alertListener(Item[] items, Status status, String message) {
        String listenerMessage = null;
        if (status == Status.SUCCESS && message.equals("201")) {
            listenerMessage = "Gegenstand erfolgreich geteilt";
        } else if (status == Status.SUCCESS && message.equals("200")) {
            listenerMessage = "Alle Gegenstände vom Server geholt";
        } else if (status == Status.SUCCESS && message.equals("404")) {
            listenerMessage = "Keine Gegenstände verfügbar auf Server";
        } else if (status == Status.FAILURE) {
            listenerMessage = "Hoppla, da ist etwas schiefgegangen. Versuchen Sie es noch einmal." +
                    "Fehlerdetail: " + message;
        }
        listener.receiveData(items, status, listenerMessage);
    }



}
