package com.example.zeningzhang.client;

/**
 * Created by ZeningZhang on 7/12/16.
 */
public class TradingItems {
    private String itemName, publishDate,itemType;

    public TradingItems(){

    }

    public TradingItems(String name,String date,String type)
    {
        this.itemName = name;
        this.publishDate = date;
        this.itemType =type;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
