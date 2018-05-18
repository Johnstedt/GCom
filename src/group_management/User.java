package group_management;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable, Comparable{

	private String nickname;
	private String ip;
	private Integer port;

	public User(String nick, String ip, Integer port){

		this.nickname = nick;
		this.ip = ip;
		this.port = port;

	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new User(this.nickname, this.ip, this.port);
	}

	public String getNickname() {
		return nickname;
	}

	public String getIp() {
		return ip;
	}

	public Integer getPort() {
		return port;
	}

	public int compare(User obj1, User obj2) {
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

	@Override
	public int compareTo(Object o) {
		if (o.getClass() != User.class)
			return 0;
		User obj2 = (User) o;
		if (!ip.equals(obj2.ip))
				return ip.compareTo(obj2.ip);
		if (!Objects.equals(port, obj2.port))
			return port.compareTo(obj2.port);
		return nickname.compareTo(obj2.nickname);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (nickname != null ? !nickname.equals(user.nickname) : user.nickname != null)
			return false;
		if (ip != null ? !ip.equals(user.ip) : user.ip != null) return false;
		return port != null ? port.equals(user.port) : user.port == null;
	}

	@Override
	public int hashCode() {
		int result = nickname != null ? nickname.hashCode() : 0;
		result = 31 * result + (ip != null ? ip.hashCode() : 0);
		result = 31 * result + (port != null ? port.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return nickname;
	}
}
