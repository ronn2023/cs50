from cs50 import get_string
answer = get_string("Text: ")
sentences = 0
i = 0
while True:
    if answer[i] == "." or answer[i] == "!" or answer[i] == "?":
        sentences += 1

    i += 1
    if i == len(answer):
        break

letters = 0
i = 0
spaces = 0
for char in answer:
    if char.isalpha():
        letters += 1
    i += 1
    if char.isspace():
        spaces += 1

words = spaces + 1
L = 100 * (letters / words)
S = 100 * (sentences / words)
index = 0.0588 * L - 0.296 * S - 15.8
index = round(index)
if index < 0:
    print("Before grade 1")
elif index >= 16:
    print("Grade 16+")
else:
    print(f"Grade {index}")