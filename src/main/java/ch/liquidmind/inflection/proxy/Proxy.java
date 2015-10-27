package ch.liquidmind.inflection.proxy;

import java.lang.reflect.Method;

import __java.lang.__Class;
import ch.liquidmind.inflection.loader.TaxonomyLoader;
import ch.liquidmind.inflection.model.external.Taxonomy;
import ch.liquidmind.inflection.model.external.View;

public class Proxy
{
	// Note that the view's taxonomy (View.getTaxonomy()) may be distinct from
	// this taxonomy.
	private Taxonomy taxonomy;
	private View view;

	// Constructor for collection proxies.
	protected Proxy( String taxonomyName )
	{
		this( taxonomyName, null );
	}
	
	// Constructor for normal proxies.
	protected Proxy( String taxonomyName, String viewName )
	{
		super();
        taxonomy = TaxonomyLoader.getContextTaxonomyLoader().loadTaxonomy( taxonomyName );
        this.view = taxonomy.getView( viewName );
	}

	private Taxonomy getTaxonomy()
	{
		return taxonomy;
	}

	private View getView()
	{
		return view;
	}

	// TODO: should probably put these accessors in a different place altogether,
	// to avoid any confusion, esp. with frameworks.
	public static Taxonomy getTaxonomy( Proxy proxy )
	{
		return proxy.getTaxonomy();
	}

	public static View getView( Proxy proxy )
	{
		return proxy.getView();
	}
	
	protected Method getMethod( String name, Class< ? >[] paramTypes )
	{
		return __Class.getDeclaredMethod( this.getClass(), name, paramTypes );
	}
	
	@SuppressWarnings( "unchecked" )
	protected < T extends Object > T invoke( String methodName, Class< ? >[] paramTypes, Object[] params ) throws Throwable
	{
		Method method = __Class.getDeclaredMethod( this.getClass(), methodName, paramTypes );
		Object retVal = ProxyHandler.getContextProxyHandler().invoke( this, method, params );
		
		return (T)retVal;
	}
	
	// TODO: refactor this and the above method and/or the entire class to fit
	// better with the two distinct cases: (non-collection) object proxy and collection proxy
	@SuppressWarnings( "unchecked" )
	protected < T extends Object > T invokeOnCollection( String methodName, Class< ? >[] paramTypes, Object[] params ) throws Throwable
	{
		Method method = __Class.getDeclaredMethod( this.getClass(), methodName, paramTypes );
		Object retVal = CollectionProxyHandler.getContextCollectionProxyHandler().invoke( this, method, params );
		
		return (T)retVal;
	}
}
