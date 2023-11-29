package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


class User {
	private String username;
	private String password;
	private String name;
	private String email;
	private String about;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
}

class Database {
	private static final String userName = "root";
	private static final String password = "root";
	private static final String connectionUrl = "jdbc:mysql://localhost:3306/?user=root";

	static final Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection establishConn = DriverManager.getConnection(connectionUrl, userName, password);
		return establishConn;
	}

	static final Statement getStatement(Connection establishConn) throws SQLException {
		Statement stmt = establishConn.createStatement();
		return stmt;
	}

	static final PreparedStatement getPreparedStatement(Connection establishConn, String query) throws SQLException {
		PreparedStatement preparedStmt = establishConn.prepareStatement(query);
		return preparedStmt;
	}

	static final void closeConnection(Connection estConnection) {
		try {
			estConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

public class MiniSocialNetwork {
	private void createUser(Scanner scanner, User user) throws ClassNotFoundException, SQLException {
		System.out.print("Enter username: ");
		user.setUsername(scanner.nextLine().trim().toLowerCase());
		System.out.print("Enter password: ");
		user.setPassword(scanner.nextLine().trim());
		System.out.print("Enter name: ");
		user.setName(scanner.nextLine());
		System.out.print("Enter email: ");
		user.setEmail(scanner.nextLine().trim().toLowerCase());

		Connection con = Database.getConnection();
		try {
			
			String query = "INSERT INTO JavaFullStackCourse.profile(username, password, name, email) VALUES (?,?,?,?)";
			PreparedStatement pstmt;
			pstmt = Database.getPreparedStatement(con, query);
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getEmail());
			if (pstmt.executeUpdate() > 0) {
				System.out.println("User created Successfully.");
			} else {
				System.out.println("Failed to create User.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.closeConnection(con);
		}

	}
	
	private void logout(User user) {
		user.setUsername(null);
		user.setPassword(null);
		user.setName(null);
		user.setEmail(null);
		user.setAbout(null);
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		MiniSocialNetwork msn = new MiniSocialNetwork(); 
		User user = new User();
		Scanner scanner = new Scanner(System.in);
		Connection con;
		String username, password, name, email, about;

		while (true) {
			System.out.println("\nMini Social Network Menu:");
			System.out.println("1. Create User");
			System.out.println("2. Login");
			System.out.println("3. View Profile");
			System.out.println("4. Update Details");
			System.out.println("5. Logout");
			System.out.println("6. Delete Account");
			System.out.println("7. Exit");

			System.out.print("Enter your choice: ");
			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				msn.createUser(scanner, user);
				break;

			case 2:
				System.out.print("Enter username: ");
				username = scanner.nextLine().trim().toLowerCase();
				System.out.print("Enter password: ");
				password = scanner.nextLine().trim();

				con = Database.getConnection();
				String query = "SELECT * FROM JavaFullStackCourse.profile WHERE username = ? and password = ?";
				PreparedStatement pstmt = Database.getPreparedStatement(con, query);
				pstmt.setString(1, username);
				pstmt.setString(2, password);
				ResultSet usernames = pstmt.executeQuery();
				if (usernames.next()) {
					user.setUsername(usernames.getString("username"));
					user.setPassword(usernames.getString("password"));
					user.setName(usernames.getString("name"));
					user.setEmail(usernames.getString("email"));
					user.setAbout(usernames.getString("about"));
					System.out.println("Welcome " + user.getName());
				} else {
					System.out.println("Invalid Credentials.");
				}
				Database.closeConnection(con);
				break;

			case 3:
				if (user.getUsername() != null) {

					while (choice != 0) {
						System.out.println("\n============================="
								+ "\nMini Social Network Menu:");
						System.out.println("1. Update Name");
						System.out.println("2. Update Email");
						System.out.println("3. Update About");
						System.out.println("4. Change Password");
						System.out.println("0. Cancel & Go to Home");

						System.out.print("Enter your choice: ");
						choice = scanner.nextInt();
						scanner.nextLine();

						switch (choice) {
						case 1:
							System.out.print("Enter New name: ");
							name = scanner.nextLine();
							con = Database.getConnection();
							query = "UPDATE JavaFullStackCourse.profile SET name = ? where username = ?";
							pstmt = Database.getPreparedStatement(con, query);
							pstmt.setString(1, name);
							pstmt.setString(2, user.getUsername());
							if (pstmt.executeUpdate() > 0) {
								System.out.println("Name updated successfully.");
							}
							Database.closeConnection(con);
							break;

						case 2:
							System.out.print("Enter New email: ");
							email = scanner.next().toLowerCase();
							con = Database.getConnection();
							query = "UPDATE JavaFullStackCourse.profile SET email = ? where username = ?";
							pstmt = Database.getPreparedStatement(con, query);
							pstmt.setString(1, email);
							pstmt.setString(2, user.getUsername());
							if (pstmt.executeUpdate() > 0) {
								System.out.println("Email updated successfully.");
							}
							Database.closeConnection(con);
							break;
						case 3:
							System.out.print("Enter About: ");
							about = scanner.nextLine();
							con = Database.getConnection();
							query = "UPDATE JavaFullStackCourse.profile SET About = ? where username = ?";
							pstmt = Database.getPreparedStatement(con, query);
							pstmt.setString(1, about);
							pstmt.setString(2, user.getUsername());
							if (pstmt.executeUpdate() > 0) {
								System.out.println("About updated successfully.");
							}
							Database.closeConnection(con);
							break;
						case 4:
							System.out.print("Enter New password: ");
							password = scanner.next();
							con = Database.getConnection();
							query = "UPDATE JavaFullStackCourse.profile SET password = ? where username = ?";
							pstmt = Database.getPreparedStatement(con, query);
							pstmt.setString(1, password);
							pstmt.setString(2, user.getUsername());
							if (pstmt.executeUpdate() > 0) {
								System.out.println("Password Changed successfully.");
							}
							Database.closeConnection(con);
							break;
						case 0:
							break;
						default:
							System.out.println("Invalid choice. Please enter a valid option.");
							break;
						}
					}
				} else {
					System.out.println("You Need to Login.");
				}
				break;

			case 4:
				System.out.print("Enter username: ");
				username = scanner.nextLine().trim().toLowerCase();
				System.out.print("Enter password: ");
				password = scanner.nextLine().trim();

				con = Database.getConnection();
				query = "DELETE FROM JavaFullStackCourse.profile WHERE username = ? and password = ?";
				pstmt = Database.getPreparedStatement(con, query);
				pstmt.setString(1, username);
				pstmt.setString(2, password);
				if (pstmt.executeUpdate() > 0) {
					System.out.println("Your Account deleted successfully");
				} else {
					System.out.println("Invalid Credentials.");
				}
				Database.closeConnection(con);
				break;
				
			case 5:
				if (user.getUsername() != null) {
					msn.logout(user);
					System.out.println("Successfully Logout.");
				} else {
					System.out.println("No user to Log out.");
				}
				break;

			case 6:
				scanner.close();
				System.out.println("Thank You...!");
				System.exit(0);
				break;

			default:
				System.out.println("Invalid choice. Please enter a valid option.");
			}
		}
	}
}