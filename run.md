### How to build and run the *TIGER* compiler.

------------------------------

### To run the compiler.

You could simply run the compiler with the pre-compiled `lib/tiger.jar` (by jdk9). (Using `-h` option to get more details about running.)

	java -cp lib/tiger.jar tiger.compiler.Compiler <src-file> -o <output-file>

Here is an example of compiling queens.tig from the testcases.

	# Assuming at the top directory of this package
	java -cp lib/tiger.jar tiger.compiler.Compiler Testcases/Official/Good/queens.tig -o queens.s

------------------------------

### To build the compiler from scratch.

If you want to compile from source codes, the procedures are also simple.

#### Build Step 1 (optional): build the scanner (lexical analyzer) and parser (syntactical analyzer).

The scanner and parser are built by the [JLex lexical analyzer generator](http://www.cs.princeton.edu/~appel/modern/java/JLex/) and [CUP parser generator](http://www.cs.princeton.edu/~appel/modern/java/CUP/). Since the corresponding versions have been included here, simple run `make` in the `scanner` directory will be fine.

	# Assuming at the top directory of this package
	cd src/tiger/scanner
	make

This will generate `Lexer.java`, `Parser.java` and `Sym.java` which will be used for the *TIGER* compiler. The pre-built version of these files have been included, thus this step is optional.

#### Build Step 2: compile the whole compiler.

Simply using `javac` to compile them.

	# Assuming at the top directory of this package
	cd src
	javac -cp . tiger/compiler/Compiler.java

#### Build Step 3: run the compiler.

Run the compiler with the compiled class file.

	# Assuming at the top directory of this package
	cd src
	java -cp . tiger.compiler.Compiler <src-file> -o <output-file>


