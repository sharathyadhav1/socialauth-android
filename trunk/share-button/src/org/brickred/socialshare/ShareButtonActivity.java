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

import org.brickred.socialauth.Photo;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Main class of the ShareButton Example for SocialAuth Android SDK. <br>
 * 
 * The main objective of this example is to access social media providers
 * Facebook, Twitter and others by clicking a single button "Share".On Clicking
 * the button the api will open dialog of providers. User can access the
 * provider from dialog and can update the status.
 * 
 * The class first creates a button in main.xml. It then adds button to
 * SocialAuth Android Library <br>
 * 
 * Then it adds providers Facebook, Twitter and others to library object by
 * addProvider method and finally enables the providers by calling enable method<br>
 * 
 * After successful authentication of provider, it receives the response in
 * responseListener and then update status by updatestatus() method <br>
 * 
 * @author vineet.aggarwal@3pillarglobal.com
 * 
 */

public class ShareButtonActivity extends Activity {

	// SocialAuth Component
	SocialAuthAdapter adapter;
	Profile profileMap;
	List<Photo> photosList;

	// Android Components
	Button update;
	EditText edit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Welcome Message
		TextView textview = (TextView) findViewById(R.id.text);
		textview.setText("Welcome to SocialAuth Demo. Connect any provider and then press Update button to Share Update.");

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
		adapter.addCallBack(Provider.FOURSQUARE, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		adapter.addCallBack(Provider.GOOGLE, "http://socialauth.in/socialauthdemo");

		adapter.addCallBack(Provider.SALESFORCE, "https://socialauth.in:8443/socialauthdemo/socialAuthSuccessAction.do");

		adapter.addCallBack(Provider.YAMMER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

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
			final String providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("ShareButton", "Provider Name = " + providerName);
			Toast.makeText(ShareButtonActivity.this, providerName + " connected", Toast.LENGTH_LONG).show();

			update = (Button) findViewById(R.id.update);
			edit = (EditText) findViewById(R.id.editTxt);

			// Please avoid sending duplicate message. Social Media Providers
			// block duplicate messages.

			update.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					adapter.updateStatus(edit.getText().toString());
					Toast.makeText(ShareButtonActivity.this, "Message posted on " + providerName, Toast.LENGTH_LONG)
							.show();
				}
			});

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

}