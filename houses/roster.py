import sys
from sys import argv, exit
from cs50 import SQL

# Parse Command Line parameter abd get house name
if len(sys.argv) != 2:
    print("Error: Please pass in a house name!")
    exit(1)
house_name = sys.argv[1]

# Initialize cursor to execute db commands
db = SQL("sqlite:///students.db")

# Goal: Find all Students in students.db for specified house name
command = f"SELECT first, middle, last, birth from students WHERE house = \"{house_name}\" ORDER BY last, first"

students = db.execute(command)
output_dict = {}
for student in students: 
    # construct full name from sele   ct statement output
    full_name = ""
    if student['middle'] != "NULL":
        full_name = f"{student['first']} {student['middle']} {student['last']}"
    else:
        full_name = f"{student['first']} {student['last']}"
    
    # skip if full name has already been seen
    if full_name in output_dict: 
        continue
    
    # print output data for unseen full name
    output_dict[full_name] = f"{full_name}, born {student['birth']}"
    print(f"{output_dict[full_name]}")
    
#credit to https://www.python-course.eu/sql_python.php for their explaination on how to cleanly execute an sql command within a python document
#credit to https://www.digitalocean.com/community/tutorials/how-to-format-text-in-python-3 for the syntax of having escape double quotes