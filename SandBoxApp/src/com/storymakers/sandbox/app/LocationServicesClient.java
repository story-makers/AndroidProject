package com.storymakers.sandbox.app;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class LocationServicesClient implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private OnConnectListener onConnectListener;
	private LocationClient mLocationClient;
	private FragmentActivity contextActivity;
	private int playServiceRequestCode;

	public interface OnConnectListener {
		public void onConnect();
	}

	public static LocationServicesClient getInstance(Context ctx, int serviceCode) {
		return new LocationServicesClient(ctx, serviceCode);
	}

	private LocationServicesClient(Context ctx, int requestCode) {
		contextActivity = (FragmentActivity) ctx;
		playServiceRequestCode = requestCode;
		onConnectListener = (OnConnectListener) contextActivity;
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(ctx, this, this);
		}
	}

	/*private void kickStartLocationUpdates() {
		contextActivity.getApplicationContext().getLocationManager().requestLocationUpdates(
			    LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
			        @Override
			        public void onStatusChanged(String provider, int status, Bundle extras) {
			        }
			        @Override
			        public void onProviderEnabled(String provider) {
			        }
			        @Override
			        public void onProviderDisabled(String provider) {
			        }
			        @Override
			        public void onLocationChanged(final Location location) {
			        }
			    });
	}*/

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(contextActivity, playServiceRequestCode);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			Toast.makeText(contextActivity.getApplicationContext(),
					"Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
		}		
	}

	@Override
	public void onConnected(Bundle arg0) {
		onConnectListener.onConnect();
	}

	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(contextActivity, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	}

	public void connect() {
		if (isGooglePlayServicesAvailable()) {
			mLocationClient.connect();
		}
	}

	public void disconnect() {
		mLocationClient.disconnect();
	}

	public Location getLastLocation() {
		Location loc = null;
		if (mLocationClient.isConnected()) {
			loc = mLocationClient.getLastLocation();
			if (loc == null) {
				Toast.makeText(contextActivity, "GPS might be off!", Toast.LENGTH_SHORT).show();
			}
		}
		return loc;
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {

		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	private boolean isGooglePlayServicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(contextActivity);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			return true;
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, contextActivity,
					playServiceRequestCode);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(contextActivity.getSupportFragmentManager(), "Location Updates");
			}

			return false;
		}
	}
}
