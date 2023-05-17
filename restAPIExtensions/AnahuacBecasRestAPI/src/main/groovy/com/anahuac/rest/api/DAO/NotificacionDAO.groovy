package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.slf4j.Logger
import com.anahuac.catalogos.CatNotificaciones
import com.anahuac.catalogos.CatNotificacionesCampus
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result

class NotificacionDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Result getCartasNotificaciones(String campus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			CatNotificaciones row = new CatNotificaciones()
			List<CatNotificacionesCampus> rows = new ArrayList<CatNotificacionesCampus>();
			closeCon = validarConexion();
			//String consulta = "SELECT c.* FROM catnotificaciones c INNER JOIN procesocaso pc on pc.caseid=c.caseid and pc.campus=? WHERE c.tipoCorreo IN ('Notificaciones SDAE BC' ,'Notificaciones SDAE FNZ' ,'Cartas SDAE BC','Cartas SDAE FNZ') "
			String consulta = "SELECT c.* FROM catnotificaciones c INNER JOIN procesocaso pc on pc.caseid = c.caseid and pc.campus = ? WHERE c.tipoCorreo IN ('Notificación de SDAE', 'Carta de SDAE') ORDER BY c.codigo ASC";
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, campus);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				row = new CatNotificaciones();
				row.anguloImagenFooter = rs.getString("anguloImagenFooter");
				row.anguloImagenHeader = rs.getString("anguloImagenHeader");
				row.asunto = rs.getString("asunto");
				row.caseId = rs.getString("caseId");
				row.codigo = rs.getString("codigo");
				row.comentarioLeon = rs.getString("comentarioLeon");
				row.contenido  = rs.getString("contenido");
				row.contenidoCorreo = rs.getString("contenidoCorreo");
				row.contenidoLeonel = rs.getString("contenidoLeonel");
				row.descripcion = rs.getString("descripcion");
				row.docGuiaEstudio = rs.getString("docGuiaEstudio");
				row.enlaceBanner = rs.getString("enlaceBanner");
				row.enlaceContacto = rs.getString("enlaceContacto");
				row.enlaceFacebook = rs.getString("enlaceFacebook");
				row.enlaceFooter = rs.getString("enlaceFooter");
				row.enlaceInstagram = rs.getString("enlaceInstagram");
				row.enlaceTwitter = rs.getString("enlaceTwitter");
				row.nombreImagenFooter = rs.getString("nombreImagenFooter");
				row.textoFooter  = rs.getString("textoFooter");
				row.tipoCorreo = rs.getString("tipoCorreo");
				row.titulo = rs.getString("titulo");
				row.urlImgFooter = rs.getString("urlImgFooter");
				row.urlImgHeader = rs.getString("urlImgHeader");
				row.setPersistenceId(rs.getLong("persistenceid"));
				
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno;
	}
}
