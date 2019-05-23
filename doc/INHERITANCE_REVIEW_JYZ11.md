#PART 1

1. What is an implementation decision that your design is encapsulating (i.e., hiding) for other areas of the program?

Some parts of our front end code (such as the GUI, but not the display of the Grid class) are entirely separated 
from the backend code. However, my partner's code completely separates the front and backend code. This makes sense,
because you can check if the displays/buttons work without having the added complication of backend logic messing 
everything up. Debugging the front/back end would be independent of each other.

2. What inheritance hierarchies are you intending to build within your area and what behavior are they based around?

For the both of us, we made the abstract Super Cell class based upon methods/instance variables that all cells 
should have, and each extension/child class, has it's own specific methods (for example, the Game of Life cell 
wouldn't move, but a Fish cell would move). However, I added all of the rules under the cell class in my code, but
my partner has them separated in different classes that also follows a super/child hierarchy, which makes the 
code cleaner, but also has more dependencies. 

3. What parts within your area are you trying to make closed and what parts open to take advantage of this polymorphism you are creating?

The parts that I wanted to make closed were the specific cell types, because there will no longer be anything else 
that extends from that class, but the parent class should have abstract methods that are open for Overrides. My 
partner also made the back end and front end closed from each other. However, for the both of us, our specific cell
classes can redefine methods from the parent class to suit their own needs.

4. What exceptions (error cases) might occur in your area and how will you handle them (or not, by throwing)?

If the information from XML file is input into a cell, and then the cell has no idea how to use it, then there
should be error checks that would send the user messages that would say something like "please input a correctly" 
formatted file.

5. Why do you think your design is good (also define what your measure of good is)?

My definition of good is that there wouldn't be as much dependencies on classes. For example, my partner was 
able to separate the cell from the rules, but they both depend on each other to make a view, and then to implement 
the rules on that view. However, because the rules and cell image are combined in my code, there's less dependencies.
However, this also makes my classes super long and harder to debug.

#PART 2
1. How is your area linked to/dependent on other areas of the project?

The cell class is linked directly to the Grid class: a cell requires the grid class to send information about the 
its neighbors, because the cell itself won't know what's surrounding it. Then, the grid needs the cell class to 
fill it up.

2. Are these dependencies based on the other class's behavior or implementation?

These dependencies are based on other class' behaviors, because if the grid were to give wrongly formatted data
to the cell class, like the neighbors, then the cell class wouldn't be able to function as it should. On the 
other hand, if the cell could not be initialized properly, then the Grid class wouldn't be able to display
anything.

3. How can you minimize these dependencies?

Separate the Grid view from the Backend Grid stuff, so that the dependencies wouldn't be so integrated. In this 
way, at least the Grid class would be able to display things even without the cell class, and the Backend 
Grid would be passing the information back and forth to update it to the Grid, which makes everything a bit 
cleaner.

4. Go over one pair of super/sub classes in detail to see if there is room for improvement. 

Super Cell, and then Game of Life cell. The super cell has all the methods and instance variables that all cells 
should have, and the game of life cell specifies some of the abstract methods in the cell class. 

5. Focus on what things they have in common (these go in the superclass) and what about them varies (these go in the subclass).

The view of the cell is shared amongst all cells, but a subclass of the cell would specify the size and color of 
what the actual cell has. Also, a segregation cell might have an extra method "move", where the physical cell can
move around. Other cells do not need this method, so it does not exist in the parent cell class.

#PART 3

1. Come up with at least five use cases for your part (most likely these will be useful for both teams).

    * Have a preset rows/columns of cells generated (randomly) to see if they can properly follow rules, based on 
    neighbors and things like that (not dependent on the parser)
    * Print statements to follow the sequence of method calls throughout the code
    * Create test cases for XML that purposely gives awful information to see if out code can handle it/passes the
    right error messages
    * Switch simulations and makes sure that the entire previous simulation is deleted. 
    * Adding a separate simulation (that is not one of the four that we created) without breaking into any of our 
    previous code

2. What feature/design problem are you most excited to work on?

I am most excited about getting all the cases of the cell to start working. I really like how we are using 
inheritance, because I've never done that, and I'm really excited to learn how to use it and to apply it 
to all different cell types and watch them behave differently, even though they have the same parent class. 

3. What feature/design problem are you most worried about working on?

I am most worried about integrating the cell class with the grid class. I think the code will get really messy
because on my team, we separated the Grid and cell class to different people. But, to work on my cell class, I 
need to change code in the Grid class, and my teammate needs to depend heavily on the Cell class in order to 
test anything in the Grid class. This will be a huge mess and has the potential to be a huge gitlab merge problem.