package ba.sum.fpmoz.skriptomat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ba.sum.fpmoz.skriptomat.adapter.ScriptAdapter;
import ba.sum.fpmoz.skriptomat.model.Script;

public class SavedActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    ScriptAdapter scriptAdapter;
    SearchView searchView;


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference savedRef;



    private static final String TAG = "SAVED_ACT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_saved);

        savedRef = mDatabase.getReference("Script");

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_saved);


         bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                        overridePendingTransition(0, 0);
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
                        return true;

                    case R.id.navigation_account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        String saved = getIntent().getStringExtra("saved");
        recyclerView = findViewById(R.id.savedRv);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        FirebaseRecyclerOptions<Script> options = new FirebaseRecyclerOptions.Builder<Script>()
                .setQuery(mDatabase.getReference("Script").orderByChild("saved").equalTo(true),Script.class)
                .build();
        ProgressDialog progressDialog = new ProgressDialog(this);
       // progressDialog.setMessage("Loading...");
        progressDialog.show();

        scriptAdapter= new ScriptAdapter(options,progressDialog);
        recyclerView.setAdapter(scriptAdapter);

    }


    private void performSearch(String query) {
        savedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Script script = dataSnapshot.getValue(Script.class);
                    if (script.isSaved() && script.getScript().contains(query)) {
                        Log.d(TAG, "Script: " + script.getScript());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });

    }


    @Override
    protected void onStart(){
        super.onStart();
        scriptAdapter.startListening();
    }
    @Override
    protected  void onStop(){
        super.onStop();
        scriptAdapter.stopListening();

    }
    private boolean isEmailValid(String email){
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "(fpmoz.sum.ba)$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}