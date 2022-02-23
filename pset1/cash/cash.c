#include <stdio.h> //standard input outpud header
#include <cs50.h> //I am using this in order to use get_float
#include <math.h> //I need this to use the modulo's and the int cents code

int main(void)
{
    float answer; //declaring float
    do
    {
        answer = get_float("Change Owed:\n");
    }
    while (answer < 0); //reprompts user if they select a number that is non-positive
    int cents = round(answer * 100); //rounding number of cents

    int c = cents / 25; // this is storing the number of quarters used
    int d = cents % 25; //this stores the remainder

    int e = d / 10; //this stores the number of dimes used
    int f = d % 10;//remainder

    int g = f / 5; // this stores the number of nickels used
    int h = f % 5;//remainder

    int i = h / 1; //this stores the number of pennies used

    int n = c + e + g + i;//this finds the total number of coins used

    printf("%d\n", c + e + g + i);
}