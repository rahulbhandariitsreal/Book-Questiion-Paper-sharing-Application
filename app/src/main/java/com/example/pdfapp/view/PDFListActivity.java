package com.example.pdfapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pdfapp.PDFuploaderActivity;
import com.example.pdfapp.R;
import com.example.pdfapp.adapter.List_Adapter;
import com.example.pdfapp.databinding.ActivityPdflistBinding;
import com.example.pdfapp.viewmodel.PDFViewmodel;

import java.util.ArrayList;

public class PDFListActivity extends AppCompatActivity implements ClickListener {

    public static String COURSE_NAME;
    private ActivityPdflistBinding binding;

    public static  String LIST_NAME;


    private List_Adapter adapter;
    private ArrayList<String> list_pdf=new ArrayList<>();
    private RecyclerView recyclerView;

    private PDFViewmodel viewmodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=    ActivityPdflistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        COURSE_NAME=getIntent().getStringExtra("course_name");
        binding.tittle.setText(COURSE_NAME);



        recyclerView=findViewById(R.id.showrecyclerlist);
        adapter=new List_Adapter(list_pdf,this,2);
        adapter.setListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        viewmodel=new ViewModelProvider(this).get(PDFViewmodel.class);
        setrecyclerview();







    }



    private void setrecyclerview() {

        viewmodel.getallrealtedpds(COURSE_NAME).observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {

                list_pdf.clear();
                list_pdf.addAll(strings);

            }
        });
        adapter.notifyDataSetChanged();
    }

    public void Addpdfnew(View view) {
        startActivity(new Intent(PDFListActivity.this, PDFuploaderActivity.class));
    }

    @Override
    public void itemclick(int position) {

   LIST_NAME=list_pdf.get(position);
        Intent intent=new Intent(PDFListActivity.this, ShowPDF.class);
        intent.putExtra("pdf_name",LIST_NAME);
        intent.putExtra("course_name",COURSE_NAME);
        startActivity(intent);

    }

    public void exitmefromlist(View view) {
        onBackPressed();
    }
}