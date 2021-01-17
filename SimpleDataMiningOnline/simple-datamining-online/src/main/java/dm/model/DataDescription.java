package dm.model;

import java.util.List;
import java.util.Map;

/**
 * DataDescription
 */
public class DataDescription {

    private String name; // 数据集名称
    private String[] features;  //特征
    private Integer[] shape; // 数据集的样本数和特征数, 例如 [1000, 8] 1000条数据，特征数为8
    private List<String[]> values; // 数据集的值, 每行数据以 字符串数组形式保存
    private Map<String, Map<String, String>> description; // 数据集各特征的描述

    public DataDescription() {
    }

    public DataDescription(String name, String[] features, Integer[] shape, List<String[]> values,
            Map<String, Map<String, String>> description) {
        this.name = name;
        this.features = features;
        this.shape = shape;
        this.values = values;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getFeatures() {
        return features;
    }

    public void setFeatures(String[] features) {
        this.features = features;
    }

    public Integer[] getShape() {
        return shape;
    }

    public void setShape(Integer[] shape) {
        this.shape = shape;
    }

    public List<String[]> getValues() {
        return values;
    }

    public void setValues(List<String[]> values) {
        this.values = values;
    }

    public Map<String, Map<String, String>> getDescription() {
        return description;
    }

    public void setDescription(Map<String, Map<String, String>> description) {
        this.description = description;
    }
    
    

}