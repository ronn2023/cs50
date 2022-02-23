import sys
from sys import argv, exit
import csv
import re
import math


#Make sure user inputs both a csv and sequence file
if len(sys.argv) != 3:
    print("Error: Please input a csv and sequence file!")
    exit()

#Open csv file and store the strs in a list
file = open(argv[1], "r")
line = file.readline()
line = line.rstrip("\n")
strs = list(line.split(','))
strs.remove('name')
length_plus1 = len(strs) + 1

#Open dna file
dna_file = open(argv[2], "r")
dna_file_line = dna_file.read().replace("\n", " ")
number = []

#Calculate amount of times dna sequence is repeated and store the values in a list
for x in range(1,length_plus1):
    #Credit to this website for their expression on how to find the number of sequentially repeated substrings within a string
    #https://www.quora.com/How-do-I-get-the-number-of-consecutive-occurrences-of-a-substring-in-a-string-using-Python
    #Post was by Mark Gritter

    pattern = "(?=((" + re.escape(strs[x-1]) + ")+))"
    matches = re.findall( pattern, dna_file_line)
    if len(matches) == 0:
        continue
    number.append(max( len( m[0] ) // len( strs[x-1] ) for m in matches))

#Compare the outputed list with the csv file's values
for file_csv_line in file:
    file_csv_line = file_csv_line.rstrip('\n')
    compare_str = list(file_csv_line.split(','))
    name = compare_str[0]
    del compare_str[0:1]
    for i in range ( len(compare_str) ):
        compare_str[i] = int(compare_str[i])
    #if lists are the same, print the name of whom the dna sequence belongs to
    if compare_str == number:
        print(name)
        dna_file.close()
        file.close()
        exit()
#else print no match and close the open files
print("No Match")
dna_file.close()
file.close()

