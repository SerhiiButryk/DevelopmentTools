#!/usr/bin/env python3

import subprocess
import sys
import inspect
import os

from urllib.request import urlretrieve

# 
# Utility script for Android
#

def Red(mes): return "\033[91m{}\033[00m" .format(mes)
def Green(mes): return "\033[92m{}\033[00m" .format(mes)

# 
# A help 
# 

def printHelp():
    Log.I("\nUsage: ./android.py [options...]\n\n")

    Log.I(Green("-i, -input [text]") + " - Enters a text in the focused view on the screen\n")
    Log.I("Example: ./android.py -i \"Some text\"\n")

    Log.I(Green("-top-activity") + " - Prints current top activity\n")
    Log.I("Example: ./android.py -top-activity\n")

    Log.I(Green("-resign-apk, -no-prompt") + " - Resigns apk with default keystore\n")
    Log.I("Example: ./android.py -resign-apk app_name.apk\n")
    Log.I("Or\n")
    Log.I("Example: ./android.py -resign-apk -no-prompt app_name.apk\n")

    Log.I(Green("-info") + " - Prints info about connected device\n")
    Log.I("Example: ./android.py -info\n")

    Log.I(Green("-enter-creds") + " - Enters the next 2 strings in the 2 text fileds if has focus\n")
    Log.I("Example: ./android.py -enter-creds email:password\n")

    Log.I(Green("-process-info") + " - Shows proccess info for a process\n")
    Log.I("Example: ./android.py -process-info 13380\n")
    Log.I("Or\n")
    Log.I("Example: ./android.py -process-info com.example.my.package.name\n")

    Log.I(Green("-cert-info") + " - Shows certificate signature info for apk\n")
    Log.I("Example: ./android.py -cert-info my.apk\n")

    Log.I(Green("-package-info") + " - Shows detailed package info for app\n")
    Log.I("Example: ./android.py -package-info my.good.package\n")

    Log.I(Green("-decomp") + " - Decompile apk using apktool\n")
    Log.I("Example: ./android.py -decomp my_good.apk\n")

    Log.I(Green("-sym") + " - Symbolicate Java/Kotlin crash\n")
    Log.I("Example: ./android.py -sym mapping.txt crash_stack_logs.txt\n")

    Log.I(Green("-build") + " - Build apk from decompiled code\n")
    Log.I("Example: ./android.py -build folder_with_decompiled_apk_code\n")

    Log.I(Green("-nsym debugData/ crash_logs.txt") + " - Symbolicate Native crash\n")
    Log.I("Example: ./android.py -nsym ~/my-symbols/armeabi-v7a/ crash_logs.txt\n")

# 
# Constants
# 

KEY_PASS="android"
KEY_ALIAS="androiddebugkey"

#
#  Functions & Classes
# 

# Check if we have the passed argumnets
def hasCommand(args_list) -> list:
    value = ""
    found = False
    
    # Iterate over a list of arguments starting from the first element
    for index, arg in enumerate(sys.argv[1:]):    
        
        # Search for options and args.
        for elem in args_list:
            if elem == arg:
                # Option is found.
                found = True
        
        # Search for arg values.
        # If it doesn't start with '-' symbol then get it
        if not arg.startswith('-'):
            value = arg
        
        # To exclude matches which have more flags 
        # and some of them can be matched
        if arg.startswith('-') and arg not in args_list:
            found = False       
            return (value, found)
        
    return (value, found) 

def hasSingleCommand(arg) -> bool:
    for index, a in enumerate(sys.argv[1:]):
        if arg == a:
            return True
    return False

def getArgWithValue(argument) -> str:
    for index, arg in enumerate(sys.argv[1:]):  
        if argument == arg:
            nextArg = sys.argv[index + 2]
            return nextArg         
    return ""

def path(arg) -> str:
    return os.path.realpath(os.path.expanduser(arg))

class Runner:

    # Executes shell command
    @classmethod
    def run(cls, command, logs = False):
        return cls.runInternalUsingCheckoutRun(command, logs)

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
        
        text_result = ""

        try:
            output = subprocess.check_output(["bash", "-c", command])
            text_result = output.decode('utf-8')
        except subprocess.CalledProcessError:
            if logs:
                Log.I("Command retruned non zero status code")

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
    return sys.platform == 'linux'

def isMac() -> bool:
    return sys.platform == 'darwin'

