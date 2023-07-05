package ba.sum.fpmoz.skriptomat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ba.sum.fpmoz.skriptomat.databinding.ActivityAddScriptBinding;
import ba.sum.fpmoz.skriptomat.databinding.ActivityScriptBinding;


public class AddScriptActivity extends AppCompatActivity {


    private ActivityAddScriptBinding binding;

    Button submitBtn;
    ImageButton pdfBtn;

    private String courseId = "", script = "", id = script,timestamp= script ;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/");
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri pdfUri = null;



    private static final  int PDF_PICK_CODE = 1002;
    private static final String TAG= "ADD_PDF_TAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();



        binding= ActivityAddScriptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference= database.getReference("Course");
        List<String> idList= new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String id = snapshot.getKey();
                    String course = snapshot.child("Course").getValue(String.class);
                    String idAndCourse = id;
                    idList.add(idAndCourse);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, idList);
                AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.courseInputTxt);
                textView.setAdapter(adapter);

                textView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,long id){
                        String selectCourse = (String) parent.getItemAtPosition(position);
                        textView.setText(selectCourse);
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        String courseId = getIntent().getStringExtra("courseId");


        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Molimo pričekajte");
        progressDialog.setCanceledOnTouchOutside(false);

        //Dohvaćanje pdf
        pdfBtn = (ImageButton) findViewById(R.id.pdfBtn);
        pdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            pdfPickIntent();
            }
        });



        submitBtn =  findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }
    private void pdfPickIntent() {
        Log.d(TAG, "pdfPickIntent: početak odabira pdf dokumenta");
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Odaberite PDF"), PDF_PICK_CODE);

    }


    private void validateData(){
        Log.d(TAG, "validateData: validating data...");

        courseId = binding.courseInputTxt.getText().toString().trim();
        script = binding.scriptInputText.getText().toString().trim();
        id = binding.scriptInputText.getText().toString().trim();
        timestamp = binding.scriptInputText.getText().toString().trim();




        if(TextUtils.isEmpty(courseId)){
            Toast.makeText(this, "Unesite naziv kolegija!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "validateData: title prazan");
        }else if(TextUtils.isEmpty(script)){
            Toast.makeText(this, "Unesite naziv skripte!", Toast.LENGTH_SHORT).show();
        }else if(pdfUri == null){
            Toast.makeText(this,"Odaberite pdf skripte!",Toast.LENGTH_SHORT).show();
        } else{
            uploadFilesToStorage();
        }
    }

    private void uploadFilesToStorage() {
        Log.d(TAG, "uploadImgToStorage: dodavanje u pohranu");

        progressDialog.setMessage("Dodavanje pdf-a..");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();

        String filePathAndName1 = "Script/files/" + script;

        final String[] uploadedPdfUrl = new String[1];

        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(filePathAndName1);
        storageReference1.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: uspješno dodan pdf");
                Log.d(TAG, "onSuccess: dohvat pdf urla");

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                uploadedPdfUrl[0] = "" + uriTask.getResult();
                if (uploadedPdfUrl[0] != null ) {
                    uploadDataToDb(uploadedPdfUrl[0], timestamp);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailture: greška pri dodavanju pdf " + e.getMessage());
                Toast.makeText(AddScriptActivity.this, "Dodavanje PDFa greška " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void uploadDataToDb(String uploadedPdfUrl,long timestamp) {

        Log.d(TAG, "uploadPdfToStorage: dodavanje u bazu");

        progressDialog.setMessage("Dodavanje informacija o slici");


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("courseId", "" + courseId);
        hashMap.put("script", "" + script);
        hashMap.put("timestamp", "" + script);
        hashMap.put("id", "" + script);
        hashMap.put("url", "" + uploadedPdfUrl);
        hashMap.put("saved",false);


        DatabaseReference ref = FirebaseDatabase.getInstance("https://skriptomat-5acff-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Script");
        ref.child("" + script)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onSuccess: Uspješno dodavanje u bazu ");
                        Toast.makeText(AddScriptActivity.this, "Uspješno dodavanje u bazu ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddScriptActivity.this, ScriptActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailture: Neuspješno dodavanje u bazu " + e.getMessage());
                        Toast.makeText(AddScriptActivity.this, "Neuspješno dodavanje u bazu " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(resultCode == RESULT_OK){
                if(requestCode == PDF_PICK_CODE){
                    Log.d(TAG, "onActivityResult: PDF odabran");

                    pdfUri = data.getData();

                    Log.d(TAG, "onActivityResult: URIPDF: " +pdfUri);
                }
        } else{
            Log.d(TAG, "onActivityResult: zatvaranje odabira");
            Toast.makeText(this, "zatvaranje odabira ", Toast.LENGTH_SHORT).show();
        }
    }
}
