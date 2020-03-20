package exam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.Math;

class Solution {
	
	public int solution (int N) {
   	String str = Integer.toString(N);
	int temp = N;
	List<Integer> list = new ArrayList<>();
	int i =0;
	while (i<str.length()) {
		list.add(0,temp % 10);
		temp /=10;
		i+=1;
	}
	Object[] ints = list.toArray();
	for (int j = 0; j<ints.length; j++) {
		if ((int)ints[j] <5 ) {
			list.add(j,5);
		}
	}
	int ans = 0;
	Object[] ansArray = list.toArray();
	for(int k=0; k<ansArray.length; k++) {
		ans += (int)ansArray[k]* Math.pow(10, ansArray.length-k-1);
	}
	return ans;
}
	
	public int solution (String S) {
		char [] chars = S.toCharArray();
		List <Character> l = new ArrayList<>(); 
		int count = 0;
		for (int i =0; i<chars.length;i++) {
			l.add(chars[i]);
		}
		if (l.get(0) == 'a') {
			if (l.get(1)!='a') {
				l.add(0,'a');
				count +=1;
			}
		}
		else {
			l.add(0,'a');
			l.add(0,'a');
			count +=2;
		}
		
		for (int j =1; j<l.size()-1;j++) {
			if (l.get(j) == 'a') {
				if(l.get(j-1)!='a') {
					if(l.get(j+1)!= 'a') {
					l.add(j+1, 'a');
					count+=1;
				}
				}
				else {
					if (l.get(j+1) =='a') {
						return -1;
						}
					}
			}
			else {
				if(l.get(j+1)!= 'a' && (j+2) <= l.size()&& l.get(j+2)!='a') {
				l.add(j+1,'a');
				count +=1;
				}
			}
		}
		if (l.get(l.size()-1)!='a') {
			count +=2;
		}
		else {
			if (l.get(l.size()-2)!= 'a'){
					count +=1;
			}
		}
	return count;
	}
		
}
