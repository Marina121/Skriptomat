package ba.sum.fpmoz.skriptomat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import ba.sum.fpmoz.skriptomat.databinding.ActivityAddCourseBinding;
import ba.sum.fpmoz.skriptomat.model.Course;

public class AddCourseActivity extends AppCompatActivity {

    private ActivityAddCourseBinding binding;

    Button submitBtn;

    private String collegeId = "", course="",type="";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        binding = ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Molimo pričekajte");
        progressDialog.setCanceledOnTouchOutside(false);



        submitBtn =  findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }


    private void validateData(){
        Log.d(TAG, "validateData: validating data...");

        collegeId = binding.collegeIdInputTxt.getText().toString().trim();
        course = binding.courseInputTxt.getText().toString().trim();
        type = binding.typeInputTxt.getText().toString().trim();



        if(TextUtils.isEmpty(collegeId)){
            Toast.makeText(this, "Unesite naziv collegeId!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "validateData: title prazan");
        }else if (TextUtils.isEmpty(course)){
            Toast.makeText(this, "Unesite naziv course!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "validateData: title prazan");
        }else if(TextUtils.isEmpty(type)){
            Toast.makeText(this, "Unesite type!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "validateData: title prazan");
        }
        else{
            uploadFilesToStorage();
        }
    }

    private void uploadFilesToStorage() {
        Log.d(TAG, "uploadImgToStorage: dodavanje u pohranu");

        progressDialog.show();

        long timestamp = System.currentTimeMillis();

        String filePathAndName1 = "Course/files/" + course;


        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(filePathAndName1);



        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("collegeId", ""+collegeId+"_"+type);
        hashMap.put("course", ""+course);
        hashMap.put("id", ""+course);
        hashMap.put("timestamp", ""+course);
        hashMap.put("type",""+type);





        DatabaseReference ref = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Course");
        ref.child(""+course)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onSuccess: Uspješno dodavanje u bazu ");
                        Toast.makeText(AddCourseActivity.this, "Uspješno dodavanje u bazu ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCourseActivity.this, CourseActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailture: Neuspješno dodavanje u bazu " + e.getMessage());
                        Toast.makeText(AddCourseActivity.this, "Neuspješno dodavanje u bazu " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}