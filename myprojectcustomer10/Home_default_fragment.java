package com.dilanka456.myprojectcustomer10;

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

import com.dilanka456.myprojectcustomer10.Adapter.PackageAdapter;
import com.dilanka456.myprojectcustomer10.Adapter.SuggestListAdapter;
import com.dilanka456.myprojectcustomer10.Holder.PackageListHolder;
import com.dilanka456.myprojectcustomer10.Model.Package;
import com.dilanka456.myprojectcustomer10.Model.Salon;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class Home_default_fragment extends Fragment {
    RecyclerView packListView,suggestListView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter<Package, PackageListHolder> packrecyclerAdapter;
    private PackageAdapter packageAdapter;
    private SuggestListAdapter suggestListAdapter;
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_default_fragment, container, false);
        packListView = view.findViewById(R.id.package_list_view);
        packListView.setHasFixedSize(true);
        setUpRecyclerView();

        suggestListView = view.findViewById(R.id.suggest_salon_list_view);
        suggestListView.setHasFixedSize(true);
        setUpsuggestList();

        email = ((Home) getActivity()).getEmail();


        return view;
    }

    public void setUpsuggestList(){
        Query query = db.collection("Salon").whereEqualTo("status",true);
        FirestoreRecyclerOptions<Salon> salonOption = new FirestoreRecyclerOptions.Builder<Salon>().setQuery(query,Salon.class).build();

       suggestListAdapter = new SuggestListAdapter(salonOption);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        suggestListView.setLayoutManager(linearLayoutManager);
        suggestListView.setAdapter(suggestListAdapter);


    }


    public void setUpRecyclerView(){
        Query query = db.collection("Package");

        FirestoreRecyclerOptions <Package> options = new FirestoreRecyclerOptions.Builder<Package>().setQuery(query, Package.class).build();

        packageAdapter = new PackageAdapter(options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        packListView.setLayoutManager(linearLayoutManager);
        packListView.setAdapter(packageAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (packageAdapter != null) {
            packageAdapter.startListening();
        }
        if (suggestListAdapter!=null){
            suggestListAdapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (packageAdapter != null) {
            packageAdapter.stopListening();
        }
        if (suggestListAdapter!=null){
            suggestListAdapter.startListening();
        }

    }
}
