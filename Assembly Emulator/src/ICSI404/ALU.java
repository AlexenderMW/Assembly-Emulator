package ICSI404;
//This class imitates an ALU, it will accept ALU codes with 1 0r 2 operands and output what that code maps to.
public class ALU {
	//status where the LSB is the ZF, then next is the NF, the next is the CF, the next is the OF flag
	private LongWord programStatusWord = new LongWord();
	ALU()
	{
		
	}
	//Mian controller to interpret ALU code and calling the correct function it also updates the relavent staus flags
	public LongWord operate(int code, LongWord op1, LongWord op2) throws Exception
	{
		LongWord newWord = new LongWord();
		switch(code)
		{
			//AND function, ALU status is cleared
			case 0:
				this.clearStatus();
				newWord = op1.and(op2);
				if(newWord.getSigned() == 0)
					programStatusWord.setBit(0);
				if(newWord.getBit(31))
					programStatusWord.setBit(1);
				break;
				//OR function, ALU status is cleared
			case 1:
				this.clearStatus();
				newWord = op1.or(op2);
				if(newWord.getSigned() == 0)
					programStatusWord.setBit(0);
				if(newWord.getBit(31))
					programStatusWord.setBit(1);
				break;
				//XOR function if op2 == -1, ALU status is cleared and NOT
			case 2:
				this.clearStatus();
				if(op2.getSigned() == -1)
				{
					newWord = op1.not();
					if(newWord.getSigned() == 0)
						programStatusWord.setBit(0);
					if(newWord.getBit(31))
						programStatusWord.setBit(1);
				}
				//xor
				else
				{
				newWord = op1.xor(op2);
				if(newWord.getSigned() == 0)
					programStatusWord.setBit(0);
				if(newWord.getBit(31))
					programStatusWord.setBit(1);
				}
				break;
				//ADD function, ALU status is cleared
			case 3:
				this.clearStatus();
				newWord = rippleCarryAdd(op1, op2, false);
				if(newWord.getSigned() == 0)
					programStatusWord.setBit(0);
				if(newWord.getBit(31))
					programStatusWord.setBit(1);
				break;
				//SUB function, ALU status is cleared
			case 4:
				this.clearStatus();
				op2 = op2.not();
				newWord = rippleCarryAdd(op1, op2, true);
				if(newWord.getSigned() == 0)
					programStatusWord.setBit(0);
				if(newWord.getBit(31))
					programStatusWord.setBit(1);
				break;
				//SLL function, ALU status is cleared, checks OF flag
			case 5:
				this.clearStatus();
				if(op2.getUnsigned() == 1)
					if(!op1.getBit(31))
						programStatusWord.setBit(3);
				newWord = op1.shiftLeftLogical(op2.getSigned());
				if(newWord.getSigned() == 0)
					programStatusWord.setBit(0);
				if(newWord.getBit(31))
					programStatusWord.setBit(1);
				break;
				//SRL function, ALU status is cleared
			case 6:
				this.clearStatus();
				newWord = op1.shiftRightLogical(op2.getSigned());
				if(newWord.getSigned() == 0)
					programStatusWord.setBit(0);
				break;
				//SRA function, ALU status is cleared
			case 7:
				this.clearStatus();
				newWord = op1.shiftRightArithmetic(op2.getSigned());
				if(newWord.getSigned() == 0)
					programStatusWord.setBit(0);
				if(newWord.getBit(31))
					programStatusWord.setBit(1);
				break;
		}
		return newWord;
	}
	//The ripple carry adder for 2 base 2 numbers.  Returns new number, throws flags where appropriate for ZF NF CF OF.
	private LongWord rippleCarryAdd(LongWord a, LongWord b, boolean cin) throws Exception
	{
		//checks if longword b is negative, if so it should be the inverse so just add 1 and the function can continue
		if(cin)
		{
			Boolean overFlow = true;
			for(int i = 0; (i < 32) && overFlow; ++i)
			{
				if(b.getBit(i))
					b.clearBit(i);
				else
				{
					b.setBit(i);
					overFlow = false;
				}
			}
		}
		//creating new longword not touching the 31st bit but saving the overflow
		LongWord sumWord = new LongWord();
		Boolean overflow = false;
		for(int i = 0; i < 31; ++i)
		{
			//checking if there is only 1 set bit, if so sumWord bit is set
			if(a.getBit(i) ^ b.getBit(i))
			{
				if(!overflow)
					sumWord.setBit(i);
			}
			//checking if both bits are set, if so checking for overflow
			else if(a.getBit(i) && b.getBit(i))
			{
				if(overflow)
					sumWord.setBit(i);
				overflow = true;
			}
			else
			{
				if(overflow == true)
				{
					sumWord.setBit(i);
						overflow = false;
				}
			}
		}
		//if 1 bit is set and overflow is true then set CF to true
		if((a.getBit(31) ^ b.getBit(31)) && overflow)
		{
			programStatusWord.setBit(2);
		}
		//if either 0 bits are set or both
		if(!(a.getBit(31) ^ b.getBit(31)))
		{
			//if 2 bits are set and no overflow set OF flag
			if((a.getBit(31) && b.getBit(31)) && !overflow)
				programStatusWord.setBit(3);
			//if 0 bits are set and overflow set OF flag
			else if(!(a.getBit(31) && b.getBit(31)) && overflow)
				programStatusWord.setBit(3);
			//if 2 bits are set and overflow set CF flag and set number to MSB to 1
			else if(a.getBit(31) && b.getBit(31) && overflow)
			{
				programStatusWord.setBit(2);
				sumWord.setBit(31);
			}
		}
		else if(a.getBit(31) ^ b.getBit(31) ^ overflow)
		{
			sumWord.setBit(31);
		}
		return sumWord;
	}
	//get function of status, returns ALU flags
	public LongWord getStatus()
	{
		return this.programStatusWord;
	}
	//helper function for legibility
	private void clearStatus()
	{
		this.programStatusWord.set(0);
	}

}
