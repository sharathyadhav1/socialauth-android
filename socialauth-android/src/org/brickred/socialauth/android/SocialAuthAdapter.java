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

package org.brickred.socialauth.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.Constants;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * Main class of the SocialAuth Android SDK. Wraps a user interface component
 * with the SocialAuth functionality of updating status, getting user profiles
 * and contacts on Facebook, Twitter, LinkedIn and MySpace. <br>
 * 
 * Currently it can be used in three different ways. First, it can be attached
 * with a Button that user may click. Clicking will open a menu with various
 * social networks listed that the user can click on. Clicking on any network
 * opens a dialog for authentication with that social network. Once the user is
 * authenticated, you can use various methods from the AuthProvider interface to
 * update status, get profile or contacts. <br>
 * 
 * Secondly, it can be attached to a LinearLayout for creating a Bar with
 * several buttons, one for each social network. Clicking on these icons will
 * open a dialog which will authenticate the user and one the user is
 * authenticated, you can use various methods from the AuthProvider interface to
 * update status, get profile and contacts. <br>
 * 
 * Lastly, you can just launch the authentication dialog directly from any event
 * you prefer. Examples for all of these ways is provided in the examples
 * directory of the SocialAuth Android SDK
 * 
 * @author vineeta@brickred.com
 * @author abhinavm@brickred.com
 * 
 */

