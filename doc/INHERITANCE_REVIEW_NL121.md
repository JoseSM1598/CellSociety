## Part 1
1. We are separating the XML reading, game play, cells, and grid into different classes. 
2. Our Cell class will be extended once for each simulation. It has an abstract update() method which will encapsulate the logic (rules) of each game.
3. The Cell's coodinates and state are open, as there will be a getter methods to retrieve both. The Grid class will contain the Cells, and the Cells themselves are closed (as in, the neither the main class or XML class should interact with them). 
4. If a Cell has a neighbor that has been moved already when it's trying to update (yet it is trying to survey it), then it is clearly outdated. To ameliorate this, we will allow each Cell to have access to the array within the Grid class, so that they all have a current snapshot. 
5. I define good as *intuitive*. I think it is intuitive that the Cells control their own positions (via their own update functions) via their update() methods. 

## Part 2
1. The Cell class is linked to the Grid class, which initializes it, and the GUI class, which displays it. 
2. The Cell class needs the Grid in order to be able to determine what its neighbors are be. 
3. We can minimize the dependencies by initializing the Cells outside of the Grid. 
4. GameofLifeCell and SegregationCell. They both have coordinates, state, and a need to know their neighbors (superclass). GameOfLife, however, never changes position, so its update() function wouldn't include that. SegregationCell does though, so its update() must incorporate a swap. 

## Part 3
1. Use cases of Cell: swap positions, update state, update display, get state of neighboring cells, choosing location to swap with. 
2. I'm excited to work on the Segregation implementation of the Cell class, as it involves swapping. 
3. I'm worried about making sure that the cells are able to change state and update in two different passes. 