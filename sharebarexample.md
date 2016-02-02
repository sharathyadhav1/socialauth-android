# Share Bar #

This option lets you integrate a bar of providers in your app as shown below :

![https://socialauth-android.googlecode.com/svn/wiki/images/sharebar.png](https://socialauth-android.googlecode.com/svn/wiki/images/sharebar.png)

  * Load main.xml and create bar using LinearLayout. You can modify the bar using main.xml for your app.
```
    setContentView(R.layout.main);
    LinearLayout bar = (LinearLayout)findViewById(R.id.linearbar);
    bar.setBackgroundResource(R.drawable.bar_gradient);
```


  * Create SocialAuthAdapter object.
  * Add providers to object.
  * Pass bar object as argument to enable() method. This method will    create the bar and enable providers.
  * Receive the response in ResponseListener, A Dialoglistener to listen responses.
  * Call update() to share the status.

**Here is the code snippet :**
```

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      
        TextView textview = (TextView)findViewById(R.id.text);
        textview.setText("Welcome to SocialAuth Demo. We are sharing text SocialAuth Android by Share Bar.");
        
        LinearLayout bar = (LinearLayout)findViewById(R.id.linearbar);
        bar.setBackgroundResource(R.drawable.bar_gradient);
        
        // Add Bar to library
        adapter = new SocialAuthAdapter(new ResponseListener());
        
        // Add providers
	adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
	adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
	adapter.addProvider(Provider.LINKEDIN, R.drawable.linkedin);
	adapter.addProvider(Provider.MYSPACE, R.drawable.myspace);

	// Add email and mms providers
	adapter.addProvider(Provider.EMAIL, R.drawable.email);
	adapter.addProvider(Provider.MMS, R.drawable.mms);

	// For twitter use add callback method. Put your own callback url here.
	adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
        
        adapter.enable(bar);
    }
```


## Share Message ##
(Supports : Facebook, Twitter, Linkedin, MySpace, Yahoo, Yammer)
```

private final class ResponseListener implements DialogListener 
{
   public void onComplete(Bundle values) {
     	    
   edit = (EditText) findViewById(R.id.editTxt);
   adapter.updateStatus(edit.getText().toString(), new MessageListener(),false);
   }

   public void onError(DialogError error) {
     Log.d("ShareBar" , "Error");
   }

   public void onCancel() {
     Log.d("ShareBar" , "Cancelled");
   }

}

// To get status of message after authentication
private final class MessageListener implements SocialAuthListener<Integer> {
 
   public void onExecute(Integer t) {
   
   Integer status = t;
   
   if (status.intValue() == 200 || status.intValue() == 201 ||status.intValue() == 204)
   Toast.makeText(ShareBarActivity.this, "Message posted",   Toast.LENGTH_LONG).show();
   
   else
   Toast.makeText(ShareBarActivity.this, "Message not posted",Toast.LENGTH_LONG).show();
  }

  public void onError(SocialAuthError e) {

  }
}
```
## Share Message by EMAIL & MMS ##

```
private final class ResponseListener implements DialogListener 
{
   public void onComplete(Bundle values) {
   final String providerName =values.getString(SocialAuthAdapter.PROVIDER);

// Share via Email Intent
if (providerName.equalsIgnoreCase("share_mail")) {
	// enter your email intent code here			
}

// Share via Email Intent
if (providerName.equalsIgnoreCase("share_mms")) {
	// enter your mms intent code here			
}

}

```

**You can also use share-bar to load profile , contacts , images and feeds.**

To see the functionality, view **share-bar example** from socialauth-android-sdk-2.6 zip on Download Page.