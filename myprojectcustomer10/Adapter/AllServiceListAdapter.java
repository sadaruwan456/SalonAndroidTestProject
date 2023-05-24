package com.dilanka456.myprojectcustomer10.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dilanka456.myprojectcustomer10.Holder.AllServiceListHolder;
import com.dilanka456.myprojectcustomer10.Model.Package;
import com.dilanka456.myprojectcustomer10.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class AllServiceListAdapter extends FirestoreRecyclerAdapter<Package, AllServiceListHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AllServiceListAdapter(@NonNull FirestoreRecyclerOptions<Package> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllServiceListHolder holder, int position, @NonNull Package model) {

        holder.pack_name.setText(model.getName());
        holder.pack_price.setText("Rs."+model.getPrice());
        holder.pack_duration.setText(model.getDuration()+"Minutes");
        Picasso.get().load(model.getImg_url()).into(holder.pack_img);
        String packId = getSnapshots().getSnapshot(position).getId();
        holder.packDocId= packId;

    }

    @NonNull
    @Override
    public AllServiceListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salon_service_list_item, parent, false);
        return new AllServiceListHolder(view);
    }


}
