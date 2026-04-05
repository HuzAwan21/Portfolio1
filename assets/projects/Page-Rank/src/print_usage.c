#include <stdio.h>

void print_usage(){
    printf ("Usage: ./pagerank [OPTIONS] ... [FILENAME]\n");
    printf ("Perform pagerank computations for a given file in the DOT format\n\n");
    printf ("  -h     Print a brief overview of the available command line parameters\n");
    printf ("  -r N   Simulate N steps of the random surfer and output the result\n");
    printf ("  -m N   Simulate N steps of the Markov chain and output the result\n");
    printf ("  -s     Compute and print the statistics of the graph as defined in section 3.4\n");
    printf ("  -p P   Set the parameter p to P%%. (Default: P = 10)\n");
}