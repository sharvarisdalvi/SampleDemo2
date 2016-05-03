package com.Utils.pack;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelOperation {

	String path;
	XSSFWorkbook wd;
	XSSFSheet sh;
	XSSFRow rw;
	FileInputStream fis;
	ArrayList<String> PageDetails;
	public  void ReadWorkbook(String path) throws IOException
	{
		this.path = path;
		File file = new File(path);
		//fis = ExcelOperation.class.getClassLoader().getResourceAsStream(Excelname);
		fis = new FileInputStream(file);
		wd = new XSSFWorkbook(fis);
	}

	public void GetSheet(String sheetname)
	{
		sh =wd.getSheet(sheetname);

	}
	public ArrayList<String> FetchData()
	{
		Iterator<Row> iterator = sh.iterator();

		while(iterator.hasNext())
		{
			PageDetails = new ArrayList<String>();
			Row nextRow = iterator.next();
			Iterator<Cell> celliterator = nextRow.cellIterator();

			while(celliterator.hasNext())
			{
				Cell cell = celliterator.next();
				switch(cell.getCellType())
				{
				case Cell.CELL_TYPE_NUMERIC:
					double numericCellValue = cell.getNumericCellValue();
					String value = String.valueOf(new Double(numericCellValue).longValue());
					PageDetails.add(value);
					break;
				case Cell.CELL_TYPE_STRING:
					String value1 =cell.getStringCellValue();
					PageDetails.add(value1);
					break;
				}

			}
			
		}
		return PageDetails;


	}
/*public static void main(String[]args) throws IOException
{
	
	ExcelOperation excel = new ExcelOperation();
	excel.ReadWorkbook("../Framework/src/test/resources/HealthKart.xlsx");
	excel.GetSheet("Login");
	ArrayList<String> fetchData = excel.FetchData();
	System.out.println(fetchData);
}*/

	
}
