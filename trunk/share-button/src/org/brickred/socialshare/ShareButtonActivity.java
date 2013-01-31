/*
 ===========================================================================
 Copyright (c) 2012 Three Pillar Global Inc. http://threepillarglobal.com

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sub-license, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ===========================================================================
 */

package org.brickred.socialshare;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.brickred.socialauth.Album;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Feed;
import org.brickred.socialauth.Photo;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Main class of the ShareButton Example for SocialAuth Android SDK. <br>
 * 
 * The main objective of this example is to access social media providers
 * Facebook, Twitter and others by clicking a single button "Share".On Clicking
 * the button the api will open dialog of providers. User can access the
 * provider from dialog and can update the status, get profile , get contacts
 * and upload images.
 * 
 * The class first creates a button in main.xml. It then adds button to
 * SocialAuth Android Library <br>
 * 
 * Then it adds providers Facebook, Twitter and others to library object by
 * addProvider method and finally enables the providers by calling enable method<br>
 * 
 * After successful authentication of provider, it receives the response in
 * responseListener and then automatically update status by updatestatus()
 * method , get user profile , get contacts , upload image , get feeds and get
 * albums <br>
 * 
 * @author vineet.aggarwal@3pillarglobal.com
 * 
 */

public class ShareButtonActivity extends Activity {

	// SocialAuth Component
	SocialAuthAdapter adapter;
	Profile profileMap;
	List<Photo> photosList;
	public static Bitmap bitmap;
	private static final int SELECT_PHOTO = 100;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Welcome Message
		TextView textview = (TextView) findViewById(R.id.text);
		textview.setText("Welcome to SocialAuth Demo. We are sharing text SocialAuth Android by share button.");

		// Create Your Own Share Button
		Button imgLoad = (Button) findViewById(R.id.loadImageButton);

		imgLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Taking image from phone gallery
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, SELECT_PHOTO);
			}
		});

		// Create Your Own Share Button
		Button share = (Button) findViewById(R.id.sharebutton);
		share.setText("Share");
		share.setTextColor(Color.WHITE);
		share.setBackgroundResource(R.drawable.button_gradient);

		// Add it to Library
		adapter = new SocialAuthAdapter(new ResponseListener());

		// Add providers

		// supports profile , friends , message status , upload image , get
		// albums , get feeds
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);

		// supports profile , friends , message status , get feeds
		adapter.addProvider(Provider.LINKEDIN, R.drawable.linkedin);

		// supports profile , friends , message status
		adapter.addProvider(Provider.MYSPACE, R.drawable.myspace);
		adapter.addProvider(Provider.YAHOO, R.drawable.yahoo);
		adapter.addProvider(Provider.YAMMER, R.drawable.yammer);

		// Supports only profile and contacts
		adapter.addProvider(Provider.FOURSQUARE, R.drawable.foursquare);
		adapter.addProvider(Provider.GOOGLE, R.drawable.google);

		// Supports only profile
		adapter.addProvider(Provider.SALESFORCE, R.drawable.salesforce);
		adapter.addProvider(Provider.RUNKEEPER, R.drawable.runkeeper);

		// Providers require setting user call Back url
		adapter.addCallBack(Provider.FOURSQUARE,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		adapter.addCallBack(Provider.GOOGLE,
				"http://socialauth.in/socialauthdemo");

		adapter.addCallBack(Provider.SALESFORCE,
				"https://socialauth.in:8443/socialauthdemo/socialAuthSuccessAction.do");

		adapter.addCallBack(Provider.YAMMER,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		// Enable Provider
		adapter.enable(share);

	}

	/**
	 * Listens Response from Library
	 * 
	 */

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			Log.d("ShareButton", "Authentication Successful");

			// Get name of provider after authentication
			String providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("ShareButton", "Provider Name = " + providerName);

			// Sharing Updates
			adapter.updateStatus("SocialAuth Android"
					+ System.currentTimeMillis());
			Toast.makeText(ShareButtonActivity.this,
					"View Logcat for Message Information", Toast.LENGTH_SHORT)
					.show();

			// Get User Profile
			try {
				profileMap = adapter.getUserProfile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.d("Share-Button",
					"Validate ID = " + profileMap.getValidatedId());
			Log.d("Share-Button", "First Name  = " + profileMap.getFirstName());
			Log.d("Share-Button", "Last Name   = " + profileMap.getLastName());
			Log.d("Share-Button",
					"Display Name   = " + profileMap.getDisplayName());
			Log.d("Share-Button", "Email       = " + profileMap.getEmail());
			Log.d("Share-Button", "Country  	 = " + profileMap.getCountry());
			Log.d("Share-Button", "Language  	 = " + profileMap.getLanguage());
			Log.d("Share-Button",
					"Profile Image URL  = " + profileMap.getProfileImageURL());

			Toast.makeText(ShareButtonActivity.this,
					"View Logcat for Profile Information", Toast.LENGTH_SHORT)
					.show();

			// Get List of contacts
			List<Contact> contactsList = adapter.getContactList();

			if (contactsList != null && contactsList.size() > 0) {
				for (Contact p : contactsList) {

					if (TextUtils.isEmpty(p.getFirstName())
							&& TextUtils.isEmpty(p.getLastName())) {
						p.setFirstName(p.getDisplayName());
					}

					Log.d("Share-Button",
							"Display Name = " + p.getDisplayName());
					Log.d("Share-Button", "First Name = " + p.getFirstName());
					Log.d("Share-Button", "Last Name = " + p.getLastName());
					Log.d("Share-Button", "Contact ID = " + p.getId());
					Log.d("Share-Button", "Profile URL = " + p.getProfileUrl());
				}
			}

			Toast.makeText(ShareButtonActivity.this,
					"View Logcat for Contacts Information", Toast.LENGTH_SHORT)
					.show();

			// Upload Photo

			if (bitmap != null) {
				int imgStatus = adapter.uploadImage("HelloWorldTest",
						"icon.png", bitmap, 0);
				Log.d("ShareButton", String.valueOf(imgStatus));

				Toast.makeText(ShareButtonActivity.this,
						"View Logcat for Image Upload Information",
						Toast.LENGTH_SHORT).show();
			}

			// Get Feeds : For Facebook , Twitter and Linkedin Only
			List<Feed> feedList = adapter.getFeeds();
			if (feedList != null && feedList.size() > 0) {
				for (Feed f : feedList) {

					Log.d("Share-Button", "Feed ID = " + f.getId());
					Log.d("Share-Button", "Screen Name = " + f.getScreenName());
					Log.d("Share-Button", "Message = " + f.getMessage());
					Log.d("Share-Button", "Get From = " + f.getFrom());
					Log.d("Share-Button", "Created at = " + f.getCreatedAt());
				}
			}

			// Get Albums and Photos : For FaceBook and Twitter Only
			List<Album> albumList = adapter.getAlbums();

			if (albumList != null && albumList.size() > 0) {

				// Get Photos inside Album
				for (Album a : albumList) {

					Log.d("Share-Button", "Album ID = " + a.getId());
					Log.d("Share-Button", "Album Name = " + a.getName());
					Log.d("Share-Button", "Cover Photo = " + a.getCoverPhoto());
					Log.d("Share-Button",
							"Photos Count = " + a.getPhotosCount());

					photosList = a.getPhotos();

					if (photosList != null && photosList.size() > 0) {

						for (Photo p : photosList) {
							Log.d("Share-Button", "Photo ID = " + p.getId());
							Log.d("Share-Button", "Name     = " + p.getTitle());
							Log.d("Share-Button",
									"Thumb Image = " + p.getThumbImage());
							Log.d("Share-Button",
									"Small Image = " + p.getSmallImage());
							Log.d("Share-Button",
									"Medium Image = " + p.getMediumImage());
							Log.d("Share-Button",
									"Large Image = " + p.getLargeImage());
						}
					}
				}
			}
		}

		@Override
		public void onError(SocialAuthError error) {
			Log.d("ShareButton", "Authentication Error: " + error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("ShareButton", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			Log.d("Share-Button", "Dialog Closed by pressing Back Key");
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				InputStream imageStream;
				try {
					imageStream = getContentResolver().openInputStream(
							selectedImage);
					bitmap = BitmapFactory.decodeStream(imageStream);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
	}

}