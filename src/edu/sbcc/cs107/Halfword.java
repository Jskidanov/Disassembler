package edu.sbcc.cs107;

/**
 * @author Jon Skidanov
 * CS 107: Disassembler Project
 *
 * This class is used to model a half-word of an object file. Each half-word must have an address as well as a data
 * value that can be disassembled into mnemonics and optional operands.
 * 
 * Note that the half-word is 16 bits but we are using a Java int which is typically 32 bits. Be sure to take that into
 * account when working with it.
 *
 */
public class Halfword {
	private int address;
	private int data;
	
	/**
	 * Constructor for a halfword.
	 * 
	 * @param address
	 * @param data
	 */
	public Halfword(int address, int data) {
		this.address = address;
		this.data = data;
	}
	
	/** 
	 * toString method.
	 * 
	 * The format for the halfword is a hex value 8 characters wide (address), a single space, and a hex
	 * value four characters wide (data).
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	
	/**
	 * Since the hex values of the address and data have been converted to base 10 for this class
	 * I am ensuring there are buffers of 0s on the left side if the size of each hex String does not match the size
	 * of the desired data type. (For example, "0x400 -> 0x0400")
	 */
	public String toString() {
		
		
		String addressHex = Integer.toHexString(address).toUpperCase(); // Accurate, but not necessarily a HalfWord
		String dataHex = Integer.toHexString(data).toUpperCase(); //Accurate, but necessarily a Byte
		
		String addressHexBuffer = "";    
		String dataHexBuffer = "";     
		
		for (int i = addressHex.length(); i < 8; i++){
			
			addressHexBuffer += "0";
			
		}
		
		for (int i = dataHex.length(); i < 4; i++){
			
			dataHexBuffer += "0";
		}
		
		
		return addressHexBuffer + addressHex + " " + dataHexBuffer + dataHex;
		
	}

	/**
	 * Get the address of the half-word.
	 * 
	 * @return
	 */
	public int getAddress() {
		return this.address;
	}
	
	/**
	 * Get the data of the half-word.
	 * 
	 * @return
	 */
	public int getData() {
		return this.data;
	}

}
