package com.danapps.polytech.fragments.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.util.Objects;

public class NavigationFragment extends Fragment {

    private MapView mapView;
    private GoogleMap googleMap;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    private GeoApiContext geoApiContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_navigation, null);


        MarkerOptions[] places = {
                new MarkerOptions().position(new LatLng(60.007224, 30.372821)).title(getString(R.string.mainBuilding)),
                new MarkerOptions().position(new LatLng(60.008859, 30.372726)).title(getString(R.string.corpus1)),
                new MarkerOptions().position(new LatLng(60.008492, 30.374974)).title(getString(R.string.corpus2)),
                new MarkerOptions().position(new LatLng(60.007146, 30.381604)).title(getString(R.string.corpus3)),
                new MarkerOptions().position(new LatLng(60.007449, 30.376937)).title(getString(R.string.corpus4)),
                new MarkerOptions().position(new LatLng(59.999852, 30.374400)).title(getString(R.string.corpus5)),
                new MarkerOptions().position(new LatLng(60.000091, 30.367683)).title(getString(R.string.corpus6)),
                new MarkerOptions().position(new LatLng(60.000666, 30.366200)).title(getString(R.string.corpus9)),
                new MarkerOptions().position(new LatLng(60.000582, 30.368989)).title(getString(R.string.corpus10)),
                new MarkerOptions().position(new LatLng(60.009048, 30.377305)).title(getString(R.string.corpus11)),
                new MarkerOptions().position(new LatLng(60.007133, 30.390397)).title(getString(R.string.corpus15)),
                new MarkerOptions().position(new LatLng(60.007729, 30.389621)).title(getString(R.string.corpus16))
        };

        place1 = places[2];
        place2 = places[7];

        mapView = view.findViewById(R.id.nav_map_view2);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        geoApiContext = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_key))
                .build();

        view.findViewById(R.id.load_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPolyline != null) {
                    currentPolyline.remove();
                }

                DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                        .origin(place1.getPosition().latitude + "," + place1.getPosition().longitude)
                        .destination(place2.getPosition().latitude + "," + place2.getPosition().longitude)
                        .mode(TravelMode.WALKING);

                request.setCallback(new PendingResult.Callback<DirectionsResult>() {
                    @Override
                    public void onResult(final DirectionsResult result) {
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                currentPolyline = googleMap.addPolyline(new PolylineOptions().addAll(
                                        PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath())));
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Route failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        try {
            MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                try {
                    boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Objects.requireNonNull(getContext()), R.raw.mapstyle));
                    if (!success)
                        Log.e("MAP", "parsing failed");

                } catch (Resources.NotFoundException e) {
                    Log.e("MAP", "Can`t find style. Error" + e);
                }

                googleMap.setMyLocationEnabled(true);
                NavigationFragment.this.googleMap = googleMap;
                NavigationFragment.this.googleMap.addMarker(place1);
                NavigationFragment.this.googleMap.addMarker(place2);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(places[0].getPosition(), 17f));
            }
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