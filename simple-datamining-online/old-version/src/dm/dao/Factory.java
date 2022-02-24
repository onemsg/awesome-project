package dm.dao;

public class Factory {
	
	public Factory() {
		
	}
	// 类方法，返回一个DataSetDAO实例
	public static DataSetDAO getDataSetDao() throws Exception {
		DataSetDAO dao = new DataSetDAO();
		return dao;
	}
}
