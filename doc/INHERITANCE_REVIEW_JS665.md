#Jose San Martin Inheritance Review

## Part 1

* An implementation decision that our design is hiding from other areas of our program is the use of inheritance in our Cell class

*The inheritance hierarchy we are building is around the Cell class. The Cell class is a parent class with subclasses for each simulation, i.e. GameOfLifeCell, ect. The behavior they are based around is the update behavior and the display behavior

* The parts that are closed are methods such as getCell(), updateCell(), and other get/set methods. The parts that are open are methods that are unique to each simulation, such as update(), getState(), swap(). 

* Our current implementation shouldn't have any errors that can be caused by the user

*Our design is good because it is flexible. In my opinion, a good design is one that is flexible and open to the addition of new features. 


## Part 2

* My Grid class and Cell class are very intertwined and dependent on each other. 

*They are based on the behavior of the classes. WHen the cells update, they need to be passed to the Grid so that the grid can be updated, and then the new grid has to be passed to the Cells

* I can create a backendGrid class to fix this. 

* We talked about this a lot already. I think that for my project it would be much better if we tried to encapsulate the classes as much as possible. An example would be to create a DeadCell class and an AliveCell class. 


## Part 3

* Use cases include 

* I am most excited to work on the predator prey simulation

* I am most nervous to work on the swapping feature, which is used in the segregation. 