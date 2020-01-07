package immunity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

public class Test {

	public static void main(String[] args) {
		HashMap<String,Integer> map = new HashMap<String, Integer>();
		map.put("rabA", 123);
		
		for(String item : map.keySet()) {
			System.out.println(item);
		}
		for(Entry<String, Integer> item2: map.entrySet()) {
			System.out.println(item2.getKey());
			System.out.println(item2.getValue());
		}
		Collection<Integer> values = map.values();
		System.out.println(values);
	}

}
