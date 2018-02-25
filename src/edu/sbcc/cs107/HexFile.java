package edu.sbcc.cs107;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Jon Skidanov
 * CS 107: Disassembler Project
 *
 * This code implements working with a Hex file. The hex file format is documented 
 * at http://www.keil.com/support/docs/1584/
 */
public class HexFile {
	/**
	 * This is where you load the hex file. By making it an ArrayList you can easily traverse it in order.
	 */
	private ArrayList<String> hexFile = null;
	
	/* Add other variables here. */

	/**
	 * Constructor that loads the .hex file.
	 * 
	 * @param hexFileName
	 * @throws FileNotFoundException
	 */
	public HexFile(String hexFileName) throws FileNotFoundException {
		/* Your code here */
	}

	/**
	 * Pulls the length of the data bytes from an individual record.
	 * 
	 * This extracts the length of the data byte field from an individual 
	 * hex record. This is referred to as LL->Record Length in the documentation.
	 * 
	 * @param Hex file record (one line).
	 * @return record length.
	 */
	public int getDataBytesOfRecord(String record) {
		/* Your code here */
		return 0;
	}
	
	/**
	 * Get the starting address of the data bytes.
	 * 
	 * Extracts the starting address for the data. This tells you where the data bytes 
	 * start and are referred to as AAAA->Address in the documentation.
	 * 
	 * @param Hex file record (one line).
	 * @return Starting address of where the data bytes go.
	 */
	public int getAddressOfRecord(String record) {
		/* Your code here */
		return 0;
	}
	
	/**
	 * Gets the record type.
	 * 
	 * The record type tells you what the record can do and determines what happens
	 * to the data in the data field. This is referred to as DD->Data in the 
	 * documentation.
	 * 
	 * @param Hex file record (one line).
	 * @return Record type.
	 */
	public int getRecordType(String record) {
		/* Your code here */
		return 0;
	}

	/**
	 * Returns the next halfword data byte.
	 * 
	 * This function will extract the next halfword from the Hex file. By repeatedly calling this
	 * function it will look like we are getting a series of halfwords. Behind the scenes we must 
	 * parse the HEX file so that we are extracting the data from the data files as well as indicating
	 * the correct address. This requires us to handle the various record types. Some record types
	 * can effect the address only. These need to be processed and skipped. Only data from recordType
	 * 0 will result in something returned. When finished processing null is returned.
	 * 
	 * @return Next halfword.
	 */
	public Halfword getNextHalfword() {
		/* Your code here */
		return null;
	}
}
