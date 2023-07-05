package ba.sum.fpmoz.skriptomat.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ba.sum.fpmoz.skriptomat.CourseActivity;
import ba.sum.fpmoz.skriptomat.R;
import ba.sum.fpmoz.skriptomat.model.College;

public class CollegeAdapter extends FirebaseRecyclerAdapter<College, CollegeAdapter.CollegeViewHolder> implements Filterable {

    Context context;
    public static final String TAG ="COLLEGE_ADAPTER";
    FirebaseDatabase mDatabase =FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private ProgressDialog progressDialog;
    private List<College> collegeList;
    private List<College> collegeListFiltered;




    public CollegeAdapter(@NonNull FirebaseRecyclerOptions<College> options, ProgressDialog progressDialog){
        super(options);
        this.progressDialog = progressDialog;
    }

    @NonNull
    @Override
    public CollegeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_view, parent, false);
        return new CollegeAdapter.CollegeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollegeViewHolder holder, int position, @NonNull College model) {
        String college = model.getCollege();
        String timestamp = model.getTimestamp();
        String type = model.getType();
        holder.collegeTv.setText(model.getCollege());
        holder.collegeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser  = mAuth.getCurrentUser();
                if(isEmailValid(currentUser.getEmail())){
                    moreOptionsDialog(model, holder);
                }
            }
        });

    }

    private void moreOptionsDialog(College model, CollegeViewHolder holder) {
        String collegeId = model.getTimestamp();
        String type = model.getType();
        String[] options = {"Preddiplomski studij", "Diplomski studij",};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which==0){
                    Intent intent = new Intent(context, CourseActivity.class);
                    intent.putExtra("collegeId", collegeId + "_undergraduate");
                    intent.putExtra("type", collegeId+"_undergraduate");
                    context.startActivity(intent);
                }else if(which==1){
                    Intent intent = new Intent(context,CourseActivity.class);
                    intent.putExtra("collegeId",collegeId+"_graduate");
                    intent.putExtra("type", collegeId+"_graduate");
                    context.startActivity(intent);
                }
            }
        }).show();
    }


    class CollegeViewHolder extends RecyclerView.ViewHolder {
        TextView collegeTv;

        public CollegeViewHolder(@NonNull View itemView) {
            super(itemView);
            collegeTv = itemView.findViewById(R.id.collegeTv);
        }
    }

    public boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "(fpmoz.sum.ba)$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase();
                if (searchText.isEmpty()) {
                    collegeListFiltered = collegeList;
                } else {
                    List<College> filteredList = new ArrayList<>();
                    for (College college : collegeList) {
                        if (college.getCollege().toLowerCase().contains(searchText) || college.getCollege().toLowerCase().contains(searchText)) {
                            filteredList.add(college);
                        }
                    }
                    collegeListFiltered = filteredList;
                }
                FilterResults results = new FilterResults();
                results.values = collegeListFiltered;
                results.count = collegeListFiltered.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                collegeListFiltered = (List<College>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public void onDataChanged() {
        super.onDataChanged();
        progressDialog.dismiss();
    }

    @Override
    public void onError(@NonNull DatabaseError error) {
        super.onError(error);
        progressDialog.dismiss();
    }
}