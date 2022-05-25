package ICSI404;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.BitSet;

public class LongWord {
	public BitSet longWord;
	//creating longword with a BitSet of size 32, all bits set to 0
	LongWord()
	{
		this.longWord = new BitSet(32);
		this.longWord.clear();
	}
	//get method for BitSet index
	public boolean getBit(int i) throws Exception // Get bit i as a boolean
	{
		if((i < 0)||(i > 31))
			throw new Exception("index is out of bounds");
		return this.longWord.get(i);
	}
	//mutator method to set index to true
	public void setBit(int i) throws Exception // set bit i (set to true/1)
	{
		if((i < 0)||(i > 31))
			throw new Exception("index is out of bounds");
		this.longWord.set(i);
	}
	//mutator method to set index to flase
	void clearBit(int i) throws Exception // clear bit i (reset to false/0)
	{
		if((i < 0)||(i > 31))
			throw new Exception("index is out of bounds");
		this.longWord.clear(i);
	}
	//mutator method to flip bit at index
	void toggleBit(int i) throws Exception // toggles (flips) bit i
	{
		if((i < 0)||(i > 31))
			throw new Exception("index is out of bounds");
		this.longWord.flip(i);
	}
	public int getSigned()// returns the value of this long-word as an int
	{
		//checking if negative, if true get 2's compliment and save the negative
		Boolean sign = this.longWord.get(31);
		BitSet temp = new BitSet();
		temp = (BitSet) this.longWord.clone();
		if(this.longWord.get(31))
		{
			temp.flip(0, 32);
			Boolean overFlow = true;
			for(int i = 0; (i < 32) && overFlow; ++i)
			{
				if(temp.get(i))
					temp.clear(i);
				else
				{
					temp.set(i, true);
					overFlow = false;
				}
			}
		}
		//calulating decimal
		int decimal = 0;
		for(int i = 0; i < 32; ++i)
		{
			if(temp.get(i))
				decimal += (int)Math.pow(2, i);
		}
		//if negative, not it
		if(sign)
			decimal = -decimal;
		return decimal;
	}
	public long getUnsigned() // returns the value of this long-word as a long
	{
		long decimal = 0;
		//calculting decimal
		for(int i = 0; i < 32; ++i)
		{
			if(this.longWord.get(i))
				decimal += (int)Math.pow(2, i);
		}
		return decimal;
	}
	public void set(int value)// set the value of the bits of this long-word
	{
		//checking if negative
		this.longWord.clear();
		Boolean sign = false;
		if(value < 0)
		{
			sign = true;
			value = -value;
		}
		//converting to base 2
		for(int i = 0; value > 0; ++i)
		{
			if(value % 2 == 0)
				this.longWord.clear(i);
			else
				this.longWord.set(i, true);
			value = value / 2;	
		}
		//negative value turns into 2's compliment
		if(sign)
		{
			this.longWord.flip(0, 32);
			Boolean overFlow = true;
			for(int i = 0; (i < 32) && overFlow; ++i)
			{
				if(this.longWord.get(i))
					this.longWord.clear(i);
				else
				{
					this.longWord.set(i, true);
					overFlow = false;
				}
			}
		}
	}
	// (used for testing only)
	public void copy(LongWord other) // copies the values of the bits from
									//another long-word into this one
	{
		this.longWord = (BitSet)other.longWord.clone();
	}
	public LongWord shiftLeftLogical(int amount) throws Exception// left-shift this long-word by
																// amount bits (padding with 0’s), creates a new long-word
	{
		//Exception handling
		if((amount < 0) || (amount >= 32))
			throw new Exception("shift amount produces undefined behaviour");
		//creating a new LongWord and tempary one for shifting
		LongWord newWord = new LongWord();
		newWord.longWord = (BitSet) this.longWord.clone();
		LongWord tempWord = new LongWord();
		tempWord.longWord = (BitSet) this.longWord.clone();
		//shifting
		while(amount > 0)
		{
			for(int i = 0; i < 31; ++i)
			{
				if(tempWord.longWord.get(i))
					newWord.setBit(i + 1);
				else
					newWord.clearBit(i + 1);
			}
			newWord.clearBit(0);
			tempWord.longWord = (BitSet) newWord.longWord.clone();
			--amount;
		}
		return newWord;
	}
	
