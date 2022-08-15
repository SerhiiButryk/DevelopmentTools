# Project main module build file

LOCAL_PATH:= $(call my-dir)

$(info Entering RootBuild.mk file LOCAL_PATH=$(LOCAL_PATH))

include $(CLEAR_VARS)

LOCAL_SRC_FILES := ../src/main/compute.cpp
LOCAL_MODULE := compute 
LOCAL_STATIC_LIBRARIES := add mul

include $(BUILD_EXECUTABLE)