def getAndroidBuildToolsPath() -> str:

    android_home = os.getenv('ANDROID_HOME')
    if android_home is None:
        Log.E("Variable 'ANDROID_HOME' is not set. Please, set this variable.")
        return ""

    command = ""

    if isLinux():
        # On Linux you need to add '-P' option for 'grep'
        # Grep matching string like this "35.0.0"
        command = "ls " + android_home + "/build-tools" + "| sort -r | grep -P \"\\d{2}.\\d{1}.\\d{1}$\" | head -n 1"
    
    elif isMac():
        # On MAC you need to add '-E' option for 'grep'
        # Grep matching string like this "35.0.0"
        command = "ls " + android_home + "/build-tools" + "| sort -r | grep -E \"\\d{2}.\\d{1}.\\d{1}$\" | head -n 1"

    else:
        Log.E("getAndroidBuildToolsPath(): Failed to select OS")
    
    version = Runner.run(command)
    path = android_home + "/build-tools/" + version.strip() + "/"
    
    Log.I("Selected build tools: '{}'\n".format(path))
    return path

def getNDKPath() -> str:
    android_home = os.getenv('ANDROID_HOME')
    if android_home is None:
        Log.E("Variable 'ANDROID_HOME' is not set. Please, set this variable.")
        return ""

    command = ""

    if isLinux():
        # On Linux you need to add '-P' option for 'grep'
        # Grep matching string like this "35.0.0"
        command = "ls " + android_home + "/ndk" + "| sort -r | grep -P \"\\d{2}.\\d{1}.\\d{7}$\" | head -n 1"
    
    elif isMac():
        # On MAC you need to add '-E' option for 'grep'
        # Grep matching string like this "35.0.0"
        command = "ls " + android_home + "/ndk" + "| sort -r | grep -E \"\\d{2}.\\d{1}.\\d{7}$\" | head -n 1"

    else:
        Log.E("getNDKPath(): Failed to select OS")

    version = Runner.run(command)
    return android_home + "/ndk/" + version.strip() + "/"

# Get deafult Android keystore path
def getAndroidKeystorePath() -> str:

    KEY_STORE_PATH = ".android/debug.keystore"

    home = os.getenv('HOME')   
    if home is None:
        Log.E("Variable 'HOME' is not set. Please, set this variable.")
        return "" 
    
    keystore = home + "/" + KEY_STORE_PATH

    Log.I("Selected key store file: '{}'\n".format(keystore))

    return keystore

def getRetracePath() -> str:

    android_home = os.getenv('ANDROID_HOME')
    if android_home is None:
        Log.E("Variable 'ANDROID_HOME' is not set. Please, set this variable.")
        return ""

    return android_home + "tools/proguard/lib"

def hasAnyDevices() -> bool:

    res = Runner.run("adb devices")

    lines = res.count('\n')

    if lines > 2:
        return True

    Log.E("Device list is empty. No devices currently available.\n")    
    return False


#
# Start of the program
#

TEXT, COMMAND_ENTER_TEXT_FOUND = hasCommand(["-i", "-input"])

if COMMAND_ENTER_TEXT_FOUND:

    if hasAnyDevices() == False:
        Runner.exit()

    Log.I("Entering: '{}'\n".format(TEXT))
    result = Runner.run("adb shell input text " + TEXT)
    Log.I("Done\n")
    # print("Test")
    Runner.exit()

##############################################################################################

_, COMMAND_SHOW_TOP_ACTIVITY_FOUND = hasCommand(["-top-activity"])

if COMMAND_SHOW_TOP_ACTIVITY_FOUND:

    if hasAnyDevices() == False:
        Runner.exit()

    Log.I("Top activity:\n")
    result = Runner.run("adb shell dumpsys activity | grep mCurrentFocus=Window")
    Log.I(result)
    Runner.exit()

##############################################################################################

_, COMMAND_DEVICE_INFO_FOUND = hasCommand(["-info"])

if COMMAND_DEVICE_INFO_FOUND:

    if hasAnyDevices() == False:
        Runner.exit()

    result = Runner.run("adb shell getprop ro.build.version.release")
    Log.I(Green("Release version: ") + result)

    result = Runner.run("adb shell getprop ro.build.version.release_or_codename")
    Log.I(Green("Release or code name version: ") + result)

    result = Runner.run("adb shell getprop ro.build.id")
    Log.I(Green("Build ID: ") + result)

    result = Runner.run("adb shell getprop ro.product.manufacturer")
    Log.I(Green("Manufacturer: ") + result)

    result = Runner.run("adb shell getprop ro.product.model")
    Log.I(Green("Device model: ") + result)

    result = Runner.run("adb shell getprop ro.product.cpu.abilist")
    Log.I(Green("Supported ABI list: ") + result)

    result = Runner.run("adb shell getprop ro.build.version.sdk")
    Log.I(Green("SDK version: ") + result)

    Runner.exit()

