package com.anahuac.rest.api.DB

class Statements {
	
	//Banner 
	public static final String GET_BANNER_API_INFO = "SELECT urltokenbannersdae, usuariobannersdae, urlbannersdae, pwBannerSDAE FROM CATAPIKEY WHERE CAMPUS_PID = ?";
	
	//implementación correo
	public static final String SELECT_PROPERTIES_BONITA = "SELECT A.tenantid, A.id, A.process_id, A.name, A.value FROM proc_parameter AS A INNER JOIN process_definition AS B ON B.processid = A.process_id AND B.activationstate='ENABLED' WHERE A.name IN ('usuario','password') ORDER BY B.version DESC Limit 2";
	public static final String GET_CAT_NOTIFICACIONES_CAMPUS_PROCESO_CODIGO="SELECT * from psgrcatnotificaciones where case_id=(SELECT max(caseid) FROM procesocaso where campus = ? and proceso='PSGRCatNotificaciones') and codigo=?"
	public static final String GET_DETALLESOLICITUD="select IdBanner,ObservacionesRechazo,ObservacionesListaRoja,ObservacionesCambio,ordenpago  from detallesolicitud where caseid=(select concat(caseid,'') from SOLICITUDDEADMISION where correoelectronico=?  ORDER BY persistenceid desc limit 1)"
	public static final String GET_CAMPUS_ID_FROM_CLAVE="SELECT persistenceid as campus_id FROM CATCAMPUS where grupoBonita=? limit 1 "
	public static final String GET_ASPIRANTE_TRANSFERENCIA="SELECT campusanterior, campusnuevo FROM catbitacoratransferencias where correoaspirante=? order by persistenceid desc  limit 1"
	public static final String GET_ASPIRANTE_ASISTENCIA="WITH FILTER (username) as (values(?)) SELECT paselista.persistenceid, paselista.asistencia, pruebas.cattipoprueba_pid, ctp.descripcion FROM paselista paselista INNER JOIN pruebas pruebas ON pruebas.persistenceid=paselista.prueba_pid INNER JOIN cattipoprueba ctp ON ctp.persistenceid=pruebas.cattipoprueba_pid WHERE username=(SELECT username from filter) and paselista.asistencia=true"
	public static final String GET_DOCUMENTOSTEXTOS_BY_CAMPUSPID="SELECT * FROM psgrcatdocumentostextos where campus_pid=(SELECT persistenceid FROM psgrcatcampus where grupo_bonita=? and is_eliminado=false limit 1)"
	public static final String GET_INFOCARTATEMPORAL_PLANTILLA="WITH FILTER (correo) as (values(LOWER(?)))select * from infocartatemporal where curp=(SELECT curp from SOLICITUDDEADMISION where LOWER(correoelectronico)=(SELECT correo from filter) limit 1) OR  numerodematricula=(SELECT idbanner from detallesolicitud d left join SOLICITUDDEADMISION s on s.caseid=d.caseid::bigint where LOWER(s.correoelectronico)=(SELECT correo from filter) limit 1)"
	public static final String GET_INFOCARTA_PLANTILLA="WITH FILTER (correo) as (values(LOWER(?)))select * from infocarta where curp=(SELECT curp from SOLICITUDDEADMISION where LOWER(correoelectronico)=(SELECT correo from filter) limit 1) OR  numerodematricula=(SELECT idbanner from detallesolicitud d left join SOLICITUDDEADMISION s on s.caseid=d.caseid::bigint where LOWER(s.correoelectronico)=(SELECT correo from filter) limit 1)"
	
}
