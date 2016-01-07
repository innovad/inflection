package ch.liquidmind.inflection.model.linked;

import java.lang.reflect.Method;

import ch.liquidmind.inflection.model.external.Property;

public class PropertyLinked extends MemberLinked implements Property
{
	private Method readMethod;
	private Method writeMethod;

	public PropertyLinked( String name )
	{
		super( name );
	}

	@Override
	public Method getReadMethod()
	{
		return readMethod;
	}

	public void setReadMethod( Method readMethod )
	{
		this.readMethod = readMethod;
	}

	@Override
	public Method getWriteMethod()
	{
		return writeMethod;
	}

	public void setWriteMethod( Method writeMethod )
	{
		this.writeMethod = writeMethod;
	}
}
