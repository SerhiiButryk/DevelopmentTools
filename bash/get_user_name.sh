#!/bin/bash 

# Author: Serhii Butryk
#
# Program demontraits basic structure of Bash programm language.
# Program asks for User name information from User and dispays it on the screen.

# Execute Shell script with utility functions needed for this script
. utility_functions.sh

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

print_script_debug_info

ask_for_user_names "" ""
print_names

print_message "Finished"