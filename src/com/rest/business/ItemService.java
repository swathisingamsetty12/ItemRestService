package com.rest.business;

import java.sql.SQLException;
import javax.xml.bind.JAXBElement;
import com.rest.inventory.entity.ItemInventory;
import com.rest.inventory.entity.ItemInventoryInfo;
import com.rest.item.entity.ItemList;
import com.rest.response.Response;


public interface ItemService {		
	public ItemInventory getItems(String locale) throws SQLException;
	public ItemInventoryInfo getItemInfo(String id) throws SQLException ;	
	public Response createOrUpdateItem(JAXBElement<ItemList> items) throws SQLException;	
}
