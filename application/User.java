package application;

public class User implements UserStruct {
	private String name;
	
	public User(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean equals(User user) {
		return this.name.equals(user.name);
	}
}
