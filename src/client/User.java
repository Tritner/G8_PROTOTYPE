package client;

public class User{
	private String name;
	private String email;
	private String password;
	private String phone;
	private String address;
	private String role;
	private String status;

	public User(String name, String email, String password, String phone, String address, String role, String status) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.address = address;
		this.role = role;
		this.status = status;
	}	
	
	
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public String getRole() {
		return role;
	}

	public String getStatus() {
		return status;
	}
	
	
}
