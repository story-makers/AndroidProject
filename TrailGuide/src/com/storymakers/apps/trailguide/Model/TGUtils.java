package com.storymakers.apps.trailguide.model;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import com.parse.LocationCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.storymakers.apps.trailguide.interfaces.LoactionAvailableHandler;

public class TGUtils {

	public static byte[] getBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		return byteBuffer.toByteArray();
	}

	public static byte[] getBytesFromUri(Context ctx, Uri photo) {
		InputStream iStream;
		byte[] inputData = null;
		try {
			iStream = ctx.getContentResolver().openInputStream(photo);
			try {
				inputData = getBytes(iStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return inputData;
		/*
		 * if (inputData != null) { ParseFile pphoto = new ParseFile(name,
		 * inputData); TGPost p = TGPost.createNewPost(story,
		 * TGPost.PostType.PHOTO); p.setPhoto(pphoto);
		 * p.setLocation(getGeoLocationFromPhoto(photo.getEncodedPath()));
		 * p.saveData(); Toast.makeText(this, "Image Uploaded",
		 * Toast.LENGTH_SHORT).show(); }
		 */
	}

	public static float[] getGeoLocationFromPhoto(String photo_path) {
		ExifInterface exif;
		float latlong[] = { (float) 0.0, (float) 0.0 };
		try {
			exif = new ExifInterface(photo_path);
			exif.getLatLong(latlong);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return latlong;
	}

	public static void getCurrentLocation(final LoactionAvailableHandler handle) {
		ParseGeoPoint.getCurrentLocationInBackground(10000,
				new LocationCallback() {

					@Override
					public void done(ParseGeoPoint geoPoint, ParseException e) {
						if (geoPoint != null) {
							handle.foundLocation(geoPoint);
						} else {
							handle.onFail();
						}

					}
				});

	}

	public static String getUserEmailOnDevice(Context ctx) {
		AccountManager manager = AccountManager.get(ctx);
		Account[] accounts = manager.getAccountsByType("com.google");
		List<String> possibleEmails = new LinkedList<String>();

		for (Account account : accounts) {
			// TODO: Check possibleEmail against an email regex or treat
			// account.name as an email address only for certain account.type
			// values.
			possibleEmails.add(account.name);
		}

		if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
			String email = possibleEmails.get(0);
			String[] parts = email.split("@");
			if (parts.length > 0 && parts[0] != null)
				return email;
			else
				return null;
		} else
			return null;
	}

	public static String getCompleteAddressString(Context context,
			double LATITUDE, double LONGITUDE) {
		String strAdd = "";
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(LATITUDE,
					LONGITUDE, 1);
			if (addresses != null) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");

				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress
							.append(returnedAddress.getAddressLine(i)).append(
									"\n");
				}
				strAdd = strReturnedAddress.toString();
				Log.w("My Current loction address",
						"" + strReturnedAddress.toString());
			} else {
				Log.w("My Current loction address", "No Address returned!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("My Current loction address", "Canont get Address!");
		}
		return strAdd;
	}
}
