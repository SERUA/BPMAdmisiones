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
	public static final String GET_CAMPUS_ID_FROM_CLAVE="SELECT persistenceid as campus_id FROM PSGRCATCAMPUS where grupo_bonita=? limit 1 "
	public static final String GET_ASPIRANTE_TRANSFERENCIA="SELECT campusanterior, campusnuevo FROM catbitacoratransferencias where correoaspirante=? order by persistenceid desc  limit 1"
	public static final String GET_ASPIRANTE_ASISTENCIA="WITH FILTER (username) as (values(?)) SELECT paselista.persistenceid, paselista.asistencia, pruebas.cattipoprueba_pid, ctp.descripcion FROM paselista paselista INNER JOIN pruebas pruebas ON pruebas.persistenceid=paselista.prueba_pid INNER JOIN cattipoprueba ctp ON ctp.persistenceid=pruebas.cattipoprueba_pid WHERE username=(SELECT username from filter) and paselista.asistencia=true"
	public static final String GET_DOCUMENTOSTEXTOS_BY_CAMPUSPID="SELECT * FROM psgrcatdocumentostextos where campus_pid=(SELECT persistenceid FROM psgrcatcampus where grupo_bonita=? and is_eliminado=false limit 1)"
	public static final String GET_INFOCARTATEMPORAL_PLANTILLA="WITH FILTER (correo) as (values(LOWER(?)))select * from infocartatemporal where curp=(SELECT curp from SOLICITUDDEADMISION where LOWER(correoelectronico)=(SELECT correo from filter) limit 1) OR  numerodematricula=(SELECT idbanner from detallesolicitud d left join SOLICITUDDEADMISION s on s.caseid=d.caseid::bigint where LOWER(s.correoelectronico)=(SELECT correo from filter) limit 1)"
	public static final String GET_INFOCARTA_PLANTILLA="WITH FILTER (correo) as (values(LOWER(?)))select * from infocarta where curp=(SELECT curp from SOLICITUDDEADMISION where LOWER(correoelectronico)=(SELECT correo from filter) limit 1) OR  numerodematricula=(SELECT idbanner from detallesolicitud d left join SOLICITUDDEADMISION s on s.caseid=d.caseid::bigint where LOWER(s.correoelectronico)=(SELECT correo from filter) limit 1)"
	public static final String INSERT_CATBITACORACORREOS="INSERT INTO PSGRCatBitacoraCorreos  (persistenceid, persistenceversion,codigo,de,estatus,fecha_creacion,mensaje,para,campus) values (case when (SELECT max(persistenceId)+1 from PSGRCatBitacoraCorreos ) is null then 1 else (SELECT max(persistenceId)+1 from PSGRCatBitacoraCorreos ) end,0,?,?,?,now(),?,?,?)"
	
	//CARTAS
	public static final String GET_CARTAS_NOTIFICACIONES_ALT="SELECT c.* FROM psgrcatnotificaciones c INNER JOIN psgrprocesocaso pc on pc.case_id=c.case_id and pc.campus=? WHERE c.tipo_correo <> 'Notificación de SDAE'";
	public static final String GET_CARTAS_NOTIFICACIONES_ESTATUS="SELECT c.* FROM PSGRcatnotificaciones c INNER JOIN psgrprocesocaso pc on pc.case_id=c.case_id and pc.campus = ? WHERE c.tipo_correo IN ([ESTATUS])";
	public static final String UPDATE_CAT_NOTIFICACIONES_SDAE ="update psgrcatnotificaciones set angulo_imagen_footer = ?, angulo_imagen_header = ?, asunto = ?, comentario_leon = ?, contenido  = ?, contenido_correo = ?, contenido_leonel = ?, descripcion = ?, doc_guia_estudio = ?, enlace_banner = ?, enlace_contacto = ?, enlace_facebook = ?, enlace_footer = ?, enlace_instagram = ?, enlace_twitter = ?, nombre_imagen_footer = ?, texto_footer  = ?, tipo_correo = ?, titulo = ?, url_img_footer = ?, url_img_header = ? WHERE persistenceid = ?;";
	public static final String INSERT_CAT_NOTIFICACIONES_SDAE ="INSERT INTO psgrcatnotificaciones  (persistenceid, persistenceversion, angulo_imagen_footer , angulo_imagen_header , asunto , comentario_leon , contenido  , contenido_correo , contenido_leonel , descripcion , doc_guia_estudio , enlace_banner , enlace_contacto , enlace_facebook , enlace_footer , enlace_instagram , enlace_twitter , nombre_imagen_footer , texto_footer  , tipo_correo , titulo , url_img_footer , url_img_header,   codigo,   case_id, is_eliminado) values (case when (SELECT max(persistenceId)+1 from psgrcatnotificaciones ) is null then 1 else (SELECT max(persistenceId)+1 from psgrcatnotificaciones) end, 0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false)";
	
	//SESIONES
	public static final String GET_SESIONES_POSIBLES = "SELECT persistenceid, persistenceid::VARCHAR AS persistenceId_string, descripcion_entrevista, duracion_entrevista_minutos, nombre, fecha_entrevista FROM PSGRCitaEntrevista  WHERE campus_pid = ? AND DATE(fecha_entrevista::TIMESTAMP) >= DATE(now()) ORDER BY fecha_entrevista ASC ";
	public static final String GET_HORARIOS_BY_SESION = "SELECT hor.persistenceid, hor.hora_fin, hor.hora_inicio, ent.nombre, ent.descripcion_entrevista FROM PSGRCitaHorario AS hor INNER JOIN PSGRCitaEntrevista AS ent ON ent.persistenceid = hor.cita_entrevista_pid WHERE hor.cita_entrevista_pid = ? ORDER BY hor.hora_inicio::TIME ASC";
	public static final String GET_HORARIOS_BY_SESION_FRONT = "SELECT hor.persistenceid, hor.hora_fin, hor.hora_inicio, ent.nombre, ent.descripcion_entrevista, (SELECT COUNT(*) FROM PSGRCitaResponsable WHERE horario_pid = hor.persistenceid AND disponible_resp = true AND ocupado = false) > 0 AS responsables_disponibles FROM PSGRCitaHorario AS hor INNER JOIN PSGRCitaEntrevista AS ent ON ent.persistenceid = hor.cita_entrevista_pid WHERE hor.cita_entrevista_pid = ? ORDER BY hor.hora_inicio::TIME ASC";
	public static final String INSERT_SESION = "INSERT INTO PSGRCitaEntrevista (persistenceid, persistenceversion, duracion_entrevista_minutos, nombre, descripcion_entrevista, fecha_entrevista, campus_pid) VALUES ( (CASE WHEN (SELECT MAX(persistenceId)+1 FROM PSGRCitaEntrevista ) IS NULL THEN 1 ELSE (SELECT MAX(persistenceId) + 1 FROM PSGRCitaEntrevista) end), 0, ?, ?, ?, ?, ?) RETURNING persistenceid;";
	public static final String UPDATE_SESION = "UPDATE PSGRCitaEntrevista SET  nombre = ?, descripcion_entrevista = ? WHERE persistenceid = ?";
	public static final String INSERT_HORARIOS = "INSERT INTO PSGRCitaHorario (persistenceid, persistenceversion, hora_inicio, hora_fin, cita_entrevista_pid) VALUES ( (CASE WHEN (SELECT MAX(persistenceId)+1 FROM PSGRCitaHorario ) IS NULL THEN 1 ELSE (SELECT MAX(persistenceId) + 1 FROM PSGRCitaHorario) end), '0', ?, ?, ?) RETURNING persistenceid";
	public static final String GET_SESIONES_TODAS = "SELECT persistenceid, persistenceid::VARCHAR AS persistenceId_string, descripcion_entrevista, duracion_entrevista_minutos, nombre, fecha_entrevista, campus_pid FROM PSGRCitaEntrevista WHERE campus_pid = ? ORDER BY fecha_entrevista ASC";
	public static final String INSERT_RESPONSABLES_LISTA = "INSERT INTO PSGRCitaResponsableLista (persistenceid, persistenceversion, nombre, responsable_id, eliminado, cita_entrevista_pid) VALUES ( (CASE WHEN (SELECT MAX(persistenceId)+1 FROM PSGRCitaResponsableLista ) IS NULL THEN 1 ELSE (SELECT MAX(persistenceId) + 1 FROM PSGRCitaResponsableLista) end), '0', ?, ? ,false, ?);";
	public static final String INSERT_RESPONSABLE_CITA = "INSERT INTO PSGRCitaResponsable (persistenceid, persistenceversion, horario_pid, responsable_id, cita_entrevista_pid, ocupado, disponible_resp, eliminado) VALUES ( (CASE WHEN (SELECT MAX(persistenceId)+1 FROM PSGRCitaResponsable ) IS NULL THEN 1 ELSE (SELECT MAX(persistenceId) + 1 FROM PSGRCitaResponsable) end), '0', ?, ?, ?, ?, ?, false);";
	
	//BANDEJAS 
	public static final String GET_LISTADO_SOLICITUDES = "SELECT regi.caseid, estatus.descripcion AS estatus_solicitud, regi.fecha_registro, regi.fecha_ultima_modificacion, regi.correo_electronico, prog.estudiara_programa_otra_un_pid, camp.descripcion AS campus, camp.grupo_bonita, posg.descripcion AS posgrado, peri.descripcion AS periodo, gest.nombre AS carrera, pers.apellido_paterno, pers.apellido_materno, pers.curp, pers.fecha_nacimiento, pers.lugar_nacimiento_estado, pers.lugar_nacimiento_pais, pers.nombre, pers.pasaporte, pers.alumno_anahuac, pers.id_banner, pers.lugar_nacimiento_ciudad, pers.urlfoto, naci.descripcion AS nacionalidad, sexo.descripcion AS sexo, posinfo.institucion, posinfo.programa, posinfo.promedio, regi.mensaje_admin_escolar, regi.tiene_requisitos_adicionales FROM PSGRRegistro AS regi LEFT JOIN PSGRSolAdmiPrograma AS prog ON prog.caseid = regi.caseid LEFT JOIN PSGRCatCampus AS camp ON prog.campus_pid = camp.persistenceid LEFT JOIN PSGRCatPosgrado AS posg ON posg.persistenceid = prog.posgrado_pid LEFT JOIN PSGRCatPeriodo AS peri ON peri.persistenceid = prog.periodo_ingreso_pid LEFT JOIN PSGRCatGestionEscolar AS gest ON gest.persistenceid = prog.programa_interes_pid LEFT JOIN PSGRSolAdmiDatosPersonales AS pers ON pers.caseid = regi.caseid LEFT JOIN PSGRCatNacionalidad AS naci ON naci.persistenceid = pers.nacionalidad_pid LEFT JOIN PSGRCatSexo AS sexo ON sexo.persistenceid = pers.sexo_pid LEFT JOIN PSGRSolAdmiInfoAcademica AS posinfo ON posinfo.caseid = regi.caseid LEFT JOIN PSGRCatEstatusProceso AS estatus ON regi.estatus_solicitud = estatus.clave [WHERE] [ORDER_BY] [LIMIT_OFFSET];";
	public static final String GET_CONTEO_SOLICITUDES = "SELECT COUNT(*) AS total_registros FROM PSGRRegistro AS regi LEFT JOIN PSGRSolAdmiPrograma AS prog ON prog.caseid = regi.caseid LEFT JOIN PSGRCatCampus AS camp ON prog.campus_pid = camp.persistenceid LEFT JOIN PSGRCatPosgrado AS posg ON posg.persistenceid = prog.posgrado_pid LEFT JOIN PSGRCatPeriodo AS peri ON peri.persistenceid = prog.periodo_ingreso_pid LEFT JOIN PSGRCatGestionEscolar AS gest ON gest.persistenceid = prog.programa_interes_pid LEFT JOIN PSGRSolAdmiDatosPersonales AS pers ON pers.caseid = regi.caseid LEFT JOIN PSGRCatNacionalidad AS naci ON naci.persistenceid = pers.nacionalidad_pid  LEFT JOIN PSGRCatSexo AS sexo ON sexo.persistenceid = pers.sexo_pid LEFT JOIN PSGRSolAdmiInfoAcademica AS posinfo ON posinfo.caseid = regi.caseid LEFT JOIN PSGRCatEstatusProceso AS estatus ON regi.estatus_solicitud = estatus.clave [WHERE]";
	
	//SOLICITUD
	public static final String UPDATE_SOL_ADMI_DATOS_PERSONALES = "UPDATE PSGRSolAdmiDatosPersonales SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, curp = ? WHERE persistenceId = ?";
	public static final String UPDATE_SOL_ADMI_DATOS_CONTACTO = "UPDATE PSGRSolAdmiDatosContacto SET correo_contacto = ? WHERE persistenceId = ?";
	public static final String UPDATE_SOL_ADMI_PROGRAMA = "UPDATE PSGRSolAdmiPrograma SET campus_pid = ?, posgrado_pid = ?, programa_interes_pid = ?, periodo_ingreso_pid = ? WHERE  persistenceId = ?";
	public static final String GET_SOLICITUDES_DUPLICADOS = "SELECT regi.nombre, regi.apellido_paterno, regi.apellido_materno, pers.curp, pers.id_banner, regi.fecha_registro, pers.pasaporte FROM PSGRRegistro AS regi INNER JOIN PSGRSolAdmiDatosPersonales AS pers ON regi.caseid = pers.caseid WHERE (LOWER(regi.nombre) LIKE('%[NOMBRE]%') AND LOWER(regi.apellido_paterno) LIKE LOWER('%[APELLIDO_P]%') ) OR (LOWER(regi.nombre) LIKE('%[NOMBRE]%') AND LOWER(regi.apellido_materno) LIKE LOWER('%[APELLIDO_M]%') ) OR (LOWER(regi.apellido_paterno) LIKE LOWER('%[APELLIDO_P]%') AND LOWER(regi.apellido_materno) LIKE LOWER('%[APELLIDO_M]%') ) OR (LOWER(pers.curp) LIKE LOWER('%[CURP]%')) OR (LOWER(pers.pasaporte) LIKE LOWER('%[PASAPORTE]%')) OR (LOWER(pers.id_banner) LIKE LOWER('%[IDBANNER]%'))  ORDER BY regi.caseid ASC";
	
	//REQUISITOS ADICIONALES AUXILIAR
	public static final String GET_REQUISITOS_ADICIONALES_AUXILIAR = "SELECT aux.persistenceid, aux.caseid, cat.clave, cat.nombre, cat.descripcion, cat.requiere_documento, cat.tipo_de_archivo AS tipo_de_archivo_aceptado, aux.cumplido, aux.url_azure, aux.requisito_adicional_pid FROM AUXISolAdmiRequisitoAdicional AS aux LEFT JOIN PSGRCatRequisitosAdicionales AS cat ON aux.requisito_adicional_pid = cat.persistenceid WHERE aux.caseid = ?;";
	public static final String INSERT_REQUISITO_ADICIONAL_AUXILIAR = "INSERT INTO AUXISolAdmiRequisitoAdicional (persistenceid, caseid, cumplido, requisito_adicional_pid) VALUES (case when (SELECT max(persistenceId)+1 from AUXISolAdmiRequisitoAdicional) is null then 1 else (SELECT max(persistenceId)+1 from AUXISolAdmiRequisitoAdicional) end, ?, ?, ?) RETURNING persistenceid;";
	public static final String DELETE_REQUISITO_ADICIONAL_AUXILIAR = "DELETE FROM AUXISolAdmiRequisitoAdicional WHERE persistenceid = ?";
	public static final String UPDATE_REQUISITO_ADICIONAL_AUXILIAR = "UPDATE AUXISolAdmiRequisitoAdicional SET cumplido = ?, url_azure = ? WHERE persistenceid = ?";
	public static final String SELECT_DISTINCT_CASEID_REQUISITO_ADICIONAL_AUXILIAR = "SELECT DISTINCT req_auxi.caseid FROM AUXISolAdmiRequisitoAdicional AS req_auxi WHERE req_auxi.caseid IS NOT NULL";

}
