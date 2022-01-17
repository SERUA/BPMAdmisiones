package com.anahuac.rest.api.DB

class StatementsCatalogos {
	//INSERTS
	//EJEMPLO insert autoincrementado: 		INSERT tabla (persistenceid) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM tabla));
	
	public static final String INSERT_CATTIPOMONEDA = "INSERT INTO CATTIPOMONEDA (PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CATTIPOMONEDA), ?, ?, now(), false, 1, ?);";
	
	public static final String INSERT_CATPROVIENENINGRESOS = "INSERT INTO CATPROVIENENINGRESOS (PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CATPROVIENENINGRESOS), ?, ?, now(), false, 1, ?);";
	
	public static final String INSERT_CAT_GENERICO = "INSERT INTO CATGEN (PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CATGEN), ?, ?, now(), false, 1, ?);";
	//UPDATES
	
	public static final String UPDATE_CATTIPOMONEDA = "UPDATE CATTIPOMONEDA SET CLAVE = ?, DESCRIPCION = ?, ISELIMINADO = ?, USUARIOCREACION = ? WHERE PERSISTENCEID = ?;";
	
	public static final String UPDATE_CATPROVIENENINGRESOS = "UPDATE CATPROVIENENINGRESOS SET CLAVE = ?, DESCRIPCION = ?, ISELIMINADO = ?, USUARIOCREACION = ? WHERE PERSISTENCEID = ?;";
	
	public static final String UPDATE_CAT_GENERICO = "UPDATE CATGEN SET CLAVE = ?, DESCRIPCION = ?, ISELIMINADO = ?, USUARIOCREACION = ? WHERE PERSISTENCEID = ?;";
	//GETS
	
	public static final String GET_CATGENERICO = "SELECT PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION FROM [CATALOGO] [WHERE] [ORDERBY] [LIMITOFFSET]";
	
	public static final String GET_COUNT_CATGENERICO = "SELECT COUNT(persistenceid) as registros FROM [CATALOGO] [WHERE] ";
	
	/*Catálogo de manejo documentos*/
	public static final String GET_COUNT_CAT_MANEJO_DOCUMENTOS = "SELECT COUNT(PERSISTENCEID) AS registros FROM CatManejoDocumentos [WHERE] ";
	public static final String GET_CAT_MANEJO_DOCUMENTOS = "SELECT PERSISTENCEID, IDCAMPUS, IDTIPOAPOYO, NOMBREDOCUMENTO, ISOBLIGATORIO, URLDOCUMENTOAZURE, FECHACREACION, USUARIOCREACION, DESCRIPCIONDOCUMENTO, ISELIMINADO FROM CatManejoDocumentos [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String INSERT_CAT_MANEJO_DOCUMENTOS = "INSERT INTO CatManejoDocumentos (PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CatManejoDocumentos), ?, ?, now(), false, 1, ?);";
	public static final String UPDATE_CAT_MANEJO_DOCUMENTOS = "";
	public static final String DELETE_CAT_MANEJO_DOCUMENTOS = "UPDATE CatManejoDocumentos SET isEliminado = true WHERE PERSISTENCEID = ?";
	/*Fin del catálogo de manejo documentos*/
	
	/*Catálogo de tipo de apoyo*/
	public static final String GET_COUNT_CAT_TIPO_APOYO = "SELECT COUNT(PERSISTENCEID) AS TOTAL FROM CATTIPOAPOYO [WHERE] ";
	public static final String GET_CAT_TIPO_APOYO = "SELECT PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION, REQUIEREVIDEO, CONDICIONESVIDEO FROM CATTIPOAPOYO [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String INSERT_CAT_TIPO_APOYO = "INSERT INTO CATTIPOAPOYO  (PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION, REQUIEREVIDEO, CONDICIONESVIDEO) VALUES((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CATTIPOAPOYO), ?, ?, now(), false, 1, ?, ?, ?);";
	public static final String UPDATE_CAT_TIPO_APOYO = "";
	public static final String DELETE_CAT_TIPO_APOYO = "UPDATE CatManejoDocumentos SET isEliminado = true WHERE PERSISTENCEID = ?";
	/*Fin del catálogo de tipo de apoyo*/
}
