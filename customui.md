# Custom UI #

This option lets you implement providers in your way. You can show them either in ListView , GridView , Context Menu , Dialog or any way you want.
Here is example of how to use library with own List View:

![https://socialauth-android.googlecode.com/svn/wiki/images/socialauthcontacts.png](https://socialauth-android.googlecode.com/svn/wiki/images/socialauthcontacts.png)


**Here is example how to use custom-ui with listview :**
```
    // Adapter initialization
    adapter = new SocialAuthAdapter(new ResponseListener());
        
    listview = (ListView)findViewById(R.id.listview);  
    listview.setAdapter(new CustomAdapter(this, adapter));   
```


```

holder.text.setOnClickListener(new OnClickListener() {

@Override
public void onClick(View v) {

    // providers array contains name of providers
    adapter.authorize(context, providers[position]);

   }
});



```

## Sign Out ##
The sign out functionality now sign out the provider.Fixed the bug in V 1.0

(V2.0 Supports : All)
```

adapter.signout();

```

## Share Message and Links ##
(Supports : Facebook, Twitter, Linkedin, MySpace, Yahoo, Yammer)

![https://socialauth-android.googlecode.com/svn/wiki/images/sharedata.png](https://socialauth-android.googlecode.com/svn/wiki/images/sharedata.png)

```

private final class ResponseListener implements DialogListener 
{
   public void onComplete(Bundle values) {
   
      adapter.updateStatus(edit.getText().toString(), new MessageListener(),false);			
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

## Update Message with Link Preview ##
(Supports : Facebook)
```

adapter.updateStory(
		"Hello SocialAuth Android" + System.currentTimeMillis(),
		"Google SDK for Android",
		"Build great social apps and get more installs.",
	        "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.",
		"https://www.facebook.com",  
                "http://carbonfreepress.gr/images/facebook.png",
		new MessageListener());
```


## Get Profile ##
(Supports : All)

![https://socialauth-android.googlecode.com/svn/wiki/images/profile.png](https://socialauth-android.googlecode.com/svn/wiki/images/profile.png)

```

private final class ResponseListener implements DialogListener 
{
   public void onComplete(Bundle values) {
   
      adapter.getUserProfileAsync(new ProfileDataListener());			
   }
}

// To receive the profile response after authentication
private final class ProfileDataListener implements SocialAuthListener<Profile> {

@Override
public void onExecute(Profile t) {

   Log.d("Custom-UI", "Receiving Data");
   Profile profileMap = t;
   Log.d("Custom-UI",  "Validate ID         = " + profileMap.getValidatedId());
   Log.d("Custom-UI",  "First Name          = " + profileMap.getFirstName());
   Log.d("Custom-UI",  "Last Name           = " + profileMap.getLastName());
   Log.d("Custom-UI",  "Email               = " + profileMap.getEmail());
   Log.d("Custom-UI",  "Gender  	         = " + profileMap.getGender());
   Log.d("Custom-UI",  "Country  	         = " + profileMap.getCountry());
   Log.d("Custom-UI",  "Language  	         = " + profileMap.getLanguage());
   Log.d("Custom-UI",  "Location 	         = " + profileMap.getLocation());
   Log.d("Custom-UI",  "Profile Image URL  = " + profileMap.getProfileImageURL());
}

```


## Get Contacts ##

![https://socialauth-android.googlecode.com/svn/wiki/images/contact.png](https://socialauth-android.googlecode.com/svn/wiki/images/contact.png)

```
private final class ResponseListener implements DialogListener 
{
   public void onComplete(Bundle values) {
   
      adapter.getContactListAsync(new ContactDataListener());			
   }
}

// To receive the contacts response after authentication
private final class ContactDataListener implements SocialAuthListener<List<Contact>> {

@Override
public void onExecute(List<Contact> t) {

   Log.d("Custom-UI", "Receiving Data");
   List<Contact> contactsList = t;
  if (contactsList != null && contactsList.size() > 0) {
  for (Contact c : contactsList) {
    Log.d("Custom-UI", "Contact ID = " + c.getId());
    Log.d("Custom-UI", "Display Name = " + c.getDisplayName());
    Log.d("Custom-UI", "First Name = " + c.getFirstName());
    Log.d("Custom-UI", "Last Name = " + c.getLastName());
    Log.d("Custom-UI", "Email = " + c.getEmail());
    }
}

```

## Get Feeds ##
(Supports : Facebook, Twitter)

Get Feeds from your Facebook , Twitter Account.

![https://socialauth-android.googlecode.com/svn/wiki/images/feed.png](https://socialauth-android.googlecode.com/svn/wiki/images/feed.png)

```
private final class ResponseListener implements DialogListener 
{
   public void onComplete(Bundle values) {
   
     adapter.getFeedsAsync(new FeedDataListener());		
   }
}

// To receive the feed response after authentication
private final class FeedDataListener implements SocialAuthListener<List<Feed>> {

 @Override
 public void onExecute(List<Feed> t) {

 Log.d("Custom-UI", "Receiving Data");
			
 List<Feed> feedList = t;
 if (feedList != null && feedList.size() > 0) {
 for (Feed f : feedList) {
     Log.d("Custom-UI", "Feed ID = " + f.getId());
     Log.d("Custom-UI", "Screen Name = " + f.getScreenName());
     Log.d("Custom-UI", "Message = " + f.getMessage());
     Log.d("Custom-UI", "Get From = " + f.getFrom());
     Log.d("Custom-UI", "Created at = " + f.getCreatedAt());
     }
 }			
		

 @Override
 public void onError(SocialAuthError e) {

 }
}



```

## Get Albums ##
(Supports : Facebook, Twitter)

Get Albums from your Facebook , Twitter Account.

![https://socialauth-android.googlecode.com/svn/wiki/images/album.png](https://socialauth-android.googlecode.com/svn/wiki/images/album.png)

```

private final class ResponseListener implements DialogListener 
{
   public void onComplete(Bundle values) {
   
     adapter.getAlbumsAsync(new AlbumDataListener());	
   }
}

// To receive the album response after authentication
private final class AlbumDataListener implements SocialAuthListener<List<Album>> {

@Override
public void onExecute(List<Album> t) {

  Log.d("Custom-UI", "Receiving Data");
  List<Album> albumList = t;
  if (albumList != null && albumList.size() > 0) {

  // Get Photos inside Album
  for (Album a : albumList) {

  Log.d("Custom-UI", "Album ID = " + a.getId());
  Log.d("Custom-UI", "Album Name = " + a.getName());
  Log.d("Custom-UI", "Cover Photo = " + a.getCoverPhoto());
  Log.d("Custom-UI", "Photos Count = " + a.getPhotosCount());

  photosList = a.getPhotos();
  if (photosList != null && photosList.size() > 0) {

  for (Photo p : photosList) {
        Log.d("Custom-UI", "Photo ID = " + p.getId());
	Log.d("Custom-UI", "Name     = " + p.getTitle());
	Log.d("Custom-UI", "Thumb Image = " + p.getThumbImage());
	Log.d("Custom-UI", "Small Image = " + p.getSmallImage());
	Log.d("Custom-UI", "Medium Image = " + p.getMediumImage());
	Log.d("Custom-UI", "Large Image = " + p.getLargeImage());
       }
     }
  }
}

  @Override
  public void onError(SocialAuthError e) {
  }
}


```


## Upload Photo ##
(Supports : Facebook, Twitter)

![https://socialauth-android.googlecode.com/svn/wiki/images/shareupdate.png](https://socialauth-android.googlecode.com/svn/wiki/images/shareupdate.png)

Now you can upload photo to Facebook and Twitter. We have given two examples to get images

  * Get Image from gallery and create bitmap to post (share-button example)
  * Get Image from drawables and create bitmap to post ( share-bar and custom-ui example)

Then use below code to post image with description or status.

```
	
private final class ResponseListener implements DialogListener 
{
   public void onComplete(Bundle values) {
   
     adapter.uploadImageAsync(edit.getText().toString(), "icon.png", bitmap, 0,new UploadImageListener());
   }
}

// To get status of image upload after authentication
private final class UploadImageListener implements SocialAuthListener<Integer> {

@Override
public void onExecute(Integer t) {
  Log.d("Custom-UI", "Uploading Data");
  Integer status = t;
  Log.d("Custom-UI", String.valueOf(status));
  Toast.makeText(CustomUI.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
}

@Override
public void onError(SocialAuthError e) {
 }
}

```


> To see the functionality, view **custom-ui example** from socialauth-android zip on Download Page.