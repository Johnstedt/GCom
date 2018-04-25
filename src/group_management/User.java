package group_management;

public class User {

	private String nickname;
	private String ip;
	private Integer port;

	public User(String nick, String ip, Integer port){

		this.nickname = nick;
		this.ip = ip;
		this.port = port;

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
}
