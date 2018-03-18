package com.archives.supervisor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.archives.R;
import com.archives.help.Constants;
import com.archives.help.Keys;
import com.archives.models.Report;
import com.archives.models.Upload;
import com.archives.models.student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by locall on 2/17/2018.
 */

public class send_report extends AppCompatActivity {

    private ImageButton upload;
    private  FirebaseDatabase database;
    private DatabaseReference ref;
    private List<student> studentList = new ArrayList<>();
    private  String namestudent[] , idstudent[];
    private Button send ;
    private ImageView back;
    public int PDF_REQ_CODE = 1;
    //uri to store file
    private Uri filePath;
    private EditText namereport,number_student;
    private String Token = "",selected_student = "",supervisor_id="";
    //firebase objects
    private StorageReference storageReference ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ربط هذه الكلاس بالواجهة ليتم التعرف ع العناصر
        setContentView(R.layout.send_report);

        SharedPreferences prefs = getSharedPreferences("arch", MODE_PRIVATE);
        Token = prefs.getString(Keys.KEY_TOKEN, "");
        supervisor_id=prefs.getString(Keys.KEY_SUPERVIOR, "");


        storageReference  = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        number_student =(EditText) findViewById(R.id.number_student);
        send = (Button)findViewById(R.id.send);
        namereport = (EditText)findViewById(R.id.report_name);
        back = (ImageView)findViewById(R.id.back);
        upload = (ImageButton)findViewById(R.id.upload_file);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 1);
    }
});


      send.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              // add all student user in list
              ref.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      studentList.clear();

                      for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                          student student = snapshot.getValue(student.class);
                          if (dataSnapshot.getValue() == null) {
                              Toast.makeText(send_report.this, "Not found", Toast.LENGTH_SHORT).show();
                          }else  {
                              studentList.add(student);
                              //  mAdapter.notifyDataSetChanged();}
                          }

                      }
                      namestudent =new String[studentList.size()];
                      idstudent =new String[studentList.size()];
                      for(int i=0;i<studentList.size();i++){
                          // student student =studentList.get(i);
                          namestudent[i] = studentList.get(i).getName();
                          idstudent[i] =studentList.get(i).getId();
                          if(idstudent[i].equals(number_student.getText().toString())){
                            selected_student = number_student.getText().toString();
                          }

                      }


                  }
                  @Override
                  public void onCancelled (DatabaseError databaseError){

                  }

              });
              getPDF();
              uploadFileReport();
          }
      });


    }

    private void uploadFileReport() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            //creating the upload object to store uploaded image details
                            Report report = new Report(namereport.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString(),selected_student,supervisor_id,Token);

                            //adding an upload to firebase database
                            ref.child("uploadFileSupervisor").push().setValue(report);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Upload is " + "50" + "% done");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                //uploading the file
                filePath  = data.getData();

                Toast.makeText(this, "PDF is Selected", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }



        }
    }
    //this function will get the pdf from the storage
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(send_report.this,"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(send_report.this,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
