package com.storymakers.apps.trailguide.fragments;

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
import android.widget.Toast;

import com.storymakers.apps.trailguide.R;
import com.storymakers.apps.trailguide.model.TGPost;
import com.storymakers.apps.trailguide.model.TGPost.PostType;
import com.storymakers.apps.trailguide.model.TGStory;

public class CreateDialogFragment extends DialogFragment {
	PostType type;
	EditText etNote;
	ImageView ivPhoto;
	TextView tvPointInfo;
	Button btnDone;
	Button btnCancel;
	TGStory draftStory;
	TGPost editPost;

	private OnDialogDoneListener doneListener;

	public interface OnDialogDoneListener {
		public void onDone(TGPost post);
	}
	
	public CreateDialogFragment() {
		// Empty constructor required for DialogFragment
	}
	
	public static CreateDialogFragment newInstance(int postType, TGStory draft) {
		CreateDialogFragment frag = new CreateDialogFragment();
		Bundle args = new Bundle();
		args.putInt("post_type", postType);
		//args.putParcelable("draft_story", draft);
		frag.setArguments(args);
		return frag;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
		type = PostType.values()[getArguments().getInt("post_type")];
		// get draft story from bundle args.

		View view;
		switch(type) {
		case PHOTO:
			view = inflater.inflate(R.layout.fragment_create_photo, container, false);
			addPhoto(view);
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
			/*getDialog().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);*/
		}
		return view;
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    switch(type) {
	    case PHOTO:
			// call the appropriate handler from activity to save the post
			break;
		case NOTE:
			// call the appropriate handler from activity to save the post
			break;
		case LOCATION:
			// call the appropriate handler from activity to save the post
			break;
		default:
			// call the appropriate handler from activity to save the post
	    }
	    super.onSaveInstanceState(outState);
	}

	private void editTitle(View v) {
		this.getDialog().setTitle(R.string.hike_title);
		etNote = (EditText) v.findViewById(R.id.etNote);
		if (draftStory != null) {
			etNote.setText(draftStory.getTitle());
		}
		btnDone = (Button) v.findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (draftStory == null) {
					// draft should never be null.
					Toast.makeText(getActivity(), "Draft story should not be null", Toast.LENGTH_SHORT).show();
				}
				//draftStory.setTitle(etNote.getText().toString());
				CreateDialogFragment.this.dismiss();
			}
		});
		setupCancelButton(v);
	}

	private void editLocationPoint(View v) {
		this.getDialog().setTitle(R.string.capture_point);
		tvPointInfo = (TextView) v.findViewById(R.id.tvPointInfo);
		// set tvPointInfo after getting response from location services.
		etNote = (EditText) v.findViewById(R.id.etNote);
		if (editPost != null) { // populated when someone clicks a post to edit it.
			etNote.setText(editPost.getNote());
		}
		btnDone = (Button) v.findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// create or edit the editPost.
				CreateDialogFragment.this.dismiss();
			}
		});
		setupCancelButton(v);
	}

	private void editNote(View v) {
		this.getDialog().setTitle(R.string.edit_note);
		etNote = (EditText) v.findViewById(R.id.etNote);
		if (editPost != null) { // populated when someone clicks a post to edit it.
			etNote.setText(editPost.getNote());
		}
		btnDone = (Button) v.findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// create or edit the editPost.
				// editPost.setNote(etNote.getText().toString());
				CreateDialogFragment.this.dismiss();
			}
		});
		setupCancelButton(v);
	}

	private void addPhoto(View v) {
		this.getDialog().setTitle(R.string.add_photo);
		etNote = (EditText) v.findViewById(R.id.etNote);
		ivPhoto = (ImageView) v.findViewById(R.id.ivPhotoTaken);
		ivPhoto.setImageResource(android.R.color.transparent);
		// never allow someone to edit a photo post
		btnDone = (Button) v.findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// create editPost of type photo.
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
