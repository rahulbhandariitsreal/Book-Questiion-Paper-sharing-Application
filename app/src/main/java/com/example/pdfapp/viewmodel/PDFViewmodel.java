package com.example.pdfapp.viewmodel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pdfapp.repository.PDFRepo;

import java.util.ArrayList;

public class PDFViewmodel extends ViewModel {


    private final PDFRepo repository;
private LiveData<ArrayList<String>> RELATEDCOURSES;

    private LiveData<ArrayList<String>> RELATED_PDF;



    public PDFViewmodel() {
        repository=PDFRepo.getInstance();
    }


    public String getAccesstoken(String coursename,String pdfname){

        return repository.getPDFaccesstoken(coursename,pdfname);

    }


    public void uploadupf(Uri pdfuri, String PDFname, String PDFrelated, Context context){
         repository.uploadpdf(pdfuri,PDFname,PDFrelated,context);
    }

    public LiveData<ArrayList<String>> getallrealtedpds(String coursename){

        RELATED_PDF= repository.getallpdf_list(coursename);
        return RELATED_PDF;
    }

    public LiveData<ArrayList<String>> getallrelatedcourses(){

        RELATEDCOURSES= repository.getallpdf();
return RELATEDCOURSES;
    }
}
