 #include <time.h>
#include <stdlib.h>
#include <limits.h>
#include <math.h>


#include "ai.h"
#include "utils.h"
#include "hashtable.h"
#include "priority_queue.h"
#include "assert.h" // added by me

/**
 * Retrieve solution and return a string containing the squence of moves
*/
char* save_solution( node_t* solution_node ){
	node_t* n = solution_node;
	char *solution_string = malloc(sizeof(char) * solution_node->depth+1);
	solution_string[n->depth] = '\0';
	while (n->parent != NULL)
	{

		switch (n->move) {
		case up:
			if (n->parent->state.map[n->state.player_y][n->state.player_x] == '$')
				solution_string[n->depth-1] = 'U';
			else
				solution_string[n->depth-1] = 'u';
			break;

		case down:  
			if (n->parent->state.map[n->state.player_y][n->state.player_x] == '$')
				solution_string[n->depth-1] = 'D';
			else     
				solution_string[n->depth-1] = 'd';
			break;

		case left:  
			if (n->parent->state.map[n->state.player_y][n->state.player_x] == '$')
				solution_string[n->depth-1] = 'L';
			else    
				solution_string[n->depth-1] = 'l';
			break;

		case right: 
			if (n->parent->state.map[n->state.player_y][n->state.player_x] == '$')
				solution_string[n->depth-1] = 'R';
			else       
				solution_string[n->depth-1] = 'r';
			break;

		}

		n = n->parent;
	}
	return solution_string;
}

/**
 * Copy a src into a dst state
*/
void copy_state(sokoban_t* init_data, state_t* dst, state_t* src){

	dst->map = malloc(sizeof(char *) * init_data->lines);
	for ( int i = 0; i < init_data->lines; ++i ){
		int width = strlen(src->map[i]) + 1;
		dst->map[i] = malloc(width);    
		memcpy(dst->map[i], src->map[i], width);
	}

	dst->player_x = src->player_x;

	dst->player_y = src->player_y;

}

/**
 * Create the initial node
*/
node_t* create_init_node( sokoban_t* init_data ){
	node_t * new_n = (node_t *) malloc(sizeof(node_t));
	new_n->parent = NULL;	
	new_n->priority = 0;
	new_n->depth = 0;
	new_n->num_childs = 0;
	new_n->move = -1;
	new_n->state.map = malloc(sizeof(char *) * init_data->lines);
	for (int i = 0; i < init_data->lines; ++i)
	{
		int width = strlen(init_data->map[i]) + 1;
		new_n->state.map[i] = malloc(width);    
		memcpy(new_n->state.map[i], init_data->map[i], width);
	}

	new_n->state.player_x = init_data->player_x;

	new_n->state.player_y = init_data->player_y;
	return new_n;
}

/**
 * Create the a node from a parent node
*/
node_t* create_node( sokoban_t* init_data, node_t* parent ){
	node_t * new_n = (node_t *) malloc(sizeof(node_t));
	new_n->parent = parent;
	new_n->depth = parent->depth + 1;
	copy_state(init_data, &(new_n->state), &(parent->state));
	return new_n;
	
}

/**
 * Apply an action to node n, create a new node resulting from 
 * executing the action, and return if the player moved
*/
bool applyAction(sokoban_t* init_data, node_t* n, node_t** new_node, move_t action ){

	bool player_moved = false;

    *new_node = create_node( init_data, n );
    (*new_node)->move = action;
	(*new_node)->priority =  -(*new_node)->depth;

    player_moved = execute_move_t( init_data, &((*new_node)->state), action );	

	return player_moved;

}

/**
 * Book keeping variable and function to free memory once the solver finishes
*/
node_t** expanded_nodes_table;
unsigned expanded_nodes_table_size = 10000000; //10M
void update_explore_table(node_t* n, unsigned expanded_nodes ){
    if( expanded_nodes > expanded_nodes_table_size - 1){
	expanded_nodes_table_size *= 2;
	expanded_nodes_table = realloc( expanded_nodes_table, sizeof(node_t*) * expanded_nodes_table_size );
	
    }
    
    expanded_nodes_table[ expanded_nodes ] = n;

}

void free_memory(unsigned expanded_nodes, int lines){
	for( unsigned i = 0; i < expanded_nodes; i++){
		node_t* current = expanded_nodes_table[ i ];
		if(!current){
			continue;
		}
		free_state(current->state, lines);
		free(current);
    }
	free(expanded_nodes_table);
}

/**
 * Given a 2D map, returns a 1D map
*/
void flatten_map( sokoban_t* init_data, char **dst_map, char **src_map){

	int current_i = 0;
	for (int i = 0; i < init_data->lines; ++i)
	{
		int width = strlen(src_map[i]);
		for ( int j = 0; j < width; j++ ){
			(*dst_map)[current_i]  = src_map[i][j];
			current_i++;
		}
	}
}

/**
 * Find a solution by exploring all possible paths
 */
