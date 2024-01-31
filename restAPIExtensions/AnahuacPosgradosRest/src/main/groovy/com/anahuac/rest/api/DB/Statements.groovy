package com.anahuac.rest.api.DB

class Statements {
	//Azure
	public static final String CONFIGURACIONESSSA="SELECT * FROM CATCONFIGURACION WHERE CLAVE = 'SASAzure'";
	
	//Banner 
	public static final String GET_BANNER_API_INFO = "SELECT urltokenbannersdae, usuariobannersdae, urlbannersdae, pwBannerSDAE FROM CATAPIKEY WHERE CAMPUS_PID = ?";
	
	//implementación correo
	public static final String SELECT_PROPERTIES_BONITA = "SELECT A.tenantid, A.id, A.process_id, A.name, A.value FROM proc_parameter AS A INNER JOIN process_definition AS B ON B.processid = A.process_id AND B.activationstate='ENABLED' WHERE A.name IN ('usuario','password') ORDER BY B.version DESC Limit 2";
//	public static final String GET_CAT_NOTIFICACIONES_CAMPUS_PROCESO_CODIGO="SELECT * from PSGRCatNotificaciones where case_id=(SELECT max(case_id) FROM psgrprocesocaso where campus = ? and proceso='PSGRCatNotificaciones') and codigo=?"
	public static final String GET_CAT_NOTIFICACIONES_CAMPUS_PROCESO_CODIGO="SELECT * from psgrcatnotificaciones where case_id=(SELECT max(case_id) FROM psgrprocesocaso where campus = ? and proceso='CatNotificaciones') and codigo=?"
	public static final String GET_DETALLESOLICITUD="select IdBanner,ObservacionesRechazo,ObservacionesListaRoja,ObservacionesCambio,ordenpago  from detallesolicitud where caseid=(select concat(caseid,'') from SOLICITUDDEADMISION where correoelectronico=?  ORDER BY persistenceid desc limit 1)"
	public static final String GET_DETALLESOLICITUD_PSGR="select mensaje_admin_escolar, mensaje_area_academic, mensaje_comite_admision from PSGRRegistro where correo_electronico=? ORDER BY persistenceid desc limit 1;";
	public static final String GET_CAMPUS_ID_FROM_CLAVE="SELECT persistenceid as campus_id FROM PSGRCATCAMPUS where grupo_bonita=? limit 1 "
	public static final String GET_ASPIRANTE_TRANSFERENCIA="SELECT campusanterior, campusnuevo FROM catbitacoratransferencias where correoaspirante=? order by persistenceid desc  limit 1"
	public static final String GET_ASPIRANTE_ASISTENCIA="WITH FILTER (username) as (values(?)) SELECT paselista.persistenceid, paselista.asistencia, pruebas.cattipoprueba_pid, ctp.descripcion FROM paselista paselista INNER JOIN pruebas pruebas ON pruebas.persistenceid=paselista.prueba_pid INNER JOIN cattipoprueba ctp ON ctp.persistenceid=pruebas.cattipoprueba_pid WHERE username=(SELECT username from filter) and paselista.asistencia=true"
	public static final String GET_DOCUMENTOSTEXTOS_BY_CAMPUSPID="SELECT * FROM psgrcatdocumentostextos where campus_pid=(SELECT persistenceid FROM psgrcatcampus where grupo_bonita=? and is_eliminado=false limit 1)"
	public static final String GET_INFOCARTATEMPORAL_PLANTILLA="WITH FILTER (correo) as (values(LOWER(?)))select * from infocartatemporal where curp=(SELECT curp from SOLICITUDDEADMISION where LOWER(correoelectronico)=(SELECT correo from filter) limit 1) OR  numerodematricula=(SELECT idbanner from detallesolicitud d left join SOLICITUDDEADMISION s on s.caseid=d.caseid::bigint where LOWER(s.correoelectronico)=(SELECT correo from filter) limit 1)"
	public static final String GET_INFOCARTA_PLANTILLA="WITH FILTER (correo) as (values(LOWER(?)))select * from infocarta where curp=(SELECT curp from SOLICITUDDEADMISION where LOWER(correoelectronico)=(SELECT correo from filter) limit 1) OR  numerodematricula=(SELECT idbanner from detallesolicitud d left join SOLICITUDDEADMISION s on s.caseid=d.caseid::bigint where LOWER(s.correoelectronico)=(SELECT correo from filter) limit 1)"
	public static final String INSERT_CATBITACORACORREOS="INSERT INTO PSGRCatBitacoraCorreos  (persistenceid, persistenceversion,codigo,de,estatus,fecha_creacion,mensaje,para,campus) values (case when (SELECT max(persistenceId)+1 from PSGRCatBitacoraCorreos ) is null then 1 else (SELECT max(persistenceId)+1 from PSGRCatBitacoraCorreos ) end,0,?,?,?,now(),?,?,?)"
	public static final String GET_CONFIGURACIONES_CAMPUS = "SELECT clave, valor FROM PSGRConfiguraciones WHERE id_campus = ?";
	//CARTAS
	public static final String GET_CARTAS_NOTIFICACIONES_ALT="SELECT c.* FROM psgrcatnotificaciones c INNER JOIN psgrprocesocaso pc on pc.case_id=c.case_id and pc.campus=? WHERE c.tipo_correo <> 'Notificación de SDAE'";
	public static final String GET_CARTAS_NOTIFICACIONES_ESTATUS="SELECT c.* FROM PSGRcatnotificaciones c INNER JOIN psgrprocesocaso pc on pc.case_id=c.case_id and pc.campus = ? WHERE c.tipo_correo IN ([ESTATUS])";
	public static final String UPDATE_CAT_NOTIFICACIONES_SDAE ="update psgrcatnotificaciones set angulo_imagen_footer = ?, angulo_imagen_header = ?, asunto = ?, comentario_leon = ?, contenido  = ?, contenido_correo = ?, contenido_leonel = ?, descripcion = ?, doc_guia_estudio = ?, enlace_banner = ?, enlace_contacto = ?, enlace_facebook = ?, enlace_footer = ?, enlace_instagram = ?, enlace_twitter = ?, nombre_imagen_footer = ?, texto_footer  = ?, tipo_correo = ?, titulo = ?, url_img_footer = ?, url_img_header = ? WHERE persistenceid = ?;";
	public static final String INSERT_CAT_NOTIFICACIONES_SDAE ="INSERT INTO psgrcatnotificaciones  (persistenceid, persistenceversion, angulo_imagen_footer , angulo_imagen_header , asunto , comentario_leon , contenido  , contenido_correo , contenido_leonel , descripcion , doc_guia_estudio , enlace_banner , enlace_contacto , enlace_facebook , enlace_footer , enlace_instagram , enlace_twitter , nombre_imagen_footer , texto_footer  , tipo_correo , titulo , url_img_footer , url_img_header,   codigo,   case_id, is_eliminado) values (case when (SELECT max(persistenceId)+1 from psgrcatnotificaciones ) is null then 1 else (SELECT max(persistenceId)+1 from psgrcatnotificaciones) end, 0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false)";
	
