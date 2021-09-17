package com;

import java.util.List;

import com.ReturnType;

public class LinkObject {
	public void InitToWhen(List<MockInfo> mockinfolist, List<MockInitInfo> mockinitinfolist) {
		for(int index_when = 0;index_when < mockinfolist.size();index_when++) {
			if(mockinfolist.get(index_when).getType() == ReturnType.SIMPLE_NAME_TYPE) {
				for(int index_init = 0;index_init < mockinitinfolist.size();index_init++) {
					MockInfo mockinfo = mockinfolist.get(index_when);
					MockInitInfo mockinitinfo = mockinitinfolist.get(index_init);
					String whenfield = mockinfo.getField();
					String initfield = mockinitinfo.getField();	
					if(initfield.equals(whenfield)) {
						String when_name = mockinfo.getContent();
						String init_name = mockinitinfo.getName();
						if(when_name.equals(init_name)) {
							System.out.println(initfield);
							System.out.println(whenfield);
							System.out.println(init_name);
							System.out.println(when_name);
						}
					}
				}
			}
		}
	}
}
