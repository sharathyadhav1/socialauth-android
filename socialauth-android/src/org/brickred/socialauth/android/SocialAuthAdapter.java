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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.brickred.socialauth.Album;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Feed;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.exception.SocialAuthException;
import org.brickred.socialauth.plugin.AlbumsPlugin;
import org.brickred.socialauth.plugin.FeedPlugin;
import org.brickred.socialauth.util.AccessGrant;
import org.brickred.socialauth.util.Constants;
import org.brickred.socialauth.util.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * Main class of the SocialAuth Android SDK. Wraps a user interface component
 * with the SocialAuth functionality of updating status, getting user profiles,
 * contacts, upload images, get user feeds, get user albums on Facebook,
 * Twitter, LinkedIn, MySpace, Yahoo, Google FourSquare, Runkeeper, SalesForce
 * and Yammer. <br>
 * 
 * Currently it can be used in three different ways. First, it can be attached
 * with a Button that user may click. Clicking will open a menu with various
 * social networks listed that the user can click on. Clicking on any network
 * opens a dialog for authentication with that social network. Once the user is
 * authenticated, you can use various methods from the AuthProvider interface to
 * update status, get profile, contacts, user feeds, album feeds and upload
 * images. <br>
 * 
 * Secondly, it can be attached to a LinearLayout for creating a Bar with
 * several buttons, one for each social network. Clicking on these icons will
 * open a dialog which will authenticate the user and one the user is
 * authenticated, you can use various methods from the AuthProvider interface to
 * update status, get profile, contacts, user feeds, album feeds and upload
 * images. <br>
 * 
 * Lastly, you can just launch the authentication dialog directly from any event
 * you prefer. Examples for all of these ways is provided in the examples
 * directory of the SocialAuth Android SDK
 * 
 * @author vineet.aggarwal@3pillarglobal.com
 * @author abhinav.maheswari@3pillarglobal.com
 * 
 */

public class SocialAuthAdapter {

	/**
	 * Enum of all supported providers
	 * 
	 */
	public enum Provider {
		FACEBOOK(Constants.FACEBOOK, "fbconnect://success",
				"fbconnect://success?error_reason"), TWITTER(Constants.TWITTER,
				"twitterapp://connect", "twitterapp://connect?denied"), LINKEDIN(
				Constants.LINKEDIN,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do",
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do?oauth_problem"), MYSPACE(
				Constants.MYSPACE, "http://socialauth.in",
				"http://socialauth.in/?oauth_problem"), RUNKEEPER(
				Constants.RUNKEEPER,
				"http://socialauth.in/socialauthdemo/socialauthSuccessAction.do",
				"http://socialauth.in/socialauthdemo/socialauthSuccessAction.do/?error"), YAHOO(
				Constants.YAHOO, "http://socialauth.in/socialauthdemo",
				"http://socialauth.in/socialauthdemo/?oauth_problem"), FOURSQUARE(
				Constants.FOURSQUARE,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do",
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do/?oauth_problem"), GOOGLE(
				Constants.GOOGLE, "http://socialauth.in/socialauthdemo",
				"http://socialauth.in/socialauthdemo/?oauth_problem"), YAMMER(
				Constants.YAMMER,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do",
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do/?oauth_problem"), SALESFORCE(
				Constants.SALESFORCE,
				"https://socialauth.in:8443/socialauthdemo/socialAuthSuccessAction.do",
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do/?oauth_problem");

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

		/**
		 * returns cancel URI
		 */
		String getCancelUri() {
			return this.cancelUri;
		}

		/**
		 * returns Callback URI
		 */
		String getCallBackUri() {
			return this.callbackUri;
		}

		/**
		 * Set callback URI
		 */
		public void setCallBackUri(String callbackUri) {
			this.callbackUri = callbackUri;
		}

		/**
		 * Returns the unique string representing the provider
		 */
		@Override
		public String toString() {
			return name;
		}
	}

