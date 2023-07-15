# Cellular Automaton
This project implements a cellular automaton and displays it on a Swing screen. 
Several options are available to modify the cellular automaton during runtime, like resizing and reinitialization. 
Finally, one can navigate through the game steps and can save the different automaton steps into files. 

## Introduction 

This project implements a cellular automaton and displays it on a Swing screen. 
The following gives the definition of cellular automatons as extracted from the [Automaton Wikipedia page](https://en.wikipedia.org/wiki/Cellular_automaton):

***Cellular Automaton:*** A cellular automaton is a discrete model of computation studied in automata theory. 
A cellular automaton consists of a regular grid of cells, each in one of a finite number of states, such as *on* and *off*. The grid can be in any finite number of dimensions. For each cell, a set of cells called its *neighborhood* is defined relative to the specified cell. An initial state (time t = 0) is selected by assigning a state for each cell. A new generation is created (advancing t by 1), according to some fixed rule (generally, a mathematical function) that determines the new state of each cell in terms of the current state of the cell and the states of the cells in its neighborhood. Typically, the rule for updating the state of cells is the same for each cell and does not change over time, and is applied to the whole grid simultaneously.

This project implements the cellular automaton in a 2-dimensionnal matrix. 
The initial state consists of the cells of the board set to living or dead. 
At each turn, the new state of each cell is computed following the rule:
* If a dead cell has 3 living neighbors then it is set to the living state.
* If a cell is living state and has 2 or 3 living neighbors then it stays alive.
* Otherwise the cell is set into the dead state.  

## Board choices 



## Options 

## How to run
