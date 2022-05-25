package ICSI404;

public class TestLongWord {
	public static void main(String[]args) throws Exception 
	{
		TestLongWord test = new TestLongWord();
		test.runTests();
	}
	public void runTests() throws Exception
	{
		LongWord word = new LongWord();
		//value tests
		System.out.println("value tests, for of decimal, binary, and hex\n");
		word.set(Integer.MAX_VALUE);
		int a = word.getSigned();
		System.out.println(a);
		System.out.println(word.toString());
		word.set(Integer.MIN_VALUE + 1);
		a = word.getSigned();
		System.out.println(a);
		System.out.println(word.toString());
		word.set(-1);
		a = word.getSigned();
		System.out.println(a);
		System.out.println(word.toString());
		word.set(100);
		a = word.getSigned();
		System.out.println(a);
		System.out.println(word.toString());
		word.toggleBit(10);
		a = word.getSigned();
		System.out.println(a);
		System.out.println(word.toString());
		long b = word.getUnsigned();
		System.out.println(b);
		System.out.println(word.toString());
		//function tests
		LongWord word2 = new LongWord();
		word2.set(-716410);
		System.out.println("checking functions\n");
		System.out.println(word2.toString());
		System.out.println("SetBit 9\n");
		word2.setBit(8);
		System.out.println(word2.toString());
		
		System.out.println("clearBit 9\n");
		word2.clearBit(8);
		System.out.println(word2.toString());
		
		System.out.println("copy\n");
		LongWord newWord = new LongWord();
		newWord.copy(word2);
		System.out.println(newWord.toString());
		
		System.out.println("leftShiftLogical by 3\n");
		System.out.println(word2.toString());
		LongWord newWord2 = word2.shiftLeftLogical(3);
		System.out.println(newWord2.toString());
		
		System.out.println("shiftRightArithmetic by 3\n");
		System.out.println(word2.toString());
		LongWord newWord3 = word2.shiftRightArithmetic(3);
		System.out.println(newWord3.toString());
		
		System.out.println("shiftRightLogical by 4\n");
		System.out.println(word2.toString());
		LongWord newWord4 = word2.shiftRightLogical(4);
		System.out.println(newWord4.toString());
		
		System.out.println("logic operations original/comparison and, or, xor, not, in that order\n");
		System.out.println(word2.toString());
		System.out.println(newWord4.toString());
		LongWord newWord5 = word2.and(newWord4);
		System.out.println(newWord5.toString());
		newWord5 = word2.or(newWord4);
		System.out.println(newWord5.toString());
		newWord5 = word2.xor(newWord4);
		System.out.println(newWord5.toString());
		newWord5 = word2.not();
		System.out.println(newWord5.toString());
		System.out.println("isZero\n");
		LongWord word3 = new LongWord();
		word3.set(0);
		System.out.println(word3.toString());
		System.out.println(word3.isZero());
		
		System.out.println("\n\n\n----------------New Tests\n\n\n");
		ALU alu = new ALU();
		LongWord op1 = new LongWord();
		LongWord op2 = new LongWord();
		LongWord result = new LongWord();
		//testing ALU commands in order

		
		//ADD please change numbers here
		result.set(0);
		System.out.println("ADD\n");
		op1.set(-7);
		System.out.println(op1.toString());
		System.out.println(op1.getSigned());
		op2.set(234);
		System.out.println(op2.toString());
		System.out.println(op2.getSigned());
		result = alu.operate(4 , op1, op2);
		System.out.println(result.toString());
		System.out.println(result.getSigned());
		System.out.println(status(alu.getStatus()));
		
		//SUB please change numbers here
		result.set(0);
		System.out.println("SUB\n");
		op1.set(-7);
		System.out.println(op1.toString());
		System.out.println(op1.getSigned());
		op2.set(234);
		System.out.println(op2.toString());
		System.out.println(op2.getSigned());
		result = alu.operate(5 , op1, op2.not());
		System.out.println(result.toString());
		System.out.println(result.getSigned());
		System.out.println(status(alu.getStatus()));
		
		//SLL
		result.set(0);
		System.out.println("SLL\n");
		op1.set(-7);
		System.out.println(op1.toString());
		op2.set(1);
		System.out.println(op2.toString());
		result = alu.operate(6 , op1, op2);
		System.out.println(result.toString());
		System.out.println(status(alu.getStatus()));
		
		//SRL
		result.set(0);
		System.out.println("SRL\n");
		op1.set(-7);
		System.out.println(op1.toString());
		op2.set(3);
		System.out.println(op2.toString());
		result = alu.operate(7 , op1, op2);
		System.out.println(result.toString());
		System.out.println(status(alu.getStatus()));
		
		//SLL
		result.set(0);
		System.out.println("SRA\n");
		op1.set(-7);
		System.out.println(op1.toString());
		op2.set(2);
		System.out.println(op2.toString());
		result = alu.operate(8 , op1, op2);
		System.out.println(result.toString());
		System.out.println(status(alu.getStatus()));
		
	}
	public String status(LongWord status) throws Exception
	{
		String string = new String();
		string = "";
		if(status.getBit(0))
			string = string + "ZF ";
		else
			string = string + "!ZF ";
		if(status.getBit(1))
			string = string + "NF ";
		else
			string = string + "!NF ";
		if(status.getBit(2))
			string = string + "CF ";
		else
			string = string + "!CF ";
		if(status.getBit(3))
			string = string + "OF ";
		else
			string = string + "!OF ";
		return string;
	}
}
