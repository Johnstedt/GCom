package message;

public enum MessageType {
	JOIN, LEAVE, ASK_GROUPS, SEND_GROUPS, TEXT, INTERNAL;

	public String toShortString(MessageType type) {
		switch (type){
			case JOIN:
				return "J";
			case LEAVE:
				return "L";
			case ASK_GROUPS:
				return "AG";
			case SEND_GROUPS:
				return "SG";
			case TEXT:
				return "T";
			case INTERNAL:
				return "I";
			default:
				return "?";

		}
	}
}
