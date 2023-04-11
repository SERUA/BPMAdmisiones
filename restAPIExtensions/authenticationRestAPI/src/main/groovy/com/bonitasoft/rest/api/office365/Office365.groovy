package com.bonitasoft.rest.api.office365

import com.microsoft.aad.msal4j.AuthorizationCodeParameters
import com.microsoft.aad.msal4j.AuthorizationRequestUrlParameters
import com.microsoft.aad.msal4j.ClientCredentialFactory
import com.microsoft.aad.msal4j.ClientCredentialParameters
import com.microsoft.aad.msal4j.ConfidentialClientApplication
import com.microsoft.aad.msal4j.IAuthenticationResult
import com.microsoft.aad.msal4j.IClientCredential
import com.microsoft.aad.msal4j.IClientSecret
import com.microsoft.aad.msal4j.Prompt
import com.microsoft.aad.msal4j.ResponseMode

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import java.security.spec.PKCS8EncodedKeySpec
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import java.util.stream.Collectors

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

import org.bonitasoft.console.common.server.utils.PermissionsBuilder
import org.bonitasoft.console.common.server.utils.PermissionsBuilderAccessor
import org.bonitasoft.console.common.server.utils.SessionUtil
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.LoginAPI
import org.bonitasoft.engine.api.ProfileAPI
import org.bonitasoft.engine.identity.ContactDataCreator
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserCreator
import org.bonitasoft.engine.identity.UserNotFoundException
import org.bonitasoft.engine.identity.UserUpdater
import org.bonitasoft.engine.profile.Profile
import org.bonitasoft.engine.profile.ProfileMemberCreator
import org.bonitasoft.engine.profile.ProfileSearchDescriptor
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.bonitasoft.engine.session.APISession
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder

class Office365 implements RestApiController {
	private static Properties props = null;
	
	private static final String PWD_PREFIX = "pWdSocPre_"
	
	private static final String PROFILE="User";
	
	private static String authority;
	private static String clientId;
	private static String scope;
	private static String keyPath;
	private static String certPath;
	private static String SECRET;
	private static String REDIRECT_URI;
	private static ConfidentialClientApplication app;

