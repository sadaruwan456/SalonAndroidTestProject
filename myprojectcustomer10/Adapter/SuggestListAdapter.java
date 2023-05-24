package com.dilanka456.myprojectcustomer10.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dilanka456.myprojectcustomer10.Holder.SuggestSalonListHolder;
import com.dilanka456.myprojectcustomer10.Model.Salon;
import com.dilanka456.myprojectcustomer10.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class SuggestListAdapter extends FirestoreRecyclerAdapter<Salon, SuggestSalonListHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SuggestListAdapter(@NonNull FirestoreRecyclerOptions<Salon> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SuggestSalonListHolder holder, int position, @NonNull Salon model) {
        holder.suggest_name.setText(model.getName());
        Picasso.get().load(model.getLogo_path()).into(holder.suggest_img);
        String salonDocId = getSnapshots().getSnapshot(position).getId();
        holder.salon_doc_id= salonDocId;

    }

    @NonNull
    @Override
    public SuggestSalonListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggest_salon_list_item, parent, false);
        return new SuggestSalonListHolder(inflate);
    }
}
