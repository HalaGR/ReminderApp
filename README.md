# ReminderApp
##How To:
•	To watch the demo video, you can find it at https://drive.google.com/drive/folders/1cPQbBXtAS9VMUcM8eJsq1pSuEfqnChZV?usp=sharing 
•	  Import repository from master branch for project Smart Reminder   from the link below :"https://github.com/HalaGR/ReminderApp.git".

•	Pre-requests:
This project was created using Android Studio Chipmunk|2021.2.1 Patch one version 7.2.2.

•	In android studio:
1)Open your Android Studio then go to the File > New > Project from Version Control 
2)After clicking on the Project from Version Control a pop-up screen will arise . In the Version control choose Git from the drop-down menu. 
3)Then at last paste the link in the URL, choose your Directory. Click on the Clone button and you are done.
4) to use the code, replace <key> text in both below mentioned files with your google service API key. 
app/src/main/java/com/example/locationreminder/ControlActivity.java
app/src/release/res/values/google_maps_api.xml
•	The following build settings have been specified in build.gradle,
and you should make sure you have the corresponding Android SDK packages installed:
 minSdkVersion: 24
 compileSdkversion: 33
 targetSdkVersion: 33
 buildToolsVersion: 31.0.0 

•	note the dependencies for the project defined in build.gradle;
 	the app includes the libraries:play-services  firebase, Gson, google- services.json.
•	To check the database:
1)	Go to firebase console https://console.firebase.google.com and login with firebaseacc08@gmail.com passwords: firebaseacc1
2)	Choose project ReminderApp
3)	Under Build the relevant pages are Authentication and fireStore database.

