# The below command is basically equivalent to including all the Android.mk files 
# in all the sub directories manually. In our case, this will help 
# to include add/Android.mk and multiply/Android.mk
$(info Entering Android.mk file of sub module)

include $(call all-subdir-makefiles)																																																																																																																																																																																																																																																																																																																																																																																																																																										