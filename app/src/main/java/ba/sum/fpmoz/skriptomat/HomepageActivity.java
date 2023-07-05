package ba.sum.fpmoz.skriptomat;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ba.sum.fpmoz.skriptomat.adapter.NewsAdapter;
import ba.sum.fpmoz.skriptomat.model.NewsItem;


public class HomepageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private RecyclerView recyclerView;
    private List<NewsItem> newsList;
    private NewsAdapter newsAdapter;

    ImageButton sumBtn, linkedBtn, moodleBtn;


    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_homepage);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(newsAdapter);

        queue = Volley.newRequestQueue(this);

        sumBtn = findViewById(R.id.sumBtn);
        linkedBtn= findViewById(R.id.linkedBtn);
        moodleBtn = findViewById(R.id.moodleBtn);

        sumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.sum.ba";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        linkedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://ba.linkedin.com/school/universityofmostar/";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        moodleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://eucenje.sum.ba/moodle/";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        return true;
                    case R.id.navigation_sum_chat:
                        startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_faculty:
                        startActivity(new Intent(getApplicationContext(), FacultyActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_saved:
                        startActivity(new Intent(getApplicationContext(), SavedActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        String url = "https://web-admin.sum.ba/api/web/objave";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                List<NewsItem> newsItemList = parseNews(jsonArray);
                                NewsAdapter adapter = new NewsAdapter(HomepageActivity.this, newsItemList);
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
            @Override
            public byte[] getBody() {
                String jsonBody = "{\"page\":1,\"postsPerPage\":10,\"categories\":[\"E-u\\u010denje\"]}";
                return jsonBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/113.0");
                headers.put("Accept", "application/json, text/plain, */*, application/json");
                headers.put("Accept-Language", "hr,hr-HR;q=0.8,en-US;q=0.5,en;q=0.3");
                headers.put("Accept-Encoding", "gzip, deflate, br");
                headers.put("Content-Type", "application/json;charset=utf-8");
                headers.put("language", "hr");
                headers.put("Origin", "https://eucenje.sum.ba");
                headers.put("Connection", "keep-alive");
                headers.put("Referer", "https://eucenje.sum.ba/");
                headers.put("Sec-Fetch-Dest", "empty");
                headers.put("Sec-Fetch-Mode", "cors");
                headers.put("Sec-Fetch-Site", "same-site");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        queue.add(stringRequest);
    }

    private List<NewsItem> parseNews(JSONArray jsonArray) throws  JSONException{
        List<NewsItem> newsList = new ArrayList<>();
        for (int i = 0;i<jsonArray.length();i++){
            JSONObject newsObject = jsonArray.getJSONObject(i);


            String title = newsObject.getString("title");
            String alias = newsObject.getString("content");
            String image = newsObject.getString("image");

            NewsItem newsItem = new NewsItem (title, alias, image);
            newsList.add(newsItem);
        }
        return  newsList;
    }
}