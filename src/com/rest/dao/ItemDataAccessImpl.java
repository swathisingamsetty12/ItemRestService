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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemDataAccessImpl implements ItemDataAccess {
	
	private final static Logger logger = LoggerFactory.getLogger(ItemDataAccessImpl.class);
	
	public ItemInventory getItems(String locale) throws SQLException {	
		ItemInventory itemInventoryList=new ItemInventory();		
		Map<String, List<Map<String, ItemInfo>>> itemByStoreDetails=ItemProvider.getInstance().getModel();
		
		if(itemByStoreDetails != null && !itemByStoreDetails.isEmpty()) {			
			Set<String> itemIds=itemByStoreDetails.keySet();		
			Iterator<String> itemIdsIterator=((itemIds != null && !itemIds.isEmpty())?itemIds.iterator():null);			
			logger.info(" itemIdsIterator is not null");
			while(itemIdsIterator.hasNext()) {				
				String itemId=itemIdsIterator.next();				
				if(itemId !=null && !itemId.isEmpty()) {
					ItemInventoryInfo itemInventoryInfo=new ItemInventoryInfo();	
					itemInventoryInfo=getItemsById(itemId);
					if(itemInventoryInfo.getItemId() != null && !itemInventoryInfo.getItemId().isEmpty()) {						
						itemInventoryInfo.setLocale(locale);
						itemInventoryList.getItemInventoryInfo().add(itemInventoryInfo);
					}
					else {						
						logger.info("No data for the itemId :"+itemId);
					}
				}		
				else
					logger.info("Item Id is empty");	
			}
		}
		else
			logger.info("No data in the database");					
		return itemInventoryList;		
	}
	
	public ItemInventoryInfo getItemsById(String id) {	
		ItemInventoryInfo itemInventoryInfo=new ItemInventoryInfo();
		if (id == null) {	
			logger.warn(" Trying to get the data for an null item. ");		
		}
		else {
			List<Inventory> inventoryList=new ArrayList<Inventory>();
			List<Map<String, ItemInfo>> storeByItemInventoryList=new ArrayList<Map<String,ItemInfo>>();
			Iterator<Map<String, ItemInfo>> storeByItemInventoryListIterator;
			Map<String, List<Map<String, ItemInfo>>> itemByStoreDetails=ItemProvider.getInstance().getModel();
			
			if(itemByStoreDetails != null && !itemByStoreDetails.isEmpty()) {
				storeByItemInventoryList=itemByStoreDetails.get(id);
				storeByItemInventoryListIterator=((storeByItemInventoryList != null && !storeByItemInventoryList.isEmpty())?storeByItemInventoryList.iterator():null);	
				Map<String, ItemInfo> storeByItemInventory=new HashMap<String, ItemInfo>();
				
				Inventory inventory=null;
				ItemInfo storeItem = new ItemInfo();
				String storeId = null;
				
				if(storeByItemInventoryListIterator == null) {					
					logger.info("The given itemId is not present in the database.");
					return itemInventoryInfo;					
				}
				else {
					while(storeByItemInventoryListIterator.hasNext()) {		
						storeByItemInventory=storeByItemInventoryListIterator.next();			
						inventory=new Inventory();				
						Iterator<String> storeByItemIterator = ((storeByItemInventory != null && !storeByItemInventory.isEmpty())?storeByItemInventory.keySet().iterator():null) ;   
						if(storeByItemIterator !=null ) {
							while (storeByItemIterator.hasNext()) {			
								storeId = storeByItemIterator.next();				
								storeItem = storeByItemInventory.get(storeId);				
							}
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
			}			
			logger.info("No records in the database.");	
		}
		return itemInventoryInfo;		
	}		
	
	public Response createOrupdateItem(JAXBElement<ItemList> items, Response itemResponse) throws SQLException {		
		Map<String, List<Map<String, ItemInfo>>> itemByStoreInventoryList=ItemProvider.getInstance().getModel();
		List<Map<String, ItemInfo>> storeInventoryList=new ArrayList<Map<String,ItemInfo>>();		
		Map<String, ItemInfo> storeInventory=new HashMap<String, ItemInfo>();
		List<ItemInfo> itemList =((items.getValue() != null && (items.getValue().getItemInfo() != null && !items.getValue().getItemInfo().isEmpty()))?items.getValue().getItemInfo():null);
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
}
