package ch.liquidmind.inflection.compiler;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath.ClassInfo;

import ch.liquidmind.inflection.loader.TaxonomyLoader;
import ch.liquidmind.inflection.model.compiled.TaxonomyCompiled;
import ch.liquidmind.inflection.util.ExceptionWrapper;

public class CompilationJob
{
	public enum CompilationMode
	{
		NORMAL,
		BOOTSTRAP
	}
	
	private TaxonomyLoader taxonomyLoader;
	private File targetDirectory;
	private CompilationMode compilationMode;
	private Map< String, TaxonomyCompiled > knownTaxonomiesCompiled = new HashMap< String, TaxonomyCompiled >();
	private List< CompilationUnit > compilationUnits = new ArrayList< CompilationUnit >();
	private Set< ClassInfo > allClassesInClassPath;
	
	public CompilationJob( TaxonomyLoader taxonomyLoader, File targetDirectory, CompilationMode compilationMode, File ... sourceFiles )
	{
		super();
		this.taxonomyLoader = taxonomyLoader;
		this.targetDirectory = targetDirectory;
		this.compilationMode = compilationMode;
		
		for ( File sourceFile : sourceFiles )
			compilationUnits.add( new CompilationUnit( sourceFile, this ) );
	}

	public TaxonomyLoader getTaxonomyLoader()
	{
		return taxonomyLoader;
	}
	
	public File getTargetDirectory()
	{
		return targetDirectory;
	}
	
	public CompilationMode getCompilationMode()
	{
		return compilationMode;
	}

	public Map< String, TaxonomyCompiled > getKnownTaxonomiesCompiled()
	{
		return knownTaxonomiesCompiled;
	}

	public List< CompilationUnit > getCompilationUnits()
	{
		return ImmutableList.copyOf( compilationUnits );
	}
	
	public List< CompilationFault > getCompilationFaults()
	{
		List< CompilationFault > compilationFaults = new ArrayList< CompilationFault >();
		
		for ( CompilationUnit compilationUnit : compilationUnits )
			compilationFaults.addAll( compilationUnit.getCompilationFaults() );
		
		return compilationFaults;
	}
	
	public boolean hasCompilationErrors()
	{
		return CompilationUnit.containsCompilationErrors( getCompilationFaults() );
	}
	
	public void printFaults()
	{
		printFaults( System.err );
	}
	
	public void printFaults( OutputStream os )
	{
		for ( CompilationFault fault : getCompilationFaults() )
			fault.print( os );
	}

	Set< ClassInfo > getAllClassesInClassPath()
	{
		if ( allClassesInClassPath == null )
			allClassesInClassPath = ExceptionWrapper.ClassPath_from( getTaxonomyLoader().getClassLoader() ).getAllClasses();
		
		return allClassesInClassPath;
	}
}
