#!/bin/bash 

# Author: Serhii Butryk
#
# Program demontraits basic structure of Bash programm language.
# Program asks for User name information from User and dispays it on the screen.

# Utils functions

# Prints text in green color
print_message() {

    # https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
    GREEN_COLOR='\033[0;32m'
    RESET_COLOR='\033[0m'

    # Format passed text
    printf "${GREEN_COLOR}$1${RESET_COLOR}\n"

}

# Prints text in red color
print_error() {
    
    # https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
    RED_COLOR='\033[0;31m'
    RESET_COLOR='\033[0m'

    # Format passed text
    printf "${RED_COLOR}$1${RESET_COLOR}\n"

}

# Global variables
SCRIPT_FILE_NAME=$0
SCRIPT_WORING_DIRECTORY=$(pwd)

# Print info about this script
print_message "Running script: $SCRIPT_FILE_NAME in directory $SCRIPT_WORING_DIRECTORY"

# Global variables
USER_NAME=""
USER_SUNAME=""

ask_for_user_names() {
    
    # If one argument are provided
    if [ -n $1 ]; then
        echo "Please, enter your user name"
        read USER_NAME
    fi

    # If second argument are provided
    if [ -n $2 ]; then  
        echo "Please, enter your user suname"
        read USER_SUNAME
    fi

}

print_names() {
    
    # If entered user name is empty
    if [ -z $USER_NAME ]; then 
        print_error "Sorry, you've entered empty user name"
    else 
        echo "Your name is $USER_NAME"
    fi    


    # If entered user suname is empty
    if [ -z $USER_SUNAME ]; then 
        print_error "Sorry, you've entered empty user suname"
    else 
        echo "Your suname is $USER_SUNAME"
    fi

}

ask_for_user_names "" ""
print_names

print_message "Finished"