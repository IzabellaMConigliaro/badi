# Badi Android Coding challenge

The purpose of this challenge is to test the applicant capabilities and, by touching different elements of an existing app, get an idea of how he/she would adapt under different circumstances (existing code, UI, app logic, API communication…) before entering at [Badi](http://badiapp.com/). 

It's based on the architecture, tools and guidelines that we use when developing for the Android platform 

Libraries and tools included:

- [Android support and design](https://developer.android.com/topic/libraries/support-library/index.html) (Support-v4/v7/v13/annotations, Appcompat, Recycler view, Card View, Design, Custom tabs, Animated vector drawable and ExifInterface)
- [Google Play services](https://developers.google.com/android/guides/overview) (GCM, Location, Analytics, Maps, Places and Authentication)
- [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid) 
- [Retrofit 2](http://square.github.io/retrofit/)
- [Dagger 2](http://google.github.io/dagger/)
- [AutoValue](https://github.com/google/auto/tree/master/value) with [Gson](https://github.com/rharter/auto-value-gson) and [Parcelable](https://github.com/rharter/auto-value-parcel) extensions
- [Butterknife](https://github.com/JakeWharton/butterknife)
- [Glide](https://github.com/bumptech/glide)
- [CircleImageView](https://github.com/hdodenhof/CircleImageView)
- [Sub Sampling Scale Image View](https://github.com/davemorrissey/subsampling-scale-image-view)
- [CircleIndicator](https://github.com/ongakuer/CircleIndicator)
- [Calligraphy](https://github.com/chrisjenx/Calligraphy)
- [Easy Image](https://github.com/jkwiecien/EasyImage)
- [Material Date Time Picker](https://github.com/wdullaer/MaterialDateTimePicker)
- [Timber](https://github.com/JakeWharton/timber)
- [Mockito](http://mockito.org/)
- [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/)
- [Crashlytics](https://try.crashlytics.com/)
- [Checkstyle](http://checkstyle.sourceforge.net/), [PMD](https://pmd.github.io/) and [Findbugs](http://findbugs.sourceforge.net/) for code analysis

## Requirements

- JDK 1.8
- [Android SDK](http://developer.android.com/sdk/index.html)
- Min SDK - Android Jelly Bean 4.1 [(API 16)](https://developer.android.com/about/versions/jelly-bean.html)
- Target SDK - Android Oreo 8.1 [(API 27)](https://developer.android.com/about/versions/oreo/index.html)
- Latest Android SDK Tools and build tools.


## Architecture

This project follows badi's Android architecture guidelines that are based on [MVP (Model View Presenter)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) and [Clean Architecture](http://fernandocejas.com/2015/07/18/architecting-android-the-evolution/).

It also follows patterns found on [Android Architecture Blueprints sample by Google](https://github.com/googlesamples/android-architecture).

## Code Quality

This project integrates a combination of unit tests, functional test and code analysis tools. 

### Tests

To run **unit** tests on your machine:

``` 
./gradlew test
``` 

To run **functional** tests on connected devices:

``` 
./gradlew connectedAndroidTest
``` 
