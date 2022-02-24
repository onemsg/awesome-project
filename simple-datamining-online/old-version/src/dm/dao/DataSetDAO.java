package dm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataSetDAO {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	public DataSetDAO() throws Exception {
		this.conn = new DBConnection().getConnection();
	}
	
	public DataSetDAO(Connection conn) {
		this.conn = conn;
	}

	public DataSet getDataSet(int ID) throws SQLException {
		DataSet dataSet = null;
		String sql = "select ID,DataName,DataPath from dataset_public where ID=?";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setInt(1, ID);
		ResultSet rs = this.pstmt.executeQuery();
		while(rs.next()) {
			dataSet = new DataSet(rs.getInt(1),rs.getString(2), rs.getString(3));
		}
		this.pstmt.close();
		return dataSet;
	}
	
	public DataSet getDataSet(String dataName ) throws SQLException {
		DataSet dataSet = null;
		String sql = "select ID,DataName,DataPath from dataset_public where DataName=?";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setString(1,dataName);
		ResultSet rs = this.pstmt.executeQuery();
		while(rs.next()) {
			dataSet = new DataSet(rs.getInt(1),rs.getString(2), rs.getString(3));
		}
		this.pstmt.close();
		return dataSet;
	}
	
	public List<DataSet> getAllDataSet() throws SQLException{
		List<DataSet> list = new ArrayList<DataSet>();
		String sql = "select ID,DataName,DataPath from dataset_public";
		this.pstmt = this.conn.prepareStatement(sql);
		ResultSet rs = this.pstmt.executeQuery();
		DataSet dataSet;
		while(rs.next()) {
			dataSet = new DataSet(rs.getInt(1),rs.getString(2), rs.getString(3));
			list.add(dataSet);
		}
		this.pstmt.close();
		return list;
	}
	
	public String getDataSetPath(int ID) throws SQLException {
		return getDataSet(ID).getDataPath();
	}
	
	public String getDataSetPath(String dataName) throws SQLException {
		return getDataSet(dataName).getDataPath();
	}
	
	public List<String[]> getAllDataSet2() throws SQLException{
		List<DataSet> alldata = getAllDataSet();
		List<String[]> list = new ArrayList<String[]>();
		String[] tmp; 
		for(DataSet data : alldata ) {
			tmp = new String[3];
			tmp[0] = String.valueOf(data.getId());
			tmp[1] = data.getDataName();
			tmp[2] = data.getDataPath();
			list.add(tmp);
		}
		this.pstmt.close();
		return list;
	}
	
	public boolean add(DataSet dataSet) throws SQLException {
		boolean flag = false;
		String sql = "insert into dataset_public(DataName,DataPath) values(?,?)";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setString(1,dataSet.getDataName());
		String path = dataSet.getDataPath().replace("\\", "\\\\");
		this.pstmt.setString(2, path);
		if (this.pstmt.executeUpdate() > 0) {
			flag = true;
		}
		this.pstmt.close();
		return flag;
	}
}
