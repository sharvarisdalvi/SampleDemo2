package com.Utils.pack;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.base.pack.BrowserEventManager;

public class SupportUtils {

	private static Properties prop;
	static{
		/*	FileReader reader=new FileReader("Property.properties");
		 *  Properties p=new Properties();  
	    p.load(reader);  */
		prop = new Properties();
		try {
			InputStream in = BrowserEventManager.class.getClassLoader().getResourceAsStream("property.properties");
			/*File file = new File("../Eventclass/test/property.properties");
			FileInputStream in = new FileInputStream(file);*/
			prop.load(in);
		} catch (IOException ioe) {
			System.out.println("Error found..!!!" + ioe.getMessage());
		}
	}

	public static Properties getproperty()
	{
		return prop;

	}

	public static boolean IsRemoteEnvironment()
	{
		if(prop.getProperty("RemoteEnvironment").equalsIgnoreCase("YES"))
			return true;
		return false;
	}

	public static  String  Browser(){

		String name =prop.getProperty("Browser");

		return name;
	}

	public static String  path()
	{
		String name =prop.getProperty("path");
		return name ;
	}
}