##############################################################################################

TEXT, COMMAND_ENTER_CREDENTIALS_FOUND = hasCommand(["-enter-creds"])

if COMMAND_ENTER_CREDENTIALS_FOUND:

    if hasAnyDevices() == False:
        Runner.exit()

    strings = TEXT.split(':')

    Log.I("Entering text...\n")

    Runner.run("adb shell input text " + strings[0])
    Runner.run("adb shell input keyevent 66")
    Runner.run("adb shell input text " + strings[1])
    Runner.run("adb shell input keyevent 66")

    Log.I("Done\n")

    Runner.exit()

##############################################################################################

APK_NAME, COMMAND_RESIGN_APK_FOUND = hasCommand(["-resign-apk", "-no-prompt"])
SHOULD_PROMPT = hasSingleCommand("-no-prompt")

if COMMAND_RESIGN_APK_FOUND and APK_NAME:

    if hasAnyDevices() == False:
        Runner.exit()
    
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
    
    Log.I("Unzipping...\n")

    # Create temp dir
    Runner.run("rm -rf temp")
    Runner.run("mkdir temp")

    # Unzip
    Runner.run("unzip -q " + APK_NAME + " -d temp/")

    # Remove META-INF/
    Runner.run("rm -rf temp/META-INF")

    if not SHOULD_PROMPT:
        # Ask for modification
        Log.I(Green("Now it's time to modify the app. Please, add changes to [temp/] or click any key to continue...\n"))
        input()

    Log.I("Zipping...\n")

    # Zip
    os.chdir("temp")
    Runner.run("zip -0 -r temp.zip *")
    os.chdir("..")

    Log.I("Signing...\n")

    # Zipalign
    Runner.run(build_tools_path + "zipalign -p -f 4 temp/temp.zip resigned-app.apk")

    # Sign
    command = build_tools_path + "apksigner sign --ks " +  getAndroidKeystorePath() + " --ks-pass pass:" + KEY_PASS + " --ks-key-alias " + KEY_ALIAS + " resigned-app.apk"
    Runner.run(command)

    Log.I("Find file: resigned-app.apk\n")

    Log.I("Done\n")
    
    Runner.exit()

##############################################################################################

PID, COMMAND_PROCESS_INFO_FOUND = hasCommand(["-process-info"])

if COMMAND_PROCESS_INFO_FOUND and PID:

    if hasAnyDevices() == False:
        Runner.exit()

    result = Runner.run("adb shell ps -A | grep " + PID)

    if not result:
        Log.I("Process is not alive\n")
    else:
        Log.I(result)

    Runner.exit()

##############################################################################################

APK, COMMAND_CERT_INFO_FOUND = hasCommand(["-cert-info"])

if COMMAND_CERT_INFO_FOUND and APK:

    if hasAnyDevices() == False:
        Runner.exit()

    build_tools_path = getAndroidBuildToolsPath()

    # If cannot get path then exit
    if not build_tools_path:
        Log.I("Stopped due to an error. Cannot proceed")
        Runner.exit()

    result = Runner.run(build_tools_path + "apksigner verify --print-certs " + APK)

    Log.I(result)

    Runner.exit()

##############################################################################################

PACKAGE_NAME, COMMAND_PACKAGE_INFO_FOUND = hasCommand(["-package-info"])

if COMMAND_PACKAGE_INFO_FOUND and PACKAGE_NAME:

    if hasAnyDevices() == False:
        Runner.exit()

    result = Runner.run("adb shell dumpsys package " + PACKAGE_NAME)

    Log.I(Green("START >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"))

    Log.I(result)

    Log.I(Green("END >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"))

    Runner.exit()

##############################################################################################

APK_NAME, COMMAND_DECOMPILE_APK = hasCommand(["-decomp"])

