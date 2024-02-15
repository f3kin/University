#include <curses.h>
#include "utils.h"



/**************************************
* Box movement given a state state  *
***************************************/

bool is_goal_loc(int y, int x, sokoban_t* init_data)
{
	return (init_data->map_save[y][x] == '.') || (init_data->map_save[y][x] == '+') || (init_data->map_save[y][x] == '*');
}


bool push_box_left(sokoban_t* init_data,state_t* state)
{
	if (state->map[state->player_y][state->player_x-2] == '$' || state->map[state->player_y][state->player_x-2] == '*') {
		return false;
	} else if (state->map[state->player_y][state->player_x-2] == '#') {
		return false;
	} else {
		state->map[state->player_y][state->player_x-1] = '@';
		if(state->map[state->player_y][state->player_x-2] == '.')
			state->map[state->player_y][state->player_x-2] = '*';
		else
			state->map[state->player_y][state->player_x-2] = '$';
		
		state->map[state->player_y][state->player_x] = ' ';
        if (is_goal_loc( state->player_y, state->player_x, init_data) && state->map[state->player_y][state->player_x] == ' ') {
	        	state->map[state->player_y][state->player_x] = '.';
        }
		state->player_x--;
	}
	return true;
}

bool push_box_right(sokoban_t* init_data,state_t* state)
{
	if (state->map[state->player_y][state->player_x+2] == '$' || state->map[state->player_y][state->player_x+2] == '*') {
		return false;
	} else if (state->map[state->player_y][state->player_x+2] == '#') {
		return false;
	} else {
		state->map[state->player_y][state->player_x+1] = '@';
		if(state->map[state->player_y][state->player_x+2] == '.')
			state->map[state->player_y][state->player_x+2] = '*';
		else
			state->map[state->player_y][state->player_x+2] = '$';
		state->map[state->player_y][state->player_x] = ' ';
         if (is_goal_loc( state->player_y, state->player_x, init_data) && state->map[state->player_y][state->player_x] == ' ') {
	        	state->map[state->player_y][state->player_x] = '.';
        }
		state->player_x++;
	}
	return true;
}

bool push_box_up(sokoban_t* init_data,state_t* state)
{
	if (state->map[state->player_y-2][state->player_x] == '$' || state->map[state->player_y-2][state->player_x] == '*') {
		return false;
	} else if (state->map[state->player_y-2][state->player_x] == '#') {
		return false;
	} else {
		state->map[state->player_y-1][state->player_x] = '@';

		if(state->map[state->player_y-2][state->player_x] == '.')
			state->map[state->player_y-2][state->player_x] = '*';
		else
			state->map[state->player_y-2][state->player_x] = '$';

		state->map[state->player_y][state->player_x] = ' ';
         if (is_goal_loc( state->player_y, state->player_x, init_data) && state->map[state->player_y][state->player_x] == ' ') {
	        	state->map[state->player_y][state->player_x] = '.';
        }
		state->player_y--;
	}
	return true;
}

bool push_box_down(sokoban_t* init_data,state_t* state)
{
	if (state->map[state->player_y+2][state->player_x] == '$' || state->map[state->player_y+2][state->player_x] == '*') {
		return false;
	} else if (state->map[state->player_y+2][state->player_x] == '#') {
		return false;
	} else {
		
		state->map[state->player_y+1][state->player_x] = '@';
		
		if(state->map[state->player_y+2][state->player_x] == '.')
			state->map[state->player_y+2][state->player_x] = '*';
		else
			state->map[state->player_y+2][state->player_x] = '$';
			
		state->map[state->player_y][state->player_x] = ' ';
        if (is_goal_loc( state->player_y, state->player_x, init_data) && state->map[state->player_y][state->player_x] == ' ') {
	        	state->map[state->player_y][state->player_x] = '.';
        }
		state->player_y++;
	}
	return true;
}



/**************************************
* Player Moves given a state state  *
***************************************/

