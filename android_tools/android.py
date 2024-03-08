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
BUILD_TOOLS = "/Users/sbutr/Library/Android/sdk/build-tools/34.0.0/"
KEY_PASS="android"
KEY_ALIAS="androiddebugkey"

android_text_input_command = ["adb", "shell", "input", "text"]
android_top_activity_command = ["adb", "shell", "dumpsys", "activity", "|", "grep", "mCurrentFocus=Window"]
android_zipalign_command = ["zipalign", "-p", "-f", "-v"]

#
#  Functions & Classes
# 

class CommandRunner:

    # Check if we have the passed argumnets
    def hasCommand(this, args_list):
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
    def run(this, command):
        this.runInternal(command, False)

    # Executes shell command
    def runAndLog(this, command):
        -this.runInternal(command, True)

    # Executes shell command
    def runInternal(this, command, logs):
        if logs:
            Log.I(Green("Executing command: " + str(command) + "\n"))
        result = subprocess.run(command)
        if logs:
            Log.I(Green("Done. Code: " + str(result.returncode) + "\n")) 

    def exit(this):
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

# 
# Start  
# 

# Parse arguments and run commands
            
runner = CommandRunner()            

TEXT, COMMAND_ENTER_TEXT_FOUND = runner.hasCommand(["-i", "-input"])

if COMMAND_ENTER_TEXT_FOUND:
    Log.I("Entering text:\n")
    android_text_input_command.append(TEXT)
    runner.runAndLog(android_text_input_command)
    runner.exit()

_, COMMAND_SHOW_TOP_ACTIVITY_FOUND = runner.hasCommand(["-top-activity"])

if COMMAND_SHOW_TOP_ACTIVITY_FOUND:
    Log.I("Top activity:\n")
    runner.run(android_top_activity_command)
    runner.exit()

_, COMMAND_DEVICE_INFO_FOUND = runner.hasCommand(["-info"])

if COMMAND_DEVICE_INFO_FOUND:

    Log.I(Green("Release version:\n"))   
    runner.run(["adb", "shell", "getprop", "ro.build.version.release"])

    Log.I(Green("Release or code name version:\n"))
    runner.run(["adb", "shell", "getprop", "ro.build.version.release_or_codename"])

    Log.I(Green("Build ID:\n"))   
    runner.run(["adb", "shell", "getprop", "ro.build.id"])

    Log.I(Green("Manufacturer:\n"))   
    runner.run(["adb", "shell", "getprop", "ro.product.manufacturer"])

    Log.I(Green("Device model:\n"))   
    runner.run(["adb", "shell", "getprop", "ro.product.model"])

    Log.I(Green("Supported ABI list:\n"))
    runner.run(["adb", "shell", "getprop", "ro.product.cpu.abilist"])

    Log.I(Green("SDK version:\n"))   
    runner.run(["adb", "shell", "getprop", "ro.build.version.sdk"])

    runner.exit()

TEXT, COMMAND_ENTER_CREDENTIALS_FOUND = runner.hasCommand(["-enter-creads"])

if COMMAND_ENTER_CREDENTIALS_FOUND:

    strings = TEXT.split(':')

    Log.I("Entering text:\n")

    runner.run(["adb", "shell", "input", "text", strings[0]])
    runner.run(["adb", "shell", "input", "keyevent", "66"])
    runner.run(["adb", "shell", "input", "text", strings[1]])
    runner.run(["adb", "shell", "input", "keyevent", "66"])

    runner.exit()

APK_NAME, COMMAND_RESIGN_APK_FOUND = runner.hasCommand(["-resign-apk"])

if COMMAND_RESIGN_APK_FOUND and APK_NAME:
    
    # Steps to resign app:
    # 1. Unzip.
    # 2. Modify if neccessary.
    # 3. Remove META-INF
    # 4. Zip
    # 5. Run zipalign
    # 6. Resign with selected keystore  
    
    Log.I("Unzipping...")

    # Make temp dir
    runner.run(["rm", "-rf", "temp"])
    runner.run(["mkdir", "temp"])

    # Unzip
    runner.run(["unzip", "-q", APK_NAME, "-d", "temp"])

    # Remove META-INF/
    runner.run(["rm", "-rf", "temp/META-INF"])

    Log.I("Zipping...")

    # Zip
    os.chdir("temp")
    runner.run(["zip", "-q", "-0", "-r", "../temp.apk", ".", "-i", "*"])
    os.chdir("..")

    Log.I("Signing...")

    # Zipalign
    runner.run([BUILD_TOOLS + "zipalign", "-p", "-f", "4", "temp.apk", "out.apk"])

    # Sign
    runner.run([BUILD_TOOLS + "apksigner", "sign", "--ks", KEY_STORE_PATH, "--ks-pass", "pass:" + KEY_PASS, "--ks-key-alias", KEY_ALIAS, "out.apk"])

    Log.I("Done")
    
    runner.exit()

# Looks like provided args are incorrect, so show a help
Log.E("Sorry, cannot execute this command. Please, make sure that the arguments are correct.\n")

printHelp()

# End 