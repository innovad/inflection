package ch.liquidmind.inflection.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import ch.liquidmind.inflection.model.compiled.TaxonomyCompiled;

public class CompilationUnit
{
	public static class CompilationUnitRaw
	{
		private File sourceFile;

		public CompilationUnitRaw( File sourceFile )
		{
			super();
			this.sourceFile = sourceFile;
		}

		public File getSourceFile()
		{
			return sourceFile;
		}

		public void setSourceFile( File sourceFile )
		{
			this.sourceFile = sourceFile;
		}
	}

	public static class CompilationUnitParsed
	{
		private ParseTree parseTree;
		private CommonTokenStream tokens;
		private String[] sourceFileContent;
		
		public CompilationUnitParsed()
		{
			super();
		}

		public ParseTree getParseTree()
		{
			return parseTree;
		}

		public void setParseTree( ParseTree parseTree )
		{
			this.parseTree = parseTree;
		}

		public CommonTokenStream getTokens()
		{
			return tokens;
		}

		public void setTokens( CommonTokenStream tokens )
		{
			this.tokens = tokens;
		}
		
		public String[] getSourceFileContent()
		{
			if ( sourceFileContent == null )
			{
				String sourceFileAsString = tokens.getTokenSource().getInputStream().toString();
				sourceFileContent = sourceFileAsString.split( System.lineSeparator() );
			}
			
			return sourceFileContent;
		}
	}
	
	public static class CompilationUnitCompiled
	{
		public static abstract class Import
		{
			private boolean wasReferenced = false;

			public boolean getWasReferenced()
			{
				return wasReferenced;
			}

			public void setWasReferenced( boolean wasReferenced )
			{
				this.wasReferenced = wasReferenced;
			}
		}
		
		public static abstract class ImportedType extends Import
		{}
		
		public static class ImportedClasses extends ImportedType
		{
			private List< ImportedClass > importedClasses = new ArrayList< ImportedClass >();

			public List< ImportedClass > getImportedClasses()
			{
				return importedClasses;
			}
		}
		
		public static class ImportedClass
		{
			private String name;
			private boolean wasExplicit = true;
			
			public ImportedClass( String name, boolean wasExplicit )
			{
				super();
				this.name = name;
				this.wasExplicit = wasExplicit;
			}
			
			public String getName()
			{
				return name;
			}

			public boolean getWasExplicit()
			{
				return wasExplicit;
			}
		}
		
		public static class ImportedTaxonomy extends ImportedType
		{
			private String name;
			
			public ImportedTaxonomy( String name )
			{
				this.name = name;
			}

			public String getName()
			{
				return name;
			}

			public void setName( String name )
			{
				this.name = name;
			}
		}
		
		public static class ImportedPackage extends Import
		{
			private String name;
			
			public ImportedPackage( String name )
			{
				this.name = name;
			}

			public String getName()
			{
				return name;
			}

			public void setName( String name )
			{
				this.name = name;
			}
		}
		
		// Note that types and packages are stored in separate lookup tables to avoid naming
		// conflicts; technically, it is possible to have a package and a class with the same name.
		private String packageName;
		private Map< String, ImportedType > importedTypes = new HashMap< String, ImportedType >();
		private Map< String, ImportedPackage > importedPackages = new HashMap< String, ImportedPackage >();
		private List< TaxonomyCompiled > taxonomiesCompiled = new ArrayList< TaxonomyCompiled >();
		
		public CompilationUnitCompiled()
		{
			super();
		}

		public String getPackageName()
		{
			return packageName;
		}

		public void setPackageName( String packageName )
		{
			this.packageName = packageName;
		}

		public Map< String, ImportedType > getImportedTypes()
		{
			return importedTypes;
		}

		public Map< String, ImportedPackage > getImportedPackages()
		{
			return importedPackages;
		}

		public List< TaxonomyCompiled > getTaxonomiesCompiled()
		{
			return taxonomiesCompiled;
		}
	}
	
	private CompilationUnitRaw compilationUnitRaw;
	private CompilationUnitParsed compilationUnitParsed;
	private CompilationUnitCompiled compilationUnitCompiled;
	
	// Note that compilation errors and warnings may be generated during
	// parsing (syntax error) or during compilation (grammar error), which
	// is why compilationFaults belong here rather than in CompilationUnitParsed
	// or CompilationUnitCompiled.
	private List< CompilationFault > compilationFaults = new ArrayList< CompilationFault >();
	private InflectionErrorListener errorListener = new InflectionErrorListener( this );
	private CompilationJob parentCompilationJob;
	
	public CompilationUnit( File sourceFile, CompilationJob parentCompilationJob )
	{
		super();
		compilationUnitRaw = new CompilationUnitRaw( sourceFile );
		compilationUnitParsed = new CompilationUnitParsed();
		compilationUnitCompiled = new CompilationUnitCompiled();
		this.parentCompilationJob = parentCompilationJob;
	}

	public CompilationUnitRaw getCompilationUnitRaw()
	{
		return compilationUnitRaw;
	}

	public CompilationUnitParsed getCompilationUnitParsed()
	{
		return compilationUnitParsed;
	}

	public CompilationUnitCompiled getCompilationUnitCompiled()
	{
		return compilationUnitCompiled;
	}

	public List< CompilationFault > getCompilationFaults()
	{
		return compilationFaults;
	}
	
	public InflectionErrorListener getErrorListener()
	{
		return errorListener;
	}

	public CompilationJob getParentCompilationJob()
	{
		return parentCompilationJob;
	}

	public boolean hasCompilationErrors()
	{
		return containsCompilationErrors( compilationFaults );
	}
	
	public static boolean containsCompilationErrors( List< CompilationFault > compilationFaults )
	{
		boolean hasCompilationErrors = false;
		
		for ( CompilationFault compilationFault : compilationFaults )
		{
			if ( compilationFault instanceof CompilationError )
			{
				hasCompilationErrors = true;
				break;
			}
		}
		
		return hasCompilationErrors;
	}
}