if COMMAND_DECOMPILE_APK and APK_NAME:

    Log.I("Decompiling '" + APK_NAME + "'\n")
    Log.I("...\n")

    destFolder = "output"

    apkTool = "apktool"
    apkToolJar = "apktool_2.11.0.jar"

    noFiles = os.path.isfile(apkTool) == False or os.path.isfile(apkToolJar) == False
    platfromIsSupported = isLinux() == True or isMac() == True
    
    if noFiles and platfromIsSupported:

        if os.path.isfile(apkTool) == False:

            Log.I("Installing apktool for the first time...\n")     

            url = ""
            if isLinux():
                url = ("https://raw.githubusercontent.com/iBotPeaches/Apktool/master/scripts/linux/apktool")
            elif isMac():
                url = ("https://raw.githubusercontent.com/iBotPeaches/Apktool/master/scripts/osx/apktool")

            filename = "apktool"

            try:
                file, headers = urlretrieve(url, filename)
                res = False

                for name, value in headers.items():
                    if name == "Content-Length" and int(value) > 0:
                        Log.I("Success {Content-Length: " + value + "}\n")
                        res = True
                        break

                if res == False:
                    Log.E("Content is 0. Failed. Stopped.\n")
                    Runner.exit()

                #  7 - rwe
                #  4 - r
                os.chmod(file, 0o744)

            except:
                Log.E("Failed to download apktool. Try again later. Stopped\n")
                Runner.exit()

        if os.path.isfile(apkToolJar) == False:

            try:
            
                Log.I("Downloading apktool_2.11.0.jar for the first time\n")

                url = ("https://bitbucket.org/iBotPeaches/apktool/downloads/apktool_2.11.0.jar")
                filename = "apktool_2.11.0.jar"

                _, headers = urlretrieve(url, filename)
                res == False

                for name, value in headers.items():
                    if name == "Content-Length" and int(value) > 0:
                        Log.I("Success {Content-Length: " + value + "}\n")
                        res = True
                        break

                if res == False:
                    Log.E("Content is 0. Failed. Stopped.\n")
                    Runner.exit()

            except:
                Log.E("Failed to download apktool jar. Try again later. Stopped\n")
                Runner.exit()
    else:                        
        Log.E("Sorry, your platform is not supported. Script supports only Mac and Linux platfroms. Stopped.\n")
        Runner.exit()
                

    Runner.run("./apktool d " + APK_NAME + " -o " + destFolder)

    Log.I(Green("Done. Find folder: '" + destFolder + "'\n"))

    Runner.exit()

##############################################################################################    

_, COMMAND_SYMB_CRASH_STACK = hasCommand(["-sym"])

if COMMAND_SYMB_CRASH_STACK:

    if len(sys.argv) < 4:
        Log.E("Sorry. Not enoght arguments.")            
        Runner.exit()

    mappingFile = sys.argv[2]
    crashLogFile = sys.argv[3]

    retrace_path = getRetracePath()
    outFile = "result.txt"

    Runner.run("java -jar " + retrace_path + "/retrace.jar " + mappingFile + " " + crashLogFile + " > " + outFile)

    Log.I(Green("Done. Find file: '" + outFile + "'\n"))

    Runner.exit()

##############################################################################################

APK_FOLDER, COMMAND_BUILD_APK = hasCommand(["-build"])

if COMMAND_BUILD_APK and APK_FOLDER:

    Runner.run("apktool -v b " + APK_FOLDER)

    Log.I(Green("Done\n"))

    Runner.exit()

NEXT, COMMAND_SYM_NATIVE_CRASH = hasCommand(["-nsym"])
if COMMAND_SYM_NATIVE_CRASH:

    if len(sys.argv) < 4:
        Log.E("Not enough arguments. Sorry.\n")
        Runner.exit()    
    
    symbols = getArgWithValue("-nsym")
    logs = path(NEXT)
    ndk = getNDKPath()

    print("Folder with symbols: " + symbols)
    print("Crash logs: " + logs)
    print("NDK: " + ndk)

    # Example: /Users/sbutr/Library/Android/sdk/ndk/21.3.6528147/ndk-stack -sym ~/my-symbols/armeabi-v7a/ -dump ~/Desktop/crash 
    Runner.run(ndk + "ndk-stack -sym " + symbols + " -dump " + logs)

    Log.I(Green("Done\n"))
    Runner.exit()

# Looks like provided args are not correct, so show a help
Log.E("Sorry, cannot execute this command. \nPlease, make sure that the arguments are correct. A help:\n")
printHelp()