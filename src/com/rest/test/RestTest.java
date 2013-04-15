package com.rest.test;

import static org.junit.Assert.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.junit.Test;
import com.rest.inventory.entity.Inventory;
import com.rest.inventory.entity.ItemInventory;
import com.rest.inventory.entity.ItemInventoryInfo;
import com.rest.item.entity.ItemInfo;
import com.rest.item.entity.ItemList;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class RestTest {

	@Test
	public void testGetResponseStausCode() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		try {
			HttpGet httpget = new HttpGet(
					"http://wltest.sstech.cloudbees.net/services/items");
			HttpContext localContext = new BasicHttpContext();
			HttpResponse response = httpclient.execute(httpget, localContext);
			assertEquals(HttpStatus.SC_OK, response.getStatusLine()
					.getStatusCode());
		} catch (Exception e) {
			fail("unexpected exception has happened " + e.getMessage());
			e.printStackTrace();
		}
	}	

	@Test
	public void testGetResponseContentTypeIsJson() throws ClientProtocolException,
			IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();	
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet(
				"http://wltest.sstech.cloudbees.net/services/items");		
		HttpResponse response = httpclient.execute(request);		
		@SuppressWarnings("deprecation")
		String mimeType = EntityUtils.getContentMimeType(response.getEntity());
		assertEquals(jsonMimeType, mimeType);
	}

	@Test
	public void testGetResponsePayloadIsJson() throws ClientProtocolException,
			IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();			
		HttpUriRequest request = new HttpGet(
				"http://wltest.sstech.cloudbees.net/services/items");
		request.setHeader("Accept", "application/json");		
		HttpResponse response = httpclient.execute(request);				
		JsonFactory jfactory = new JsonFactory();
		JsonParser jParser = jfactory.createJsonParser(EntityUtils.toString(response.getEntity()));			
		
		ItemInventory itemInvExpected=new ItemInventory();
		ItemInventoryInfo itemInvInfo=new ItemInventoryInfo();		
		itemInvInfo.setItemId("2");
		itemInvInfo.setLocale("en_US");
		itemInvInfo.setPrice(10.00f);
		List<Inventory> inventoryInfo=new ArrayList<Inventory>();
		Inventory inv=new Inventory();inv.setStoreId("9001");inv.setQuantity(BigInteger.valueOf(5l));inv.setDescription("This top color is Pink");
		inventoryInfo.add(inv);
		itemInvInfo.setInventoryInfo(inventoryInfo);		
		itemInvExpected.getItemInventoryInfo().add(itemInvInfo);		

		while (jParser.nextToken() != JsonToken.END_OBJECT) {			 
			String fieldname = jParser.getCurrentName();			
			if ("itemId".equals(fieldname)) {	 
			  // current token is "itemId", move to next, which is "itemId"'s value
			  jParser.nextToken();
			  assertEquals(itemInvExpected.getItemInventoryInfo().get(0).getItemId(),jParser.getText());	 
			}
		  }
		jParser.close();
	}
	
	@Test
	public void testPutRequest() throws ClientProtocolException,
			IOException {
					
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service=client.resource(UriBuilder.fromUri("http://wltest.sstech.cloudbees.net/services/items/createItem").build());
		
		ItemInfo item1 = new ItemInfo("4", "Jacket", "9002", "Sunnyvale", 30.00,"This jacket is something special.", BigInteger.valueOf(5l));
		ItemList itemList=new ItemList();
		itemList.getItemInfo().add(item1);	
		
		ClientResponse clientResponse =service.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, itemList);				
		assertEquals(200,clientResponse.getStatus());
	}
	
	@Test
	public void testCreateRequest() throws ClientProtocolException,
			IOException {
					
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service=client.resource(UriBuilder.fromUri("http://wltest.sstech.cloudbees.net/services/items/createItem").build());
		
		ItemInfo item1 = new ItemInfo("5", "Jacket", "9002", "Sunnyvale", 30.00,"This jacket is something special.", BigInteger.valueOf(5l));
		ItemList itemList=new ItemList();
		itemList.getItemInfo().add(item1);	
		
		ClientResponse clientResponse =service.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, itemList);				
		assertEquals(200,clientResponse.getStatus());
	}
	
}
