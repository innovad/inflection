package ch.liquidmind.inflection.proxy;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// TODO: rename to ProxyList (analogous to ArrayList, LinkedList, etc.)
public class ListProxy< E extends Object > extends Proxy implements List< E >
{
	protected ListProxy( String taxonomyName )
	{
		super( taxonomyName );
	}

	@Override
	public int size()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains( Object o )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator< E > iterator()
	{
        try
        {
    		return invokeOnCollection( "iterator", new Class< ? >[] {}, new Object[] {} );
        }
        catch ( RuntimeException | Error ex )
        {
            throw ex;
        }
        catch ( java.lang.Throwable ex )
        {
            throw new IllegalStateException( "Unexpected exception type: " + ex.getClass().getName(), ex );
        }
	}

	@Override
	public Object[] toArray()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public < T > T[] toArray( T[] a )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add( E e )
	{
        try
        {
    		return invokeOnCollection( "add", new Class< ? >[] { Object.class }, new Object[] { e } );
        }
        catch ( RuntimeException | Error ex )
        {
            throw ex;
        }
        catch ( java.lang.Throwable ex )
        {
            throw new IllegalStateException( "Unexpected exception type: " + ex.getClass().getName(), ex );
        }
	}

	@Override
	public boolean remove( Object o )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll( Collection< ? > c )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll( Collection< ? extends E > c )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll( int index, Collection< ? extends E > c )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll( Collection< ? > c )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll( Collection< ? > c )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings( "unused" )
	@Override
	public void clear()
	{
		// TODO Auto-generated method stub
		int i = 0;
	}

	@Override
	public E get( int index )
	{
        try
        {
    		return invokeOnCollection( "get", new Class< ? >[] { int.class }, new Object[] { index } );
        }
        catch ( RuntimeException | Error ex )
        {
            throw ex;
        }
        catch ( java.lang.Throwable ex )
        {
            throw new IllegalStateException( "Unexpected exception type: " + ex.getClass().getName(), ex );
        }
	}

	@Override
	public E set( int index, E element )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings( "unused" )
	@Override
	public void add( int index, E element )
	{
		// TODO Auto-generated method stub
		int i = 0;
	}

	@Override
	public E remove( int index )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf( Object o )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lastIndexOf( Object o )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator< E > listIterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator< E > listIterator( int index )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List< E > subList( int fromIndex, int toIndex )
	{
		// TODO Auto-generated method stub
		return null;
	}
}
