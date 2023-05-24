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

import com.dilanka456.myprojectcustomer10.Adapter.DiscountListAdapter;
import com.dilanka456.myprojectcustomer10.Model.Discount;
import com.dilanka456.myprojectcustomer10.Model.Salon;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Discount_list_fragment extends Fragment {

    RecyclerView dicountList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DiscountListAdapter discountListAdapter;
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discount_list_fragment, container, false);
        dicountList = view.findViewById(R.id.discount_relist_view);
        dicountList.setHasFixedSize(true);
        setDiscountList();

        email = ((Home) getActivity()).getEmail();
        return view;
    }

    public void setDiscountList(){
        Query query = db.collection("Discount");
        FirestoreRecyclerOptions<Discount> salonOption = new FirestoreRecyclerOptions.Builder<Discount>().setQuery(query,Discount.class).build();

        discountListAdapter = new DiscountListAdapter(salonOption);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        dicountList.setLayoutManager(linearLayoutManager);
        dicountList.setAdapter(discountListAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (discountListAdapter != null) {
            discountListAdapter.startListening();
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        if (discountListAdapter != null) {
            discountListAdapter.stopListening();
        }
    }
}
