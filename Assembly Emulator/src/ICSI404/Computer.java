package ICSI404;

import java.util.Arrays;
import java.util.BitSet;

public class Computer {
	private Boolean halted;
	private ALU alu = new ALU();
	private ALU PCalu = new ALU(); //used when flags can't be disrupted
	private Memory memory = new Memory();
	private LongWord[] registers = new LongWord[16];//registers
	private LongWord PC = new LongWord();//Program Counter keeps track of where in memory
	private LongWord IR = new LongWord();//instruction register
	private LongWord op1 = new LongWord();//op1 register
	private LongWord op2 = new LongWord();//op2 register
	private LongWord result = new LongWord();//output register
	//masks
	private LongWord fourthByteMask = new LongWord(); {fourthByteMask.set(61440);} //"xxxx1111000000000000"
	private LongWord thirdByteMask = new LongWord(); {thirdByteMask.set(3840);} //"xxxx0000111100000000"
	private LongWord secondByteMask = new LongWord(); {secondByteMask.set(240);} //"xxxx0000000011110000"
	private LongWord firstByteMask = new LongWord(); {firstByteMask.set(15);} //"xxxx0000000000001111"
	private LongWord immediateMask = new LongWord(); {immediateMask.set(255);}//"xxxx0000000011111111"
	private LongWord jumpMask = new LongWord(); {jumpMask.set(4095);}//"xxxx0000111111111111"
	private LongWord branchMask = new LongWord(); {branchMask.set(1023);}//"xxxx0000001111111111"
	private LongWord branchOpMask = new LongWord(); {branchOpMask.set(-1024);}//"fffffc00"
	//computer construction initializes computer
	//halt = false
	//registers initialized
	//PC set to 0
	public Computer()
	{
		halted = false;
		PC.set(0);
		for(int i = 0; i < 16; ++i)
		{
			this.registers[i] = new LongWord();
		}
	}
	//run method executes all instuctions found in memory starting at address 0
	public void run() throws Exception
	{
		for(;;)
		{
			if(halted == true)
			{
				throw new Exception("Computer has been halted");
			}
			fetch();
			decode();
			execute();
			store();
		}
	}
	//fetch reads in instructions from memory, 2 bytes are read in and stored in Instruction register
	public void fetch() throws Exception 
	{
		//System.out.println("fetch() PC = " + PC.getUnsigned());//use for testing
		//update PC and store instruction
		LongWord updatePC = new LongWord();
		updatePC.set(2);
		IR = memory.read(PC, 2);
		PC = PCalu.operate(3, PC, updatePC);
		System.out.println("fetch() IR = " + IR.toString() + "----- PC = " + PC.getUnsigned());
	}
	//decode function gets relivant bits and stores them in the op1 and op2 registers in needed
	public void decode() throws Exception 
	{
		//opcode 0001 interrupt
		if((IR.getBit(15) == false) && (IR.getBit(14) == false) && (IR.getBit(13) == false) && (IR.getBit(12) == true))
		{
			this.op2 = alu.operate(0, immediateMask, IR);
		}
		//MOV 0010 
		if((IR.getBit(15) == false) && (IR.getBit(14) == false) && (IR.getBit(13) == true) && (IR.getBit(12) == false))
		{
			//storing register to be stored in later into op1
			LongWord op1register = new LongWord();
			op1register = alu.operate(0, thirdByteMask, IR);
			LongWord shift = new LongWord();
			shift.set(8);
			op1register = alu.operate(6,  op1register, shift);
			this.op1.copy(op1register);
			//storing immediate value into op2
			LongWord immediate = new LongWord();
			immediate = alu.operate(0, immediateMask, IR);
			this.op2.copy(immediate);
		}
		//jump opcode 0011
		if((IR.getBit(15) == false) && (IR.getBit(14) == false) && (IR.getBit(13) == true) && (IR.getBit(12) == true))
		{
			LongWord op1register = new LongWord();
			op1register = alu.operate(0, jumpMask, IR);
			this.op1.copy(op1register);
		}
		//ALU opcodes and CMP 0100
		if((IR.getBit(15) == true) || 
		   (IR.getBit(15) == false) && (IR.getBit(14) == true) && (IR.getBit(13) == false) && (IR.getBit(12) == false))
		{
			//getting op1
			LongWord op1register = new LongWord();
			op1register = alu.operate(0, secondByteMask, IR);
			LongWord shift = new LongWord();
			shift.set(4);
			op1register = alu.operate(6,  op1register, shift);
			this.op1.copy(registers[(int) op1register.getUnsigned()]);
			System.out.println("decode() op1register = " + op1register.toString() + "----- op1 =" + this.op1.toString());
			//getting op2
			LongWord op2register = new LongWord();
			op2register = alu.operate(0, firstByteMask, IR);
			this.op2.copy(registers[(int) op2register.getUnsigned()]);
			System.out.println("decode() op2register = " + op1register.toString() + "----- op2 =" + this.op2.toString());
		}
		//branch opcode 0101 
		if((IR.getBit(15) == false) && (IR.getBit(14) == true) && (IR.getBit(13) == false) && (IR.getBit(12) == true))
		{
			LongWord address = new LongWord();
			address = PCalu.operate(0, branchMask, IR);
			this.op1.copy(address);
		}
	}
	public void execute() throws Exception 
	{
		//opcode 0000 halt
		if((IR.getBit(15) == false) && (IR.getBit(14) == false) && (IR.getBit(13) == false) && (IR.getBit(12) == false))
		{
			this.halted = true;
		}
		//opcode 0001 interrupt
		if((IR.getBit(15) == false) && (IR.getBit(14) == false) && (IR.getBit(13) == false) && (IR.getBit(12) == true))
		{
			if(op2.getUnsigned() == 0)
			{
				for(int i = 0; i < 15; ++i)
				{
					System.out.println("R" + i + ": " + registers[i].toString());
				}
			}
			//print memory
			else if(op2.getUnsigned() == 1)
			{
				//printing memory, 4 bytes per line
				for(int i = 0; i < 256; ++i)
				{
					String printByte = String.format("%8s", Integer.toBinaryString(memory.memory[i] & 0xFF)).replace(' ', '0');
					printByte = printByte.substring(0, 4) + " " + printByte.substring(4);
					++i;
					String printByte2 = String.format("%8s", Integer.toBinaryString(memory.memory[i] & 0xFF)).replace(' ', '0');
					printByte2 = printByte2.substring(0, 4) + " " + printByte2.substring(4);
					++i;
					String printByte3 = String.format("%8s", Integer.toBinaryString(memory.memory[i] & 0xFF)).replace(' ', '0');
					printByte3 = printByte3.substring(0, 4) + " " + printByte3.substring(4);
					++i;
					String printByte4 = String.format("%8s", Integer.toBinaryString(memory.memory[i] & 0xFF)).replace(' ', '0');
					printByte4 = printByte4.substring(0, 4) + " " + printByte4.substring(4);
					String printByteFinal = printByte + " " + printByte2 + " " + printByte3 + " " + printByte4; 
					System.out.println(printByteFinal);
				}
			}
			else
				throw new Exception("Interupt not valid");
		}	
		//opcode 0010 MOV
		if((IR.getBit(15) == false) && (IR.getBit(14) == false) && (IR.getBit(13) == true) && (IR.getBit(12) == false))
		{
			byte[] immediateByte = new byte[1];
			immediateByte = op2.longWord.toByteArray();
			op2.set((int)immediateByte[0]);
		}
		//jump opcode 0011
		if((IR.getBit(15) == false) && (IR.getBit(14) == false) && (IR.getBit(13) == true) && (IR.getBit(12) == true))
		{
			LongWord op1register = new LongWord();
			op1register.copy(this.op1);
			LongWord shift = new LongWord();
			shift.set(1);
			//unhiding 0 LSB
			op1register = alu.operate(5,  op1register, shift);
			PC.set((int)op1register.getUnsigned());
		}
		//CMP opcode 0100
		if((IR.getBit(15) == false) && (IR.getBit(14) == true) && (IR.getBit(13) == false) && (IR.getBit(12) == false))
		{
			alu.operate(4, op1, op2);
		}
		//Branch opcode 0101
		if((IR.getBit(15) == false) && (IR.getBit(14) == true) && (IR.getBit(13) == false) && (IR.getBit(12) == true))
		{
			LongWord address = new LongWord();
			address.copy(this.op1);
			//if negative XOR all bits above 10th bit
			if(address.getBit(9) == true)
				address = PCalu.operate(2,  address, branchOpMask);
			LongWord shift = new LongWord();
			shift.set(1);
			//SRA to unhide LSB 
			address = PCalu.operate(5,  address, shift);
			//branch if not equal CC = 00
			if((IR.getBit(10) == false) && ( IR.getBit(11) == false))
			{
				//if not ((ZF == true) && (NF == 0)) aka equal
				if(!((alu.getStatus().getBit(0) == true) && (alu.getStatus().getBit(1) == false)))
				{
					this.PC = PCalu.operate(3, this.PC, address);
				}
			}
			//branch if less than CC = 01
			if((IR.getBit(11) == false) && ( IR.getBit(10) == true))
			{
				//if not ((ZF == false) && (NF == true)) aka R1 < R2
				if(!((alu.getStatus().getBit(0) == false) && (alu.getStatus().getBit(1) == true)))
				{
					this.PC = PCalu.operate(3, this.PC, address);
				}
			}
			//branch if equal CC = 10
			if((IR.getBit(11) == true) && ( IR.getBit(10) == false))
			{
				//if ((ZF == true) && (NF == 0)) aka equal
				if((alu.getStatus().getBit(0) == true) && (alu.getStatus().getBit(1) == false))
				{
					this.PC = PCalu.operate(3, this.PC, address);
				}
			}
			//branch less or equal CC = 11
			if((IR.getBit(11) == true) && ( IR.getBit(10) == true))
			{
				//if not (ZF ^ NF ) aka only 1 or other are true
				if((alu.getStatus().getBit(0) ^ alu.getStatus().getBit(1)))
				{
					this.PC = PCalu.operate(3, this.PC, address);
				}
			}
		}
		//ALU opcode
		if(IR.getBit(15) == true)
		{
			//call alu based on opcode given from 1000-1111
			LongWord opCode = new LongWord();
			opCode = alu.operate(0, fourthByteMask, IR);
			LongWord shift = new LongWord();
			shift.set(12);
			opCode = PCalu.operate(6,  opCode, shift);
			opCode.clearBit(3);
			result.copy(alu.operate((int)opCode.getUnsigned(),  op1, op2));
		}
	}
	
