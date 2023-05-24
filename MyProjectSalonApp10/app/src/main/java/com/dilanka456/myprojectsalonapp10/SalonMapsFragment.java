package com.dilanka456.myprojectsalonapp10;

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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class SalonMapsFragment extends Fragment {

    private static final int LOCATION_PERMISSION = 100;
    private static final String TAG = "MapFragmentTag";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap currentMap;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            currentMap = googleMap;

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SalonMapsFragment.super.getContext());
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            setCurrentLocation();
        }
    };

 LatLng currentLocation;
    private MarkerOptions currentMarker;

    private void setCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(SalonMapsFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SalonMapsFragment.super.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);

            return;
        }
        Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
        lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                 currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                BitmapDescriptor icon_current = getBitDes(getActivity(),R.drawable.ic_tracking);

                currentMarker = new MarkerOptions().icon(icon_current).draggable(true).position(currentLocation).title("I'm here");
                currentMap.addMarker(currentMarker);
                currentMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                currentMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                currentMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        Log.d(TAG,"DragStart");
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        Log.d(TAG,"Draging");

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        Log.d(TAG,"SetPosition");
                        currentLocation=marker.getPosition();
                        ((Location_set_activity)getActivity()).setCurrentLatLon(currentLocation);

                    }
                });
            }
        });
    }

    private BitmapDescriptor getBitDes(FragmentActivity activity, int icon) {
        Drawable LAYER_1 = ContextCompat.getDrawable(activity,icon);
        LAYER_1.setBounds(0, 0, LAYER_1.getIntrinsicWidth(), LAYER_1.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(LAYER_1.getIntrinsicWidth(), LAYER_1.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        LAYER_1.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_salon_maps, container, false);
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
}