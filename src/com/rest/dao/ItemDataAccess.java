package com.rest.dao;

import java.sql.SQLException;
import javax.xml.bind.JAXBElement;
import com.rest.inventory.entity.ItemInventory;
import com.rest.inventory.entity.ItemInventoryInfo;
import com.rest.item.entity.ItemList;
import com.rest.response.Response;

public interface ItemDataAccess {	
	public ItemInventory getItems(String locale) throws SQLException;	
	public ItemInventoryInfo getItemsById(String id) ;	
	public Response createOrupdateItem(JAXBElement<ItemList> items, Response itemResponse) throws SQLException ;	
}
