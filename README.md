# Compra Rápida

There are currently several applications available for grocery shopping for the Android platform. Most of these applications are a digital version of the traditional paper list. In other words, they provide facilities to assemble the shopping list, with an easy inclusion of items and history record, but at the time of purchases, when the user goes to the market to effectively use this list, the products are listed in the order of insertion, or grouped by categories. Therefore, this project aims to help the user during the purchases, helping him to pick the products in the best possible order, by arranging the sections present in the supermarket.

## Starting

The instructions here will assist you in a functional development version of this project.

### Requirements

* Android Studio
* Firebase account

### Installing

Clone the repository
```
git clone https://github.com/ifsc-saojose/compra-rapida-android.git
```
### Config GoogleMaps Location service

Insert your Android GEO API KEY on AndroidManifest.xml file.
The Android GEO API Key can be obtained from [here](https://developers.google.com/maps/documentation/geocoding/get-api-key)
```
   <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value=" < YOUR-API-KEY > " />
```
### Config Firebase Service
In order to integrate the firebase with the Android project, it`s recommended to use the Firebase Assistant in Android Studio:

Click on:
```
Tools > Firebase to open the Assistant window.
```
Click to expand the option Realtime Database, and select "click and retrieve data" and follow the instructions to connect the app to Firebase.

Also, click to expand the option Storage and make sure to connect the Storage services as well.

### Firebase schema

In order to have the same products used in this application, it's necessary to import the JSON structure with products and categories in your real time database on the firebase.
A copy of the schema used in this application is available at the firebase folder located at the root of this repository.

### Firebase file storage

The images used on this application was stored on firebase storage services. In order to use the same images of this application, you need to create a folder on your firebase storage named products to insert all images available at firebase folder on this repository into your firebase storage account.
Noticed that the images on the firebase folder have the same name of the ID of the products.


## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Retrofit](http://square.github.io/retrofit/) - HTTP API into a Java interface.
* [Firebase](https://firebase.google.com/) - Real-time database and file storage
* [Glide](https://github.com/bumptech/glide/) - Image loader

## Authors

* **Beatriz da Silveira** - *Initial work* - [beatriz1304](https://github.com/beatriz1304)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* IFSC Campus São José 
