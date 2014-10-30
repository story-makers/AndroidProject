package com.storymakers.apps.trailguide.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseGeoPoint;
import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.TrailGuideApplication;
import com.storymakers.apps.trailguide.interfaces.LocationAvailableHandler;
import com.storymakers.apps.trailguide.model.RemoteDBClient;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGUtils;

public class CreateDialogFragment extends DialogFragment {
	PostType type;
	EditText etNote;
	ImageView ivPhoto;
	ImageView ivPointInfo;
	Button btnDone;
	Button btnCancel;
	TGPost editPost = null;
	Mode dialogMode;

	public enum Mode {
		ADD, EDIT
	}

	private OnDialogDoneListener doneListener;

	public interface OnDialogDoneListener {
		public void onDone(TGPost post, Mode mode);

		public void onDoneTitle(String title, Mode mode);
	}

	public CreateDialogFragment() {
		// Empty constructor required for DialogFragment
	}

	public static CreateDialogFragment newInstance(int postType,
			String dialogContent) {
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
			dialogMode = Mode.ADD;
			editPost = TGPost.createNewPost(null, type);
		} else {
			dialogMode = Mode.EDIT;
		}
		switch (type) {
		case PHOTO:
			view = inflater.inflate(R.layout.fragment_create_photo, container,
					false);
			if (args.containsKey("content")) {
				String photoUrl = args.getString("content");
				addPhoto(view, photoUrl);
			} else {
				addPhoto(view, null);
			}
			break;
		case NOTE:
			view = inflater.inflate(R.layout.fragment_edit_note, container,
					false);
			editNote(view);
			break;
		case LOCATION:
			view = inflater.inflate(R.layout.fragment_edit_point, container,
					false);
			editLocationPoint(view);
			break;
		default:
			view = inflater.inflate(R.layout.fragment_edit_note, container,
					false);
			editTitle(view);
		}
		return view;

	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnDialogDoneListener) {
			doneListener = (OnDialogDoneListener) activity;
		} else {
			throw new ClassCastException(
					activity.toString()
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
		etNote.setHint(R.string.cutom_hike_title);
		btnDone = (Button) v.findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doneListener.onDoneTitle(etNote.getText().toString(),
						dialogMode);
				CreateDialogFragment.this.dismiss();
			}
		});
		setupCancelButton(v);
	}

	private void editLocationPoint(View v) {
		if (dialogMode == Mode.EDIT) {
			this.getDialog().setTitle(R.string.edit_point);
		} else {
			this.getDialog().setTitle(R.string.add_point);
		}
		ivPointInfo = (ImageView) v.findViewById(R.id.ivPointInfo);

		// ivPointInfo.setBackgroundResource(R.anim.map_wait_blink);
		// AnimationDrawable frameAnimation = (AnimationDrawable) ivPointInfo
		// .getBackground();

		// Start the animation (looped playback by default).
		// frameAnimation.start();

		if (editPost.getLocation() != null
				&& editPost.getLocation().getLatitude() > 0) {
			ParseGeoPoint geoPoint = editPost.getLocation();
			Uri staticMapUri = TrailGuideApplication.getStaticMapObject()
					.getMap((float) geoPoint.getLatitude(),
							(float) geoPoint.getLongitude(), 240, 240, true,
							null);
			ivPointInfo.clearAnimation();
			ImageLoader.getInstance().displayImage(staticMapUri.toString(),
					ivPointInfo);

		} else {
			Animation animation = new AlphaAnimation(1, 0);
			animation.setDuration(1000); // duration - half a second

			animation.setInterpolator(new LinearInterpolator()); // do not alter
																	// animation
																	// rate
			animation.setRepeatCount(Animation.INFINITE); // Repeat animation
															// infinitely
			// animation.setRepeatMode(Animation.REVERSE);
			// ivPointInfo.setText("Fetching location...");
			ivPointInfo.startAnimation(animation);
			TGUtils.getCurrentLocation(new LocationAvailableHandler() {
				@Override
				public void onFail() {
					ivPointInfo.clearAnimation();
				}

				@Override
				public void foundLocation(ParseGeoPoint point) {
					editPost.setLocation(point.getLatitude(),
							point.getLongitude());
					Uri staticMapUri = TrailGuideApplication
							.getStaticMapObject().getMap(
									(float) point.getLatitude(),
									(float) point.getLongitude(), 240, 240,
									true, null);
					ivPointInfo.clearAnimation();
					ImageLoader.getInstance().displayImage(
							staticMapUri.toString(), ivPointInfo);
				}
			});
		}

		// set ivPointInfo after getting response from location services.
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
				doneListener.onDone(editPost, dialogMode);
				CreateDialogFragment.this.dismiss();
			}
		});
		setupCancelButton(v);
	}

	private void editNote(View v) {
		if (dialogMode == Mode.EDIT) {
			this.getDialog().setTitle(R.string.edit_note);
		} else {
			this.getDialog().setTitle(R.string.add_note);
		}
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
				doneListener.onDone(editPost, dialogMode);
				CreateDialogFragment.this.dismiss();
			}
		});
		setupCancelButton(v);
	}

	private void addPhoto(View v, String localPhotoUrl) {
		if (dialogMode == Mode.EDIT) {
			this.getDialog().setTitle(R.string.edit_photo);
		} else {
			this.getDialog().setTitle(R.string.add_photo);
		}
		etNote = (EditText) v.findViewById(R.id.etNote);
		if (editPost.getNote() != null) {
			etNote.setText(editPost.getNote());
		}
		ivPhoto = (ImageView) v.findViewById(R.id.ivPhotoTaken);
		ivPhoto.setImageResource(android.R.color.transparent);
		if (localPhotoUrl == null) { // load from post.
			ImageLoader.getInstance().displayImage(editPost.getPhoto_url(),
					ivPhoto);
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
				doneListener.onDone(editPost, dialogMode); // sets the post on
															// the story.
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