	// Constants
	public static final String PROVIDER = "provider";
	public static final String ACCESS_GRANT = "access_grant";

	// SocialAuth Components
	private SocialAuthManager socialAuthManager;
	private Profile profileMap;
	private List<Contact> contactsList;
	private List<Feed> feedList;
	private List<Album> albumList;
	private DialogListener dialogListener;
	private Provider currentProvider;

	// Variables and Arrays
	private String url;
	private int providerCount = 0;
	private final Provider authProviders[];
	private final int authProviderLogos[];

	// Android Components
	private Context context;
	private final Handler handler = new Handler();

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
	 * Adds callback URL
	 * 
	 * @param provider
	 *            Provider to be enables
	 * @param calBack
	 *            CallBack URL String
	 */
	public void addCallBack(Provider provider, String callBack) {
		if (provider.name() == Constants.FACEBOOK
				|| provider.name() == Constants.TWITTER
				|| provider.name() == Constants.LINKEDIN
				|| provider.name() == Constants.MYSPACE) {
			Log.d("SocialAuthAdapter", "Callback Url not require");
		} else
			provider.setCallBackUri(callBack);
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
		context = ctx;
		// Click Listener For Share Button
		sharebtn.setOnClickListener(new OnClickListener() {
			@Override
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
							@Override
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
		context = ctx;
		// Handles Clicking Events for Buttons
		View.OnClickListener viewlistener = new View.OnClickListener() {
			@Override
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
	 * Method to handle configuration , Use directly for CustomUI
	 */
	public void authorize(Context ctx, Provider provider) {
		if (!Util.isNetworkAvailable(ctx)) {
			dialogListener
					.onError(new SocialAuthError(
							"Please check your Internet connection",
							new Exception("")));
			return;
		}
		context = ctx;
		currentProvider = provider;
		Log.d("SocialAuthAdapter", "Selected provider is " + currentProvider);

		// Initialize socialauth manager if not already done
		if (socialAuthManager != null) {
			// If SocialAuthManager is not null and contains Provider Id, send
			// response to listener
			if (socialAuthManager.getConnectedProvidersIds().contains(
					currentProvider.toString())) {
				Log.d("SocialAuthAdapter", "Provider already connected");
				Bundle bundle = new Bundle();
				bundle.putString(SocialAuthAdapter.PROVIDER,
						currentProvider.toString());
				dialogListener.onComplete(bundle);
			}

			// If SocialAuthManager is not null and not contains Provider Id
			else {
				connectProvider(ctx, provider);
			}

		}
		// If SocialAuthManager is null
		else {
			Log.d("SocialAuthAdapter",
					"Loading keys and secrets from configuration");

			socialAuthManager = new SocialAuthManager();
			loadConfig(ctx);
			connectProvider(ctx, provider);

		}
	}

	/**
	 * Signs out the user out of current provider
	 * 
	 * @return Status of signing out
	 */
	public boolean signOut(String providerName) {
		CookieSyncManager cookieSyncMngr = CookieSyncManager
				.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();

		AccessGrant accessGrant = socialAuthManager.getProvider(providerName)
				.getAccessGrant();
		if (accessGrant != null)
			try {
				socialAuthManager.getProvider(providerName)
						.setAccessGrant(null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		String filePath = context.getFilesDir().getAbsolutePath()
				+ File.separatorChar + providerName + "_accessGrant.ser";
		File tokenFile = new File(filePath);
		tokenFile.delete();

		socialAuthManager.disconnectProvider(providerName);

		if (socialAuthManager.getConnectedProvidersIds().contains(providerName))
			Log.d("SocialAuth", " Provider Still Connected");

		Log.d("SocialAuthAdapter", "Disconnecting Provider");
		return true;
	}

	/**
	 * Method to update status of user
	 * 
	 * @param message
	 *            The message to be send.
	 */

	public void updateStatus(final String message) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					getCurrentProvider().updateStatus(message);
					Log.d("SocialAuthAdapter", "Message Posted");

				} catch (Exception e) {
					dialogListener.onError(new SocialAuthError(
							"Message Not Posted", e));
				}
			}
		};

		new Thread(runnable).start();
	}

	/**
	 * Disable title of dialog.
	 * 
	 * @param titleStatus
	 *            default false , Set true to disable dialog titlebar
	 * 
	 */
	public void disableTitle(boolean titleStatus) {
		SocialAuthDialog.titleStatus = titleStatus;
	}

	/**
	 * Method to get Profile of User
	 * 
	 * @return Profile Profile Object containing User Profile Details .
	 */

	public Profile getUserProfile() {
		try {
			profileMap = new profileTask().execute().get();
		} catch (InterruptedException e) {
			dialogListener.onError(new SocialAuthError("Unknown Error", e));
		} catch (ExecutionException e) {
			dialogListener.onError(new SocialAuthError("Unknown Error", e));
		}
		return profileMap;
	}

	/**
	 * Method to get List of Contacts
	 * 
	 * @return List containing Contacts .
	 */

	public List<Contact> getContactList() {
		try {
			contactsList = new contactTask().execute().get();
		} catch (InterruptedException e) {
			dialogListener.onError(new SocialAuthError("Unknown Error", e));
		} catch (ExecutionException e) {
			dialogListener.onError(new SocialAuthError("Unknown Error", e));
		}

		return contactsList;
	}

	/**
	 * Method to get feeds from provider
	 * 
	 * @return List containing Feeds .
	 * 
	 *         feed List Contains
	 * 
	 *         Screen Name - For Twitter Only ID - Feed Id Message - Feed
	 *         Created at - Date of creation of feed Get From - Sender of feed
	 */

	public List<Feed> getFeeds() {
		try {
			feedList = new feedTask().execute().get();
		} catch (InterruptedException e) {
			dialogListener.onError(new SocialAuthError("Unknown Error", e));
		} catch (ExecutionException e) {
			dialogListener.onError(new SocialAuthError("Unknown Error", e));
		}

		return feedList;
	}

	/**
	 * Method to get albums from provider
	 * 
	 * @return List containing Albums and Photos .
	 * 
	 *         feed List Contains
	 * 
	 *         ID - Album ID Name - Album Name CoverPhoto - Album CoverPhoto
	 *         Photo Count - Number of photos in album Photos Array - Array
	 *         containing list of photos of album
	 */

	public List<Album> getAlbums() {
		try {
			albumList = new albumTask().execute().get();
		} catch (InterruptedException e) {
			dialogListener.onError(new SocialAuthError("Unknown Error", e));
		} catch (ExecutionException e) {
			dialogListener.onError(new SocialAuthError("Unknown Error", e));
		}

		return albumList;
	}

	/**
	 * Method to upload image on provider
	 * 
	 * @param message
	 *            message to be attached with image
	 * @param fileName
	 *            image file name
	 * @param bitmap
	 *            image bitmap to be uploaded
	 * @param quality
	 *            image quality for jpeg , enter 0 for png
	 * 
	 * @return upload status
	 */
	public int uploadImage(String message, String fileName, Bitmap bitmap,
			int quality) {
		Integer uploadStatus = 0;

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			if (fileName.endsWith("PNG") || fileName.endsWith("png")) {
				bitmap.compress(CompressFormat.PNG, 0, bos);
			} else if (fileName.endsWith("JPEG") || fileName.endsWith("JPG")
					|| fileName.endsWith("jpg") || fileName.endsWith("jpeg")) {
				bitmap.compress(CompressFormat.JPEG, quality, bos);
			} else {
				Log.d("SocialAuthAdapter", "Image Format not supported");
			}

			InputStream inputStream = new ByteArrayInputStream(
					bos.toByteArray());

			uploadStatus = new uploadImageTask().execute(message, fileName,
					inputStream).get();
			return uploadStatus.intValue();

		} catch (InterruptedException e) {
			dialogListener.onError(new SocialAuthError("Unknown Error", e));
			return 0;

		} catch (ExecutionException e) {
			dialogListener.onError(new SocialAuthError("Unknown Error", e));
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	// ******************* Private Utility Methods**********************//

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
	private void startDialogAuth(final Context context, final Provider provider) {
		CookieSyncManager.createInstance(context);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					url = socialAuthManager.getAuthenticationUrl(
							provider.toString(), provider.getCallBackUri())
							+ "&type=user_agent&display=touch";

					handler.post(new Runnable() {
						@Override
						public void run() {
							Log.d("SocialAuthAdapter", "Loading URL : " + url);
							String callbackUri = provider.getCallBackUri();
							Log.d("SocialAuthAdapter", "Callback URI : "
									+ callbackUri);
							new SocialAuthDialog(context, url, provider,
									dialogListener, socialAuthManager).show();
						}
					});
				} catch (Exception e) {
					dialogListener.onError(new SocialAuthError(
							"URL Authentication error", e));
				}
			}
		};

		new Thread(runnable).start();
	}

