# Search tool

# Simple search in file

$ grep -inw "John Williams" note.txt

# Search in current directory including all text files

$ grep -inw -C 2 "John Williams" ./*.txt

# Search in curent directory and its subdirectories

$ grep -inwr -C 2 "John Williams" .

# Show file names which contain a match

$ grep -ilwr -C 2 "John Williams" .

# Show file names which contain a match with number of macthes

$ grep -icwr -C 2 "John Williams" .

# Invert match show lines which DOEAN'T contain a match word

$ grep -ivwr "John Williams" .

# Show lines 5 which contains a word 'git' in a history

history | grep -m 5 git

history | grep --max-count=5 git

# Show only mathing word and don't print full line

grep -wiro -C 2  "Tom" .

# Search for a word in .txt files

grep -iwro --include=\*.txt "Tom"

# Search for a word in .txt and .cpp files

grep -iwro --include=\*.{txt,cpp} "Tom"

# Search for a word in .txt and .cpp files and exclude dir

grep -iwro --include=\*.{txt,cpp} --exclude-dir=build "Tom"

grep -iwro --include=\*.{txt,cpp} --exclude-dir={build, temp} "Tom"

# Find a line which contian vivek or raj word (OR option)
 
grep -E 'vivek|raj' temp.txt

# All the lines that contain both “Dev” and “Tech” in it (in the same order (AND option)

grep -E 'Dev.*Tech' employee.txt

# Find only line which begging with vivek word

grep ^vivek /etc/passwd

# Find only line which ending with vivek word

grep vivek$ /etc/passwd

Guides:
https://www.cyberciti.biz/faq/grep-regular-expressions/