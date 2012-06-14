The Assembly Language Simulator was my final year project at Strathclyde university.
The purpose of te project was to support students in developing good models of low-level program execution. The project 

examines the Intel 8086 assembly language and implements a simulator to execute programs, allowing users to log and trace 

the steps of computation, observing changes to the machine store. The application supports assessment whereby students can 

fill in missing sections of assembly code.
The simulator will show users how each segment (code segment (CS), data segment (DS), stack segment (SS), and extra segment 

(ES)) are affected as the program executes, as well as how the machine store is affected.

System Requirements

The application was developed using Java 6, in order for the application to run properly, the latest java development must 

be installed on the user’s system.

Running application

Execute the ant script (build.xml). This will generate a jar file named assembly_language_simulator.jar

You can launch the application directly from the file using the following command:

java -jar Assembly_Language_Simulator.jar

By Double-clicking on the file using a windows machine this should launch the application.

Developing an Assembly Program

Assembly code can be written in the text editor. The user can assemble the program by pressing the “Assembler” button. 
Either ‘Step’ or ‘Run’ can be clicked, by the user, in order to show the animation.

Step animates the current line of the program loaded. The user can press this repeatedly until the program halts or runs 

out of lines to be executed.

Run animates the program at a selected speed. You can stop and continue during its execution.

Developing a high Level Program

This application comes with six sample programs, but you can choose to type in your own program. The program is compiled by 

pressing the “compile” button. This will compile the program to 8086 assemble code.