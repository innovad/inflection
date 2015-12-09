package ch.liquidmind.inflection.grammar;

import org.junit.Test;

import ch.liquidmind.inflection.compiler.util.InflectionCompilerTestUtility;
import ch.liquidmind.inflection.test.AbstractInflectionTest;
import ch.liquidmind.inflection.test.InflectionFileMock;

public class TypeReferenceResolutionTest extends AbstractInflectionTest
{

	@Test
	public void testTypeReferenceResolution_SimpleNameResolutionWithTypeImport_SuccessfulCompilation() throws Exception
	{
		doTest( job -> {
			InflectionCompilerTestUtility.assertSuccessfulCompilation( job );
		} , createInflectionFileMock( "a", "package a; taxonomy A {}" ), 
			createInflectionFileMock( "b", "package b; import a.A; taxonomy B extends A {}" ) );
	}
	
	@Test
	public void testTypeReferenceResolution_FullyQualifiedNameResolutionWithTypeImport_SuccessfulCompilation() throws Exception
	{
		doTest( job -> {
			InflectionCompilerTestUtility.assertSuccessfulCompilation( job );
		} , createInflectionFileMock( "a", "package a; taxonomy A {}" ), 
			createInflectionFileMock( "b", "package b; import a.A; taxonomy B extends a.A {}" ) );
	}
	
	@Test
	public void testTypeReferenceResolution_SimpleNameResolutionWithPackageImport_SuccessfulCompilation() throws Exception
	{
		doTest( job -> {
			InflectionCompilerTestUtility.assertSuccessfulCompilation( job );
		} , createInflectionFileMock( "a", "package a; taxonomy A {}" ), 
			createInflectionFileMock( "b", "package b; import a.*; taxonomy B extends A {}" ) );
	}
	
	@Test
	public void testTypeReferenceResolution_FullyQualifiedNameResolutionWithPackageImport_SuccessfulCompilation() throws Exception
	{
		doTest( job -> {
			InflectionCompilerTestUtility.assertSuccessfulCompilation( job );
		} , createInflectionFileMock( "a", "package a; taxonomy A {}" ), 
			createInflectionFileMock( "b", "package b; import a.*; taxonomy B extends a.A {}" ) );
	}
	
	@Test
	public void testTypeReferenceResolution_SimpleNameResolutionWithoutImport_CompilationFailure() throws Exception
	{
		doTest( job -> {
			InflectionCompilerTestUtility.assertCompilationFailure( job );
		} , createInflectionFileMock( "a", "package a; taxonomy A {}" ), 
			createInflectionFileMock( "b", "package b; taxonomy B extends A {}" ) );
	}
	
	@Test
	public void testTypeReferenceResolution_FullyQualifiedNameResolutionWithoutImport_SuccessfulCompilation() throws Exception
	{
		doTest( job -> {
			InflectionCompilerTestUtility.assertSuccessfulCompilation( job );
		} , createInflectionFileMock( "a", "package a; taxonomy A {}" ), 
			createInflectionFileMock( "b", "package b; taxonomy B extends a.A {}" ) );
	}
	
	@Test
	public void testTypeReferenceResolution_ClasspathResolution_SuccessfulCompilation() throws Exception
	{
		InflectionFileMock[] classpath = { createInflectionFileMock( "a", "package a; taxonomy A {}" ) };
		
		doTest( job -> {
			InflectionCompilerTestUtility.assertSuccessfulCompilation( job );
		} , classpath, createInflectionFileMock( "b", "package b; taxonomy B extends a.A {}" ) );
	}
	
	@Test
	public void testTypeReferenceResolution_IllegalClasspathResolution_CompilationFailure() throws Exception
	{
		InflectionFileMock[] classpath = { createInflectionFileMock( "a", "package a; taxonomy A {}" ) };
		
		doTest( job -> {
			InflectionCompilerTestUtility.assertCompilationFailure( job );
		} , classpath, createInflectionFileMock( "b", "package b; taxonomy B extends A {}" ) );
	}
					
}