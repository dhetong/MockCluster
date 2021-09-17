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
	
	public void MockInfoLink(List<LinkedList> mocklinkset) {
	}
}
