#!/bin/bash

# Lists basic bash features
# More info: http://linuxintro.org/wiki/Shell_scripting_tutorial

# Execute Shell script with utility functions needed for this script
. utility_functions.sh

print_script_debug_info

# --------------- 1. Defining arrays ---------------

names=(Tom John Sem Peter)

# Print all values and length of array
echo "Hello ${names[@]} ! There are ${#names} people."

# Print one by one value
echo "It's pleasent to see you ${names[0]}, ${names[1]}, ${names[2]} and ${names[3]} !"

# --------------- 2. Script parameters ---------------

# Prints all passed parameters if any
echo "Passed parameters: $@"

# Prints 1 parameter if any
echo "Your first parameter is $1"

MY_DIR_NAME="SamplDir"
# If directory does not exist then create it
[ -d $MY_DIR_NAME ] || mkdir $MY_DIR_NAME 

# --------------- 3. Strings ---------------

str1=""
str2="Example"
str3="Cool"

if [ "$str1" ]; then
	echo "Str1 is not empty"
fi

if [ -z "$str1"  ]; then
	echo "Str1 is empty"
fi

if [ "$str3" == "$str2"  ]; then
	echo "Str1 equals to Srt2"
fi

# --------------- 3. File attributes and checks ---------------

file1="./file_one"
file2="./file_two"

if [ -e $file_one  ]; then
	echo "File one exists"
fi
	
if [ -f "$file1" ]; then
	echo "$file1 is a normal file"
fi

if [ -r "$file1" ]; then
	echo "$file1 is readable"
fi

if [ -w "$file1" ]; then
	echo "$file1 is writable"
fi

if [ -x "$file1" ]; then
	echo "$file1 is executable"
fi

if [ -d "$file1" ]; then
	echo "$file1 is a directory"
fi

if [ -L "$file1" ]; then
	echo "$file1 is a symbolic link"
fi

if [ -p "$file1" ]; then
	echo "$file1 is a named pipe"
fi

if [ -S "$file1" ]; then
	echo "$file1 is a network socket"
fi

if [ -G "$file1" ]; then
	echo "$file1 is owned by the group"
fi

if [ -O "$file1" ]; then
	echo "$file1 is owned by the userid"
fi

# --------------- 4. Conditions ---------------

if [ $age -ge 16 ] # More or equal
then
    echo "You can drive"
elif [ $age -eq 15 ] # Equal
then 
    echo "You can drive next year"
else
    echo "You can not drive next year"
fi	