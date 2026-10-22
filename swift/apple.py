#!/usr/bin/env python3

from pathlib import Path
import subprocess
import sys
import inspect
import os
from typing import List, Tuple
from zipfile import ZipFile
import ssl
from urllib.request import urlretrieve

def Red(mes: str) -> str:
    return f"\033[91m{mes}\033[00m"

def Green(mes: str) -> str:
    return f"\033[92m{mes}\033[00m"

def Blue(mes: str) -> str:
    return f"\033[0;34m{mes}\033[00m"

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

# 
# A help 
# 

def help() -> None:

    log(Log.INFO,         
    """
A tool for Apple apps debugging.
Usage: ./apple.py [options...]

-plist - Parses a plist file and prints its content
Example: ./apple.py -plist info.plist

-hmacho - Parses a Mach-O binary and prints its content
Example: ./apple.py -hmacho myapp

-disraw - Disassembles a Mach-O binary TEXT section and prints its content
Example: ./apple.py -disraw myapp

-decompiler - Open decompiler (Ghidra) for reverse engineering
Example: ./apple.py -decompiler

-xcodetools - Prints the path to Xcode tools
Example: ./apple.py -xcodetools

-demangle - Demangle Swift symbols
Example: ./apple.py -demangle 'symbol_name'

-sil - Print Swift SIL code
Example: ./apple.py -sil 'file.swift'
    """ + '\n')

#
# Args parse utility
# 

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

#
# Commands runner utility
# 
class Runner:

    # Executes shell command
    @classmethod
    def run(cls, command: str, logs: bool = False) -> str:
        return cls.runInternalUsingCheckoutRun(command, logs)

    # Executes shell command
    @classmethod
    def runInternalUsingSubprocessRun(cls, command: str, logs: bool = False) -> None:
        if logs:
            log(Log.INFO, Blue(f"Executing command: {command}\n"))
        result = subprocess.run(command, shell=True)
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

def processCommand(commands: list[str], actionFunc, hasArgs = True) -> None:
    arg, isFound = hasCommand(commands)  
    
    if not isFound:
        return
    
    if hasArgs and not arg:
        return    
    
    actionFunc(arg)
    Runner.exit()          

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

# -------------------------------------------------------------------
# Start of the program
# -------------------------------------------------------------------

def processPlistCommand(fileIn) -> None:

    fileOut = "output.txt"
    
    command = f"plutil -p {fileIn} > {fileOut}"
    Runner.runInternalUsingSubprocessRun(command)

    log(Log.INFO, Blue(f"Done. Output saved to {fileOut}\n"))

processCommand(["-plist"], processPlistCommand)

# -------------------------------------------------------------------

def processHMachCommand(args) -> None:
    
    command = f"otool -h {args}"
    result = Runner.run(command)

    log(Log.INFO, f"{result}\n")

processCommand(["-hmacho"], processHMachCommand)

# -------------------------------------------------------------------

def processDisrawCommand(args) -> None:

    fileOut = "output.txt"
    
    command = f"otool -XVvt {args} > {fileOut}"
    Runner.runInternalUsingSubprocessRun(command)

    log(Log.INFO, Blue(f"Done. Output saved to {fileOut}\n"))

processCommand(["-disraw"], processDisrawCommand)

# -------------------------------------------------------------------

def openghidraCommand(args) -> None:

    version = "12.1.4"
    link = "https://github.com/NationalSecurityAgency/ghidra/releases/download/Ghidra_12.1.4_build/ghidra_12.1.4_PUBLIC_20260921.zip"

    macHome = Path.home()
    
    decomDir = Path(f"{macHome}/.tmp/ghidra_{version}")
    decomDir.mkdir(parents=True, exist_ok=True)

    executable = Path(f"{decomDir}/ghidra_{version}_PUBLIC/ghidraRun")
    
    if not executable.exists():
        
        log(Log.INFO, "Ghidra decompiler is not found. Downloading...\n")

        # Download and extract Ghidra
        filelocation = Path(f"{decomDir}/ghidra.zip")
        downloadFile(link, filelocation)  

        log(Log.INFO, "Unzipping...\n")

        # Unzip the downloaded file
        with ZipFile(Path(filelocation), 'r') as zObject: 
            zObject.extractall(path=decomDir) 
            zObject.close()    

        # Remove quarantine attribute to avoid security warnings
        cleanUp = f"xattr -r -d com.apple.quarantine {decomDir}/ghidra_{version}_PUBLIC"    
        Runner.runInternalUsingSubprocessRun(cleanUp) 

        # Make the executable file executable
        executable.chmod(0o755)  
        launcherExecutable = f"{decomDir}/ghidra_{version}_PUBLIC/support/launch.sh"
        Path(launcherExecutable).chmod(0o755)

        os.chdir(f"{decomDir}/ghidra_{version}_PUBLIC/support/gradle/")

        # Building native components
        Path("gradlew").chmod(0o755)
        Runner.runInternalUsingSubprocessRun("./gradlew buildNatives")  

        executable = "../../ghidraRun" 

    log(Log.INFO, Blue(f"Opening... \nDARK THEME: Starting window -> Edit -> Theme -> Switch\n"))

    command = f"{executable} &>/dev/null &"
    Runner.runInternalUsingSubprocessRun(command)

processCommand(["-decompiler"], openghidraCommand, hasArgs=False)

# -------------------------------------------------------------------

def processXcodeToolsCommand(args) -> None:
    
    command = f"xcode-select -p"
    result = Runner.run(command)

    log(Log.INFO, f"{result}")

processCommand(["-xcodetools"], processXcodeToolsCommand, hasArgs=False)

# -------------------------------------------------------------------

def processDemangleCommand(args) -> None:
    
    command = f"xcrun swift-demangle '{args}'"
    result = Runner.run(command)

    log(Log.INFO, f"{result}")

processCommand(["-demangle"], processDemangleCommand)

# -------------------------------------------------------------------

def processSILCommand(args) -> None:
    
    command = f"swiftc -emit-sil '{args}' | xcrun swift-demangle > output.sil"
    result = Runner.run(command)

    log(Log.INFO, f"{result}")

processCommand(["-sil"], processSILCommand)

# Looks like provided args are not correct, so show a help
log(Log.ERROR, "Sorry, cannot execute this command. \nPlease, make sure that the arguments are correct. A help:\n")
help()    