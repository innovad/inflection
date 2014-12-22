package ch.zhaw.inflection.compiler;

import java.io.File;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

public class InflectionErrorListener extends BaseErrorListener
{
	private File compilationUnit;
	private boolean syntaxErrorOccured = false;
	
	public InflectionErrorListener( File compilationUnit )
	{
		super();
		this.compilationUnit = compilationUnit;
	}

	@Override
	public void syntaxError( Recognizer< ?, ? > recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e )
	{
		syntaxErrorOccured = true;
		displayError( compilationUnit, (CommonTokenStream)recognizer.getInputStream(), (Token)offendingSymbol, (Token)offendingSymbol, msg );
	}

	public static void displayError( File compilationUnit, CommonTokenStream tokens, Token offendingTokenStart, Token offendingTokenEnd, String msg )
	{
		displayErrorOrWarning( "ERROR: ", compilationUnit, tokens, offendingTokenStart, offendingTokenEnd, msg );
	}
	
	public static void displayWarning( File compilationUnit, CommonTokenStream tokens, Token offendingTokenStart, Token offendingTokenEnd, String msg )
	{
		displayErrorOrWarning( "WARNING: ", compilationUnit, tokens, offendingTokenStart, offendingTokenEnd, msg );
	}
	
	public static void displayErrorOrWarning( String errorOrWarning, File compilationUnit, CommonTokenStream tokens, Token offendingTokenStart, Token offendingTokenEnd, String msg )
	{
		System.err.println( errorOrWarning + compilationUnit.getName() + ", line " + offendingTokenStart.getLine() + ":" + offendingTokenStart.getCharPositionInLine() + " " + msg );

		String input = tokens.getTokenSource().getInputStream().toString();
		String[] lines = input.split( "\n" );
		String errorLine = lines[ offendingTokenStart.getLine() - 1 ];
		System.err.println( errorLine );
		
		for ( int i = 0; i < offendingTokenStart.getCharPositionInLine(); i++ )
			if ( errorLine.substring( i, i + 1 ).equals( "\t" ) )
				System.err.print( "\t" );
			else
				System.err.print( " " );
		
		int start = offendingTokenStart.getStartIndex();
		int stop = offendingTokenEnd.getStopIndex();
		
		if ( start >= 0 && stop >= 0 )
			for ( int i = start; i <= stop; i++ )
				System.err.print( "^" );
		
		System.err.println();
	}

	public boolean syntaxErrorOccured()
	{
		return syntaxErrorOccured;
	}
}
