package com.rest.business;

import java.sql.SQLException;
import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rest.dao.ItemDataAccess;
import com.rest.dao.ItemDataAccessImpl;
import com.rest.inventory.entity.ItemInventory;
import com.rest.inventory.entity.ItemInventoryInfo;
import com.rest.item.entity.ItemList;
import com.rest.response.Response;


public class ItemServiceImpl implements ItemService {
	
	private final static Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
	
	ItemDataAccess itemDataAccess=new ItemDataAccessImpl();
	
	public ItemServiceImpl() {
		super();
	}
	
	public ItemInventory getItems(String locale) throws SQLException {
		ItemInventory itemInventory=new ItemInventory();
		itemInventory=itemDataAccess.getItems(locale);
		if(itemInventory.getItemInventoryInfo().size() < 0) {
			ItemInventoryInfo itemInventoryInfo=new ItemInventoryInfo();			
			itemInventoryInfo.setResponse("No records in the database");
			itemInventory.getItemInventoryInfo().add(itemInventoryInfo);			
		}
		return itemInventory;
	}
	
	public ItemInventoryInfo getItemInfo(String id) throws SQLException {		
		ItemInventoryInfo itemInventoryInfo=new ItemInventoryInfo();
		itemInventoryInfo=itemDataAccess.getItemsById(id);		
		if(( itemInventoryInfo.getItemId() == null)) {			
			itemInventoryInfo.setResponse("No records in the database");
		}		
		return itemInventoryInfo;		
	}
	
	public Response createOrUpdateItem(JAXBElement<ItemList> items) throws SQLException {
	    Response itemResponses=new Response();		
	    itemResponses=itemDataAccess.createOrupdateItem(items,itemResponses);		
	    return itemResponses;	    
	}	
}
