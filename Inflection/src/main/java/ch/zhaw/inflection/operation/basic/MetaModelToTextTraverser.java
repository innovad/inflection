package ch.zhaw.inflection.operation.basic;

import ch.zhaw.inflection.model.HGroup;
import ch.zhaw.inflection.model.VmapInstance;


public class MetaModelToTextTraverser extends IndentingPrintWriterTraverser
{
	public static final String CONFIGURATION = MetaModelToTextTraverser.class.getName() + CONFIGURATION_SUFFIX;

	public MetaModelToTextTraverser( HGroup hGroup )
	{
		this( hGroup, getConfiguration( CONFIGURATION ) );
	}
	
	public MetaModelToTextTraverser( HGroup hGroup, VmapInstance vmapInstance )
	{
		super( hGroup, vmapInstance );
	}
}
