# cellsociety 

Put your source code, resources, and property files here.

## Error Checks
The error checks that were implemented in out code include:
* Invalid file type - this error is triggered when the user does not input a simulation name that matches 
with any of our simulation names (which are stored in an Enum named "SimulationType")
* No File - if the user goes to select a file, but closes the file explorer before selecting a file, then 
no file is selected and an alert tells the user to actually select a file
* Cell Out Of Bounds - the only way to get this error is if the user wants to specify the position of what 
types of cells that they want. In this case, if they create too many rows/columns for the grid size that
they initialize, then this error warns them of this
* Invalid Number of Cells - this error is triggered when the user puts in the number of each cell that they 
want, but the total sum of all of these cells is not equal to the number of cells in the grid that they initialize
* Invalid Cell Percentage - this error is thrown when the user inputs a percentage that sums to more than 1
* Invalid Cell Type - if the user were to give a cell type that was not a cell type that we have, then they 
would be given this error. We checked the cell name's string with the Enum "states"
* Not XML - this error is the only error thrown outside of the XMLCheck class. If the XMLReader, which parses
the file, encounters a non-XML file, then it will warn the users.
* No Simulation Name - the XML file does not contain a Simulation name. In this case, an alert shows in front of 
the user, and there are no default settings for this error. The user must input what type of Simulation they 
want to see.
* Wrong Height or Width - the user didn't input a height or width, or their height and width input was wrong (a negative 
number, or a decimal). In these cases, either the absolute value is returned, or there's a default setting for grid
height and width, but the user is warned about their mistake.
* Set Initial Config - the initial configurations are how the user wants to input the cells; by percent, by number, 
or by position. The user must specify what initial configuration that they want to use.
* Wrong Initial Config-if the user does not input what type of initial configuration that they want (such as percent,
count, or by position), then they will be given an error.
* No Description - if the user doesn't give a description in their XML file about the nature of the game, then 
a default description "no description found" is returned, but there will be an alert that warns the user about 
the situation 

All of these errors, with the exception of Not and XML file, are all thrown/fixed in the XMLCheck class. In this way, 
by the time the information reaches the rest of the code, the information has already been filtered. In other words, 
there's no possible way that the code will crash after the data is checked.