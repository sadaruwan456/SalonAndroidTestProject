package com.dilanka456.myprojectcustomer10.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.dilanka456.myprojectcustomer10.Holder.AllSalonListHolder;
import com.dilanka456.myprojectcustomer10.Model.Salon;
import com.dilanka456.myprojectcustomer10.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class AllSalonListAdapter extends FirestoreRecyclerAdapter<Salon, AllSalonListHolder> {
    private View view;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AllSalonListAdapter(@NonNull FirestoreRecyclerOptions<Salon> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllSalonListHolder holder, int position, @NonNull Salon model) {
        holder.salon_name.setText(model.getName());
        if (model.isStatus()){
            holder.salon_status.setTextColor(ContextCompat.getColor(view.getContext(), R.color.light_green));
            holder.salon_status.setText("Open");
        }else{
            holder.salon_status.setTextColor(ContextCompat.getColor(view.getContext(), R.color.red));
            holder.salon_status.setText("Close");
        }

        Picasso.get().load(model.getLogo_path()).into(holder.salon_img);
        String salonId = getSnapshots().getSnapshot(position).getId();
        holder.salonDocId= salonId;

    }

    @NonNull
    @Override
    public AllSalonListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_salon_list_item, parent, false);

        this.view = view;
        return new AllSalonListHolder(view);
    }
}
