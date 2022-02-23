#include <cs50.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// Max voters and candidates
#define MAX_VOTERS 100
#define MAX_CANDIDATES 9

// preferences[i][j] is jth preference for voter i
int preferences[MAX_VOTERS][MAX_CANDIDATES];

// Candidates have name, vote count, eliminated status
typedef struct
{
    string name;
    int votes;
    bool eliminated;
}
candidate;

// Array of candidates
candidate candidates[MAX_CANDIDATES];

// Numbers of voters and candidates
int voter_count;
int candidate_count;

// Function prototypes
bool vote(int voter, int rank, string name);
void tabulate(void);
bool print_winner(void);
int find_min(void);
bool is_tie(int min);
void eliminate(int min);

int main(int argc, string argv[])
{
    // Check for invalid usage
    if (argc < 2)
    {
        printf("Usage: runoff [candidate ...]\n");
        return 1;
    }

    // Populate array of candidates
    candidate_count = argc - 1;
    if (candidate_count > MAX_CANDIDATES)
    {
        printf("Maximum number of candidates is %i\n", MAX_CANDIDATES);
        return 2;
    }
    for (int i = 0; i < candidate_count; i++)
    {
        candidates[i].name = argv[i + 1];
        candidates[i].votes = 0;
        candidates[i].eliminated = false;
    }

    voter_count = get_int("Number of voters: ");
    if (voter_count > MAX_VOTERS)
    {
        printf("Maximum number of voters is %i\n", MAX_VOTERS);
        return 3;
    }

    // Keep querying for votes
    for (int i = 0; i < voter_count; i++)
    {

        // Query for each rank
        for (int j = 0; j < candidate_count; j++)
        {
            string name = get_string("Rank %i: ", j + 1);

            // Record vote, unless it's invalid
            if (!vote(i, j, name))
            {
                printf("Invalid vote.\n");
                return 4;
            }
        }

        printf("\n");
    }

    // Keep holding runoffs until winner exists
    while (true)
    {
        // Calculate votes given remaining candidates
        tabulate();

        // Check if election has been won
        bool won = print_winner();


        if (won)
        {
            break;
        }

        // Eliminate last-place candidates
        int min = find_min();
        bool tie = is_tie(min);

        // If tie, everyone wins
        if (tie)
        {
            for (int i = 0; i < candidate_count; i++)
            {
                if (!candidates[i].eliminated)
                {
                    printf("%s\n", candidates[i].name);
                }
            }
            break;
        }

        // Eliminate anyone with minimum number of votes
        eliminate(min);

        // Reset vote counts back to zero
        for (int i = 0; i < candidate_count; i++)
        {
            candidates[i].votes = 0;
        }
    }
    return 0;
}

// Record preference if vote is valid
bool vote(int voter, int rank, string name)
{
    bool found = false;
    int candidate_num;
    int j = rank;

    for (candidate_num = 0; candidate_num < candidate_count; candidate_num++)
    {
        if (strcmp(candidates[candidate_num].name, name) == 0)
        {
            preferences[voter][j] = candidate_num;
            found = true;
        }
    }
    return (found);
    // TODO
}


// Tabulate votes for non-eliminated candidates
void tabulate(void)
{
    int   candidates_selected, i, j;
    bool vote;


    for (i = 0; i < voter_count; i++)
    {
        j = 0;
        vote = false;
        while ((!vote) && (j < candidate_count))
        {
            candidates_selected = preferences[i][j];
            if (candidates[candidates_selected].eliminated == true)
            {
                j++;
            }
            else
            {
                // printf("Came here i= %d, j = %d \n",i,j);
                candidates[candidates_selected].votes++;
                vote = true;
            }
        }

    }
    return;
}

// Print the winner of the election, if there is one
bool print_winner(void)
{
    for (int i = 0; i < candidate_count; i++)
    {
        if (candidates[i].votes > ((voter_count) / 2))
        {
            printf("%s\n", candidates[i].name);
            return (true);
        }

    }
    return false;
}

// Return the minimum number of votes any remaining candidate has
int find_min(void)
{
    int min = candidates[0].votes;


    for (int i = 0; i < candidate_count; i++)
    {
        if (candidates[i].eliminated == false)
        {
            if (candidates[i].votes < min)
            {
                min = candidates[i].votes;
            }
        }
    }
    return (min);
    printf("%d", min);
}

// Return true if the election is tied between all candidates, false otherwise
bool is_tie(int min)
{
    bool tie = false;

    for (int i = 0; i < candidate_count; i++)
    {
        if (candidates[i].eliminated == false)
        {
            if (min == candidates[i].votes)
            {
                tie = true;
            }
            else
            {
                tie = false;
            }
        }
    }
    return (tie);
}

// Eliminate the candidate (or candidiates) in last place
void eliminate(int min)
{
    for (int i = 0; i < candidate_count; i++)
    {
        if (candidates[i].votes == min)
        {
            candidates[i].eliminated = true;
        }
    }

    return;
}
