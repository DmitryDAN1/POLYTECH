package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class NavigationFragment extends Fragment {

    private MapView mapView;
    private GoogleMap googleMap;
    MarkerOptions place1, place2;
    private Polyline currentPolyline;
    GeoApiContext geoApiContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_navigation, null);

        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");

        mapView = view.findViewById(R.id.nav_map_view2);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        geoApiContext = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_key))
                .build();

        view.findViewById(R.id.load_btn).setOnClickListener(v -> {
            if(currentPolyline != null) {
                currentPolyline.remove();
            }

            DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                    .origin("27.658143,85.3199503")
                    .destination("27.667491,85.3208583")
                    .mode(TravelMode.WALKING);

            request.setCallback(new PendingResult.Callback<DirectionsResult>() {
                @Override
                public void onResult(DirectionsResult result) {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() -> currentPolyline = googleMap.addPolyline(new PolylineOptions().addAll(
                            PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath()))
                    ));
                }

                @Override
                public void onFailure(Throwable e) {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() -> Toast.makeText(getContext(), "Route failed", Toast.LENGTH_SHORT).show());
                }
            });
        });

        try {
            MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(googleMapp -> {
            googleMap = googleMapp;
            googleMap.addMarker(place1);
            googleMap.addMarker(place2);
            //LatLng polytechMARKER = new LatLng(60.007207, 30.372812);
            //googleMap.addMarker(new MarkerOptions().position(polytechMARKER).title("ЦЕ ГЗ")).showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place1.getPosition(), 15f));
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}