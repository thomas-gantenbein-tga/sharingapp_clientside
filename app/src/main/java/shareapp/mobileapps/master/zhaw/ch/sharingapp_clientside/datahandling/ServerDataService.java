package shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.datahandling;

import android.support.v7.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.helpers.Constants;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.helpers.Endpoint;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.helpers.Status;
import shareapp.mobileapps.master.zhaw.ch.sharingapp_clientside.model.Item;

/**
 * Uses appengine (either local or cloud) to store, find, delete shared items.
 */
public class ServerDataService implements DataService {

    private static final int SOCKET_TIMEOUT_MS = 5_000;
    private static final Endpoint endpoint = Endpoint.GOOGLE_APP;
    private DataListener listener;
    private RequestQueue requestQueue;
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


    public ServerDataService(AppCompatActivity activity) {
        requestQueue = Volley.newRequestQueue(activity);
    }

    @Override
    public void deliverAllItems(DataListener listener) {
        this.listener = listener;
        String urlString = new StringBuilder().append(endpoint.getUrlBasePath())
                .append("/").append("items").toString();
        Request<NetworkResponse> request = new NetworkResponseRequest(Request.Method.GET,
                urlString, null, onResponse, onResponseError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    @Override
    public void alertListener(Item[] items, Status status, String message) {
        String listenerMessage = null;
        if (status == Status.SUCCESS) {
            if (message.equals("201")) {
                listenerMessage = Constants.itemShared;
            } else if (message.equals("200")) {
                listenerMessage = Constants.itemsFetched;
            } else if (message.equals("404")) {
                listenerMessage = Constants.notItemsAvailable;
            } else if (message.equals("204")) {
                listenerMessage = Constants.itemDeleted;
            }
        } else if (status == Status.FAILURE) {
            if (message.endsWith("ClientError")) {
                listenerMessage = Constants.itemNotFound;
            } else {
                listenerMessage = Constants.unexpectedError + message;
            }
        }


        listener.receiveData(items, status, listenerMessage);
    }

    @Override
    public void findItems(DataListener listener, String... params) {
        this.listener = listener;
        List<String> searchParams = new ArrayList<>();
        for (String param : params) {
            if (!param.endsWith("=") && !param.contains("Kategorie wählen")) {
                searchParams.add(param);
            }
        }
        if (searchParams.isEmpty()) {
            return;
        }
        StringBuilder getParams = new StringBuilder();
        getParams.append("?").append(searchParams.get(0));
        for (int i = 1; i < searchParams.size(); i++) {
            getParams.append("&").append(searchParams.get(i));
        }
        String urlString = new StringBuilder()
                .append(endpoint.getUrlBasePath())
                .append("/items")
                .append(getParams.toString())
                .toString();
        Request<NetworkResponse> request = new NetworkResponseRequest(Request.Method.GET,
                urlString, null, onResponse, onResponseError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    @Override
    public void deliverUserItems(DataListener listener, String username) {
        this.listener = listener;
        String urlString = new StringBuilder()
                .append(endpoint.getUrlBasePath())
                .append("/items/searchByOwner/")
                .append(username)
                .toString();
        Request<NetworkResponse> request = new NetworkResponseRequest(Request.Method.GET,
                urlString, null, onResponse, onResponseError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }

    @Override
    public void deleteItem(DataListener listener, String itemId) {
        this.listener = listener;
        String urlString = new StringBuilder()
                .append(endpoint.getUrlBasePath())
                .append("/items/delete/")
                .append(itemId)
                .toString();
        Request<NetworkResponse> request = new NetworkResponseRequest(Request.Method.POST,
                urlString, null, onResponse, onResponseError);
        requestQueue.add(request);
    }

    @Override
    public void deliverItemWithPictureOnly(DataListener listener, String itemId) {
        this.listener = listener;
        String urlString = new StringBuilder()
                .append(endpoint.getUrlBasePath())
                .append("/items/")
                .append(itemId)
                .append("/pictureonly")
                .toString();
        Request<NetworkResponse> request = new NetworkResponseRequest(Request.Method.GET,
                urlString, null, onResponse, onResponseError);
        requestQueue.add(request);
    }


}
