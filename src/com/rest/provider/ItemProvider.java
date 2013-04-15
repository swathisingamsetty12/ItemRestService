package com.rest.provider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rest.item.entity.ItemInfo;

public class ItemProvider {
	private static ItemProvider instance=null;

	private static Map<String, List<Map<String, ItemInfo>>> contentProvider = new HashMap<String, List<Map<String,ItemInfo>>>();
	
	private ItemProvider() {
		//List<Map<String, com.rest.item.entity.ItemInfo>> lmap1 = new ArrayList<Map<String,ItemInfo>>();
		List<Map<String, ItemInfo>>	lmap2 = new ArrayList<Map<String,ItemInfo>>();		
		
		/*Map<String, ItemInfo> storeItem1=new HashMap<String, ItemInfo>();		
		ItemInfo item1 = new ItemInfo("1", "Shirt", "9000", "Sunnyvale", 10.00,"This shirt color is Blue", BigInteger.valueOf(5l));		
		storeItem1.put(item1.getStoreId(), item1);
		lmap1.add(storeItem1);
		contentProvider.put(item1.getItemId(), lmap1);*/
		
		Map<String, ItemInfo> storeItem2=new HashMap<String, ItemInfo>();
		ItemInfo item2 = new ItemInfo("2", "Top", "9001", "Sunnyvale", 10.00,"This top color is Pink", BigInteger.valueOf(5l));
		storeItem2.put(item2.getStoreId(), item2);		
		lmap2.add(storeItem2);
		contentProvider.put(item2.getItemId(), lmap2);	
	}
	
	public static ItemProvider getInstance() {		
		if(instance == null){		
			instance= new ItemProvider();		
		}		
		return instance;
	}

	public Map<String, List<Map<String, ItemInfo>>> getModel() {
		return contentProvider;
	}
}