public class SocialAuthAdapter implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Enum of all supported providers
	 * 
	 * @author abhinavm@brickred.com
	 */
	public enum Provider {
		FACEBOOK(Constants.FACEBOOK, "fbconnect://success",
				"fbconnect://cancel"), TWITTER(Constants.TWITTER,
				"twitterapp://connect", "twitterapp://connect?denied"), LINKEDIN(
				Constants.LINKEDIN,
				"http://opensource.brickred.com/socialauthdemo/socialAuthSuccessAction.do",
				"http://brickred.com/socialauthdemo"), MYSPACE(
				Constants.MYSPACE, "http://opensource.brickred.com",
				"http://opensource.brickred.com/?oauth_problem");

		private String name;
		private String cancelUri;
		private String callbackUri;

		/**
		 * Constructor with unique string representing the provider
		 * 
		 * @param name
		 */
		Provider(String name, String callbackUri, String cancelUri) {
			this.name = name;
			this.cancelUri = cancelUri;
			this.callbackUri = callbackUri;
		}

		String getCancelUri() {
			return this.cancelUri;
		}

		String getCallbackUri() {
			return this.callbackUri;
		}

		/**
		 * Returns the unique string representing the provider
		 */
		public String toString() {
			return name;
		}
	}

	// Constants
	public static final String PROVIDER = "provider";

	// SocialAuth Components
	private SocialAuthManager socialAuthManager;

	// Variables
	private DialogListener dialogListener;
	private Provider currentProvider;

	private int providerCount = 0;
	private Provider authProviders[];
	private int authProviderLogos[];

	
	/**
	 * Constructor
	 * 
	 * @param listener
	 *            Listener for the adapter events
	 */
	
	public SocialAuthAdapter(DialogListener listener) {
		authProviders = new Provider[Provider.values().length];
		authProviderLogos = new int[Provider.values().length];
		this.dialogListener = listener;
	}
	

	/**
	 * Attaches a new listener to the adapter. Define logos and providers.
	 * 
	 * @param listener
	 */
	public void setListener(DialogListener listener) {
		this.dialogListener = listener;
	}

	/**
	 * Enables a provider
	 * 
	 * @param provider
	 *            Provider to be enables
	 * @param logo
	 *            Image resource for the logo of the provider
	 */
	public void addProvider(Provider provider, int logo) {
		authProviders[providerCount] = provider;
		authProviderLogos[providerCount] = logo;
		providerCount++;
	}

	/**
	 * Enables a button with the SocialAuth menu
	 * 
	 * @param sharebtn
	 *            The button that will be clicked by user to start sharing
	 */
	public void enable(Button sharebtn) {

		Log.d("SocialAuthAdapter", "Enabling button with SocialAuth");
		final Context ctx = sharebtn.getContext();

		// Click Listener For Share Button
		sharebtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// This dialog will show list of all providers
				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
				builder.setTitle("Share via");
				builder.setCancelable(true);
				builder.setIcon(android.R.drawable.ic_menu_more);

				// Handles Click Events
				String[] providerNames = new String[providerCount];
				int[] providerLogos = new int[providerCount];

				for (int i = 0; i < providerCount; i++) {
					providerNames[i] = authProviders[i].toString();
					providerLogos[i] = authProviderLogos[i];
				}

				builder.setSingleChoiceItems(new ShareButtonAdapter(ctx,
						providerNames, providerLogos), 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								// Getting selected provider and starting
								// authentication
								authorize(ctx, authProviders[item]);
								dialog.dismiss();
							}
						});
				final AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
	}

	/**
	 * Enables a LinearLayout with SocialAuth functionality
	 * 
	 * @param linearbar
	 *            The LinearLayout which is created as a bar
	 */
	public void enable(LinearLayout linearbar) {

		Log.d("SocialAuthAdapter", "Enabling bar with SocialAuth");
		final Context ctx = linearbar.getContext();

		// Handles Clicking Events for Buttons
		View.OnClickListener viewlistener = new View.OnClickListener() {
			public void onClick(View v) {
				// Getting selected provider and starting authentication
				authorize(ctx, authProviders[v.getId()]);
			}
		};

		// Adding Buttons to Bar
		for (int i = 0; i < providerCount; i++) {
			ImageView provider = new ImageView(ctx);
			provider.setId(i);
			provider.setImageResource(authProviderLogos[i]);
			provider.setPadding(10, 5, 10, 5);
			provider.setOnClickListener(viewlistener);
			linearbar.addView(provider);
		}
	}

	/**
	 * Returns the last authenticated provider. Please use the SocialAuth API to
	 * find out about the methods available in this interface
	 * 
	 * @return Provider object
	 */
	public AuthProvider getCurrentProvider() {
		if (currentProvider != null) {
			return socialAuthManager.getProvider(currentProvider.toString());
		} else {
			return null;
		}
	}

	/**
	 * Sets Size of Dialog. Max value for Portrait and Landscape - 40,60
	 */
	public void setDialogSize(float width, float height) {
		if (width < 0 || width > 40)
			SocialAuthDialog.width = 40;
		else
			SocialAuthDialog.width = width;

		if (height < 0 || height > 60)
			SocialAuthDialog.height = 60;
		else
			SocialAuthDialog.height = height;
	}

	/**
	 * Method to handle configuration
	 */
	public void authorize(Context ctx, Provider provider) {
		if (!Util.isNetworkAvailable(ctx)) {
			dialogListener
					.onError(new SocialAuthError(
							"Please check your Internet connection",
							new Exception("")));
			return;
		}
		currentProvider = provider;
		Log.d("SocialAuthAdapter", "Selected provider is " + currentProvider);

		// Initialize socialauth manager if not already done
		if (socialAuthManager != null) {
			// Mechanism for multiple provider selection
			if (socialAuthManager.getConnectedProvidersIds().contains(currentProvider.toString())) 
			{
				Bundle bundle = new Bundle();
				bundle.putString(SocialAuthAdapter.PROVIDER,currentProvider.toString());
				dialogListener.onComplete(bundle);
				Log.d("SocialAuthAdapter", "Provider already connected");
			} 
			else 
			{
				Log.d("SocialAuthAdapter","Starting webview for authentication");
				startDialogAuth(ctx, currentProvider);
			}
		} else {
			Log.d("SocialAuthAdapter","Loading keys and secrets from configuration");
			Resources resources = ctx.getResources();
			// Read from the /assets directory
			AssetManager assetManager = resources.getAssets();
			try {
				InputStream inputStream = assetManager.open("oauth_consumer.properties");
				SocialAuthConfig config = new SocialAuthConfig();
				config.load(inputStream);
				socialAuthManager = new SocialAuthManager();
				socialAuthManager.setSocialAuthConfig(config);
				startDialogAuth(ctx, currentProvider);

			} catch (IOException ioe) {
				dialogListener.onError(new SocialAuthError(
						"Could not load configuration", ioe));
			} catch (Exception e) {
				dialogListener.onError(new SocialAuthError("Unknown error", e));
			}

		}
	}

	/**
	 * Internal method to handle dialog-based authentication backend for
	 * authorize().
	 * 
	 * @param context
	 *            The Android Activity that will parent the auth dialog.
	 * @param provider
	 *            Provider being authenticated
	 * 
	 */
	private void startDialogAuth(Context context, Provider provider) {
		CookieSyncManager.createInstance(context);

		try {
			String url = socialAuthManager.getAuthenticationUrl(
					provider.toString(), provider.getCallbackUri())
					+ "&type=user_agent&display=touch";
			Log.d("SocialAuthAdapter", "Loading URL : " + url);

			String callbackUri = provider.getCallbackUri();
			Log.d("SocialAuthAdapter", "Callback URI : " + callbackUri);
			new SocialAuthDialog(context, url, provider, dialogListener,
					socialAuthManager).show();

		} catch (Exception e) {
			dialogListener.onError(new SocialAuthError("Unknown error", e));
		}
	}

	/**
	 * Signs out the user out of current provider
	 * 
	 * @return Status of signing out
	 */
	public boolean signOut() {
		boolean signedin = socialAuthManager.disconnectProvider(currentProvider
				.toString());
		Log.d("SocialAuthAdapter", "Disconnecting " + String.valueOf(signedin));
		return signedin;
	}
}
