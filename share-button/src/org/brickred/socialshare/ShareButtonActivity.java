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

import java.util.List;

import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
 * method , get user profile , get contacts and upload image<br>
 * 
 * @author vineet.aggarwal@3pillarglobal.com
 * 
 */

public class ShareButtonActivity extends Activity {

	// SocialAuth Component
	SocialAuthAdapter adapter;
	Profile profileMap;
	public static Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.main);

		// Welcome Message
		TextView textview = (TextView) findViewById(R.id.text);
		textview.setText("Welcome to SocialAuth Demo. We are sharing text SocialAuth Android by share button.");

		// Create Your Own Share Button
		Button imgLoad = (Button) findViewById(R.id.loadImageButton);

		imgLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						ImageGallery.class);
				startActivity(intent);
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

		// supports profile , friends , message status
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		adapter.addProvider(Provider.LINKEDIN, R.drawable.linkedin);
		adapter.addProvider(Provider.MYSPACE, R.drawable.myspace);
		adapter.addProvider(Provider.YAHOO, R.drawable.yahoo);
		adapter.addProvider(Provider.YAMMER, R.drawable.yammer);

		// Supports only profile and contacts
		adapter.addProvider(Provider.FOURSQUARE, R.drawable.foursquare);
		adapter.addProvider(Provider.GOOGLE, R.drawable.google);

		// Supports only profile
		adapter.addProvider(Provider.SALESFORCE, R.drawable.salesforce);
		adapter.addProvider(Provider.RUNKEEPER, R.drawable.runkeeper);

		// Providers require user call Back

		// adapter.addCallBack(Provider.FOURSQUARE,
		// "http://recfit.sayantam.in/");

		// adapter.addCallBack(Provider.GOOGLE,
		// "http://socialauth.in/socialauthdemo");

		// adapter.addCallBack(Provider.SALESFORCE,
		// "https://recfit.sayantam.in:8443/");

		// adapter.addCallBack(Provider.YAMMER,
		// "http://socialauth.in/socialauthdemo");

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
		}

		@Override
		public void onError(SocialAuthError error) {
			Log.d("ShareButton", "Authentication Error: " + error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("ShareButton", "Authentication Cancelled");
		}

	}

}