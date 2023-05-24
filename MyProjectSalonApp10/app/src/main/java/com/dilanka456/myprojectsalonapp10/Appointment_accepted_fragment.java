package com.dilanka456.myprojectsalonapp10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectsalonapp10.Adapters.AppAcceptListAdapter;
import com.dilanka456.myprojectsalonapp10.Model.Appointment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Appointment_accepted_fragment extends Fragment {

    RecyclerView accept_list;
    private String salonDocId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AppAcceptListAdapter appAcceptListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_accepted_fragment, container, false);
        accept_list = view.findViewById(R.id.app_accept_list_view);
        accept_list.setHasFixedSize(true);
        salonDocId = ((Apointments) getActivity()).getSalonDocId();
        setAcceptList(salonDocId);
        return view;
    }

    private void setAcceptList(String salonDocId) {
        Query query = db.collection("Appointment").whereEqualTo("status","Payed").whereEqualTo("salonDocId",salonDocId);

        FirestoreRecyclerOptions<Appointment> accOptions = new FirestoreRecyclerOptions.Builder<Appointment>().setQuery(query,Appointment.class).build();

        appAcceptListAdapter = new AppAcceptListAdapter(accOptions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        accept_list.setLayoutManager(linearLayoutManager);
        accept_list.setAdapter(appAcceptListAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (appAcceptListAdapter != null) {
            appAcceptListAdapter.startListening();
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        if (appAcceptListAdapter != null) {
            appAcceptListAdapter.stopListening();
        }
    }
}
