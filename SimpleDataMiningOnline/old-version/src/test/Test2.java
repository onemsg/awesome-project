package test;

import dm.dao.*;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
public class Test2 {

	public static void main(String[] args) throws SQLException, Exception {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("0.00");
		String d = df.format(3.6584);
		System.out.println(d);
		
	}

}
