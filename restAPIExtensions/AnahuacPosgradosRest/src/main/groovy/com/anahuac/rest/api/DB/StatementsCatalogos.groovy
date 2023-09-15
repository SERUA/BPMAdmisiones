package com.anahuac.rest.api.DB

class StatementsCatalogos {
	
	//CatEjemplo
	public static final String INSERT_CATTIPOMONEDA = "INSERT INTO CATTIPOMONEDA (PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CATTIPOMONEDA), ?, ?, now(), false, 1, ?);";

	public static final String INSERTCATFILTROSEGURIDAD = "INSERT INTO PSGRFILTROSEGURIDAD (PERSISTENCEID, PERSISTENCEVERSION, ROL, SERVICIO) VALUES ((SELECT COALESCE(MAX(PERSISTENCEID), 0) + 1 FROM PSGRFILTROSEGURIDAD), ?, ?, ?)";
	public static final String DELETECATFILTROSEGURIDAD = "DELETE FROM PSGRFILTROSEGURIDAD WHERE PERSISTENCEID = ?";
	public static final String UPDATECATFILTROSEGURIDAD = "UPDATE PSGRFILTROSEGURIDAD SET ROL = ?, SERVICIO = ? WHERE PERSISTENCEID = ?";
	
	//PSGRCatEstatusProceso
	public static final String INSERT_CATESTATUSPROCESO = "INSERT INTO PSGRCatEstatusProceso (persistenceid, persistenceversion, clave, descripcion, orden) VALUES (( CASE WHEN (SELECT max(persistenceId) + 1 from PSGRCatEstatusProceso ) is null then 1 else (SELECT max(persistenceId) + 1 from PSGRCatEstatusProceso) end), 0, ?, ?, ?)";
	public static final String DELETE_CATESTATUSPROCESO = "DELETE FROM PSGRCatEstatusProceso WHERE persistenceid = ?";
	public static final String UPDATE_CATESTATUSPROCESO = "UPDATE PSGRCatEstatusProceso SET clave = ?, descripcion = ?, orden = ? WHERE persistenceid = ?";
	public static final String SELECT_CATESTATUSPROCESO = "SELECT persistenceid, orden, clave, descripcion FROM PSGRCatEstatusProceso [WHERE] [ORDERBY]";
}