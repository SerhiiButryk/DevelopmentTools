#!/usr/bin/env python3

import subprocess
import sys
import inspect
import os

# 
# Utility script for Android
#

def Red(mes): return "\033[91m {}\033[00m" .format(mes)
def Green(mes): return "\033[92m {}\033[00m" .format(mes)

# 
# A HELP 
# 

def printHelp():
    Log.I("Usage: ./android.py [args...]\n")

    Log.I(Green("-i, -input [text]") + " - Enters a text in the focused view on the screen\n");
    Log.I("Example: ./android.py -i \"Some text\"\n");

    Log.I(Green("-top-activity") + " - Prints current top activity\n");
    Log.I("Example: ./android.py -top-activity\n");

    Log.I(Green("-resign-apk") + " - Resigns apk with default keystore\n");
    Log.I("Example: ./android.py -resign-apk app_name.apk\n");

    Log.I(Green("-info") + " - Prints info about connected device\n");
    Log.I("Example: ./android.py -info\n");

    Log.I(Green("-enter-creads") + " - Enters the next 2 strings in the 2 text fileds if has focus\n");
    Log.I("Example: ./android.py -enter-creads email:password\n");

# 
# CONSTANTS and shell commands
# 

KEY_STORE_PATH = "/Users/sbutr/.android/debug.keystore"
KEY_PASS="android"
KEY_ALIAS="androiddebugkey"

android_text_input_command = "adb shell input text "
android_top_activity_command = "adb shell dumpsys activity | grep mCurrentFocus=Window"
android_zipalign_command = "zipalign -p -f -v"

#
#  Functions & Classes
# 

class Runner:

    # Check if we have the passed argumnets
    @classmethod
    def hasCommand(cls, args_list):
        value = ""
        found = False
        # Iterate over a list of arguments starting from 1 element
        for index, arg in enumerate(sys.argv[1:]):    
            for elem in args_list:
                if elem == arg:
                    # If it has the next argument, then get it
                    if (index + 1) < len(sys.argv[1:]):
                        value = sys.argv[index+2]
                    # An arg is found
                    return (value, True)
        # An arg is not found     
        return (value, found) 
   
    # Executes shell command   
    @classmethod
    def run(cls, command):
        cls.runInternalUsingSubprocessRun(command, False)

    # Executes shell command
    @classmethod
    def runAndLog(cls, command):
        cls.runInternalUsingSubprocessRun(command, True)

    @classmethod
    def runWithPipes(cls, command):
        return cls.runInternalUsingCheckoutRun(command, False)

    # Executes shell command
    @classmethod
    def runInternalUsingSubprocessRun(cls, command, logs):
        if logs:
            Log.I(Green("Executing command: " + str(command) + "\n"))
        result = subprocess.run(command)
        if logs:
            Log.I(Green("Done. Code: " + str(result.returncode) + "\n")) 

    @classmethod
    def runInternalUsingCheckoutRun(cls, command, logs):
        if logs:
            Log.I(Green("Executing command: " + str(command) + "\n"))
        
        output = subprocess.check_output(["bash", "-c", command])
        text_result = output.decode('utf-8')

        return text_result

    @classmethod
    def exit(cls):
        # End the program
        sys.exit()


class Log:

    INFO = 1
    ERROR = 2
    DEBUG = 3
    DEBUG_MODE = False

    @classmethod
    def I(this, message):
        this.internal(this.INFO, message)

    @classmethod
    def E(this, message):
        this.internal(this.ERROR, message)

    @classmethod
    def D(this, message):
        this.internal(this.DEBUG, message)

    @classmethod
    def internal(this, level, message):

        formatted = ""

        if level == this.ERROR:
            # Error formatting
            formatted = Red(message)
        elif level == this.DEBUG:        
            # Main formatting
            formatted = Green(message)  
        else:
            # No formatting
            formatted = message      

        # Insert line number
        if this.DEBUG_MODE:
            lineNumber = inspect.stack()[1][2]
            print(lineNumber, formatted, end="")
        else:
            print(formatted, end="")

def isLinux() -> bool:
    return os.uname().sysname == 'Linux'

def isMac() -> bool:
    return os.uname().sysname == 'Darwin'

def getAndroidBuildToolsPath() -> str:

    android_home = os.getenv('ANDROID_HOME')
    if android_home is None:
        Log.E("Variable 'ANDROID_HOME' is not set. Please, set this variable.")
        return ""

    command = ""

    if isLinux():
        # On Linux you need to add '-P' option for 'grep'
        # Grep matching string like this "35.0.0"
        command = "ls " + android_home + "/build-tools" + "| sort -r | grep -P \"\d{2}.\d{1}.\d{1}$\" | head -n 1"
    
    elif isMac():
        # On MAC you need to add '-E' option for 'grep'
        # Grep matching string like this "35.0.0"
        command = "ls " + android_home + "/build-tools" + "| sort -r | grep -E \"\d{2}.\d{1}.\d{1}$\" | head -n 1"

    else:
        Log.E("getAndroidBuildToolsPath(): Failed to select OS")
    
    version = Runner.runWithPipes(command)
    path = android_home + "/build-tools/" + version.strip() + "/"
    
    Log.I("Build tools path: '{}'\n".format(path))
    return path

