#!/usr/bin/env python3

import subprocess
import sys
import inspect
import os
import shutil

from urllib.request import urlretrieve
from zipfile import ZipFile 

# 
# Utility script for Android
# 
# It is developed for Linux and Mac OS
# Note that some bugs coud be present.
#

def Red(mes): return f"\033[91m{mes}\033[00m"
def Green(mes): return f"\033[92m{mes}\033[00m"
def Blue(mes): return f"\033[0;34m{mes}\033[00m"

# 
# A help 
# 

def printHelp():
    
    log(Log.INFO,         
    """
    Usage: ./android.py [options...]

    -i, -input [text] - Enters a text in the focused view on the screen
    Example: ./android.py -i \"Some text\"

    -top-activity - Prints current top activity
    Example: ./android.py -top-activity

    -resign-apk, -no-prompt - Resigns apk with default keystore
    Example: ./android.py -resign-apk app_name.apk
    or
    Example: ./android.py -resign-apk -no-prompt app_name.apk

    -info - Prints info about connected device
    Example: ./android.py -info

    -enter-creds - Enters the next 2 strings in the 2 text fileds if has focus
    Example: ./android.py -enter-creds email:password

    -process-info - Shows proccess info for a process
    Example: ./android.py -process-info 13380
    or
    Example: ./android.py -process-info com.example.my.package.name

    -cert-info - Shows certificate signature info for apk
    Example: ./android.py -cert-info my.apk

    -package-info - Shows detailed package info for app
    Example: ./android.py -package-info my.good.package

    -decomp - Decompile apk using apktool
    Example: ./android.py -decomp my_good.apk

    -sym - Symbolicate Java/Kotlin crash
    Example: ./android.py -sym mapping.txt crash_stack_logs.txt

    -build - Build apk from decompiled code
    Example: ./android.py -build folder_with_decompiled_apk_code

    -nsym debugData/ crash_logs.txt - Symbolicate Native crash
    Example: ./android.py -nsym ~/my-symbols/armeabi-v7a/ crash_logs.txt

    -smalitodex -o classes.dex -jar smali-3.0.9-dev-fat.jar ./smali_classes/ - Convert SMALI to DEX
    Example: ./android.py -smalitodex -o classes.dex -jar smali-3.0.9-dev-fat.jar ./smali_classes/
    Hint:
    Smali assembler github page: https://github.com/google/smali/tree/main

    -jadxopen classes.dex - Open DEX file using Jadx
    Example: ./android.py -jadxopen classes.dex
    Hint:
    Jadx github page: https://github.com/skylot/jadx
    """ + '\n'
    )

# 
# Constants
# 

KEY_PASS="android"
KEY_ALIAS="androiddebugkey"

#
#  Functions & Classes
# 

# 
# Log utility
# 

class Log:
    INFO = 1
    ERROR = 2
    DEBUG = 3
    DEBUG_MODE = False

def log(level, message):
    
    formatted = ""

    if level == Log.ERROR:
        # Error formatting
        formatted = Red(message)
    elif level == Log.DEBUG:        
        # Neutral formatting
        formatted = Blue(message)  
    else:
        # No formatting
        formatted = message      

    # Insert line number
    if Log.DEBUG_MODE:
        lineNumber = inspect.stack()[1][2]
        print(lineNumber, formatted, end="")
    else:
        print(formatted, end="")

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
            log(Log.INFO, Blue("Executing command: " + str(command) + "\n"))
        result = subprocess.run(command)
        if logs:
            log(Log.INFO, Blue("Done. Code: " + str(result.returncode) + "\n")) 

    @classmethod
    def runInternalUsingCheckoutRun(cls, command, logs):
        if logs:
            log(Log.INFO, Blue("Executing command: " + str(command) + "\n"))
        
        text_result = ""

        try:
            output = subprocess.check_output(["bash", "-c", command])
            text_result = output.decode('utf-8')
        except subprocess.CalledProcessError:
            if logs:
                log(Log.INFO, "Command retruned non zero status code")

        return text_result

    @classmethod
    def exit(cls):
        # End the program
        sys.exit()

def isLinux() -> bool:
    return sys.platform == 'linux'

def isMac() -> bool:
    return sys.platform == 'darwin'

def getAndroidBuildToolsPath() -> str:

    android_home = os.getenv('ANDROID_HOME')
    if android_home is None:
        log(Log.ERROR, "Variable 'ANDROID_HOME' is not set. Please, set this variable.")
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
        log(Log.ERROR, "getAndroidBuildToolsPath(): Failed to select OS")
    
    version = Runner.run(command)

    path = f"{android_home}/build-tools/{version.strip()}/"
    
    log(Log.INFO, f"Selected build tools: '{path}'\n")
    return path

