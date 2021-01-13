package com.anahuac.rest.api.DAO

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.bdm.BDMQueryUtil
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.hamcrest.core.IsNull
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatApiKey
import com.anahuac.catalogos.CatApiKeyDAO
import com.anahuac.catalogos.CatCampus
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.ConektaOxxo
import com.anahuac.rest.api.Entity.ConektaPaymentResponse
import com.anahuac.rest.api.Entity.OrdenBitacora
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper
import io.conekta.Charge
import io.conekta.Conekta
import io.conekta.Customer
import io.conekta.LineItems
import io.conekta.Order
import io.conekta.OxxoPayment
import io.conekta.PaymentMethod
import io.conekta.SpeiPayment

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class ConektaDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConektaDAO.class);
	
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	ResultSet rs2;

	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
	}

    public Result pagoOxxoCash(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        List < ConektaOxxo > lstResultado = new ArrayList < ConektaOxxo > ();
        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        Integer start = 0;
        Integer end = 99999;
        Boolean isFound = true;
        String nombreCandidato = "";
		Long nowUnixTimestamp = System.currentTimeMillis();
		Long thirtyDaysFromNowUnixTimestamp = (nowUnixTimestamp + 30L * 24 * 60 * 60 * 1000)/ 1000L;
		String thirtyDaysFromNow = thirtyDaysFromNowUnixTimestamp.toString();
		String token = "";
		String name = "";
		String email = "";
		String phone = "";
		String street1 = "";
		String postal_code = "";
		String country = "";
		String unit_price = "";
		String campus_id = "";
		String campus = "";
		
        try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			name = object.name;
			email = object.email;
			phone = object.phone;
			unit_price = object.unit_price;
			campus_id = object.campus_id;
			caseId = Long.valueOf(object.caseId);
			campus = object.campus;
			
			Result resultApiKey = getApiKeyByCampus(context, campus_id);
			
			if(resultApiKey.success) {
				Conekta.setApiKey(resultApiKey.getData().get(0));
			} else {
				throw new Exception("Error inesperado");
			}
			
			Order order = Order.create(
				new JSONObject("{"
					+ "'line_items': [{"
						+ "'name': 'Admisión',"
						+ "'unit_price': " + unit_price + ","
						+ "'quantity': 1"
					+ "}],"
					+ "'currency': 'MXN',"
					+ "'customer_info': {"
						+ "'name': '" + name + "',"
						+ "'email': '" + email + "',"
						+ "'phone': '" + phone + "'"
					+ "},"
					 +"'metadata': {'description': 'Pago de examen de Admisión' , 'reference' : '1334523452345'},"
					+ "'charges':[{"
						  + "'payment_method': {"
						  + "'type': 'oxxo_cash',"
							+ "'expires_at': " + thirtyDaysFromNow
						+ "}"
					+ "}]"
				+ "}")
			);
			
			
            LineItems line_item = (LineItems) order.line_items.get(0);
            Charge charge = (Charge) order.charges.get(0);
            OxxoPayment oxxoPayment = (OxxoPayment) charge.payment_method;

			double amount = order.amount / 100;
			DecimalFormat twoPlaces = new DecimalFormat("0.00");
			
            lstResultado.add(new ConektaOxxo(
				order.id,
                "\$" + twoPlaces.format(amount).toString() + " " + order.currency,
                line_item.quantity + " - " + line_item.name + " - " + (line_item.unit_price / 100),
                oxxoPayment.reference)
			);
			
			
			//--------------------PARA LA BITACORA DE PAGOS---------------------------
			OrdenBitacora ordenBit = new OrdenBitacora();
			ordenBit.setNoTransaccion(order.id);
			ordenBit.setUsuarioAspirante(email);
			ordenBit.setFechaMovimiento(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:00.000'Z'").format(new Date()));
			ordenBit.setMonto("\$" + twoPlaces.format(amount).toString() + " " + order.currency);
			ordenBit.setMedioPago("Oxxo Pay. Referencia: " + oxxoPayment.reference);
			ordenBit.setEstatus("Pago pendiente");
			ordenBit.setObservaciones("");
			ordenBit.setCaseId(caseId);
			ordenBit.setNombrePago(name);
			ordenBit.setCampus(campus);

			crearRegistroPago(ordenBit);
			//--------------------FIN PARA LA BITACORA DE PAGOS-----------------------
								
            resultado.setData(lstResultado);
            resultado.setSuccess(true);
        } catch (io.conekta.ErrorList error) {
			LOGGER.error error.details.get(0).message
			resultado.setSuccess(false);
			resultado.setError(error.details.get(0).message);
		} catch (Exception e) {
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            LOGGER.error "ERROR=================================";
            LOGGER.error e.getMessage();
            e.printStackTrace();
        } 
		
		
        return resultado
    }
	
	public Result pagoTarjeta(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List<ConektaPaymentResponse> lstResultado = new ArrayList<ConektaPaymentResponse>();
//		Conekta.setApiKey("key_dHs4rzK9otyxcbqTrzn5sw");
		String token = "";
		String name = "";
		String email = "";
		String phone = "";
		String street1 = "";
		String postal_code = "";
		String country = "";
		String unit_price = "";
		String campus_id = "";
		double amount;
		DecimalFormat twoPlaces = new DecimalFormat("0.00");
		String order_id = "";
		String cardEnd = "";
		String cardBrand = "";
		Long caseId = 0L;
		String campus = "";
		String nombrePago = "";
		
		try{
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			token = object.idToken;
			email = object.email;
			phone = object.phone;
			name = object.name;
			unit_price = object.unit_price;
			campus_id = object.campus_id;
			cardEnd = object.last4;
			cardBrand = object.cardBrand;
			caseId = Long.valueOf(object.caseId);
			nombrePago = object.nombrePago;
			campus = object.campus;
			
			Result resultApiKey = getApiKeyByCampus(context, campus_id);
			
			if(resultApiKey.success) {
				Conekta.setApiKey(resultApiKey.getData().get(0));
			} else {
				throw new Exception("Error inesperado");
			}
			
			Customer customer = Customer.create(new JSONObject("{"
				+ "'name': '" + name + "',"
				+ "'email': '" + email + "',"
				+ "'phone': '" + phone + "',"
				+ "'metadata': {'description': 'Pago de examen de Admisión' , 'reference' : '1334523452345'},"
				+ "'payment_sources':[{"
					+ "'type': 'card',"
					+ "'token_id': '" + token + "'"
				+ "}]"
			+ "}"
			));

			Order order = Order.create(new JSONObject("{"
				+ "'currency': 'MXN', "
				+ "'customer_info': {"
					+ "'customer_id': '" + customer.getId() + "'"
				+ "},"
				+ "'line_items': [{"
					+ "'name': 'Admisión',"
					+ "'unit_price': " + unit_price + ","
					+ "'quantity': 1"
				+ "}],"
				+ "'charges': [{"
					+ "'payment_method': {"
						+ "'type': 'default'"
					+ "}"
				+ "}]"
			+ "}"
			));
		
			LineItems line_item = (LineItems)order.line_items.get(0);
			Charge charge = (Charge) order.charges.get(0);
			PaymentMethod payment_method = (PaymentMethod) charge.payment_method;
			order_id = order.id;
			lstResultado.add(new ConektaPaymentResponse(order.id, "" + (order.amount/100) + order.currency, line_item.quantity + " - "
				+ line_item.name + " - "
				+ (line_item.unit_price/100), payment_method.getVal("auth_code")+"", payment_method.getVal("name") + " - "
				+ payment_method.getVal("last4") + " - "
				+ payment_method.getVal("brand") + " - "
				+ payment_method.getVal("type")));
			
			//--------------------PARA LA BITACORA DE PAGOS---------------------------
			OrdenBitacora ordenBit = new OrdenBitacora();
			ordenBit.setNoTransaccion(order.id);
			ordenBit.setUsuarioAspirante(email);
			ordenBit.setFechaMovimiento(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:00.000'Z'").format(new Date()));
			ordenBit.setMonto("\$" + twoPlaces.format(amount).toString() + " " + order.currency);
			ordenBit.setMedioPago("Tarjeta " + payment_method.getVal("brand") + " que termina en " +  payment_method.getVal("last4"));
			ordenBit.setEstatus("Pago aceptado");
			ordenBit.setObservaciones("");
			ordenBit.setCaseId(caseId);
			ordenBit.setNombrePago(nombrePago);
			ordenBit.setCampus(campus);
			
			crearRegistroPago(ordenBit);
			//--------------------FIN PARA LA BITACORA DE PAGOS-----------------------
			resultado.setData(lstResultado)
			resultado.setSuccess(true)
		  
		} catch (io.conekta.ErrorList error) {
			LOGGER.error error.details.get(0).message;
			String errorMessage =  error.details.get(0).message;
			
			OrdenBitacora ordenBit = new OrdenBitacora();
			ordenBit.setNoTransaccion(order_id);
			ordenBit.setUsuarioAspirante(email);
			ordenBit.setFechaMovimiento(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:00.000'Z'").format(new Date()));
			ordenBit.setMonto("\$" + unit_price.toString() + "MXN");
			ordenBit.setMedioPago("Tarjeta " + cardBrand + " que termina en " +  cardEnd);
			ordenBit.setEstatus("Pago rechazado");
			ordenBit.setObservaciones(error.details.get(0).message);
			ordenBit.setCaseId(caseId);
			ordenBit.setNombrePago(nombrePago);
			ordenBit.setCampus(campus);
			
			crearRegistroPago(ordenBit);
			
			resultado.setSuccess(false);
			resultado.setError(error.details.get(0).message);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		    System.out.println(e.getMessage());
		} 
		
		return resultado;
	}
	
	public Result pagoSPEI(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		
		Result resultado = new Result();
		List<Map<String, String>> lstResultado = new ArrayList<Map<String, String>>();
		Long caseId = 0L;
//		Conekta.setApiKey("key_dHs4rzK9otyxcbqTrzn5sw");
		Long nowUnixTimestamp = System.currentTimeMillis();
		Long thirtyDaysFromNowUnixTimestamp =  (nowUnixTimestamp + 30L * 24 * 60 * 60 * 1000) / 1000L;
		String thirtyDaysFromNow = thirtyDaysFromNowUnixTimestamp.toString();
		String token = "";
		String name = "";
		String email = "";
		String phone = "";
		String unit_price = "";
		String campus_id = "";
		String campus = "";
		
		try{
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			name = object.name;
			email = object.email;
			phone = object.phone;
			unit_price = object.unit_price;
			campus_id = object.campus_id;
			caseId = Long.valueOf(object.caseId);
			campus = object.campus;
			
			Result resultApiKey = getApiKeyByCampus(context, campus_id);
			
			if(resultApiKey.success) {
				Conekta.setApiKey(resultApiKey.getData().get(0));
			} else {
				throw new Exception("Error inesperado");
			}
			
			Order order = Order.create(
				new JSONObject("{"
					+ "'line_items': [{"
						+ "'name': 'Admisión',"
						+ "'unit_price': " + unit_price + ","
						+ "'quantity': 1"
					+ "}],"
					+ "'currency': 'MXN',"
					+ "'customer_info': {"
						+ "'name': '" + name + "',"
						+ "'email': '" + email + "',"
						+ "'phone': '" + phone + "'"
					+ "},"
					 +"'metadata': {'description': 'Pago de examen de Admisión' , 'reference' : '1334523452345'},"
					+ "'charges':[{"
					  	+ "'payment_method': {"
						  + "'type': 'spei',"
							+ "'expires_at': " + thirtyDaysFromNow
						+ "}"
					+ "}]"
				+ "}")
			);
			
			LineItems line_item = (LineItems) order.line_items.get(0);
			Charge charge = (Charge) order.charges.get(0);
			SpeiPayment speiPayment = (SpeiPayment) charge.payment_method;
			Map<String, String> mapResultado = new HashMap<String, String>();
			mapResultado.put("speiBank", speiPayment.bank);
			mapResultado.put("CLABE", speiPayment.clabe);
			double amount = order.amount / 100;
			DecimalFormat twoPlaces = new DecimalFormat("0.00");
			mapResultado.put("amount", "\$" + twoPlaces.format(amount).toString() + " " + order.currency);
			mapResultado.put("id", order.id);
			
			//--------------------PARA LA BITACORA DE PAGOS---------------------------
			OrdenBitacora ordenBit = new OrdenBitacora();
			ordenBit.setNoTransaccion(order.id);
			ordenBit.setUsuarioAspirante(email);
			ordenBit.setFechaMovimiento(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:00.000'Z'").format(new Date()));
			ordenBit.setMonto("\$" + twoPlaces.format(amount).toString() + " " + order.currency);
			ordenBit.setMedioPago("Pago SPEI. CLABE: " + speiPayment.clabe + ". Banco: " + speiPayment.bank);
			ordenBit.setEstatus("Pago pendiente");
			ordenBit.setObservaciones("");
			ordenBit.setCaseId(caseId);
			ordenBit.setNombrePago(name);
			ordenBit.setCampus(campus);

			crearRegistroPago(ordenBit);
			//--------------------FIN PARA LA BITACORA DE PAGOS-----------------------
			
			lstResultado.add(mapResultado);
			
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (io.conekta.ErrorList error) {
			LOGGER.error error.details.get(0).message
			resultado.setSuccess(false);
			resultado.setError(error.details.get(0).message);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
			System.out.println(e.getMessage());
		} 
		return resultado;
	}
	
	public Result getApiKeyByCampus(RestAPIContext context, String campusId) {
		Result resultado = new Result();
		List<String> lstResultado = new ArrayList<String>();
		String apiKey = "";
		
		try{
			def objApiKey = context.getApiClient().getDAO(CatApiKeyDAO.class);
			List<CatApiKey> lstApiKey = objApiKey.find(0, 999);
			
			for(int i = 0; i < lstApiKey.size(); i++) {
				if(campusId == lstApiKey.get(i).getCampus().getPersistenceId().toString()) {
					apiKey = lstApiKey.get(i).getConekta();
					break;
				}
			}
			
			lstResultado.add(apiKey);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
			
			resultado.toString();
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return resultado;
	}
	
	public Result getOrderPaymentMethod(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		
		Result resultado = new Result();
		List<Map<String, String>> lstResultado = new ArrayList<HashMap<String, String>>();
		Map<String, String> mapResultado = new HashMap<String, String>();
		Long nowUnixTimestamp = System.currentTimeMillis();
		Long thirtyDaysFromNowUnixTimestamp =  (nowUnixTimestamp + 30L * 24 * 60 * 60 * 1000) / 1000L;
		String thirtyDaysFromNow = thirtyDaysFromNowUnixTimestamp.toString();
		String order_id = "";
		String campus_id = "";
		
		try{
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			order_id = object.order_id;
			campus_id = object.campus_id;
			
			Result resultApiKey = getApiKeyByCampus(context, campus_id);
			
			if(resultApiKey.success) {
				Conekta.setApiKey(resultApiKey.getData().get(0));
			} else {
				throw new Exception("Error inesperado");
			}
			
			Order order = Order.find(order_id);
			Charge charge = (Charge) order.charges.get(0);
			PaymentMethod payment_method = (PaymentMethod) charge.payment_method;
			String paymentType  = payment_method.type
			mapResultado.put("paymentType", paymentType);
			lstResultado.add(mapResultado);
			
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (io.conekta.ErrorList error) {
			LOGGER.error error.details.get(0).message
			resultado.setSuccess(false);
			resultado.setError(error.details.get(0).message);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return resultado;
	}
	
	public Result getOrderDetails(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List < Object > lstResultado = new ArrayList < Object > ();
		String order_id = "";
		String campus_id = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			order_id = object.order_id;
			campus_id = object.campus_id;
			
			Result resultApiKey = getApiKeyByCampus(context, campus_id);
			
			if(resultApiKey.success) {
				Conekta.setApiKey(resultApiKey.getData().get(0));
			} else {
				throw new Exception("Error inesperado");
			}
			
			Order order = Order.find(order_id);
			LineItems line_item = (LineItems) order.line_items.get(0);
			Charge charge = (Charge) order.charges.get(0);
			PaymentMethod payment_method = (PaymentMethod) charge.payment_method;
			double amount = order.amount / 100;
			DecimalFormat twoPlaces = new DecimalFormat("0.00");
			Map<String, String> mapResultado = new HashMap<String, String>();
			Date createdAt = new Date((long)1000 * order.created_at);
			SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
			formatDate.setTimeZone(TimeZone.getTimeZone("GMT-6"));
			SimpleDateFormat formatHours = new SimpleDateFormat("hh:mm");
			formatHours.setTimeZone(TimeZone.getTimeZone("GMT-6"));
			String dateString = formatDate.format(createdAt);
			String timeString = formatHours.format(createdAt);
			
			if(payment_method.type.equals("spei")) {
				SpeiPayment speiPayment = (SpeiPayment) charge.payment_method;
				mapResultado.put("speiBank", speiPayment.bank);
				mapResultado.put("CLABE", speiPayment.clabe);
				mapResultado.put("amount", "\$" + twoPlaces.format(amount).toString() + " " + order.currency);
				mapResultado.put("id", order.id);
				mapResultado.put("createdAtDate", dateString);
				mapResultado.put("createdAtTime", timeString);
				mapResultado.put("type", payment_method.type);
				
			} else if(payment_method.type.equals("oxxo")) {
				OxxoPayment oxxoPayment = (OxxoPayment) charge.payment_method;
				
				mapResultado.put("referencia", oxxoPayment.reference);
				mapResultado.put("amount", "\$" + twoPlaces.format(amount).toString() + " " + order.currency);
				mapResultado.put("id", order.id);
				mapResultado.put("createdAtDate", dateString);
				mapResultado.put("createdAtTime", timeString);
				mapResultado.put("type", payment_method.type);
			} else {
				mapResultado.put("cardNumber", payment_method.getVal("last4"));
				mapResultado.put("amount", "\$" + twoPlaces.format(amount).toString() + " " + order.currency);
				mapResultado.put("id", order.id);
				mapResultado.put("authorizationCode", payment_method.getVal("auth_code"));
				mapResultado.put("createdAtDate", dateString);
				mapResultado.put("createdAtTime", timeString);
				mapResultado.put("name", order.customer_info.name);
				mapResultado.put("type", payment_method.type);
				
//				lstResultado.add(mapResultado);
			}
			
			lstResultado.add(mapResultado);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (io.conekta.ErrorList error) {
			LOGGER.error error.details.get(0).message
			resultado.setSuccess(false);
			resultado.setError(error.details.get(0).message);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		}
		
		
		return resultado
	}
	
	public Result getConektaPublicKey(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List<String> lstResultado = new ArrayList<String>();
		String apiKey = "";
		String campus_id = "";
		
		try{
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			campus_id = object.campus_id;
			def objApiKey = context.getApiClient().getDAO(CatApiKeyDAO.class);
			List<CatApiKey> lstApiKey = objApiKey.find(0, 999);
			
			for(int i = 0; i < lstApiKey.size(); i++) {
				if(campus_id == lstApiKey.get(i).getCampus().getPersistenceId().toString()) {
					apiKey = lstApiKey.get(i).getConektaPublicKey();
					break;
				}
			}
			
			lstResultado.add(apiKey);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
			
			resultado.toString();
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return resultado;
	}
	
	public Result ejecutarEsperarPago(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		List<DetalleSolicitud> lstResultado = new ArrayList<DetalleSolicitud>();
		
		try {
			String username = "";
			String password = "";
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			username = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");

			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			String id = object.data.object.id;
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			def objDetalleSolicitudDAO = context.getApiClient().getDAO(DetalleSolicitudDAO.class);
			List<DetalleSolicitud> detalleSolicitud = objDetalleSolicitudDAO.findByOrdenPago(id, 0, 1);
			apiClient.login(username, password);
			String caseId = detalleSolicitud.get(0).caseId;
			def startedBy = apiClient.getProcessAPI().getProcessInstance(Integer.parseInt(caseId)).startedBy;
			apiClient.processAPI.executeFlowNode(startedBy, apiClient.processAPI.getHumanTaskInstances(Long.valueOf(caseId), "Esperar pago", 0, 1).get(0).getId());
			resultado.setSuccess(true);
		}catch(Exception ex) {
			LOGGER.error ex.getMessage()
			resultado.setSuccess(false)
			resultado.setError(ex.getMessage())
		}
		
		return resultado;
	}
	
	public Result getOrdersByCampus(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		List<Order> lstResultado = new ArrayList<Order>();
		String apiKey = "";
		String campus_id = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			order_id = object.order_id;
			campus_id = object.campus_id;
			
			Result resultApiKey = getApiKeyByCampus(context, campus_id);
			
			if(resultApiKey.success) {
				Conekta.setApiKey(resultApiKey.getData().get(0));
			} else {
				throw new Exception("Error inesperado");
			}
			
			Order order = Order.find();
			LOGGER.error "ORDER INFO : " +  order.toString();
			
			resultado.setSuccess(true);
		}catch(Exception ex) {
			LOGGER.error ex.getMessage()
			resultado.setSuccess(false)
			resultado.setError(ex.getMessage())
		}
	}
	
	public Result crearRegistroPago(OrdenBitacora ordenBit) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();
			con.setAutoCommit(false);
			Date fecha = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:00.000'Z'").parse(ordenBit.getFechaMovimiento());
			pstm = con.prepareStatement(Statements.INSERT_BITACORA_PAGO);
			pstm.setString(1, ordenBit.getEstatus());
			pstm.setString(2, "");
			pstm.setDate(3, convert(fecha));
			pstm.setBoolean(4, false);
			pstm.setString(5, ordenBit.getMedioPago());
			pstm.setString(6, ordenBit.getMonto());
			pstm.setString(7, ordenBit.getNoTransaccion());
			pstm.setString(8, ordenBit.getObservaciones());
			pstm.setString(9, ordenBit.getUsuarioAspirante());
			pstm.setLong(10, 0L);
			pstm.setLong(11, ordenBit.getCaseId());
			pstm.setString(12, ordenBit.getCampus());
			pstm.setString(13, ordenBit.getNombrePago());
			
			pstm.executeUpdate();
			
			con.commit();
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
			LOGGER.error "ERROR EN EL LOGUEO"
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	private static java.sql.Date convert(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}
	//Convert Date to Calendar
	private Calendar dateToCalendar(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
	public Result selectBitacoraPago(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "WHERE isEliminado = false", orderby = "ORDER BY ", errorlog = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			OrdenBitacora row = new OrdenBitacora();
			List<OrdenBitacora> rows = new ArrayList<OrdenBitacora>();
			closeCon = validarConexion();
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "NOTRANSACCION":
						if(where.contains("WHERE")) {
							where+= " AND ";
						} else {
							where+= " WHERE ";
						}
						where +=" LOWER(NOTRANSACCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')";
						}else {
							where+="LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "USUARIOASPIRANTE":
						if(where.contains("WHERE")) {
							where+= " AND ";
						} else {
							where+= " WHERE ";
						}
						where +=" LOWER(USUARIOASPIRANTE) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')";
						}else {
							where+="LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "FECHAMOVIMIENTO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						} else {
							where+= " WHERE ";
						}
						where +=" LOWER(FECHAMOVIMIENTO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')";
						}else {
							where+="LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "MONTO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						} else {
							where+= " WHERE ";
						}
						where +=" LOWER(MONTO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')";
						}else {
							where+="LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "MEDIOPAGO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						} else {
							where+= " WHERE ";
						}
						where +=" LOWER(MEDIOPAGO) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')";
						}else {
							where+="LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "ESTATUS":
						if(where.contains("WHERE")) {
							where+= " AND ";
						} else {
							where+= " WHERE ";
						}
						where +=" LOWER(ESTATUS) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')";
						}else {
							where+="LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "OBSERVACIONES":
						if(where.contains("WHERE")) {
							where+= " AND ";
						} else {
							where+= " WHERE ";
						}
						where +=" LOWER(OBSERVACIONES) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')";
						}else {
							where+="LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "CAMPUS":
						if(where.contains("WHERE")) {
							where+= " AND ";
						} else {
							where+= " WHERE ";
						}
						where +=" LOWER(CAMPUS) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')";
						}else {
							where+="LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
				}
			}
			
			switch(object.orderby) {
				case "NOTRANSACCION":
					orderby += "noTransaccion";
				break;
				case "USUARIOASPIRANTE":
					orderby += "usuarioAspirante";
				break;
				case "FECHAMOVIMIENTO":
					orderby += "fechaMovimiento";
				break; 
				case "MONTO":
					orderby += "monto";
				break; 
				case "MEDIOPAGO":
					orderby += "medioPago";
				break;
				case "ESTATUS":
					orderby += "estatus";
				break;
				case "OBSERVACIONES":
					orderby += "observaciones";
				break;
			}
			
			if(orderby.equals("ORDER BY ")) {
				orderby += "PERSISTENCEID";
			}
			
			orderby += " " + object.orientation;
			
			String consulta = Statements.GET_BITACORA_PAGO;
			consulta = consulta.replace("[WHERE]", where);
			String consultaCount = Statements.GET_COUNT_BITACORA_PAGO;
			consultaCount = consultaCount.replace("[WHERE]", where);
			
			pstm = con.prepareStatement(consultaCount.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
			rs = pstm.executeQuery();
			
			errorlog = consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "");
			
			if(rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();
			
			while(rs.next()){
				row = new OrdenBitacora();
				row.setNoTransaccion(rs.getString("noTransaccion"));
				row.setUsuarioAspirante(rs.getString("usuarioAspirante"));
				row.setFechaMovimiento(rs.getString("fechaMovimiento"));
				row.setMonto(rs.getString("monto"));
				row.setMedioPago(rs.getString("medioPago"));
				row.setEstatus(rs.getString("estatus"));
				row.setObservaciones(rs.getString("observaciones"));
				row.setCaseId(rs.getLong("caseId"));
				row.setNombrePago(rs.getString("nombrePago"))
				
				rows.add(row);
			}
			resultado.setSuccess(true);
			resultado.setError_info(errorlog);
			resultado.setData(rows);
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
}