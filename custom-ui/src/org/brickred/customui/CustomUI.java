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

package org.brickred.customui;

import java.util.List;

import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Main class of the CustomUI Example for SocialAuth Android SDK. <br>
 * 
 * The main objective of this example is to access social media providers
 * Facebook, Twitter and others by creating your own UI.
 * 
 * Here we are creating a ListView. The ListView contains list of all providers
 * On clicking any provider, it authorizes the provider by calling authorize
 * method.
 * 
 * After successful authentication of provider, it receives the response in
 * responseListener and then shows a dialog containing options for getting user
 * profile , for updating status and to get contact list.<br>
 * 
 * @author vineet.aggarwal@3pillarglobal.com
 * 
 */

// Please see strings.xml for list values

public class CustomUI extends Activity {

	// SocialAuth Components
	SocialAuthAdapter adapter;
	Profile profileMap;

	// Android Components
	ListView listview;
	AlertDialog dialog;

	// Variables
	boolean status;
	public static int pos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Adapter initialization
		adapter = new SocialAuthAdapter(new ResponseListener());

		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(new CustomAdapter(this, adapter));

		// Welcome Message
		TextView textview = (TextView) findViewById(R.id.text);
		textview.setText("Welcome to SocialAuth Demo. Use SocialAuth by creating your own UI.");
		textview.setTextColor(Color.WHITE);
		textview.setGravity(Gravity.CENTER);
		textview.setPadding(0, 30, 0, 0);

	}

	// To receive the response after authentication
	private final class ResponseListener implements DialogListener {

		@Override
		public void onComplete(Bundle values) {

			Log.d("Custom-UI", "Successful");

			// Changing Sign In Text to Sign Out
			// Code to refresh Single ListView Item : You can remove it for your
			// app
			View v = listview.getChildAt(pos
					- listview.getFirstVisiblePosition());
			TextView pText = (TextView) v.findViewById(R.id.signstatus);
			pText.setText("Sign Out");

			// Get the provider
			final String providerName = values
					.getString(SocialAuthAdapter.PROVIDER);
			Log.d("Custom-UI", "providername = " + providerName);

			int res = getResources().getIdentifier(providerName + "_array",
					"array", CustomUI.this.getPackageName());

			AlertDialog.Builder builder = new AlertDialog.Builder(CustomUI.this);
			builder.setTitle("Select Options");
			builder.setCancelable(true);
			builder.setIcon(android.R.drawable.ic_menu_more);

			builder.setSingleChoiceItems(new ArrayAdapter<String>(
					CustomUI.this, R.layout.provider_options, getResources()
							.getStringArray(res)), 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {

							Events(item, providerName);
						}
					});
			dialog = builder.create();
			dialog.show();

		}

		@Override
		public void onError(SocialAuthError error) {
			Log.d("Custom-UI", "Error");
		}

		@Override
		public void onCancel() {
			Log.d("Custom-UI", "Cancelled");
		}
	}

	// Method to handle events of providers
	public void Events(int position, String provider) {

		switch (position) {
		case 0: // Code to print user profile details for all providers
		{

			profileMap = adapter.getUserProfile();

			Log.d("Custom-UI", "Validate ID = " + profileMap.getValidatedId());
			Log.d("Custom-UI", "First Name  = " + profileMap.getFirstName());
			Log.d("Custom-UI", "Last Name   = " + profileMap.getLastName());
			Log.d("Custom-UI", "Email       = " + profileMap.getEmail());
			Log.d("Custom-UI", "Gender  	 = " + profileMap.getGender());
			Log.d("Custom-UI", "Country  	 = " + profileMap.getCountry());
			Log.d("Custom-UI", "Language  	 = " + profileMap.getLanguage());
			Log.d("Custom-UI", "Location 	 = " + profileMap.getLocation());
			Log.d("Custom-UI",
					"Profile Image URL  = " + profileMap.getProfileImageURL());

			Toast.makeText(CustomUI.this,
					"View Logcat for Profile Information", Toast.LENGTH_SHORT)
					.show();

			break;
		}

		case 1: {

			if (provider.equalsIgnoreCase("foursquare")
					|| provider.equalsIgnoreCase("google")) {
				getContacts();
			} else if (provider.equalsIgnoreCase("runkeeper")
					|| provider.equalsIgnoreCase("salesforce")) {
				dialog.dismiss();
			} else // Code to Post Message for all providers
			{
				adapter.updateStatus("SocialAuth Android"
						+ System.currentTimeMillis());
				Toast.makeText(CustomUI.this, "Message posted on " + provider,
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		case 2: {

			if (provider.equalsIgnoreCase("foursquare")
					|| provider.equalsIgnoreCase("google")) {
				dialog.dismiss();
			} else {
				getContacts();
			}

			break;
		}

		case 3: {
			if (provider.equalsIgnoreCase("facebook")
					|| provider.equalsIgnoreCase("twitter")) {

				// Upload Photo

				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.icon);

				int imgStatus = adapter.uploadImage("HelloWorldTest",
						"icon.png", bmp, 0);
				Log.d("Custom-UI", String.valueOf(imgStatus));

				Toast.makeText(CustomUI.this,
						"View Logcat for Image Upload Information",
						Toast.LENGTH_SHORT).show();
			} else {
				dialog.dismiss();
			}

			break;
		}

		case 4: // Back to Activity
		{
			dialog.dismiss();
			break;
		}
		}

	}

	// Code to get Contacts List for all providers
	public void getContacts() {
		List<Contact> contactsList = adapter.getContactList();

		if (contactsList != null && contactsList.size() > 0) {
			for (Contact p : contactsList) {

				if (TextUtils.isEmpty(p.getFirstName())
						&& TextUtils.isEmpty(p.getLastName())) {
					p.setFirstName(p.getDisplayName());
				}

				Log.d("Custom-UI", "Display Name = " + p.getDisplayName());
				Log.d("Custom-UI", "First Name = " + p.getFirstName());
				Log.d("Custom-UI", "Last Name = " + p.getLastName());
				Log.d("Custom-UI", "Contact ID = " + p.getId());
				Log.d("Custom-UI", "Profile URL = " + p.getProfileUrl());

			}
		}
		Toast.makeText(CustomUI.this, "View Logcat for Contacts Information",
				Toast.LENGTH_SHORT).show();
	}

}