#!/usr/bin/env python3

import subprocess
import sys
import inspect
import os
import shutil
from typing import List, Tuple, Optional
from urllib.request import urlretrieve
from zipfile import ZipFile
import ssl
import zipfile

# 
# Utility script for Android
# 
# It is developed for Linux and Mac OS
# Note that some bugs could be present.
#

# Global variables
jadxExecFileName = "jadx.jar"   

smaliDecompilerVersion = "3.0.6"
smaliDecompilerDestFile = "smali_decoder.zip"
smaliFatJarName = "smali-3.0.5-dev-fat.jar"

def Red(mes: str) -> str:
    return f"\033[91m{mes}\033[00m"

def Green(mes: str) -> str:
    return f"\033[92m{mes}\033[00m"

def Blue(mes: str) -> str:
    return f"\033[0;34m{mes}\033[00m"

# 
# A help 
# 

def printHelp() -> None:

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

    -enter-creds - Enters the next 2 strings in the 2 text fields if has focus
    Example: ./android.py -enter-creds email:password

    -process-info - Shows process info for a process
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

    -smalitodex -o classes.dex ./smali_classes/ - Convert SMALI to DEX
    Example: ./android.py -smalitodex -o classes.dex ./smali_classes/
    Hint:
    Smali assembler github repo: https://github.com/google/smali/tree/main

    -jadxopen classes.dex - Open DEX file using Jadx
    Example: ./android.py -jadxopen classes.dex
    Hint:
    Jadx github repo: https://github.com/skylot/jadx
    """ + '\n'
    )

# 
# Constants
# 

KEY_PASS: str = "android"
KEY_ALIAS: str = "androiddebugkey"

#
#  Functions & Classes
# 

# 
# Log utility
# 

class Log:
    INFO: int = 1
    ERROR: int = 2
    DEBUG: int = 3
    DEBUG_MODE: bool = False

def log(level: int, message: str) -> None:
    formatted: str = ""

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
        lineNumber: int = inspect.stack()[1][2]
        print(f"{lineNumber} {formatted}", end="")
    else:
        print(formatted, end="")

def hasCommand(args_list: List[str]) -> Tuple[str, bool]:
    """
    Check if any of the provided arguments exist in the command-line arguments.
    """
    for arg in sys.argv[1:]:
        if arg in args_list:
            # Return the next argument if it doesn't start with '-'
            next_arg = sys.argv[sys.argv.index(arg) + 1] if sys.argv.index(arg) + 1 < len(sys.argv) and not sys.argv[sys.argv.index(arg) + 1].startswith('-') else ""
            return next_arg, True
    return "", False

def hasSingleCommand(arg: str) -> bool:
    """
    Check if a single command exists in the command-line arguments.
    """
    return arg in sys.argv[1:]

def getArgWithValue(argument: str) -> str:
    """
    Get the value associated with a specific argument.
    """
    try:
        index = sys.argv.index(argument)
        return sys.argv[index + 1] if index + 1 < len(sys.argv) else ""
    except ValueError:
        return ""

def path(arg: str) -> str:
    return os.path.realpath(os.path.expanduser(arg))

class Runner:

    # Executes shell command
    @classmethod
    def run(cls, command: str, logs: bool = False) -> str:
        return cls.runInternalUsingCheckoutRun(command, logs)

    # Executes shell command
    @classmethod
    def runInternalUsingSubprocessRun(cls, command: str, logs: bool) -> None:
        if logs:
            log(Log.INFO, Blue(f"Executing command: {command}\n"))
        result = subprocess.run(command)
        if logs:
            log(Log.INFO, Blue(f"Done. Code: {result.returncode}\n")) 

    @classmethod
    def runInternalUsingCheckoutRun(cls, command: str, logs: bool) -> str:
        if logs:
            log(Log.INFO, Blue(f"Executing command: {command}\n"))
        
        text_result: str = ""

        try:
            output = subprocess.check_output(["bash", "-c", command])
            text_result = output.decode('utf-8')
        except subprocess.CalledProcessError:
            if logs:
                log(Log.INFO, "Command returned non-zero status code")

        return text_result

    @classmethod
    def exit(cls) -> None:
        log(Log.INFO, "Stopped.\n")
        # End the program
        sys.exit()

def isLinux() -> bool:
    return sys.platform == 'linux'

def isMac() -> bool:
    return sys.platform == 'darwin'

def getAndroidBuildToolsPath() -> str:
    android_home: Optional[str] = os.getenv('ANDROID_HOME')
    if android_home is None:
        log(Log.ERROR, "Variable 'ANDROID_HOME' is not set. Please, set this variable.")
        return ""

    command: str = ""

    if isLinux():
        # On Linux you need to add '-P' option for 'grep'
        # Grep matching string like this "35.0.0"
        command = f"ls {android_home}/build-tools | sort -r | grep -P '\\d{{2}}.\\d{{1}}.\\d{{1}}$' | head -n 1"
    
    elif isMac():
        # On MAC you need to add '-E' option for 'grep'
        # Grep matching string like this "35.0.0"
        command = f"ls {android_home}/build-tools | sort -r | grep -E '\\d{{2}}.\\d{{1}}.\\d{{1}}$' | head -n 1"

    else:
        log(Log.ERROR, "getAndroidBuildToolsPath(): Failed to select OS")
    
    version: str = Runner.run(command)

    path: str = f"{android_home}/build-tools/{version.strip()}/"
    
    log(Log.INFO, f"Selected build tools: '{path}'\n")
    return path

def getNDKPath() -> str:
    android_home: Optional[str] = os.getenv('ANDROID_HOME')
    if android_home is None:
        log(Log.ERROR, "Variable 'ANDROID_HOME' is not set. Please, set this variable.")
        return ""

    command: str = ""

    if isLinux():
        # On Linux you need to add '-P' option for 'grep'
        # Grep matching string like this "35.0.0"
        command = f"ls {android_home}/ndk | sort -r | grep -P '\\d{{2}}.\\d{{1}}.\\d{{7}}$' | head -n 1"
    
    elif isMac():
        # On MAC you need to add '-E' option for 'grep'
        # Grep matching string like this "35.0.0"
        command = f"ls {android_home}/ndk | sort -r | grep -E '\\d{{2}}.\\d{{1}}.\\d{{7}}$' | head -n 1"

    else:
        log(Log.ERROR, "getNDKPath(): Failed to select OS")

    version: str = Runner.run(command)

    return f"{android_home}/ndk/{version.strip()}/"

# Get default Android keystore path
def getAndroidKeystorePath() -> str:
    KEY_STORE_PATH: str = ".android/debug.keystore"

    home: Optional[str] = os.getenv('HOME')   
    if home is None:
        log(Log.ERROR, "Variable 'HOME' is not set. Please, set this variable.")
        return "" 
    
    keystore: str = f"{home}/{KEY_STORE_PATH}"

    log(Log.INFO, f"Selected key store file: '{keystore}'\n")

    return keystore

def getRetracePath() -> str:
    android_home: Optional[str] = os.getenv('ANDROID_HOME')
    if android_home is None:
        log(Log.ERROR, "Variable 'ANDROID_HOME' is not set. Please, set this variable.")
        return ""

    return f"{android_home}tools/proguard/lib"

def hasAnyDevices() -> bool:
    """
    Check if any devices are connected via ADB.
    """
    res: str = Runner.run("adb devices")
    # Count lines excluding the header and empty lines
    if len([line for line in res.splitlines() if line.strip()]) > 1:
        return True

    log(Log.ERROR, "Device list is empty. No devices currently available.\n")
    return False

def downloadFile(url: str, filename: str) -> bool:
    """
    Download a file from the given URL and save it to the specified filename.
    """
    
    if os.path.exists(filename):
        log(Log.INFO, f"File '{filename}' already exists. Skipping the download.\n")
        return True

    log(Log.INFO, f"Started the downloading: {url}, file name: {filename}\n")

    try:
        # Create an unverified SSL context to work around SSL certificate issues
        ssl._create_default_https_context = ssl._create_unverified_context
        file, headers = urlretrieve(url, filename)
        content_length = headers.get("Content-Length")
        # Content length could be None for example for zip files
        # Check this case and do not fail the download
        if content_length:
            if content_length and int(content_length) > 0:
                log(Log.INFO, f"Success: Content-Length: {content_length}\n")
                return True
            log(Log.ERROR, "Content length is 0. Failed.\n")
            return False
        return True
    except Exception as e:
        log(Log.ERROR, f"Failed to download file. Error: {e}\n")
        return False

def confirmAction(message: str) -> bool:
    """
    Prompt the user for confirmation with a yes/no question.
    """
    user_input: str = input(f"{message} (yes/no): ").strip().lower()
    return user_input == "yes"

# See https://github.com/google/smali
def downloadSmaliDecompiler() -> bool:
    return downloadFile(f"https://github.com/google/smali/archive/refs/tags/{smaliDecompilerVersion}.zip", smaliDecompilerDestFile)

def buildSmaliFatJar() -> bool:

    if os.path.exists(smaliFatJarName):
        log(Log.INFO, f"Fat jar is present. Skipping build step.\n")
        return True

    if not os.path.exists(smaliDecompilerDestFile):
        log(Log.ERROR, f"File '{smaliDecompilerDestFile}' does not exist. Cannot build smali fat jar.\n")
        return False

    tempDir = "smali-decompiler-temp"

    # Unzip file
    Runner.run(f"unzip -q {smaliDecompilerDestFile} -d {tempDir}/")

    # Build fat jar

    os.chdir(f"./{tempDir}/smali-{smaliDecompilerVersion}")
    os.chmod("./gradlew", 0o744)
    
    Runner.run("./gradlew smali:fatJar")

    # Copy fat jar
    shutil.copy(src=f"./smali/build/libs/{smaliFatJarName}", dst="../../")

    os.chdir("../../")
    
    # Clear files
    shutil.rmtree(f"./{tempDir}")

    log(Log.INFO, Blue("Built successfully !!!\n"))

    return True
    
def downloadJadx() -> bool:

    jadxLink: str = "https://github.com/skylot/jadx/releases/download/v1.5.1/jadx-1.5.1.zip"
    jadxDownloadedFile: str = "jadx.zip"
    fileToExtract: str = "lib/jadx-1.5.1-all.jar"

    if not os.path.isfile(jadxExecFileName):
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
            os.remove(path(f"./{jadxDownloadedFile}"))

    else:
        log(Log.INFO, "Launching Jadx...\n")    

    return True            

#
# Start of the program
#

TEXT, COMMAND_ENTER_TEXT_FOUND = hasCommand(["-i", "-input"])

if COMMAND_ENTER_TEXT_FOUND:
    
    if not hasAnyDevices():
        Runner.exit()

    log(Log.INFO, f"Entering: '{TEXT}'\n")
    
    Runner.run(f"adb shell input text {TEXT}")
    
    log(Log.INFO, "Done\n")
    
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
    if not hasAnyDevices():
        Runner.exit()

    commands = [
        ("adb shell getprop ro.build.version.release", "Release version"),
        ("adb shell getprop ro.build.version.release_or_codename", "Release or code name version"),
        ("adb shell getprop ro.build.id", "Build ID"),
        ("adb shell getprop ro.product.manufacturer", "Manufacturer"),
        ("adb shell getprop ro.product.model", "Device model"),
        ("adb shell getprop ro.product.cpu.abilist", "Supported ABI list"),
        ("adb shell getprop ro.build.version.sdk", "SDK version"),
    ]

    for cmd, description in commands:
        result = Runner.run(cmd)
        log(Log.INFO, f"{Blue(description)}: {result}")

    Runner.exit()

##############################################################################################

TEXT, COMMAND_ENTER_CREDENTIALS_FOUND = hasCommand(["-enter-creds"])

if COMMAND_ENTER_CREDENTIALS_FOUND:

    if not hasAnyDevices():
        Runner.exit()

    try:
        
        username, password = TEXT.split(":")
        
        log(Log.INFO, "Entering text...\n")
        
        Runner.run(f"adb shell input text {username}")
        Runner.run("adb shell input keyevent 66")
        Runner.run(f"adb shell input text {password}")
        Runner.run("adb shell input keyevent 66")
        
        log(Log.INFO, "Done\n")

    except ValueError:
        log(Log.ERROR, "Invalid credentials format. Use 'username:password'.\n")

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
    platfromIsSupported = isLinux() or isMac()
    
    if not platfromIsSupported:
        log(Log.ERROR, """Sorry, your platform is not supported. 
            Only Mac and Linux platfroms are supported. Stopped.\n""")
        Runner.exit()

    if noFiles:

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
                
    Runner.run(f"./apktool d {APK_NAME} -o {destFolder}")

    log(Log.INFO, Blue(f"Done. Find folder: '{destFolder}'\n"))

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

    if len(sys.argv) < 5:
        log(Log.ERROR, "Not enough arguments.\n")
        Runner.exit()   

    if not downloadSmaliDecompiler():
        Runner.exit()    

    if not buildSmaliFatJar():
        Runner.exit()   

    outFile = getArgWithValue("-o")    
    print("Out file: " + outFile)

    # Last argument is path to smali code
    filePath = path(sys.argv[6])
    print(f"Path to smali code: {filePath} \n")

    # Full commad example
    # java -jar ./smali-3.0.9-dev-fat.jar assemble -o classes.dex ./output/smali_classes2/    
    Runner.run(f"java -jar {smaliFatJarName} assemble -o {outFile} {filePath}")

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