## Project Title

FI Assessment<br/>
Kenza & **Mehdi**

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


## Development
This project was developed using the following tools:
-  Java Development Kit version 1.8.0_144
-  Apache Maven 3.5.0
-  IntelliJ IDEA 2017.2.5







## Getting Started

These instructions
These instructions will get you a copy of the project up and running on your local machine for
development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
Give examples
```

### Installing

A step by step series of examples that tell you have to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags).

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc
