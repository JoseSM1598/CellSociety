# Code Refactoring 

## Duplication Refactoring
We did not really have many duplicated code. Our code smells generally came from too much complexity in the nested for loops and if loops, which we will work on fixing. 

## Checklist Refactoring
We focused on fixing the classes that each of us made, first. For example, José worked on the XMLReader and the FireCell class, so he worked on fixing the issues in those classes first, while Natalie worked on the GUI class that she mostly worked on. 

Most of the issues were a result of magic values in the GUI. Natalie went through her classes--GUI, Parameter, and Stat--and replaced these with private final variables. 

There was an issue of a code smell within the WatorCell class due to three nested if statements in the update method. To refactor this issue, Joyce created a new method to get rid of the issue. This would also increase the readability and decrease the length of the update method.

## General Refactoring
There were not many issues in the Code Smells or Java Code sections, and the issues that were there were pretty easy to fix. 

For Code Smells, the issues involved fixing a quadruple for loops and nested if statements, which we chose not to fix before we implemented different kinds of Grid shapes, as each will have a different kind of getNeighbors() method. For the Java code, some of the issues were "Add constructor to Main java class", which we agreed on wasn't necessary. 

Other changes that we made include fixing the way we defined variables, for example in the method parameter declarations we would say hashSet<>var when we should have been using the interface, set<> instead of hashSet<>. We also changed the get/set methods in the Grid class to only return unmodifiable sets/lists
## Longest Method Refactoring

We only had a couple of methods that were flagged as too long. Most of those methods were 11 lines (which we didn't think was that huge of an issue). 