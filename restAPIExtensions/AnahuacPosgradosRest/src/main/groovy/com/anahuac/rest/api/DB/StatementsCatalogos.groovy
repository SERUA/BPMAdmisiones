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
	public static final String SELECT_CATPAIS  = "SELECT * FROM PSGRCatPais [WHERE] [ORDERBY] [LIMITOFFSET]";
 
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
	
	public static final String GET_CATGESTIONESCOLAR = "SELECT GE.*, posgrado.descripcion AS posgrado, periodo.descripcion AS periodo, campus.descripcion as nombreCampus FROM psgrcatgestionescolar as GE  LEFT JOIN psgrcatcampus campus ON campus.descripcion = GE.campus LEFT JOIN PSGRCatPosgrado posgrado ON posgrado.persistenceid = GE.posgrado_pid LEFT JOIN PSGRCatPeriodo periodo ON periodo.persistenceid = GE.posgrado_pid [CAMPUS]  [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String INSERT_CATGESTIONESCOLAR = "INSERT INTO PSGRCatGestionEscolar (persistenceid, case_id, persistenceversion, fecha_creacion, is_eliminado, campus, clave, nombre, descripcion, enlace, tipo_centro_estudio, propedeutico, programa_parcial, is_medicina, tipo_licenciatura, inscripcion_enero, inscripcion_mayo, inscripcion_agosto, inscripcion_septiembre, url_img_licenciatura, idioma, usuario_creacion, campus_referencia_pid, orden, periodo_pid, posgrado_pid) VALUES (COALESCE((SELECT MAX(persistenceid)::TEXT FROM PSGRCatGestionEscolar), '0')::INTEGER + 1, COALESCE((SELECT MAX(case_id)::TEXT FROM PSGRCatGestionEscolar), '0')::INTEGER + 1, COALESCE((SELECT MAX(persistenceversion)::TEXT FROM PSGRCatGestionEscolar), '0')::INTEGER + 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
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
	public static final String SELECT_CATESTADOCIVIL  = "SELECT * FROM PSGRCatEstadoCivil [WHERE] [ORDERBY]";
	
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
	
	//PSGRCatTipoEmpleado
	public static final String INSERT_CATTIPOEMPLEADO = "INSERT INTO PSGRCatTipoEmpleado (persistenceid, persistenceversion, clave, orden, descripcion, is_eliminado) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatTipoEmpleado), 0,?, ?, ?, false);";
	public static final String DELETE_CATTIPOEMPLEADO = "UPDATE PSGRCatTipoEmpleado SET is_eliminado = true WHERE persistenceid = ?";
	public static final String UPDATE_CATTIPOEMPLEADO = "UPDATE PSGRCatTipoEmpleado SET clave = ?, orden = ?, descripcion = ? WHERE persistenceid = ?";
	public static final String SELECT_CATTIPOEMPLEADO = "SELECT * FROM PSGRCatTipoEmpleado [WHERE] [ORDERBY]";
	
	//PSGRCatTipoAlumno
	public static final String INSERT_CATTIPOALUMNO = "INSERT INTO PSGRCatTipoAlumno (persistenceid, persistenceversion, clave, orden, descripcion, is_eliminado, fecha_creacion, usuario_creacion) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatTipoAlumno), 0,?, ?, ?, false, ?, ?);";
	public static final String DELETE_CATTIPOALUMNO = "UPDATE PSGRCatTipoAlumno SET is_eliminado = true WHERE persistenceid = ?";
	public static final String UPDATE_CATTIPOALUMNO = "UPDATE PSGRCatTipoAlumno SET clave = ?, orden = ?, descripcion = ? WHERE persistenceid = ?";
	public static final String SELECT_CATTIPOALUMNO = "SELECT * FROM PSGRCatTipoAlumno [WHERE] [ORDERBY]";
	
	//PSGRCatTipoAdmision
	public static final String INSERT_CATTIPOADMISION = "INSERT INTO PSGRCatTipoAdmision (persistenceid, persistenceversion, clave, orden, descripcion, is_eliminado, fecha_creacion, usuario_creacion) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatTipoAdmision), 0,?, ?, ?, false, ?, ?);";
	public static final String DELETE_CATTIPOADMISION = "UPDATE PSGRCatTipoAdmision SET is_eliminado = true WHERE persistenceid = ?";
	public static final String UPDATE_CATTIPOADMISION = "UPDATE PSGRCatTipoAdmision SET clave = ?, orden = ?, descripcion = ? WHERE persistenceid = ?";
	public static final String SELECT_CATTIPOADMISION = "SELECT * FROM PSGRCatTipoAdmision [WHERE] [ORDERBY]";
	
	//PSGRCatResidencia
	public static final String INSERT_CATRESIDENCIA = "INSERT INTO PSGRCatResidencia (persistenceid, persistenceversion, clave, orden, descripcion, is_eliminado, fecha_creacion, usuario_creacion) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatResidencia), 0,?, ?, ?, false, ?, ?);";
	public static final String DELETE_CATRESIDENCIA = "UPDATE PSGRCatResidencia SET is_eliminado = true WHERE persistenceid = ?";
	public static final String UPDATE_CATRESIDENCIA = "UPDATE PSGRCatResidencia SET clave = ?, orden = ?, descripcion = ? WHERE persistenceid = ?";
	public static final String SELECT_CATRESIDENCIA = "SELECT * FROM PSGRCatResidencia [WHERE] [ORDERBY]";
	
	//PSGRCatGiroEmpresa
	public static final String INSERT_CATGIROEMPRESA = "INSERT INTO PSGRCatGiroEmpresa (persistenceid, persistenceversion, clave, orden, descripcion, is_eliminado) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatGiroEmpresa), 0,?, ?, ?, false);";
	public static final String DELETE_CATGIROEMPRESA = "UPDATE PSGRCatGiroEmpresa SET is_eliminado = true WHERE persistenceid = ?";
	public static final String UPDATE_CATGIROEMPRESA = "UPDATE PSGRCatGiroEmpresa SET clave = ?, orden = ?, descripcion = ? WHERE persistenceid = ?";
	public static final String SELECT_CATGIROEMPRESA = "SELECT * FROM PSGRCatGiroEmpresa [WHERE] [ORDERBY]";
	
	//PSGRCatGradoEstudio
	public static final String INSERT_CATGRADOESTUDIO = "INSERT INTO PSGRCatGradoEstudio (persistenceid, persistenceversion, clave, orden, descripcion, is_eliminado) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatGradoEstudio), 0,?, ?, ?, false);";
	public static final String DELETE_CATGRADOESTUDIO = "UPDATE PSGRCatGradoEstudio SET is_eliminado = true WHERE persistenceid = ?";
	public static final String UPDATE_CATGRADOESTUDIO = "UPDATE PSGRCatGradoEstudio SET clave = ?, orden = ?, descripcion = ? WHERE persistenceid = ?";
	public static final String SELECT_CATGRADOESTUDIO = "SELECT * FROM PSGRCatGradoEstudio [WHERE] [ORDERBY]";
	
	//PSGRCatPuestoTrabajo
	public static final String INSERT_CATPUESTOTRABAJO = "INSERT INTO PSGRCatPuestoTrabajo (persistenceid, persistenceversion, clave, orden, descripcion, is_eliminado) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatPuestoTrabajo), 0,?, ?, ?, false);";
	public static final String DELETE_CATPUESTOTRABAJO = "UPDATE PSGRCatPuestoTrabajo SET is_eliminado = true WHERE persistenceid = ?";
	public static final String UPDATE_CATPUESTOTRABAJO = "UPDATE PSGRCatPuestoTrabajo SET clave = ?, orden = ?, descripcion = ? WHERE persistenceid = ?";
	public static final String SELECT_CATPUESTOTRABAJO = "SELECT * FROM PSGRCatPuestoTrabajo [WHERE] [ORDERBY]";
	
	//PSGRCatParentesco
	public static final String INSERT_CATPARENTESCO = "INSERT INTO PSGRCatParentesco (persistenceid, persistenceversion, clave, descripcion, is_eliminado, fecha_creacion, usuario_creacion) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatParentesco), 0, ?, ?, false, ?, ? );";
	public static final String DELETE_CATPARENTESCO = "UPDATE PSGRCatParentesco SET is_eliminado = true WHERE persistenceid = ?";
	public static final String UPDATE_CATPARENTESCO = "UPDATE PSGRCatParentesco SET clave = ?, descripcion = ? WHERE persistenceid = ?";
	public static final String SELECT_CATPARENTESCO = "SELECT * FROM PSGRCatParentesco [WHERE] [ORDERBY]";
	
	//PSGRCatRequisitosAdicionales
	public static final String INSERT_CATREQUISITOSADICIONALES = "INSERT INTO PSGRCatRequisitosAdicionales (persistenceid, persistenceversion, clave, descripcion, is_eliminado, fecha_creacion, usuario_creacion, campus_pid, posgrado_pid, nombre, tipo_de_archivo, requiere_documento) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatRequisitosAdicionales), 0, ?, ?, false, ?, ?, ?, ?, ?, ?, ?);";
	public static final String DELETE_CATREQUISITOSADICIONALES = "UPDATE PSGRCatRequisitosAdicionales SET is_eliminado = true WHERE persistenceid = ?";
	public static final String UPDATE_CATREQUISITOSADICIONALES = "UPDATE PSGRCatRequisitosAdicionales SET clave = ?, descripcion = ?, nombre = ?, tipo_de_archivo = ?, requiere_documento = ? WHERE persistenceid = ?";
	public static final String SELECT_CATREQUISITOSADICIONALES = "SELECT * FROM PSGRCatRequisitosAdicionales [WHERE] [ORDERBY]";
	
	//PSGRCatNotificaciones
	public static final String INSERT_CATNOTIFICACIONES = "INSERT INTO PSGRCatNotificaciones (persistenceid, angulo_imagen_footer, angulo_imagen_header, asunto, bloque_aspirante, case_id, codigo, comentario_leon, contenido, contenido_correo, contenido_leonel, descripcion, doc_guia_estudio, enlace_banner, enlace_contacto, enlace_facebook, enlace_footer, enlace_instagram, enlace_twitter, informacion_lic, is_eliminado, nombre_imagen_footer, nombre_imagen_header, persistenceversion, texto_footer, tipo_correo, titulo, url_img_footer, url_img_header) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatNotificaciones), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	public static final String INSERT_CATPROCESOCASO = "INSERT INTO PSGRProcesoCaso (persistenceid,case_id, persistenceversion, campus, proceso) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRProcesoCaso),(CAST((SELECT COALESCE(MAX(case_id::integer), 0) + 1 FROM PSGRProcesoCaso) AS text)), 0,?, ?);";
	public static final String INSERT_CATNOTIFICACIONESCAMPUS = "INSERT INTO PSGRCatNotificacionesCampus (persistenceid, persistenceversion, catcampus_pid, catnotificacionesfirma_pid, codigo, copia, footer, header) VALUES ((SELECT COALESCE(MAX(persistenceid), 0) + 1 FROM PSGRCatNotificacionesCampus), 0, ?, ?, ?, ?, ?, ?);";
	
	
	//ConsultasPersonalizadas
	public static final String GET_PERIODOBYIDCAMPUS  = "SELECT * FROM PSGRCatPeriodo WHERE id_campus = ?";
	public static final String GET_POSGRADOBYIDCAMPUS  = "SELECT * FROM PSGRCatPosgrado WHERE campus_pid = ?";
	public static final String GET_GESTIONESCOLARBYIDCAMPUS  = "SELECT * FROM PSGRCatGestionEscolar WHERE campus_referencia_pid = ? AND is_eliminado <> true ";
	
	//CORREOS
	public static final String GET_APYKEYMAILGUN="SELECT * FROM PSGRConfiguraciones WHERE id_campus = ? LIMIT 1"
	public static final String DELETE_CATNOTIFICACIONES = "DELETE FROM PSGRCatNotificaciones WHERE persistenceid = ?";
	
	//PSGRCatPais
	public static final String GET_APYKEY="SELECT * FROM PSGRCatPais [WHERE] [ORDERBY] [LIMITOFFSET]"
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
	
	//PSGRCatDocumentos
	public static final String INSERT_CATDOCUMENTOS = "INSERT INTO PSGRCatManejoDocumentos (persistenceid, persistenceversion, clave, descripcion, nombre_documento, fecha_creacion_date, es_opcional, tipo_de_archivo, is_eliminado, campus_pid, posgrado_pid) VALUES (( CASE WHEN (SELECT max(persistenceId) + 1 from PSGRCatManejoDocumentos ) is null then 1 else (SELECT max(persistenceId) + 1 from PSGRCatManejoDocumentos) end), 0, ?, ?, ?, ?, ?, ?, false, ?, ?)";
	public static final String DELETE_CATDOCUMENTOS = "UPDATE PSGRCatManejoDocumentos SET is_eliminado = true WHERE persistenceid = ?";
	public static final String UPDATE_CATDOCUMENTOS = "UPDATE PSGRCatManejoDocumentos SET clave = ?, descripcion = ?, nombre_documento = ?, es_opcional = ?, tipo_de_archivo = ? WHERE persistenceid = ?";
	public static final String SELECT_CATDOCUMENTOS = "SELECT persistenceid, clave, valor,  id_campus FROM PSGRCatManejoDocumentos WHERE id_campus = ?";
	public static final String GET_CATDOCUMENTOS = "SELECT GE.*, campus.descripcion as nombreCampus FROM PSGRCatManejoDocumentos as GE  LEFT JOIN psgrcatcampus campus ON campus.persistenceid = GE.campus_pid [CAMPUS]  [WHERE] [ORDERBY] ";//[LIMITOFFSET]
	
	//PSGRCatMedioEnteraste
	public static final String INSERT_CATMEDIOSENTERASTE = "INSERT INTO PSGRCatMedioEnteraste (persistenceid, persistenceversion, clave, descripcion, orden, is_eliminado) VALUES (( CASE WHEN (SELECT max(persistenceId) + 1 from PSGRCatMedioEnteraste ) is null then 1 else (SELECT max(persistenceId) + 1 from PSGRCatMedioEnteraste) end), 0, ?, ?, ?, false)";
	public static final String DELETE_CATMEDIOSENTERASTE = "UPDATE PSGRCatMedioEnteraste SET is_eliminado = true WHERE persistenceid = ?";
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