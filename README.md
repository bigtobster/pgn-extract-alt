# PGN-Extract-Alt
A highly extensible modular rewrite of the PGN processing application pgn-extract written in Java and available on Windows, Mac and Linux.

##Latest##
 <ul>
 	<li>Project Started! - 19/11/2015</li>
 	<li>PGN-Extract-Alt Released! - 28/01/201</li>
 	<li>Major enhancements including Machine Correlation! - 16/03/2016</li>
 	<li>Added multiple Methods of splitting duplicate games! - 17/03/2016</li>
 	<li>Updates to documentation! - 17/03/2016</li>
 	<li>New Wiki Created! - 20/03/2016</li>
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

##Installation and Invocation##
See the "Getting Started" section of the [project Wiki](https://github.com/bigtobster/pgn-extract-alt/wiki).

##Usage##
See the [Usage Documentation](https://github.com/bigtobster/pgn-extract-alt/wiki/Usage-Documentation).

##Contact##
Developer: Toby Leheup - toby.leheup@googlemail.com

Supervisor: Dr David Barnes - D.J.Barnes@kent.ac.uk

##Licence##

Code and documentation copyright 2015 Toby Leheup.

Code released under the GPL v3.0 license.

Documentation released under the Creative Commons license.
