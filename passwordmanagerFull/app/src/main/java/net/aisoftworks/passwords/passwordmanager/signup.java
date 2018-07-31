package net.aisoftworks.passwords.passwordmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class signup extends AppCompatActivity {


    Button signup;
    EditText firstname, lastname, username, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        signup = (Button) findViewById(R.id.signup);
        //back = (Button) findViewById(R.id.back);
        username = (EditText) findViewById(R.id.username);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        pass = (EditText) findViewById(R.id.password);

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                signup();
            }

        });

        /*
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                finish();
            }

        });
        */
    }



    private void signup(){

        String url = "http://passwords.aisoftworks.net/signUp.php";
        RequestQueue rq = Volley.newRequestQueue(this);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("firstName", firstname.getText().toString().trim());
        parameters.put("lastName", lastname.getText().toString().trim());
        parameters.put("login", username.getText().toString().trim());
        parameters.put("password", pass.getText().toString().trim());

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(parameters),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //VolleyLog.v("Response:%n %s", response.toString(4));
                            if(Integer.parseInt(response.getString("id")) == 0) {
                                Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_LONG).show();

                            }else {
                                Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