void find_solution(sokoban_t* init_data, bool show_solution)
{
	// Keep track of solving time
	clock_t start = clock();
	
	// Solution String containing the sequence of moves
	char* solution = NULL;

	HashTable hashTable;
	struct heap pq;

	// Statistics
	unsigned generated_nodes = 0;
	unsigned expanded_nodes = 0;
	unsigned duplicated_nodes = 0;
	// int max_depth = 0; commented out as currently note used
	unsigned solution_size = 0;

	// Choose initial capacity of PRIME NUMBER 
	// Specify the size of the keys and values you want to store once 
	// The Hash Table only accept a 1D key and value.
	ht_setup( &hashTable, sizeof(int8_t) * init_data->num_chars_map, sizeof(int8_t) * init_data->num_chars_map, 16769023);

	// Data structure to create a 1D representation of the map
	// Needed to interact with the hash table
	char* flat_map = calloc( init_data->num_chars_map, sizeof(char));

	// Initialize heap
	heap_init(&pq);

	// Initialize expanded nodes table. 
	// This table will be used to free your memory once a solution is found
	expanded_nodes_table = (node_t**) malloc( sizeof(node_t*) * expanded_nodes_table_size ); //line 4


	// Add the initial node
	node_t* n = create_init_node( init_data ); // LINE 2 ???
	
	// Use the max heap API provided in priority_queue.h
	heap_push(&pq,n);
	
	// Loop that implements Dijkstra's sokving algorithm to find the shortest path
	while(pq.size > 0) {
		// Pop a node from the priority queue to consider and add it to the visited list
		n = heap_delete(&pq);
		// set_priority(n); // Call the heuristic function to set the priority based on minimal distances
		update_explore_table(n, expanded_nodes);
		expanded_nodes++; 
		state_t* new_state = &(n->state);

		// Check if the node has fit the winning condition, so that the algorithm can terminate
		if (winning_condition(init_data, new_state)) { 
			solution = save_solution(n);
			solution_size = n->depth;
			break;
		}

		int num_moves = 4; 
		// Iterate over the possible moves from the current node
    	for (int i = 0; i < num_moves; i++) {
        	move_t a = (move_t)(i);
			// Create a new node and sets its values based on the current action from n
			node_t* new_node; 
			bool player_moved = applyAction(init_data, n, &new_node, a);
			generated_nodes++;
			
			// Check if the move is valid - ignore deadlocks and states where a player can't move
			if ((player_moved == false) || simple_deadlocks(init_data, &(new_node->state))){ 
				free_state(new_node->state, init_data->lines);
				free(new_node);
				continue;
			}

			// Modify the 2D map into a 1D data structure
			flatten_map(init_data, &flat_map,new_node->state.map);
			// If the node has already been visited (don't add it to the hash table)
			if (ht_contains(&hashTable, flat_map) == HT_FOUND) {
				duplicated_nodes++;
				free_state(new_node->state, init_data->lines);
				free(new_node);
				continue;
			}

			// The node is not a duplicate, so we can add it to the hashtable, assuming there is no error
			assert(ht_insert(&hashTable, flat_map, flat_map) != HT_ERROR);
			heap_push(&pq, new_node);
		}
	}

	//----------------------------
	
	// Free Memory of HashTable, Explored and flatmap
	ht_clear(&hashTable);
	ht_destroy(&hashTable);
	free_memory(expanded_nodes, init_data->lines);
	free(flat_map);
	//----------------------------

	// Stop clock
	clock_t end = clock();
	double cpu_time_used = ((double) (end - start)) / CLOCKS_PER_SEC;

	// Show Soltion	
	if( show_solution && solution != NULL ) play_solution( *init_data, solution);

	endwin();

	if( solution != NULL ){
		printf("\nSOLUTION:                               \n");
		printf( "%s\n\n", solution);
		FILE *fptr = fopen("solution.txt", "w");
		if (fptr == NULL)
		{
			printf("Could not open file");
			return ;
		}
		fprintf(fptr,"%s\n", solution);
		fclose(fptr);
		free(solution);
	}


	printf("STATS: \n");
	printf("\tExpanded nodes: %'d\n\tGenerated nodes: %'d\n\tDuplicated nodes: %'d\n", expanded_nodes, generated_nodes, duplicated_nodes);
	printf("\tSolution Length: %d\n", solution_size);
	printf("\tExpanded/seconds: %d\n", (int)(expanded_nodes/cpu_time_used) );
	printf("\tTime (seconds): %f\n", cpu_time_used );
	
	//Free and set to null the heap data before freeing all of its pointers
	emptyPQ(&pq, init_data->lines);
	free(pq.heaparr);
	
}

void solve(char const *path, bool show_solution)
{
	/**
	 * Load Map
	*/
	sokoban_t sokoban = make_map(path, sokoban);
	
	/**
	 * Count number of boxes and Storage locations
	*/
	map_check(sokoban);

	/**
	 * Locate player x,y position
	*/
	sokoban = find_player(sokoban);
	
	sokoban.base_path = path;

	find_solution(&sokoban, show_solution);

	/* new: free system's components of sokoban */ 
	free_sokoban(sokoban);

}

/******* Code I have started writing to implement a heuristic (incomplete) ******/
// void set_priority(node_t node){
// 	int x = state->player_x;
// 	int y = state->player_y;
// 	char car = node.state->map[y][x];
// }
