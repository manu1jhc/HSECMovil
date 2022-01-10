package com.pango.hsec.hsec.Maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pango.hsec.hsec.R;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MapsActivity2  extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    View mapView;
    ListView listView;
    private Marker markerPais;
    Button bttnGuardar;
    String latLong;
    Double latitudGPS, longitudGPS;


    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //datos.getString("title")
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        bttnGuardar = (Button) findViewById(R.id.btnCordenas);

        bttnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("cordenada",latLong);
                intent.putExtra("tipo","maps");

                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // A partir de las versión 6 en adelante cambia las políticas sobre los permisos
        // Hay que verificar los permisos e informar al usuario si va a brindar los accesos correspondientes

        // Validamos la versión
        if( Build.VERSION.SDK_INT >= 23) {
            // Validamos si ACCESS_FINE_LOCATION tiene permisos otorgados por el usuario
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Informamos al usuario sobre que permisos se le van a solicitar.
                ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  }, MY_PERMISSION_ACCESS_FINE_LOCATION);
                return;
            } else {
                // Esta parte se ejecuta cuando los permisos son otorgados por el usuario
                getMap();
            }
        } else {
            // Esta bloque se ejecuta cuando una versión de android es inferior a la 6 o API 23, obtiene la información sobre los permisos
            // del AndroidManifest.xml
            getMap();
        }
        mMap.setOnMapClickListener(this);

    }

    private void getMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled (true);
        Bundle datos = this.getIntent().getExtras();
        latitudGPS = Double.valueOf(datos.getString("latitud"));
        longitudGPS = Double.valueOf(datos.getString("longitud"));

        //LatLng peru = new LatLng(-12.046374, -77.042793);
        LatLng peru = new LatLng(latitudGPS, longitudGPS);
        mMap.addMarker(new MarkerOptions().position(peru).title("Marcador en Perú"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(peru));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                // Si el usuario acepta los permisos
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMap();
                } else {
                    // Si el usuario no brinda los permisos
                    // Toast.makeText(MainActivity.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // Añadir marker en la posición
        //Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));

        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

        // Clears the previously touched position
        mMap.clear();

        // Animating to the touched position
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        mMap.addMarker(markerOptions);

        String format = String.format(Locale.getDefault(),
                "Lat/Lng = (%f,%f)", latLng.latitude, latLng.longitude);
        latLong = String.format(Locale.getDefault(),
                "%f,%f", latLng.latitude, latLng.longitude);
        Toast.makeText(this, format, Toast.LENGTH_LONG).show();

    }

}