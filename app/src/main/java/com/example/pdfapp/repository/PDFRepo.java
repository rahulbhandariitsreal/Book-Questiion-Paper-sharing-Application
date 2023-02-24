package com.example.pdfapp.repository;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pdfapp.PDFuploaderActivity;
import com.example.pdfapp.model.PDF;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;


public class PDFRepo {


    private FirebaseDatabase database;

    private FirebaseStorage storage;

    private ArrayList<String> List_of_PDF_GROUP=new ArrayList<>();
    private StorageReference storageReference;

    private  String pdfaccesstoken;


    private DatabaseReference databaseReference;

    private MutableLiveData<ArrayList<String>> List_of_PDF_GROUP_live=new MutableLiveData<>();

    private MutableLiveData<ArrayList<String>> List_of_PDF_LIST_live=new MutableLiveData<>();

    private static PDFRepo Instance;

    public static PDFRepo getInstance(){
        if(Instance==null){
            Instance=new PDFRepo();
        }
        return Instance;
    }

    private PDF currentpdf;

    public PDFRepo() {
      database=FirebaseDatabase.getInstance();
      storage=FirebaseStorage.getInstance();
      currentpdf=new PDF();

    }

    public String getPDFaccesstoken(String coursename,String pdf_name){

       databaseReference=database.getReference("pdf").child(coursename).child(pdf_name).child("pdfURI");

       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               pdfaccesstoken=snapshot.getValue().toString();

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
        return pdfaccesstoken;
    }

    public LiveData<ArrayList<String>> getallpdf_list(String course_name){

        databaseReference=database.getReference("pdf").child(course_name);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List_of_PDF_GROUP.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    List_of_PDF_GROUP.add(snapshot1.getKey().toString());
                }
                List_of_PDF_LIST_live.setValue(List_of_PDF_GROUP);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return List_of_PDF_LIST_live;
    }

    public LiveData<ArrayList<String>> getallpdf(){

        databaseReference=database.getReference("pdf");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List_of_PDF_GROUP.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    List_of_PDF_GROUP.add(snapshot1.getKey().toString());
                }
                List_of_PDF_GROUP_live.setValue(List_of_PDF_GROUP);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


return List_of_PDF_GROUP_live;
    }

    public void uploadpdf(Uri pdfuri, String PDFname, String PDFrelated, Context context){


        storageReference=storage.getReference("MYfiles").child(PDFname+PDFrelated);
        databaseReference=database.getReference("pdf").child(PDFrelated).child(PDFname);


        storageReference.putFile(pdfuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {


                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String pdftokenoffirebase = uri.toString();

                            currentpdf.setPdfURI(pdftokenoffirebase);
                            currentpdf.setName(PDFname);
                            currentpdf.setRelatedto(PDFrelated);
                            databaseReference.setValue(currentpdf).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    PDFuploaderActivity.activityPdfuploaderBinding.progressbarupload.setVisibility(View.GONE);
                                    PDFuploaderActivity.activityPdfuploaderBinding.edit.setText("");
                                    PDFuploaderActivity.activityPdfuploaderBinding.coursesSpinner.setSelection(0);
                                    PDFuploaderActivity.activityPdfuploaderBinding.text.setText("Select PDF");
                                    Toast.makeText(context, "Successfull uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else{
                    Toast.makeText(context, "Failed to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });

        }



    }