bool move_left_player(sokoban_t* init_data,state_t* state)
{
	if (state->map[state->player_y][state->player_x-1] != '#') {
		if (state->map[state->player_y][state->player_x-1] == '$' || state->map[state->player_y][state->player_x-1] == '*') {
			return push_box_left(init_data, state);
		} else {
			state->map[state->player_y][state->player_x-1] = '@';
			state->map[state->player_y][state->player_x] = ' ';
            if (is_goal_loc( state->player_y, state->player_x, init_data) && state->map[state->player_y][state->player_x] == ' ') {
	        	state->map[state->player_y][state->player_x] = '.';
        	}
			state->player_x--;
            return true;
		}
	}
    return false;
}

bool move_right_player(sokoban_t* init_data,state_t* state)
{
	if (state->map[state->player_y][state->player_x+1] != '#') {
		if (state->map[state->player_y][state->player_x+1] == '$' || state->map[state->player_y][state->player_x+1] == '*') {
			return push_box_right(init_data, state);
		} else {
			state->map[state->player_y][state->player_x+1] = '@';
			state->map[state->player_y][state->player_x] = ' ';
            if (is_goal_loc( state->player_y, state->player_x, init_data) && state->map[state->player_y][state->player_x] == ' ') {
	        	state->map[state->player_y][state->player_x] = '.';
        	}
			state->player_x++;
            return true;
		}
	}
    return false;
}

bool move_up_player(sokoban_t* init_data,state_t* state)
{
	if (state->map[state->player_y-1][state->player_x] != '#') {
		if (state->map[state->player_y-1][state->player_x] == '$' || state->map[state->player_y-1][state->player_x] == '*') {
			return push_box_up(init_data, state);
		} else {
			state->map[state->player_y-1][state->player_x] = '@';
			state->map[state->player_y][state->player_x] = ' ';
            if (is_goal_loc( state->player_y, state->player_x, init_data) && state->map[state->player_y][state->player_x] == ' ') {
	        	state->map[state->player_y][state->player_x] = '.';
        	}
			state->player_y--;
            return true;
		}
	}   
    return false;
}

bool move_down_player(sokoban_t* init_data,state_t* state)
{
	if (state->map[state->player_y+1][state->player_x] != '#') {
		if (state->map[state->player_y+1][state->player_x] == '$' || state->map[state->player_y+1][state->player_x] == '*') {
			return push_box_down(init_data, state);
		} else {
			state->map[state->player_y+1][state->player_x] = '@';
			state->map[state->player_y][state->player_x] = ' ';
            if (is_goal_loc( state->player_y, state->player_x, init_data) && state->map[state->player_y][state->player_x] == ' ') {
	        	state->map[state->player_y][state->player_x] = '.';
        	}
			state->player_y++;
            return true;
		}
	}
    return false;
}


bool execute_move_t(sokoban_t* init_data, state_t* state, move_t move) {

    bool player_moved = false;

    //Determine which button is pushed
    switch (move) {
    case up:      
        player_moved = move_up_player(init_data, state);
        break;

    case down:       
        player_moved = move_down_player(init_data, state);
        break;

    case left:      
        player_moved = move_left_player(init_data, state);
        break;

    case right:        
        player_moved = move_right_player(init_data, state);
        break;

    }

	return player_moved;

	
}

/*****************************************************************************
* Function:    simple_deadlocks                                        		 *
* Parameters:  sokoban_t* init_data, state_t* state                          *
* Returns:     bool                                                          *
* Description: Check if box has been pusehd into a loc in a corner wall or a *
*              freeze deadlock node and loc != destination                   *
*****************************************************************************/

