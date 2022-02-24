package dm.entity;

import org.springframework.data.annotation.Id;

/**
 * 对应数据库中存储的数据集
 */
public class DataLocation {
	
	@Id
	private Long id;		//数据库中的索引

	private String name; 	//数据集名字

	private String path;	//数据集文件路径
	
	public DataLocation() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	    	
	@Override
	public String toString() {
        return id + ": " + name + "@ " + path;
	}
}
