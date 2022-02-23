from cs50 import get_float

#Prompt user until input is valid
while True:
    answer = get_float("Change Owed: ")
    if answer > 0.00:
        break
    
number = int(100 * answer)

#calculate amount of coins and store remainder
quarters = int(number / 25)
rm = number % 25

dimes = int(rm / 10)
rma = rm % 10

nickels = int(rma / 5)
rman = rma % 5

pennies = int(rman)
#print total amount of coins
print(f"{quarters + dimes + nickels + pennies}")