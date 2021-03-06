grammar Inflection;

@header
{
	package ch.liquidmind.inflection.grammar;
}

/*
 * Notes:
 * - Rename keyword "use" to "uses" to be more in line with "extends", "implements", etc.
 */
 
/*
 * Ideas:
 * - New modifier operation type which can be either read, write or readwrite. Can be
 *   set at method or taxonomy level.
 * - Solution to bootstraping problem: introduce ability to parse Java class files; the views
 *   could then refer to either compiled or uncompiled classes. This would allow you to run
 *   inflection *before* normal compilation, thus avoiding the catch 22 of classes that
 *   reference proxies that have not yet been generated and thus cannot be compiled.
 * - It should be possible to get a proxy to a super class even if the sub class doesn't
 *   exist in the taxonomy. In other words, the proxy registry should be modified to supply
 *   a proxy of the most specific type, which is java.lang.Object if nothing else (note that
 *   we will have to solve the problem of specifying which objects should *not* be viewed;
 *   this is currently implicitly achieved due to matching the specific type).
 * - Views are currently defined according to include and exclude rules, whereby the rules
 *   are evaluated in the declared order and exclude rules are applied after include rules.
 *   Perhaps it makes more sense to abandon the includes before excludes and simply evaluate
 *   them in their declared order.
 * - One very useful feature would be a taxonomy diff function that would illustrate the
 *   differences between two taxonomies; very important for refactoring.
 */

// COMPILATION UNIT

compilationUnit
	:	packageDeclaration importDeclarations taxonomyDeclaration*
	;

packageDeclaration
	:	specificPackage
	|	defaultPackage
	;
	
specificPackage
	:	PACKAGE aPackage SEMICOLON
	;
	
defaultPackage
	:
	;

importDeclarations
	:	importDeclaration*
	;

importDeclaration
	:	IMPORT importSymbol SEMICOLON
	;

importSymbol
	:	typeImport
	|	packageImport
	;
	
typeImport
	:	type
	;
	
packageImport
	:	aPackage DOT wildcardIdentifier	// wildcardIdentifier must be exactly '*'
	;

// TAXONOMY

taxonomyDeclaration
	:	taxonomyAnnotation* TAXONOMY taxonomyName taxonomyExtensions taxonomyBody
	;
	
taxonomyAnnotation
	:	annotation
	;
	
taxonomyName
	:	identifier
	;
	
taxonomyExtensions
	:	( EXTENDS extendedTaxonomy ( COMMA extendedTaxonomy )* )?
	|	// default is Taxonomy
	;
	
extendedTaxonomy
	:	type
	;

taxonomyBody
	:	CURLY_BRACKET_OPEN defaultAccessMethodModifier viewDeclaration* CURLY_BRACKET_CLOSE
	;

defaultAccessMethodModifier
	:	DEFAULT accessMethodModifier SEMICOLON
	|	// default is null
	;

// VIEW

viewDeclaration
	:	viewAnnotation* includeViewModifier VIEW includableClassSelector ( COMMA includableClassSelector )* ( USE usedClassSelector ( COMMA usedClassSelector )* )? viewBody
	|	excludeViewModifier VIEW excludableClassSelector ( COMMA excludableClassSelector )* SEMICOLON
	;
	
viewAnnotation
	:	annotation
	;
	
includeViewModifier
	:	includModifier
	;
	
excludeViewModifier
	:	excludeModifier
	;	
viewBody
	:	CURLY_BRACKET_OPEN memberDeclaration* CURLY_BRACKET_CLOSE
	;

includableClassSelector
	:	aliasableClassSelector
	|	wildcardClassSelector
	;
	
excludableClassSelector
	:	wildcardClassSelector
	;

aliasableClassSelector
	:	classSelector AS alias
	;
	
usedClassSelector
	:	classSelector
	;

classSelector
	:	type
	;

wildcardClassSelector
	:	type
	|	wildcardType
	;

// MEMBER

