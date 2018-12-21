Intercom allows software businesses to chat with prospective and existing customers within their app, on their website, through social media, or via email. It is kind of customer care service.

Steps to Integrate :

1. Add intercom in build.gradle file

compile 'io.intercom.android:intercom-sdk-base:5.+'
compile 'io.intercom.android:intercom-sdk-fcm:5.+'


2. Initialize Intercom

you need to get your Intercom app ID and Android API key. To find these, just select the 'Intercom for Android' option in your app settings at
https://app.intercom.io/a/apps/iqsm455a/users/segments/active


3. In Application class add following line in onCreate() method

Intercom.initialize(this, BuildConfig.INTERCOM_API_KEY, BuildConfig.INTERCOM_APP_ID);


4. Register user in BaseActivity.java

- In onCreate()

Intercom.client().registerIdentifiedUser(new Registration().withUserId(“your_email_id”));

- Under on Resume
Intercom.client().handlePushMessage();

after that intercom icon will appear on bottom left corner of device


5. To Logout user session:

  Intercom.client().logout();

6. Some other features

To hide icon: Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
To adjust position : Intercom.client().setBottomPadding(60);


Reference link : https://developers.intercom.com/installing-intercom/docs/android-installation
