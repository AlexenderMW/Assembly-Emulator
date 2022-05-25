package ICSI404;

import java.util.BitSet;

public class Memory {

	private static final int memSize = 256;//const size of memory array
	public byte memory[] = new byte[memSize]; //byte array to imitate memory
	//memory constructot, memory[] initialized to all 0
	public Memory()
	{
		for(int i = 0; i < memSize; ++i)
		{
			memory[i] = 0;
		}
	}
	//member function used to determine memory size
	public int memSize()
	{
		return this.memSize();
	}
	//read retrieves up to 4 bytes of memory at a time.  bits retrieved in big enianda order
	public LongWord read(LongWord address, int numBytes) throws Exception
	{
		//chekcing if within memory
		if((address.getSigned() < 0) || (address.getSigned() + numBytes > memSize))
			throw new Exception("Out of bounds memory access");
		if((numBytes != 1) && (numBytes != 2) && (numBytes != 4))
			throw new Exception("number of bytes not allowed");
		LongWord readByte = new LongWord();
		int wordLength = 0;
		//reading in bytes
		for(int i = (address.getSigned() + numBytes - 1); i >= address.getSigned(); --i)
		{
			//convert byte to bitset
			byte read = memory[i];
			BitSet readSet = BitSet.valueOf(new byte[] {read});
			//create longword from whats read in
			for(int j = 0; j < 8; ++j)
			{
				if(readSet.get(j) == false)
					readByte.clearBit(wordLength);
				else
					readByte.setBit(wordLength);
				++wordLength;
			}
		}
		return readByte;
	}
	//takes in an address, a longword, and the number of bytes to write.  Updates memory starting from address given
	public void write(LongWord address, LongWord word, int numBytes) throws Exception
	{
		//checking if within memory
		if((address.getSigned() < 0) || (address.getSigned() + numBytes > memSize))
			throw new Exception("Out of bounds memory access");
		if((numBytes != 1) && (numBytes != 2) && (numBytes != 4))
			throw new Exception("number of bytes not allowed");
		int wordLength = 8 * numBytes - 1;
		for(int i = address.getSigned(); i < address.getSigned() + numBytes; ++i)
		{
			//converting longword to byte
			String binaryToByte = "";
			for(int j = 7; j >= 0; j--)
			{
				if(word.getBit(wordLength) == false)
					binaryToByte += "0";
				else
					binaryToByte += "1";
				--wordLength;
			}
			//writing to memory
			int decimal = Integer.parseInt(binaryToByte,2);
			memory[i] = (byte)decimal;
		}
	}

}
