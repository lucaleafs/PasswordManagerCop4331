package net.aisoftworks.passwords.passwordmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;
import android.content.Intent;


public class LoginActivity extends AppCompatActivity {

    Button login, btn1;
    EditText username, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        login = (Button) findViewById(R.id.login);
        btn1 = (Button) findViewById(R.id.signup);
        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);

        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent suView = new Intent(LoginActivity.this, signup.class);
                startActivity(suView);
            }

        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Login();
            }
        });
    }

    public void Login(){

        String url = "http://passwords.aisoftworks.net/Login.php";
        RequestQueue rq = Volley.newRequestQueue(this);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("login", username.getText().toString().trim());
        parameters.put("password", pass.getText().toString().trim());

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(parameters),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //VolleyLog.v("Response:%n %s", response.toString(4));
                            if(Integer.parseInt(response.getString("id")) == 0) {
                                Toast.makeText(getApplicationContext(), "Incorrect Username or Password", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                Intent nextView = new Intent(LoginActivity.this, MainActivity.class);
                                nextView.putExtra("ID", response.getString("id"));
                                startActivity(nextView);
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
