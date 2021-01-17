package dm.model;

import java.util.Map;
/**
 * ModelInfo
 */
public class ModelInfo {

    private String modelName;   //模型名称
    private String options;     //模型参数
    private String dataName;    //数据集名称
    private String evalMethod;  //验证方法
    private Integer kFlods;     //交叉验证中的K折
    private Double splitRadio;  //分割验证中的分割比例
    //模型验证指标{"summary": **, "matrix": **, "classDetails"：**}
    private Map<String,String> summary;
    
    public ModelInfo(){

    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getEvalMethod() {
        return evalMethod;
    }

    public void setEvalMethod(String evalMethod) {
        this.evalMethod = evalMethod;
    }

    public Map<String, String> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, String> summary) {
        this.summary = summary;
    }

    public Integer getkFlods() {
        return kFlods;
    }

    public void setkFlods(Integer kFlods) {
        this.kFlods = kFlods;
    }

    public Double getSplitRadio() {
        return splitRadio;
    }

    public void setSplitRadio(Double splitRadio) {
        this.splitRadio = splitRadio;
    }

    public boolean isEmpty(){
        if(modelName == null && evalMethod == null){
                return true;
        }else return false;
    }

    @Override
    public String toString() {
        return "ModelInfo [dataName=" + dataName + ", evalMethod=" + evalMethod + ", kFlods=" + kFlods + ", modelName="
                + modelName + ", options=" + options + ", splitRadio=" + splitRadio + ", summary=" + summary + "]";
    }

}