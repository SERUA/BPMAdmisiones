package com.anahuac.rest.api.DB

class StatementsCatalogos {
	//PSGRCatFiltroSeguridad
	public static final String INSERT_CATFILTROSEGURIDAD = "INSERT INTO PSGRFiltroSeguridad (persistenceid, persistenceversion, rol, servicio) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRFiltroSeguridad), ?, ?, ?);";
	public static final String DELETE_CATFILTROSEGURIDAD = "DELETE FROM PSGRFiltroSeguridad WHERE persistenceid = ?";
	public static final String UPDATE_CATFILTROSEGURIDAD = "UPDATE PSGRFiltroSeguridad SET rol = ?, servicio = ? WHERE persistenceid = ?";
	public static final String SELECT_CATFILTROSEGURIDAD = "SELECT persistenceid, rol, servicio FROM PSGRFiltroSeguridad [WHERE] [ORDERBY]";
	
	//PSGRCatPais
	public static final String INSERT_CATPAIS = "INSERT INTO PSGRCatPais (persistenceid, case_id, id, persistenceversion, is_eliminado, clave, descripcion, usuario_creacion, fecha_creacion, orden) VALUES (COALESCE((SELECT MAX(persistenceid)::TEXT FROM PSGRCatPais), '0')::INTEGER + 1, COALESCE((SELECT MAX(CASE_ID)::TEXT FROM PSGRCatPais), '0')::INTEGER + 1, COALESCE((SELECT MAX(case_id)::TEXT FROM PSGRCatPais), '0')::INTEGER + 1, ?, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATPAIS  = "UPDATE PSGRCatPais SET is_eliminado = ? WHERE persistenceid = ?;";
	public static final String UPDATE_CATPAIS  = "UPDATE PSGRCatPais SET clave = ?, descripcion = ?, orden = ?, usuario_creacion = ? WHERE persistenceid = ?;";
	public static final String SELECT_CATPAIS  = "SELECT persistenceid, clave, descripcion, usuario_creacion, fecha_creacion, is_eliminado FROM PSGRCatPais [WHERE] [ORDERBY]";
 
	//PSGRCatEstado
	public static final String INSERT_CATESTADO = "INSERT INTO PSGRCatEstados (persistenceid, case_id, is_eliminado, pais_pid, clave, descripcion, usuario_creacion, fecha_creacion, orden, persistenceversion) VALUES (COALESCE((SELECT MAX(persistenceid)::TEXT FROM PSGRCATESTADOS), '0')::INTEGER + 1, COALESCE((SELECT MAX(CASE_ID)::TEXT FROM PSGRCatEstados), '0')::INTEGER + 1, ?, ?, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATESTADO  = "UPDATE PSGRCatEstados SET is_eliminado = ? WHERE persistenceid = ?;";
	public static final String UPDATE_CATESTADO  = "UPDATE PSGRCatEstados SET clave = ?, descripcion = ?, usuario_creacion = ? WHERE persistenceid = ?;";
	public static final String SELECT_CATESTADO  = "SELECT persistenceid, orden, clave, descripcion, usuario_creacion, fecha_creacion, is_eliminado FROM PSGRCatEstados [WHERE] [ORDERBY]";
	
	//PSGRCatSexo
	public static final String INSERT_CATSEXO = "INSERT INTO PSGRCatSexo (persistenceid, is_eliminado, persistenceversion, clave, descripcion, usuario_creacion, fecha_creacion) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatSexo), ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATSEXO  = "DELETE FROM PSGRCatSexo WHERE persistenceid = ?";
	public static final String UPDATE_CATSEXO  = "UPDATE PSGRCatSexo SET clave = ?, descripcion = ?, usuario_creacion = ? WHERE persistenceid = ?;";
	public static final String SELECT_CATSEXO  = "SELECT persistenceid, clave, descripcion, usuario_creacion, fecha_creacion, is_eliminado FROM PSGRCatSexo [WHERE] [ORDERBY]";
	
