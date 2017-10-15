package tiger.scanner;

import java_cup.runtime.Symbol;
import tiger.errors.*;

%%

%class Lexer
%unicode
%line
%column
%cup

%{
	StringBuffer collecting_str = new StringBuffer();
	ErrorList e_list;
	
    private int commentCount = 0;
    private Symbol token(int tag) {
        return new Symbol(tag, yyline, yycolumn, new TokenInfo(tag,yyline,yycolumn,null));
    }
    
    private Symbol token(int tag, Object value) {
        return new Symbol(tag, yyline, yycolumn, new TokenInfo(tag,yyline,yycolumn,value));
    }
    private void add_error(String m){
    	if(e_list==null)
    		throw new RuntimeException(m+" at "+yyline+","+yycolumn);
    	else
    		e_list.add_error(m,yyline,yycolumn,"lexer");
    }
    
    public Lexer(java.io.InputStream in,ErrorList e){
    	this(in);
    	e_list = e;
    }
%}

%eofval{
    if (yystate() == YYCOMMENT) {
    	add_error("Comment mismatch, need closing */ at EOF");
    }
    else if(yystate() == YYSTR) {
        add_error("String mismatch, need closing '\"' at EOF");
    }
    return token(Sym.EOF, null);
%eofval}

LineTerm = \n|\r|\r\n
Identifier = [A-Za-z][_a-zA-Z0-9]*
DecInteger = [0-9]+
Whitespace = {LineTerm}|[ \t\f]

%state YYCOMMENT
%state YYSTR

%%

<YYINITIAL> {
    "/*" { commentCount = 1; yybegin(YYCOMMENT); }
    "*/" { add_error("Comment mismatch, extra */ found"); }
    
    \" { yybegin(YYSTR); }
    
    "array"     { return token(Sym.ARRAY); }
    "break"     { return token(Sym.BREAK); }
    "do"      	{ return token(Sym.DO); }
    "else"     	{ return token(Sym.ELSE); }
    "end"     	{ return token(Sym.END); }
    "for"      	{ return token(Sym.FOR); }
    "function"  { return token(Sym.FUNCTION); }
    "if"      	{ return token(Sym.IF); }
    "in"    	{ return token(Sym.IN); }
    "let"    	{ return token(Sym.LET); }
    "nil"   	{ return token(Sym.NIL); }
    "of"      	{ return token(Sym.OF); }
    "then"    	{ return token(Sym.THEN); }
    "to"   		{ return token(Sym.TO); }
    "type"     	{ return token(Sym.TYPE); }
    "var" 		{ return token(Sym.VAR); }
    "while"  	{ return token(Sym.WHILE); }
    
    ","	{ return token(Sym.COMMA); }
    ":" { return token(Sym.COLON); }
    ";" { return token(Sym.SEMICOLON); }
    "(" { return token(Sym.LPAREN); }
    ")" { return token(Sym.RPAREN); }
    "[" { return token(Sym.LBRACK); }
    "]" { return token(Sym.RBRACK); }
    "{" { return token(Sym.LBRACE); }
    "}" { return token(Sym.RBRACE); }
    "." { return token(Sym.DOT); }
    "+" { return token(Sym.PLUS); }
    "-" { return token(Sym.MINUS); }
    "*" { return token(Sym.TIMES); }
    "/" { return token(Sym.DIVIDE); }
    "=" { return token(Sym.EQ); }
    "<>" { return token(Sym.NEQ); }
    "<"  { return token(Sym.LT); }
    "<=" { return token(Sym.LE); }
    ">"  { return token(Sym.GT); }
    ">=" { return token(Sym.GE); }
    "&"  { return token(Sym.AND); }
    "|"  { return token(Sym.OR); }
    ":=" { return token(Sym.ASSIGN); }
    
    {Identifier} { return token(Sym.ID, yytext()); }
    {DecInteger} { return token(Sym.INT, Integer.valueOf(yytext())); }
    {Whitespace} { /* skip */ }
    
    [^] { add_error("Illegal character " + yytext()); }
}

<YYCOMMENT> {
    "/*" { commentCount++; }
    "*/" { commentCount--; if (commentCount == 0) yybegin(YYINITIAL); }
    [^]  {}
}

<YYSTR>{
	\"	{ 	yybegin(YYINITIAL);
			String tmp = collecting_str.toString();
			collecting_str = new StringBuffer();
			return token(Sym.STRING,tmp);
		}
	\\n { collecting_str.append('\n'); }
	\\t { collecting_str.append('\t'); }
	\\\" { collecting_str.append('\"'); }
	\\\\ { collecting_str.append('\\'); }
	\\"^"(@ | [A-Z] | \[ | \] | \\ | _ | \^ ) {
			int tmp = yytext().charAt(2);
			switch(tmp){
				case '@': collecting_str.append((char)0); break;
				case '[': collecting_str.append((char)27); break;
				case '\\': collecting_str.append((char)28); break;
				case ']': collecting_str.append((char)29); break;
				case '^': collecting_str.append((char)30); break;
				case '_': collecting_str.append((char)31); break;
				default:  collecting_str.append((char)(tmp-'A'+1)); break;
			}
		}			
	\\[0-9][0-9][0-9] { 
		String tmp = yytext();
		int y= Integer.parseInt(tmp.substring(1));
		if(0<=y && y<=255)
			collecting_str.append((char)y);
		else
			add_error("Illegal ASCII code "+yytext());
	}
	\\{Whitespace}+\\ { /* skip */ }
	{LineTerm}	{ add_error("Illegal newline "); yybegin(YYINITIAL); }
	. { collecting_str.append(yytext()); }
}
