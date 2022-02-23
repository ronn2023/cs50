// Implements a dictionary's functionality

#include <stdbool.h>
#include <string.h>
#include "dictionary.h"
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <stdint.h>
#include <strings.h>

unsigned int word_size = 0;

// Represents a node in a hash table
typedef struct node
{
    char word[LENGTH + 1];
    struct node *next;
}
node;

// Number of buckets in hash table
const unsigned int N = 5000;

// Hash table
node *table[N];

// Returns true if word is in dictionary else false
bool check(const char *word)
{
    int match = 0;
    bool found = false;
    int i = 0;
    struct node *head_node, *next_node;
    int hash_index;
    char input_word[50];

    //copy word into array to access individual letters of word
    strcpy(input_word, word);

    //find the index of that word
    hash_index = hash(input_word);

    head_node = table[hash_index];
    next_node = head_node;

    //check if the spelling is right by comparing the word in the text with the word in the dictionary through the linklist at array of hash_index
    while (next_node != NULL)
    {
        if ((strlen(next_node-> word)) != (strlen(input_word)))
        {
            next_node = next_node -> next;
            continue;
        }
        match = strcasecmp(next_node->word, input_word);
        if (match == 0)
        {
            found = true;
            break;
        }
        next_node = next_node -> next;

    }
    return (found);
}

// Hashes word to a number
unsigned int hash(const char *word)
{

    /*credit to this site for finding the algorythm for the Hash (variation of Daniel J Bernstein's algorithm)
    https://stackoverflow.com/questions/114085/fast-string-hashing-algorithm-with-low-collision-rates-with-32-bit-integer
    */

    unsigned long c, i, h;
    //creates a hash with good distribution of data
    for (i = h = 0; word[i]; i++)
    {
        c = toupper(word[i]);
        h = ((h << 5) + h) ^ c;
    }
    h = h % N;
    return h;
}

// Loads dictionary into memory, returning true if successful else false
bool load(const char *dictionary)
{
    //declarations
    struct node *next_node, *temp_node, *head_node, *n;
    unsigned int array_index;
    FILE *fp;
    char arrword[50];
    bool works = true;


    //sets the table index's to not point at anything in the beginning
    for (int i = 0; i < N; i++) //Initialize
    {
        table[i] = NULL;
    }


    fp = fopen(dictionary, "r+");
    //check for error
    if (fp == NULL)
    {
        printf("Error: Please enter a dictionary file! \n");
        works = false;
        return (works);
    }

    //scan words into array until end of file
    while (fscanf(fp, "%s", arrword) != EOF)
    {
        // printf("%s ",arrword);

        array_index = hash(arrword);

        //create new node
        n = malloc(sizeof(node));

        if (n == NULL)
        {
            printf("Error: Not enough memory \n");
            works = false;
            return (works);
        }
        //copy word into node
        strcpy(n->word, arrword);
        n->next = NULL;

        head_node = table[array_index];
        if (head_node == NULL)
        {
            table[array_index] = n;
            word_size++;
        }
        else
        {

            temp_node = head_node;
            table[array_index]  = n;
            n-> next = head_node;
            word_size ++;

        }
    }
    //close file and return success or failure
    fclose(fp);
    return (works);

}

// Returns number of words in dictionary if loaded else 0 if not yet loaded
unsigned int size(void)
{
    //word_size is calculated within load
    return (word_size);
}

// Unloads dictionary from memory, returning true if successful else false
bool unload(void)
{
    int i;
    struct node *head_node, *next_node;

    for (i = 0; i < N; i++)
    {
        head_node = table[i];
        next_node = head_node;
        while (next_node != NULL)
        {
            next_node = next_node->next;
            free(head_node);
            head_node = next_node;

        }

    }
    //check if memory was successfully freed
    if (i == N)
    {
        return true;
        printf("released all the memory \n");
    }
    else
    {
        return false;
    }
}
