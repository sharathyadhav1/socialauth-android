## Known Issues and Solutions ##

**General**
  * All the providers disallow same message to be post again to avoid spamming.You may get error if you try to post same message.
  * Facebook requires sending appID. Use adaptre.addAppplicationID() method. Generate Keyhash and add in your facebook app account.
  * Google, Twitter, Foursquare, Yammer , Salesforce requires user callback url defined in account. use addCallback method for this.

  * Officially we support SDK 2.2. The API will work on SDK 2.1 too except Linkedin.

  * If you get this error while importing projects :

> _Project has no default.properties file! Edit the project properties to set one._

> Check your ADT version and upgrade to latest one.
> To check :
> Help -> About Eclipse -> Installation Details -> Installed Software Tab -> Android Development Tools

  * You may get **UnknownHostException** while testing on emulator or device.Here are possible solutions:
    * Check you have inserted android.permission.Internet under manifest tag of your androidmanifest.xml.
    * For device , check your internet connection. Is it on?
    * For emulator , close it. Relaunch the emulator.
    * If relaunching the emulator not solves the issue, then create new      avd and launch it.


**Facebook**
  * Facebook shows different language sometimes. This is a bug filed to FaceBook.Here is the link https://developers.facebook.com/bugs/407246299295529?browse=search_4fa410ea79db26337556383
  * You may get already authorized screen which is due to recent api changes from Facebook. Press Ok to continue.
  * You may get error AppId is null, use adapter.appID method.
  * You may get error app is not set up properly.  Click status tab in your facebook account and make it public for all users.
  * Don't forget to add key hash.

**Twitter**
If you find issue in twitter authentication , you can try following things :

  * Check read-write permission in your twitter account
  * The callback url field should be field and it should be valid url.
  * Check you have used addCallback method. This is due to recent changes in twitter api.

**Linkedin**
  * The callback url field should be valid url.
  * You should remove your old app before using the new lib

**Yahoo**
  * If you get URL authentication error for Yahoo. Try to clear you app settings and then login again.

**Yammer**

  * Yammer oAuth screen is not mobile optimized. Although we have tried to do some tweaks in webview but you may face issue in some phones.
  * Yammer provides oAuth access to network members only.

**Runkeeper**
  * Runkeeper oAuth Screen is partially optimized. We have done optimization at our end.
  * Runkeeper takes time to open in emulater due to low rendering speed of emulater.