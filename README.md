# Slint

Slint is the Shared Library Install Name Tool project to make creating a collection of a compiled binary
program and it's dependent shared libraries an easy task.  So perhaps an easier way to distribute compiled
code with your project.

Currently just the Mac is supported.  Since the Mac does not allow for building static libraries and binary
executables, a program like this is most handy on the Mac.

The idea for this program comes from [Comskip] (https://github.com/erikkaashoek/Comskip) and
[matryoshka-name-tool] (https://github.com/essandess/matryoshka-name-tool).  Comskip mentions the use
of matryoshka-name-tool to perform this task for the Mac.  Unfortunately I could not get matryoshka-name-tool
to work.  It's most likely an operator error on my part and since I'm not a Python user I thought I'd spend a
day or two writing similar functionality instead of hacking the matryoshka-name-tool script.

I came to building Comskip for my project [jflicks(source)] (https://github.com/djb61230/jflicks) and
[jflicks.org] (http://www.jflicks.org).  I have personally used Comskip before (it is terrific) but I hadn't
included it with public jflicks because until I saw the source here on github, it only supported Windows.
Oh you could get it to run on the Mac and Linux with [Wine] (https://www.winehq.org) but that was even more
difficult to distribute.  At the time Comskip was only donation-ware and distributing it was not encouraged
by it's author.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You need a Java 8 JDK.  I used one from Oracle but OpenJDK should work fine.
You also need a Mac.

### Installing

Just clone and build with maven.

## Running the tests

Just one simple test that runs - simply uses the otool program on itself, from Java of course.

## Deployment

Simple 'mvn clean install' will create an executable jar in the target directory.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Doug Barnum** - [jflicks](http://www.jflicks.org)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc

