package ch.liquidmind.inflection.compiler.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.google.common.io.Files;

import __java.nio.file.__Files;
import ch.liquidmind.inflection.compiler.CompilationJob;
import ch.liquidmind.inflection.compiler.CompilationJob.CompilationMode;
import ch.liquidmind.inflection.compiler.InflectionCompiler;
import ch.liquidmind.inflection.loader.TaxonomyLoader;
import ch.liquidmind.inflection.test.mock.AbstractFileMock;
import ch.liquidmind.inflection.test.mock.InflectionFileMock;
import ch.liquidmind.inflection.test.mock.JavaFileMock;

public final class InflectionCompilerTestUtility
{
	public static CompilationJob createCompilationJob( InflectionFileMock... inflectionFileMocks )
	{
		return createCompilationJob( TaxonomyLoader.getSystemTaxonomyLoader(), inflectionFileMocks );
	}

	public static CompilationJob createCompilationJob( TaxonomyLoader taxonomyLoader, InflectionFileMock... inflectionFileMocks )
	{
		if ( inflectionFileMocks == null )
		{
			throw new IllegalArgumentException( "inflectionFileMocks must not be null" );
		}
		File[] inflectionFileArray = writeFileMocksToFiles( inflectionFileMocks );
		return new CompilationJob( taxonomyLoader, __Files.createTempDirectory( null, "tax", new FileAttribute< ? >[ 0 ] ).toFile(), CompilationMode.NORMAL, inflectionFileArray );
	}

	/**
	 * Compiles *.inflect files to *.tax files
	 * 
	 * @param inflectionFileMocks
	 * @return directory location with compiled *.tax files
	 */
	public static File compileInflection( InflectionFileMock... inflectionFileMocks )
	{
		CompilationJob job = createCompilationJob( inflectionFileMocks );
		InflectionCompiler.compile( job );
		File compiledTaxonomyDir = job.getTargetDirectory();
		assertTrue( compiledTaxonomyDir.exists() );
		assertTrue( job.getCompilationFaults().isEmpty() );
		return compiledTaxonomyDir;
	}

	/**
	 * Compiles *.java files to *.class files
	 * 
	 * @param javaFileMocks
	 * @return directory location with compiled *.class files
	 */
	public static File compileJava( JavaFileMock... javaFileMocks )
	{
		if ( javaFileMocks == null )
		{
			throw new IllegalArgumentException( "javaFileMocks must not be null" );
		}
		File outputDir = Files.createTempDir();
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector< JavaFileObject > diagnostics = new DiagnosticCollector< JavaFileObject >();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager( diagnostics, null, null );
		Iterable< ? extends JavaFileObject > compilationUnits = fileManager.getJavaFileObjects( writeFileMocksToFiles( javaFileMocks ) );
		Iterable< String > options = Arrays.asList( "-d", outputDir.getAbsolutePath() );
		JavaCompiler.CompilationTask task = compiler.getTask( null, fileManager, diagnostics, options, null, compilationUnits );
		Boolean result = task.call();
		assertTrue( "Successful Java Compilation", result );
		return outputDir;
	}

	private static File[] writeFileMocksToFiles( AbstractFileMock... fileMock )
	{
		Path rootDir = __Files.createTempDirectory( null, "filemock", new FileAttribute< ? >[ 0 ] );
		List< File > inflectFileList = new ArrayList< >();
		for ( AbstractFileMock mock : fileMock )
		{
			Path file = mock.writeToFile( rootDir );
			inflectFileList.add( file.toFile() );
		}
		File[] inflectionFileArray = inflectFileList.toArray( new File[ inflectFileList.size() ] );
		return inflectionFileArray;
	}

	public static void assertSuccessfulCompilation( CompilationJob job )
	{
		assertFalse( "Compilation units must exist", job.getCompilationUnits().isEmpty() );
		assertFalse( "Compilation errors must not exist", job.hasCompilationErrors() );
	}

	public static void assertCompilationFailure( CompilationJob job )
	{
		assertTrue( job.hasCompilationErrors() );
		assertFalse( "Compilation errors must not exist", job.getCompilationFaults().isEmpty() );
		String message = job.getCompilationFaults().get( 0 ).getMessage();
		assertNotNull( "Fault message must exist", message != null );
		assertTrue( "", message.length() > 0 );
	}

}
