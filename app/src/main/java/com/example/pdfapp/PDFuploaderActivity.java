package com.example.pdfapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pdfapp.databinding.ActivityPdfuploaderBinding;
import com.example.pdfapp.viewmodel.PDFViewmodel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PDFuploaderActivity extends AppCompatActivity {


    private Uri pdfuri;

    private long sizeofpdf;

    private String PDFrelatedto;

   public  static   Spinner spinner;

    public static ActivityPdfuploaderBinding activityPdfuploaderBinding;

    private PDFClickHandler pdfclickhandlers;



    private PDFViewmodel viewmodel;

    private SpinnerActivity listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfuploader);

listener=new SpinnerActivity();
activityPdfuploaderBinding= DataBindingUtil.setContentView(this,R.layout.activity_pdfuploader);
pdfclickhandlers=new PDFClickHandler(this);
activityPdfuploaderBinding.setPdfclickhandlers(pdfclickhandlers);

        viewmodel=new ViewModelProvider(this).get(PDFViewmodel.class);

         spinner = (Spinner) findViewById(R.id.courses_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.courses_name, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(listener);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


    }

    public class SpinnerActivity implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            PDFrelatedto=parent.getItemAtPosition(pos).toString();

            Toast.makeText(PDFuploaderActivity.this, ""+PDFrelatedto, Toast.LENGTH_SHORT).show();
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    public class PDFClickHandler {
        Context context;

        public PDFClickHandler(Context context) {
            this.context = context;
        }

        public void uploadpdf(View view) {
            //uploadpdf
        UploadPDF();
        }

        public void backtoshow(View view){
            onBackPressed();
        }


        public void submitpdf(View view){



String PDFname=activityPdfuploaderBinding.edit.getText().toString().trim();



if(TextUtils.isEmpty(PDFname) || TextUtils.isEmpty(PDFrelatedto) || TextUtils.isEmpty(pdfuri.toString()) ){
    Toast.makeText(context, "Empty fields", Toast.LENGTH_SHORT).show();
    activityPdfuploaderBinding.edit.setError("FILL IT");
    activityPdfuploaderBinding.coursesSpinner.setAutofillHints("Select something");
}  else {

    activityPdfuploaderBinding.progressbarupload.setVisibility(View.VISIBLE);

    ExecutorService executorService=Executors.newSingleThreadExecutor();

    executorService.execute(new Runnable() {
        @Override
        public void run() {
            viewmodel.uploadupf(pdfuri, PDFname, PDFrelatedto,PDFuploaderActivity.this);
        }
    });


}
        }


    }


    private void UploadPDF() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // We will be redirected to choose pdf
        galleryIntent.setType("application/pdf");
        startActivityForResult(galleryIntent, 1);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode==1) {

            // Here we are initialising the progress dialog box
            assert data != null;
            pdfuri = data.getData();


            Cursor returnCursor =
                    getContentResolver().query(pdfuri, null, null, null, null);
            /*
             * Get the column indexes of the data in the Cursor,
             * move to the first row in the Cursor, get the data,
             * and display it.
             */
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            sizeofpdf= Long.parseLong(Long.toString(returnCursor.getLong(sizeIndex)));
             if (sizeofpdf>10000000 ) {
                Toast.makeText(PDFuploaderActivity.this, "Reduce pdf size", Toast.LENGTH_SHORT).show();
                activityPdfuploaderBinding.text.setText("Pease select file less than 10 mb");

            }else {
                 activityPdfuploaderBinding.text.setText(pdfuri.toString());
             }

        }
    }
}