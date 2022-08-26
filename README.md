# Converter: PDF to MP3

![GitHub commit activity](https://img.shields.io/github/commit-activity/m/he1ex-tG/PdfReader?logo=GitHub) ![GitHub last commit](https://img.shields.io/github/last-commit/he1ex-tG/PdfReader?logo=GitHub) ![GitHub issues](https://img.shields.io/github/issues/he1ex-tG/PdfReader?logo=GitHub) ![GitHub pull requests](https://img.shields.io/github/issues-pr/he1ex-tG/PdfReader?logo=GitHub) ![GitHub](https://img.shields.io/github/license/he1ex-tG/PdfReader?logo=GitHub)

## Note
This is an educational project aimed at gaining practical experience in 
working with the Spring Framework.  

## About what

This project is a service developed to convert PDF files to MP3 audio format.
It consists of two components:

- [Frontend](#frontend) 
- [Backend](#backend)

You can get the conversion result using both the web interface and the 
REST API. It also provides the opportunity to store the converted files on 
the server. 

## Technologies used:

1. [Spring Boot](https://spring.io/projects/spring-boot)
2. [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
3. [Thymeleaf](https://www.thymeleaf.org/)
4. [Nicepage - For autogenerate html and css](https://nicepage.com/)
5. [H2 Database](https://www.h2database.com/html/main.html)
6. [ITextPDF](https://itextpdf.com/)
7. [FreeTTS](https://freetts.sourceforge.io/)
8. [Lame](https://lame.sourceforge.io/)

## Frontend

The main page looks like this:

!!! ДОБАВИТЬ ЧТО-ТО ТИПА "КАРТИНКА ДЛЯ КРАСОТЫ"
[Screenshot here]

As you can see, the interface is quite simple and straightforward. The PDF 
file is selected via a dialog box. The conversion process starts after 
pressing the "Convert" button. The produced MP3 files are displayed in the 
list below. Each file can be played on the page or downloaded to local 
storage.

I don't really enjoy creating UI, so I used [Nicepage](https://nicepage.com/) 
to create the pages. By doing this, I visualized the process of creating html 
pages and saved myself from having to manually write styles. Additionally, 
NicePage creates suitable styles for correct display of http pages on mobile 
devices, tablets, etc.

To process the results of requests to the REST API and display them on the 
page, I used [Thymeleaf](https://www.thymeleaf.org/): displaying a list of 
files and automatically creating links.

## Backend

The entire Backend is written in Kotlin. It can be divided into several main 
parts: [API](#1-api), [file storage](#2-file-storage), [converter](#3-converter).

### 1. API

The API is built using the features provided by [Spring Boot](https://spring.io/projects/spring-boot). 
It provides some endpoints that can be used by third party services:
>- GET /files: Get a list of files uploaded by the user
>- GET /files/fileName: Download file
>- POST /files: Upload PDF file, convert it and store on serverside
>- POST /file: Upload PDF file, convert it and download result without storing

****************************** Добавить про RestController

The API interacts with methods declared in the StorageHandler interface.

### 2. File storage

As mentioned above, work with file storage is carried out through the 
interface. This makes it possible to choose how files are stored. In the 
first edition, files were stored in a directory on the file system. Later, 
I added another implementation of the interface. The files are now stored in
the H2 database. Both implementations are currently available and can be 
used by specifying the appropriate __profile__: "filestorage" or "h2database".

The use of the second implementation (__profile__: "h2database") is
preferred because In addition to files, the database stores user data. 
This makes it possible to determine the ownership of files by a particular 
user, and is also necessary (in the future) for user authentication and
authorisation.

Interaction with [H2 Database](https://www.h2database.com/html/main.html) is 
implemented using [Spring Data JPA](https://spring.io/projects/spring-data-jpa):
the necessary entities are created, as well as repositories for working with 
these entities.

### 3. Converter

Converting a PDF file into text (array of bytes) is done using the 
[ITextPDF](https://itextpdf.com/) library. Then the text is converted to audio - 
the [FreeTTS](https://freetts.sourceforge.io/) library is used for this. 
Here is a small note: by default, FreeTTS does not provide the ability
output the audio stream as a ByteArrayInputStream or ByteArray, for example. 
Therefore, I made my own implementation of the AudioPlayer interface. 
This approach made it possible to abandon the use of intermediate saving of 
audio data to a file, as well as to hot convert from WAV to MP3 on the fly using 
[Lame](https://lame.sourceforge.io/). Finally, the MP3 audio stream is used to 
save or give to the user without saving.

## TODO

- [x] Add WAV to MP3 conversion
- [x] Add using database instead of storing MP3 in files
- [ ] Add authorization and authentication (Spring Security)
  - [ ] Add the corresponding REST API
  - [ ] Rework database structure
- [ ] Move the user web interface to a separate project









> You can visit my Android bot of Granblue Fantasy for more usage details and examples of how to use this framework: https://github.com/steve1316/granblue-automation-android

### Some additional usage examples at:

-   [Uma Android Training Helper](https://github.com/steve1316/uma-android-training-helper)
-   [Uma Android Automation](https://github.com/steve1316/uma-android-automation)
-   [Master Duel Decklist Importer](https://github.com/steve1316/masterduel-android-decklist-importer)
-   [Girls' Frontline Android Auto](https://github.com/steve1316/gfl-android-auto)

This template project serves as a starter point for Android botting or as a general framework that relies on Computer Vision template matching via OpenCV and executing gestures like tapping and scrolling to accomplish a automation goal. It uses MediaProjection Service to programmatically take screenshots and the Accessibility Service to execute gestures. The framework is well annotated with documentation to serve as explanations and usage hints.

https://user-images.githubusercontent.com/18709555/118407909-c933be00-b637-11eb-92c2-3c4acd355aff.mp4

# Provided Features

-   A Home page that also houses the Message Log to allow the user to see informational logging messages.
-   A Settings page that utilizes SharedPreferences to share data across the application and its associated Services.
-   Floating overlay button to issue START/STOP signals to the bot process.
-   Fleshed out template matching functions via OpenCV
-   Notifications to alert users of various changes during workflow.
-   Accessibility Service that will allow the bot process to execute gestures on the screen like tapping a specific point.
-   Automatically checking for new app updates from your designated GitHub repository.
-   Ability for sending you messages via Discord private DMs.

# Requirements

1. [Android Device or Emulator (Nougat 7.0+)](https://developer.android.com/about/versions)
    1. (Experimental) Tablets supported with minimum 1600 pixel width like the Galaxy Tab S7. If oriented portrait, browsers like Chrome needs to have Desktop Mode turned off and situated on the left half of the tablet. If landscape, browsers like Chrome needs to have Desktop Mode turned on and situated on the left half of the tablet.
    2. Tested emulator was Bluestacks 5 with the following settings:
        - P64 (Beta)
        - 1080x1920 (Portrait Mode as emulators do not have a way to tell the bot that it rotated.)
        - 240 DPI
        - 4+ GB of Memory

# Instructions

1. Download the project and extract.
2. Go to `https://opencv.org/releases/` and download OpenCV 4.5.1 (make sure to download the Android version of OpenCV) and extract it.
3. Create a new folder inside the root of the `android` folder named `opencv` and copy the extracted files in `/OpenCV-android-sdk/sdk/` from Step 2 into it.
4. Build the Javascript portion of the project by running `yarn install` in the root of the project folder as well.
5. You can now build and run on your Android Device or create your own .apk file.
6. `Clean Project` and then `Rebuild Project` under the Build menu.
7. After building is complete, you can test the capability of this framework in the Android Studio's emulator.
8. After you familiarized yourself with what the framework can do, you can refactor the application's package name in various parts of the project and in `settings.gradle`. Also adjust the app name in `app.json` and in `strings.xml`.
9. If you want your application automatically check for the latest updates from your GitHub repo using `AppUpdater`, you can either reuse the already provided `app/update.xml` in the template or do the following:
    1. Upload a .xml file to your Github repo using the provided example `app/update.xml` with your updated version number, release notes, and link to the `Releases` page of your GitHub repo.
    2. Update the `setUpdateXML()` with the `RAW` link to your new update.xml.
    3. Now when a user has a lower version number compared to the latest version in your `Releases` page in your GitHub repo, they will be prompted with a dialog window like this:

> ![i_view32_cyUHsXlLFG](https://user-images.githubusercontent.com/18709555/125871637-0a803f09-fbc3-49b9-ae39-1a77cf64bbf3.png)

### Some things to note while developing

1. `ImageUtils` class reads in images in `.webp` format to reduce apk file size. You can change this if you wish.
2. All images are recommended to be categorized in separate folders inside the /assets/ folder. Be sure to update the `folderName` variables inside the various functions in `ImageUtils`. Or you could remove the need to organize them and just put all image assets into one place. Just make sure to update the code references to the `folderName` variables.
3. When working on a horizontal screen, the coordinate axis gets flipped as well. So if your vertical orientation dimensions is 1080x2400, then the horizontal orientation dimensions gets flipped to 2400x1080.
4. (on the `old` branch) If you want to create nested Fragment Preference settings, there is an example provided to showcase how to do that in SettingsFragment.kt and mobile_navigation.xml.

# Important Classes to be familiar with

## BotService

-   Facilitates the display and moving of the floating overlay button.
-   Able to start/stop the bot process on a new Thread and notify users of bot state changes like Success or Exception encountered.

## ImageUtils

-   Able to template match for a single image or multiple image locations on the screen.
-   Able to detect text via Google's ML Kit (Read up on the Google's documentation and my usage of it at `https://github.com/steve1316/granblue-automation-android/blob/main/app/src/main/java/com/steve1316/granblueautomation_android/utils/ImageUtils.kt` for a better understanding of it)

## MediaProjectionService

-   Starts up the MediaProjection Service to allow the `ImageUtils` class to programmatically grab screenshots to perform template matching on it.

## MessageLog

-   Sends informational logging messages from the `Game` class to the Home page of the application to quickly view what is going on.
-   Automatically saves logs into text files when the bot stops.

## MyAccessibilityService

-   Starts up the Accessibility Service to allow the `Game` class to execute gestures at specified (x,y) coordinates on the screen.
    -   Supported gestures are: tap, swipe, and scroll via AccessibilityAction.
-   Note: If you encounter this Exception: `kotlin.UninitializedPropertyAccessException: lateinit property instance has not been initialized`, it means that you must have terminated the application via Android Studio. This causes the Accessibility Service to bug out. This does not happen in regular use without interference from Android Studio. To fix this, you can toggle on/off the Accessibility Service until the Toast message pops back up signalling that the Service is now running properly again.

## JSONParser

-   Reads in data and settings from a .json file which you can then choose to store however you like, whether it be stored in a static object or put into the SharedPreferences.

## NotificationUtils

-   Allows the bot process to create and update Notifications to notify users of the status of the bot and whether or not the bot encounters an Exception.
-   Sends a STOP signal to the bot process from the Notification's button via the `StopServiceReceiver` class.

# Technologies used

1. [MediaProjection - Used to obtain full screenshots](https://developer.android.com/reference/android/media/projection/MediaProjection)
2. [AccessibilityService - Used to dispatch gestures like tapping and scrolling](https://developer.android.com/reference/android/accessibilityservice/AccessibilityService)
3. [OpenCV Android 4.5.1 - Used to template match](https://opencv.org/releases/)
4. [Tesseract4Android 2.1.1 - For performing OCR on the screen](https://github.com/adaptech-cz/Tesseract4Android)
5. [Google's Firebase Machine Learning OCR for Text Detection](https://developers.google.com/ml-kit)
6. [AppUpdater 2.7 - For automatically checking and notifying the user for new app updates](https://github.com/javiersantos/AppUpdater)
7. [Javacord 3.3.2 - For Discord integration](https://github.com/Javacord/Javacord)
8. [Twitter4j - For Twitter integration](https://github.com/Twitter4J/Twitter4J)
9. [Klaxon 5.5 - For parsing JSON files](https://github.com/cbeust/klaxon)
10. [React Native 0.64.3 - Used to display the UI and manage bot settings](https://reactnative.dev/)