	//SESIONES
	public static final String GET_SESIONES_POSIBLES = "SELECT ce.persistenceid, ce.persistenceid::VARCHAR AS persistenceId_string, ce.descripcion_entrevista, ce.duracion_entrevista_minutos, ce.nombre, ce.fecha_entrevista, STRING_AGG(DISTINCT cr.responsable_id::VARCHAR, ', ') AS responsable_id FROM PSGRCitaEntrevista ce JOIN PSGRCitaResponsable cr ON ce.persistenceid = cr.cita_entrevista_pid WHERE ce.campus_pid = ? AND DATE(ce.fecha_entrevista::TIMESTAMP) >= DATE(now()) GROUP BY ce.persistenceid, ce.persistenceid::VARCHAR, ce.descripcion_entrevista, ce.duracion_entrevista_minutos, ce.nombre, ce.fecha_entrevista ORDER BY ce.fecha_entrevista ASC ";
	public static final String GET_HORARIOS_BY_SESION = "SELECT hor.persistenceid, hor.hora_fin, hor.hora_inicio, ent.nombre, ent.descripcion_entrevista FROM PSGRCitaHorario AS hor INNER JOIN PSGRCitaEntrevista AS ent ON ent.persistenceid = hor.cita_entrevista_pid WHERE hor.cita_entrevista_pid = ? ORDER BY hor.hora_inicio::TIME ASC";
	public static final String GET_HORARIOS_BY_SESION_FRONT = "SELECT hor.persistenceid, hor.hora_fin, hor.hora_inicio, ent.nombre, ent.descripcion_entrevista, ent.fecha_entrevista, ent.duracion_entrevista_minutos, (SELECT COUNT(*) FROM PSGRCitaResponsable WHERE horario_pid = hor.persistenceid AND disponible_resp = true AND ocupado = false) > 0 AS responsables_disponibles, STRING_AGG(DISTINCT res.persistenceid::VARCHAR, ', ') AS persistenceId_string FROM PSGRCitaHorario AS hor INNER JOIN PSGRCitaEntrevista AS ent ON ent.persistenceid = hor.cita_entrevista_pid LEFT JOIN PSGRCitaResponsable AS res ON hor.persistenceid = res.horario_pid WHERE hor.cita_entrevista_pid = ? GROUP BY hor.persistenceid, hor.hora_fin, hor.hora_inicio, ent.nombre, ent.descripcion_entrevista, ent.fecha_entrevista, ent.duracion_entrevista_minutos ORDER BY hor.hora_inicio::TIME ASC;";
	public static final String GET_RESPONSABLES_BY_ID_HORARIO = "SELECT persistenceid, ocupado, responsable_id, horario_pid, disponible_resp  FROM PSGRCitaResponsable AS resp WHERE resp.horario_pid =  ?";
	public static final String INSERT_SESION = "INSERT INTO PSGRCitaEntrevista (persistenceid, persistenceversion, duracion_entrevista_minutos, nombre, descripcion_entrevista, fecha_entrevista, campus_pid, is_presencial, liga, ubicacion) VALUES ( (CASE WHEN (SELECT MAX(persistenceId)+1 FROM PSGRCitaEntrevista ) IS NULL THEN 1 ELSE (SELECT MAX(persistenceId) + 1 FROM PSGRCitaEntrevista) end), 0, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING persistenceid;";
	public static final String UPDATE_SESION = "UPDATE PSGRCitaEntrevista SET  nombre = ?, descripcion_entrevista = ? WHERE persistenceid = ?";
	public static final String INSERT_HORARIOS = "INSERT INTO PSGRCitaHorario (persistenceid, persistenceversion, hora_inicio, hora_fin, cita_entrevista_pid) VALUES ( (CASE WHEN (SELECT MAX(persistenceId)+1 FROM PSGRCitaHorario ) IS NULL THEN 1 ELSE (SELECT MAX(persistenceId) + 1 FROM PSGRCitaHorario) end), '0', ?, ?, ?) RETURNING persistenceid";
	public static final String UPDATE_HORARIOS = "UPDATE PSGRCitaHorario SET hora_inicio= ?, hora_fin= ? WHERE persistenceid = ?";
	public static final String GET_SESIONES_TODAS = "SELECT persistenceid, persistenceid::VARCHAR AS persistenceId_string, descripcion_entrevista, duracion_entrevista_minutos, nombre, fecha_entrevista, campus_pid, is_presencial, liga, ubicacion FROM PSGRCitaEntrevista WHERE campus_pid = ? ORDER BY fecha_entrevista ASC";
	public static final String INSERT_RESPONSABLES_LISTA = "INSERT INTO PSGRCitaResponsableLista (persistenceid, persistenceversion, nombre, responsable_id, eliminado, cita_entrevista_pid) VALUES ( (CASE WHEN (SELECT MAX(persistenceId)+1 FROM PSGRCitaResponsableLista ) IS NULL THEN 1 ELSE (SELECT MAX(persistenceId) + 1 FROM PSGRCitaResponsableLista) end), '0', ?, ? ,false, ?);";
	public static final String INSERT_RESPONSABLE_CITA = "INSERT INTO PSGRCitaResponsable (persistenceid, persistenceversion, horario_pid, responsable_id, cita_entrevista_pid, ocupado, disponible_resp, eliminado) VALUES ( (CASE WHEN (SELECT MAX(persistenceId)+1 FROM PSGRCitaResponsable ) IS NULL THEN 1 ELSE (SELECT MAX(persistenceId) + 1 FROM PSGRCitaResponsable) end), '0', ?, ?, ?, ?, ?, false) RETURNING persistenceid;";
	public static final String UPDATE_RESPONSABLE_CITA = "UPDATE PSGRCitaResponsable SET ocupado = ?, disponible_resp = ? WHERE persistenceid  = ?";
	public static final String INSERT_ENTREVISTADOR_CARRERA = "INSERT INTO PSGRCitaEntrevistadorCarrera (persistenceid, persistenceversion, entrevistador_id, carrera_pid, campus_pid, sesion_id) VALUES ((CASE WHEN (SELECT MAX(persistenceId)+1 FROM PSGRCitaEntrevistadorCarrera ) IS NULL THEN 1 ELSE (SELECT MAX(persistenceId) + 1 FROM PSGRCitaEntrevistadorCarrera) end), '0', ?, ?, ?, ?)";
	public static final String DELETE_ENTREVISTADOR_CARRERA = "DELETE FROM PSGRCitaEntrevistadorCarrera WHERE persistenceid = ?";
	public static final String DELETE_ENTREVISTADOR_CARRERA_BY_CARRERA = "DELETE FROM PSGRCitaEntrevistadorCarrera WHERE sesion_id = ?";
	public static final String GET_ENTREVISTADOR_CARRERA = "SELECT entrevistador_id, gest.* FROM PSGRCitaEntrevistadorCarrera AS entr INNER JOIN PSGRCatGestionEscolar AS gest ON gest.persistenceid = entr.carrera_pid WHERE sesion_id = ? ";
	public static final String GET_ENTREVISTADOR_CARRERA_INFO = "SELECT entrevistador_id, gest.* FROM PSGRCitaEntrevistadorCarrera AS entr INNER JOIN PSGRCatGestionEscolar AS gest ON gest.persistenceid = entr.carrera_pid WHERE sesion_id = ? AND entrevistador_id = ?";
	public static final String UPDATE_PASE_LISTA_ASISTENCIA  = "UPDATE PSGRCitaAspirante SET asistio = ? WHERE caseid = ? AND (is_eliminado IS NULL OR is_eliminado = false)";
	
