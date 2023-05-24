package com.dilanka456.myprojectcustomer10;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectcustomer10.Adapter.AllSalonListAdapter;
import com.dilanka456.myprojectcustomer10.Model.Salon;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class All_salon_fragment extends Fragment {


    RecyclerView all_salon_list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AllSalonListAdapter allSalonListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_salon_fragment, container, false);

        all_salon_list = view.findViewById(R.id.all_salon_load_list_view);
        all_salon_list.setHasFixedSize(true);
        setAllSalonList();




        return view;
    }

    private void setAllSalonList() {
       Query query = db.collection("Salon");
        FirestoreRecyclerOptions<Salon> options = new FirestoreRecyclerOptions.Builder<Salon>().setQuery(query,Salon.class).build();

        allSalonListAdapter = new AllSalonListAdapter(options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        all_salon_list.setLayoutManager(linearLayoutManager);
        all_salon_list.setAdapter(allSalonListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (allSalonListAdapter != null) {
            allSalonListAdapter.startListening();
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        if (allSalonListAdapter != null) {
            allSalonListAdapter.stopListening();
        }
    }
}