/**
* The four if statements check the four orientations of a sokoban around a box and call functions
* to test if these orientations are corners or freeze deadlocks, returning true if either of these
* possibilities are true (the calls to check if the wall possibility occurred is commented out).
* Early returns are also added throughout the function, which increases its size but decreases 
* its efficiency when running the algorithm
*/
bool simple_deadlocks(sokoban_t* init_data, state_t* state) {
	/* Initialise location and return values */
	bool freeze = false;
  	bool corner = false;
	// bool wall = false;
    int x = state->player_x;
    int y = state->player_y;

    if (state->map[state->player_y + 1][state->player_x] == '$') {
		y = state->player_y + 1;
		//checks if you are in a corner
		corner = corner_check(x, y, init_data, state); 
		if(corner) {
			return true;
		}
		//checks if you are at a freeze deadlock
		freeze = immovable_check(x, y, init_data, state); 
		if(freeze) {
			return true;
		}
		/*
		//checks if a box has been pushed into an unbroken wall
		wall = wall_check(x, y, init_data, state);
		if(wall){
			return true;
		}
		*/
    }

    if( state->map[state->player_y-1][state->player_x] == '$') {
		y = state->player_y - 1;
		//checks if you are in a corner
		corner = corner_check(x, y, init_data, state);
		if(corner) {
			return true;
		}
		//checks if you are at a freeze deadlock
		freeze = immovable_check(x, y, init_data, state); 
		if(freeze) {
			return true;
		}
		/*
		//checks if a box has been pushed into an unbroken wall
		wall = wall_check(x, y, init_data, state);
		if(wall){
			return true;
		}
		*/
    }

	if( state->map[state->player_y][state->player_x + 1] == '$') {
		x = state->player_x + 1;
		//checks if you are in a corner
		corner = corner_check(x, y, init_data, state);
		if(corner) {
			return true;
		}
		//checks if you are at a freeze deadlock
		freeze = immovable_check(x, y, init_data, state); 
		if(freeze) {
			return true;
		}
		/*
		//checks if a box has been pushed into an unbroken wall
		wall = wall_check(x, y, init_data, state);
		if(wall){
			return true;
		}
		*/
    }

	if( state->map[state->player_y][state->player_x - 1] == '$') {
		x = state->player_x - 1;
		//checks if you are in a corner
        corner = corner_check(x, y, init_data, state);
		if(corner) {
			return true;
		}
		//checks if you are at a freeze deadlock
		freeze = immovable_check(x, y, init_data, state); 
		if(freeze) {
			return true;
		}
		/*
		//checks if a box has been pushed into an unbroken wall
		wall = wall_check(x, y, init_data, state);
		if(wall){
			return true;
		}
		*/
    }
    return false;
}

bool corner_check(int x, int y, sokoban_t* init_data, state_t* state){
    // Check if a corner has occurred and it is not a goal location
    if (((state->map[y][x+1] == '#' && state->map[y+1][x] == '#') ||
        (state->map[y+1][x] == '#' && state->map[y][x-1] == '#') ||
        (state->map[y][x-1] == '#' && state->map[y-1][x] == '#') ||
        (state->map[y-1][x] == '#' && state->map[y][x+1] == '#')) &&
        !is_goal_loc( state->player_y, state->player_x, init_data) ) {
            return true;  
    }
    return false;
}

/**
* This was an original early deadlock check function that I have made
* It checks if a box has been pushed against an unbroken wall with no goals along it
* This returns true if this is the case (function is incomplete)
*/
bool wall_check(int x, int y, sokoban_t* init_data, state_t* state){ 
	/* Variable initialisation for the iteration process */
	int move = 1;
	char right_above = state->map[y + 1][x + move];
	char right= state->map[y][x + move];
	char left_above = state->map[y + 1][x - move];
	char right_below = state->map[y - 1][x + move];
	char left_below = state->map[y - 1][x - move];
	char left = state->map[y][x - move];
	bool right_corner = false;
	bool left_corner = false;

	//check the point isn't a goal
	if(is_goal_loc( state->player_y, state->player_x, init_data)){
		return false;
	}

	//Iterate along the wall left and right simultaneously using the move stepper
	while(1) {
		if ((right_above == '#' || right_above == '$' || right_above == '*') &&
    	(left_above == '#' || left_above == '$' || left_above == '*') &&
    	(right_below == '#' || right_below == '$' || right_below == '*') &&
    	(left_below == '#' || left_below == '$' || left_below == '*')) {
			break;
		}
		if ((right == '.') || (left == '.')) {
			break;
		}	
		if (right == '#' || right == '$' || right == '*') {
			right_corner = true;
		}
		if (left == '#' || left == '$' || left == '*') {
			left_corner = true;
		}

		//If both sides have reached a wall, it is a wall deadlock
		if(left_corner && right_corner) {
			return true;
		}
		move++;
	}
	return false;
}

