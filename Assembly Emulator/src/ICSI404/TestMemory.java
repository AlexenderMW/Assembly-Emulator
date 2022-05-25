package ICSI404;

import java.util.BitSet;
//quick test for memory
public class TestMemory {

	public static void main(String[]args) throws Exception 
	{
		TestMemory test = new TestMemory();
		test.runTests();
	}
	public void runTests() throws Exception
	{
		//writing 2 bytes to memory then reading them out note, byte must be between 127 and -127
		Memory memoryTest = new Memory();
		LongWord byte1 = new LongWord();
		LongWord byte2 = new LongWord();
		LongWord byte3 = new LongWord();
		LongWord byte4 = new LongWord();
		byte1.set(127);//xxxxxx0111 1111
		byte2.set(-127);//xxxxxx1000 0001
		byte3.set(6874);//xxxx0001 1010 1101 1010
		byte4.set(1970608858);//0111 0101 0111 0101 0001 1010 1101 1010
		LongWord address = new LongWord();
		address.set(0);//writing
		memoryTest.write(address, byte1, 1);
		address.set(1);
		memoryTest.write(address, byte2, 1);
		address.set(2);
		memoryTest.write(address, byte3, 2);
		address.set(4);
		memoryTest.write(address, byte4, 4);
		//reading
		LongWord longword = new LongWord();
		longword.set(0);
		LongWord readWord = new LongWord();
		readWord = memoryTest.read(longword, 1);
		System.out.println(readWord.toString());
		longword.set(1);
		readWord = memoryTest.read(longword, 1);
		System.out.println(readWord.toString());
		longword.set(2);
		readWord = memoryTest.read(longword, 2);
		System.out.println(readWord.toString());
		longword.set(4);
		readWord = memoryTest.read(longword, 4);
		System.out.println(readWord.toString());
		//error check
		//invalid byte request
		//memoryTest.write(address, byte4, 5);
		//readWord = memoryTest.read(longword, 5);
		
		//out of bounds error
		//address.set(-6);
		//memoryTest.write(address, byte4, 4);
		//memoryTest.write(address, byte4, 4);
	}
}
