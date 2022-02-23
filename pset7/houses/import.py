import sys, csv, json
from sys import argv
from cs50 import SQL

if len(sys.argv) != 2:
    print("Error:")
    exit()
file_name = sys.argv[1]

with open(file_name, "r") as file:
    dict_reader = csv.DictReader(file)

    # initialize db cursor
    db = SQL("sqlite:///students.db")

    for index, person in enumerate(dict_reader):
        # get names for each person
        full_name = person["name"]
        house, birth = person["house"], person["birth"]
        first_name, last_name, middle_name = "","",""

        # split the name based on number of spaces
        names = full_name.split()

        # check if full_name has a middle name
        if len(names) == 2:
            first_name, middle_name, last_name = names[0], "NULL", names[1]
        elif len(names) == 3:
            first_name, middle_name, last_name = names[0], names[1], names[2]
        print(first_name, middle_name, last_name)

        sql_command = f"INSERT INTO students(first, middle, last, house, birth) VALUES (\'{first_name}\', \'{middle_name}\', \'{last_name}\', \'{house}\', \'{birth}\')"

        db.execute(sql_command)
#credit to https://www.python-course.eu/sql_python.php for their explaination on how to cleanly execute an sql command within a python document
#credit to https://www.digitalocean.com/community/tutorials/how-to-format-text-in-python-3 for the syntax of having escape double quotes