package com.anahuac.rest.api.Entity.custom

class MenuParent extends Menu{
	public static String GET="SELECT DISTINCT * FROM (SELECT bam.id, bam.parentid, CASE WHEN parent.displayname IS NULL THEN true ELSE false END                                     isparent, '../apps/'||ba.token||'/'||bap.token AS url, ba.token                                parenttoken, bam.displayname                         menu, bam.index_, bap.token, ba.displayname, parent.displayname parent FROM business_app ba LEFT JOIN public.business_app_menu bam ON bam.applicationid=ba.id LEFT JOIN public.business_app_page bap ON bap.id=bam.applicationpageid LEFT JOIN public.business_app_menu parent ON bam.parentid=parent.id INNER JOIN app_menu_role apr ON apr.business_app_menuid=bam.displayname WHERE ba.displayname='Administrativo INVP'AND (apr.roleid IN (SELECT um.roleid FROM user_membership um WHERE userid=?)) ORDER BY parent.displayname DESC, bam.index_ ) datos ORDER BY isparent desc, parent DESC, index_ ASC;"
	private List<Menu> child;

	public List<Menu> getChild() {
		return child;
	}

	public void setChild(List<Menu> child) {
		this.child = child;
	}
	@Override
	public boolean equals(Object arg0) {
		Boolean part1=arg0 != null;
		Boolean part3=((MenuParent) arg0).getId().equals(this.parentid) || ((MenuParent) arg0).getParentid().equals(this.id);
		return (part1 && part3);
	}
}
