package com.example.beatriz.listatcc.Location;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.GoogleApiRequest;
import com.example.beatriz.listatcc.Model.GoogleAPIObjectResponse;
import com.example.beatriz.listatcc.Model.Mercado;
import com.example.beatriz.listatcc.Model.Result;
import com.example.beatriz.listatcc.RetrofitHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CurrentLocationPoint extends Fragment implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener,
        RetrofitHelper.RetrofitHelperCallback {
    private static final int REQUEST_FINE_LOCATION = 0;
    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public static double mCurrentLatitude;
    public static double mCurrentLongitude;
    public static boolean mIsfirstRunning = true;
    public static boolean mUpdateLocation = false;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * If connected get lat and long
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            mCurrentLatitude = location.getLatitude();
            mCurrentLongitude = location.getLongitude();

            if (mIsfirstRunning) {
                mIsfirstRunning = false;
                updateCurrentLocation();
            }
            Log.i("LOCATION: ", mCurrentLatitude + " : " + mCurrentLongitude);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLatitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();

        Log.i("CURRENT_LOCATION: ", String.valueOf(mCurrentLatitude));
        Toast.makeText(mContext, mCurrentLatitude + " WORKS " + mCurrentLongitude + "", Toast.LENGTH_LONG).show();
        updateCurrentLocation();
    }

    private void updateCurrentLocation() {
        mUpdateLocation = true;

        RetrofitHelper retrofitHelper = new RetrofitHelper(mContext, this, GoogleApiRequest.class);
        Call<GoogleAPIObjectResponse> mapCall = ((GoogleApiRequest) retrofitHelper.getT()).getSupermarket(mCurrentLatitude + "," + mCurrentLongitude, String.valueOf(5000), "prominence", "grocery_or_supermarket", GoogleApiRequest.GOOGLE_API_KEY);
        retrofitHelper.execute(mapCall);
    }

    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(mContext, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), perm)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{perm}, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                } else {
                    // no granted
                }
                return;
            }
        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        //Delete o conteúdo da tabela SupermarketCurrentNearby e insere tudo novamente
        Log.i("TAG: ", "GRAVA NO BANCO!");
        if (mUpdateLocation) {
            GoogleAPIObjectResponse googleAPIObjectResponse = (GoogleAPIObjectResponse) response.body();
            List<Result> currentSupermarkets = googleAPIObjectResponse.getResults();
            ArrayList<Mercado> mercadoList = new ArrayList();

            for (int i = 0; i < currentSupermarkets.size(); i++) {
                Result result = currentSupermarkets.get(i);
                Mercado mercado = new Mercado(result.getId(), result.getName(), result.getVicinity(), result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
                mercadoList.add(mercado);
            }

            DatabaseHandler db = new DatabaseHandler(mContext);
            db.updateSupermarketCurrentNearby(mercadoList);
            mUpdateLocation = false;
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {
    }
}