# 
# The start  
# 

##############################################################################################

TEXT, COMMAND_ENTER_TEXT_FOUND = Runner.hasCommand(["-i", "-input"])

if COMMAND_ENTER_TEXT_FOUND:
    Log.I("Entering: '{}'\n".format(TEXT))
    result = Runner.runWithPipes("adb shell input text " + TEXT)
    Log.I("Done\n")
    # print("Test")
    Runner.exit()

##############################################################################################

_, COMMAND_SHOW_TOP_ACTIVITY_FOUND = Runner.hasCommand(["-top-activity"])

if COMMAND_SHOW_TOP_ACTIVITY_FOUND:
    Log.I("Top activity:\n")
    result = Runner.runWithPipes("adb shell dumpsys activity | grep mCurrentFocus=Window")
    Log.I(result)
    Runner.exit()

##############################################################################################

_, COMMAND_DEVICE_INFO_FOUND = Runner.hasCommand(["-info"])

if COMMAND_DEVICE_INFO_FOUND:

    result = Runner.runWithPipes("adb shell getprop ro.build.version.release")
    Log.I(Green("Release version: ") + result)

    result = Runner.runWithPipes("adb shell getprop ro.build.version.release_or_codename")
    Log.I(Green("Release or code name version: ") + result)

    result = Runner.runWithPipes("adb shell getprop ro.build.id")
    Log.I(Green("Build ID: ") + result)

    result = Runner.runWithPipes("adb shell getprop ro.product.manufacturer")
    Log.I(Green("Manufacturer: ") + result)

    result = Runner.runWithPipes("adb shell getprop ro.product.model")
    Log.I(Green("Device model: ") + result)

    result = Runner.runWithPipes("adb shell getprop ro.product.cpu.abilist")
    Log.I(Green("Supported ABI list: ") + result)

    result = Runner.runWithPipes("adb shell getprop ro.build.version.sdk")
    Log.I(Green("SDK version: ") + result)

    Runner.exit()

##############################################################################################

TEXT, COMMAND_ENTER_CREDENTIALS_FOUND = Runner.hasCommand(["-enter-creads"])

if COMMAND_ENTER_CREDENTIALS_FOUND:

    strings = TEXT.split(':')

    Log.I("Entering text...\n")

    Runner.runWithPipes("adb shell input text " + strings[0])
    Runner.runWithPipes("adb shell input keyevent 66")
    Runner.runWithPipes("adb shell input text " + strings[1])
    Runner.runWithPipes("adb shell input keyevent 66")

    Log.I("Done\n")

    Runner.exit()

##############################################################################################

APK_NAME, COMMAND_RESIGN_APK_FOUND = Runner.hasCommand(["-resign-apk"])

if COMMAND_RESIGN_APK_FOUND and APK_NAME:
    
    # Steps to resign app:
    # 1. Unzip.
    # 2. Modify if neccessary.
    # 3. Remove META-INF
    # 4. Zip
    # 5. Run zipalign
    # 6. Resign with selected keystore  

    build_tools_path = getAndroidBuildToolsPath()

    # If cannot get path then exit
    if not build_tools_path:
        Log.I("Stopped due to an error. Cannot proceed")
        Runner.exit()
    
    Log.I("Unzipping...")

    # Create temp dir
    Runner.runWithPipes("rm -rf temp")
    Runner.runWithPipes("mkdir temp")

    # Unzip
    Runner.runWithPipes("unzip -q " + APK_NAME + " -d temp")

    # Remove META-INF/
    Runner.runWithPipes("rm -rf temp/META-INF")

    Log.I("Zipping...")

    # Zip
    os.chdir("temp")
    Runner.runWithPipes("zip -q -0 -r ../temp.apk . -i *")
    os.chdir("..")

    Log.I("Signing...")

    # Zipalign
    Runner.runWithPipes(build_tools_path + "zipalign -p -f 4 temp.apk out.apk")

    # Sign
    command = build_tools_path + "apksigner sign --ks " +  KEY_STORE_PATH + " --ks-pass pass: " + KEY_PASS + " --ks-key-alias " + KEY_ALIAS + " out.apk"
    Runner.runWithPipes(command)

    Log.I("Done\n")
    
    Runner.exit()

# Looks like provided args are incorrect, so show a help
Log.E("Sorry, cannot execute this command. Please, make sure that the arguments are correct.\n")

printHelp()

# The end 