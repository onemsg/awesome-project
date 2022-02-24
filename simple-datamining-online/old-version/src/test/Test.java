package test;

import com.mysql.cj.util.Util;

import dm.weka.core.*;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.Instance;
import java.util.*;
import weka.core.converters.ConverterUtils;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import java.io.File;
public class Test {

	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String s = "Hello";
		s.equals("");
		Map<String,String> map = new HashMap<String, String>();
		

		
	}
	
	
	public static void printList(String[] list) {
		StringBuilder strList = new StringBuilder();
		strList.append("[ ");
		int n = 1;
		for(String str : list) {
			strList.append("\"" + str + "\"");
			strList.append(", ");
			if(strList.length() > 120 * n ) {
				strList.append("\n");
				n++;
			}
		}
		strList.deleteCharAt(strList.length()-2);
		strList.append(" ]");
		System.out.println(strList);
	}
	
	public static void printInfo(DataExploration explor) {
		Map<String, Map<String,String>> info = explor.getDataDescribe();
		for(String key : info.keySet()) {
			System.out.println(key + ": ");
			for(String key2 : info.get(key).keySet()){
				System.out.println(key2 + ": " + info.get(key).get(key2));
			}
			System.out.println("===============================");
		}
	}
}