	//BANDEJAS 
	//public static final String GET_LISTADO_SOLICITUDES = "SELECT regi.caseid, estatus.descripcion AS estatus_solicitud, regi.fecha_registro, regi.fecha_ultima_modificacion, regi.correo_electronico, prog.estudiara_programa_otra_un_pid, camp.descripcion AS campus, camp.grupo_bonita, posg.descripcion AS posgrado, peri.descripcion AS periodo, gest.nombre AS carrera, pers.apellido_paterno, pers.apellido_materno, pers.curp, pers.fecha_nacimiento, pers.lugar_nacimiento_estado, pers.lugar_nacimiento_pais, pers.nombre, pers.pasaporte, pers.alumno_anahuac, pers.id_banner, pers.lugar_nacimiento_ciudad, pers.urlfoto, naci.descripcion AS nacionalidad, sexo.descripcion AS sexo, posinfo.institucion, posinfo.programa, posinfo.promedio, regi.mensaje_admin_escolar, regi.tiene_requisitos_adicionales FROM PSGRRegistro AS regi LEFT JOIN PSGRSolAdmiPrograma AS prog ON prog.caseid = regi.caseid LEFT JOIN PSGRCatCampus AS camp ON prog.campus_pid = camp.persistenceid LEFT JOIN PSGRCatPosgrado AS posg ON posg.persistenceid = prog.posgrado_pid LEFT JOIN PSGRCatPeriodo AS peri ON peri.persistenceid = prog.periodo_ingreso_pid LEFT JOIN PSGRCatGestionEscolar AS gest ON gest.persistenceid = prog.programa_interes_pid LEFT JOIN PSGRSolAdmiDatosPersonales AS pers ON pers.caseid = regi.caseid LEFT JOIN PSGRCatNacionalidad AS naci ON naci.persistenceid = pers.nacionalidad_pid LEFT JOIN PSGRCatSexo AS sexo ON sexo.persistenceid = pers.sexo_pid LEFT JOIN PSGRSolAdmiInfoAcademica AS posinfo ON posinfo.caseid = regi.caseid LEFT JOIN PSGRCatEstatusProceso AS estatus ON regi.estatus_solicitud = estatus.clave [WHERE] [ORDER_BY] [LIMIT_OFFSET];";
	// Sin duplicados
	public static final String GET_LISTADO_SOLICITUDES = "SELECT regi.caseid, regi.is_segunda_oportunidad, estatus.descripcion AS estatus_solicitud, regi.id_banner_validacion, regi.fecha_registro, regi.fecha_ultima_modificacion, regi.correo_electronico, prog.estudiara_programa_otra_un_pid, camp.descripcion AS campus, camp.grupo_bonita, posg.descripcion AS posgrado, peri.descripcion AS periodo, gest.nombre AS carrera, pers.apellido_paterno, pers.apellido_materno, pers.curp, pers.fecha_nacimiento, pers.lugar_nacimiento_estado, pers.lugar_nacimiento_pais, pers.nombre, pers.pasaporte, pers.alumno_anahuac, pers.id_banner, pers.lugar_nacimiento_ciudad, pers.urlfoto, naci.descripcion AS nacionalidad, sexo.descripcion AS sexo, posinfo.institucion, posinfo.programa, posinfo.promedio, regi.mensaje_admin_escolar, regi.tiene_requisitos_adicionales, cont.telefono_celular, cont.correo_contacto FROM PSGRRegistro AS regi LEFT JOIN PSGRSolAdmiPrograma AS prog ON prog.caseid = regi.caseid LEFT JOIN PSGRCatCampus AS camp ON prog.campus_pid = camp.persistenceid LEFT JOIN PSGRCatPosgrado AS posg ON posg.persistenceid = prog.posgrado_pid LEFT JOIN PSGRCatPeriodo AS peri ON peri.persistenceid = prog.periodo_ingreso_pid LEFT JOIN PSGRCatGestionEscolar AS gest ON gest.persistenceid = prog.programa_interes_pid LEFT JOIN PSGRSolAdmiDatosPersonales AS pers ON pers.caseid = regi.caseid LEFT JOIN PSGRCatNacionalidad AS naci ON naci.persistenceid = pers.nacionalidad_pid LEFT JOIN PSGRCatSexo AS sexo ON sexo.persistenceid = pers.sexo_pid LEFT JOIN (SELECT *, ROW_NUMBER() OVER (PARTITION BY caseid ORDER BY fecha_termino DESC) AS row_number FROM PSGRSolAdmiInfoAcademica) AS posinfo ON posinfo.caseid = regi.caseid AND posinfo.row_number = 1 LEFT JOIN PSGRCatEstatusProceso AS estatus ON regi.estatus_solicitud = estatus.clave LEFT JOIN PSGRSolAdmiDatosContacto AS cont ON regi.caseid = cont.caseid [WHERE] [ORDER_BY] [LIMIT_OFFSET];";
	public static final String GET_CONTEO_SOLICITUDES = "SELECT COUNT(*) AS total_registros FROM PSGRRegistro AS regi LEFT JOIN PSGRSolAdmiPrograma AS prog ON prog.caseid = regi.caseid LEFT JOIN PSGRCatCampus AS camp ON prog.campus_pid = camp.persistenceid LEFT JOIN PSGRCatPosgrado AS posg ON posg.persistenceid = prog.posgrado_pid LEFT JOIN PSGRCatPeriodo AS peri ON peri.persistenceid = prog.periodo_ingreso_pid LEFT JOIN PSGRCatGestionEscolar AS gest ON gest.persistenceid = prog.programa_interes_pid LEFT JOIN PSGRSolAdmiDatosPersonales AS pers ON pers.caseid = regi.caseid LEFT JOIN PSGRCatNacionalidad AS naci ON naci.persistenceid = pers.nacionalidad_pid LEFT JOIN PSGRCatSexo AS sexo ON sexo.persistenceid = pers.sexo_pid LEFT JOIN (SELECT *, ROW_NUMBER() OVER (PARTITION BY caseid ORDER BY fecha_termino DESC) AS row_number FROM PSGRSolAdmiInfoAcademica) AS posinfo ON posinfo.caseid = regi.caseid AND posinfo.row_number = 1 LEFT JOIN PSGRCatEstatusProceso AS estatus ON regi.estatus_solicitud = estatus.clave [WHERE]";
	public static final String GET_LISTADO_SOLICITUDES_PASE_LISTA = "SELECT regi.caseid, estatus.descripcion AS estatus_solicitud, regi.fecha_registro, regi.fecha_ultima_modificacion, regi.correo_electronico, prog.estudiara_programa_otra_un_pid, camp.descripcion AS campus, camp.grupo_bonita, posg.descripcion AS posgrado, peri.descripcion AS periodo, gest.nombre AS carrera, pers.apellido_paterno, pers.apellido_materno, pers.curp, pers.fecha_nacimiento, pers.lugar_nacimiento_estado, pers.lugar_nacimiento_pais, pers.nombre, pers.pasaporte, pers.alumno_anahuac, pers.id_banner, pers.lugar_nacimiento_ciudad, pers.urlfoto, naci.descripcion AS nacionalidad, sexo.descripcion AS sexo, posinfo.institucion, posinfo.programa, posinfo.promedio, regi.mensaje_admin_escolar, regi.tiene_requisitos_adicionales, entr.fecha_entrevista, resp.responsable_id FROM PSGRRegistro AS regi LEFT JOIN PSGRSolAdmiPrograma AS prog ON prog.caseid = regi.caseid LEFT JOIN PSGRCatCampus AS camp ON prog.campus_pid = camp.persistenceid LEFT JOIN PSGRCatPosgrado AS posg ON posg.persistenceid = prog.posgrado_pid LEFT JOIN PSGRCatPeriodo AS peri ON peri.persistenceid = prog.periodo_ingreso_pid LEFT JOIN PSGRCatGestionEscolar AS gest ON gest.persistenceid = prog.programa_interes_pid LEFT JOIN PSGRSolAdmiDatosPersonales AS pers ON pers.caseid = regi.caseid LEFT JOIN PSGRCatNacionalidad AS naci ON naci.persistenceid = pers.nacionalidad_pid LEFT JOIN PSGRCatSexo AS sexo ON sexo.persistenceid = pers.sexo_pid LEFT JOIN PSGRSolAdmiInfoAcademica AS posinfo ON posinfo.caseid = regi.caseid LEFT JOIN PSGRCatEstatusProceso AS estatus ON regi.estatus_solicitud = estatus.clave LEFT JOIN PSGRCitaAspirante AS cita ON cita.caseid = regi.caseid LEFT JOIN PSGRCitaHorario AS hora ON cita.cita_horario_pid = hora.persistenceid LEFT JOIN PSGRCitaEntrevista AS entr ON hora.cita_entrevista_pid = entr.persistenceid LEFT JOIN PSGRCitaResponsable AS resp ON cita.responsable_pid = resp.persistenceId [WHERE] [ORDER_BY] [LIMIT_OFFSET];";
	
