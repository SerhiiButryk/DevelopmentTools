#!/usr/bin/env python3

# Classes in Python

class Employee:
    pass

empl1 = Employee()
empl2 = Employee()

print(empl1)
print(empl2)

# Setting attributes

empl1.name = "Employee 1"
empl2.name = "Employee 2"

print(empl1.name)
print(empl2.name)

# Using init and other methods

class People:

    # This is simmilar to static variables in other languages
    company_name = "Cool company Inc."

    def __init__(this, first_name, last_name) -> None:
        this.first_name = first_name
        this.last_name = last_name

    def fullName(this):
        return '{} {}'.format(this.first_name, this.last_name)
    
    # This is simmilar to static method in other languages
    @classmethod
    def getCompanyName(cls):
        return cls.company_name
    
    # This is simmilar to static method in other languages
    @classmethod
    def setCompanyName(cls, new_company_name):
        cls.company_name = new_company_name

john = People("John", "Smith")
tom = People("Tom", "Smith")

print(john.fullName())
print(tom.fullName())

# Can access without class instance

print(People.company_name)

# Can change a company name

People.setCompanyName("Supercool company Inc.")

print(john.company_name)
print(tom.company_name)