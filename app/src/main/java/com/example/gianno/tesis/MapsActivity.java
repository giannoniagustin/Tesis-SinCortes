package com.example.gianno.tesis;


import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static Context context;
    private GoogleMap mMap; // Debería ser null si Google Play services APK no está disponible.
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private HashMap<String,Object> parametros;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapsActivity.context = getApplicationContext();
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 segundos, en milisegundos
                .setFastestInterval(1 * 1000); // 1 segundo, en milisegundos


        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    //¿Por qué onResume () y no onStart ()? Ver ciclo de vida del Activity
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void setUpMapIfNeeded() {
        // Chequeo de null para confirmar que no hemos instanciado el mapa todavía.
        if (mMap == null) {
            // Trata de obtener el mapa de SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Chequea si tuvimos éxito en obtener el mapa.
            if (mMap != null) {

                setUpMap();
            }
        }
    }


    private void setUpMap() {



    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
        parametros=new HashMap<String, Object>();
        parametros.put("mMap", mMap);
        ArrayList<NameValuePair>  parametrosPost= new ArrayList<NameValuePair>();
        parametrosPost.add(new BasicNameValuePair("Latitud", Double.toString(latLng.latitude)));
        parametrosPost.add(new BasicNameValuePair("Longitud", Double.toString(latLng.longitude)));
        parametros.put("ParametrosPost", parametrosPost);
        ObtenerCortesBD cortesBd =new ObtenerCortesBD();
        cortesBd.execute(parametros, null, null);
        ObtenerRutaGoogle camino =new ObtenerRutaGoogle();
        camino.execute(parametros, null, null);
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

         latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("Ud. esta aquí!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mMap.addMarker(options);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Inicia una actividad que trata de resolver el error.
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location Services Connection falló con el código: " + connectionResult.getErrorCode());
        }
    }

    @Override
    //Este nuevo método se llama cada vez que una nueva ubicación es detectada por servicios de Google Play
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public static Context getAppContext() {
        return context;
    }
}
