#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <unistd.h>
#include <string.h>

#include "utils.h"
#include "printusage.h"
#include "parse_input.h"


void compute_transition_matrix(Graph* graph, double p, double** matrix) {
    int N = graph->num_nodes;
    
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            matrix[i][j] = (1.0 / N) * p;
        }
    }

    for (int i = 0; i < N; i++) {
        Node node = graph->nodes[i];
        if (node.outdegree > 0) {
            for (int j = 0; j < node.outdegree; j++) {
                char* target = node.links[j].link;
                for (int k = 0; k < N; k++) {
                    if (strcmp(graph->nodes[k].node, target) == 0) {
                        matrix[i][k] += (1.0 - p) * (1.0 / node.outdegree);
                    }
                }
            }
        } else {
            for (int k = 0; k < N; k++) {
                matrix[i][k] += (1.0 - p) * (1.0 / N);
            }
        }
    }
}


void matrix_multiply(double* vec, double** matrix, double* result, int N) {
    for (int i = 0; i < N; i++) {
        result[i] = 0.0;
        for (int j = 0; j < N; j++) {
            result[i] += vec[j] * matrix[j][i];
        }
    }
}


void simulate_markov_chain(Graph* graph, int num_steps, double p) {
    int N = graph->num_nodes;
    double** transition_matrix = (double**)malloc(N * sizeof(double*));
    for (int i = 0; i < N; i++) {
        transition_matrix[i] = (double*)malloc(N * sizeof(double));
    }

    compute_transition_matrix(graph, p, transition_matrix);

    double* curr_vector = (double*)malloc(N * sizeof(double));
    double* next_vector = (double*)malloc(N * sizeof(double));

    for (int i = 0; i < N; i++) {
        curr_vector[i] = 1.0 / N;
    }

    for (int step = 0; step < num_steps; step++) {
        matrix_multiply(curr_vector, transition_matrix, next_vector, N);
        double* temp = curr_vector;
        curr_vector = next_vector;
        next_vector = temp;
    }

    for (int i = 0; i < N; i++) {
        printf("%s\t%.10lf\n", graph->nodes[i].node, curr_vector[i]);
    }

    for (int i = 0; i < N; i++) {
        free(transition_matrix[i]);
    }
    free(transition_matrix);
    free(curr_vector);
    free(next_vector);
}
