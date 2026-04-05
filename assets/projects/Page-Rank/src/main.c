/*
 * main.c
 *
 * Programming 2 - Project 3 (PageRank)
 */

#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <unistd.h>
#include <string.h>

#include "utils.h"
#include "printusage.h"
#include "parse_input.h"
#include "random_surfer.h"
#include "marchov_chain.h"



int min_indegree(Graph* graph){
    int min_indegree = graph->num_edges;
    for (int i=0; i<graph->num_nodes;i++){
        int indegree_cur= graph->nodes[i].indegree;
        if (min_indegree > indegree_cur){
           min_indegree=indegree_cur;
        }
    }
    return min_indegree;
}
int max_indegree(Graph* graph){
    int max_indegree =0;
    for (int i=0; i<graph->num_nodes;i++){
        int indegree_cur= graph->nodes[i].indegree;
        if (max_indegree < indegree_cur){
           max_indegree=indegree_cur;
        }
    }
    return max_indegree;
}
int min_outdegree(Graph* graph){
    int min_outdegree = graph->num_edges;
    for (int i=0; i<graph->num_nodes;i++){
        int outdegree_cur= graph->nodes[i].outdegree;
        if (min_outdegree > outdegree_cur){
           min_outdegree=outdegree_cur;
        }
    }
    return min_outdegree;
}
int max_outdegree(Graph* graph){
    int max_outdegree = 0;
    for (int i=0; i<graph->num_nodes;i++){
        int outdegree_cur= graph->nodes[i].outdegree;
        if (max_outdegree < outdegree_cur){
           max_outdegree=outdegree_cur;
        }
    }
    return max_outdegree;
}



int main(int argc, char *const *argv) {
  
  // initialize the random number generator
  rand_init();

  int opt;
  int rN;
  int mN;
  int hflag=0;
  int rflag=0;
  int mflag=0;
  int sflag=0;
  double P=0.1;
  int arg_counter=1;

  const char* filename = argv[argc-1];
  if(!filename){
    fprintf(stderr,"Error: %s File does not exist\n",filename);
    print_usage();
    exit(1);
  }

  if(strcmp(filename,"-h")==0){
   print_usage();
   exit(0);
  } else{

  Graph* graph = parse_input(filename);

  if (graph == NULL) {
    fprintf(stderr, "Error: Failed to parse input file\n");
    exit(1);
  }

  while ((opt=getopt(argc,argv,"hr:m:sp:"))!=-1){
    switch(opt){
    case 'h':
      hflag=1;
      arg_counter +=1;

      print_usage();

    exit(0);
      
  case 'r':
      rflag=1;
      arg_counter +=2;

      if (optarg) {
        rN = atoi(optarg);
        if (rN<1){
        fprintf (stderr,"Error: -r N ... N must be larger than zero\n");
        exit(1);
        }
      }else{
        fprintf(stderr,"Error: -r expects one argument");
        exit(1);
      }

  break;

  case 'm':
      mflag=1;
      arg_counter +=2;

      if (optarg) {
        mN = atoi(optarg);
        if (mN<1){
        fprintf (stderr,"Error: -m N ... N must be larger than zero\n");
        exit(1);
        }
      }else{
        fprintf(stderr,"Error: -m expects one argument");
        exit(1);
      }

  break;

  case 's':
      sflag=1;
      arg_counter +=1;
      
  break;

  case 'p':
      arg_counter +=2;
      int k;

      if (optarg) {
        k = atoi(optarg);
        if (k<1||k>100){
        fprintf (stderr,"Error: -p P ... P must be between 1 and 100\n");
        exit(1);
        } 
        P= k/100.0;

      }else{
        fprintf(stderr,"Error: -m expects one argument");
        exit(1);
      }

    break;

    case '?':
      fprintf(stderr, "Error: Unknown option: -%c\n", optopt);
      print_usage();
    exit(1);

    case ':':
      fprintf(stderr, "Error: Missing argument for -%c\n", optopt);
      print_usage();
    exit(1);

    default:
      print_usage();
    exit(1);
       
    }
  } 

  if (hflag==0 && rflag==0 && mflag==0 && sflag==0){
    fprintf(stderr,"Error: At least one of the optional parameters -h, -r, -m and -s must be used at a time\n");
    exit(1);
  }

  if  (arg_counter==argc||arg_counter!=argc -1){
    fprintf (stderr,"Error: invalid arguments detected\n");
    exit(1);
  }

  if (sflag==1){
    printf("%s:\n",graph->graph_name);
    printf("- num nodes: %d\n", graph->num_nodes);
    printf("- num edges: %d\n", graph->num_edges);
    printf("- indegree: %d-%d\n",min_indegree(graph), max_indegree(graph));
    printf("- outdegree: %d-%d\n",min_outdegree(graph),max_outdegree(graph));
  }

  if (rflag==1){
    simulate_random_surfer(graph, rN, P);
  }

  if (mflag == 1) {
    simulate_markov_chain(graph, mN, P);
  }

  destroy_graph(graph);
  exit(0);
}
}
