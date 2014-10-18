package com.storymakers.apps.trailguide.model;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;

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
	
	public static void getCurrentLocation(final LoactionAvailableHandler handle){
		ParseGeoPoint.getCurrentLocationInBackground(10000, new LocationCallback() {
			
			@Override
			public void done(ParseGeoPoint geoPoint, ParseException e) {
				if (geoPoint != null){
					handle.foundLocation(geoPoint);
				}else {
					handle.onFail();
				}
				
			}
		});
		
	}
	
}
