# Getting Started #

## Step 1 : Prerequisites ##
  * Get Latest Android SDK and ADT plugin installed from http://developer.android.com/tools in your eclipse.
  * Get the API Keys and secrets for providers you want to integrate in app. You can take help from following URLs :
    * Twitter    - [(Show Screenshot)](Twitter.md)
    * Facebook   - [(Show Screenshot)](Facebook.md)
    * MySpace    - [(Show Screenshot)](MySpace.md)
    * Linkedin   - [(Show Screenshot)](Linkedin.md)
    * Yahoo      - [(Show Screenshot)](Yahoo.md)
    * Google     - [(Show Screenshot)](Google.md)
    * SalesForce - [(Show Screenshot)](SalesForce.md)
    * Foursquare - [(Show Screenshot)](Foursquare.md)
    * Yammer     - [(Show Screenshot)](Yammer.md)
    * Runkeeper  - [(Show Screenshot)](Runkeeper.md)
    * Google Plus( Google OAuth 2.0) - [(Show Screenshot)](GooglePlus.md)
    * Flickr - [(Show Screenshot)](Flickr.md)

  * Replace the keys and secrets in oauth\_consumer.properties file with your keys and secrets.

## Step 2 : Know SDK ##

The SDK contains following directories :

  * dist -  This contains socialauth-android-3.2.jar containing Android components.You need to include it in your app.

  * libs -  This contains socialauth-4.4.jar.  The socialauth-4.4.jar is the underlying OAuth infrastructure.You need to include it in your app.

  * assets - Contains the OAuth consumer keys and secrets, that you can get by registering your application with facebook, linkedin, twitter and myspace and other providers. We have bundled our keys so that you can test quickly, but it is strongly recommended that you change these keys. First, it is a security issue for your application and secondly sometimes our keys give errors  because too many developers are testing.

  * src - This contains the entire socialauth-android Eclipse project if you would like to debug or you need to make some changes.

  * examples - This contains three examples that we have built for you showing how the SDK can be used to build a Share button, Share bar or a completely custom UI.

  * javadoc - Documentation for SocialAuth Android.

## Step 3 : Integrating SDK ##

  * Create your Android project.

  * Add socialauth-android-3.2.jar and socialauth4.4.jar from dist and libs folder of SDK zip file  to libs folder of you app.

  * If you are using ADT17 or more then you need to rename the lib folder in your app to libs. Your app will add the jar to build path automatically if you are using ADT 17.

  * Add images of providers to drawables. You can also take images from apps in examples folder of SDK zip file.

  * Add oauth\_consumer.properties file from assets folder of SDK zip file in assets folder of your application.

  * Move to your project properties -> Java Build Path -> Order and Export. Move the socialauth libs on top and export. Check examples in case you find issues else you may get noClassDefFound Error.

## Step 3 : Adding Permissions in Manifest File ##
```
 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
```

## Step 4 : Adding Config changes attributes ##
If your app uses both portrait and landscape modes , we recommend you to use follow settings in activity tag.
```
android:configChanges="keyboardHidden|orientation"
```


## Step 5 : Implementation ##

You can use library in following three ways  :

  * By creating a Share Button that will call a dialog to show list of all providers. See [(sharebuttonexample)](sharebuttonexample.md) page for implementation.
  * By creating a Share Bar containing list of all providers that can be launch on single click. See [(sharebarexample)](sharebarexample.md) page for implementation.
  * By creating Custom UI and implement providers in your own way.See [(customui)](customui.md) page for implementation.
  * By using it as Action-bar menu. Use share-menu example.

## Demo Apps ##
CustomUI : The app demonstrates how you can access different functionalists of specific provider.

## Please Note ##

SocialAuth Android 3.2 supports Facebook , Twitter , MySpace , Linkedin , Google, FourSquare ,SalesForce , Yahoo, Yammer, Runkeeper, Google Plus(Google OAuth2.0), Instagram , Flicker

**SocialAuth Android 3.2 not supports below SDK 2.2 Froyo**

