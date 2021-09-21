package com;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.ReturnType;

public class LinkObject {	
	public List<LinkedList> InitToWhen(List<MockInfo> mockinfolist, List<MockInitInfo> mockinitinfolist) {
		List<LinkedList> mocklinkset = new ArrayList<>();
		
		for(int index_init = 0;index_init < mockinitinfolist.size();index_init++) {			
			for(int index_mock = 0;index_mock < mockinfolist.size();index_mock++) {
				MockInitInfo mockinitinfo = mockinitinfolist.get(index_init);
				MockInfo mockinfo = mockinfolist.get(index_mock);
				String initfield = mockinitinfo.getField();
				String whenfield = mockinfo.getField();
				if(initfield.equals(whenfield)) {
					String when_name = mockinfo.getObject();
					String init_name = mockinitinfo.getName();
					
					if(init_name.equals(when_name)) {
						mockinitinfo.initHasWhen(true);
						
						LinkedList<Info> pattern_list = new LinkedList<>();
						pattern_list.addFirst(mockinitinfo);
						pattern_list.addLast(mockinfo);
						
						mocklinkset.add(pattern_list);
					}
				}
			}
		}
		
		return mocklinkset;
	}
	
	//identify when pattern using simple value as return value
	public void SimpleValueFilter(List<LinkedList> mocklinkset) {
		for(int index = 0;index < mocklinkset.size();index++) {
		}
	}
	
	public void MockInfoLink(List<LinkedList> mocklinkset) {
		List<LinkedList> linkset = new ArrayList<>();
		
		for(int index_f = 0;index_f < mocklinkset.size();index_f++) {
			boolean hasMockReturn = false; //mark whether the return value of when pattern is a mock object
			
			LinkedList<Info> patternlist_head = mocklinkset.get(index_f);
			int index_head = patternlist_head.size()-1;
			if(patternlist_head.get(index_head) instanceof MockInitInfo)
				continue;
			MockInfo mockinfo = (MockInfo) patternlist_head.get(index_head);
			
			for(int index_b = 0;index_b < mocklinkset.size();index_b++) {
				if(index_b == index_f)
					continue;
				LinkedList<Info> patternlist_tail = mocklinkset.get(index_b);
				if(patternlist_tail.get(0) instanceof MockInfo)
					continue;
				MockInitInfo mockinitinfo = (MockInitInfo) patternlist_tail.get(0);
				
				if(isLinked(mockinfo, mockinitinfo)) {
					hasMockReturn = true;
				}
			}
			
			if(hasMockReturn == false) {
				// if the return value is not a mock object
				
			}
		}
	}
	
	public boolean isLinked(MockInfo head, MockInitInfo tail) {
		if(head.getType() != ReturnType.SIMPLE_NAME_TYPE)
			return false;
		if(!head.getContent().equals(tail.getName()))
			return false;
		return true;
	}
}
