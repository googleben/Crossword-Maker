package com.ben.cross;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Crossword {
	
	public String o;
	public String[] fin;
	public int[] center;
	public int freq;
	private final String[] BACKUP = {"portal", "half life", "fallout", "grand theft auto", "steam", "gog galaxy", "the witcher", "v for vendetta"};
	private String[] words;
	private ArrayList<String> used;
	int oLength;
	
	public Crossword(String o, int freq) {
		this.o = o.toLowerCase();
		this.freq = freq;
		used = new ArrayList<String>();
		fin = new String[o.length()];
		center = new int[o.length()];
		words = BACKUP;
		setLength();
	}
	
	public void make() {
		ArrayList<Integer> done = new ArrayList<Integer>();
		ArrayList<Integer> cant = new ArrayList<Integer>();
		
		while (done.size()!=freq && cant.size()+done.size()!=oLength) {
			for (int i = 0; i<o.length(); i++) {
				if (!done.contains(i) && !cant.contains(i) && Math.random()>0.8) {
					
					if (o.charAt(i)!='-') {
						Object[] ans = findWord(o.charAt(i));
						if (ans[0]!=null) {
							done.add(i);
							fin[i] = (String)ans[0];
							center[i] = (int)ans[1];
						} else { cant.add(i); }
					}
					
					
				}
			}
		}
		System.out.println(done.size()+" "+cant.size());
		
		for (int i = 0; i<o.length(); i++) {
			if (!done.contains(i)) {
				fin[i] = ""+o.charAt(i);
				center[i] = 0;
			}
		}
		
		for (int i = 0; i<fin.length; i++) {
			fin[i] = fin[i].toUpperCase();
		}
		
	}
	
	public Object[] findWord(char c) {
		Object[] ans = new Object[2];
		ans[0] = null;
		ans[1] = null;
		
		for (String s : words) {
			if (s.contains(""+c)) {
				if (!used.contains(s)) {
					ans[0] = s;
					int ind = s.lastIndexOf(c);
					ans[1] = ind;
					used.add(s);
					break;
				}
			}
		}
		
		
		return ans;
	}
	
	public void printDebug() {
		for (int i = 0; i<oLength; i++) {
			System.out.println(fin[i]+" "+center[i]+" "+o.charAt(i));
		}
	}
	
	public void print() {
		int bigB = getSpaceBefore();
		
		
		for (int i = 0; i<fin.length; i++) {
			int c = center[i];
			String s = fin[i];
			int before = bigB-c;
			String ans = "";
			for (int j = 0; j<before; j++) ans+=" ";
			ans+=s;
			System.out.println(ans);
		}
		
	}
	
	public int getSpaceBefore() {
		int bigB = 0;
		for (int i : center) {
			if (i>bigB) bigB = i;
		}
		return bigB;
	}
	
	public void getWordsFromFile(String location) {
		ArrayList<String> ans = new ArrayList<String>();
		try {
			Scanner file = new Scanner(new File(location));
			while (file.hasNextLine()) ans.add(file.nextLine());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		if (ans.size()>0) words = ans.toArray(new String[0]);
	}
	
	public void setLength() {
		int length = 0;
		for (char c : o.toCharArray()) {
			if (c!='-') length++;
		}
		oLength = length;
	}
	
}
