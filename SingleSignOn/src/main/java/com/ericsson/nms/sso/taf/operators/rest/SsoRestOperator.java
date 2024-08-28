package com.ericsson.nms.sso.taf.operators.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.ericsson.cifwk.taf.RestOperator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tal.rest.*;
import com.ericsson.cifwk.taf.tools.RestTool;
import com.ericsson.nms.sso.taf.handlers.HtmlParser;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;

public class SsoRestOperator implements RestOperator {
	


	public enum Method {
	    GET, POST
	}
	public static final String expectedNullResponse = RestResponseCode.INTERNAL_SERVER_ERROR.toString();
	public static final String expectedPostSuccess = RestResponseCode.OK.toString();
	private static Logger log = Logger.getLogger(SsoRestOperator.class);
	private RestTool restTool;
	
	private List<List<String>> headers;
	private List<String> tokens = new ArrayList<String>();
	private boolean trustSslCertificates=true;
	private boolean followRedirectionsAutomatically=true;
	
	
	private boolean useHttpsIfProvided=true;
	
	private String getAuthCookie(List<String> header){	
		String authCookie="";
		if(header.size()<17) {
			log.error("No Auth in header!:"+header);
			return "NONE";
		}
		String authHeader=header.get(13);
		int beginIndex=authHeader.indexOf("iPlanetDirectoryPro=");
		int endIndex=authHeader.indexOf("; Domain");
		if(beginIndex>0 && endIndex>0 && endIndex-beginIndex > 60) {
			authCookie=authHeader.substring(beginIndex, endIndex);
		} else {
			authCookie="NONE";
			log.error("No Auth Cookie found!");
		}	
		log.debug("Auth Cookie:"+authCookie);
 	   	return authCookie;
	}
	
	/**
	 * Gets response header for last request - singular
	 * @return
	 */
	public List<Long> getExecutionTimes() {
		return restTool.getLastExecutionTimes();
	}

	/**
	 * 
	 * @param response
	 * @return Page HTML Title
	 */
	public String getHTMLTitle(String response) {
		return HtmlParser.getHTMLTitle(response);
	}

	public String getLastAuthCookie(){
		List<String> lastHeader=headers.get(headers.size());
		log.debug("last Header :"+lastHeader);
		return getAuthCookie(lastHeader);
	}

	/**
	 * Gets response header for last request - singular
	 * @return
	 */
	public List<String> getLastRestHeader() {
		log.debug("Location Header"+headers);
		return headers.get(headers.size());
	}

	public boolean isFollowRedirectionsAutomatically() {
		return followRedirectionsAutomatically;
	}

	public boolean isTrustSslCertificates() {
		return trustSslCertificates;
	}
	public boolean isUseHttpsIfProvided() {
		return useHttpsIfProvided;
	}
	
	/**
     * 
     * @param myHost
     * @param uri
     * @return
     */
    public List<String> makeGETRequest(String uri) {
    	return makeRestRequest(Method.GET, uri, null);
    }
    
    /**
     * 
     * @param myHost
     * @param uri
     * @return
     */
    public List<String> makeGETRequest(String uri, List<String> headers ) {
    	log.debug("Headers set"+headers);
    	restTool.setHttpHeaders(headers);
    	return makeRestRequest(Method.GET, uri, null);
    }

    /**
     * 
     * @param myHost
     * @param uri
     * @return
     */
    public List<String> makeGETRequest(String uri, Map<String,String> params ) {
    	return makeRestRequest(Method.GET, uri, params);
    }
    
    /**
     * 
     * @param myHost
     * @param uri
     * @return
     */
    public List<String> makePOSTRequest(String uri, Map<String, String> postParams) {
    	return makeRestRequest(Method.POST, uri, postParams);
    }
    
    private List<String> makeRestRequest(Method method, String uri, Map<String, String> Params){
    	
    	List<String> results;
    	restTool.trustSslCertificates=trustSslCertificates;
    	restTool.followRedirectionsAutomatically=followRedirectionsAutomatically;	
    	restTool.useHttpsIfProvided=useHttpsIfProvided;
    
    	if(method.equals(Method.GET)) {
    		results= restTool.get(uri, Params);
    	} else {
    		results= restTool.post(uri, Params);
    	}
    	headers = restTool.getLastResponseHeaders();
    	log.info("Number of response strings: "+results.size());
    	log.info("Number of response headers: "+headers.size());
    	log.info("Average Response time: "+restTool.getAverageExecutionTime());
    	return results;
    }
    
    
    public void setFollowRedirectionsAutomatically(
			boolean followRedirectionsAutomatically) {
		this.followRedirectionsAutomatically = followRedirectionsAutomatically;
	}
    public Map<String, String> setOpenAMAuthenticateParams(String user, String pass) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", user);
        params.put("password",pass);
		return params;
	}
    
	public Map<String, String> setOpenAMLogoutParams(String token) {
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("subjectid", token);
		return params;
	}
	
	public void setTrustSslCertificates(boolean trustSslCertificates) {
		this.trustSslCertificates = trustSslCertificates;
	}
	
	public void setup(Host host) {
		restTool= new RestTool(host);
	}

	public void setUseHttpsIfProvided(boolean useHttpsIfProvided) {
		this.useHttpsIfProvided = useHttpsIfProvided;
	}
	
	public Map<String, String> setWebAuthenticateParams(String user, String pass) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("IDToken1", user);
        params.put("IDToken2",pass);
		return params;
	}

	public boolean verifyAuthTokenPresent() {
		boolean result=true;
		int good=0;
		int count = 0;
		for (List<String> header : headers ) {
			String auth=getAuthCookie(header);
	        if(auth.equals("NONE")) {
	        	log.error("Count:"+count+" Auth: " +auth);
	        	result=false;
	        } else {
	        	log.debug("Count:"+count+" Auth: "+auth);  
	        	good++;
	        }
	        count++;
		}
		log.info("Number of valid Auth headers:"+good);
		return result;
	}

	public boolean verifyLauncherLoginPageTitle(List<String> htmlPage) {
		return verifyPageTitle(SsoTestDataProvider.getLauncherLoginPageTitle(), htmlPage);
	}
	
	public boolean verifyLauncherPageTitle(List<String> htmlPage) {
		return verifyPageTitle(SsoTestDataProvider.getLauncherPageTitle(), htmlPage);
	}
	
	
	private boolean verifyPageTitle(String pageTitle, List<String> htmlPage) {
		boolean result=true;
		int count = 0;
		for (String page : htmlPage ) {
			String title=getHTMLTitle(page);
	        if(title.equals(pageTitle)) {
	        	log.debug("Count:"+count+" Title: " + title);
	        } else {
	        	log.error("Count:"+count+" Title: "+title+" does not equal: "+pageTitle);
	        	result=false;
	        }
	        count++;
		}
		return result;
	}
	
	public boolean verifyTokenPresent(List<String> tokenList) {
		boolean result=true;
		int count = 0;
		for (String token : tokenList ) {
			token=token.replace("\n", "");
	        if(token.contains("token.id=") && token.split("token.id=").length > 0) {       	
	        	log.debug("Count:"+count+" TOKEN : " + token);
	        	tokens.add(token.substring(token.indexOf("=")+1));
	        } else {
	        	log.error("Count:"+count+" TOKEN : " + token);
	        	result=false;
	        }
	        count++;
		}
		return result;
	}

	public List<String> getTokens() {
		return tokens;
	}


	
    

}
