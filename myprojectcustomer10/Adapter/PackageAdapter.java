package com.dilanka456.myprojectcustomer10.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dilanka456.myprojectcustomer10.Holder.PackageListHolder;
import com.dilanka456.myprojectcustomer10.Model.Package;
import com.dilanka456.myprojectcustomer10.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class PackageAdapter extends FirestoreRecyclerAdapter<Package, PackageListHolder> {




    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PackageAdapter(@NonNull FirestoreRecyclerOptions<Package> options) {
        super(options);


    }

    @Override
    protected void onBindViewHolder(@NonNull PackageListHolder holder, int position, @NonNull Package model) {
        holder.packName.setText(model.getName());
        holder.packPrice.setText("Rs."+model.getPrice());
        Picasso.get().load(model.getImg_url()).into(holder.packImg);
        String packDocId = getSnapshots().getSnapshot(position).getId();
        holder.pack_doc_id = packDocId;



    }

    @NonNull
    @Override
    public PackageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_list_item, parent, false);
        return new PackageListHolder(view);
    }
}


