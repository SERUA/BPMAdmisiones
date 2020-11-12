package com.anahuac.rest.api.DAO

import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.hamcrest.core.IsNull
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

class ConektaDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConektaDAO.class);

    public Result pagoOxxoCash(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		LOGGER.error "entre"
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
		Conekta.setApiKey("key_dHs4rzK9otyxcbqTrzn5sw");
		String token = "";
		String name = "";
		String email = "";
		String phone = "";
		String street1 = "";
		String postal_code = "";
		String country = "";
		LOGGER.error "unit_price"
		String unit_price = "";
		
        try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			name = object.name;
			email = object.email;
			phone = object.phone;
			street1 = object.street1;
			postal_code = object.postal_code;
			country = object.country;
			LOGGER.error object.toString();
			unit_price = object.unit_price;
			
			
			Order order = Order.create(
				new JSONObject("{"
					+ "'line_items': [{"
						+ "'name': 'Admisión',"
						+ "'unit_price': " + unit_price + ","
						+ "'quantity': 1"
					+ "}],"
					+ "'shipping_lines': [{"
						+ "'amount': 1500,"
						+ "'carrier': 'FEDEX'"
					+ "}],"
					+ "'currency': 'MXN',"
					+ "'customer_info': {"
						+ "'name': '" + name + "',"
						+ "'email': '" + email + "',"
						+ "'phone': '" + phone + "'"
					+ "},"
					+ "'shipping_contact':{"
						+ "'address': {"
							+ "'street1': '" + street1 + "',"
							+ "'postal_code': '" + postal_code + "',"
							+ "'country': '" + country + "'"
						+ "}"
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
			LOGGER.error "pase order"
			
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
        } catch (Exception e) {
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            LOGGER.error "ERROR=================================";
            LOGGER.error e.getMessage();
            e.printStackTrace();
        } catch (io.conekta.ErrorList error) {
			LOGGER.error error.details.get(0).message
		} 
		
        return resultado
    }
	
	public Result pagoTarjeta(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List<ConektaPaymentResponse> lstResultado = new ArrayList<ConektaPaymentResponse>();
		Conekta.setApiKey("key_dHs4rzK9otyxcbqTrzn5sw");
		String token = "";
		String name = "";
		String email = "";
		String phone = "";
		String street1 = "";
		String postal_code = "";
		String country = "";
		String unit_price = "";

		try{
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			token = object.idToken;
			email = object.email;
			phone = object.phone;
			name = object.name;
			street1 = object.street1;
			postal_code = object.postal_code;
			country = object.country;
			unit_price = object.unit_price;
			
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
		  
		}catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		    System.out.println(e.getMessage());
		} catch (io.conekta.ErrorList error) {
			LOGGER.error error.details.get(0).message
		} 
		return resultado;
	
	}
	
	public Result pagoSPEI(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		List<Map<String, String>> lstResultado = new ArrayList<Map<String, String>>();
		Conekta.setApiKey("key_dHs4rzK9otyxcbqTrzn5sw");
		Long nowUnixTimestamp = System.currentTimeMillis();
		Long thirtyDaysFromNowUnixTimestamp =  (nowUnixTimestamp + 30L * 24 * 60 * 60 * 1000) / 1000L;
		String thirtyDaysFromNow = thirtyDaysFromNowUnixTimestamp.toString();
		String token = "";
		String name = "";
		String email = "";
		String phone = "";
		String street1 = "";
		String postal_code = "";
		String country = "";
		String unit_price = "";
		
		try{
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			name = object.name;
			email = object.email;
			phone = object.phone;
			street1 = object.street1;
			postal_code = object.postal_code;
			country = object.country;
			unit_price = object.unit_price;
			Order order = Order.create(
				new JSONObject("{"
					+ "'line_items': [{"
						+ "'name': 'Admisión',"
						+ "'unit_price': " + unit_price + ","
						+ "'quantity': 1"
					+ "}],"
					+ "'shipping_lines': [{"
						+ "'amount': 1500,"
						+ "'carrier': 'FEDEX'"
					+ "}],"
					+ "'currency': 'MXN',"
					+ "'customer_info': {"
						+ "'name': '" + name + "',"
						+ "'email': '" + email + "',"
						+ "'phone': '" + phone + "'"
					+ "},"
					+ "'shipping_contact':{"
						+ "'address': {"
							+ "'street1': '" + street1 + "',"
							+ "'postal_code': '" + postal_code + "',"
							+ "'country': '" + country + "'" 
						+ "}"
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
}