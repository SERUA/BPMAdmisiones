package com.anahuac.rest.api.DB

class StatementsCatalogos {
	//PSGRCatFiltroSeguridad
	public static final String INSERT_CATFILTROSEGURIDAD = "INSERT INTO PSGRFiltroSeguridad (persistenceid, persistenceversion, rol, servicio) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRFiltroSeguridad), ?, ?, ?)";
	public static final String DELETE_CATFILTROSEGURIDAD = "DELETE FROM PSGRFiltroSeguridad WHERE persistenceid = ?";
	public static final String UPDATE_CATFILTROSEGURIDAD = "UPDATE PSGRFiltroSeguridad SET rol = ?, servicio = ? WHERE persistenceid = ?";
	public static final String SELECT_CATFILTROSEGURIDAD = "SELECT persistenceid, rol, servicio FROM PSGRFiltroSeguridad [WHERE] [ORDERBY]";
	
	//PSGRCatEstado
	public static final String INSERT_CATESTADO = "INSERT INTO PSGRCatEstados (PERSISTENCEID, PERSISTENCEVERSION, CASE_ID, IS_ELIMINADO, PAIS, CLAVE, DESCRIPCION, USUARIO_CREACION, FECHA_CREACION, ORDEN) VALUES ((SELECT COALESCE(MAX(PERSISTENCEID), 0) + 1 FROM PSGRCATESTADOS), (SELECT COALESCE(MAX(PERSISTENCEVERSION), 0) + 1 FROM PSGRCATESTADOS), (SELECT COALESCE(MAX(CASE_ID), 0) + 1 FROM PSGRCATESTADOS), ?, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATESTADO  = "DELETE FROM PSGRCatEstados WHERE persistenceid = ?";
	public static final String UPDATE_CATESTADO  = "UPDATE PSGRCatEstados SET clave = ?, descripcion = ?, usuario_creacion = ? WHERE persistenceid = ?;";
	public static final String SELECT_CATESTADO  = "SELECT persistenceid, orden, clave, descripcion, usuario_creacion, fecha_creacion, is_eliminado FROM PSGRCatEstados [WHERE] [ORDERBY]";
	
	//PSGRCatPais
	public static final String GET_CATPAIS="SELECT * FROM PSGRCatPais [WHERE] [ORDERBY] [LIMITOFFSET]"
	public static final String GET_VALIDACION_ORDEN_EDIT = "SELECT COUNT(PERSISTENCEID) AS  total FROM [TABLA] WHERE ORDEN::Integer = ? AND IS_ELIMINADO = false AND PERSISTENCEID <> ?";
	public static final String GET_VALIDACION_ORDEN = "SELECT COUNT(PERSISTENCEID) AS  total FROM [TABLA] WHERE ORDEN::Integer = ? AND IS_ELIMINADO = false";
	public static final String GET_VALIDACION_CLAVE_EDIT = "SELECT COUNT(PERSISTENCEID) AS  total FROM [TABLA] WHERE LOWER(CLAVE) = ? AND IS_ELIMINADO = false AND PERSISTENCEID <> ?";
	public static final String GET_VALIDACION_CLAVE = "SELECT COUNT(PERSISTENCEID) AS  total FROM [TABLA] WHERE LOWER(CLAVE) = ? AND IS_ELIMINADO = false";
	
	
	
	//PSGRCatEstatusProceso
	public static final String INSERT_CATESTATUSPROCESO = "INSERT INTO PSGRCatEstatusProceso (persistenceid, persistenceversion, clave, descripcion, orden) VALUES (( CASE WHEN (SELECT max(persistenceId) + 1 from PSGRCatEstatusProceso ) is null then 1 else (SELECT max(persistenceId) + 1 from PSGRCatEstatusProceso) end), 0, ?, ?, ?)";
	public static final String DELETE_CATESTATUSPROCESO = "DELETE FROM PSGRCatEstatusProceso WHERE persistenceid = ?";
	public static final String UPDATE_CATESTATUSPROCESO = "UPDATE PSGRCatEstatusProceso SET clave = ?, descripcion = ?, orden = ? WHERE persistenceid = ?";
	public static final String SELECT_CATESTATUSPROCESO = "SELECT persistenceid, orden, clave, descripcion FROM PSGRCatEstatusProceso [WHERE] [ORDERBY]";
	
	//PSGRCatSiNo
	public static final String INSERT_CATSINO = "INSERT INTO PSGRCatSiNo (persistenceid, persistenceversion, clave, descripcion, es_si_o_no, es_talvez, es_otro, eliminado) VALUES (( CASE WHEN (SELECT max(persistenceId) + 1 from PSGRCatSiNo ) is null then 1 else (SELECT max(persistenceId) + 1 from PSGRCatSiNo) end), 0, ?, ?, ?, ?, ?, false)";
	public static final String DELETE_CATSINO = "DELETE FROM PSGRCatSiNo WHERE persistenceid = ?";
	public static final String UPDATE_CATSINO = "UPDATE PSGRCatSiNo SET clave = ?, descripcion = ?, es_si_o_no = ?, es_talvez = ?, es_otro = ? WHERE persistenceid = ?";
	public static final String SELECT_CATSINO = "SELECT persistenceid, clave, descripcion,  es_si_o_no, es_talvez, es_otro, eliminado FROM PSGRCatSiNo [WHERE] [ORDERBY]";
}