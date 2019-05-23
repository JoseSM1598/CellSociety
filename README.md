# Cell Society
**Names**: Natalie Le, Jose San Martin, Joyce Zhou
**Timeline**: September 17th - 30th, ~30 hours
**Roles**:
* Natalie = GUI / front-end, simulation
* Jose = XML Reader, simulation
* Joyce = Simulation

**Starting the Project**: Main.java

**Testing files**: The XML files within the data directory are used to test this project. The errors checked for are described in the next section.

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

All of these errors, with the exception of Not and XML file, are all thrown/fixed in the XMLCheck class. In this way, by the time the information reaches the rest of the code, the information has already been filtered. In other words, there's no possible way that the code will crash after the data is checked.

## Using the Program
The program automatically opens up into a double-paned window, which can be operated independently. The “Upload” button is used to upload an XML file, which will then populate the grid. The speed adjustment, play/pause, and reset functionality is controlled using the bottom buttons. Once an XML file is uploaded, the appropriate parameters for that game populate the pane to the right. The settings of these can be adjusted by the user—clicking the “Submit” button will commit the changes to the simulation, which will reset.

## Assumptions/Simplifications
We assumed that the grid would always be in the shape of a square, and that only the shapes of the cells would change (if at all).

In the Fire simulation, the example online said that “for the sake of simplicity, we will only look through the four cardinal directions.” Since this appeared to be an unwanted constraint, for our Fire simulation we assumed that implementing neighbor-checking in all directions was ideal and preferred, which we did.

## Design Decisions
When phased with the multi-layered simulations (such as SugarScape and Foraging), we changed our grid such that instead of holding cells in every slot, each element would hold a Set of cells so that multiple could be in one location.

## Known Bugs
We do not have a case in our exception-checking for multi-layered simulations, like SugarScape and Foraging. If the user tries to change a state-related parameter (i.e. the number of a certain kind of cell) in any of the these simulations, the program will throw a error saying that the wrong number of cells has been initialized given the grid size. This exception is only valid for single-layered simulations, where the number of cells cannot exceed the total number of spots available.

Additionally, the SugarScape simulation does not work—only its initialization does. When the simulation is played, it looks like the Patch cells are becoming Agents—that’s actually not happening. When an Agent cell moves, it stretches to the size of the Patch cell it is on top of, so you cannot see the Patch underneath. However, beyond that, this simulation is still incomplete, as the Patch cells do not change colors accordingly after initialization and finding the proper cells given its “vision” was not implemented.

## Overall Impressions
We enjoyed this assignment and its emphasis on writing flexible code. The ease with which we were able to implement the additional required feature on the last day—in this case, the extra XML tag <description/> and the GUI to display it—was a testament to how flexible our XML and GUI code was. Although we were grateful that we didn’t have to implement this next feature, in the future it would be interesting if one of the Week 2 requirements was to change the shape of the grid itself, as one of our assumptions was that it would always be a square.
