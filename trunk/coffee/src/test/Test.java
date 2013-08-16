package test;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<Object> items = new ArrayList<Object>();
		Lst lst = new Lst(items);
		items.add("xxxx");
		lst.print();
		
		String s = "Step1:Comment on 2 games, and now you\'ve get {0}";
		System.out.println(MessageFormat.format(s, 8));
	}
}

class Lst {
	List<Object> items;

	Lst(List<Object> items) {
		this.items = items;
	}

	public void print() {
		System.out.println(items);
	}
}