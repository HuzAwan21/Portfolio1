#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <unistd.h>
#include <string.h>

#include "utils.h"
#include "printusage.h"
#include "parse_input.h"

int bored(double p) {
    int n = (int)(100 * p);
    unsigned index = randu(100);
    return index < n ? 1 : 0;
}

void simulate_random_surfer(Graph* graph, int num_steps, double p) {
    if (num_steps <= 0) {
        return;
    }

    if (graph == NULL || graph->nodes == NULL || graph->num_nodes <= 0) {
        return;
    }

    char** visited = malloc(num_steps * sizeof(char*));
    if (!visited) {
        fprintf(stderr, "Error: Memory allocation failed\n");
        exit(1);
    }

    unsigned N = graph->num_nodes;
    if (N == 0) {
        fprintf(stderr, "Error: Graph has no nodes\n");
        free(visited);
        return;
    }

    unsigned curr_node_index = randu(N);
    char* curr_node = graph->nodes[curr_node_index].node;

    for (int step = 0; step < num_steps; step++) {
        if (bored(p)) {
            curr_node_index = randu(N);
            curr_node = graph->nodes[curr_node_index].node;
        } else {
            if (graph->nodes[curr_node_index].outdegree > 0) {
                int link_index = randu(graph->nodes[curr_node_index].outdegree);
                curr_node = graph->nodes[curr_node_index].links[link_index].link;
                for (int i = 0; i < N; i++) {
                    if (strcmp(curr_node, graph->nodes[i].node) == 0) {
                        curr_node_index = i;
                        break;
                    }
                }
            } else {
                curr_node_index = randu(N);
                curr_node = graph->nodes[curr_node_index].node;
            }
        }
        visited[step] = curr_node;
    }

    for (int i = 0; i < N; i++) {
        char* node = graph->nodes[i].node;
        int m = 0;

        for (int j = 0; j < num_steps; j++) {
            if (strcmp(node, visited[j]) == 0) {
                m++;
            }
        }
        double w = (double)m / num_steps;
        printf("%s\t%.10lf\n", node, w);
    }

    free(visited);
}