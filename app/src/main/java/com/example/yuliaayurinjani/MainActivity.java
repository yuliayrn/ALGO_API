package com.example.yuliaayurinjani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuliaayurinjani.R;

import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.example.yuliaayurinjani.R;
import com.example.yuliaayurinjani.dataalgo.DataAlgoritma;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    ArrayList<DataAlgoritma> dataAlgoritma = new ArrayList();
    JSONObject jsonObject;
    ListView listview;
    FloatingActionButton btnRefresh;
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(view -> getDataAlgoritma());
        loadingIndicator = findViewById(R.id.loading);
        getDataAlgoritma();
    }

    void setupListviewAlgoritma () {
        listview = findViewById(R.id.listViewAlgoritma);
        ItemAdapter adapter = new ItemAdapter(this, dataAlgoritma);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onItemClick);
    }

    private AdapterView.OnItemClickListener onItemClick;

    {
        onItemClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataAlgoritma algoritma = dataAlgoritma.get(position);
                Intent intent = new Intent(MainActivity.this, DetailAlgoritma.class);
                intent.putExtra("Algoritma", algoritma);
                startActivity(intent);
            }
        };
    }

    final String url = "https://ewinsutriandi.github.io/mockapi/algoritma_pengurutan.json";
    void getDataAlgoritma() {
        dataAlgoritma.clear();
        loadingIndicator.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonObject = response;
                        refreshView();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Not Foun", error.toString());
                        loadingIndicator.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Pastikan Anda Terhubung Dengan Interet", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
        setupListviewAlgoritma();
    }

    void refreshView() {
        Iterator<String> key = jsonObject.keys();
        while(key.hasNext()) {
            String nameAlgoritma = key.next();
            try {
                JSONObject data = jsonObject.getJSONObject(nameAlgoritma);
                String deskripsi = data.getString("deskripsi");

                String logo = data.getString("gambar");
                String baca_lebih_lanjut = data.getString("baca_lebih_lanjut");
                dataAlgoritma.add(new DataAlgoritma(nameAlgoritma, baca_lebih_lanjut, deskripsi, logo));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        loadingIndicator.setVisibility(View.GONE);
        setupListviewAlgoritma();
    }

}