memberDeclaration
	:	memberAnnotation* includeMemberModifier accessMethodModifier includableMemberSelector ( COMMA includableMemberSelector )* SEMICOLON
	|	memberAnnotation* excludeMemberModifier accessMethodModifier excludableMemberSelector ( COMMA excludableMemberSelector )* SEMICOLON
	;
	
memberAnnotation
	:	annotation
	;
	
includeMemberModifier
	:	includModifier
	;
	
excludeMemberModifier
	:	excludeModifier
	;
	
accessMethodModifier
	:	PROPERTY
	|	FIELD
	|	// default is null
	;

includableMemberSelector
	:	aliasableMemberSelector
	|	wildcardMemberSelector
	;

excludableMemberSelector
	:	wildcardMemberSelector
	;

aliasableMemberSelector
	:	memberSelector AS alias			// Aliases cannot conflict with member names or other member aliases.
	;

memberSelector
	:	identifier
	;
	
wildcardMemberSelector
	:	identifier
	|	wildcardIdentifier
	;
	
// COMMON

includModifier
	:	INCLUDE?
	;

excludeModifier
	:	EXCLUDE
	;

annotation
	:	AT annotationClass ANNOTATION_BODY?
	;
	
annotationClass
	:	type
	;
	
type
	:	( aPackage DOT )? simpleType
	;
	
wildcardType
	:	( aPackage DOT )? wildcardSimpleType
	;
	
aPackage
	:	identifier ( DOT identifier )*
	;
	
simpleType
	:	identifier
	;
	
wildcardSimpleType
	:	wildcardIdentifier
	;
	
alias		// aliases cannot have wildcards
	:	identifier
	;
	
identifier
	:	IDENTIFIER
	;

wildcardIdentifier
	:	WILDCARD_IDENTIFIER
	;
	
// TOKENS

// TODO: introduce support for nested annotations, or; introduce full support for annotations.
ANNOTATION_BODY
	:	'(' .*? ')'
	;
	
MULTI_LINE_COMMENT
	:	'/*' .*? '*/' -> skip
	;

SINGLE_LINE_COMMENT
	:	'//' .*? '\r'? '\n' -> skip
	;
	
PACKAGE		: 'package';
IMPORT		: 'import';
DEFAULT		: 'default';
TAXONOMY	: 'taxonomy';
EXTENDS		: 'extends';
VIEW		: 'view';
USE			: 'use';
PROPERTY	: 'property';
FIELD		: 'field';
INCLUDE		: 'include';
EXCLUDE		: 'exclude';
AS			: 'as';
BASIC_TYPE	: 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' | 'boolean' | 'char';

// Note that while most of these keywords are not used in the
// grammar, they are never the less declared in the lexer to
// ensure that they are not used as identifiers.
JAVA_KEYWORD
	:	'abstract' | 'continue' | 'for' | 'new' | 'switch'
	|	'assert' | 'default' | 'if' | 'package' | 'synchronized'
	|	'boolean' | 'do' | 'goto' | 'private' | 'this'
	|	'break' | 'double' | 'implements' | 'protected' | 'throw'
	|	'byte' | 'else' | 'import' | 'public' | 'throws'
	|	'case' | 'enum' | 'instanceof' | 'return' | 'transient'
	|	'catch' | 'extends' | 'int' | 'short' | 'try'
	|	'char' | 'final' | 'interface' | 'static' | 'void'
	|	'class' | 'finally' | 'long' | 'strictfp' | 'volatile'
	|	'const' | 'float' | 'native' | 'super' | 'while'
	;
	
IDENTIFIER
	:	[a-zA-Z_$] [a-zA-Z_$0-9]*
	;

WILDCARD_IDENTIFIER
	:	[a-zA-Z_$*] [a-zA-Z_$*0-9]*
	;

AT					: '@';
SEMICOLON			: ';';
COLON				: ':';
DOT					: '.';
COMMA				: ',';
CURLY_BRACKET_OPEN	: '{';
CURLY_BRACKET_CLOSE	: '}';

WS	:	[ \r\t\n]+ -> skip;

