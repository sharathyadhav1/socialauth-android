![https://socialauth-android.googlecode.com/svn/wiki/images/android.png](https://socialauth-android.googlecode.com/svn/wiki/images/android.png)

## For latest source code and wiki, please visit <a href='https://github.com/3pillarlabs/socialauth-android/'>SocialAuth Android on GitHub</a> ##

## Click here to <a href='http://sourceforge.net/projects/socialauth-android/files/'>Download SocialAuth-Android-3.2-SDK</a> ##


SocialAuth Android is an Android version of popular SocialAuth Java library. Now you do not need to integrate multiple SDKs if you want to integrate your application with multiple social networks.  You just need to add few lines of code after integrating the SocialAuth Android library in your app.


The API enables user authentication and sharing updates through different various social networks and hides all the intricacies of generating signatures & token, doing security handshakes and provide an easy mechanism to build cool social apps.



With this library, you can:

  * Quickly build share functionality for posting updates on facebook, twitter, linkedin and more
  * Easily create a Share button or a social bar containing various social networks
  * Access profile of logged in user for easy user registration
  * Import friend contacts of logged in user (Email, Profile URL and Name)
  * Do much more using our flexible API like extend it for more network


## Whats new in Version 3.2 ? ##

 Bugs Solved : Facebook issue solved. Now we are using native web view login. please check the wiki to create Facebook app using native flow.
 Bugs Solved : Foursquare issue fixed
 Bugs Solved : Signout bug fixed
 Bugs Solved : Custom -UI example upload image bug fixed
 Documentation Guides for Facebook, Google Plus, Flickr , Share-Menu and more

## Whats new in Version 3.1 ? ##
This is an incremental release with twitter api fix

## Whats new in Version 3.0 ? ##
  * **New Providers Support** : Instagram , Flickr
  * **New Example** : Share- Menu - Now use provides in Android ShareAction Provider. Check wiki and example for use.
  * **Contacts :** Support added for Google Plus, Flickr , Instagram
  * **Feeds    :** Support added for Google Plus, Instagram
  * **Albums   :** Support added for Google Plus. Download Picasa Albums
  * **Generic OAuth2 Provider** : Users can create own oAuth2 Providers from sdk.
  * **Bugs Solved :** Publish Story bug for Facebook Solved
  * **Bugs Solved :** Get Profile Images for FourSquare
  * **Bugs Solved :** UI issues for Yahoo , Yammer Solved

Check examples for use.

## Existing Features ##
  * **Providers Support :** Facebook, Twitter, Linkedin, MySpace, FourSquare , Google , Google Plus, Runkeeper, Yammer , Yahoo
  * **User Profile :** Get User Profile -  All Providers
  * **Get Contacts :** Get User Contacts - Facebook , Twitter , Linkedin, MySpace , Yahoo , Yammer, Google, FourSquare
  * **Update Status :** Share Status - Facebook, Twitter, Linkedin, MySpace, Yahoo, Yammer
  * **Upload Images :** Share Images - Facebook, Twitter
  * **Feed Plugin :** Show User Feeds - Facebook , Twitter, Linkedin
  * **Career Plugin :** Show information for Job, Education, Recommendations - Linkedin
  * **Update Story :** Can update status with image preview and links - Facebook
  * **Multiple Update Status** : Can Shares status to multiple providers at once.
  * **SDK Support :** From SDK 2.2
  * **URL Encoding for Status Update :** Post messages in different languages.
  * **Email Provider :** Send Email from Native Email Clients
  * **MMS Provider :** Send Message from native Messaging Clients
  * **AddConfig Method :** Enable users to keep keys and secrets in code.
  * **API Method :** Create custom api methods easily.
  * **Access Token:** Library saves access token of providers
  * **Downloading:** Downloads content asynchrously. Synchronous methods also available
  * **More :** Support for Google TV


The SocialAuth Android SDK is a single download containing our library, ready examples to understand the functionality.

**Be part of us by suggesting new features or reporting any issues!**

## Getting Started ##
  1. Get the API Keys and secrets for providers you want to integrate in app. You can take help from following URLs :
    * Twitter  - [(Show Screenshot)](Twitter.md)
    * Facebook - [(Show Screenshot)](Facebook.md)
    * MySpace  - [(Show Screenshot)](MySpace.md)
    * Linkedin - [(Show Screenshot)](Linkedin.md)
    * Yahoo      - [(Show Screenshot)](Yahoo.md)
    * Google     - [(Show Screenshot)](Google.md)
    * SalesForce - [(Show Screenshot)](SalesForce.md)
    * Foursquare - [(Show Screenshot)](Foursquare.md)
    * Yammer     - [(Show Screenshot)](Yammer.md)
    * Runkeeper  - [(Show Screenshot)](Runkeeper.md)
  1. Download SocialAuth-android.zip from [Downloads](http://code.google.com/p/socialauth-android/downloads/list) page.
  1. Use [Getting Started](http://code.google.com/p/socialauth-android/wiki/GettingStarted) page to view the steps to integrate library.
  1. See Examples in zip file to view implementation


## How does it Work? ##

Once SocialAuth Android is integrated into your application, following is the authentication process:

  1. User opens the app and chooses the provider to request the authentication by using SocialAuth-android library.
  1. User is redirected to Facebook, Twitter or other provider's login site by library where they enter their credentials.
  1. Upon successful login, provider asks for user’s permission to share their basic data with your app.
  1. Once user accepts it,On successful authentication the library redirects user to app.
  1. Now user can call SocialAuth Android library to get information about user profile, gets contacts list or share status to friends.

![https://socialauth-android.googlecode.com/svn/wiki/images/socialauthandroid-process.png](https://socialauth-android.googlecode.com/svn/wiki/images/socialauthandroid-process.png)


For any help mail us at : labs@3pillarglobal.com

## Share it with your Friends!!! ##
&lt;wiki:gadget url="http://hosting.gmodules.com/ig/gadgets/file/113501407083818715381/Opti365ShareWebpageHelper.xml" border="0"/&gt;