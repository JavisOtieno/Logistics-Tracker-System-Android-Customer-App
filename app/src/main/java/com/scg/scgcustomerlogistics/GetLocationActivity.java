package com.scg.scgcustomerlogistics;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GetLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private PlacesClient placesClient;
    private SearchView searchView;
    private GoogleMap googleMap;
    private MapView mapView;
    private Marker userMarker;
    private Button submitButton;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Select Pickup");

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyALLsNWwOC09xsRAqrK0S7dINi6BpNc7iw");
        }
        placesClient = Places.createClient(this);

        // Set up the autocomplete fragment
        AutocompleteSupportFragment autocompleteFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Handle place selection
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // Get the selected place details
                LatLng selectedLatLng = place.getLatLng();
                userMarker.setPosition(selectedLatLng);
                userMarker.setTitle(place.getName());
                Log.d("Place Selected", "Place: " + place.getName());
                Log.d("Place Location", "Lat: " + selectedLatLng.latitude + ", Lng: " + selectedLatLng.longitude);

                // Move the map to the selected place
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 12));
            }

            @Override
            public void onError(com.google.android.gms.common.api.Status status) {
                Log.d("Place Selection Error", status.getStatusMessage());
            }

        });

        submitButton = (Button) findViewById(R.id.submitButton);

//        searchView = findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // Handle search query submission
//                Toasts.toastIconError(GetLocationActivity.this,query);
//                searchLocation(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                getAutocompletePredictions(newText); // Get autocomplete suggestions as the user types
//                return false;
//            }
//        });

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LatLng currentPosition = userMarker.getPosition();

                double latitude = currentPosition.latitude;
                double longitude = currentPosition.longitude;

//                Toasts.toastIconError(GetLocationActivity.this,latitude+" "+longitude);

                Intent intent = new Intent(GetLocationActivity.this,
                        SetDestinationActivity.class);
                intent.putExtra("latitude",latitude+"");
                intent.putExtra("longitude",longitude+"");
                intent.putExtra("locationName",userMarker.getTitle());
                Toast.makeText(GetLocationActivity.this,userMarker.getTitle()+"-",Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });

    }

    private void getAutocompletePredictions(String query) {
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setSessionToken(token)
                .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener((response) -> {
                    // Display suggestions
                    List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                    for (AutocompletePrediction prediction : predictions) {
                        String placeName = prediction.getFullText(null).toString();
                        // Handle the predictions, you can show them in a custom dropdown or use a simple log
                        Log.d("Autocomplete", "Prediction: " + placeName);
                    }
                })
                .addOnFailureListener((exception) -> {
                    exception.printStackTrace();
                    // Handle failure
                });
    }

    private void searchLocation(String query) {
        Geocoder geocoder = new Geocoder(this);
//        Toasts.toastIconError(GetLocationActivity.this, "-"+query);
        Toast.makeText(this, "-"+query, Toast.LENGTH_SHORT).show();
        try {

            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                if (userMarker != null) {
                    userMarker.setPosition(latLng);
                    userMarker.setTitle(address.getAddressLine(0));
                } else {
                    userMarker = googleMap.addMarker(new MarkerOptions().
                            position(latLng).title(query));
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Adjust zoom level as needed
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle errors (e.g., no results or network failure)
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        Double latitude=Double.parseDouble(
                getIntent().getStringExtra("latitude"));
        Double longitude=Double.parseDouble(
                getIntent().getStringExtra("longitude"));
        String initialLocationName = getIntent().getStringExtra("locationName");


        LatLng initialPosition = new LatLng(latitude, longitude);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 12));

        // Add a marker and allow dragging
        userMarker = googleMap.addMarker(new MarkerOptions()
                .position(initialPosition)
                .draggable(true));
        userMarker.setTitle(initialLocationName);

        // Update marker position on drag
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) { }

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
//                userMarker.setPosition(marker.getPosition());
//                userMarker.setTitle(marker.getTitle());
//
//                Toast.makeText(GetLocationActivity.this,marker.getTitle()+marker.getPosition().latitude+marker.getPosition().longitude+"-drag",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onMarkerDragEnd(@NonNull final Marker marker) {
                final LatLng pos = marker.getPosition();

                // Kick off reverse geocoding on a background thread
                new Thread(() -> {
                    Geocoder geocoder = new Geocoder(GetLocationActivity.this, Locale.getDefault());
                    String addressText = "Unknown location";

                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                pos.latitude, pos.longitude, 1);

                        if (addresses != null && !addresses.isEmpty()) {
                            Address addr = addresses.get(0);
                            // Build a concise address line (e.g. "Nairobi, Kenya")
                            addressText = addr.getAddressLine(0);
//                            String city = addr.getLocality();
//                            String country = addr.getCountryName();
//                            addressText = (city != null ? city : "") +
//                                    (city != null && country != null ? ", " : "") +
//                                    (country != null ? country : "");
//                            if (addressText.trim().isEmpty()) {
//                                // fallback to full address line
//                                addressText = addr.getAddressLine(0);
//                            }
                        }
                    } catch (IOException e) {
                        Log.e("GetLocationActivity", "Geocoder failed", e);
                    }

                    final String title = addressText;

                    // Update marker on the UI thread
                    runOnUiThread(() -> {
                        userMarker.setTitle(title);
                        userMarker.showInfoWindow();
                        Toast.makeText(
                                GetLocationActivity.this,
                                "Moved to: " + title,
                                Toast.LENGTH_SHORT
                        ).show();
                    });
                }).start();
            }


        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
