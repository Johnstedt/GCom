package message;

import group_management.User;

import java.util.List;

import static message.MessageType.TEXT;

public class SendInterface {
	private static Message cm(String text, String name, User self, List<User> sendTo) {
		return new Message(TEXT, name, self, sendTo, text);
	}

	public static Message createMessage(Object text, String name, User self, Object sendTo) {
		return cm((String)text, name, self, (List<User>)sendTo);
	}

}
