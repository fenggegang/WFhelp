package com.fggang.market;

import com.fggang.datasave.items.Item;
import com.fggang.datasave.items.ItemList;
import com.fggang.tools.FuzzyMatch;

public class ItemOrder {
    public static String getItemOrder(String itemName) {
        boolean fullLevel = false;
        itemName = itemName.toLowerCase();

        if(itemName.contains("p") && !itemName.contains("prime")) {
            itemName = itemName.replaceAll("p" , "Prime");
        }

        if(itemName.contains("满级")) {
            fullLevel = true;
            itemName = itemName.replaceAll("满级" , "");
        }

        Item resultItem = FuzzyMatch.getMatchItem(itemName, ItemList.getItems() , 2);

        if(resultItem == null) {
            System.out.println("未找到物品");
            return "未找到物品";
        }

        System.out.println(resultItem.item);

        System.out.println(resultItem.url);

        if(fullLevel) {

            int rank = ItemRank.getItemMax(resultItem.url);


            System.out.println("物品满级等级: " + rank);


            return PostItemOrder.getStringItemOrder(resultItem , rank);
        }


        return PostItemOrder.getStringItemOrder(resultItem , -1);
    }
}
