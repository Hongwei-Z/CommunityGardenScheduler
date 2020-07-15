package com.CSCI3130.gardenapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;

/**
 *  This is a Google Map fragment to be added into the CreateTaskActivity
 *
 *  @author Elizabeth Eddy
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    Location currentLocation;
    LatLng selectedLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    /**
     * Constuct the View and attempt to fetch the users current location
     *
     * @param inflater
     * @param container
     * @param savedInstanceState T
     * @return the map view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_maps, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        //To Do: only if add task, not edit or details
        fetchLocation();
        return v;
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(MapFragment.this);
                    }
                }
            }
        });
    }

    /**
     * Result from application permissions request and fetch location if successful
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // set selected to default Halifax if no current location found
        if (currentLocation != null){
            selectedLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        } else {
            selectedLocation = new LatLng(44.6454, -63.5766);
        }

        // set up map
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        addMapMarker(googleMap);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 18));

        // add new marker on click
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                selectedLocation = latLng;
                addMapMarker(googleMap);
            }
        });
    }

    private void addMapMarker(GoogleMap googleMap){
        TextView locationText = getActivity().findViewById(R.id.locationText);
        DecimalFormat df = new DecimalFormat("#.#####");
        String lat = df.format(selectedLocation.latitude);
        String lng = df.format(selectedLocation.longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(selectedLocation);
        markerOptions.title("Location " + lat + " : " + lng);
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(selectedLocation));
        googleMap.addMarker(markerOptions);
        locationText.setText("Location " + lat + " : " + lng);
    }
}
