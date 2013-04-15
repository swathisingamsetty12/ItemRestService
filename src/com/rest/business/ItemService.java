package com.rest.business;

import java.sql.SQLException;
import javax.xml.bind.JAXBElement;
import com.rest.dao.ItemDataAccess;
import com.rest.inventory.entity.ItemInventory;
import com.rest.inventory.entity.ItemInventoryInfo;
import com.rest.item.entity.ItemList;
import com.rest.response.Response;


public interface ItemService {
		
	public ItemInventoryInfo getItemInfo(String id) throws SQLException ;
	
	public Response createOrUpdateItem(JAXBElement<ItemList> items) throws SQLException;	
	
	public void deleteItem(String id) ;

	public ItemInventory getItems(String locale) throws SQLException ;
}
