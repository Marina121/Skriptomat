package ba.sum.fpmoz.skriptomat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ba.sum.fpmoz.skriptomat.adapter.CollegeAdapter;
import ba.sum.fpmoz.skriptomat.model.College;
import ba.sum.fpmoz.skriptomat.model.Course;

public class CollegeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CollegeAdapter collegeAdapter;
    private ImageButton backBtn;
    private SearchView searchView;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference collegeRef;


    public static final String TAG = "COLLEGE_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_college);

        collegeRef = mDatabase.getReference("College");

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

        //FloatingActionButton addCollegeBtn = findViewById(R.id.addCollegeBtn);
        backBtn = findViewById(R.id.backBtn);
        searchView = findViewById(R.id.searchView);

        String facultyId = getIntent().getStringExtra("facultyId");

        recyclerView = findViewById(R.id.collegeRv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        FirebaseRecyclerOptions<College> options = new FirebaseRecyclerOptions.Builder<College>()
                .setQuery(mDatabase.getReference("College").orderByChild("facultyId").equalTo(Long.valueOf(facultyId)), College.class)
                .build();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        collegeAdapter = new CollegeAdapter(options, progressDialog);
        recyclerView.setAdapter(collegeAdapter);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CollegeActivity.this, FacultyActivity.class);
                startActivity(i);
            }
        });
    }

    private void performSearch(String query) {
        Query searchQuery = collegeRef
                .orderByChild("college")
                .startAt(query)
                .endAt(query + "\uf8ff");
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    College college = dataSnapshot.getValue(College.class);
                    Log.d(TAG, "College:" + college.getCollege());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d(TAG, "Error"+ error.getMessage());
            }
        });

        FirebaseRecyclerOptions<College> options = new FirebaseRecyclerOptions.Builder<College>()
                .setQuery(searchQuery, College.class).build();

        collegeAdapter.updateOptions(options);
        collegeAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        collegeAdapter.startListening();
        Log.d(TAG, "starting: početak");
    }

    @Override
    protected void onStop() {
        super.onStop();
        collegeAdapter.stopListening();
        Log.d(TAG, "ending: kraj");
    }

    private boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "(fpmoz.sum.ba)$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
