package com.dilanka456.myprojectcustomer10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dilanka456.myprojectcustomer10.Model.Salon;
import com.dilanka456.myprojectcustomer10.directionsLib.FetchURL;
import com.dilanka456.myprojectcustomer10.pojo.mapDistanceObj;
import com.dilanka456.myprojectcustomer10.pojo.mapTimeObj;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MapsFragment extends Fragment {

    private static final int LOCATION_PERMISSION = 100;
    private GoogleMap currentMap;
    private static final String TAG = "MapFragment";
    private FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();




    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            currentMap = googleMap;
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsFragment.super.getContext());





//
//            Toast.makeText(getActivity().getApplicationContext(), "SalonDosId: "+salonDocId, Toast.LENGTH_SHORT).show();

//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



            UpdateCustomerLocation();
        }
    };
    private LatLng customerLocation;
    private LatLng salonLatLng;
    private LatLng salonLocation;
    private double salonLatdd;
    private double salonLondd;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==LOCATION_PERMISSION){
            if (permissions.length>0){
                UpdateCustomerLocation();
            }
        }
    }
    private void UpdateCustomerLocation() {

        TextView latView = getActivity().findViewById(R.id.salon_doc_test_lat_lbl);
        String salonLat = latView.getText().toString();

        TextView lonView = getActivity().findViewById(R.id.salon_doc_test_lon_lbl);
        String salonLon = lonView.getText().toString();
        salonLatdd = Double.parseDouble(salonLat);
        salonLondd = Double.parseDouble(salonLon);
        if (ActivityCompat.checkSelfPermission(MapsFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsFragment.super.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);
            return;
        }
        Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();

        lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
            Polyline destPoltline;
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                     customerLocation = new LatLng(location.getLatitude(), location.getLongitude());


                     salonLocation = new LatLng(salonLatdd,salonLondd);

                    BitmapDescriptor icon_current = getBitDes(getActivity(),R.drawable.walk_icon);
                    BitmapDescriptor icon_dest = getBitDes(getActivity(),R.drawable.ic_tracking);

                    MarkerOptions currentLocation  = new MarkerOptions().icon(icon_current).draggable(false).position(customerLocation).title("I'm here");
                    MarkerOptions destinationLocation  = new MarkerOptions().icon(icon_dest).draggable(false).position(salonLocation).title("I want to go");

                   currentMap.addMarker(currentLocation);
                    currentMap.addMarker(destinationLocation);
                    currentMap.moveCamera(CameraUpdateFactory.newLatLng(customerLocation));
                    currentMap.moveCamera(CameraUpdateFactory.zoomTo(13));//1 to 20 values ok for zoom

                    new FetchURL() {
                        @Override
                        public void onTaskDone(Object... values) {
                            if (destPoltline!=null){
                                destPoltline.remove();
                            }

                            destPoltline = currentMap.addPolyline((PolylineOptions) values[0]);
                        }

                        @Override
                        public void onDistanceTaskDone(mapDistanceObj distance) {

                        }

                        @Override
                        public void onTimeTaskDone(mapTimeObj time) {

                        }
                    }.execute(getUrl(customerLocation,salonLocation,"driving"),"driving");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {

            mapFragment.getMapAsync(callback);
        }
    }

    private BitmapDescriptor getBitDes(FragmentActivity activity, int icon) {
        Drawable LAYER_1 = ContextCompat.getDrawable(activity,icon);
        LAYER_1.setBounds(0, 0, LAYER_1.getIntrinsicWidth(), LAYER_1.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(LAYER_1.getIntrinsicWidth(), LAYER_1.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        LAYER_1.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        Log.d(TAG,"URL:"+url);
        return url;
    }




}