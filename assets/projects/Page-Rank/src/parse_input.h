#ifndef PARSE_INPUT_H
#define PARSE_INPUT_H


typedef struct Link {
    char link[257];
}Link;

typedef struct Edge {
    char source[257];
    char target[257];
} Edge;

typedef struct Node{
    char node[257];
    int indegree;
    int outdegree;
    Link* links;
}Node;
typedef struct Graph {
    char  graph_name[257];
    Edge *edges;
    Node *nodes;
    int num_nodes;
    int num_edges;
} Graph;

void destroy_graph(Graph* graph);
Graph* parse_input(const char* filename);

#endif
