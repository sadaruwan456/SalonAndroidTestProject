package com.dilanka456.myprojectsalonapp10.Holders;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectsalonapp10.Create_discount;
import com.dilanka456.myprojectsalonapp10.Create_packages;
import com.dilanka456.myprojectsalonapp10.Model.Package;
import com.dilanka456.myprojectsalonapp10.R;

import java.security.PublicKey;

public class PackageListHolader extends RecyclerView.ViewHolder {

    public ImageView pack_img;
    public TextView pack_name,pack_des,pack_price,dic_lbl;
    public Button pack_edit_btn,pack_dic_btn;
    public Package package_new;
    public String salon_doc_id;
    public String packageId;


public PackageListHolader(View item){

    super(item);
    pack_img = item.findViewById(R.id.pack_image_view);
    pack_name =item.findViewById(R.id.pack_name_lbl);
    pack_des = item.findViewById(R.id.pack_des_lbl);
    pack_edit_btn = item.findViewById(R.id.pack_edite_btn);
    pack_price = item.findViewById(R.id.pack_price_lbl);
    pack_dic_btn = item.findViewById(R.id.pack_distount_btn);
    dic_lbl = item.findViewById(R.id.pack_iten_dis_lbl);


    pack_edit_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(item.getContext(), Create_packages.class);
            intent.putExtra("salonDocId",salon_doc_id);
            intent.putExtra("packFound",true);
            intent.putExtra("packageId",packageId);
            item.getContext().startActivity(intent);

        }
    });

    pack_dic_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(item.getContext(), Create_discount.class);
            intent.putExtra("salonDocId",salon_doc_id);
            intent.putExtra("packageId",packageId);
            item.getContext().startActivity(intent);
        }
    });



}
}
