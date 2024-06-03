package com.anahuac.rest.api.DAO

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.CatGrupoSocialCustom
import com.anahuac.rest.api.Entity.Custom.CatHermanoCustom
import com.anahuac.rest.api.Entity.Custom.CatIdiomaCustom
import com.anahuac.rest.api.Entity.Custom.CatInformacionEscolarCustom
import com.anahuac.rest.api.Entity.Custom.CatParienteEgresadoAnahuacCustom
import com.anahuac.rest.api.Entity.Custom.CatTerapiaCustom
import com.anahuac.rest.api.Entity.Custom.CatUniversidadesCustom
import com.anahuac.rest.api.Entity.Custom.ContactoEmergenciaEntity
import com.anahuac.rest.api.Entity.Custom.PadresTutorEntity
import com.anahuac.rest.api.Utilities.FileDownload
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat

class SolicitudUsuarioDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno
	}
	public Result getIdbanner(Integer parameterP, Integer parameterC, String idbanner, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_IDBANNER_BY_IDBANNER)
				pstm.setString(1, idbanner)

				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
	
					rows.add(columns);
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
			
			} catch (Exception e) {
			
				resultado.setError_info("error al obtener GET_IDBANNER_BY_IDBANNER")
				resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getPadresTutorVencido(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			PadresTutorEntity row = new PadresTutorEntity()
			List<PadresTutorEntity> rows = new ArrayList<PadresTutorEntity>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_PADRESTUTOR_VENCIDOS;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new PadresTutorEntity()
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setApellidos(rs.getString("apellidos"))
				row.setCalle(rs.getString("calle"))
				row.setCaseid(rs.getLong("caseid"))
				row.setCiudad(rs.getString("ciudad"))
				row.setCodigopostal(rs.getString("codigopostal"))
				row.setColonia(rs.getString("colonia"))
				row.setCorreoelectronico(rs.getString("correoelectronico"))
				row.setDelegacionmunicipio(rs.getString("delegacionmunicipio"))
				row.setDesconozcodatospadres(rs.getBoolean("desconozcodatospadres"))
				row.setEmpresatrabaja(rs.getString("empresatrabaja"))
				row.setEstadoextranjero(rs.getString("estadoextranjero"))
				row.setGiroempresa(rs.getString("giroempresa"))
				row.setIstutor(rs.getBoolean("istutor"))
				row.setNombre(rs.getString("nombre"))
				row.setNumeroexterior(rs.getString("numeroexterior"))
				row.setNumerointerior(rs.getString("numerointerior"))
				row.setOtroparentesco(rs.getString("otroparentesco"))
				row.setPuesto(rs.getString("puesto"))
				row.setTelefono(rs.getString("telefono"))
				row.setVivecontigo(rs.getBoolean("vivecontigo"))
				row.setCampusegreso(rs.getString("campusegreso"))
				row.setEgresoanahuac(rs.getString("egresoanahuac"))
				row.setEscolaridad(rs.getString("escolaridad"))
				row.setEstado(rs.getString("estado"))
				row.setPais(rs.getString("pais"))
				row.setParentesco(rs.getString("parentesco"))
				row.setTitulo(rs.getString("titulo"))
				row.setTrabaja(rs.getString("trabaja"))
				row.setVive(rs.getString("vive"))
				row.setVencido(rs.getBoolean("vencido"))
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getPadreVencido(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			PadresTutorEntity row = new PadresTutorEntity()
			List<PadresTutorEntity> rows = new ArrayList<PadresTutorEntity>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_PADRE_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new PadresTutorEntity()
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setApellidos(rs.getString("apellidos"))
				row.setCalle(rs.getString("calle"))
				row.setCaseid(rs.getLong("caseid"))
				row.setCiudad(rs.getString("ciudad"))
				row.setCodigopostal(rs.getString("codigopostal"))
				row.setColonia(rs.getString("colonia"))
				row.setCorreoelectronico(rs.getString("correoelectronico"))
				row.setDelegacionmunicipio(rs.getString("delegacionmunicipio"))
				row.setDesconozcodatospadres(rs.getBoolean("desconozcodatospadres"))
				row.setEmpresatrabaja(rs.getString("empresatrabaja"))
				row.setEstadoextranjero(rs.getString("estadoextranjero"))
				row.setGiroempresa(rs.getString("giroempresa"))
				row.setIstutor(rs.getBoolean("istutor"))
				row.setNombre(rs.getString("nombre"))
				row.setNumeroexterior(rs.getString("numeroexterior"))
				row.setNumerointerior(rs.getString("numerointerior"))
				row.setOtroparentesco(rs.getString("otroparentesco"))
				row.setPuesto(rs.getString("puesto"))
				row.setTelefono(rs.getString("telefono"))
				row.setVivecontigo(rs.getBoolean("vivecontigo"))
				row.setCampusegreso(rs.getString("campusegreso"))
				row.setEgresoanahuac(rs.getString("egresoanahuac"))
				row.setEscolaridad(rs.getString("escolaridad"))
				row.setEstado(rs.getString("estado"))
				row.setPais(rs.getString("pais"))
				row.setParentesco(rs.getString("parentesco"))
				row.setTitulo(rs.getString("titulo"))
				row.setTrabaja(rs.getString("trabaja"))
				row.setVive(rs.getString("vive"))
				row.setVencido(rs.getBoolean("vencido"))
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getMadreVencido(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			PadresTutorEntity row = new PadresTutorEntity()
			List<PadresTutorEntity> rows = new ArrayList<PadresTutorEntity>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_MADRE_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new PadresTutorEntity()
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setApellidos(rs.getString("apellidos"))
				row.setCalle(rs.getString("calle"))
				row.setCaseid(rs.getLong("caseid"))
				row.setCiudad(rs.getString("ciudad"))
				row.setCodigopostal(rs.getString("codigopostal"))
				row.setColonia(rs.getString("colonia"))
				row.setCorreoelectronico(rs.getString("correoelectronico"))
				row.setDelegacionmunicipio(rs.getString("delegacionmunicipio"))
				row.setDesconozcodatospadres(rs.getBoolean("desconozcodatospadres"))
				row.setEmpresatrabaja(rs.getString("empresatrabaja"))
				row.setEstadoextranjero(rs.getString("estadoextranjero"))
				row.setGiroempresa(rs.getString("giroempresa"))
				row.setIstutor(rs.getBoolean("istutor"))
				row.setNombre(rs.getString("nombre"))
				row.setNumeroexterior(rs.getString("numeroexterior"))
				row.setNumerointerior(rs.getString("numerointerior"))
				row.setOtroparentesco(rs.getString("otroparentesco"))
				row.setPuesto(rs.getString("puesto"))
				row.setTelefono(rs.getString("telefono"))
				row.setVivecontigo(rs.getBoolean("vivecontigo"))
				row.setCampusegreso(rs.getString("campusegreso"))
				row.setEgresoanahuac(rs.getString("egresoanahuac"))
				row.setEscolaridad(rs.getString("escolaridad"))
				row.setEstado(rs.getString("estado"))
				row.setPais(rs.getString("pais"))
				row.setParentesco(rs.getString("parentesco"))
				row.setTitulo(rs.getString("titulo"))
				row.setTrabaja(rs.getString("trabaja"))
				row.setVive(rs.getString("vive"))
				row.setVencido(rs.getBoolean("vencido"))
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getContactoEmergencia(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			ContactoEmergenciaEntity row = new ContactoEmergenciaEntity()
			List<ContactoEmergenciaEntity> rows = new ArrayList<ContactoEmergenciaEntity>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_CONTACTO_EMERGENCIA_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new ContactoEmergenciaEntity()
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setNombre(rs.getString("nombre"))
				row.setOtroparentesco(rs.getString("otroparentesco"))
				row.setTelefono(rs.getString("telefono"))
				row.setTelefonocelular(rs.getString("telefonocelular"))
				row.setParentesco(rs.getString("parentesco"))
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getHermanosVencidos(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatHermanoCustom row = new CatHermanoCustom()
			List<CatHermanoCustom> rows = new ArrayList<CatHermanoCustom>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_HERMANOS_VENCIDOS;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new CatHermanoCustom()
				row.setCaseid(rs.getLong("caseid"))
				row.setNombre(rs.getString("nombres"))
				row.setApellidos(rs.getString("apellidos"))
				row.setFechanacimiento(rs.getString("fechanacimiento"))
				row.setIsestudia(rs.getBoolean("isestudia"))
				row.setEscuelaestudia(rs.getString("escuelaestudia"))
				row.setIstrabaja(rs.getBoolean("istrabaja"))
				row.setEmpresatrabaja(rs.getString("empresatrabaja"))
				
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInformacionEscolarVencidos(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatInformacionEscolarCustom row = new CatInformacionEscolarCustom()
			List<CatInformacionEscolarCustom> rows = new ArrayList<CatInformacionEscolarCustom>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_INFORMACION_ESCOLAR_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new CatInformacionEscolarCustom()
				row.setCaseid(rs.getLong("caseid"))
				row.setAnoinicio(rs.getString("anoinicio"))
				row.setAnofin(rs.getString("anofin"))
				row.setPromedio(rs.getString("promedio"))
				row.setCiudad(rs.getString("ciudad"))
				row.setEstadoextranjero(rs.getString("estadoextranjero"))
				row.setOtraescuela(rs.getString("otraescuela"))
				row.setGrado(rs.getString("grado"))
				if(!rs.getString("otraescuela").equals("")) {
					row.setEscuela(rs.getString("otraescuela"))
				}else {
					row.setEscuela(rs.getString("escuela"))
				}
				row.setEstado(rs.getString("estado"))
				
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getUniviersidadesVencidos(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatUniversidadesCustom row = new CatUniversidadesCustom()
			List<CatUniversidadesCustom> rows = new ArrayList<CatUniversidadesCustom>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_UNIVERSIDADES_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new CatUniversidadesCustom()
				row.setCaseid(rs.getLong("caseid"))
				row.setNombre(rs.getString("nombre"))
				row.setCarrera(rs.getString("carrera"))
				row.setAnoinicio(rs.getString("anoinicio"))
				row.setAnofin(rs.getString("anofin"))
				row.setMotivosuspension(rs.getString("motivosuspension"))
				
				
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getIdiomaVencidos(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatIdiomaCustom row = new CatIdiomaCustom()
			List<CatIdiomaCustom> rows = new ArrayList<CatIdiomaCustom>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_IDIOMA_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new CatIdiomaCustom()
				row.setCaseid(rs.getLong("caseid"))
				row.setOtroidioma(rs.getString("otroidioma"))
				row.setIdiomahablas(rs.getString("idiomahablas"))
				row.setConversacion(rs.getString("nivelconversacion"))
				row.setEscritura(rs.getString("nivelescritura"))
				row.setTraduccion(rs.getString("niveltraduccion"))
				row.setLectura(rs.getString("nivellectura"))
				
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getTerapiaVencidos(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatTerapiaCustom row = new CatTerapiaCustom()
			List<CatTerapiaCustom> rows = new ArrayList<CatTerapiaCustom>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_TERAPIA_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new CatTerapiaCustom()
				row.setCaseid(rs.getLong("caseid"))
				row.setTipoterapia(rs.getString("tipoterapia"))
				row.setCuantotiempo(rs.getString("cuantotiempo"))
				row.setRecibidoterapia(rs.getString("recibidoterapiastring"))
				row.setOtraterapia(rs.getString("otraterapia"))
				row.setTipodeterapia(rs.getString("tipodeterapia"))
				row.setHasrecibidoterapia(rs.getString("hasrecibidoterapia"))
				
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getGrupoSocialVencidos(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatGrupoSocialCustom row = new CatGrupoSocialCustom()
			List<CatGrupoSocialCustom> rows = new ArrayList<CatGrupoSocialCustom>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_GRUPOSOCIAL_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new CatGrupoSocialCustom()
				row.setCaseid(rs.getLong("caseid"))
				row.setNombre(rs.getString("nombre"))
				row.setAfiliacion(rs.getString("afiliacion"))
				row.setTiempo(rs.getString("tiempo"))
				row.setPertenecesaunclub(rs.getString("pertenecesaunclub"))
				row.setGruposocial(rs.getString("gruposocial"))
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getParienteEgresadoVencidos(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatParienteEgresadoAnahuacCustom row = new CatParienteEgresadoAnahuacCustom()
			List<CatParienteEgresadoAnahuacCustom> rows = new ArrayList<CatParienteEgresadoAnahuacCustom>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_PARIENTE_EGRESADO_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new CatParienteEgresadoAnahuacCustom()
				row.setCaseid(rs.getLong("caseid"))
				row.setNombre(rs.getString("nombre"))
				row.setApellidos(rs.getString("apellidos"))
				row.setCarrera(rs.getString("carrera"))
				row.setCorreo(rs.getString("correo"))
				row.setCampus(rs.getString("campus"))
				row.setParentesco(rs.getString("parentesco"))
				row.setDiploma(rs.getString("diploma"))
				rows.add(row)
			}
				resultado.setSuccess(true)
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getTempKeyAzure() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			List<String> rows = new ArrayList<String>();
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA);
			rs= pstm.executeQuery();
			
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			
			rows.add(SSA);
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
//	public Result getDuplicados(String curp, String nombre, String correoElectronico, String fechaNacimiento,String caseid) {
public Result getDuplicados(String curp, 
		 primerNombre,  segundoNombre, 
		 apellidoPaterno,apellidoMaterno, 
		 sexo,idbanner,caseid
	) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog = "";
		
		//.replace("[IDBANNER]", caseid)
		
		try {
			String consulta = Statements.GET_DUPLICADOSV3.replace("[CASEID]", caseid).replace("[IDBANNER]", idbanner).replace("[CURP]", curp).replace("[PRIMERNOMBRE]", primerNombre).replace("[SEGUNDONOMBRE]", segundoNombre).replace("[APELLIDOPATERNO]", apellidoPaterno).replace("[APELLIDOMATERNO]", apellidoMaterno).replace("[SEXO]", sexo)
			errorlog +=consulta;
			
			closeCon = validarConexion();
			
			/*pstm = con.prepareStatement(Statements.GET_DUPLICADOSV2);
			pstm.setString(1, primerNombre);
			pstm.setString(2, segundoNombre);
			pstm.setString(3, apellidoPaterno);
			pstm.setString(4, apellidoMaterno);
			pstm.setLong(5,  Long.valueOf(caseid));
			pstm.setString(6, correoElectronico);
			pstm.setString(7, curp);

			errorlog+= " curp: "+curp+" nombre: "+nombre+" correoElectronico: "+correoElectronico+" fecha: "+fechaNacimiento;*/
			
			pstm = con.prepareStatement(consulta);
			rs= pstm.executeQuery();
			
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				info.add(columns);
			}
			
			resultado.setSuccess(true);
			resultado.setData(info);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getPropedeutico(String correo) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			
			pstm = con.prepareStatement(Statements.GET_PROPEDEUTICO);
			pstm.setString(1, correo)
			
			rs= pstm.executeQuery();
			
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				info.add(columns)
			}
			
			resultado.setSuccess(true);
			resultado.setData(info);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getCorreoByCaseid(String caseId) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			
			pstm = con.prepareStatement(Statements.GET_CORREO_BY_CASEID);
			pstm.setLong(1, Long.valueOf(caseId))
			
			rs= pstm.executeQuery();
			
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				info.add(columns)
			}
			
			resultado.setSuccess(true);
			resultado.setData(info);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getUserIdBanner(String idBanner) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			
			pstm = con.prepareStatement(Statements.GET_USERNAME_BY_IDBANNER);
			pstm.setString(1, idBanner)
			
			rs= pstm.executeQuery();
			
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				info.add(columns)
			}
			
			resultado.setSuccess(true);
			resultado.setData(info);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}


public Result updateViewDownloadSolicitud(Integer parameterP, Integer parameter, String key, String intento, Boolean tipoTabla, String jsonData, RestAPIContext context) {
    Result resultado = new Result();
    Boolean closeCon = false;
	Boolean executionQuery = false;
    String errorLog = "";
    String replaceColumn = "";
    String where = "";
    String replaceTableSolicitud = "";
    String replaceTablePadresTutor = "";
    String replaceTableContacEmergencia = "";
	String fechaOutput = "";
    List < String > rows = new ArrayList < String > ();

    try {
        closeCon = validarConexion();
        con.setAutoCommit(false)

        def jsonSlurper = new JsonSlurper();
        def object = jsonSlurper.parseText(jsonData);
		
        if (tipoTabla == true) {
            replaceTableSolicitud = " SolicitudDeAdmision ";
            replaceTablePadresTutor = " PadresTutor ";
            replaceTableContacEmergencia = " ContactoEmergencias ";
			executionQuery = true;
        } else {
            replaceTableSolicitud = " SolicitudDeAdmisionRespaldo ";
            replaceTablePadresTutor = " PadresTutorRespaldo ";
            replaceTableContacEmergencia = " ContactoEmergenciasRespaldo ";
            executionQuery = true;
        }

        if(replaceTablePadresTutor.equals(" PadresTutorRespaldo ") && key.equals("IT") || replaceTablePadresTutor.equals(" PadresTutorRespaldo ") && key.equals("DPT")) {
			 where = " WHERE caseid = ? AND countintento = ? AND vive_pid IS NULL AND istutor = 't'";
        } else if(replaceTablePadresTutor.equals(" PadresTutor ") && key.equals("IT") || replaceTablePadresTutor.equals(" PadresTutor ") && key.equals("DPT")) {
             where = " WHERE caseid = ? AND persistenceid = ? AND vive_pid IS NULL AND istutor = 't'";
        } else if (replaceTablePadresTutor.equals(" PadresTutorRespaldo ") && key.equals("IPA") || replaceTablePadresTutor.equals(" PadresTutorRespaldo ") && key.equals("DPPA") || replaceTablePadresTutor.equals(" PadresTutorRespaldo ") &&  key.equals("IMA") || replaceTablePadresTutor.equals(" PadresTutorRespaldo ") &&  key.equals("DPMA")) {
             where = " WHERE caseid = ? AND countintento = ? AND vive_pid IS NOT NULL AND istutor = 'f'";
        } else if (replaceTablePadresTutor.equals(" PadresTutor ") && key.equals("IPA") || replaceTablePadresTutor.equals(" PadresTutor ") && key.equals("DPPA") || replaceTablePadresTutor.equals(" PadresTutor ") && key.equals("IMA") || replaceTablePadresTutor.equals(" PadresTutor ") && key.equals("DPMA")) {
             where = " WHERE caseid = ? AND persistenceid = ? AND vive_pid IS NOT NULL AND istutor = 'f'";
        }
		
		if(replaceTablePadresTutor.equals(" PadresTutorRespaldo ")  && object.egresoAnahuac_pid != 77 || replaceTablePadresTutor.equals(" PadresTutor ") && object.egresoAnahuac_pid != 77) {
			replaceColumn = ", catcampusegreso_pid = ?";
		}
		if(key.equals("IP") && fechaNacimiento != null || key.equals("IP") && object.fechaNacimiento != "") {
			fechaOutput = object.fechaNacimiento;
			SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date formatoFechaNacimiento = output.parse(fechaOutput);
			fechaOutput = output.format(formatoFechaNacimiento)
		}

        if (key.equals("IP")) {
            pstm = con.prepareStatement(Statements.UPDATE_SECCION_INFORMACION_PERSONAL.replace("[TABLA]", replaceTableSolicitud));
            errorLog += "Sección: Información personal | "+pstm;
            pstm.setString(1, object.primerNombre);
            pstm.setString(2, object.segundoNombre);
            pstm.setString(3, object.apellidoPaterno);
            pstm.setString(4, object.apellidoMaterno);
            pstm.setString(5, fechaOutput);
            pstm.setLong(6, object.sexo_pid);
            pstm.setLong(7, object.nacionalidad_pid);
            pstm.setLong(8, object.religion_pid);
            pstm.setString(9, object.curp);
            pstm.setLong(10, object.estadoCivil_pid);
            pstm.setString(11, object.telefonoCelular);
            pstm.setLong(12, object.caseid);
            pstm.setString(13, object.correoElectronico);
            errorLog += "Fin sección: Información personal | "+pstm;

        } else if (key.equals("DP")) {
            pstm = con.prepareStatement(Statements.UPDATE_SECCION_DOMICILIO_PERMANENTE.replace("[TABLA]", replaceTableSolicitud));
            errorLog += "Sección: Domicilio permanente | " + pstm;
			errorLog += "OBJETO | " + object;
            pstm.setLong(1, object.pais_pid);
            pstm.setString(2, object.codigoPostal);
            pstm.setString(3, object.estadoExtranjero);
            pstm.setLong(4, object.estado_pid);
            pstm.setString(5, object.ciudad);
            pstm.setString(6, object.delegacionMunicipio);
            pstm.setString(7, object.colonia);
            pstm.setString(8, object.calle);
            pstm.setString(9, object.calle2);
            pstm.setString(10, object.numExterior);
            pstm.setString(11, object.numInterior);
            pstm.setString(12, object.telefono);
            pstm.setString(13, object.otroTelefonoContacto);
            pstm.setLong(14, object.caseid);
            pstm.setString(15, object.correoElectronico);

            errorLog += "Fin sección: Domicilio permanente | "+pstm;

        } else if (key.equals("IB")) {
            pstm = con.prepareStatement(Statements.UPDATE_SECCION_INFORMACION_BACHILLERATO.replace("[TABLA]", replaceTableSolicitud));
            errorLog += "Sección: Información bachillerato | "+pstm;
            pstm.setLong(1, Integer.parseInt(object.bachillerato_pid));
            pstm.setString(2, object.nombreBachillerato);
            pstm.setString(3, object.paisBachillerato);
            pstm.setString(4, object.estadoBachillerato);
            pstm.setString(5, object.ciudadBachillerato);
            pstm.setString(6, object.promedioGeneral);
            pstm.setLong(7, object.resultadoPAA);
            pstm.setLong(8, object.caseid);
            pstm.setString(9, object.correoElectronico);
            errorLog += "Fin sección: Información bachillerato | "+pstm;

        } else if (key.equals("IT")) {
            while (executionQuery == true) {
                pstm = con.prepareStatement(Statements.UPDATE_SECCION_INFORMACION_TUTOR.replace("[TABLA]", replaceTablePadresTutor).replace("[COLUMN]", replaceColumn).replace("[WHERE]", where));
                errorLog += "Sección: Información tutor | "+pstm;
                pstm.setLong(1, object.titulo_pid);
                pstm.setString(2, object.nombre);
                pstm.setString(3, object.apellidos);
                pstm.setLong(4, object.parentesco_pid);
                pstm.setString(5, object.otroParentesco);
                pstm.setString(6, object.correoElectronico);
                pstm.setLong(7, object.escolaridad_pid);
                pstm.setLong(8, object.egresoAnahuac_pid);
                pstm.setLong(9, object.trabaja_pid);
                pstm.setString(10, object.empresaTrabaja);
                pstm.setString(11, object.giro);
                pstm.setString(12, object.puesto);
				
				if (replaceTablePadresTutor.equals(" PadresTutor ")) {
					if (object.egresoAnahuac_pid.equals(77)) {
						pstm.setLong(13, object.caseid);
						pstm.setLong(14, object.persistenceid);
					} else {
						pstm.setLong(13, object.campusegreso_pid);
						pstm.setLong(14, object.caseid);
						pstm.setLong(15, object.persistenceid);
					}
					replaceTablePadresTutor = " PadresTutorRespaldo ";
					where = " WHERE caseid = ?  AND countintento = ? AND vive_pid IS NULL AND istutor = 't'";
				} else if (replaceTablePadresTutor.equals(" PadresTutorRespaldo ")) {
					if(object.egresoAnahuac_pid.equals(77)) {
						pstm.setLong(13, object.caseid);
						pstm.setLong(14, (intento.equals("null") ? 0 : Integer.parseInt(intento)));
					} else {
						pstm.setLong(13, object.campusegreso_pid);
						pstm.setLong(14, object.caseid);
						pstm.setLong(15, (intento.equals("null") ? 0 : Integer.parseInt(intento)));
					}
                    executionQuery = false;
                }
                errorLog += "Fin sección: Información tutor | "+pstm;
                pstm.executeUpdate(); 
            }

        } else if (key.equals("DPT")) {
            while (executionQuery == true) {
                pstm = con.prepareStatement(Statements.UPDATE_SECCION_DOMICILIO_TUTOR.replace("[TABLA]", replaceTablePadresTutor).replace("[WHERE]", where));
                errorLog += "Sección: Domicilio tutor | "+pstm;
                pstm.setLong(1, object.pais_pid);
                pstm.setString(2, object.codigoPostal);
                pstm.setString(3, object.estadoExtranjero);
                pstm.setLong(4, object.estado_pid);
                pstm.setString(5, object.ciudad);
                pstm.setString(6, object.delegacionMunicipio);
                pstm.setString(7, object.colonia);
                pstm.setString(8, object.calle);
                pstm.setString(9, object.numExterior);
                pstm.setString(10, object.numInterior);
                pstm.setString(11, object.telefono);
                pstm.setLong(12, object.caseid);

                if (replaceTablePadresTutor.equals(" PadresTutor ")) {
					pstm.setLong(13, object.persistenceid);
					replaceTablePadresTutor = " PadresTutorRespaldo ";
					where = " WHERE caseid = ?  AND countintento = ? AND vive_pid IS NULL AND istutor = 't'";
				} else if (replaceTablePadresTutor.equals(" PadresTutorRespaldo ")) {
                    pstm.setLong(13, (intento.equals("null") ? 0 : Integer.parseInt(intento)));
                    executionQuery = false;
                }

                errorLog += "Fin sección: Domicilio tutor | "+pstm;
                pstm.executeUpdate();
            }

        } else if (key.equals("IPA")) {
            while (executionQuery == true) {
                pstm = con.prepareStatement(Statements.UPDATE_SECCION_INFORMACION_PADRE.replace("[TABLA]", replaceTablePadresTutor).replace("[COLUMN]", replaceColumn).replace("[WHERE]", where));
                errorLog += "Sección: Información padre | "+pstm;
                pstm.setLong(1, object.titulo_pid);
                pstm.setString(2, object.nombre);
                pstm.setString(3, object.apellidos);
                pstm.setString(4, object.correoElectronico);
                pstm.setLong(5, object.escolaridad_pid);
                pstm.setLong(6, object.egresoAnahuac_pid);
                pstm.setLong(7, object.trabaja_pid);
                pstm.setString(8, object.empresaTrabaja);
                pstm.setString(9, object.giro);
                pstm.setString(10, object.puesto);

                if (replaceTablePadresTutor.equals(" PadresTutor ")) {
                	if (object.egresoAnahuac_pid.equals(77)) {
                		pstm.setLong(11, object.caseid);
                		pstm.setLong(12, object.persistenceid);
                	} else {
                		pstm.setLong(11, object.campusegreso_pid);
                		pstm.setLong(12, object.caseid);
                		pstm.setLong(13, object.persistenceid);
                	}
                	replaceTablePadresTutor = " PadresTutorRespaldo ";
                    where = " WHERE caseid = ? AND countintento = ? AND vive_pid IS NOT NULL AND istutor = 'f'";
                } else if (replaceTablePadresTutor.equals(" PadresTutorRespaldo ")) {
                	if (object.egresoAnahuac_pid.equals(77)) {
                		pstm.setLong(11, object.caseid);
                		pstm.setLong(12, (intento.equals("null") ? 0 : Integer.parseInt(intento)));
                	} else {
                		pstm.setLong(11, object.campusegreso_pid);
                		pstm.setLong(12, object.caseid);
                		pstm.setLong(13, (intento.equals("null") ? 0 : Integer.parseInt(intento)));
                	}
                    executionQuery = false;
                }
                errorLog += "Fin sección: Información padre | "+pstm;
                pstm.executeUpdate();
            }
        } else if (key.equals("DPPA")) {
            while (executionQuery == true) {
                pstm = con.prepareStatement(Statements.UPDATE_SECCION_DOMICILIO_PADRE.replace("[TABLA]", replaceTablePadresTutor).replace("[WHERE]", where));
                errorLog += "Sección: Domicilio padre | "+pstm;
                pstm.setLong(1, object.pais_pid);
                pstm.setString(2, object.codigoPostal);
                pstm.setString(3, object.estadoExtranjero);
                pstm.setLong(4, object.estado_pid);
                pstm.setString(5, object.ciudad);
                pstm.setString(6, object.delegacionMunicipio);
                pstm.setString(7, object.colonia);
                pstm.setString(8, object.calle);
                pstm.setString(9, object.numExterior);
                pstm.setString(10, object.numInterior);
                pstm.setString(11, object.telefono);
                pstm.setString(12, object.viveContigo);
                pstm.setLong(13, object.caseid);

                if(replaceTablePadresTutor.equals(" PadresTutor ")) {
					pstm.setLong(14, object.persistenceid);
					replaceTablePadresTutor = " PadresTutorRespaldo ";
                    where = " WHERE caseid = ?  AND countintento = ? AND vive_pid IS NOT NULL AND istutor = 'f'";
                } else if (replaceTablePadresTutor.equals(" PadresTutorRespaldo ")) {
                    pstm.setString(14, (intento.equals("null") ? 0 : Integer.parseInt(intento)));
                    executionQuery = false;
                }
                errorLog += "Fin sección: Domicilio padre | "+pstm;
                pstm.executeUpdate();
            }
        } else if (key.equals("IMA")) {
            while (executionQuery == true) {
                pstm = con.prepareStatement(Statements.UPDATE_SECCION_INFORMACION_MADRE.replace("[TABLA]", replaceTablePadresTutor).replace("[COLUMN]", replaceColumn).replace("[WHERE]", where));
                errorLog += "Sección: Información madre | "+pstm;
                pstm.setLong(1, object.titulo_pid);
                pstm.setString(2, object.nombre);
                pstm.setString(3, object.apellidos);
                pstm.setString(4, object.correoElectronico);
                pstm.setLong(5, object.escolaridad_pid);
                pstm.setLong(6, object.egresoAnahuac_pid);
                pstm.setLong(7, object.trabaja_pid);
                pstm.setString(8, object.empresaTrabaja);
                pstm.setString(9, object.giro);
                pstm.setString(10, object.puesto);
                
                if (replaceTablePadresTutor.equals(" PadresTutor ")) {
                	if(object.egresoAnahuac_pid.equals(77)) {
                		pstm.setLong(11, object.caseid);
						pstm.setLong(12, object.persistenceid);
                	} else {
                		pstm.setLong(11, object.campusegreso_pid);
                	  	pstm.setLong(12, object.caseid);
						pstm.setLong(13, object.persistenceid);
                	}
                    replaceTablePadresTutor = " PadresTutorRespaldo ";
                    where = " WHERE caseid = ?  AND countintento = ? AND vive_pid IS NOT NULL AND istutor = 'f'";
                } else if (replaceTablePadresTutor.equals(" PadresTutorRespaldo ")) {
                	if(object.egresoAnahuac_pid.equals(77)) {
                		pstm.setLong(11, object.caseid);
						pstm.setLong(12, (intento.equals("null") ? 0 : Integer.parseInt(intento)));
                	} else {
                		pstm.setLong(11, object.campusegreso_pid);
                	  	pstm.setLong(12, object.caseid);
						pstm.setLong(13, (intento.equals("null") ? 0 : Integer.parseInt(intento)));
                	}
                    executionQuery = false;
                }
                errorLog += "fin sección: Información madre | "+pstm;
                pstm.executeUpdate();
            }

        } else if (key.equals("DPMA")) {
            while (executionQuery == true) {
                pstm = con.prepareStatement(Statements.UPDATE_SECCION_DOMICILIO_MADRE.replace("[TABLA]", replaceTablePadresTutor).replace("[WHERE]", where));
                errorLog += "Sección: Domicilio madre | "+pstm;
                pstm.setLong(1, object.pais_pid);
                pstm.setString(2, object.codigoPostal);
                pstm.setString(3, object.estadoExtranjero);
                pstm.setLong(4, object.estado_pid);
                pstm.setString(5, object.ciudad);
                pstm.setString(6, object.delegacionMunicipio);
                pstm.setString(7, object.colonia);
                pstm.setString(8, object.calle);
                pstm.setString(9, object.numExterior);
                pstm.setString(10, object.numInterior);
                pstm.setString(11, object.telefono);
                pstm.setString(12, object.viveContigo);
                pstm.setLong(13, object.caseid);

                if (replaceTablePadresTutor.equals(" PadresTutor ")) {
                	pstm.setLong(14, object.persistenceid);
                    replaceTablePadresTutor = " PadresTutorRespaldo ";
                    where = " WHERE caseid = ?  AND countintento = ?  AND vive_pid IS NOT NULL AND istutor = 'f'";
                } else if (replaceTablePadresTutor.equals(" PadresTutorRespaldo ")) {
                    pstm.setString(14, (intento.equals("null") ? 0 : Integer.parseInt(intento)));
                    executionQuery = false;
                }

                errorLog += "Fin sección: Domicilio madre | "+pstm;
                pstm.executeUpdate();
            }
        } else if (key.equals("CE")) {
            pstm = con.prepareStatement(Statements.UPDATE_SECCION_CONTACTO_EMERGENCIA.replace("[TABLA]", replaceTableContacEmergencia));
            errorLog += "Sección: Información contacto de emergencia | "+pstm;
            pstm.setString(1, object.nombre);
            pstm.setString(2, object.parentesco);
            pstm.setString(3, object.telefono);
			pstm.setString(4, object.telefonoCelular);
            pstm.setLong(5, object.caseid);
            pstm.setLong(6, object.parentesco_pid);
            pstm.setLong(7, object.persistenceid);
            errorLog += "Fin sección: Información contacto de emergencia | "+pstm;
        }

        if(key.equals("IP") || key.equals("DP") || key.equals("IB") || key.equals("CE")) {
            pstm.executeUpdate();
        }
        errorLog += "Se ejecuto el update correctamente - Consulta | " + pstm;
        con.commit();

        resultado.setData(rows)
        resultado.setSuccess(true)
        resultado.setError_info(errorLog+" | "+pstm);
    } catch (Exception e) {
        resultado.setSuccess(false);
        resultado.setError("| "+e.getMessage()+" | "+errorLog);
        resultado.setError_info(errorLog + " " + e.getMessage() + " | " + pstm)
        con.rollback();
    } finally {
        if (closeCon) {
            new DBConnect().closeObj(con, stm, rs, pstm)
        }
    }
    return resultado
}
	
	
	public Result getIsPeriodoVencido(String periodo) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_IS_PERIODO_VENCIDO);
			pstm.setLong(1, Long.parseLong(periodo));
			
			rs= pstm.executeQuery();
			
			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			Map<String, Object> columns = new LinkedHashMap<String, Object>();
			if(rs.next()) {
				columns.put("isVencido", true);
			}else {
				columns.put("isVencido", false);
			}
			info.add(columns)
			
			
			resultado.setSuccess(true);
			resultado.setData(info);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result postUpdatePeriodoVencido(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.UPDATE_PERIODO_VENCIDO_SOLICITUD);
			pstm.setLong(1, Long.parseLong(object.persistenceid));
			pstm.setLong(2, Long.parseLong(object.caseid));
			
			pstm.executeUpdate();
			
			con.commit();
			
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getUpdateFamiliaresIntento(String caseid, intentos, cantidad) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		Boolean executar = false;
		try {
			
			closeCon = validarConexion();
			
			executar = true;
			con.setAutoCommit(false)
			pstm = con.prepareStatement("UPDATE padresTutor SET countIntento = null WHERE  caseid = ${caseid} AND countIntento = ${intentos} " );
			pstm.executeUpdate();
			
			// Saca a los padres
			errorlog+="Actualizar a los padres";
			pstm = con.prepareStatement("SELECT DISTINCT ON (catparentezco_pid) * FROM padrestutor where caseid = ${caseid} AND desconozcodatospadres IS NOT NULL ORDER by catparentezco_pid,persistenceid DESC  limit 2");
			rs = pstm.executeQuery();
			
			
			while(rs.next()) {
				
				pstm = con.prepareStatement("INSERT INTO padresTutorRespaldo ( persistenceid,persistenceversion,apellidos,calle,caseid,ciudad,codigopostal,colonia,correoelectronico,delegacionmunicipio,desconozcodatospadres,empresatrabaja,estadoextranjero,giroempresa,istutor,nombre,numeroexterior,numerointerior,otroparentesco, puesto,telefono,vencido,vivecontigo,catcampusegreso_pid,categresoanahuac_pid,catescolaridad_pid,catestado_pid,catpais_pid,catparentezco_pid,cattitulo_pid,cattrabaja_pid,vive_pid,countintento) SELECT  (case when (SELECT max(persistenceId)+1 from padresTutorRespaldo) is null then 0 else (SELECT max(persistenceId)+1 from padresTutorRespaldo) END) as persistenceid,persistenceid as persistenceversion,apellidos,calle,caseid,ciudad,codigopostal,colonia,correoelectronico,delegacionmunicipio,desconozcodatospadres,empresatrabaja,estadoextranjero,giroempresa,istutor,nombre,numeroexterior,numerointerior,otroparentesco, puesto,telefono,vencido,vivecontigo,catcampusegreso_pid,categresoanahuac_pid,catescolaridad_pid,catestado_pid,catpais_pid,catparentezco_pid,cattitulo_pid,cattrabaja_pid,vive_pid, ${intentos} as countintento FROM PadresTutor WHERE caseid = ${caseid} AND Persistenceid = "+rs.getString('persistenceid') );
				pstm.executeUpdate();
				
				errorlog+="UPDATE padresTutor SET countIntento = ${intentos} WHERE  caseid = ${caseid} AND  persistenceid = "+rs.getString('persistenceid');
				pstm = con.prepareStatement("UPDATE padresTutor SET countIntento = ${intentos} WHERE  caseid = ${caseid} AND  persistenceid = "+rs.getString('persistenceid') );
				pstm.executeUpdate();
			}
			
			// Saca a los tutores
			errorlog+="Actualizar a los tutores";
			pstm = con.prepareStatement("SELECT persistenceid FROM padresTutor WHERE caseid = ${caseid} AND vive_pid IS NULL AND istutor IS TRUE ORDER BY persistenceid DESC LIMIT ${cantidad}");
			rs = pstm.executeQuery();
			while(rs.next()) {
				
				pstm = con.prepareStatement("INSERT INTO padresTutorRespaldo ( persistenceid,persistenceversion,apellidos,calle,caseid,ciudad,codigopostal,colonia,correoelectronico,delegacionmunicipio,desconozcodatospadres,empresatrabaja,estadoextranjero,giroempresa,istutor,nombre,numeroexterior,numerointerior,otroparentesco, puesto,telefono,vencido,vivecontigo,catcampusegreso_pid,categresoanahuac_pid,catescolaridad_pid,catestado_pid,catpais_pid,catparentezco_pid,cattitulo_pid,cattrabaja_pid,vive_pid,countintento) SELECT  (case when (SELECT max(persistenceId)+1 from padresTutorRespaldo) is null then 0 else (SELECT max(persistenceId)+1 from padresTutorRespaldo) END) as persistenceid,persistenceid as persistenceversion,apellidos,calle,caseid,ciudad,codigopostal,colonia,correoelectronico,delegacionmunicipio,desconozcodatospadres,empresatrabaja,estadoextranjero,giroempresa,istutor,nombre,numeroexterior,numerointerior,otroparentesco, puesto,telefono,vencido,vivecontigo,catcampusegreso_pid,categresoanahuac_pid,catescolaridad_pid,catestado_pid,catpais_pid,catparentezco_pid,cattitulo_pid,cattrabaja_pid,vive_pid, ${intentos} as countintento FROM PadresTutor WHERE caseid = ${caseid} AND Persistenceid = "+rs.getString('persistenceid') );
				pstm.executeUpdate();
				
				errorlog+=", UPDATE padresTutor SET countIntento = ${intentos} WHERE  caseid = ${caseid} AND  persistenceid = "+rs.getString('persistenceid');
				pstm = con.prepareStatement("UPDATE padresTutor SET countIntento = ${intentos} WHERE  caseid = ${caseid} AND  persistenceid = "+rs.getString('persistenceid') );
				pstm.executeUpdate();
			}
			
			if(executar) {
				con.commit();
			}
			
			resultado.setSuccess(true);
			//resultado.setData(info);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
			if(executar) {
				con.rollback();
			}
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getB64FileByUrlAzure(String urlAzure) {
		Boolean closeCon = false;
		String errorLog = "";
		Result resultado = new Result();
		String SSA = "";

		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA);
			rs = pstm.executeQuery();
			def num = Math.random();
			
			if (rs.next()) {
				SSA = rs.getString("valor");
				String urlDecodificada = "";
				urlDecodificada = urlAzure.replace("%20", " ");
				String[] elements = urlDecodificada.split("/");
				String url = java.net.URLEncoder.encode(elements[elements.length-1], "UTF-8");
				
				String finalURL = "";
				elements.eachWithIndex{it,index ->
					finalURL += (finalURL.length() == 0?"":"/")+"${(index == elements.length-1 ? url : it)}";
				}
				
				finalURL = finalURL.replace("+", "%20");
				
				if(urlAzure.toLowerCase().contains(".jpeg")) {
					columns.put("extension", ".jpeg");
					columns.put("b64", "data:image/jpeg;base64, " + (new FileDownload().b64UrlSSA(finalURL, SSA + "&v=" + num)));
				}else if(urlAzure.toLowerCase().contains(".png")) {
					columns.put("extension", ".png");
					columns.put("b64", "data:image/png;base64, " + (new FileDownload().b64UrlSSA(finalURL, SSA + "&v=" + num)));
				}else if(urlAzure.toLowerCase().contains(".jpg")) {
					columns.put("extension", ".jpg");
					columns.put("b64", "data:image/jpg;base64, " + (new FileDownload().b64UrlSSA(finalURL, SSA + "&v=" + num)));
				}else if(urlAzure.toLowerCase().contains(".jfif")) {
					columns.put("extension", ".jfif");
					columns.put("b64", "data:image/jfif;base64, " + (new FileDownload().b64UrlSSA(finalURL, SSA + "&v=" + num)));
				}else if(urlAzure.toLowerCase().contains(".pdf")) {
					columns.put("extension", ".pdf");
					columns.put("b64", "data:application/pdf;base64, " + (new FileDownload().b64UrlSSA(finalURL, SSA + "&v=" + num)));
				}
				
				rows.add(columns);
			}

			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError(errorLog);
		} catch (Exception e) {
			errorLog += e.toString();
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(errorLog);
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
}