	//SOLICITUD
	public static final String UPDATE_SOL_ADMI_DATOS_PERSONALES = "UPDATE PSGRSolAdmiDatosPersonales SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, curp = ? WHERE persistenceId = ?";
	public static final String UPDATE_SOL_ADMI_DATOS_CONTACTO = "UPDATE PSGRSolAdmiDatosContacto SET correo_contacto = ? WHERE persistenceId = ?";
	public static final String UPDATE_SOL_ADMI_PROGRAMA = "UPDATE PSGRSolAdmiPrograma SET campus_pid = ?, posgrado_pid = ?, programa_interes_pid = ?, periodo_ingreso_pid = ? WHERE  persistenceId = ?";
	public static final String GET_SOLICITUDES_DUPLICADOS = "SELECT regi.caseid, camp.clave AS campus_clave, posg.clave AS posgrado_clave, esco.clave AS programa_interes_clave, peri.descripcion AS periodo_ingreso, regi.nombre, regi.apellido_paterno, regi.apellido_materno, pers.curp, regi.id_banner_validacion, regi.fecha_registro, pers.pasaporte FROM PSGRRegistro AS regi INNER JOIN PSGRSolAdmiDatosPersonales AS pers ON regi.caseid = pers.caseid INNER JOIN PSGRSolAdmiPrograma AS prgm ON regi.caseid = prgm.caseid LEFT JOIN PSGRCatCampus AS camp ON prgm.campus_pid = camp.persistenceid LEFT JOIN PSGRCatPosgrado AS posg ON prgm.posgrado_pid = posg.persistenceid LEFT JOIN PSGRCatGestionEscolar AS esco ON prgm.programa_interes_pid = esco.persistenceid LEFT JOIN PSGRCatPeriodo AS peri ON prgm.periodo_ingreso_pid = peri.persistenceid WHERE (LOWER(regi.nombre) LIKE('%[NOMBRE]%') AND LOWER(regi.apellido_paterno) LIKE LOWER('%[APELLIDO_P]%') ) OR (LOWER(regi.nombre) LIKE('%[NOMBRE]%') AND LOWER(regi.apellido_materno) LIKE LOWER('%[APELLIDO_M]%') ) OR (LOWER(regi.apellido_paterno) LIKE LOWER('%[APELLIDO_P]%') AND LOWER(regi.apellido_materno) LIKE LOWER('%[APELLIDO_M]%') ) OR (LOWER(pers.curp) LIKE LOWER('%[CURP]%')) OR (LOWER(pers.pasaporte) LIKE LOWER('%[PASAPORTE]%')) OR (LOWER(regi.id_banner_validacion) LIKE LOWER('%[IDBANNER]%')) ORDER BY regi.caseid ASC";
	public static final String UPDATE_ESTATUS_BY_CASEID = "UPDATE PSGRRegistro SET estatus_solicitud = ? WHERE caseid = ? AND estatus_solicitud NOT IN('modificaciones_solicitadas', 'modificaciones_realizadas', 'solicitud_reactivada', 'solicitud_iniciada');";
	public static final String GET_ESTATUS_BY_CASEID = "SELECT estatus_solicitud FROM PSGRRegistro  WHERE caseid = ? ;";
	
