package com.rest.dao;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBElement;
import com.rest.inventory.entity.Inventory;
import com.rest.inventory.entity.ItemInventory;
import com.rest.inventory.entity.ItemInventoryInfo;
import com.rest.item.entity.ItemInfo;
import com.rest.item.entity.ItemList;
import com.rest.provider.ItemProvider;
import com.rest.response.Response;

public class ItemDataAccessImpl implements ItemDataAccess {
	
	public ItemInventory getItems(String locale) throws SQLException {		
		Set<String> itemIds=ItemProvider.getInstance().getModel().keySet();		
		Iterator<String> itemIdsIterator=itemIds.iterator();		
		ItemInventory itemInventoryList=new ItemInventory();
		while(itemIdsIterator.hasNext()) {		
			String itemId=itemIdsIterator.next();
			ItemInventoryInfo itemInventoryInfo=new ItemInventoryInfo();	
			itemInventoryInfo=getItemsById(itemId);			
			itemInventoryInfo.setLocale(locale);
			itemInventoryList.getItemInventoryInfo().add(itemInventoryInfo);
		}		
		return itemInventoryList;		
	}
	
	public ItemInventoryInfo getItemsById(String id) {		
		ItemInventoryInfo itemInventoryInfo=new ItemInventoryInfo();		
		List<Inventory> inventoryList=new ArrayList<Inventory>();
		List<Map<String, ItemInfo>> storeInventoryList=ItemProvider.getInstance().getModel().get(id);
		Iterator<Map<String, ItemInfo>> storeInventoryListIterator=storeInventoryList.iterator();		
		Map<String, ItemInfo> storeByItem=new HashMap<String, ItemInfo>();
		Inventory inventory=null;
		ItemInfo storeItem = new ItemInfo();
		String storeId = null;
		
		while(storeInventoryListIterator.hasNext()) {		
			storeByItem=storeInventoryListIterator.next();			
			inventory=new Inventory();				
			Iterator<String> storeByItemIterator = storeByItem.keySet().iterator();   
			
			while (storeByItemIterator.hasNext()) {			
				storeId = storeByItemIterator.next();				
				storeItem = storeByItem.get(storeId);				
			}				
			inventory.setQuantity(storeItem.getQuantity());
			inventory.setStoreId(storeId);				
			inventory.setDescription(storeItem.getDescription());			
			inventoryList.add(inventory);			
		}		
		itemInventoryInfo.setItemId(id);
		itemInventoryInfo.setPrice(storeItem.getPrice());		
		itemInventoryInfo.setInventoryInfo(inventoryList);		
		return itemInventoryInfo;
	}		
	
	public Response createOrupdateItem(JAXBElement<ItemList> items, Response itemResponse) throws SQLException {		
		Map<String, List<Map<String, ItemInfo>>> itemByStoreInventoryList=ItemProvider.getInstance().getModel();
		List<Map<String, ItemInfo>> storeInventoryList=new ArrayList<Map<String,ItemInfo>>();		
		Map<String, ItemInfo> storeInventory=new HashMap<String, ItemInfo>();
		List<ItemInfo> itemList =items.getValue().getItemInfo();
		Response response=new Response();
		
		for(ItemInfo item:itemList) {			
			if(itemByStoreInventoryList.containsKey(item.getItemId())) {				
				Map<String, ItemInfo> itemBystoreInventory=new HashMap<String, ItemInfo>();
				boolean storeExists=false;
				int index =0;
				storeInventoryList=itemByStoreInventoryList.get(item.getItemId());
				Iterator<Map<String, ItemInfo>> iterator=storeInventoryList.iterator();			
				
				while(iterator.hasNext()) {					
					itemBystoreInventory=iterator.next();
					if(itemBystoreInventory.containsKey(item.getStoreId())) {
						storeExists=true;					
						ItemInfo storeItem=itemBystoreInventory.get(item.getStoreId());
						storeItem.setQuantity(BigInteger.valueOf((storeItem.getQuantity().intValue()+item.getQuantity().intValue())));
						itemBystoreInventory.put(item.getStoreId(), storeItem);
						storeInventoryList.set(index, itemBystoreInventory);						
						itemByStoreInventoryList.put(item.getItemId(), storeInventoryList);						
						break;
					}
					index++;
				}
				if(!storeExists) {
					Map<String, ItemInfo> itemNewstoreInventory=new HashMap<String, ItemInfo>();				
					itemNewstoreInventory.put(item.getStoreId(), item);
					storeInventoryList.add(itemNewstoreInventory);
					itemByStoreInventoryList.put(item.getItemId(), storeInventoryList);
				}			
				response.getItemId().add(item.getItemId());			
		    } else {
		    	storeInventory.put(item.getStoreId(), item);
		    	storeInventoryList.add(storeInventory);
		    	ItemProvider.getInstance().getModel().put(item.getItemId(), storeInventoryList);
		    }
		}		
		if(response.getItemId().isEmpty()) {
			response.getItemId().add("ALL items are saved");    	
	    }
		else
			response.getItemId().add(" items are updated");
		return response;
	}
	
	public void deleteItem(String id) {
		if(ItemProvider.getInstance().getModel().containsKey(String.valueOf(id))) {
			ItemProvider.getInstance().getModel().remove(id);
		}
		else
			throw new RuntimeException("Delete: Item with " + id +  " not found");
	}	
}
