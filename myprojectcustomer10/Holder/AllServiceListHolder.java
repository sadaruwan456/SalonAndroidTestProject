package com.dilanka456.myprojectcustomer10.Holder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectcustomer10.One_packageBook_view;
import com.dilanka456.myprojectcustomer10.R;

public class AllServiceListHolder extends RecyclerView.ViewHolder {

    public ImageView pack_img;
    public TextView pack_name,pack_price,pack_duration;
    public Button pack_book_btn;
    public String packDocId;


    public AllServiceListHolder(View viewItem){
        super(viewItem);

        pack_img = viewItem.findViewById(R.id.salon_pack_img);
        pack_name = viewItem.findViewById(R.id.salon_pack_name);
        pack_price = viewItem.findViewById(R.id.salon_pack_price);
        pack_duration = viewItem.findViewById(R.id.salon_pack_duration);
        pack_book_btn = viewItem.findViewById(R.id.salon_pack_book_btn);

        pack_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewItem.getContext(), One_packageBook_view.class);
                intent.putExtra("packDocId",packDocId);
                viewItem.getContext().startActivity(intent);
            }
        });
    }
}
