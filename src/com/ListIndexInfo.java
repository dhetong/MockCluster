package com;

import java.util.ArrayList;
import java.util.List;

import patternnodeinfo.Info;

public class ListIndexInfo extends Info {
	private List<Integer> indexlist = new ArrayList<>();
	
	public void InsertIndex(int index) {
		indexlist.add(index);
	}
	
	public List<Integer> getIndexList(){
		return indexlist;
	}
}
