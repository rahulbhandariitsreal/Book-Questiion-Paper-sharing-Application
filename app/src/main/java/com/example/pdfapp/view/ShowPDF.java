package com.example.pdfapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pdfapp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowPDF extends AppCompatActivity {

    private String course_name;

   private InputStream pdfStream = null;
    private String pdf_name;
    private String accesstoken;

    private PDFView pdfView;

    private ProgressBar progressBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        progressBar= findViewById(R.id.my_progress);
        progressBar.setVisibility(View.VISIBLE);





        course_name = getIntent().getStringExtra("course_name");
        pdf_name = getIntent().getStringExtra("pdf_name");


        Log.v("TAGGY", course_name);
        Log.v("TAGGY", pdf_name);
        pdfView = findViewById(R.id.pdfView);

        getPDFaccesstoken(course_name, pdf_name);



    }

    public void getPDFaccesstoken(String coursename, String pdf_name) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;

        databaseReference = database.getReference("pdf").child(coursename).child(pdf_name).child("pdfURI");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accesstoken = snapshot.getValue().toString();
                executeinbackground();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowPDF.this, "Failed to load pdf", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

    }


    //to load pdf from the internet using executors
    private void executeinbackground() {

        ExecutorService executorService=Executors.newSingleThreadExecutor();
        Handler handler=new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                // our input stream.

                try {
                    // creating a new URL and passing
                    // our string in it.

                    URL url = new URL(accesstoken);
                    // creating a new http url connection and calling open
                    // connection method to open http url connection.
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    if (httpURLConnection.getResponseCode() == 200) {
                        // if the connection is successful then
                        // we are getting response code as 200.
                        // after the connection is successful
                        // we are passing our pdf file from url
                        // in our pdfstream.
                        pdfStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    }

                } catch (IOException e) {
                    // this method is
                    // called to handle errors.
                    Toast.makeText(ShowPDF.this, ""+e, Toast.LENGTH_SHORT).show();
                }
                // returning our stream
                // of PDF file.
                InputStream inputStream=pdfStream;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pdfView.fromStream(inputStream).load();
                                progressBar.setVisibility(View.GONE);

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowPDF.this);
        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("EXIT !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            super.onBackPressed();
            finish();
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }

}