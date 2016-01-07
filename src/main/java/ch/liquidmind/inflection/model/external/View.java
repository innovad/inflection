package ch.liquidmind.inflection.model.external;

import java.util.List;

public interface View extends AliasableElement
{
	// TODO: rename this to getJavaClass()
	public < T > Class< T > getViewedClass();
	public List< Class< ? > > getUsedClasses();
	public Taxonomy getParentTaxonomy();
	public List< Member > getDeclaredMembers();
	public List< Member > getUnresolvedMembers();
	public View getSuperview();
	public String getPackageName();
	public String getSimpleName();
	public Member getDeclaredMember( String nameOrAlias );
}