	//REQUISITOS ADICIONALES AUXILIAR
	public static final String GET_REQUISITOS_ADICIONALES_AUXILIAR = "SELECT aux.persistenceid, aux.caseid, cat.clave, cat.nombre, cat.descripcion, cat.requiere_documento, cat.tipo_de_archivo AS tipo_de_archivo_aceptado, aux.cumplido, aux.url_azure, aux.requisito_adicional_pid FROM AUXISolAdmiRequisitoAdicional AS aux LEFT JOIN PSGRCatRequisitosAdicionales AS cat ON aux.requisito_adicional_pid = cat.persistenceid WHERE aux.caseid = ?;";
	public static final String INSERT_REQUISITO_ADICIONAL_AUXILIAR = "INSERT INTO AUXISolAdmiRequisitoAdicional (persistenceid, caseid, cumplido, requisito_adicional_pid) VALUES (case when (SELECT max(persistenceId)+1 from AUXISolAdmiRequisitoAdicional) is null then 1 else (SELECT max(persistenceId)+1 from AUXISolAdmiRequisitoAdicional) end, ?, ?, ?) RETURNING persistenceid;";
	public static final String DELETE_REQUISITO_ADICIONAL_AUXILIAR = "DELETE FROM AUXISolAdmiRequisitoAdicional WHERE persistenceid = ?";
	public static final String UPDATE_REQUISITO_ADICIONAL_AUXILIAR = "UPDATE AUXISolAdmiRequisitoAdicional SET cumplido = ?, url_azure = ? WHERE persistenceid = ?";
	public static final String SELECT_DISTINCT_CASEID_REQUISITO_ADICIONAL_AUXILIAR = "SELECT DISTINCT req_auxi.caseid FROM AUXISolAdmiRequisitoAdicional AS req_auxi WHERE req_auxi.caseid IS NOT NULL";

