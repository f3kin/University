#ifndef __NODE__
#define __NODE__

#include "utils.h"

//to free set num_childs to 0 initially, then as find nodes, make non-zero, and when freed, subtract from parent, make them dead ends
//Essentially do a DFS to free


/**
 * Data structure containing the node information
 */
struct node_s{
    int priority;
    int depth;
    int num_childs;
    move_t move;
    state_t state;
    struct node_s* parent;
};

typedef struct node_s node_t;


#endif