	@Override
	public RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder response, RestAPIContext context) {
		String password =""
		try {
		// Load properties file and set properties used throughout the sample
		if (props==null) {
			props = loadProperties("configuration.properties", context.resourceProvider);
		}
		authority = props.AUTHORITY
		clientId = props.CLIENT_ID
		keyPath = props.KEY_PATH
		certPath = props.CERT_PATH
		scope = props.SCOPE
		SECRET= props.SECRET
		REDIRECT_URI=props.REDIRECT_URI
		
		try {
			BuildConfidentialClientObject(SECRET)
		} catch (Exception e) {
			return buildResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"""{"error" : "BuildConfidentialClientObject", "details":"${e.message}"}""")
		}
		
		def redirect = request.getParameter("redirect");
		if (redirect == null) {
			 return buildResponse(response, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter redirect is missing"}""")
		}
		
		def authCode = request.getParameter("authCode");
		if (authCode == null) {
			
			 return buildResponse(response, HttpServletResponse.SC_OK,"""{"redirectUrl" : "${generateUrl()+"&redirectUrl="+redirect}"}""")
		}
		
		
		def authResult = null
		try {
			AuthorizationCodeParameters authParams = AuthorizationCodeParameters
			.builder(authCode, new URI(REDIRECT_URI))
			.codeVerifier("Ie3Jah4INMzqV2_eC0SRjqBOOfzvSUWEth4RhTnNpsSoIUwfpfEKrMqV6_W6U3GUuijZubukho-tO2YaAqD0XbB3L8rDfiTpKTsPSTD8qCJgsd1L1LOtR6GWWR3e7K2r")
			.scopes(Collections.singleton(scope)).build()
			Future<IAuthenticationResult> future = app.acquireToken(authParams)
			authResult = future.get()
		}catch(Exception e) {
			return buildResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"""{"error" : "AuthorizationCodeParameters", "details":"${e.message}","authCode":"${authCode}", "s":"${SECRET}","u":"${REDIRECT_URI}", "c":"${clientId}","rediruri":"${redirect}","generatedURL":"${generateUrl()}"}""")
		}
		
		
		
		/*def result = null
		try {
			result = getAccessTokenByClientCredentialGrant()
		} catch (Exception e) {
			return buildResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"""{"error" : "getAccessTokenByClientCredentialGrant", "details":"${e.message}"}""")
		}*/
		
		
		def user= null 
		
		try {
			user = getUsersListFromGraph(authResult.accessToken())
		} catch (Exception e) {
			return buildResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"""{"error" : "https://graph.microsoft.com/v1.0/me", "details":"${e.message}"}""")
		}
		
		String email = user.mail;
		String familyName = user.surname
		String givenName = user.givenName
		
		IdentityAPI identityApi = context.getApiClient().getIdentityAPI();
		ProfileAPI profileApi = context.getApiClient().getProfileAPI();
		LoginAPI loginAPI = context.getApiClient().getLoginAPI();
		try {
			User u = identityApi.getUserByUserName(email);
			
			
			UserUpdater uu = new UserUpdater()
			uu.setPassword(generatePwd(email, givenName))
			identityApi.updateUser(u.getId(), uu)
		} catch(UserNotFoundException e) {
			//user doesn't exist we create him
			try {
			UserCreator uc = new UserCreator(email, generatePwd(email, givenName))
			uc.setFirstName(givenName)
			uc.setLastName(familyName)
			uc.setEnabled(true)
			ContactDataCreator creator = new ContactDataCreator()
			creator.setEmail(email)
			uc.setPersonalContactData(creator)
			User userCreated = identityApi.createUser(uc);
			
			SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0,1);
			searchOptionsBuilder.filter(ProfileSearchDescriptor.NAME, PROFILE);
			SearchResult<Profile> searchResultProfile = profileApi.searchProfiles(searchOptionsBuilder.done());
			
			ProfileMemberCreator profileMemberCreator = new ProfileMemberCreator( searchResultProfile.getResult().get(0).getId() );
			profileMemberCreator.setUserId( userCreated.getId() );
			profileApi.createProfileMember(profileMemberCreator);
			}
			catch(Exception e1) {
				return buildResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"{/\"error\" : \" "+e1.getMessage()+"\"}")
			}
		}
		
		
		
		HttpSession httpSession = request.getSession();
		password = generatePwd(email, givenName)
		APISession userApiSession = loginAPI.login(email, generatePwd(email, givenName));
		httpSession.setAttribute(SessionUtil.API_SESSION_PARAM_KEY, userApiSession);
		PermissionsBuilder permissionsBuilder = PermissionsBuilderAccessor.createPermissionBuilder(userApiSession);
		def permissions = permissionsBuilder.getPermissions();
		httpSession.setAttribute(SessionUtil.PERMISSIONS_SESSION_PARAM_KEY, permissions);

		def result=["status":"ok", "userId" : userApiSession.getUserId(), "redirect" : "private"]
	
		if (redirect != null) {
			return response.with {
				withResponseStatus(HttpServletResponse.SC_MOVED_TEMPORARILY)
				withResponse(new JsonBuilder(result).toString())
				withAdditionalHeader("Location", redirect)
				build()
			}
		}
		return buildResponse(response, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
		}catch(Exception e) {
			return buildResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"""{"error" : "${e.getMessage()}", "pass":"${password}"}""")
		}
	}
	private String generatePwd(String email, String lastname) {
		return "Av#"+(PWD_PREFIX+email+lastname).hashCode();
	}
	
	RestApiResponse buildResponse(RestApiResponseBuilder responseBuilder, int httpStatus, Serializable body) {
		return responseBuilder.with {
			withResponseStatus(httpStatus)
			withResponse(body)
			build()
		}
	}

	/**
	 * Load a property file into a java.util.Properties
	 */
	Properties loadProperties(String fileName, ResourceProvider resourceProvider) {
		Properties props = new Properties()
		resourceProvider.getResourceAsStream(fileName).withStream { InputStream s ->
			props.load s
		}
		props
	}
	
	private static void BuildConfidentialClientObject(String secret) throws Exception {
		


	 app = ConfidentialClientApplication.builder(
			 clientId,
			 ClientCredentialFactory.createFromSecret(secret))
			 .authority(authority)
			 .build();
	}
	private static IAuthenticationResult getAccessTokenByClientCredentialGrant() throws Exception {

		
		// With client credentials flows the scope is ALWAYS of the shape "resource/.default", as the
		// application permissions need to be set statically (in the portal), and then granted by a tenant administrator

		ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
				Collections.singleton(scope))
				.build();

		CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
		return future.get();
	}

	private static Object getUsersListFromGraph(String accessToken) throws IOException {
		URL url = new URL("https://graph.microsoft.com/v1.0/me");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		conn.setRequestProperty("Accept","application/json");

		int httpResponseCode = conn.getResponseCode();
		if(httpResponseCode == HttpServletResponse.SC_OK) {
			BufferedReader br = null;
			if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			
			def jsonText = new JsonSlurper().parseText(br.lines().collect(Collectors.joining()))
			return jsonText
		}else {
			return new JsonSlurper().parseText("""{ "status":"${httpResponseCode}"}, "accessToken": "${accessToken}" """)
		}


	}
	
	private String generateUrl() {
		 AuthorizationRequestUrlParameters parameters = AuthorizationRequestUrlParameters
			.builder(REDIRECT_URI, Collections.singleton("openid profile offline_access")).responseMode(ResponseMode.QUERY)
			.codeChallenge("UTL7a74nOArE-tYYoLAltpcydOnOtQWV6k2lVQwjLZw")
			.codeChallengeMethod("S256")
			.prompt(Prompt.SELECT_ACCOUNT).build();
		
		 String redirectUrl = app.getAuthorizationRequestUrl(parameters).toString();
		return redirectUrl
	}

	
}