	/**
	 * Internal method to load socialauth configuration.
	 * 
	 * @param context
	 *            The Android Activity that will parent the auth dialog.
	 */

	private void loadConfig(Context ctx) {

		Resources resources = ctx.getResources();
		// Read from the assets directory
		AssetManager assetManager = resources.getAssets();

		InputStream inputStream;
		try {
			inputStream = assetManager.open("oauth_consumer.properties");
			SocialAuthConfig config = new SocialAuthConfig();
			config.load(inputStream);
			socialAuthManager.setSocialAuthConfig(config);
		} catch (IOException ioe) {
			dialogListener.onError(new SocialAuthError(
					"Could not load configuration", ioe));
		} catch (Exception e) {
			dialogListener.onError(new SocialAuthError("Unknown error", e));
		}
	}

	/**
	 * Internal method to connect provider. The method check for access token
	 * file. If available it connects manager with AccessGrant else create new
	 * manager and open webview
	 * 
	 * @param context
	 *            The Android Activity that will parent the auth dialog.
	 * @param provider
	 *            Provider being authenticated
	 */

	private void connectProvider(final Context ctx, final Provider provider) {

		FileInputStream fs = null;
		ObjectInputStream os = null;

		String filePath = ctx.getFilesDir().getAbsolutePath()
				+ File.separatorChar + provider.toString() + "_accessGrant.ser";
		final File file = new File(filePath);

		// If Access Token is available , connect using Access Token
		if (file.exists()) {
			try {

				fs = new FileInputStream(file);
				os = new ObjectInputStream(fs);
				final AccessGrant accessGrant = (AccessGrant) os.readObject();
				Log.d("SocialAuthAdapter", "Loading from AccessToken");

				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {

							socialAuthManager.connect(accessGrant);

							// To check validity of Access Token
							getCurrentProvider().getUserProfile()
									.getValidatedId();

							handler.post(new Runnable() {
								@Override
								public void run() {

									Bundle bundle = new Bundle();
									bundle.putString(
											SocialAuthAdapter.PROVIDER,
											currentProvider.toString());
									dialogListener.onComplete(bundle);
								}
							});
						} catch (Exception e) {
							dialogListener.onError(new SocialAuthError(
									"Token Error", e));
							Log.d("SocialAuthAdapter",
									"Starting webview for authentication for new Token");

							// Delete Access Token file and create new Manager
							file.delete();

							socialAuthManager = new SocialAuthManager();
							loadConfig(ctx);
							startDialogAuth(ctx, currentProvider);

						}
					}
				};

				new Thread(runnable).start();

				os.close();
				fs.close();

			} catch (Exception e) {
				dialogListener.onError(new SocialAuthError("Unknown error", e));
			}
		}
		// If Access Token is not available , Open Authentication
		// Dialog
		else {
			Log.d("SocialAuthAdapter", "Starting webview for authentication");
			startDialogAuth(ctx, currentProvider);
		}
	}

	/**
	 * AsyncTask to get user profile
	 */

	private class profileTask extends AsyncTask<Void, Void, Profile> {

		@Override
		protected Profile doInBackground(Void... params) {
			try {

				Profile profileList = getCurrentProvider().getUserProfile();
				Log.d("SocialAuthAdapter", "Received Profile Details");
				return profileList;
			} catch (Exception e) {
				dialogListener.onError(new SocialAuthError(
						"Profile Details not Received", e));
				return null;
			}
		}
	}

	/**
	 * AsyncTask to retrieve contacts
	 */

	private class contactTask extends AsyncTask<Void, Void, List<Contact>> {

		@Override
		protected List<Contact> doInBackground(Void... params) {
			try {
				List<Contact> contactsMap = getCurrentProvider()
						.getContactList();
				Log.d("SocialAuthAdapter", "Received Contact list");
				return contactsMap;
			} catch (Exception e) {
				e.printStackTrace();
				dialogListener.onError(new SocialAuthError(
						"Contact List not Received", e));
				return null;
			}
		}
	}

	/**
	 * AsyncTask to retrieve albums
	 */

	private class albumTask extends AsyncTask<Void, Void, List<Album>> {

		@Override
		protected List<Album> doInBackground(Void... params) {
			try {
				List<Album> albumMap = null;
				if (getCurrentProvider().isSupportedPlugin(
						org.brickred.socialauth.plugin.AlbumsPlugin.class)) {
					AlbumsPlugin p = getCurrentProvider().getPlugin(
							org.brickred.socialauth.plugin.AlbumsPlugin.class);
					albumMap = p.getAlbums();
					Log.d("SocialAuthAdapter", "Received Albums");
				} else
					Log.d("SocialAuthAdapter",
							"Albums not Supported from Provider");

				return albumMap;
			} catch (Exception e) {
				e.printStackTrace();
				dialogListener.onError(new SocialAuthError(
						"Album not Available from Provider", e));
				return null;
			}
		}
	}

	/**
	 * AsyncTask to retrieve feeds
	 */

	private class feedTask extends AsyncTask<Void, Void, List<Feed>> {

		@Override
		protected List<Feed> doInBackground(Void... params) {
			try {
				List<Feed> feedMap = null;
				if (getCurrentProvider().isSupportedPlugin(
						org.brickred.socialauth.plugin.FeedPlugin.class)) {
					FeedPlugin p = getCurrentProvider().getPlugin(
							org.brickred.socialauth.plugin.FeedPlugin.class);
					feedMap = p.getFeeds();
					Log.d("SocialAuthAdapter", "Received Feeds");
				} else
					Log.d("SocialAuthAdapter",
							"Feeds not Supported from Provider");

				return feedMap;
			} catch (Exception e) {
				e.printStackTrace();
				dialogListener.onError(new SocialAuthError(
						"Feed not Available from Provider", e));
				return null;
			}
		}
	}

	/**
	 * AsyncTask to uploadImage
	 */

	private class uploadImageTask extends AsyncTask<Object, Void, Integer> {

		@Override
		protected Integer doInBackground(Object... params) {
			Response res = null;
			try {
				res = getCurrentProvider().uploadImage((String) params[0],
						(String) params[1], (InputStream) params[2]);
				Log.d("SocialAuthAdapter", "Image Uploaded");
				return res.getStatus();
			} catch (SocialAuthException se) {
				Log.d("SocialAuthAdapter",
						"Image Upload not implemented for Provider");
				return null;
			} catch (Exception e) {
				dialogListener.onError(new SocialAuthError(
						"Image Upload Error", e));
				return null;
			}

		}
	}

}
