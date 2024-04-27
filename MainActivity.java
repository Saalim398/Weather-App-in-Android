package com.example.chkweather;

import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private final String appid = "yourapiid";
    String url = "https://api.openweathermap.org/data/2.5/weather";


    TextView textresult,tvftemp,description;
    ImageView imageView;
    EditText editText;
    Button btn;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tvftemp = findViewById(R.id.tvtemp);
        description = findViewById(R.id.forecast);
        imageView = findViewById(R.id.imgv);
        textresult = findViewById(R.id.tvresult);
        editText = findViewById(R.id.etsearch);
        btn = findViewById(R.id.search_btn);


    }


    public void getWeather(View view) {
        String tempUrl = "";
        String city = editText.getText().toString().trim();
        if (city.equals("")){
            textresult.setText("City field Cannot be empty");
        }
        else {
            if(!city.equals("")){
                tempUrl = url+"?q="+city+"&appid="+appid;

            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, tempUrl,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONObject ob = response.getJSONObject("main");

                        JSONArray jsonArray = response.getJSONArray("weather");
                        JSONObject img = jsonArray.getJSONObject(0);

//                        img.getJSONArray("[0]");
                        String icon = img.getString("icon");
                        String imageUrl = "http://openweathermap.org/img/w/"+icon+".png";

                        Picasso.get().load(imageUrl).into(imageView);
                        String Description = img.getString("description");
                        String tempvalue = ob.getString("temp");
                        double tempvaluedouble = Double.parseDouble(tempvalue);
                        tempvaluedouble = tempvaluedouble - 273;
                        tempvalue = Double.toString(tempvaluedouble).substring(0,5);
                        String humidvalue = ob.getString("humidity");
                        String presvalue = ob.getString("pressure");
                        tvftemp.setText(tempvalue+"°c");
                        description.setText(Description);
                        textresult.setText("Humidity : "+humidvalue+"%"+"\n"+
                        "Pressure : "+presvalue + "mb");


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(jsonObjectRequest);
        }

    }
}