	//PSGRCatCampus
	public static final String INSERT_CATCAMPUS = "INSERT INTO PSGRCatCampus (persistenceid, calle, clave, codigo_postal, colonia, descripcion, email, fecha_creacion, fecha_implementacion, grupo_bonita, municipio, numero_ext, numero_int, orden, url_aviso_privacidad, url_imagen, usuario_banner, estados_relacion_pid, paises_relacion_pid, id, activado, eliminado)  VALUES (COALESCE((SELECT MAX(persistenceid)::TEXT FROM PSGRCATcampus), '0')::INTEGER + 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATCAMPUS  = "UPDATE PSGRCatCampus SET eliminado = ? WHERE persistenceid = ?";
	public static final String UPDATE_CATCAMPUS  = "UPDATE PSGRCatCampus SET calle=?, clave=?, codigo_postal=?, colonia=?, descripcion=?, email=?, fecha_creacion=?, fecha_implementacion=?, grupo_bonita=?, municipio=?, numero_ext=?, numero_int=?, orden=?, url_aviso_privacidad=?, url_imagen=?, usuario_banner=?, estados_relacion_pid = ?, paises_relacion_pid = ?, id=? WHERE persistenceid=?;";
	public static final String SELECT_CATCAMPUS  = "SELECT persistenceid, clave, descripcion, usuario_creacion, fecha_creacion, is_eliminado FROM PSGRCatCampus [WHERE] [ORDERBY]";
	public static final String GET_CATCAMPUS= "SELECT  c.*, p.descripcion as pais, e.clave as cEstado, e.descripcion as dEstado FROM PSGRCatCampus c LEFT JOIN PSGRCatPais p ON c.pais_pid  = p.persistenceid  LEFT JOIN PSGRCatEstados e ON  e.persistenceid  = c.estado_pid  [WHERE] [ORDERBY] [LIMITOFFSET]"

	//PSGRCatGestionEscolar
//	public static final String GET_CATGESTIONESCOLAR = "SELECT GE.*, campus.descripcion as nombreCampus FROM PSGRCATGESTIONESCOLAR as GE  LEFT JOIN psgrcatcampus campus ON campus.grupo_bonita = GE.campus [CAMPUS]  [WHERE] [ORDERBY] [LIMITOFFSET]";
	
	public static final String GET_CATGESTIONESCOLAR = "SELECT GE.*, campus.descripcion as nombreCampus FROM psgrcatgestionescolar as GE  LEFT JOIN psgrcatcampus campus ON campus.descripcion = GE.campus [CAMPUS]  [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String INSERT_CATGESTIONESCOLAR = "INSERT INTO PSGRCatGestionEscolar (persistenceid, case_id, persistenceversion, fecha_creacion, is_eliminado, campus, clave, nombre, descripcion, enlace, tipo_centro_estudio, propedeutico, programa_parcial, is_medicina, tipo_licenciatura, inscripcion_enero, inscripcion_mayo, inscripcion_agosto, inscripcion_septiembre, url_img_licenciatura, idioma, usuario_creacion, campus_referencia_pid) VALUES (COALESCE((SELECT MAX(persistenceid)::TEXT FROM PSGRCatGestionEscolar), '0')::INTEGER + 1, COALESCE((SELECT MAX(case_id)::TEXT FROM PSGRCatGestionEscolar), '0')::INTEGER + 1, COALESCE((SELECT MAX(persistenceversion)::TEXT FROM PSGRCatGestionEscolar), '0')::INTEGER + 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATGESTIONESCOLAR  = "UPDATE PSGRCatGestionEscolar SET is_eliminado = ? WHERE persistenceid = ?";
	public static final String UPDATE_CATGESTIONESCOLAR  = "UPDATE PSGRCatGestionEscolar SET Clave=?, nombre=?, descripcion=?, enlace=?, tipo_centro_estudio=?, propedeutico=?, programa_parcial=?, is_medicina=?, tipo_licenciatura=?, inscripcion_enero=?, inscripcion_mayo=?, inscripcion_agosto=?, inscripcion_septiembre=?, url_img_licenciatura=?, idioma=?, usuario_creacion=? WHERE persistenceid=?;";
	public static final String GET_LSTCAMPUS  = "SELECT * FROM PSGRCatCampus WHERE eliminado = false";
	public static final String GET_LSTCAMPUSBYGRUPOBONITA  = "SELECT * FROM PSGRCatCampus WHERE grupo_bonita = ? AND eliminado = false";
	
	//PSGRCatPosgrado2
	public static final String GET_CATPOSGRADO2 = "SELECT GE.*, campus.descripcion as nombreCampus FROM PSGRCatPosgrado as GE  LEFT JOIN psgrcatcampus campus ON campus.persistenceid = GE.campus_pid [CAMPUS]  [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String INSERT_CATPOSGRADO2 = "INSERT INTO PSGRCatPosgrado (persistenceid, persistenceversion, fecha_registro, is_eliminado, clave, descripcion, usuario_creacion, campus_pid) VALUES (COALESCE((SELECT MAX(persistenceid)::TEXT FROM PSGRCatPosgrado), '0')::INTEGER + 1, ?, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATPOSGRADO2  = "UPDATE PSGRCatPosgrado SET is_eliminado = ? WHERE persistenceid = ?";
	public static final String UPDATE_CATPOSGRADO2  = "UPDATE PSGRCatPosgrado SET Clave=?, descripcion=? WHERE persistenceid=?;";
	
	
	//PSGRCatPropedeutico
	public static final String INSERT_CATPROPEDEUTICO = "INSERT INTO PSGRCatPropedeutico (persistenceid, is_eliminado, persistenceversion, clave, descripcion, usuario_creacion, fecha_creacion) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatPropedeutico), ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATPROPEDEUTICO  = "UPDATE PSGRCatPropedeutico SET is_eliminado = ? WHERE persistenceid = ?";
	public static final String UPDATE_CATPROPEDEUTICO  = "UPDATE PSGRCatPropedeutico SET clave = ?, descripcion = ? WHERE persistenceid = ?;";
	public static final String SELECT_CATPROPEDEUTICO  = "SELECT persistenceid, clave, descripcion, usuario_creacion, fecha_creacion, is_eliminado FROM PSGRCatPropedeutico [WHERE] [ORDERBY]";
	
	//PSGRCatPosgrado
	public static final String INSERT_CATPOSGRADO = "INSERT INTO PSGRCatPosgrado (persistenceid, is_eliminado, persistenceversion, clave, descripcion, fecha_registro) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatPosgrado), ?, ?, ?, ?, ?);";
	public static final String DELETE_CATPOSGRADO  = "UPDATE PSGRCatPosgrado SET is_eliminado = ? WHERE persistenceid = ?";
	public static final String UPDATE_CATPOSGRADO  = "UPDATE PSGRCatPosgrado SET clave = ?, descripcion = ? WHERE persistenceid = ?;";
	public static final String SELECT_CATPOSGRADO  = "SELECT persistenceid, clave, descripcion, fecha_registro, is_eliminado FROM PSGRCatPosgrado [WHERE] [ORDERBY]";
	
	//PSGRCatEstadoCivil
	public static final String INSERT_CATESTADOCIVIL = "INSERT INTO PSGRCatEstadoCivil (persistenceid, is_eliminado, persistenceversion, id, is_enabled, clave, descripcion, usuario_banner, fecha_creacion, fecha_importacion) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatEstadoCivil), ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATESTADOCIVIL  = "UPDATE PSGRCatEstadoCivil SET is_eliminado = ? WHERE persistenceid = ?";
	public static final String UPDATE_CATESTADOCIVIL  = "UPDATE PSGRCatEstadoCivil SET ID = ?, CLAVE = ?, descripcion = ? WHERE persistenceid = ?;";
	public static final String SELECT_CATESTADOCIVIL  = "SELECT persistenceid, clave, descripcion, id, usuario_banner, fecha_creacion, fecha_importacion, is_eliminado FROM PSGRCatEstadoCivil [WHERE] [ORDERBY]";
	
	//PSGRCatReligion
	public static final String INSERT_CATRELIGION = "INSERT INTO PSGRCatReligion (persistenceid, is_eliminado_value, persistenceversion, id, is_enabled_value, clave, descripcion, usuario_banner, fecha_creacion, fecha_importacion) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatReligion), ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATRELIGION  = "UPDATE PSGRCatReligion SET is_eliminado_value = ? WHERE persistenceid = ?";
	public static final String DELETE_CATRELIGIONFISICO  = "DELETE FROM PSGRCatReligion WHERE persistenceid = ?";
	public static final String UPDATE_CATRELIGION  = "UPDATE PSGRCatReligion SET ID = ?, CLAVE = ?, descripcion = ? WHERE persistenceid = ?;";
	public static final String SELECT_CATRELIGION  = "SELECT persistenceid, clave, descripcion, id, usuario_banner, fecha_creacion, fecha_importacion, is_eliminado_value FROM PSGRCatReligion [WHERE] [ORDERBY]";
	
	
	//PSGRCatNacionalidad
	public static final String INSERT_CATNACIONALIDAD = "INSERT INTO PSGRCatNacionalidad (persistenceid, is_eliminado, persistenceversion, id, is_enabled, clave, descripcion, usuario_banner, fecha_creacion, orden) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatNacionalidad), ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATNACIONALIDAD  = "UPDATE PSGRCatNacionalidad SET is_eliminado = ? WHERE persistenceid = ?";
	public static final String UPDATE_CATNACIONALIDAD  = "UPDATE PSGRCatNacionalidad SET ID = ?, CLAVE = ?, descripcion = ?, orden = ?, is_enabled = ? WHERE persistenceid = ?;";
	public static final String SELECT_CATNACIONALIDAD  = "SELECT persistenceid, orden, clave, descripcion, id, usuario_banner, fecha_creacion, is_eliminado FROM PSGRCatNacionalidad [WHERE] [ORDERBY]";
	
	//ConsultasPersonalizadas
	public static final String GET_PERIODOBYIDCAMPUS  = "SELECT * FROM PSGRCatPeriodo WHERE id_campus = ?";
	public static final String GET_POSGRADOBYIDCAMPUS  = "SELECT * FROM PSGRCatPosgrado WHERE campus_pid = ?";
	public static final String GET_GESTIONESCOLARBYIDCAMPUS  = "SELECT * FROM PSGRCatGestionEscolar WHERE campus_referencia_pid = ?";
	
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
	public static final String DELETE_CATSINO = "UPDATE PSGRCatSiNo SET eliminado = ? WHERE persistenceid = ?";
	public static final String UPDATE_CATSINO = "UPDATE PSGRCatSiNo SET clave = ?, descripcion = ?, es_si_o_no = ?, es_talvez = ?, es_otro = ? WHERE persistenceid = ?";
	public static final String SELECT_CATSINO = "SELECT persistenceid, clave, descripcion,  es_si_o_no, es_talvez, es_otro, eliminado FROM PSGRCatSiNo [WHERE] [ORDERBY]";
	
	//PSGRConfiguraciones
	public static final String INSERT_CONFIGURACIONES = "INSERT INTO PSGRConfiguraciones (persistenceid, persistenceversion, clave, valor, id_campus) VALUES (( CASE WHEN (SELECT max(persistenceId) + 1 from PSGRConfiguraciones ) is null then 1 else (SELECT max(persistenceId) + 1 from PSGRConfiguraciones) end), 0, ?, ?, ?)";
	public static final String DELETE_CONFIGURACIONES = "DELETE FROM PSGRConfiguraciones WHERE persistenceid = ?";
	public static final String UPDATE_CONFIGURACIONES = "UPDATE PSGRConfiguraciones SET clave = ?, valor = ? WHERE persistenceid = ?";
	public static final String SELECT_CONFIGURACIONES = "SELECT persistenceid, clave, valor,  id_campus FROM PSGRConfiguraciones WHERE id_campus = ?";
	public static final String GET_CONFIGURACIONES = "SELECT GE.*, campus.descripcion as nombreCampus FROM PSGRConfiguraciones as GE  LEFT JOIN psgrcatcampus campus ON campus.persistenceid = GE.id_campus [CAMPUS]  [WHERE] [ORDERBY] [LIMITOFFSET]";
	
	//PSGRCatMedioEnteraste
	public static final String INSERT_CATMEDIOSENTERASTE = "INSERT INTO PSGRCatMedioEnteraste (persistenceid, persistenceversion, clave, descripcion, orden) VALUES (( CASE WHEN (SELECT max(persistenceId) + 1 from PSGRCatMedioEnteraste ) is null then 1 else (SELECT max(persistenceId) + 1 from PSGRCatMedioEnteraste) end), 0, ?, ?, ?)";
	public static final String DELETE_CATMEDIOSENTERASTE = "DELETE FROM PSGRCatMedioEnteraste WHERE persistenceid = ?";
	public static final String UPDATE_CATMEDIOSENTERASTE = "UPDATE PSGRCatMedioEnteraste SET clave = ?, descripcion = ?, orden = ? WHERE persistenceid = ?";
	public static final String SELECT_CATMEDIOSENTERASTE = "SELECT persistenceid, orden, clave, descripcion FROM PSGRCatMedioEnteraste [WHERE] [ORDERBY]";
	
	//PSGRCatPeriodo
	public static final String INSERT_CATPERIODO = "INSERT INTO PSGRCATPERIODO  (persistenceid, persistenceversion, activo, clave, descripcion, fecha_creacion, fecha_inicio, fecha_fin, fecha_importacion, id, is_anual, is_eliminado, is_enabled, is_propedeutico, is_semestral, id_campus, usuario_banner, ano, codigo) VALUES (( CASE WHEN (SELECT max(persistenceId) + 1 from PSGRCATPERIODO ) is null then 1 else (SELECT max(persistenceId) + 1 from PSGRCATPERIODO) end), 0, true, ?, ?,?, ?, ?, ?, ?, ?, false, true, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATPERIODO = "UPDATE PSGRCATPERIODO SET is_eliminado = ? WHERE persistenceid = ?";
	public static final String UPDATE_CATPERIODO = "UPDATE PSGRCATPERIODO SET clave = ?, descripcion = ?, fecha_inicio = ?, fecha_fin = ?, id = ?, is_anual = ?, is_propedeutico = ?, is_semestral = ? WHERE persistenceid = ?";
	public static final String SELECT_CATPERIODO = "SELECT * FROM PSGRCATPERIODO [WHERE] [ORDERBY]";
	public static final String SELECT_COUNT_CATPERIODO = "SELECT COUNT(*) AS total_rows FROM PSGRCATPERIODO [WHERE] [ORDERBY]";
	
	public static final String GET_PROCESS_DEFINITION = "SELECT * FROM PROCESS_DEFINITION WHERE name = ? ORDER BY id DESC LIMIT 1";
}