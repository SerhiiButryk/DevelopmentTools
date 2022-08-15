# The top level build file

TOP_LOCAL_PATH:= $(call my-dir)

$(info )
$(info [Entering Android.mk file] PATH=$(TOP_LOCAL_PATH) ABI=$(APP_ABI) MODE=$(APP_OPIM) ARCH=$(TARGET_ARCH))
$(info )

include $(CLEAR_VARS)

# Include sub module build file
include $(TOP_LOCAL_PATH)/../../submodules/Android.mk

# Include main build file
include $(TOP_LOCAL_PATH)/Root.mk