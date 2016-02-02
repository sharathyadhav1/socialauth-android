# Introduction #

The example used Android Application class used to set global settings. the idea here is to use socialauth adapter object across activities. We are extending share-bar example for usecase.

Copy files below in your project.

**ShareBarActivity.java**

ShareBar activity create bar and on clicking any provider launches webview. The webview on successful authentication launches ProviderActivity.

```

public class ShareBarActivity extends Activity {

	// SocialAuth Component
	SocialAuthAdapter adapter;
	boolean status;
	Profile profileMap;
	MyApplication myApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Welcome Message
		TextView textview = (TextView) findViewById(R.id.text);
		textview.setText("Welcome to SocialAuth Demo. We are sharing text SocialAuth Android by Share Bar.");

		LinearLayout bar = (LinearLayout)findViewById(R.id.linearbar);
		bar.setBackgroundResource(R.drawable.bar_gradient);

		// Add Bar to library
		adapter = new SocialAuthAdapter(new ResponseListener());

		// Add providers
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
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

			Log.d("ShareBar", "Authentication Successful");

			// Get name of provider after authentication
			String providerName = values.getString(SocialAuthAdapter.PROVIDER);

			myApp = (MyApplication) getApplication();

			myApp.setSocialAuthAdapter(adapter);

			Intent i = new Intent(ShareBarActivity.this, ProviderActivity.class);
			i.putExtra("ProviderName", providerName);
			startActivity(i);

		}

		@Override
		public void onError(SocialAuthError error) {
			error.printStackTrace();
			Log.d("ShareBar", error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("ShareBar", "Authentication Cancelled");
		}

	}

}

```


**ProviderActivity.java**

The activity performs all event to post message , get profile , contacts etc.

```

public class ProviderActivity extends Activity {

	// SocialAuth Component
	SocialAuthAdapter adapter;
	boolean status;
	Profile profileMap;
	MyApplication myApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);

		myApp = (MyApplication) getApplication();
		adapter = myApp.getSocialAuthAdapter();

		String providerName = getIntent().getStringExtra("ProviderName");
		Log.d("Provider Activity", "Provider Name = " + providerName);

		// Please avoid sending duplicate message. Social Media Providers
		// block duplicate messages.

		adapter.updateStatus("Hello World SocialAuth"
				+ System.currentTimeMillis());

		// Welcome Message
		TextView textview = (TextView) findViewById(R.id.message);
		textview.setText("Message posted on" + providerName);

	}
}

```


**MyApplication.java**

```
public class MyApplication extends Application {

	// SocialAuth Component
	private SocialAuthAdapter socialAuthAdpater;

	public SocialAuthAdapter getSocialAuthAdapter() {
		return socialAuthAdpater;
	}

	public void setSocialAuthAdapter(SocialAuthAdapter socialAuthAdapter) {
		this.socialAuthAdpater = socialAuthAdapter;
	}
}

```


**AndroidManifest.xml**

```

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
      package="org.brickred.socialbaractivity"
      android:versionCode="1"
      android:versionName="1.0">

	<uses-sdk android:minSdkVersion="10" />
    <uses-permission android:name="android.permission.INTERNET"></uses-permission>
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>

    <application android:icon="@drawable/icon" android:label="@string/app_name" android:name="MyApplication">
        <activity android:name=".ShareBarActivity"
                  android:label="@string/app_name">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
         <activity android:name=".ProviderActivity"></activity>
    </application>    
</manifest>


```