def getNDKPath() -> str:
    android_home = os.getenv('ANDROID_HOME')
    if android_home is None:
        log(Log.ERROR, "Variable 'ANDROID_HOME' is not set. Please, set this variable.")
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
        log(Log.ERROR, "getNDKPath(): Failed to select OS")

    version = Runner.run(command)

    return f"{android_home}/ndk/{version.strip()}/"

# Get deafult Android keystore path
def getAndroidKeystorePath() -> str:

    KEY_STORE_PATH = ".android/debug.keystore"

    home = os.getenv('HOME')   
    if home is None:
        log(Log.ERROR, "Variable 'HOME' is not set. Please, set this variable.")
        return "" 
    
    keystore = f"{home}/{KEY_STORE_PATH}"

    log(Log.INFO, f"Selected key store file: '{keystore}'\n")

    return keystore

def getRetracePath() -> str:

    android_home = os.getenv('ANDROID_HOME')
    if android_home is None:
        log(Log.ERROR, "Variable 'ANDROID_HOME' is not set. Please, set this variable.")
        return ""

    return android_home + "tools/proguard/lib"

def hasAnyDevices() -> bool:

    res = Runner.run("adb devices")

    lines = res.count('\n')

    if lines > 2:
        return True

    log(Log.ERROR, "Device list is empty. No devices currently available.\n")    
    return False

def downloadFile(url, filename) -> bool:

    log(Log.INFO, f"Start downloading file: {url}\n")

    try:
        file, headers = urlretrieve(url, filename)
        res = False

        for name, value in headers.items():
            if name == "Content-Length" and int(value) > 0:
                log(Log.INFO, f"Success: Content-Length: {value}\n")
                res = True
                break

        if res == False:
            log(Log.ERROR, "Content length is 0. Failed.\n")

        return res

    except:
        log(Log.ERROR, "Failed to download file. Try again later.\n")
        return False

def confirmAction(message) -> bool:
    
    user_input = input(f"{message} (yes/no): ")
    
    if user_input.lower() == "yes":
        print("Continuing...")
        return True
    else:
        print("Stopping...")
        return False
    
# Global variables
jadxExecFileName = "jadx.jar"      
    
def downloadJadx() -> bool:

    jadxLink = "https://github.com/skylot/jadx/releases/download/v1.5.1/jadx-1.5.1.zip"
    jadxDownloadedFile = "jadx.zip"
    fileToExtract = "lib/jadx-1.5.1-all.jar"

    if os.path.isfile(jadxExecFileName) == False:

        print("Downloading jadx...")    
        
        if downloadFile(jadxLink, jadxDownloadedFile):

            print("Unzipping jadx...")  

            with ZipFile(path(f"./{jadxDownloadedFile}"), 'r') as zObject: 

                zObject.extract(fileToExtract, path=path("./")) 
                
                zObject.close() 

            shutil.move(path(f"./{fileToExtract}"), path(f"./{jadxExecFileName}")) 

            os.chmod(path(f"./{jadxExecFileName}"), 0o744)

            log(Log.INFO, Blue("Cleaning...\n"))

            os.rmdir(path("./lib"))
            os.remove(path("./" + jadxDownloadedFile))

    else:
        log(Log.INFO, "Jadx is already present. Nothing to do.\n")    

    return True            

#
# Start of the program
#

TEXT, COMMAND_ENTER_TEXT_FOUND = hasCommand(["-i", "-input"])

if COMMAND_ENTER_TEXT_FOUND:

    if hasAnyDevices() == False:
        Runner.exit()

    log(Log.INFO, f"Entering: '{TEXT}'\n")
    result = Runner.run("adb shell input text " + TEXT)
    log(Log.INFO, "Done\n")
    # print("Test")
    Runner.exit()

##############################################################################################

_, COMMAND_SHOW_TOP_ACTIVITY_FOUND = hasCommand(["-top-activity"])

if COMMAND_SHOW_TOP_ACTIVITY_FOUND:

    if hasAnyDevices() == False:
        Runner.exit()

    log(Log.INFO, "Top activity:\n")
    result = Runner.run("adb shell dumpsys activity | grep mCurrentFocus=Window")
    log(Log.INFO, result)
    Runner.exit()

##############################################################################################

_, COMMAND_DEVICE_INFO_FOUND = hasCommand(["-info"])

if COMMAND_DEVICE_INFO_FOUND:

    if hasAnyDevices() == False:
        Runner.exit()

    result = Runner.run("adb shell getprop ro.build.version.release")
    log(Log.INFO, Blue("Release version: ") + result)

    result = Runner.run("adb shell getprop ro.build.version.release_or_codename")
    log(Log.INFO, Blue("Release or code name version: ") + result)

    result = Runner.run("adb shell getprop ro.build.id")
    log(Log.INFO, Blue("Build ID: ") + result)

    result = Runner.run("adb shell getprop ro.product.manufacturer")
    log(Log.INFO, Blue("Manufacturer: ") + result)

    result = Runner.run("adb shell getprop ro.product.model")
    log(Log.INFO, Blue("Device model: ") + result)

    result = Runner.run("adb shell getprop ro.product.cpu.abilist")
    log(Log.INFO, Blue("Supported ABI list: ") + result)

    result = Runner.run("adb shell getprop ro.build.version.sdk")
    log(Log.INFO, Blue("SDK version: ") + result)

    Runner.exit()

