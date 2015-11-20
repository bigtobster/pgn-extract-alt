# PGN-extract-alt
A highly extensible modular rewrite of the PGN processing application pgn-extract written in Java and available on Windows, Mac and Linux.

##Latest##
 <ul>
 	<li>Project Started! - 19/11/2015</li>
 </ul>

##Overview##
This is a research project conducted at the University of Kent, UK.

PGN-extract is a popular and useful PGN processing application written by David Barnes and can be found <a href="https://www.cs.kent.ac.uk/people/staff/djb/pgn-extract/">here</a>. Over time, it has become monolithic and difficult to extend. This has also placed limitations on optimisations such as filtering ordering and concurrency as well as community contribution. The original PGN-extract project is written in C which, whilst highly performant, does limit the project in terms of extension and compatibility. PGN-extract is feature rich and a full list of features can be found <a href="https://www.cs.kent.ac.uk/people/staff/djb/pgn-extract/help.html">here</a>.

PGN-extract-alt aims to provide a modular alternative to PGN-extract using modern tools and languages. It is not intended to reproduce all of the features currently available in PGN-extract but instead provide a framework and accompanying documentation for the community to add whatever features they desire. PGN-extract-alt's core will be released with the following features:

<ul>
	<li>Filter Ordering Optimiser</li>
	<li>PGN type conversion (SAN, FEN, etc)</li>
	<li>Tag editing</li>
	<li>Interactive CLI Interface</li>
	<li>Highly concurrent implementation</li>
</ul>

Additionally, the following (non-essential) modules will be released with the core package:

<ul>
	<li>Machine Correlation Filtering</li>
	<li>Move Bound Filtering</l>
	<li>Result Filtering</li>
	<li>Duplicate Game Filtering</li>
	<li>Set Based Filtering</li>
</ul>

Recommendations for future modules are welcomed. 

##Installation##
TBC

##Usage##
TBC

##Documentation##
TBC

##Tutorial##
TBC

##Structure##
The core of PGN-extract-alt is based on Chesspresso (http://www.chesspresso.org/). The interface is built on Clamshell-CLI (https://github
.com/vladimirvivien/clamshell-cli).

The Machine Correlation module uses Stockfish 6 as its benchmark (https://stockfishchess.org/) wrapped in a thin-client called Chess-misc (https://github.com/rahular/chess-misc). 

TBC

##Contact##
Developer: Toby Leheup - toby.leheup@googlemail.com

Supervisor: Dr David Barnes - D.J.Barnes@kent.ac.uk

##Licence##

Code and documentation copyright 2015 Toby Leheup.

Code released under the MIT license.

Documentation released under the Creative Commons license.
