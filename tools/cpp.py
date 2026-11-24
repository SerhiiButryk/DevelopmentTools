#!/usr/bin/env python3

import os
import argparse
import sys
import subprocess

# 
# A tool for C/C++ program debugging or analysis
#
# TODO: Add otool for macOS support

def Red(mes): return "\033[91m{}\033[00m" .format(mes)
def Green(mes): return "\033[92m{}\033[00m" .format(mes)

parser = argparse.ArgumentParser(prog='cpptool.py',
                        description='A tool for C/C++ program debugging or analysis', 
                        usage="cpptool.py [options...]", 
                        formatter_class=argparse.RawTextHelpFormatter,
                        # Get rid of "unrecognized arguments" message when unknown arg is passed
                        exit_on_error=False)

def initParser() -> None:    
    
    def path_arg(arg):
            return os.path.realpath(os.path.expanduser(arg))

    parser.add_argument('-search-sym', 
                        nargs=2,  # Check if correct !
                        required=False,
                        metavar="",
                        default=None,
                        help='display symbol table for the provided file (.so or .a lib)\n' +
                        Green('example: $ cpptool.py -search-sym my_lib.a my_magic_symbol \n'))
    
    parser.add_argument('-n', 
                        nargs=1,  # Check if correct !
                        required=False,
                        metavar="",
                        default=None,
                        help='number of lines to display\n' +
                        Green('example: $ cpptool.py -n 10 -search-sym my_lib.a my_magic_symbol\n'))
    
    parser.add_argument('-show-dep-libs', 
                        nargs=1, # Check if correct !
                        required=False,
                        metavar="",
                        default=None,
                        help='display libraries which the provided file depends on\n' +
                        Green('example: $ cpptool.py -show-dep-libs my_prog\n'))
    
    parser.add_argument('-show-file-info', 
                        nargs=1, # Check if correct !
                        required=False,
                        metavar="",
                        default=None,
                        help='display all available header information, including the symbol table and relocation entries\n' +
                        'See refs & docs:\n' +
                        'https://man7.org/linux/man-pages/man5/elf.5.html\n' +
                        'https://medium.com/@allypetitt/reverse-engineering-analyzing-headers-23dc84075cd\n' +
                        Green('example: $ cpptool.py -show-file-info my_prog\n'))

    parser.add_argument('-print-sym-table', 
                        nargs=1, # Check if correct !
                        required=False,
                        metavar="",
                        default=None,
                        help='Print the symbol table entries of the file\n' +
                        Green('example: $ cpptool.py -print-sym-table my_prog\n'))                        

def parseArgs():
    return parser.parse_args()

def checkArgsNotEmpty() -> None:
    # No arguments are provided. Show help.
    if len(sys.argv) == 1:
        parser.print_help()        
        exit()

#
# Helper for shell command execution
#

class Runner:

    # Executes shell command
    @classmethod
    def run(cls, command, logs = False):
        return cls.runInternalUsingCheckoutRun(command, logs)

    # Executes shell command
    @classmethod
    def runInternalUsingSubprocessRun(cls, command, logs):
        if logs:
            print(Green("Executing command: " + str(command) + "\n"))
        result = subprocess.run(command)
        if logs:
            print(Green("Done. Code: " + str(result.returncode) + "\n")) 

    @classmethod
    def runInternalUsingCheckoutRun(cls, command, logs):
        if logs:
            print(Green("Executing command: " + str(command) + "\n"))
        
        text_result = ""

        try:
            output = subprocess.check_output(["bash", "-c", command])
            text_result = output.decode('utf-8')
        except subprocess.CalledProcessError:
            if logs:
                print("Command retruned non zero status code")

        return text_result

#
# Start of the program
#

initParser()
checkArgsNotEmpty()

try:
    # Get arguments
    args = parseArgs()

    # Display symbol table for the provided file (.so or .a lib)
    if args.search_sym:

        numberOfLines = ""
        if args.n:
            numberOfLines = "| head -n " + args.n[0]

        fileName = args.search_sym[0]
        symbol = args.search_sym[1]
        
        result = Runner.run("objdump -tC " + fileName + " | grep --color " + symbol + " " + numberOfLines)
        print(result)

        print(Green("Done"))
        exit()

    if args.show_dep_libs:

        fileName = args.show_dep_libs[0]

        result = Runner.run("ldd " + fileName)
        print(result)

        print(Green("Done"))
        exit()

    if args.show_file_info:

        fileName = args.show_file_info[0]

        result = Runner.run("objdump -x " + fileName)
        print(result)

        print(Green("Done"))
        exit()    

    if args.print_sym_table:

        fileName = args.print_sym_table[0]

        result = Runner.run("objdump -t " + fileName)
        print(result)

        print(Green("Done"))
        exit()

except argparse.ArgumentError:
    print("Sorry, you have provided incorrect arguments. See the help.\n")     
    parser.print_help()
    exit()