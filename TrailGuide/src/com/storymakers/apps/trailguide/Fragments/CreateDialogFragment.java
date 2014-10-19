package com.storymakers.apps.trailguide.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseGeoPoint;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.interfaces.LoactionAvailableHandler;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGUtils;

public class CreateDialogFragment extends DialogFragment {
	PostType type;
	EditText etNote;
	ImageView ivPhoto;
	TextView tvPointInfo;
	Button btnDone;
	Button btnCancel;
	TGPost editPost = null;

	private OnDialogDoneListener doneListener;

	public interface OnDialogDoneListener {
		public void onDone(TGPost post);
		public void onDoneTitle(String title);
	}
	
	public CreateDialogFragment() {
		// Empty constructor required for DialogFragment
	}
	
	public static CreateDialogFragment newInstance(int postType, String dialogContent) {
		CreateDialogFragment frag = new CreateDialogFragment();
		Bundle args = new Bundle();
		args.putInt("post_type", postType);
		args.putString("content", dialogContent);
		frag.setArguments(args);
		return frag;
	}

	public static CreateDialogFragment newInstance(TGPost editPost) {
		CreateDialogFragment frag = new CreateDialogFragment();
		Bundle args = new Bundle();
		args.putInt("post_type", editPost.getType().getNumVal());
		args.putString("post_id", editPost.getObjectId());
		frag.setArguments(args);
		return frag;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
		Bundle args = getArguments();
		type = PostType.values()[args.getInt("post_type")];
		if (args.containsKey("post_id") && args.getString("post_id") != null) {
			fetchPost(args.getString("post_id"));
		}
		View view;
		if (editPost == null && type != PostType.METADATA) {
			editPost = TGPost.createNewPost(null, type);
		}
		switch(type) {
		case PHOTO:
			view = inflater.inflate(R.layout.fragment_create_photo, container, false);
			if (args.containsKey("content")) {
				String photoUrl = args.getString("content");
				addPhoto(view, photoUrl);
			} else {
				addPhoto(view, null);
			}
			break;
		case NOTE:
			view = inflater.inflate(R.layout.fragment_edit_note, container, false);
			editNote(view);
			break;
		case LOCATION:
			view = inflater.inflate(R.layout.fragment_edit_point, container, false);
			editLocationPoint(view);
			break;
		default:
			view = inflater.inflate(R.layout.fragment_edit_note, container, false);
			editTitle(view);
		}
		return view;
		
    }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnDialogDoneListener) {
			doneListener = (OnDialogDoneListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement CreateDialogFragment.OnDoneDialogListener");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		super.onSaveInstanceState(arg0);
	}

	private void fetchPost(String postId) {
		editPost = RemoteDBClient.getPostById(postId);
	}

	private void editTitle(View v) {
		this.getDialog().setTitle(R.string.hike_title);
		etNote = (EditText) v.findViewById(R.id.etNote);
		btnDone = (Button) v.findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doneListener.onDoneTitle(etNote.getText().toString());
				CreateDialogFragment.this.dismiss();
			}
		});
		setupCancelButton(v);
	}

	private void editLocationPoint(View v) {
		this.getDialog().setTitle(R.string.capture_point);
		tvPointInfo = (TextView) v.findViewById(R.id.tvPointInfo);
		if (editPost.getLocation() != null && editPost.getLocation().getLatitude() > 0) {
			tvPointInfo.setText(editPost.getLocation().getLatitude() + ", " + editPost.getLocation().getLongitude());
		} else {
			tvPointInfo.setText("Fetching location...");
			TGUtils.getCurrentLocation(new LoactionAvailableHandler() {
				@Override
				public void onFail() {
				}
	
				@Override
				public void foundLocation(ParseGeoPoint point) {
					editPost.setLocation(point.getLatitude(), point.getLongitude());
					tvPointInfo.setText(point.getLatitude() + ", " + point.getLongitude());
				}
			});
		}

		// set tvPointInfo after getting response from location services.
		etNote = (EditText) v.findViewById(R.id.etNote);
		if (editPost.getNote() != null && editPost.getNote().length() > 0) {
			// populated when someone clicks a post to edit it.
			etNote.setText(editPost.getNote());
		}
		btnDone = (Button) v.findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editPost.setNote(etNote.getText().toString());
				doneListener.onDone(editPost);
				CreateDialogFragment.this.dismiss();
			}
		});
		setupCancelButton(v);
	}

	private void editNote(View v) {
		this.getDialog().setTitle(R.string.edit_note);
		etNote = (EditText) v.findViewById(R.id.etNote);
		if (editPost.getNote() != null) {
			// populated when someone clicks a post to edit it.
			etNote.setText(editPost.getNote());
		}
		btnDone = (Button) v.findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				editPost.setNote(etNote.getText().toString());
				doneListener.onDone(editPost);
				CreateDialogFragment.this.dismiss();
			}
		});
		setupCancelButton(v);
	}

	private void addPhoto(View v, String localPhotoUrl) {
		this.getDialog().setTitle(R.string.add_photo);
		etNote = (EditText) v.findViewById(R.id.etNote);
		if (editPost.getNote() != null) {
			etNote.setText(editPost.getNote());
		}
		ivPhoto = (ImageView) v.findViewById(R.id.ivPhotoTaken);
		ivPhoto.setImageResource(android.R.color.transparent);
		if (localPhotoUrl == null) { // load from post.
			ImageLoader.getInstance().displayImage(editPost.getPhoto_url(), ivPhoto);
		} else {
			Uri takenPhotoUri = Uri.parse(localPhotoUrl);
			editPost.setPhotoFromUri(getActivity(), takenPhotoUri);
			ivPhoto.setImageBitmap(TGUtils.getBitmapForLocalUri(takenPhotoUri));
		}
		btnDone = (Button) v.findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// create editPost of type photo.
				editPost.setNote(etNote.getText().toString());
				doneListener.onDone(editPost); // sets the post on the story.
				CreateDialogFragment.this.dismiss();
			}
		});
		setupCancelButton(v);
	}

	private void setupCancelButton(View v) {
		Button btnCancel = (Button) v.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreateDialogFragment.this.dismiss();
			}
		});
	}
}
