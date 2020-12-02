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
import com.anahuac.rest.api.Entity.ConektaOxxo
import com.anahuac.rest.api.Entity.ConektaPaymentResponse
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
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class ConektaDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConektaDAO.class);

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
//		getApiKeyByCampus(context, "2");
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
        try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			name = object.name;
			email = object.email;
			phone = object.phone;
			unit_price = object.unit_price;
			campus_id = object.campus_id;
			
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

		try{
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			token = object.idToken;
			email = object.email;
			phone = object.phone;
			name = object.name;
			unit_price = object.unit_price;
			campus_id = object.campus_id;
			
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
			lstResultado.add(new ConektaPaymentResponse(order.id, "" + (order.amount/100) + order.currency, line_item.quantity + " - "
				+ line_item.name + " - "
				+ (line_item.unit_price/100), payment_method.getVal("auth_code")+"", payment_method.getVal("name") + " - "
				+ payment_method.getVal("last4") + " - "
				+ payment_method.getVal("brand") + " - "
				+ payment_method.getVal("type")));
			resultado.setData(lstResultado)
			resultado.setSuccess(true)
		  
		} catch (io.conekta.ErrorList error) {
			LOGGER.error error.details.get(0).message
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
	
	public Result pagoSPEI(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		
		Result resultado = new Result();
		List<Map<String, String>> lstResultado = new ArrayList<Map<String, String>>();
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
		
		try{
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			name = object.name;
			email = object.email;
			phone = object.phone;
			unit_price = object.unit_price;
			campus_id = object.campus_id;
			
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
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			String id = object.data.object.id;
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			def objDetalleSolicitudDAO = context.getApiClient().getDAO(DetalleSolicitudDAO.class);
			List<DetalleSolicitud> detalleSolicitud = objDetalleSolicitudDAO.findByOrdenPago(id, 0, 1);
			apiClient.login("Administrador", "bpm");
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
}