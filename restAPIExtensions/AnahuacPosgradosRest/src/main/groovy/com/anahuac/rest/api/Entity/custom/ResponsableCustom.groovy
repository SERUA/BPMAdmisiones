package com.anahuac.rest.api.Entity.custom

import com.anahuac.rest.api.Entity.db.ResponsableDisponible

class ResponsableCustom {
	List<ResponsableDisponible> lstFechasDisponibles;
	
	@Override
	public boolean equals(Object arg0) {
		Boolean part1=arg0 != null;
		Boolean part2=arg0 instanceof ResponsableCustom;
		Boolean part3=((ResponsableCustom) arg0).getId().equals(this.getId());
		return (part1 && part2 && part3);
	}
}
