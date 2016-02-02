# Share Button #
This option lets you integrate share button in your app. You will get list of providers on clicking the share button as shown below :

![https://socialauth-android.googlecode.com/svn/wiki/images/socialshare.png](https://socialauth-android.googlecode.com/svn/wiki/images/socialshare.png)

In your handling code you should :
  * Create a Button.
  * Create SocialAuthAdapter object.
  * Add providers to object.
  * Pass button object as argument to enable() method. This method will    create the share button and enable providers.
  * Receive the response in ResponseListener, A Dialoglistener to listen responses.
  * Call update() to share the status.

**Here is the code snippet :**

```
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      
       // Welcome Message
        TextView textview = (TextView)findViewById(R.id.text);
        textview.setText("Welcome to SocialAuth Demo. We are sharing text SocialAuth Android by share button.");
        
        //Create Your Own Share Button
        Button share = (Button)findViewById(R.id.sharebutton);
        share.setText("Share");
        share.setTextColor(Color.WHITE);
        share.setBackgroundResource(R.drawable.button_gradient);
        		
        // Add it to Library
        adapter = new SocialAuthAdapter(new ResponseListener());
             	 
       	// Add providers
	adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
	adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
	adapter.addProvider(Provider.LINKEDIN, R.drawable.linkedin);
	adapter.addProvider(Provider.MYSPACE, R.drawable.myspace);
	adapter.addProvider(Provider.YAHOO, R.drawable.yahoo);
	adapter.addProvider(Provider.YAMMER, R.drawable.yammer);
	adapter.addProvider(Provider.EMAIL, R.drawable.email);
	adapter.addProvider(Provider.MMS, R.drawable.mms);

	// Providers require setting user call Back url
	adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
	adapter.addCallBack(Provider.YAMMER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

	// Enable Provider
	adapter.enable(share);
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
     Log.d("ShareButton" , "Error");
   }

   public void onCancel() {
     Log.d("ShareButton" , "Cancelled");
   }
}

// To get status of message after authentication
private final class MessageListener implements SocialAuthListener<Integer> {

   public void onExecute(Integer t) {
   Integer status = t;
   if (status.intValue() == 200 || status.intValue() == 201 ||status.intValue() == 204)
   Toast.makeText(ShareButtonActivity.this, "Message posted",Toast.LENGTH_LONG).show();
   else
   Toast.makeText(ShareButtonActivity.this, "Message not posted",Toast.LENGTH_LONG).show();
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

**You can also use share-button to load profile , contacts , images and feeds.**

> To see the full functionality, view **share-button example** from socialauth-android-sdk-3.2.zip on Downloads page.