/**
 * 
 */
package com.autoStock.tables;

import java.util.ArrayList;

import com.bethecoder.ascii_table.ASCIITable;

/**
 * @author Kevin Kowalewski
 *
 */
public class TableController {
	public void displayTable(TableDefinitions.AsciiTables table, ArrayList<ArrayList<String>> values){
		if (table.arrayOfColumns.length != values.get(0).size()){
			throw new IllegalStateException("AsciiTable definition, lenth of values don't match (columns, provided): " + table.arrayOfColumns.length + "," + values.get(0).size());
		}
		
	    ASCIITable.getInstance().printTable(new TableHelper().getArrayOfColumns(table.arrayOfColumns),new TableHelper().getArrayOfRows(table.arrayOfColumns,values));
	}
}
