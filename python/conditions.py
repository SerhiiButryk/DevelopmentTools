#!/usr/bin/env python3

# Comparisons:
# Equal:            ==
# Not Equal:        !=
# Greater Than:     >
# Less Than:        <
# Greater or Equal: >=
# Less or Equal:    <=
# Object Identity:  is

# False Values:
    # False
    # None
    # Zero of any numeric type
    # Any empty sequence. For example, '', (), [].
    # Any empty mapping. For example, {}.

# and
# or
# not
# is

condition = False

if not condition:
    print('Evaluated to True')
else:
    print('Evaluated to False')


a = ['1', '2', '3']
b = ['1', '2', '3']

c = a

# Like object adress in other languages
print(id(a))
print(id(b))
print(id(c))

# True
print(a == b) 
# False
print(a is b) 
# True
print(a is c) 