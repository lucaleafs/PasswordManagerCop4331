package net.aisoftworks.passwords.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent prev = getIntent();

        EditText sfield = (EditText) findViewById(R.id.searchBox);
        //todo
        listView = (ExpandableListView)findViewById(R.id.lvExp);
        retrieve();

        Button signout = (Button)findViewById(R.id.signout);
        // button click listener that brings up a form for adding a contact.
        // it was really weird on how it does it. You have to instantiate the fields/buttons within the onClick function.
        // from here, it builds the alert form and sends it when they finish typing
        signout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();

            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView)view.findViewById(R.id.lblListItem);
                String text = textView.getText().toString();
                setClipboard(getApplicationContext(), text);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        Button addBtn = (Button)findViewById(R.id.addBtn);
        // button click listener that brings up a form for adding a contact.
        // it was really weird on how it does it. You have to instantiate the fields/buttons within the onClick function.
        // from here, it builds the alert form and sends it when they finish typing
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                AlertDialog.Builder addBuilder = new AlertDialog.Builder(MainActivity.this);
                View addView = getLayoutInflater().inflate(R.layout.activity_add, null);
                final EditText username = (EditText) addView.findViewById(R.id.username);
                final EditText password = (EditText) addView.findViewById(R.id.password);
                final EditText favorite = (EditText) addView.findViewById(R.id.favorite);
                final EditText url = (EditText) addView.findViewById(R.id.url);
                final EditText icon = (EditText) addView.findViewById(R.id.icon);
                final EditText account = (EditText) addView.findViewById(R.id.account);
                Button add = (Button) addView.findViewById(R.id.add);

                // click listener specifically for the pop up form. calls the add function below

                addBuilder.setView(addView);
                final AlertDialog addDialog = addBuilder.create();
                addDialog.show();

                add.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        add(username.getText().toString().trim(), password.getText().toString().trim(),
                                favorite.getText().toString().trim(), url.getText().toString().trim(),
                                icon.getText().toString().trim(), prev.getStringExtra("ID"), account.getText().toString().trim());
                        retrieve();

                        addDialog.dismiss();
                    }

                });
            }

        });

        Button create = (Button) findViewById(R.id.Generate);
        create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String newPass = passwordGenerator();

                // Toast to display generated password
                Toast.makeText(getApplicationContext(), newPass, Toast.LENGTH_SHORT).show();

                setClipboard(getApplicationContext(), newPass);
            }
        });

        //Delete and edit currently not functional
        /*
        Button delBtn = (Button)findViewById(R.id.delBtn);
        // button click listener that brings up a form for adding a contact.
        // it was really weird on how it does it. You have to instantiate the fields/buttons within the onClick function.
        // from here, it builds the alert form and sends it when they finish typing
        delBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                AlertDialog.Builder delBuilder = new AlertDialog.Builder(MainActivity.this);
                View addView = getLayoutInflater().inflate(R.layout.activity_delete, null);
                final EditText delField = (EditText) addView.findViewById(R.id.delField);
                Button alertDelBtn = (Button) addView.findViewById(R.id.alertDelBtn);

                // click listener specifically for the pop up form. calls the add function below

                delBuilder.setView(addView);
                final AlertDialog delDialog = delBuilder.create();
                delDialog.show();

                alertDelBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        delete(delField.getText().toString().trim(), prev.getStringExtra("ID"));
                        retrieve();

                        delDialog.dismiss();
                    }

                });
            }

        });

        Button editBtn = (Button)findViewById(R.id.mainEditBtn);
        // button click listener that brings up a form for adding a contact.
        // it was really weird on how it does it. You have to instantiate the fields/buttons within the onClick function.
        // from here, it builds the alert form and sends it when they finish typing
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                AlertDialog.Builder editBuilder = new AlertDialog.Builder(MainActivity.this);
                View addView = getLayoutInflater().inflate(R.layout.activity_edit, null);
                final EditText oldUsername = (EditText) addView.findViewById(R.id.oldUname);
                final EditText username = (EditText) addView.findViewById(R.id.username);
                final EditText password = (EditText) addView.findViewById(R.id.password);
                final EditText favorite = (EditText) addView.findViewById(R.id.favorite);
                final EditText url = (EditText) addView.findViewById(R.id.url);
                final EditText icon = (EditText) addView.findViewById(R.id.iconUrl);
                final EditText account = (EditText) addView.findViewById(R.id.account);
                Button alertEdit = (Button) addView.findViewById(R.id.alertEditBtn);

                // click listener specifically for the pop up form. calls the add function below

                editBuilder.setView(addView);
                final AlertDialog editDialog = editBuilder.create();
                editDialog.show();

                alertEdit.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        edit(oldUsername.getText().toString().trim(),username.getText().toString().trim(), password.getText().toString().trim(),
                                favorite.getText().toString().trim(), url.getText().toString().trim(),
                                icon.getText().toString().trim(), prev.getStringExtra("ID"), account.getText().toString().trim());
                        retrieve();
                        editDialog.dismiss();
                    }

                });
            }

        });
        */

        final Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                AlertDialog.Builder searchBuilder = new AlertDialog.Builder(MainActivity.this);
                View searchView = getLayoutInflater().inflate(R.layout.activity_search, null);
                final EditText searchfield = (EditText) searchView.findViewById(R.id.searchBox);
                Button searchButton = (Button) searchView.findViewById(R.id.searchButton);
                // click listener specifically for the pop up form. calls the add function below


                searchBuilder.setView(searchView);
                final AlertDialog searchDialog = searchBuilder.create();
                searchDialog.show();
                searchButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        searchFunction(searchfield.getText().toString().trim(), prev.getStringExtra("ID"));
                        searchDialog.dismiss();

                    }

                });
            }

        });
    }



    private void delete(String id, String account){

        String url = "http://passwords.aisoftworks.net/deleteAccountAndroid.php";
        RequestQueue rq = Volley.newRequestQueue(this);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userId", id);
        parameters.put("account", account);


        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(parameters),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getString("error").equals("N")) {
                                Toast.makeText(getApplicationContext(), "Record successfully added!", Toast.LENGTH_SHORT).show();
                                retrieve();
                            } else{

                                Toast.makeText(getApplicationContext(), response.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        rq.add(req);
    }

    // add function that gets called from the popup form.
    private void add(String username, String password, String favorite, String u, String icon, String id, String account){

        String url = "http://passwords.aisoftworks.net/addPassword.php";
        RequestQueue rq = Volley.newRequestQueue(this);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", username);
        parameters.put("password", password);
        parameters.put("favorite", favorite);
        parameters.put("iconUrl", icon);
        parameters.put("url", u);
        parameters.put("userId", id);
        parameters.put("account", account);


        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(parameters),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getString("error").equals("No Error")) {
                                Toast.makeText(getApplicationContext(), "Record successfully added!", Toast.LENGTH_SHORT).show();
                                retrieve();
                            } else{

                                Toast.makeText(getApplicationContext(), response.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        rq.add(req);
    }

    // search function
    private void searchFunction( String account, String userID){

        String url = "http://passwords.aisoftworks.net/searchPasswordsAndroid.php";
        RequestQueue rq = Volley.newRequestQueue(this);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("account", account);
        parameters.put("userId", userID);


        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(parameters),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){


                ArrayList<String> parseList = new ArrayList<>();

                    String account = null;
                    String username = null;
                    String password = null;
                    String url = null;
                    try {
                        account = response.getString("account");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        username = response.getString("username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        password = response.getString("password");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        url = response.getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    parseList.add(account);
                    parseList.add(username);
                    parseList.add(password);
                    parseList.add(url);

                    try {
                        if(response.getString("error").equals("No Records Found")) {
                            Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();
                            retrieve();
                            return;
                        }
                    } catch (JSONException e) {
                        Log.e("at error response", "message", e);
                    }

                    initData(parseList);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                retrieve();
            }
        });

        rq.add(req);
    }

    private void initData(ArrayList<String> parseList)
    {
        ArrayList<String> listDataHeader = new ArrayList<String>();
        listHash = new HashMap<>();
        int len = parseList.size()/4;

        for (int i = 0; i < len; i++)
        {
            int base = i*4;
            listDataHeader.add(parseList.get(base));

            List<String> tempString = new ArrayList<String>();
            tempString.add(parseList.get(base+1));
            tempString.add(parseList.get(base+2));
            tempString.add(parseList.get(base+3));

            listHash.put(listDataHeader.get(i), tempString);
        }
        //Handle buttons and add onClickListeners

        //listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listHash);
        listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listHash);
        listView.setAdapter(listAdapter);
    }

    private void setClipboard(Context context, String text) {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
    }

    private static String passwordGenerator()
    {
        Random randomizer = new Random();

        // Lets you add chars to a string
        StringBuilder newPassword = new StringBuilder();

        // Gives you password length between 6 and 15 characters
        int min = 6;
        int max = 15;
        int passwordLength = randomizer.nextInt(max-min) + min;

        // Generate the password
        char temp;
        for (int i=0; i<passwordLength; i++)
        {
            temp = (char)(randomizer.nextInt(96)+32);
            newPassword.append(temp);
        }

        // Returns password as string
        return newPassword.toString();
    }

    // this is a custom class for handling json object requests that return json arrays.
    // Volley doesn't support this so it required some finesse
    public class CustomJsonArrayRequest extends JsonRequest<JSONArray> {

         /*
         * Creates a new request.
         * @param method the HTTP method to use
         * @param url URL to fetch the JSON from
         * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
         *   indicates no parameters will be posted along with request.
         * @param listener Listener to receive the JSON response
         * @param errorListener Error listener, or null to ignore errors.
         */
        public CustomJsonArrayRequest(int method, String url, JSONObject jsonRequest,
                                      Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
            super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                    errorListener);
        }

        @Override
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                return Response.success(new JSONArray(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }

    // Actual function that calls the backend fetch file and returns the JSON array
    // The toast message just displays the result as a long string.
    private void retrieve(){

        String url = "http://passwords.aisoftworks.net/fetchAll.php";
        RequestQueue rq = Volley.newRequestQueue(this);
        HashMap<String, String> parameters = new HashMap<String, String>();
        Intent temp = getIntent(); // gets the previously created intent
        String idString = temp.getStringExtra("ID");//retrieves ID passed from login activity
        parameters.put("id", idString);


        CustomJsonArrayRequest req = new CustomJsonArrayRequest(Request.Method.POST, url, new JSONObject(parameters), new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                ArrayList<String> parseList = new ArrayList<>();

                int len = response.length();
                for(int i = 0; i < len; i++)
                {
                    String account = null;
                    String username = null;
                    String password = null;
                    String url = null;
                    try {
                        account = response.getJSONObject(i).getString("account");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        username = response.getJSONObject(i).getString("username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        password = response.getJSONObject(i).getString("password");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        url = response.getJSONObject(i).getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    parseList.add(account);
                    parseList.add(username);
                    parseList.add(password);
                    parseList.add(url);
                }
                
                initData(parseList);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        rq.add(req);
    }

    // activity_edit function that takes in the new modified fields and passes it to the back end php file.
    private void edit( String oldAccount, String username, String password, String postUrl, String iconUrl, String favorite, String userID, String account){

        String url = "http://passwords.aisoftworks.net/editPasswordAndroid.php";
        RequestQueue rq = Volley.newRequestQueue(this);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("userId", userID);
        parameters.put("oldAccount", oldAccount);
        parameters.put("username", username);
        parameters.put("password", password);
        parameters.put("url", postUrl);
        parameters.put("iconUrl", iconUrl);
        parameters.put("favorite", favorite);
        parameters.put("account", account);


        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(parameters),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(), "Edit complete", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        rq.add(req);

    }
}
