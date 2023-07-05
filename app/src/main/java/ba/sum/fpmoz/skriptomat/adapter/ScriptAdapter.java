package ba.sum.fpmoz.skriptomat.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import ba.sum.fpmoz.skriptomat.R;

import ba.sum.fpmoz.skriptomat.model.Script;


public class ScriptAdapter extends FirebaseRecyclerAdapter<Script, ScriptAdapter.ScriptViewHolder> {

    Context context;
    public static final String TAG = "SCRIPT_ADAPTER";
    public static final String TAG_DOWNLOAD = "TAG_DOWNLOAD";

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private ProgressDialog progressDialog;

    public ScriptAdapter(@NonNull FirebaseRecyclerOptions<Script> options, ProgressDialog progressDialog) {
        super(options);
        this.progressDialog = progressDialog;
    }


    @NonNull
    @Override
    public ScriptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.script_view, parent, false);
        return new ScriptAdapter.ScriptViewHolder(view);
    }

    @Override
        public void onBindViewHolder(@NonNull ScriptAdapter.ScriptViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Script model) {
        String courseId = model.getCourseId();
        String script = model.getScript();
        String timestamp = model.getTimestamp();
        String id = model.getId();
        String url = model.getUrl();
        Boolean saved = model.isSaved();
        int likes = model.getLikes();
        int dislikes = model.getDislikes();


        holder.likesTv.setImageResource(R.drawable.icons8_like_16___);
        holder.dislikesTv.setImageResource(R.drawable.icons8_dislike_16___);
        holder.scriptTv.setText(model.getScript());
        holder.bind(model);

        holder.scriptTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = model.getUrl();
                downloadFile(context, url);
            }
        });

        holder.likesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Script").child(model.getId());
                mDatabase.child("likes").setValue(likes + 1);

            }
        });

        holder.dislikesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Script").child(model.getId());
                mDatabase.child("dislikes").setValue(dislikes + 1 );
            }
        });

        holder.likesView.setText(String.valueOf(model.getLikes()));
        holder.dislikesView.setText(String.valueOf(model.getDislikes()));




    }

    private void downloadFile(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Nemoguće preuzeti datoteku", Toast.LENGTH_SHORT).show();
        }
    }


    class ScriptViewHolder extends RecyclerView.ViewHolder {

        TextView scriptTv;
        ImageButton saveButton, likesTv,dislikesTv;
        TextView likesView, dislikesView;



        public ScriptViewHolder(@NonNull View itemView) {
            super(itemView);
            scriptTv = itemView.findViewById(R.id.scriptTv);
            saveButton = itemView.findViewById(R.id.saveButton);
            likesTv = itemView.findViewById(R.id.likesTv);
            dislikesTv = itemView.findViewById(R.id.dislikesTv);
            likesView = itemView. findViewById(R.id.likesView);
            dislikesView = itemView.findViewById(R.id.dislikesView);
        }


        public void bind(Script script) {
            if (script.isSaved()) {
                saveButton.setImageResource(R.drawable.ic_baseline_turned_in_24);
            } else {
                saveButton.setImageResource(R.drawable.ic_baseline_turned_in_not_24);
            }
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Script").child(script.getId());
                    if (script.isSaved()) {
                        script.setSaved(false);
                        saveButton.setImageResource(R.drawable.ic_baseline_turned_in_not_24);
                        mDatabase.child("saved").setValue(false);
                    } else {
                        script.setSaved(true);
                        saveButton.setImageResource(R.drawable.ic_baseline_turned_in_24);
                        mDatabase.child("saved").setValue(true);
                    }
                }
            });
        }

    }

}

