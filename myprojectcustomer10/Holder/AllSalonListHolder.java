package com.dilanka456.myprojectcustomer10.Holder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectcustomer10.R;
import com.dilanka456.myprojectcustomer10.salon_item_view;

public class AllSalonListHolder extends RecyclerView.ViewHolder {

    public ImageView salon_img;
    public TextView salon_name,salon_status;
    public Button book_btn;
    public String salonDocId;

    public AllSalonListHolder(View viewItem){
        super(viewItem);

        salon_img = viewItem.findViewById(R.id.all_salon_img);
        salon_name = viewItem.findViewById(R.id.all_salon_name);
        salon_status = viewItem.findViewById(R.id.all_salon_status);
        book_btn = viewItem.findViewById(R.id.all_salon_book_btn);

        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewItem.getContext(), salon_item_view.class);
                intent.putExtra("SalonDocId",salonDocId);
                viewItem.getContext().startActivity(intent);
            }
        });
    }
}
