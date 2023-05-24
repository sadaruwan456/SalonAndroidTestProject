package com.dilanka456.myprojectcustomer10.Holder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectcustomer10.One_packageBook_view;
import com.dilanka456.myprojectcustomer10.R;

public class PackageListHolder extends RecyclerView.ViewHolder {

public ImageView packImg;
public TextView packName,packPrice;
public Button packBtn;
public String pack_doc_id;
    public PackageListHolder(View view){
        super(view);
        packImg = view.findViewById(R.id.pack_image_view);
        packName = view.findViewById(R.id.pack_name_lbl);
        packPrice = view.findViewById(R.id.pack_price_lbl);
        packBtn = view.findViewById(R.id.pack_book_btn);

        packBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), One_packageBook_view.class);
                intent.putExtra("packDocId",pack_doc_id);
                view.getContext().startActivity(intent);
            }
        });


    }
}
