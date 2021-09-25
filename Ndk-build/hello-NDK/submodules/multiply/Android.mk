LOCAL_PATH:= $(call my-dir)

$(info Entering Android.mk file of mul module PATH=$(LOCAL_PATH))

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= src/mul.cpp
LOCAL_MODULE:= mul

include $(BUILD_STATIC_LIBRARY)