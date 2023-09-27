package com.tencent.joox.sdk.data.entity;

import com.tencent.joox.sdk.data.TrackItem;
import java.util.ArrayList;
import java.util.List;

public class TracksRspEntity {

    private int list_count;
    private int next_index;
    private int total_count;
    private ArrayList<TrackItem> items;

    public int getList_count() {
        return list_count;
    }

    public void setList_count(int list_count) {
        this.list_count = list_count;
    }

    public int getNext_index() {
        return next_index;
    }

    public void setNext_index(int next_index) {
        this.next_index = next_index;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public ArrayList<TrackItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<TrackItem> items) {
        this.items = items;
    }

}
