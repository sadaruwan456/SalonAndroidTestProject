package com.dilanka456.myprojectcustomer10.Holder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectcustomer10.One_packageBook_view;
import com.dilanka456.myprojectcustomer10.R;


public class DiscountListHolder extends RecyclerView.ViewHolder {

    public TextView dis_pack_name, dis_pack_price,dis_pack_dis;
    public ImageView dis_pack_img;
    public Button dis_pack_btn;
    public String pack_doc_id;


    public DiscountListHolder (View item){
        super(item);
        dis_pack_name = item.findViewById(R.id.discount_item_name);
        dis_pack_price = item.findViewById(R.id.discount_item_price);
        dis_pack_dis = item.findViewById(R.id.discount_item_dicount);
        dis_pack_img = item.findViewById(R.id.discount_item_img);
        dis_pack_btn = item.findViewById(R.id.dicount_item_book_btn);

        dis_pack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(item.getContext(), One_packageBook_view.class);
                intent.putExtra("packDocId",pack_doc_id);
                item.getContext().startActivity(intent);
            }
        });



    }
}
