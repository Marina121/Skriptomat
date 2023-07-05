package ba.sum.fpmoz.skriptomat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.storage.FirebaseStorage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ba.sum.fpmoz.skriptomat.adapter.CourseAdapter;
import ba.sum.fpmoz.skriptomat.adapter.FacultyAdapter;
import ba.sum.fpmoz.skriptomat.model.Course;
import ba.sum.fpmoz.skriptomat.model.Faculty;

public class CourseActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CourseAdapter courseAdapter;
    ImageButton backBtn;
    SearchView searchView;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseStorage mAuth = FirebaseStorage.getInstance();
    DatabaseReference courseRef;

    private static final String TAG = "COURSE_ACT";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_course);

        courseRef = mDatabase.getReference("Course");

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


        //FloatingActionButton addCourseBtn = findViewById(R.id.addCourseBtn);
        String collegeId = getIntent().getStringExtra("collegeId");

        this.recyclerView = findViewById(R.id.courseRv);
        this.recyclerView.setLayoutManager(
                new GridLayoutManager(this, 1)
        );

        FirebaseRecyclerOptions<Course> options = new FirebaseRecyclerOptions.Builder<Course>()
                .setQuery(this.mDatabase.getReference("Course")
                        .orderByChild("collegeId").equalTo(collegeId), Course.class).build();
        this.courseAdapter = new CourseAdapter(options);
        this.recyclerView.setAdapter(this.courseAdapter);
    }


    private void performSearch(String query) {
        Query searchQuery = courseRef
                .orderByChild("course")
                .startAt(query)
                .endAt(query + "\uf8ff");
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Course course = dataSnapshot.getValue(Course.class);
                    Log.d(TAG, "Course:" + course.getCourse());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d(TAG, "Error"+ error.getMessage());
            }
        });

        FirebaseRecyclerOptions<Course> options = new FirebaseRecyclerOptions.Builder<Course>()
                .setQuery(searchQuery, Course.class).build();

        courseAdapter.updateOptions(options);
        courseAdapter.startListening();
    }

    @Override
    protected void onStart () {
        super.onStart();
        this.courseAdapter.startListening();
        Log.d(TAG, "starting: početak");
    }

    @Override
    protected void onStop () {
        super.onStop();
        this.courseAdapter.stopListening();
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