##############################################################################################

TEXT, COMMAND_ENTER_CREDENTIALS_FOUND = hasCommand(["-enter-creds"])

if COMMAND_ENTER_CREDENTIALS_FOUND:

    if hasAnyDevices() == False:
        Runner.exit()

    strings = TEXT.split(':')

    log(Log.INFO, "Entering text...\n")

    Runner.run(f"adb shell input text {strings[0]}")
    Runner.run("adb shell input keyevent 66")
    Runner.run(f"adb shell input text {strings[1]}")
    Runner.run("adb shell input keyevent 66")

    log(Log.INFO, "Done\n")

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
        log(Log.INFO, "Stopped due to an error. Cannot proceed")
        Runner.exit()
    
    log(Log.INFO, "Unzipping...\n")

    # Create temp dir
    Runner.run("rm -rf temp")
    Runner.run("mkdir temp")

    # Unzip
    Runner.run(f"unzip -q {APK_NAME} -d temp/")

    # Remove META-INF/
    Runner.run("rm -rf temp/META-INF")

    if not SHOULD_PROMPT:
        # Ask for modification
        log(Log.INFO, Blue("Now it's time to modify the app. Please, add changes to [temp/] or click any key to continue...\n"))
        input()

    log(Log.INFO, "Zipping...\n")

    # Zip
    os.chdir("temp")
    Runner.run("zip -0 -r temp.zip *")
    os.chdir("..")

    log(Log.INFO, "Signing...\n")

    # Zipalign
    Runner.run(f"{build_tools_path} zipalign -p -f 4 temp/temp.zip resigned-app.apk")

    # Sign
    command = f"{build_tools_path} apksigner sign --ks {getAndroidKeystorePath()} --ks-pass pass:{KEY_PASS} --ks-key-alias {KEY_ALIAS} resigned-app.apk"
    Runner.run(command)

    log(Log.INFO, "Find file: resigned-app.apk\n")

    log(Log.INFO, "Done\n")
    
    Runner.exit()

##############################################################################################

PID, COMMAND_PROCESS_INFO_FOUND = hasCommand(["-process-info"])

if COMMAND_PROCESS_INFO_FOUND and PID:

    if hasAnyDevices() == False:
        Runner.exit()

    result = Runner.run(f"adb shell ps -A | grep {PID}")

    if not result:
        log(Log.INFO, "Process is not alive\n")
    else:
        log(Log.INFO, result)

    Runner.exit()

##############################################################################################

APK, COMMAND_CERT_INFO_FOUND = hasCommand(["-cert-info"])

if COMMAND_CERT_INFO_FOUND and APK:

    if hasAnyDevices() == False:
        Runner.exit()

    build_tools_path = getAndroidBuildToolsPath()

    # If cannot get path then exit
    if not build_tools_path:
        log(Log.INFO, "No build tool path. Stopped.")
        Runner.exit()

    result = Runner.run(f"{build_tools_path} apksigner verify --print-certs {APK}")

    log(Log.INFO, result)

    Runner.exit()

##############################################################################################

PACKAGE_NAME, COMMAND_PACKAGE_INFO_FOUND = hasCommand(["-package-info"])

if COMMAND_PACKAGE_INFO_FOUND and PACKAGE_NAME:

    if hasAnyDevices() == False:
        Runner.exit()

    result = Runner.run(f"adb shell dumpsys package {PACKAGE_NAME}")

    log(Log.INFO, Blue("START >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"))

    log(Log.INFO, result)

    log(Log.INFO, Blue("END >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"))

    Runner.exit()

##############################################################################################

APK_NAME, COMMAND_DECOMPILE_APK = hasCommand(["-decomp"])

