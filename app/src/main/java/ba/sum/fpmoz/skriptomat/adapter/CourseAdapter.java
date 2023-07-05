package ba.sum.fpmoz.skriptomat.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ba.sum.fpmoz.skriptomat.R;
import ba.sum.fpmoz.skriptomat.ScriptActivity;
import ba.sum.fpmoz.skriptomat.model.Course;
import ba.sum.fpmoz.skriptomat.model.Faculty;

public class CourseAdapter extends FirebaseRecyclerAdapter<Course,CourseAdapter.CourseViewHolder> {
    Context context;
    public static final String TAG ="COURSE_ADAPTER";
    FirebaseDatabase mDatabase =FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private ProgressDialog progressDialog;

    public CourseAdapter(@NonNull FirebaseRecyclerOptions<Course> options){
        super(options);}


    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_view, parent, false);
        return new CourseAdapter.CourseViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position, @NonNull Course model) {

        String collegeId= model.getCollegeId();
        String course = model.getCourse();
        String timestamp = model.getTimestamp();
        String id = model.getId();
        String type = model.getType();

        holder.courseTv.setText(model.getCourse());
        holder.courseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "courseId"+ course);
                Intent intent= new Intent(context, ScriptActivity.class);
                intent.putExtra("courseId", course);
                context.startActivity(intent);
            }
        });



    }
    class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseTv;


        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTv = itemView.findViewById(R.id.courseTv);

        }
    };
    public boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "(fpmoz.sum.ba)$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

}

