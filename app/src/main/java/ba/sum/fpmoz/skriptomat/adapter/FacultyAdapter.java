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
import ba.sum.fpmoz.skriptomat.CollegeActivity;
import ba.sum.fpmoz.skriptomat.model.Faculty;




public class FacultyAdapter extends FirebaseRecyclerAdapter<Faculty,FacultyAdapter.FacultyViewHolder> {

    Context context;
    public static final String TAG ="FACULTY_ADAPTER";
    FirebaseDatabase mDatabase =FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private ProgressDialog progressDialog;

    public FacultyAdapter(@NonNull FirebaseRecyclerOptions <Faculty> options){
        super(options);}


    @NonNull
    @Override
    public FacultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_view, parent, false);
        return new FacultyAdapter.FacultyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FacultyViewHolder holder, int position, @NonNull Faculty model) {
        String image = model.getImage();
        String university = model.getUniversity(); ;
        String faculty = model.getFaculty();
        String city = model.getCity();
        String type = model.getType();
        String id = model.getId();
        String uid = model.getUid();
        String timestamp = model.getTimestamp();


        holder.facultyTv.setText(model.getFaculty());
        Picasso
                .get()
                .load(model.getImage())
                .into(holder.imageTv);

        holder.imageTv.setOnClickListener(new View.OnClickListener() {
            String facultyId = model.getTimestamp();
            @Override
            public void onClick(View v) {
                Log.d(TAG, "imagesBtn:id" + facultyId);
                Intent intent = new Intent(context, CollegeActivity.class);
                intent.putExtra("facultyId", facultyId);
                context.startActivity(intent);
                /* FirebaseUser currentUser = mAuth.getCurrentUser();
                 assert currentUser != null;
                 if(isEmailValid(currentUser.getEmail())){
                    // imageOptions(model,holder);
                     imageOptionsDialog(model,holder);*/

            }
        });



    }
  /*  private void imageOptions(Faculty model, FacultyViewHolder holder){
        String facultyId = model.getTimestamp();
        Intent intent = new Intent(context, StudiesActivity.class);
        intent.putExtra("facultyId",facultyId);
        context.startActivity(intent);
    }*/

 /*   private void imageOptionsDialog(Faculty model, FacultyViewHolder holder) {
        String facultyId = model.getTimestamp();
        String[] options = {"Smjerovi"};

        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    Intent intent = new Intent(context, StudiesActivity.class);
                    intent.putExtra("facultyId", facultyId);
                    context.startActivity(intent);
                }/*else if(which ==1){
                    Intent intent = new Intent(context, GraduateStudyActivity.class);
                    intent.putExtra("facultyId",facultyId);
                    context.startActivity(intent);
                }
            }
        }).show();
    }
    */


    class FacultyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTv;
        TextView facultyTv;


        public FacultyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTv = itemView.findViewById(R.id.imageTv);
            facultyTv = itemView.findViewById(R.id.facultyTv);

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
