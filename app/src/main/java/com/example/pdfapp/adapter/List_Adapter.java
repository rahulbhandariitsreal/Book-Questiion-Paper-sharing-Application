package com.example.pdfapp.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdfapp.view.ClickListener;
import com.example.pdfapp.R;

import java.util.ArrayList;

public class List_Adapter extends RecyclerView.Adapter<List_Adapter.ViewHolder> {

    private ArrayList<String> pathname;
    private Context comtext;

    private ClickListener listener;

    private int level;


    public List_Adapter(ArrayList<String> coursesname, Context comtext, int level) {

        this.pathname = coursesname;
        this.comtext = comtext;
        this.level = level;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (level == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pdf, parent, false);
        }


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        String coursename = pathname.get(position);
        holder.name.setText(coursename);
        if (level == 1) {
            holder.imageView.setImageResource(R.drawable.books);
        } else {
            holder.imageView.setImageResource(R.drawable.pdf);
        }
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return pathname.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.course_name);
            if (level == 1) {
                imageView = itemView.findViewById(R.id.pdfcourse);
            } else {
                imageView = itemView.findViewById(R.id.pdfimage);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.itemclick(getAdapterPosition());

                }
            });
        }
    }

}
