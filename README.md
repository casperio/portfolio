## Project Title

FI Assessment

## Description
This project is an attempt to implement the system described by FI for the second round interview
scheduled for Monday November 6th at 12:00 (UTC).<br/>
The project describes a kind of Portfolio "assigner" that reads an input file containing the name,
date of birth, and asset-value of clients.  This file is read and processed in such a way that each client, based on their age, is assigned a
type of portfolio.<br/>
The output of the program is a file which contains the name of each client along with the name of
the portfolio assigned to them.

## Getting Started
### Prerequisites
-  Java Development **Kit** version 8
-  Apache Maven 3.5.0 or higher
-  JAVA_HOME should point to the **JDK 8** base directory

### Instructions
Providing Java 8 **JDK** and Maven 3 or higher are installed, the project can be run from the command line:<br/>
mvn clean install exec:java -Dexec.args="--inputDirectory=*<dir1>* --outputDirectory=*<dir2>*"

For example:<br/>
mvn clean install exec:java -Dexec.args="--inputDirectory=c:\in --outputDirectory=c:\out"

## Input-file format
The input file containing the client details should be a **CSV** file formatted as follows:
firstName,lastName,dateOfBirth,assetsValue

For example:<br/>
Matthew,Murdock,1964-04-29,4553068.46

**Note** Date should be specified in the format YYYY-MM-dd

## Development
This project was developed using the following tools:
-  Java Development Kit version 1.8.0_144
-  Apache Maven 3.5.0
-  IntelliJ IDEA 2017.2.5

