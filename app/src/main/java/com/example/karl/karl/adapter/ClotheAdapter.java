package com.example.karl.karl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karl.karl.R;
import com.example.karl.karl.model.Clothe;

import java.util.ArrayList;

public class ClotheAdapter extends RecyclerView.Adapter<ClotheAdapter.ClotheViewHolder>{
    private ArrayList<Clothe> dataList;

    public ClotheAdapter(ArrayList<Clothe> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ClotheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //View view = layoutInflater.inflate(R.layout.single_view_row, parent, false);
        View view = layoutInflater.inflate(R.layout.activity_quiz_start, parent, false);
        return new ClotheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClotheViewHolder holder, int position) {
        holder.txtClotheTitle.setText(dataList.get(position).getName());
        //holder.txtClotheBrief.setText(dataList.get(position).getBrief());
        //holder.txtClotheFilePath.setText(dataList.get(position).getFileSource());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ClotheViewHolder extends RecyclerView.ViewHolder {

        TextView txtClotheTitle, txtClotheBrief, txtClotheFilePath;

        ClotheViewHolder(View itemView) {
            super(itemView);
            /*
            txtClotheTitle =  itemView.findViewById(R.id.txt_clothe_title);
            txtClotheBrief =  itemView.findViewById(R.id.txt_clothe_brief);
            txtClotheFilePath =  itemView.findViewById(R.id.txt_clothe_file_path);*/
        }
    }
}
