#!/usr/bin/env python3

# Strings in Python

message = 'Hello "world"'
message = "Hello 'world'"

message = '''
Multi
line
'''

message = """
Multi
line
"""

print(message)

# String methods

message = 'Hello world'

print(len(message))  # Length of the string
print(message[0])  # First character
print(message[10])  # Last character

# String ranges/slices

print(message[0:5])  # First 5 characters
print(message[:5])  # The same as above
print(message[6:])  # From 6 to the end

# Print all methods of the object
print(dir(message))

# Print help
print(help(message))