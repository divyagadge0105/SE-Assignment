This project is an Android application built in Kotlin that uses OpenGL, Camera2, and JNI (C++) 
for rendering and native processing. 
Features 
•  Android Kotlin + OpenGL ES rendering 
•  Real-time camera feed using Camera2 API 
•  Native C++ integration via JNI 
•  Works on API 21+ 
•  Modular structure (Java + C++ + Web as provided) 


Project Structure 

SEAssignment/ 
│── app/ 
│   └── src/main/    
├── java/com/se/assignment/ 
│     ├── Camera2Controller.kt 
│     ├── GLRenderer.kt 
│     ├── GLView.kt 
│     ├── MainActivity.kt 
│     └── NativeBridge.kt 
├── cpp/ 
│     ├── native-lib.cpp 
│     └── CMakeLists.txt 
├── res/ 
│       
└── AndroidManifest.xml 
│── web/ 
│── build.gradle 
│── settings.gradle 
│── README.md 

App installed on emulator 
Error! Filename not specified. 
App icon on the launcher screen 
Error! Filename not specified. 
App launching screen (OpenGL Surface starting) 
Error! Filename not specified. 


How to Run 
1. Open the project in Android Studio 
2. Let Gradle sync 
3. Connect emulator/device 
4. Press Run ▶ 
�
� Tech Stack 
• Kotlin 
• OpenGL ES 
• JNI / NDK 
• C++ 
• Android Studio 
• Gradle 
This project demonstrates a hybrid Android + Web application where Android runs a native 
OpenGL/Camera pipeline and communicates with TypeScript through a lightweight web 
interface. 
Android App Features 
• Custom Android app developed using Kotlin, OpenGL ES, and Camera2 API 
• JNI integration to send frames to native C++ code 
• Smooth rendering via GLSurfaceView and custom renderer 
• Launches successfully on emulator (screenshots included) 
Web Module Features 
• Lightweight TypeScript-based frontend 
• Displays processed output from backend logic 
• Static HTML + TS files included 
Technical Highlights 
• JNI bridge for Android ↔ Native C++ communication 
• Frame processing pipeline using OpenGL ES 
• Organized project with: 
o /app → Android Kotlin + C++ 
o /web → TypeScript + HTML 
o /gradle → Build automation 
Included Screenshots 
1. App icon visible on emulator home screen 
2. App launch screen before initialization 
3. Rendering screen placeholder 
