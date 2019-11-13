package com.danapps.polytech.fragments.tabs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.danapps.polytech.R;
import com.danapps.polytech.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_navigation, null);
        NavigationChooseFragment navigationChooseFragment = new NavigationChooseFragment();
        SharedPreferences sPref = Objects.requireNonNull(getActivity()).getSharedPreferences("TimedInfo", Context.MODE_PRIVATE);
        String[] buildingsArray = getResources().getStringArray(R.array.buildings);
        MarkerOptions[] places = {
                new MarkerOptions().position(new LatLng(60.007214, 30.372825)).title(buildingsArray[0]),
                new MarkerOptions().position(new LatLng(60.008848, 30.372723)).title(buildingsArray[1]),
                new MarkerOptions().position(new LatLng(60.008428, 30.375230)).title(buildingsArray[2]),
                new MarkerOptions().position(new LatLng(60.007161, 30.381251)).title(buildingsArray[3]),
                new MarkerOptions().position(new LatLng(60.007429, 30.377105)).title(buildingsArray[4]),
                new MarkerOptions().position(new LatLng(59.999852, 30.374400)).title(buildingsArray[5]),
                new MarkerOptions().position(new LatLng(60.000091, 30.367683)).title(buildingsArray[6]),
                new MarkerOptions().position(new LatLng(60.000666, 30.366200)).title(buildingsArray[7]),
                new MarkerOptions().position(new LatLng(60.000582, 30.368989)).title(buildingsArray[8]),
                new MarkerOptions().position(new LatLng(60.009048, 30.377305)).title(buildingsArray[9]),
                new MarkerOptions().position(new LatLng(60.007133, 30.390397)).title(buildingsArray[10]),
                new MarkerOptions().position(new LatLng(60.007729, 30.389621)).title(buildingsArray[11]),
                new MarkerOptions().position(new LatLng(60.005673, 30.381761)).title(buildingsArray[12]),
                new MarkerOptions().position(new LatLng(60.006340, 30.382602)).title(buildingsArray[13]),
                new MarkerOptions().position(new LatLng(60.006556, 30.376312)).title(buildingsArray[14]),
                new MarkerOptions().position(new LatLng(60.007672, 30.376143)).title(buildingsArray[15]),
                new MarkerOptions().position(new LatLng(60.005903, 30.378971)).title(buildingsArray[16]),
                new MarkerOptions().position(new LatLng(60.002819, 30.368477)).title(buildingsArray[17]),
                new MarkerOptions().position(new LatLng(60.005921, 30.374256)).title(buildingsArray[18]),
                new MarkerOptions().position(new LatLng(60.007420, 30.379492)).title(buildingsArray[19]),
                new MarkerOptions().position(new LatLng(60.003102, 30.374487)).title(buildingsArray[20]),
                new MarkerOptions().position(new LatLng(60.009391, 30.371517)).title(buildingsArray[21]),
                new MarkerOptions().position(new LatLng(59.994482, 30.356609)).title(buildingsArray[22]),
                new MarkerOptions().position(new LatLng(60.004634, 30.379182)).title(buildingsArray[23]),
                new MarkerOptions().position(new LatLng(60.004881, 30.370738)).title(buildingsArray[24]),
                new MarkerOptions().position(new LatLng(60.004806, 30.377978)).title(buildingsArray[25]),
        };

        if (sPref.getBoolean("isRoute", false)) {
            place1 = places[sPref.getInt("Place1", 0)];
            place2 = places[sPref.getInt("Place2", 1)];
        } else {
            place1 = places[0];
            place2 = places[0];
        }


        mapView = view.findViewById(R.id.nav_map_view2);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_key))
                .build();

        if (currentPolyline != null) {
            currentPolyline.remove();
        }


        if (sPref.getBoolean("isRoute", false)) {
            DirectionsApiRequest request =
                    DirectionsApi
                    .newRequest(geoApiContext)
                    .origin(place1.getPosition().latitude + "," + place1.getPosition().longitude)
                    .destination(place2.getPosition().latitude + "," + place2.getPosition().longitude)
                    .mode(TravelMode.WALKING);

            request.setCallback(new PendingResult.Callback<DirectionsResult>() {
                @Override
                public void onResult(final DirectionsResult result) {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() -> {
                        PolylineOptions polylineOptions =
                                new PolylineOptions()
                                        .addAll(PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath()))
                                        .width(30)
                                        .color(Color.rgb(50,150,60));

                        currentPolyline = googleMap.addPolyline(polylineOptions);
                    });
                }

                @Override
                public void onFailure(Throwable e) {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Log.e("NavigationFragment", e.getLocalizedMessage());
                    mainHandler.post(() -> Toast.makeText(getContext(), "Route failed", Toast.LENGTH_SHORT).show());
                }
            });

            try {
                MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }


            sPref.edit().remove("isRoute").remove("Place1").remove("Place2").apply();
        }


        mapView.getMapAsync(googleMap -> {
            try {
                boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Objects.requireNonNull(getContext()), R.raw.mapstyle));
                if (!success)
                    Log.e("MAP", "parsing failed");
            }
            catch (Resources.NotFoundException e) {
                Log.e("MAP", "Can`t find style. Error" + e);
            }

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //TODO: что то тут должно быть но что не моя забота
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            } else {
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.setMyLocationEnabled(true);
            }


            NavigationFragment.this.googleMap = googleMap;
            NavigationFragment.this.googleMap
                    .addMarker(place1)
                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            NavigationFragment.this.googleMap
                    .addMarker(place2)
                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            NavigationFragment.this.googleMap.setBuildingsEnabled(true);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place1.getPosition(), 17f));
        });


        view.findViewById(R.id.nav_backBTN).setOnClickListener(v ->
                ((MainActivity) getActivity()).loadFragment(1));

        view.findViewById(R.id.nav_plusZoomBTN).setOnClickListener(v ->
                mapView.getMapAsync(googleMap -> {
                    if (googleMap.getCameraPosition().zoom < 20)
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom + 1));
                }));

        view.findViewById(R.id.nav_minusZoomBTN).setOnClickListener(v ->
                mapView.getMapAsync(googleMap -> {
                    if (googleMap.getCameraPosition().zoom > 0)
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(googleMap.getCameraPosition().zoom - 1));
                }));

        view.findViewById(R.id.nav_myPositionBTN).setOnClickListener(v ->
                mapView.getMapAsync(googleMap -> {
                    if (googleMap.isMyLocationEnabled() && googleMap.getMyLocation() != null)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()), 17f));
                    else if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        googleMap.setMyLocationEnabled(true);
                    }
                }));

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