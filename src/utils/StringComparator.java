package utils;

public class StringComparator {
	public int compare(String obj1, String obj2) {
		if (obj1 == null) {
			return -1;
		}
		if (obj2 == null) {
			return 1;
		}
		if (obj1.equals( obj2 )) {
			return 0;
		}
		return obj1.compareTo(obj2);
	}
}
