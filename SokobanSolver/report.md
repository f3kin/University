# Deadlock and Optimizations

Explain your optimizations if applicable:

I choose to implement a freeze deadlock detection. I did so because it seemed simpler to do than a corral deadlock and was 
previously unimplemented. This was done in the *simple_corner_deadlock* function (which I have now renamed *simple_deadlocks*)
in utils.c, which now calls both functions *corner_check* and *immovable_check*. I have slightly updated corner_check, but my main
optimization comes from the freeze deadlock detection. The capability and test maps gain no benefit from this detection, so I used 
a map designed by Ken Liem on Ed Discussion that I named *freeze_test* in *test_maps*. When not using the freeze deadlock, here is
the map's printout:

STATS: 
Expanded nodes: 935
Generated nodes: 3736
Duplicated nodes: 1675
Solution Length: 14
Expanded/seconds: 10456
Time (seconds): 0.089414

Here is the printout when uncommenting the call to *immovable_check* to use the freeze deadlock detection:

STATS: 
Expanded nodes: 891
Generated nodes: 3560
Duplicated nodes: 1612
Solution Length: 14
Expanded/seconds: 10125
Time (seconds): 0.088000

Clearly, there are far less expanded, generated and duplicated nodes when using my freeze deadlock detection.


A freeze deadlock occurs when a box is unable to move in any direction (it is blocked on both axes), meaning that the entire
game is now frozen. To implement, I check whether a goal with a sokoban next to it has non-enterable spaces on two or more of 
its adjacent nodes, and if so, it is a freeze. To test the optimization of this call,

I also attempted to implement both a wall and a heuristic call. The function call to wall_check (commented out as 
incomplete) checks whether a goal is against a horizontal wall that is unbroken vertically and does not have a goal below it,
if so, this is a form of deadlock as the ball cannot be pushed away from the wall, so the node is avoided. I am also in the 
process of creating a heuristic (also potentially commented out) that adds to the priority of nodes based on their distance to
a box, and potentially further, the distance of that box to a goal. I will work on these in the future.

These optimizations may increase the time taken to search simpler maps which don't contain any deadlocks or similar errors, but
they will always decrease (or leave the same) the number of expanded nodes in a search (they are admissible heuristics).
