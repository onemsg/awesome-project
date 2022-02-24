package dm.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	
	private final static String driver = "com.mysql.cj.jdbc.Driver";
	private final static String url = "jdbc:mysql://localhost:3306/datamining"
			+ "?user=root&password=root&useSSL=false&serverTimezone=GMT%2B8";
	private Connection conn = null;
	
	// 建立数据库连接
	public DBConnection() throws Exception{
		this(url);
	}
	
	public DBConnection(String dbUrl) throws Exception{
		try {
			Class.forName(driver);
			this.conn = DriverManager.getConnection(dbUrl);
		} catch (Exception e) {
			throw e;
		}
	}
	// 取得数据库连接
	public Connection getConnection() {
		return conn;
	}
	// 关闭数据库
	public void close() throws Exception{
		if (this.conn != null) {
			try {
				this.conn.close();
			}catch (Exception e) {
				throw e;
			}
		}
	}

}
