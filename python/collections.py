#!/usr/bin/env python3

############################
# List
############################

list = [1, 2, 3, 4, 5]
print(list)

# Length of the list
print(len(list))

# Accessing elements
print(list[0]) # First element
print(list[4]) # Last element
print(list[-1])  # Last element

# Slicing
print(list[0:3])  # First 3 elements

# Modifying elements
list.append(6)  # Add an element
list.insert(0, 1)  # Insert '1' at the index 0
print(list)

# Add a list to another list
list.extend([7, 8, 9])
print(list)

# Sorting

list = [3, 4, 1, 0, 2 ]

list.sort()  # Sort the list in ascending order
print(list)

list.sort(reverse=True)  # Sort the list in descending order
print(list)

list = [3, 4, 1, 0, 2 ]

sorted_list = sorted(list)  # Original is not modified
print(list)  # Original list
print(sorted_list)  # Sorted list

# Element index
print(list.index(4))

# Iteration
print("Iteration")
for item in list:
    print(item)

print("Iteration with index")
for index, item in enumerate(list):
    print(index, item)    


############################
# Tuple 
############################

# Immutable
tuple_1 = ('One', 'Two', 'Three')    
print(type(tuple_1))
print(tuple_1)

############################
# Set
############################

set_1 = {'One', 'Two', 'Three'}
print(type(set_1))
print(set_1)

############################
# Dictionary
############################

map = { 'key':'val', 'key2':'val2' }
print(type(map))
print(map)