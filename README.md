# PGN-extract-alt
A highly extensible modular rewrite of the PGN processing application pgn-extract written in Java and available on Windows, Mac and Linux.

##Latest##
 <ul>
 	<li>Project Started! - 19/11/2015</li>
 	<li>PGN-Extract-Alt Released! - 28/01/201</li>
 	<li>Major enhancements including Machine Correlation! - 16/03/2016</li>
 </ul>

##Overview##
This is a research project conducted at the University of Kent, UK.

PGN-extract is a popular and useful PGN processing application written by David Barnes and can be found <a href="https://www.cs.kent.ac.uk/people/staff/djb/pgn-extract/">here</a>. Over time, it has become monolithic and difficult to extend. This has also placed limitations on optimisations such as filtering ordering and concurrency as well as community contribution. The original PGN-extract project is written in C which, whilst highly performant, does limit the project in terms of extension and compatibility. PGN-extract is feature rich and a full list of features can be found <a href="https://www.cs.kent.ac.uk/people/staff/djb/pgn-extract/help.html">here</a>.

PGN-extract-alt aims to provide a modular alternative to PGN-extract using modern tools and languages. It is not intended to reproduce all of the features currently available in PGN-extract but instead provide a framework and accompanying documentation for the community to add whatever features they desire. PGN-extract-alt's core will be released with the following features:

<ul>
	<li>Player Chess Engine Correlation</li>
	<li>Tag Insertion</li>
	<li>Tag Editing</li>
	<li>Result Calculator</li>
	<li>Duplicate Game Filter</li>
	<li>Plycount Filter</li>
	<li>Result Filter</li>
	<li>Interactive CLI Interface</li>
</ul>

Recommendations for future modules are welcomed. 

##Installation##
Installation is via Maven. You will need to install Maven if you have not already done so.

* Download Repo
* Extract
* Open directory with "POM.xml" in a CLI
* Run "mvn install"

Note that currently only 64-bit architecture systems are supported. (If you have a desperate need to use the software in 32-bit, 
remove the Machine Correlation classes from the Chess and Commands packages as well as the whole uciEngine package and then rebuild.)

Installation takes quite a long time - around 5-10 minutes on reasonable hardware. This is due to the thorough and computationally intensive 
testing. To reduce the installation time, either skip the tests ("mvn install -DskipTests") or remove the Machine Correlation functionality (see 
paragraph above).

##Usage##
The binary is deployed to [pgn-extract-alt]/target and will have the filename pgn-extract-alt-[version].jar

To run the binary, open a CLI in the same directory as the binary and type: java -jar [filename]

Currently, only the following tags are supported for insertion:

Event
Site
Date
Round
White
Black
Result
WhiteElo
BlackElo
EventDate
ECO

##Extension##
Other than further modules, two considerable fundamental extensions are being considered:

1. Dependency on an improved version of Chesspresso. This will involve creating a new Chesspresso repo (original source available), depending on that and then extending accordingly. This would mean the maintenance of a second project but would significantly enhance the potential for further PGN-Extract-Alt enhancements.

2. Improving the concurrency model. This will be a particular issue with computationally intense modules. There are various concurrency models available (further details to be referenced). It would be useful to queue operations on data and then execute them in a concurrent fashion that tends towards optimal.

##Machine Correlation##
PGN-Extract-Alt is packaged with Stockfish 7 which is an open-source, strong UCI-compliant chess engine. The full usage specification for Stockfish
 can be found here: http://download.shredderchess.com/div/uci.zip. PGN-Extract-Alt calculates the degree of machine correlation by comparing the 
 quality of a player's move against the quality of the engine's move. The engine will always try to find the strongest move it can.
 
The Machine Correlation functionality is parameterised by 2 values:
 
 * Depth - The number of players ahead that the engine will assess. Higher values produce stronger play from the engine but are more 
 computationally intensive. The default is 13 which is moderately intensive. Very strong players will find an engine playing at depth 20 challenging.
 * Wait - The amount of time the machine should sleep whilst waiting for the engine to calculate. Increase for higher depths. Note that this value 
 is adjusted over time so don't fret over it. The default is usually fine except for high depths (18+).
 
The engine is configured as follows:

* MultiPV = 1
* Ponder = False
* Threads = 4
* Hash Size = 1024 Mb
	
These settings are not parameterised but are easily reconfigurable from the UCIEngine class in the uciEngine package.

The score is appended to the player's name in the output. The closer the score is to 0, the greater the correlation is with engine use.

Note that this functionality is very computationally intensive - even on modest depths and on strong hardware. Also note that the depth parameter 
should increase proportionally to the skill of the player being assessed. 

##Documentation##
Extensive technical documentation is available in <pgn-extract-alt>/target/site. Open index.html in a web browser. The following artifacts are 
available:
 
 * Technical Summary
 * Dependencies
 * Licence
 * Maven Plugin Info
 * Project Team
 * JavaDocs
 * ChangeLogs
 * Unit Test Reports
 
Note that the project follows Spring design patterns. Basic familiarity with Spring is recommended.

In order to extend PGN-Extract-Alt:

1. Add and define a new command class in commands package
2. Add and define a new chess class in chess package (filters go in the filter package)
3. Add your chess class as a Spring Bean (ignore for filters)
4. Test your command class in commands package
5. Test your chess class in chess package
6. Test your filter class in filter package
6. Rebuild (mvn install)
 
##Tutorial##
Once running, type "help" to get a full list of commands.

Tab completion is available to help you with parameters.

PGN processing has the following patterns

1. Import: import --FilePath [pathToPGN]
2. [Processing Commands] e.g: evaluate-result
3. Export: export --FilePath [pathToNewPGN]

A full user guide is currently under consideration.

##Structure##
The core of PGN-extract-alt is based on Chesspresso (http://www.chesspresso.org/). The interface is built on Spring Shell (http://docs.spring
.io/spring-shell/docs/current/reference/htmlsingle/).

Note that the source code is divided into 4 distinct packages:

* chess - For chess logic
* commands - For Spring Shell Commands logic
* filters - For logic that removes chess games
* core - For project dependent classes
* uciEngine - For engine classes

##Contact##
Developer: Toby Leheup - toby.leheup@googlemail.com

Supervisor: Dr David Barnes - D.J.Barnes@kent.ac.uk

##Licence##

Code and documentation copyright 2015 Toby Leheup.

Code released under the GPL v3.0 license.

Documentation released under the Creative Commons license.
