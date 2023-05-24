package com.dilanka456.myprojectcustomer10.Holder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectcustomer10.R;
import com.dilanka456.myprojectcustomer10.salon_item_view;


public class SuggestSalonListHolder extends RecyclerView.ViewHolder {

    public ImageView suggest_img;
    public TextView suggest_name;
    public Button suggest_btn;
    public String salon_doc_id;
    public SuggestSalonListHolder(View item){
        super(item);

        suggest_img= item.findViewById(R.id.suggest_img);
        suggest_name = item.findViewById(R.id.suggest_name);
        suggest_btn = item.findViewById(R.id.suggest_btn);
        suggest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(item.getContext(), salon_item_view.class);
                intent.putExtra("SalonDocId",salon_doc_id);
                item.getContext().startActivity(intent);
            }
        });

    }
}