	LongWord shiftRightLogical(int amount) throws Exception // right-shift this long-word
															// by amount bits (padding with 0’s), creates new long-word
	{
		//error handling
		if((amount < 0) || (amount >= 32))
			throw new Exception("shift amount produces undefined behaviour");
		LongWord newWord = new LongWord();
		newWord.longWord = (BitSet) this.longWord.clone();
		LongWord tempWord = new LongWord();
		tempWord.longWord = (BitSet) this.longWord.clone();
		//shifting the bit ragardless if negative
		while(amount > 0)
		{
			for(int i = 32; i > 0; --i)
			{
				if(tempWord.longWord.get(i))
					newWord.setBit(i - 1);
				else
					newWord.clearBit(i - 1);
			}
			newWord.clearBit(31);
			tempWord.longWord = (BitSet) newWord.longWord.clone();
			--amount;
		}
		return newWord;
	}
	public LongWord shiftRightArithmetic(int amount) throws Exception// right-shift this long-word
																	// by amount bits (sign-extending), creates a new long-word
	{
		//error handling
		if((amount < 0) || (amount >= 32))
			throw new Exception("shift amount produces undefined behaviour");
		LongWord newWord = new LongWord();
		newWord.longWord = (BitSet) this.longWord.clone();
		LongWord tempWord = new LongWord();
		tempWord.longWord = (BitSet) this.longWord.clone();
		//shifting with regard to sign bit
		while(amount > 0)
		{
			for(int i = 32; i > 0; --i)
			{
				if(tempWord.longWord.get(i))
					newWord.setBit(i - 1);
				else
					newWord.clearBit(i - 1);
			}
			newWord.clearBit(31);
			tempWord.longWord = (BitSet) newWord.longWord.clone();
			--amount;
		}
		if(this.longWord.get(31))
			newWord.setBit(31);
		return newWord;
	}
	public LongWord not() // negate this LongWord, creating another
	{
		LongWord newWord = new LongWord();
		BitSet temp = (BitSet) this.longWord.clone();
		temp.flip(0, 32);
		newWord.longWord = temp;
		return newWord;
	}
	public LongWord and(LongWord other) // and two LongWords, returning a third
	{
		LongWord newWord = new LongWord();
		BitSet temp = (BitSet) this.longWord.clone();
		temp.and(other.longWord);
		newWord.longWord = (BitSet)temp.clone();
		return newWord;
	}
	public LongWord or(LongWord other) // or two LongWords, returning a third
	{
		LongWord newWord = new LongWord();
		BitSet temp = (BitSet) this.longWord.clone();
		temp.or(other.longWord);
		newWord.longWord = temp;
		return newWord;
	}
	public LongWord xor(LongWord other) // xor two LongWords, returning a third
	{
		LongWord newWord = new LongWord();
		BitSet temp = (BitSet) this.longWord.clone();
		temp.xor(other.longWord);
		newWord.longWord = temp;
		return newWord;
	}
	public boolean isZero() // returns true if all bits are 0’s in this long-word
	{
		return this.longWord.isEmpty();
	}
	//to string method that out puts a BitSet as binary and hex numbers
	@Override
	public String toString()
	{
		String longWordString = "";
		for(int i = 31; i >= 0; --i)
		{
			if(longWord.get(i) == false)
				longWordString += "0";
			else
				longWordString += "1";
		}
		long decimal = Long.valueOf(longWordString,2);
		String hexStr = Long.toString(decimal,16);
		longWordString = "";
		for(int i = 31; i >= 0; --i)
		{
			if(((i + 1) % 4) == 0)
				longWordString += "\t";
			if(longWord.get(i) == false)
				longWordString += "0";
			else
				longWordString += "1";
		}
		return longWordString + "\t" + "0x" + hexStr;
	}
}
