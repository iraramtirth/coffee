package test;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<Object> items = new ArrayList<Object>();
		Lst lst = new Lst(items);
		items.add("xxxx");
		lst.print();
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