if COMMAND_DECOMPILE_APK and APK_NAME:

    log(Log.INFO, f"Decompiling '{APK_NAME}'\n")

    destFolder = "output"

    apkTool = "apktool"
    apkToolJar = "apktool_2.11.0.jar"

    noFiles = os.path.isfile(apkTool) == False or os.path.isfile(apkToolJar) == False
    platfromIsSupported = isLinux() == True or isMac() == True
    
    if noFiles and platfromIsSupported:

        if os.path.isfile(apkTool) == False:

            log(Log.INFO, "Installing apktool for the first time...\n")     

            url = ""
            if isLinux():
                url = ("https://raw.githubusercontent.com/iBotPeaches/Apktool/master/scripts/linux/apktool")
            elif isMac():
                url = ("https://raw.githubusercontent.com/iBotPeaches/Apktool/master/scripts/osx/apktool")

            filename = "apktool"

            downloadFile(url=url, filename=filename) 

            #  7 - rwe
            #  4 - r
            os.chmod(filename, 0o744)    

        if os.path.isfile(apkToolJar) == False:

            url = (f"https://bitbucket.org/iBotPeaches/apktool/downloads/{apkToolJar}")
            filename = apkToolJar

            downloadFile(url=url, filename=filename) 

            #  7 - rwe
            #  4 - r
            os.chmod(filename, 0o744)   

    else:                        
        log(Log.ERROR, "Sorry, your platform is not supported. Script supports only Mac and Linux platfroms. Stopped.\n")
        Runner.exit()
                

    Runner.run(f"./apktool d {APK_NAME} -o {destFolder}")

    log(Log.INFO, Blue("Done. Find folder: '{destFolder}'\n"))

    Runner.exit()

##############################################################################################    

_, COMMAND_SYMB_CRASH_STACK = hasCommand(["-sym"])

if COMMAND_SYMB_CRASH_STACK:

    if len(sys.argv) < 4:
        log(Log.ERROR, "Sorry. Not enoght arguments.")            
        Runner.exit()

    mappingFile = sys.argv[2]
    crashLogFile = sys.argv[3]

    retrace_path = getRetracePath()
    outFile = "result.txt"

    Runner.run(f"java -jar {retrace_path}/retrace.jar {mappingFile} {crashLogFile} {outFile}")

    log(Log.INFO, Blue("Done. Find file: '{outFile}'\n"))

    Runner.exit()

##############################################################################################

APK_FOLDER, COMMAND_BUILD_APK = hasCommand(["-build"])

if COMMAND_BUILD_APK and APK_FOLDER:

    Runner.run(f"apktool -v b {APK_FOLDER}")

    log(Log.INFO, Blue("Done\n"))

    Runner.exit()

##############################################################################################

NEXT, COMMAND_SYM_NATIVE_CRASH = hasCommand(["-nsym"])
if COMMAND_SYM_NATIVE_CRASH:

    if len(sys.argv) < 4:
        log(Log.ERROR, "Not enough arguments. Sorry.\n")
        Runner.exit()    
    
    symbols = getArgWithValue("-nsym")
    logs = path(NEXT)
    ndk = getNDKPath()

    print(f"Folder with symbols: {symbols}")
    print(f"Crash logs: {logs}")
    print(f"NDK: {ndk}")

    # Example: /Users/sbutr/Library/Android/sdk/ndk/21.3.6528147/ndk-stack -sym ~/my-symbols/armeabi-v7a/ -dump ~/Desktop/crash 
    Runner.run(f"{ndk} ndk-stack -sym {symbols} -dump {logs}")

    log(Log.INFO, Blue("Done\n"))
    Runner.exit()

##############################################################################################

COMMAND_CONVERT_SMALI_TO_DEX = hasSingleCommand("-smalitodex")
if COMMAND_CONVERT_SMALI_TO_DEX:

    if len(sys.argv) < 7:
        log(Log.ERROR, "Not enough arguments. Sorry.\n")
        Runner.exit()   

    outFile = getArgWithValue("-o")    
    print("Out file: " + outFile)
  
    assembler = path(getArgWithValue("-jar"))  
    print(f"Using smali assembler: {assembler}")

    # Last argument is path to smali code
    filePath = path(sys.argv[6])
    print(f"Path to smali code: {filePath} \n")

    # Full commad example
    # java -jar /home/serhii/Downloads/smali/smali/build/libs/smali-3.0.9-dev-fat.jar assemble -o classes.dex ./output/smali_classes2/    
    Runner.run(f"java -jar {assembler} assemble -o {outFile} {filePath}")

    message = f"Do you want to open '{outFile}' using Jadx ?"
    if confirmAction(message):
        
        downloadJadx()

        log(Log.INFO, Blue("Opening...\n"))

        # java -jar jadx.jar classes.dex
        Runner.run(f"java -jar {jadxExecFileName} {outFile}")    

    log(Log.INFO, Blue("Done\n"))
    Runner.exit()

##############################################################################################

NEXT, COMMAND_JADX_OPEN = hasCommand(["-jadxopen"])
if COMMAND_JADX_OPEN:

    downloadJadx()

    # java -jar jadx.jar classes.dex
    Runner.run(f"java -jar {jadxExecFileName} {NEXT}") 

    log(Log.INFO, Blue("Done\n"))
    Runner.exit()        

# Looks like provided args are not correct, so show a help
log(Log.ERROR, "Sorry, cannot execute this command. \nPlease, make sure that the arguments are correct. A help:\n")
printHelp()