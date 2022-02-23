#include <stdio.h>
#include <cs50.h>

int main (void)
{
    long answer, n;
    int count = 0;
    answer = get_long("Number:");
    n = answer;
    answer = answer;

    printf("%lu", n, answer);

   while(answer >=1)
   {
       answer = answer/10;
       count++;
   }
    printf("%d \n", count);
    printf("%lu \n", answer);


}
