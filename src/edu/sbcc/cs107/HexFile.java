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
	
	private String fullPath = "lib/";
	
	private String newRecord = "";
	
	
	/**
	 * JON NOTE:
	 * 
	 * I created two lists, one for memory locations the other for corresponding data. I was considering making LinkedLists 
	 * but figured they may not always be for one time use. 
	 * For each I also have a corresponding indexes marking unused pieces of data for the getNextHalfword() method.
	 * This is in essence a 2d Array.
	 */
	
	
	private int memoryLocPointer = 0;
	
	private ArrayList<String> memoryAddresses = null;
	
	private int dataLocPointer = 0;
	
	private ArrayList<String> dataList = null;
	

	/**
	 * Constructor that loads the .hex file.
	 * 
	 * @param hexFileName
	 * @throws FileNotFoundException
	 */
	public HexFile(String hexFileName) throws FileNotFoundException {
		
		memoryAddresses = new ArrayList<String>();
		dataList = new ArrayList<String>();
		
		fullPath = fullPath + hexFileName;
		
		
		// I scan each line into an array.
		
		Scanner s = new Scanner(new File(fullPath));
		hexFile = new ArrayList<String>();
		while (s.hasNext()){									
		    hexFile.add(s.next());
		}
		s.close();
		
		
		//Then I check each line and add locations/machine code operations.
		
		for (String line : hexFile){
			
			if (getRecordType(line) == 00)                               
			{	
				 
				ArrayList<String> halfwordLocations = new ArrayList<String>();
				ArrayList<String> commands = new ArrayList<String>();               
				
				// I create a string with the data field for that line (That omits the data in front and checksum at the end)
				
				String dataField = line.substring(9, (line.length() - 2));
																			
				// Then I calculate the number of Data Bytes contained on that line and the first address on it
				
				int numBytes = getDataBytesOfRecord(line);
				int location = getAddressOfRecord(line);
				
				//The locations are then dynamically generated and formatted for that line, starting with the first.
				
				for (int i = 0; i < numBytes; i++)
				{
					String formattedLoc = String.format("%08X", (0xFFFFFFFF & location)).toUpperCase();
					halfwordLocations.add(formattedLoc);
					
					
					location += 2;
					
				}
				
				for (int i = 0; !((i + 4) > dataField.length()); i+=4)
				
				{
					// Then I take the machine code by iterating through the data field 4 characters at a time.
					
					String operation = dataField.substring(i, (i + 4));
					
					//Here I untangle the little-Endian encoding
					String littleEndString = operation.substring(2, 4) + operation.substring(0, 2);   
					int littleEndNum = Integer.parseInt(littleEndString, 16);
					
					//Notice the 1 byte formatting instead of 2 bytes like with the addresses
					String formattedLoc = String.format("%04X", (0xFFFF & littleEndNum)).toUpperCase();
					commands.add(formattedLoc);	
				}
				
				memoryAddresses.addAll(halfwordLocations);  //Adding the halfword locations for that line
				dataList.addAll(commands); // Adding all commands in the data field for that line
				
			}
			
		}
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
	
	/**
	 * 
	 * JON NOTE:
	 * 
	 * Examination of the documentation has shown me that
	 * 
	 * ll : Data Bytes - Indexes 0 and 1.
	 * aaaa : The Address Field  - Indexes 2, 3, 4, and 5.
	 * tt : The Hex Record Type - Indexes 6 and 7.
	 * dd : The Data Field
	 * cc : The Checksum Field 
	 *
	 * I will label the strings in each method accordingly to represent my knowledge of the subject and save you time
	 */
	
	public void parseRecord(String record){
		
		newRecord = record.replace(":", "");
	}
	
	
	public int getDataBytesOfRecord(String record) {
		
		parseRecord(record);
		
		String dd = newRecord.substring(0, 2);
		int dd2 = Integer.parseInt(dd, 16); // What a useful method!
		return dd2; // Digits 0 and 1 are the Data Bytes
		
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
		
		parseRecord(record);
		
		String aaaa = newRecord.substring(2, 6);
		int aaaa2 = Integer.parseInt(aaaa, 16);
		return aaaa2; // Digits 2 - 5 are the Address
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
		
		parseRecord(record);
		
		String tt = newRecord.substring(6, 8);
		int tt2 = Integer.parseInt(tt, 16);
		return tt2; // Digits 6 and 7 are the Type
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
		
		if (!memoryAddresses.isEmpty() 
				&& 
				(((memoryLocPointer != memoryAddresses.size())) && (dataLocPointer != dataList.size())))
		
		{
			String address = memoryAddresses.get(memoryLocPointer); //Iterating through a list of pre-stored memory locations
			memoryLocPointer++; 
			
			String data = dataList.get(dataLocPointer); //Iterating through a list of pre-stored machine code instructions
			dataLocPointer++;
			
			Halfword hw = new Halfword(Integer.parseInt(address, 16), Integer.parseInt(data, 16));
			
			return hw;
			
		}
		
		else
		{
			
			return null;
		}
		
	}
}
