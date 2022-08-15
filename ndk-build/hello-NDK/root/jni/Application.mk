# Project common configurations for all modules

APP_OPIM := release

APP_ABI := all

APP_STL := c++_static

# Strip configuration
APP_STRIP_MODE := --strip-debug

APP_CPPFLAGS := -frtti -fexceptions

APP_PLATFORM := android-26

# APP_BUILD_SCRIPT := ndk_build.mk # Tells where to find build script file