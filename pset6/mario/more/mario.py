from cs50 import get_int

while True:
    height = get_int("Height : ")
    if (height > 0 and height < 9 ):
        break
i = height
while True:
    for j in range(i - 1):
        print(" ", end = "")
    j = 0
    while True:
        print("#", end = "")
        j += 1
        if height - i + 1 <= j:
            break

    print("  ", end = "")

    j = 0
    while True:
        print("#", end = "")
        j += 1
        if height - i + 1 <= j:
            break
    print("")
    i -= 1
    if i == 0:
        break