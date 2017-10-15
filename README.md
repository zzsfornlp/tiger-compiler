## *TIGER* Compiler

### What is *TIGER*

A simple compiler for the simple language *TIGER*.

Although simple, it could compile from *TIGER* src code to runnable MIPS code (on SPIM simulator).

*Tiger* is a mini lanuage from the [Tiger Book](http://www.cs.princeton.edu/~appel/modern/java/):

	Andrew, W. Appel, and P. Jens. "Modern compiler implementation in Java." (2002).

### How to run the compiler

The pre-compiled package could be find as `lib/tiger.jar`, simply run the following command will output the MIPS code.

	java -cp lib/tiger.jar <src-file> -o <output-file>

For more information about running, please take a look at `run.md`.

To run the generated MIPS code, the Simulator [SPIM](http://pages.cs.wisc.edu/~larus/spim.html) could be used. ([More info](http://www.cs.princeton.edu/~appel/modern/spim/))
