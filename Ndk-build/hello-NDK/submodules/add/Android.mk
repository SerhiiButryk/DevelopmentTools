LOCAL_PATH:= $(call my-dir)

$(info Entering Android.mk file of add module PATH=$(LOCAL_PATH))

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= src/add.cpp
LOCAL_MODULE:= add

include $(BUILD_STATIC_LIBRARY)