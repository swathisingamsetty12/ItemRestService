package com.rest.service;

import java.io.IOException;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;
import com.rest.response.Response;
import com.rest.inventory.entity.ItemInventory;
import com.rest.inventory.entity.ItemInventoryInfo;
import com.rest.item.entity.ItemList;
import com.rest.business.ItemService;
import com.rest.business.ItemServiceImpl;

@Path("/items")
public class ItemRestService {

  @Context
  HttpHeaders headers;
  
  // Return the list of items
  @GET 
  @Produces(MediaType.APPLICATION_JSON)
  public ItemInventory getItems() throws SQLException {
	  String locale=headers.getAcceptableLanguages().get(0).toString();
	  ItemService itemService=new ItemServiceImpl();
	  return itemService.getItems(locale); 
  }
  
  @GET 
  @Path("{itemIdToRetrieve}")
  @Produces(MediaType.APPLICATION_JSON)
  public ItemInventoryInfo getByItemDetails(@PathParam("itemIdToRetrieve") String id) throws SQLException {
	  ItemService itemService=new ItemServiceImpl();
    return itemService.getItemInfo(id); 
  } 
 
  //Creates an item
  @POST
  @Path("/createItem")
  @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
  @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
  public Response createItem(JAXBElement<ItemList> items) throws IOException, SQLException {	
	  ItemService itemService=new ItemServiceImpl();
	  return itemService.createOrUpdateItem(items);   
  } 
  
  //Deletes an single item 
  @DELETE
  @Path("{itemToDelete}")
  public void deleteTodo(@PathParam("itemToDelete") String id) {
	  ItemService itemService=new ItemServiceImpl();
	  itemService.deleteItem(id);   
  }
  
  //Creates or updates an item
  @PUT
  @Path("/createItem")
  @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
  @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
  public Response updateItem(JAXBElement<ItemList> items) throws SQLException {	  
	  ItemService itemService=new ItemServiceImpl();
	  return itemService.createOrUpdateItem(items); 
  }  
}
  