	//PASE DE LISTA
	public static final String GET_SESIONESPSICOLOGO = "select * from (SELECT DISTINCT(Pruebas.persistenceid)  as pruebas_id,   Pruebas.nombre, Pruebas.aplicacion, ( CASE WHEN Sesion.tipo LIKE '%R,F,E%'OR  Sesion.tipo LIKE '%R,E,F%'OR  Sesion.tipo LIKE '%F,R,E%'OR  Sesion.tipo LIKE '%F,E,R%'OR  Sesion.tipo LIKE '%E,F,R%'OR  Sesion.tipo LIKE '%E,R,F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false ) ELSE CASE WHEN Sesion.tipo LIKE '%R,F%'OR  Sesion.tipo LIKE '%F,R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='R')) ELSE CASE WHEN Sesion.tipo LIKE '%E,F%'OR  Sesion.tipo LIKE '%F,E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='E'))ELSE CASE WHEN Sesion.tipo LIKE '%R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R')) ELSE CASE WHEN Sesion.tipo LIKE '%E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'E')) ELSE CASE WHEN Sesion.tipo LIKE '%F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F')) ELSE(select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R' OR clave ='E'))END END END END END END ) as residencia, Sesion.persistenceid as sesiones_id, Pruebas.lugar, Pruebas.registrados as registrados, Sesion.nombre as nombre_sesion, ctipoprueba.descripcion as tipo_prueba, Pruebas.cupo, Pruebas.entrada,Pruebas.salida FROM PRUEBAS as Pruebas  LEFT JOIN SESIONES as Sesion on Pruebas.sesion_pid = Sesion.persistenceid  LEFT JOIN cattipoprueba ctipoprueba on ctipoprueba.PERSISTENCEID =Pruebas.cattipoprueba_pid LEFT JOIN catcampus campus on sesion.campus_pid = campus.persistenceid and campus.iseliminado = false [CAMPUS] WHERE campus.descripcion is not null and Pruebas.ISELIMINADO = FALSE AND ctipoprueba.PERSISTENCEID != 4 AND CAST(Pruebas.aplicacion AS DATE) >= CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE)  [WHERE] [ORDERBY] ) as datos [RESIDENCIA] [LIMITOFFSET]"
	public static final String EXT_SESIONESPSICOLOGO = "(SELECT COUNT (AP.persistenceid) from ASPIRANTESPRUEBAS AS AP where AP.PRUEBA_PID = Pruebas.persistenceid) as registrados,(SELECT P3.APLICACION FROM SESIONES S3 LEFT JOIN PRUEBAS P3 ON P3.SESION_PID = S3.PERSISTENCEID WHERE S3.PERSISTENCEID = Sesion.persistenceid  ORDER BY P3.APLICACION::DATE DESC LIMIT 1) as ultimaaplicacion"
	public static final String COUNT_SESIONESPSICOLOGO = "select COUNT(*) as registros from(select DISTINCT(Pruebas.persistenceid),( CASE WHEN Sesion.tipo LIKE '%R,F,E%'OR  Sesion.tipo LIKE '%R,E,F%'OR  Sesion.tipo LIKE '%F,R,E%'OR  Sesion.tipo LIKE '%F,E,R%'OR  Sesion.tipo LIKE '%E,F,R%'OR  Sesion.tipo LIKE '%E,R,F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false ) ELSE CASE WHEN Sesion.tipo LIKE '%R,F%'OR  Sesion.tipo LIKE '%F,R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='R')) ELSE CASE WHEN Sesion.tipo LIKE '%E,F%'OR  Sesion.tipo LIKE '%F,E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='E'))ELSE CASE WHEN Sesion.tipo LIKE '%R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R')) ELSE CASE WHEN Sesion.tipo LIKE '%E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'E')) ELSE CASE WHEN Sesion.tipo LIKE '%F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F')) ELSE(select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R' OR clave ='E'))END END END END END END ) as residencia,ctipoprueba.descripcion as tipo_prueba FROM PRUEBAS as Pruebas  LEFT JOIN SESIONES as Sesion on Pruebas.sesion_pid = Sesion.persistenceid  LEFT JOIN cattipoprueba ctipoprueba on ctipoprueba.PERSISTENCEID =Pruebas.cattipoprueba_pid LEFT JOIN catcampus campus on sesion.campus_pid = campus.persistenceid and campus.iseliminado = false [CAMPUS] WHERE campus.descripcion is not null and Pruebas.ISELIMINADO = FALSE AND ctipoprueba.PERSISTENCEID != 4 AND CAST(Pruebas.aplicacion AS DATE) >= CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE) [WHERE] ) as datos [RESIDENCIA]";
	
