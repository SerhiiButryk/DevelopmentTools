# 
# This is a basic Makefile example 
#
# Good tutorials on Makefiles: 
# https://makefiletutorial.com/
# 

# Run make file (this command will execute the default target, which is 'test' in this case):
# $ make
# 
# To run specific target (e.g., 'hello'):
# $ make hello
# 

# Variables
var = Hello, World!
var2 = My message:

# Helpfull functions
#
# 1. $(wildcard pattern) - This function returns a space-separated list of files that match the given pattern.

test: test.o
	@echo "Running target '$@'"
	
	g++ test.o -o test

test.o: test.cpp	 
	@echo "Running target '$@'"
	
	g++ -c test.cpp -o test.o

test.cpp:
	@echo "Running target '$@'"
	
	echo '#include <iostream>' > test.cpp
	echo 'int main() {' >> test.cpp
	echo '    std::cout << "Hello, World!" << std::endl;' >> test.cpp
	echo '    return 0;' >> test.cpp
	echo '}' >> test.cpp

hello:
# Do not print this command itself to the console, only execute the command
	@echo "$(var2)" $(var)

hey: one two
# Outputs "hey", since this is the target name
	@echo $@

# Outputs all prerequisites newer than the target
	@echo $?

# Outputs all prerequisites
	@echo $^

# Outputs the first prerequisite
	@echo $<

	touch hey

one:
	touch one

two:
	touch two
