#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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

void destroy_graph (Graph* graph){
        free (graph->edges);
        for (int i = 0; i < graph->num_nodes; i++) {
        free(graph->nodes[i].links);
        }
        free (graph->nodes);
        free (graph);
    }

void add_link(Graph* graph) {
    for (int i = 0; i < graph->num_nodes; i++) {
        
        if (graph->nodes[i].links!=NULL){
            free (graph->nodes[i].links);
            graph->nodes[i].links = NULL;
        }

        graph->nodes[i].links = malloc((graph->nodes[i].outdegree) * sizeof(Link));
        if (graph->nodes[i].links == NULL) {
            fprintf(stderr, "Error: Memory allocation failed\n");
            destroy_graph(graph);
            exit(1);
        }

        int link_index = 0;
        for (int j = 0; j < graph->num_edges; j++) {
            if (strcmp(graph->edges[j].source, graph->nodes[i].node) == 0) {
                strcpy(graph->nodes[i].links[link_index].link, graph->edges[j].target);
                link_index++;
            }
        }
    }
}    

int node_exists(Graph* graph, const char* node_name) {

    for (int i = 0; i <graph->num_nodes; i++) {
        if (strcmp(graph->nodes[i].node, node_name) == 0){
            return 1;
        }}
    return 0;
}

void add_node(Graph* graph, const char* node_name, int inout) {
    if (!node_exists(graph, node_name)) {
        graph->nodes = realloc(graph->nodes, (graph->num_nodes + 1) * sizeof(Node));
        if (graph->nodes == NULL) {
            fprintf(stderr, "Error: Memory allocation failed\n");
            destroy_graph(graph);
            exit(1);
        }
        strcpy(graph->nodes[graph->num_nodes].node, node_name);
        graph->nodes[graph->num_nodes].indegree = 0;
        graph->nodes[graph->num_nodes].outdegree = 0;
        graph->nodes[graph->num_nodes].links = NULL;
        graph->num_nodes++;
    }

    for (int i = 0; i < graph->num_nodes; i++) {
        if (strcmp(graph->nodes[i].node, node_name) == 0) {
            if (inout) {
                graph->nodes[i].indegree ++;
            } else {
                graph->nodes[i].outdegree ++;
              }
        }
    }
}

Graph* parse_input(const char* filename) {
    FILE* f = fopen(filename, "r");

    if (!f) {
        fprintf(stderr, "Error: Unable to open file %s\n", filename);
        return NULL;
    }

      char dg[8], gn[257], bropen, source[257], arrow[3], target[257];
    if (fscanf(f, "%7s %256s %c", dg, gn, &bropen) != 3 ||
        strcmp(dg, "digraph") != 0 || bropen != '{') {
        fprintf(stderr, "Error: Incorrect file format - %s can not be parsed\n", filename);
        fclose(f);
        return NULL;
    }



    Graph* graph = (Graph*)malloc(sizeof(Graph));
    graph->edges = NULL;
    graph->nodes = NULL;
    graph->num_edges = 0;
    graph->num_nodes = 0;
    strcpy(graph->graph_name, gn);


       while (fscanf(f, "%256s %2s %256s", source, arrow, target) == 3 ) {
        if (strcmp(arrow, "->") != 0) {
            fprintf(stderr, "Error: Incorrect arrow format - %s can not be parsed\n", filename);
            fclose(f);
            destroy_graph(graph);
            return NULL;
        }

        size_t len = strlen(target);
        if (len > 0 && target[len - 1] == ';') {
            target[len - 1] = '\0';
        } else {
            fprintf(stderr,"Error: Semicolon missing\n");
            return NULL;
        }
        
        

        add_node(graph, source, 0);
        add_node(graph, target, 1);
    

        graph->edges = realloc(graph->edges, (graph->num_edges + 1) * sizeof(Edge));
        strcpy(graph->edges[graph->num_edges].source, source);
        strcpy(graph->edges[graph->num_edges].target, target);
        graph->num_edges++;

        add_link(graph);
    }
    
    fclose(f);
    return graph;
    }
