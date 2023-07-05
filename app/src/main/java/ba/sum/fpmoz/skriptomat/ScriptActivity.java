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
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ba.sum.fpmoz.skriptomat.adapter.ScriptAdapter;
import ba.sum.fpmoz.skriptomat.model.College;
import ba.sum.fpmoz.skriptomat.model.Course;
import ba.sum.fpmoz.skriptomat.model.Script;
import ba.sum.fpmoz.skriptomat.model.UserProfile;

public class ScriptActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ScriptAdapter scriptAdapter;
    SearchView searchView;
    BottomNavigationView bottomNavigationView;
    TextView likesView, dislikesView;



    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference scriptRef;


    private static final String TAG = "SCRIPT_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_script);

        scriptRef = mDatabase.getReference("Script");

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


        FloatingActionButton addScriptBtn = findViewById(R.id.addScriptBtn);
        addScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (isEmailValid(currentUser.getEmail())) {
                    Intent addScriptActivity = new Intent(ScriptActivity.this, AddScriptActivity.class);
                    startActivity(addScriptActivity);
                } else {
                    Toast.makeText(getApplicationContext(), "Nemate mogućnost dodavanja skripti!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        String courseId = getIntent().getStringExtra("courseId");
        String likes = getIntent().getStringExtra("likes");
        Log.d(TAG, "courseId: " + courseId);


        recyclerView = findViewById(R.id.scriptRv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        FirebaseRecyclerOptions<Script> options = new FirebaseRecyclerOptions.Builder<Script>()
                .setQuery(mDatabase.getReference("Script").orderByChild("courseId").equalTo(courseId), Script.class)
                .build();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        scriptAdapter = new ScriptAdapter(options, progressDialog);
        recyclerView.setAdapter(scriptAdapter);

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

    }

    private void performSearch(String query) {
        Query searchQuery = scriptRef
                .orderByChild("script")
                .startAt(query)
                .endAt(query + "\uf8ff");
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Script script = dataSnapshot.getValue(Script.class);
                    Log.d(TAG, "Script:" + script.getScript());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d(TAG, "Error"+ error.getMessage());
            }
        });

        FirebaseRecyclerOptions<Script> options = new FirebaseRecyclerOptions.Builder<Script>()
                .setQuery(searchQuery, Script.class).build();

        scriptAdapter.updateOptions(options);
        scriptAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        scriptAdapter.startListening();
        Log.d(TAG, "starting:početak");
    }

    @Override
    protected void onStop() {
        super.onStop();
        scriptAdapter.stopListening();
        Log.d(TAG, "ending:kraj");
    }

    private boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "(fpmoz.sum.ba)$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}