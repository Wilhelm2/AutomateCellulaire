# Cellular Automaton
This project implements a cellular automaton and displays it on a Swing screen. 
Several options are available to modify the cellular automaton during runtime, like resizing and reinitialization. 
Finally, one can navigate through the game steps and can save the different automaton steps into files. 

## Introduction 

This project implements a cellular automaton and displays it on a Swing screen. 
The following gives the definition of cellular automatons as extracted from the [Automaton Wikipedia page](https://en.wikipedia.org/wiki/Cellular_automaton):

***Cellular Automaton:*** A cellular automaton is a discrete model of computation studied in automata theory. 
A cellular automaton consists of a regular grid of cells, each in one of a finite number of states, such as *on* and *off*. The grid can be in any finite number of dimensions. For each cell, a set of cells called its *neighborhood* is defined relative to the specified cell. An initial state (time t = 0) is selected by assigning a state for each cell. A new generation is created (advancing t by 1), according to some fixed rule (generally, a mathematical function) that determines the new state of each cell in terms of the current state of the cell and the states of the cells in its neighborhood. Typically, the rule for updating the state of cells is the same for each cell and does not change over time, and is applied to the whole grid simultaneously.

This project implements a 2-dimensionanl cellular automaton. 
In the initial state the cells of the board are either in the living or dead state. 
At each turn, the new state of each cell is computed following the rules:
* If a dead cell has 3 living neighbors then it is set to the living state.
* If a cell is in the living state and has 2 or 3 living neighbors then it stays alive.
* Otherwise the cell is set into the dead state.  

## Board choices 
The initial automaton state can be configured through the choice box which offers the following options to initialize the board: 
* **Random:** Sets ~100 randomly selected cells to the living state. 
* **Chessboard:** Initializes the board as a chessboard, ie a chaining of one living cell followed by a dead one.
* **Import file:** Imports a board from a file. The format of the file is: *O* represents a living cell and *X* a dead cell. Characters are separated by spaces and each line represents a row. See the *inputExample.txt* for an example of input file.
* **Self input:** Opens a panel of *JButtons* such that the user defines the board's initial state by hand.

## Options 
The game screen offers several options:
* **Navigate through the simulation::** The *forward* allows to go forward in the simulation steps up to the most advanced computed step. The *backward* buttons allows to go backward in the simulation steps till the initial state.
* **Stop:** The *stop* button allows to stop and resume the simulation.
* **Speed:** The user can set the simulation speed in the *Speed* textfield. The initial speed is of 1 step per second. Setting the speed to 10 sets the simulation speed to 1 step per 100ms.
* **Save image:** Saves the current automaton state as well as the initial simulation state in the folder *Image*.
* **Reset:** Resets the simulation to an empty automaton. Resets the game history as well.
* **Resize board:** Displays a dialog to input the new board dimensions, then initializes an empty board to the given dimensions.

## How to run

