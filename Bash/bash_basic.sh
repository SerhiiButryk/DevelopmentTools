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

# --------------- 5. Output redirectios ---------------

stdin - 0
stdout - 1
stderr - 2

/dev/null - Virtual device (or virtual file) where anyone can write to. All written data is discarded. 

Examples:

# Show error output
$ ping google.com 1> /dev/null 

# Show non error output
$ ping google.com 2> /dev/null 

# Redirect stdout and stderr to /dev/null
$ ping google.com > /dev/null 2>&1

# Get input from a file
$ cat < hello.txt

# --------------- 6. Piping ---------------

# If you want to pipe both the stderr and stdout to the next command, then use the “|&” instead.

Examples:

# Piping stdout and stderr to cat command
$ anything |& cat