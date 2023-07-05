package ba.sum.fpmoz.skriptomat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fpmoz.skriptomat.adapter.FacultyAdapter;
import ba.sum.fpmoz.skriptomat.model.Faculty;

public class FacultyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FacultyAdapter facultyAdapter;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    BottomNavigationView bottomNavigationView;

    private static final String TAG = "FACULTY_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_faculty);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.navigation_faculty);



        this.recyclerView = findViewById(R.id.facultyRv);
        this.recyclerView.setLayoutManager(
                new GridLayoutManager(this, 2)
        );

        FirebaseRecyclerOptions<Faculty> options = new FirebaseRecyclerOptions.Builder<Faculty>()
                .setQuery(this.mDatabase.getReference("Faculty"), Faculty.class).build();
        this.facultyAdapter = new FacultyAdapter(options);
        this.recyclerView.setAdapter(this.facultyAdapter);



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
    @Override
    protected void onStart () {
        super.onStart();
        this.facultyAdapter.startListening();
        Log.d(TAG, "starting: početak");
    }

    @Override
    protected void onStop () {
        super.onStop();
        this.facultyAdapter.stopListening();
        Log.d(TAG, "ending: kraj");
    }
}