	//Transferencias 
	public static final String GET_DATOS_REGISTRO = "SELECT * FROM PSGRSolAdmiPrograma WHERE caseid = ?;";
	public static final String UPDATE_TRANSFERENCIA_REGISTRO = "UPDATE PSGRRegistro SET campus_pid = ? WHERE caseid = ?;";
	public static final String UPDATE_TRANSFERENCIA_SOLICITUD = "UPDATE PSGRSolAdmiPrograma SET campus_pid = ?, posgrado_pid = ?, programa_interes_pid = ?, periodo_ingreso_pid = ? WHERE caseid = ?;";
	public static final String INSERT_BITACORA_TRANSFERENCIA = "INSERT INTO PSGRLogTransferencias (persistenceid, persistenceversion, caseid, campus_origen_pid, campus_destino_pid, posgrado_origen_pid, posgrado_destino_pid, lic_origen_pid, lic_destino_pid, periodo_origen_pid, periodo_destino_pid, fecha_transferencia, usuario) VALUES (CASE WHEN (SELECT MAX(persistenceId) + 1 FROM PSGRLogTransferencias) IS NULL THEN 1 ELSE (SELECT max(persistenceId) + 1 FROM PSGRLogTransferencias) END, 0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String GET_BITACORA_TRANSFERENCIAS = "SELECT logs.usuario, logs.persistenceid, cori.descripcion AS campus_origen, cdes.descripcion AS campus_detino, pori.descripcion AS posgrado_origen, pdes.descripcion AS posgrado_destino, pori.descripcion AS posgrado_origen, pdes.descripcion AS posgrado_destino , gori.descripcion AS carrera_origen, gdes.descripcion AS carrera_destino,  eori.descripcion AS periodo_origen, edes.descripcion AS periodo_destino, logs.usuario, TO_CHAR(logs.fecha_transferencia::TIMESTAMP, 'DD/MM/YYYY dd:mm') AS fecha_transferencia, pers.urlfoto, pers.id_banner, pers.nombre, pers.apellido_paterno, pers.apellido_materno, regi.correo_electronico, esta.descripcion AS estatus_solicitud  FROM PSGRLogTransferencias AS logs INNER JOIN PSGRCatCampus AS cori ON logs.campus_origen_pid = cori.persistenceid INNER JOIN PSGRCatCampus  AS cdes ON logs.campus_destino_pid = cdes.persistenceid INNER JOIN PSGRCatPosgrado AS pori ON logs.posgrado_origen_pid = pori.persistenceid INNER JOIN PSGRCatPosgrado  AS pdes ON logs.posgrado_destino_pid = pdes.persistenceid INNER JOIN PSGRCatGestionEscolar AS gori ON logs.lic_origen_pid = gori.persistenceid INNER JOIN PSGRCatGestionEscolar  AS gdes ON logs.lic_destino_pid = gdes.persistenceid INNER JOIN PSGRCatPeriodo AS eori ON logs.periodo_origen_pid = eori.persistenceid INNER JOIN PSGRCatPeriodo  AS edes ON logs.periodo_destino_pid = edes.persistenceid LEFT JOIN  PSGRSolAdmiDatosPersonales AS pers ON pers.caseid = logs.caseid LEFT JOIN PSGRRegistro AS regi ON regi.caseid = logs.caseid INNER JOIN PSGRCatEstatusProceso AS esta ON esta.clave = regi.estatus_solicitud [WHERE] [ORDER_BY] [LIMIT_OFFSET]";
	public static final String GET_COUNT_BITACORA_TRANSFERENCIAS = "SELECT COUNT(*) AS total_registros FROM PSGRLogTransferencias AS logs INNER JOIN PSGRCatCampus AS cori ON logs.campus_origen_pid = cori.persistenceid INNER JOIN PSGRCatCampus  AS cdes ON logs.campus_destino_pid = cdes.persistenceid INNER JOIN PSGRCatPosgrado AS pori ON logs.posgrado_origen_pid = pori.persistenceid INNER JOIN PSGRCatPosgrado  AS pdes ON logs.posgrado_destino_pid = pdes.persistenceid INNER JOIN PSGRCatGestionEscolar AS gori ON logs.lic_origen_pid = gori.persistenceid INNER JOIN PSGRCatGestionEscolar  AS gdes ON logs.lic_destino_pid = gdes.persistenceid INNER JOIN PSGRCatPeriodo AS eori ON logs.periodo_origen_pid = eori.persistenceid INNER JOIN PSGRCatPeriodo  AS edes ON logs.periodo_destino_pid = edes.persistenceid LEFT JOIN  PSGRSolAdmiDatosPersonales AS pers ON pers.caseid = logs.caseid LEFT JOIN PSGRRegistro AS regi ON regi.caseid = logs.caseid INNER JOIN PSGRCatEstatusProceso AS esta ON esta.clave = regi.estatus_solicitud [WHERE] ";
	
	
}