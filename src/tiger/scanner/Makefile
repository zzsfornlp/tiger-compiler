all: Lexer.java Parser.java Sym.java

Lexer.java: tiger.flex
	java -cp JFlex.jar JFlex.Main tiger.flex
	
Parser.java: tiger.cup
	java -cp java_cup.jar java_cup.Main -parser Parser -symbols Sym  < tiger.cup

Symbols.java: tiger.cup
	java -cp java_cup.jar java_cup.Main -parser Parser -symbols Sym  < tiger.cup

clean:
	rm -f Lexer.java Parser.java Sym.java Lexer.java~ Parser.java~
