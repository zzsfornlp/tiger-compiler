all: Lexer.java Parser.java Sym.java

Lexer.java: tiger.flex
	java -cp JFlex.jar JFlex.Main tiger.flex
	
Parser.java: tiger.cup java_cup
	java -cp . java_cup.Main -parser Parser -symbols Sym  < tiger.cup

Sym.java: tiger.cup java_cup
	java -cp . java_cup.Main -parser Parser -symbols Sym  < tiger.cup

java_cup: java_cup.zip
	unzip -u java_cup.zip

clean:
	rm -f Lexer.java Parser.java Sym.java Lexer.java~ Parser.java~
	rm -rf java_cup

