package com.rest.business;

import java.sql.SQLException;
import javax.xml.bind.JAXBElement;
import com.rest.dao.ItemDataAccess;
import com.rest.dao.ItemDataAccessImpl;
import com.rest.inventory.entity.ItemInventory;
import com.rest.inventory.entity.ItemInventoryInfo;
import com.rest.item.entity.ItemList;
import com.rest.response.Response;


public class ItemServiceImpl implements ItemService {
	
	ItemDataAccess itemDataAccess=new ItemDataAccessImpl();
	
	public ItemServiceImpl() {
		super();
	}
	
	public ItemInventoryInfo getItemInfo(String id) throws SQLException {		
		return itemDataAccess.getItemsById(id);
	}
	
	public Response createOrUpdateItem(JAXBElement<ItemList> items) throws SQLException {
	    Response itemResponses=new Response();		
	    itemResponses=itemDataAccess.createOrupdateItem(items,itemResponses);		
	    return itemResponses;	    
	}	
	
	public void deleteItem(String id) {
		itemDataAccess.deleteItem(id);
	}

	public ItemInventory getItems(String locale) throws SQLException {
		return itemDataAccess.getItems(locale);
	}	
}
