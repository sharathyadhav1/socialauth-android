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

package org.brickred.socialbar;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Main class of the ShareBar Example for SocialAuth Android SDK. <br>
 * 
 * The main objective of this example is to create a bar of social media
 * providers Facebook, Twitter and others. It enables user to access the
 * respective provider on single click and update the status.
 * 
 * The class first creates a bar in main.xml. It then adds bar to SocialAuth
 * Android Library <br>
 * 
 * Then it adds providers Facebook, Twitter and others to library object by
 * addProvider method and finally enables the providers by calling enable method<br>
 * 
 * After successful authentication of provider, it receives the response in
 * responseListener and then automatically update status by updatestatus()
 * method , get user profile , get contact details and upload image<br>
 * 
 * This example also shows implementation how to get feeds and albums from
 * providers.
 * 
 * @author vineet.aggarwal@3pillarglobal.com
 * 
 */

public class ShareBarActivity extends Activity {

	// SocialAuth Component
	SocialAuthAdapter adapter;
	boolean status;
	Profile profileMap;
	List<Photo> photosList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Welcome Message
		TextView textview = (TextView) findViewById(R.id.text);
		textview.setText("Welcome to SocialAuth Demo. We are sharing text SocialAuth Android by Share Bar.");

		LinearLayout bar = (LinearLayout) findViewById(R.id.linearbar);
		bar.setBackgroundResource(R.drawable.bar_gradient);

		// Add Bar to library
		adapter = new SocialAuthAdapter(new ResponseListener());

		// Add providers
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		adapter.addProvider(Provider.LINKEDIN, R.drawable.linkedin);
		adapter.addProvider(Provider.MYSPACE, R.drawable.myspace);

		// adapter.addProvider(Provider.YAHOO, R.drawable.yahoo);
		// adapter.addProvider(Provider.YAMMER, R.drawable.yammer);
		// adapter.addProvider(Provider.FOURSQUARE, R.drawable.foursquare);
		// adapter.addProvider(Provider.GOOGLE, R.drawable.google);
		// adapter.addProvider(Provider.SALESFORCE, R.drawable.salesforce);
		// adapter.addProvider(Provider.RUNKEEPER, R.drawable.runkeeper);

		// Use addCallback method from share-button example if using
		// your own keys for FOURSQUARE , GOOGLE , SALESFORCE , YAMMER

		adapter.enable(bar);

	}

	/**
	 * Listens Response from Library
	 * 
	 */

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			// Variable to receive message status

			Log.d("Share-Bar", "Authentication Successful");

			// Get name of provider after authentication
			String providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("Share-Bar", "Provider Name = " + providerName);

			// Please avoid sending duplicate message. Social Media Providers
			// block duplicate messages.
			adapter.updateStatus("SocialAuth Android"
					+ System.currentTimeMillis());

			Toast.makeText(ShareBarActivity.this,
					"View Logcat for Message Information", Toast.LENGTH_SHORT)
					.show();

			// Get User Profile
			try {
				profileMap = adapter.getUserProfile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.d("Share-Bar", "Validate ID = " + profileMap.getValidatedId());
			Log.d("Share-Bar", "First Name  = " + profileMap.getFirstName());
			Log.d("Share-Bar", "Last Name   = " + profileMap.getLastName());
			Log.d("Share-Bar",
					"Display Name   = " + profileMap.getDisplayName());
			Log.d("Share-Bar", "Email       = " + profileMap.getEmail());
			Log.d("Share-Bar", "Country  	 = " + profileMap.getCountry());
			Log.d("Share-Bar", "Language  	 = " + profileMap.getLanguage());
			Log.d("Share-Bar",
					"Profile Image URL  = " + profileMap.getProfileImageURL());

			Toast.makeText(ShareBarActivity.this,
					"View Logcat for Profile Information", Toast.LENGTH_SHORT)
					.show();

			// Get Feeds : For Facebook , Twitter Only
			List<Feed> feedList = adapter.getFeeds();
			if (feedList != null && feedList.size() > 0) {
				for (Feed f : feedList) {

					Log.d("Share-Bar", "Feed ID = " + f.getId());
					Log.d("Share-Bar", "Screen Name = " + f.getScreenName());
					Log.d("Share-Bar", "Message = " + f.getMessage());
					Log.d("Share-Bar", "Get From = " + f.getFrom());
					Log.d("Share-Bar", "Created at = " + f.getCreatedAt());
				}
			}

			// Get List of contacts
			List<Contact> contactsList = adapter.getContactList();

			if (contactsList != null && contactsList.size() > 0) {
				for (Contact p : contactsList) {

					if (TextUtils.isEmpty(p.getFirstName())
							&& TextUtils.isEmpty(p.getLastName())) {
						p.setFirstName(p.getDisplayName());
					}

					Log.d("Share-Bar", "Display Name = " + p.getDisplayName());
					Log.d("Share-Bar", "First Name = " + p.getFirstName());
					Log.d("Share-Bar", "Last Name = " + p.getLastName());
					Log.d("Share-Bar", "Contact ID = " + p.getId());
					Log.d("Share-Bar", "Profile URL = " + p.getProfileUrl());
				}
			}

			Toast.makeText(ShareBarActivity.this,
					"View Logcat for Contacts Information", Toast.LENGTH_SHORT)
					.show();

			// Upload Photo : For Facebook and Twitter Only
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon);

			int imgStatus = adapter.uploadImage("HelloWorldTest", "icon.png",
					bmp, 0);
			Log.d("Share-Bar", String.valueOf(imgStatus));

			Toast.makeText(ShareBarActivity.this,
					"View Logcat for Image Upload Information",
					Toast.LENGTH_SHORT).show();

			// Get Albums and Photos : For FaceBook and Twitter Only
			List<Album> albumList = adapter.getAlbums();

			if (albumList != null && albumList.size() > 0) {

				// Get Photos inside Album
				for (Album a : albumList) {

					Log.d("Share-Bar", "Album ID = " + a.getId());
					Log.d("Share-Bar", "Album Name = " + a.getName());
					Log.d("Share-Bar", "Cover Photo = " + a.getCoverPhoto());
					Log.d("Share-Bar", "Photos Count = " + a.getPhotosCount());

					photosList = a.getPhotos();

					if (photosList != null && photosList.size() > 0) {

						for (Photo p : photosList) {
							Log.d("Share-Bar", "Photo ID = " + p.getId());
							Log.d("Share-Bar", "Name     = " + p.getTitle());
							Log.d("Share-Bar",
									"Thumb Image = " + p.getThumbImage());
							Log.d("Share-Bar",
									"Small Image = " + p.getSmallImage());
							Log.d("Share-Bar",
									"Medium Image = " + p.getMediumImage());
							Log.d("Share-Bar",
									"Large Image = " + p.getLargeImage());
						}
					}
				}
			}
		}

		@Override
		public void onError(SocialAuthError error) {
			error.printStackTrace();
			Log.d("Share-Bar", error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("Share-Bar", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			Log.d("Share-Bar", "Dialog Closed by pressing Back Key");

		}

	}

}