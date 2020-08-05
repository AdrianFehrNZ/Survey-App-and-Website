/**
 * This page shows two editable text fields for entering a
 * new participant and the surveyor responsible for them. There is
 * also a button for submitting the contents of these textfields.
 */

package dms.restfuljavaandroidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.List;

public class AddParticipantPage extends AppCompatActivity implements LocationListener, View.OnClickListener {
    Geocoder geocoder;
    ToggleButton toggleButton;
    EditText editName, editSurveyor;
    Button addButton;
    String location;
    private TextView locationTextView;
    // value to identify permission request if handled in
    // onRequestPermissionsResult
    public static final int PERMISSION_REQUEST_CODE = 1;
    private boolean wantLocationUpdates;
    private static final String UPDATES_BUNDLE_KEY
            = "WantsLocationUpdates";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_participant_page_fragment);

        geocoder = new Geocoder(this);

        location = "None";
        toggleButton = (ToggleButton) findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(this);

        editName = (EditText) findViewById(R.id.name_edit);
        editSurveyor = (EditText) findViewById(R.id.surveyor_edit);
        addButton = (Button) findViewById(R.id.add_button);
        locationTextView = (TextView) findViewById(R.id.location_view);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(UPDATES_BUNDLE_KEY))
            wantLocationUpdates
                    = savedInstanceState.getBoolean(UPDATES_BUNDLE_KEY);
        else // activity is not being reinitialized from prior start
            wantLocationUpdates = false;
        if (!hasLocationPermission()) {
            toggleButton.setChecked(false);
            locationTextView.setText(R.string.permissions_denied);
        } else {
            toggleButton.setChecked(true);
        }

        //add listener to the button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();
                String surveyor = editSurveyor.getText().toString();

                //send the contents of the textfields to RestfulPostTask and call its doInBackground method
                RestfulPostTask postTask = new RestfulPostTask(name, surveyor, location);
                postTask.execute();

                //return to the main page
                startActivity(new Intent(AddParticipantPage.this, MainActivity.class));
            }
        });
    }

    private boolean hasLocationPermission() {
        int permissionCheck = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.request_permission_title)
                        .setMessage(R.string.request_permission_text)
                        .setPositiveButton(
                                R.string.request_permission_positive,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick
                                            (DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions
                                                (AddParticipantPage.this, new String[]
                                                                {Manifest.permission.
                                                                        ACCESS_FINE_LOCATION},
                                                        PERMISSION_REQUEST_CODE);
                                    }
                                })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }
            return false;
        }
    }

    private void startGPS() {
        LocationManager locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);
        try {
            String provider = LocationManager.GPS_PROVIDER;
            locationManager.requestLocationUpdates(provider, 0, 0, this);
            Location lastKnownLocation
                    = locationManager.getLastKnownLocation(provider);
            if (lastKnownLocation != null)
                location = cityFromLocation(lastKnownLocation);
            locationTextView.setText(cityFromLocation(lastKnownLocation));
            toggleButton.setChecked(true);
        } catch (SecurityException e) {
            locationTextView.setText(R.string.permissions_denied);
            Log.w(AddParticipantPage.class.getName(),
                    "Security Exception: " + e);
        }
    }

    private void stopGPS() {
        LocationManager locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
        toggleButton.setChecked(false);
        locationTextView.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        int permissionCheck = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (wantLocationUpdates
                && permissionCheck == PackageManager.PERMISSION_GRANTED)
            startGPS();
    }

    @Override
    public void onPause() {
        super.onPause();
        // stop location updates while the activity is paused
        int permissionCheck = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            stopGPS();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = cityFromLocation(location);
        locationTextView.setText(cityFromLocation(location));
        Log.i(AddParticipantPage.class.getName(), "Location: " + location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        locationTextView.setText(R.string.provider_status_changed);
    }

    @Override
    public void onProviderEnabled(String s) {
        locationTextView.setText(R.string.provider_enabled);
    }

    @Override
    public void onProviderDisabled(String s) {
        locationTextView.setText(R.string.provider_disabled);
        this.toggleButton.setChecked(false);
    }

    @Override
    public void onClick(View view) {
        if (view == toggleButton) {
            if (wantLocationUpdates) {
                wantLocationUpdates = false;
                locationTextView.setText("");
                stopGPS();
            } else {
                wantLocationUpdates = true;
                startGPS();
            }
        }
    }

    public String cityFromLocation(Location location) {
        String locality = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (geocoder.isPresent()) {

                if (addresses.size() > 0) {
                    Address returnAddress = addresses.get(0);
                    locality = returnAddress.getLocality();

                }
            } else {

            }
        } catch (IOException e) {
            Log.i(AddParticipantPage.class.getName(), e.getMessage());
        }

        return locality;

    }
}
