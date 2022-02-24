package dm.dao;

public class DataSet {
	
	private int id;	//数据库中的索引
	private String dataName; //数据集名字
	private String dataPath;	//数据集文件路径
	
	public DataSet() {
		
	}
	
	public DataSet(int id,String dataName,String dataPath) {
		this.setId(id);
		this.setDataName(dataName);
		this.setDataPath(dataPath);
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}
	
	@Override
	public String toString() {
		String str = dataName + ": " + dataPath;
		return str;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
