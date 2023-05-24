package com.dilanka456.myprojectsalonapp10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectsalonapp10.Adapters.AppRequstListAdapter;
import com.dilanka456.myprojectsalonapp10.Model.Appointment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Appointment_request_fragment extends Fragment {
    RecyclerView request_list;
    private String salonDocId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AppRequstListAdapter appRequstListAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_request_fragment, container, false);
        request_list = view.findViewById(R.id.app_request_list_view);
        request_list.setHasFixedSize(true);
        salonDocId = ((Apointments) getActivity()).getSalonDocId();
        setRequestList(salonDocId);

        return view;
    }

    private void setRequestList(String salonId) {
         Query query = db.collection("Appointment").whereEqualTo("status","Placed").whereEqualTo("salonDocId",salonId);
//        FirestoreRecyclerOptions<Appointment> options = new FirestoreRecyclerOptions.Builder<Appointment>().setQuery(query, Appointment.class).build();

      FirestoreRecyclerOptions<Appointment> AppOptions = new FirestoreRecyclerOptions.Builder<Appointment>().setQuery(query,Appointment.class).build();

         appRequstListAdapter = new AppRequstListAdapter(AppOptions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        request_list.setLayoutManager(linearLayoutManager);
        request_list.setAdapter(appRequstListAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (appRequstListAdapter != null) {
            appRequstListAdapter.startListening();
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        if (appRequstListAdapter != null) {
            appRequstListAdapter.stopListening();
        }
    }

}
