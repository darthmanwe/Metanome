package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.List;

import org.junit.Test;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterServiceAsync;

/**
 * Tests RPC calls to server
 */
public class ServiceTest extends GWTTestCase {

	@Test
	public void testParameterService(){
		//Setup
		AsyncCallback<List<InputParameter>> callback = new AsyncCallback<List<InputParameter>>() {
		      public void onFailure(Throwable caught) {
		    	  fail();
		      }

		      public void onSuccess(List<InputParameter> result) {  	  
		    	  assertNotNull(result);
		    	  finishTest();
		      }
		    };

		  ParameterServiceAsync parameterService = GWT.create(ParameterService.class);
		  
		  // Set a delay period
		  delayTestFinish(500);

		  parameterService.retrieveParameters("IND", "spider", callback);

	}
	
	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}
}
