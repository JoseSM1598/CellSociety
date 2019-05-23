Cell Society Team 09 Design Document
===

By: Jose San Martin, Natalie Le, Joyce Zhou

Design Overview
=

In our Cell Society design, we mostly stuck with our original plan, in that we would have two parts, back-end and front-end, that are connected by a Grid class (that scores the cells). Our Grid class almost acted as a mediator between the two, but not completey so because the Grid class itself was an instance in the GUI that was displayed to the user. 

We have a Cell abstract class that set the basic rules for as to how each cell should behave. For example, each cell, no matter the simulation, should be able to change their state, their color, their position, ect. The Cell class had an abstract method update() that each subclass had to implement. Each subclass of cells represented a different simulation, as our goal was to encapsulate the rules of each simulation within each cell subclass. We chose to do this because of the fact that in most simulations, the cells are updated based on their immediate surroundings, or neighbors. 

The other smaller instance of polymorphism we used was with Grid, as we allowed a class called GridToroidal to extend Grid. This was because Grid had a specific way of how it obtained the neighbors of each cell, so instead of adding 'if' statements to check whether the user wanted a regular grid or a toroidal grid, we used inheritance. 

For the GUI, we encapsulated most of the Front-end logic inside of the GUI class, the parameter class, and the stat class. These three classes worked together to create the screen, the graph, and the parameter sliders. Our design philosophy was that the only way for the GUI to have contact with the cells was through the Grid class, so the cell subclasses were instantiated only in the Grid class. 

Adding new features
=

* Adding a new simulation 
    * In order to add a new simulation, a couple of things need to be done. You first need to add the simulation name to the SimulationType Enum class, as well as add each possible state of the simulation to the States enum class. You then need to create a new subclass for the simulation that extends the abstract Cell class. 
    * After that is done, you need to set up the XML file for the new simulation. Some of the tags that are absolutely needed are author, name, gridHeight, gridWidth, gridShape, edgeType, all of the cellStates and all of the cellRules. 
* Adding a new shape to the Cells
    * We implemented the option to have rectangle, hexagon, and triangle cells. If you wanted to add a new shape type, you would first have to create a new Class with the name of the shape you want, and put it inside the Shapes package. 
    * This new shape class has one goal, to obtain the coordinate of the cell. The shape class takes in the width of the cell, and the x and y positions the cell will be given. From these 3 parameters, the new shape class should be able to obtain the specific points that will added to the Cell (which is a Polygon object), so that the cell can be correctly rendered. 
    * After the class is made, you simply add the new enum to the enum class GridShape, and then inside of the xml of the simulation you wish to run, you put the name of the new shape inside of the gridShape tag

* Changing colors of cell states
    * This is easily done inside of the XML of each simulation. You create a cellColor tag, and the type of each cell is the States, and the content of each tag if the color you want that state to be




Design Choices and Trade-Offs
=

* One of the most important design choices we struggled with in terms of choosing was how to store our Cells. At first, we stored them in a 2D Array, which made it easy for us to obtain the neighbors of each cell, as well as the position of each cell. However, we made the decision to switch to using a 2D array of cellBuckets to hold the cells, where each cellBucket is a class we created that are essentially hashsets. This way, we were able to have more than 1 cell at a specific location. This was especially useful when implementing the sugarscape and ant foraging simulations. While it may have been useful, it came with it's problems, as this data structure was only needed for the simulations that had multiple layers, so most of the simulations ended up having only 1 cell in each hashset. This resulted in those simulatios being a bit less efficient than when using simple 2D arrays. 

* Separating the back-end and front-end. Our original goal was to have the Grid class be the intermediate between the GUI and the cells. In practice, this mostly ended up working, and it worked because each cell was added to a root (this root was created in the Grid class), and the GUI would obtain this root to display the cells. That way, when the cells updated themselves, it is reflected on the GUI. 



Assumptions made
=

* One of the biggest assumptions was made is that the user will input the correct cell simulation. If there's no name, or there' appears a simulation that we don't implement, there's no default setting for that. Instead, we chose to handle this by delivering a pop up that would alert the user to their mistake. 
* We also assumed that the user will input a format that they wish in order to instantiate the cells.For example, if the user wanted to use percentages, then they would have to create a tag in which they wrote "percent". Without this, the program returns an alert warning the user of what had occured.