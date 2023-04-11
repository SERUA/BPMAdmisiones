package com.anahuac.rest.api.DAO
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.apache.http.Consts
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

import com.anahuac.rest.api.DB.BannerWSInfo
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class BannerRequestDAO {
	
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Result getBannerInfo(String idCampus, String idbanner) {
		Result result = new Result();
		Boolean closeCon = false;
		List<BannerWSInfo> lstResult = new ArrayList<BannerWSInfo>();
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_BANNER_API_INFO);
			pstm.setLong(1, Long.valueOf(idCampus));
			
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				String usuariobannersdae = rs.getString("usuariobannersdae");
				String pwBannerSDAE = rs.getString("pwBannerSDAE");
				String urlbannersdae = rs.getString("urlbannersdae");
				String urltokenbannersdae = rs.getString("urltokenbannersdae");
				
				HttpClient httpClient = HttpClientBuilder.create().build();
				String credentials = usuariobannersdae + ":" + pwBannerSDAE;
				byte[] bytes = credentials.getBytes();
				Base64.getEncoder().encode(bytes);
				String encoding = Base64.getEncoder().encodeToString(bytes);
				HttpPost post = new HttpPost(urltokenbannersdae);
				post.addHeader("Authorization", "Basic " + encoding);
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				formparams.add(new BasicNameValuePair("grant_type", "password"));
				formparams.add(new BasicNameValuePair("username", usuariobannersdae));
				UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
				post.setEntity(urlEntity);
				HttpResponse responsepost = httpClient.execute(post);
				HttpEntity entityPost = responsepost.getEntity();
				String response = EntityUtils.toString(entityPost);
				JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
				HttpGet get = new HttpGet(urlbannersdae + idbanner);
				String token = jsonObject.get("access_token").toString().replaceAll("\"", "");
				get.addHeader("Authorization", "Bearer " + token);
				HttpResponse responseget = httpClient.execute(get);
				BannerWSInfo bannerInfo = new BannerWSInfo();
				HttpEntity entityget = responseget.getEntity();
				
				if(entityget != null) {
					String responseget2 = EntityUtils.toString(entityget);
					JsonArray jsonArray = new JsonParser().parse(responseget2).getAsJsonArray();
					JsonObject resutadoBanner = new JsonParser().parse(jsonArray.get(0).getAsJsonObject().toString()).getAsJsonObject();
					bannerInfo.setAdmitido_a_medicina(jsonArray.get(0).getAsJsonObject().get("admitido_a_medicina").toString().replaceAll("\"", ""));
					bannerInfo.setCarga_de_materias(jsonArray.get(0).getAsJsonObject().get("carga_de_materias").toString().replaceAll("\"", ""));
					bannerInfo.setPago_de_inscripcion(jsonArray.get(0).getAsJsonObject().get("pago_de_inscripcion").toString().replaceAll("\"", ""));
					bannerInfo.setPago_de_propedeutico(jsonArray.get(0).getAsJsonObject().get("pago_de_propedeutico").toString().replaceAll("\"", ""));
					bannerInfo.setPrograma_periodo_campus(jsonArray.get(0).getAsJsonObject().get("programa_periodo_campus").toString().replaceAll("\"", ""))
				}
				
				lstResult.add(bannerInfo);
				result.setData(lstResult);
				result.setSuccess(true);
			} else {
				throw new Exception("No se ha podido encontrar la configuración del campus ");
			}
		} catch (IOException e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setError(e.getMessage());
			result.setData(new ArrayList<?>());
		} catch(Exception e){
			result.setSuccess(false);
			result.setError(e.getMessage());
			result.setData(new ArrayList<?>());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return result;
	}
	
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
	}

}
