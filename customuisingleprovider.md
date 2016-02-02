# Custom UI Single Provider Example #

Here is example to implement facebook in your app

![https://socialauth-android.googlecode.com/svn/wiki/images/onlyfacebook.png](https://socialauth-android.googlecode.com/svn/wiki/images/onlyfacebook.png)


**Here is the code snippet :**
```
    // Adapter initialization
    adapter = new SocialAuthAdapter(new ResponseListener());
        
    facebook_button = (Button)findViewById(R.id.fb_btn);
    facebook_button.setBackgroundResource(R.drawable.facebook);
     
    facebook_button.setOnClickListener(new OnClickListener() 
    {
	public void onClick(View v) 
	{
	    adapter.authorize(ProviderUI.this, Provider.FACEBOOK);
	}
    });
```

To implement linkedin use Provider.Linkedin , for twitter use Provider.Twitter and for MySpace use Provider.MySpace

## Share Message ##
```

private final class ResponseListener implements DialogListener 
{
   public void onComplete(Bundle values) {
   
      adapter.updateStatus(edit.getText().toString(), new MessageListener());			
   }

   public void onError(DialogError error) {
     Log.d("Custom-UI" , "Error");
   }

   public void onCancel() {
     Log.d("Custom-UI" , "Cancelled");
   }

}

// To get status of message after authentication
private final class MessageListener implements SocialAuthListener<Integer> {
    @Override
    public void onExecute(Integer t) {

    Integer status = t;
    if (status.intValue() == 200 || status.intValue() == 201 ||status.intValue() == 204)
     Toast.makeText(CustomUI.this, "Message posted",Toast.LENGTH_LONG).show();
    else
    Toast.makeText(CustomUI.this, "Message not posted",Toast.LENGTH_LONG).show();
   }

   @Override
   public void onError(SocialAuthError e) {
   }
}

```

## Get Profile ##
```

profileMap =  adapter.getUserProfile();
				
Log.d("Custom-UI",  "Validate ID         = " + profileMap.getValidatedId());
Log.d("Custom-UI",  "First Name          = " + profileMap.getFirstName());
Log.d("Custom-UI",  "Last Name           = " + profileMap.getLastName());
Log.d("Custom-UI",  "Email               = " + profileMap.getEmail());
Log.d("Custom-UI",  "Gender  	         = " + profileMap.getGender());
Log.d("Custom-UI",  "Country  	         = " + profileMap.getCountry());
Log.d("Custom-UI",  "Language  	         = " + profileMap.getLanguage());
Log.d("Custom-UI",  "Location 	         = " + profileMap.getLocation());
Log.d("Custom-UI",  "Profile Image URL  = " + profileMap.getProfileImageURL());
```


## Get Contacts ##
```
List<Contact> contactsList = adapter.getContactList();
						
if (contactsList != null && contactsList.size() > 0) 
{
   for (Contact p : contactsList) 
   {
	if (TextUtils.isEmpty(p.getFirstName()) && TextUtils.isEmpty(p.getLastName())) 
	{						    
            p.setFirstName(p.getDisplayName());
	}
						
	Log.d("Custom-UI" , "Display Name = " +  p.getDisplayName());
	Log.d("Custom-UI" , "First Name   = " +  p.getFirstName());
	Log.d("Custom-UI" , "Last Name    = " +  p.getLastName());
	Log.d("Custom-UI" , "Contact ID   = " +  p.getId());
	Log.d("Custom-UI" , "Profile URL  = " +  p.getProfileUrl());	
						
   }	
}
```