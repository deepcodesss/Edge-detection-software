cmake_minimum_required(VERSION 3.6)
project(opencv_native)
add_library( native-lib SHARED app/src/main/cpp/native-lib.cpp )
find_library( log-lib log )
target_link_libraries( native-lib ${log-lib} )
# To link OpenCV, adjust OpenCV_DIR and link libraries depending on how you placed OpenCV SDK.
