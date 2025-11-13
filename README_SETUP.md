# Setup & Finalization Notes

This project was prepared to be as close to 'ready to submit' as possible. Because every developer machine and CI differs, please complete these final steps before submission.

1) local.properties
- Create a `local.properties` file in the project root (next to settings.gradle) with:
  sdk.dir=/path/to/android-sdk
  ndk.dir=/path/to/android-ndk

2) OpenCV
Option A - Use OpenCV Android SDK (recommended):
- Download OpenCV Android SDK from https://opencv.org/releases/
- Copy the folder `sdk/native/jni/include` and `sdk/native/libs/<abi>` into `external/opencv-android/sdk/native/...` so CMake can find headers and libs. The included `CMakeLists.txt` is written to work with this layout.

Option B - Use AAR Maven if you prefer to add as Gradle dependency. Update `app/build.gradle` to use the AAR coordinates.

3) Build
- Open the project in Android Studio
- Let Gradle sync and build
- Run on a physical Android device (camera permissions required)

If you want, I can add an automated script to download and place OpenCV into `external/opencv-android` for you.
