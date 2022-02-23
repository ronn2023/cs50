#include <stdio.h> //standard input output header
#include <cs50.h> //I use this in order to use "string" argv
#include <string.h> //This is used to implement strlen
#include <stdlib.h> //This is used to implement atoi()
#include <ctype.h> //Used for isdigit()

#define LOWER_Z_VALUE 122
#define NUM_ALPHABET 26

int main(int argc, string argv[])
{
    int val, key, i; //declaring variables



    if ((argc != 2)) //sets up an error statement if the user doesn't select a key
    {
        printf("Error: Please type a non-zero number after ./caesar!\n");
        exit(1);
    }

    key = atoi(argv[1]);



    if (key <= 0) //this makes sure that the user doesn't choose a negative number for a key
    {
        printf("Error: Incorrect key number!\n");
        exit(1);
    }

    for (i = 0; i < strlen(argv[1]); i++) //this for loop makes sure that the user doesn't put in a special character as the key
    {
        if (!(isdigit(argv[1][i])))
        {
            exit(1);
        }
    }



    key = key % NUM_ALPHABET; //makes sure that the program can loop from 'a' to 'z'

    string answer = get_string("plaintext: "); //this asks the user for the plaintext
    printf("\n");

    for (i = 0; i < strlen(answer); i++)
    {

        if ((answer[i] == ' ') || (answer[i] ==  ',') || (answer[i] == '!')) //makes sure that the program ignores special characters
        {

            continue;
        }

        if (isupper(answer[i])) //This makes sure that the program loops characters from 'A' to 'Z' in capital letters
        {
            answer[i] = (char)(answer[i] + key);
            if (answer[i] > 'Z')
            {
                answer[i] = 'A' + (char)(answer[i] - 'Z');
                answer[i]--;
            }

        }

        else
        {
            val = (int)(answer[i]);
            val += key;

            if (val > LOWER_Z_VALUE) //this makes sure that the program loops characters from 'a' tp 'z' in lowercase letters
            {
                answer[i] = (char)(('a') + (val - LOWER_Z_VALUE));
                answer[i]--;
            }

            else
            {
                answer[i] = (char)(answer[i] + key); //this calculates the answer in the scenario that the answer + the key isn't greater than 'z'
            }

            if (answer[i] > 'z') // this calculates the answer when the answer + the key is greater than 'z'
            {
                answer[i] = 'a' + (char)(answer[i] - 'z');
                answer[i]--;
            }

        }


    }
    printf("ciphertext: %s\n ", answer);//prints the answer
    printf("\n");

}
