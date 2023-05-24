package com.dilanka456.myprojectcustomer10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dilanka456.myprojectcustomer10.Adapter.AllServiceListAdapter;
import com.dilanka456.myprojectcustomer10.Model.Package;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Top_rated_List_Fragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView allService_list;
    private AllServiceListAdapter allServiceListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_rated_list_fragment, container, false);
        allService_list = view.findViewById(R.id.all_service_list_view);
        allService_list.setHasFixedSize(true);
        setAllserviceList();


        return view;
    }

    private void setAllserviceList() {

        Query query = db.collection("Package");
        FirestoreRecyclerOptions<Package> options = new FirestoreRecyclerOptions.Builder<Package>().setQuery(query,Package.class).build();

         allServiceListAdapter = new AllServiceListAdapter(options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        allService_list.setLayoutManager(linearLayoutManager);
        allService_list.setAdapter(allServiceListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (allServiceListAdapter != null) {
            allServiceListAdapter.startListening();
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        if (allServiceListAdapter != null) {
            allServiceListAdapter.stopListening();
        }
    }
}


//********************Create ithuru fragment and set in home*****************************