/**
* This function is called to check if a freeze deadlock has occurred
* It checks the horizontal and vertical axes, and if both are blocked, a freeze deadlock has occurred
*/
bool immovable_check(int x, int y, sokoban_t* init_data, state_t* state){
	// Boolean variables to determine if either axis is blocked
	bool x_block = false;
	bool y_block = false;

	// Checks the horizontal axis for a block
	if((state->map[y][x + 1] == '#' || state->map[y][x - 1] == '#') || 
	(corner_check(x - 1, y, init_data, state) && corner_check(x + 1, y, init_data, state))) {
		if(state->map[y][x - 1] != '.' && state->map[y][x + 1] != '.') {
			x_block = true;
		}
	}

	// Checks the vertical axis for a block
	if((state->map[y + 1][x] == '#' || state->map[y - 1][x] == '#') ||
	(corner_check(x, y - 1, init_data, state) && corner_check(x, y + 1, init_data, state))) {
		if(state->map[y - 1][x] != '.' && state->map[y + 1][x] != '.') {
			y_block = true;
		}
	}

	// If both axes are blocked and it is not a goal location, return true
	return ((x_block && y_block)  && (!is_goal_loc( state->player_y, state->player_x, init_data)));
}

/*****************************************************************************
* Function:    winning_condition                                             *
* Parameters:  sokoban_t* init_data, state_t* state                          *
* Returns:     bool                                                          *
* Description: Check if all boxes are in a destination                       *
*****************************************************************************/


bool winning_condition(sokoban_t* init_data, state_t* state)
{
	if(!state){
		return false;
	}
	if(!state->map){
		return false;
	}    
for (int y = 0; y < init_data->lines; y++) {
            for (int x = 0; init_data->map_save[y][x] != '\0'; x++) {
                if (state->map[y][x] == '$')
                    return false;
            }
    }

    return true;

}

/*********
* MACROS *
*********/
#include <string.h>
#define TERMINAL_TYPE (strcmp(getenv("TERM"), "xterm") == 0 ? "rxvt" : \
  getenv("TERM"))

void play_solution( sokoban_t init_data, char* solution ){

	SCREEN *mainScreen = newterm(TERMINAL_TYPE, stdout, stdin);
	set_term(mainScreen);
	int cols = 1;
	for(int i = 0; i < init_data.lines; i++){
		if(strlen(init_data.map[i]) > (size_t) cols){
			cols = strlen(init_data.map[i]);
		}
	}
	WINDOW *mainWindow = newwin(init_data.lines, cols, 0, 0);

	cbreak();
	noecho();
	keypad(stdscr, TRUE);
	clear();
	
	for (long unsigned int i = 0; i <= strlen(solution); i++) { 
		touchwin(mainWindow);
		wnoutrefresh(mainWindow);
		doupdate();
		refresh();
		for (int i = 0; i < init_data.lines; i++)
			mvprintw(i, 0, init_data.map[i]);
		move(init_data.player_y, init_data.player_x);

		int key_pressed = 0;

		if( solution[i] == 'u' || solution[i] == 'U')
			key_pressed = KEY_UP;
		else if( solution[i] == 'd' || solution[i] == 'D')
			key_pressed = KEY_DOWN;	
		else if( solution[i] == 'l' || solution[i] == 'L')
			key_pressed = KEY_LEFT;	
		else if( solution[i] == 'r' || solution[i] == 'R')
			key_pressed = KEY_RIGHT;	
		init_data = key_check(init_data, key_pressed);
		init_data = check_zone_reset(init_data);
		usleep(500000);
	}
	touchwin(mainWindow);
	wnoutrefresh(mainWindow);
	doupdate();
	refresh();
	usleep(1500000);
}

void print_map(sokoban_t* init_data, state_t* state ){
	initscr();
	cbreak();
	noecho();
	clear();
	for (int i = 0; i < init_data->lines; i++){
		mvprintw(i, 0, state->map[i]);
		move(state->player_y, state->player_x);
	}
	refresh();
}
