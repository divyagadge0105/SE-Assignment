cmake_minimum_required(VERSION 3.10)
project(native-lib)

# Option: set OpenCV_DIR in local.properties (recommended)
if(NOT OpenCV_DIR)
    if(EXISTS "${CMAKE_SOURCE_DIR}/../../../OpenCV-android-sdk/sdk/native/jni")
        set(OpenCV_DIR "${CMAKE_SOURCE_DIR}/../../../OpenCV-android-sdk/sdk/native/jni")
    endif()
endif()

if(NOT OpenCV_DIR)
    message(FATAL_ERROR "OpenCV not found. Set OpenCV_DIR in local.properties pointing to OpenCV-android-sdk/sdk/native/jni")
endif()

find_package(OpenCV REQUIRED)

add_library(native-lib SHARED native-lib.cpp)
find_library(log-lib log)

target_include_directories(native-lib PRIVATE ${OpenCV_INCLUDE_DIRS})
target_link_libraries(native-lib ${OpenCV_LIBS} ${log-lib})