	public void store() throws Exception 
	{
		//MOV opcode 0010
		if((IR.getBit(15) == false) && (IR.getBit(14) == false) && (IR.getBit(13) == true) && (IR.getBit(12) == false))
		{
			registers[(int)op1.getUnsigned()].set(op2.getSigned());
		}
		//ALU opcodes
		if(IR.getBit(15) == true)
		{
			LongWord destination = new LongWord();
			destination = alu.operate(0, thirdByteMask, IR);
			LongWord shift = new LongWord();
			shift.set(8);
			destination = alu.operate(6,  destination, shift);
			registers[(int)destination.getUnsigned()].copy(result);
		}
	}
	//function to load in instructions, String of 2 bytes given then converted into array of longwords
	public void preLoad(String[] instructions) throws Exception
	{
		LongWord updatePC = new LongWord();
		updatePC.set(2);
		LongWord[] toWrite = new LongWord[instructions.length]; 
		//getting rid of spaces
		for(int i = 0; i < toWrite.length; ++i)
			toWrite[i] = new LongWord();
		//creating longword instruction
		for(int i = 0; i < instructions.length; ++i)
		{
			instructions[i] = instructions[i].replaceAll(" ", "");
			int wordLength = 0;
			for(int j = instructions[i].length() - 1; j >= 0; --j)
			{
				if(instructions[i].charAt(j) == '1')
					toWrite[i].setBit(wordLength);
				++wordLength;
			}
			memory.write(PC, toWrite[i], 2);
			PC = PCalu.operate(3, PC, updatePC);
		}
		//setting pc to 0 so run() starts in correct place
		PC.set(0);
	}
}
