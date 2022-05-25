package ICSI404;

public class TestComputer {

	public static void main(String[]args) throws Exception 
	{
		TestComputer test =  new TestComputer();
		test.runTests();
	}
	public void runTests() throws Exception
	{
		//MOV, ALU, INTERUPT, and HALT tests
		Computer computer = new Computer();
		Memory memoryTest = new Memory();
		//testing mov, int 1, int 2, and halt
		String mov = "0010 0001 0000 1010"; //MOV R1 10
		String mov2 = "0010 0001 1001 1100"; //MOV R1 -100
		String interupt1 = "0001 0000 0000 0000";//INT 0
		String interupt0 = "0001 0000 0000 0001";//INT 1
		String halt = "0000 0000 0000 0000";//HALT
		String[] instructions = {mov, mov2, interupt1, interupt0, halt};
		computer.preLoad(instructions);
		computer.run();
		/*
		String mov = "0010 0001 0000 1010"; //MOV R1 10
		String mov2 = "0010 0010 1001 1100"; //MOV R2 -100
		String and = "1000 0011 0010 0001"; //A R3 R2 R1 
		String or = "1001 0100 0010 0001"; //A R4 R2 R1 
		String xor = "1010 0101 0010 0001"; //A R5 R2 R1 
		String mov3 = "0010 0110 1111 1111"; //MOV R6 -1 used for not
		String not = "1010 0111 0010 0011"; //A R7 R2 R1 
		String add = "1011 1000 0010 0111"; //A R8 R2 R1 
		String sub = "1100 1001 0010 0001"; //A R9 R2 R1 should be -90 (x x x x x x 1010 0110 where x is "1111")
		String mov4 = "0010 0001 0000 0010"; //MOV R1 2
		String sll = "1101 1010 0010 0001"; //A R10 R2 R1 
		String srl = "1110 1011 0010 0001"; //A R11 R2 R1 
		String sra = "1111 1100 0010 0001"; //A R12 R2 R1 
		String interupt = "0001 0000 0000 0000"; //interrupt, print all registers
		String halt = "0000 0000 0000 0000"; //halt
		String[] instructions = {mov, mov2, and, or, xor, mov3, not, add, sub, mov4, sll, srl, sra, interupt, halt};
		computer.preLoad(instructions);
		computer.run();
		*/
		//testing JUMP
		/*
		System.out.println("----------------NEW TESTS------------------");
		Computer computer2 = new Computer();
		String jump = "0011 0000 0000 0101"; //JUMP to address 4 should skip mov5
		String mov5 = "0010 0001 0111 1111"; //MOV R1 127
		String mov6 = "0010 0001 0111 1111"; //MOV R1 127
		String mov7 = "0010 0001 0111 1111"; //MOV R1 127
		String mov8 = "0010 0001 0111 1111"; //MOV R1 127
		String interupt = "0001 0000 0000 0000"; //interrupt, print all registers
		String halt = "0000 0000 0000 0000"; //halt
		String[] instructions2 = {jump, mov5, mov6, mov7, mov8, interupt, halt};
		computer2.preLoad(instructions2);
		computer2.run();
		*/
		//testing CMP and BRANCH
		/*
		Computer computer3 = new Computer();
		String mov1 = "0010 0001 0001 0010"; //MOV R1 18 PC = 0
		String mov2 = "0010 0010 0001 0110"; //MOV R2 22 PC = 2
		String mov3 = "0010 0011 0000 0010"; //MOV R3 2 PC = 4
		String add = "1011 0001 0001 0011"; //ADD R1 R1 R3 PC = 6
		String cmp = "0100 0000 0001 0010";//CMP R1 R2 PC = 8
		String bne = "0101 0011 1111 1101";//BNE -6 should go to PC = 6 and PC = 10 Branch should happen once
		String interupt = "0001 0000 0000 0000"; //interrupt, print all registers
		String halt = "0000 0000 0000 0000"; //halt
		String[] instructions3 = {mov1, mov2, mov3, add, cmp, bne, interupt, halt};
		computer3.preLoad(instructions3);
		computer3.run();
		*/
		/*
		Computer computer3 = new Computer();
		String mov1 = "0010 0001 0001 0010"; //MOV R1 18 PC = 0
		String mov2 = "0010 0010 0001 0110"; //MOV R2 22 PC = 2
		String mov3 = "0010 0011 0000 0010"; //MOV R3 2 PC = 4
		String add = "1011 0001 0001 0011"; //ADD R1 R1 R3 PC = 6
		String cmp = "0100 0000 0001 0010";//CMP R1 R2 PC = 8
		String blt = "0101 0111 1111 1101";//BLT -6 should go to PC = 6 and PC = 10 Branch should happen once
		//String beq = "0101 10XX XXXX XXXX";
		//String ble = "0101 11XX XXXX XXXX";
		String interupt = "0001 0000 0000 0000"; //interrupt, print all registers
		String halt = "0000 0000 0000 0000"; //halt
		String[] instructions3 = {mov1, mov2, mov3, add, cmp, blt, interupt, halt};
		computer3.preLoad(instructions3);
		computer3.run();
		*/
		/*
		Computer computer3 = new Computer();
		String mov1 = "0010 0001 0001 0100"; //MOV R1 20 PC = 0
		String mov2 = "0010 0010 0001 0110"; //MOV R2 22 PC = 2
		String mov3 = "0010 0011 0000 0010"; //MOV R3 2 PC = 4
		String add = "1011 0001 0001 0011"; //ADD R1 R1 R3 PC = 6
		String cmp = "0100 0000 0001 0010";//CMP R1 R2 PC = 8
		String beq = "0101 1011 1111 1101";//Beq -6 should go to PC = 6 and PC = 10 Branch should happen once
		//String ble = "0101 11XX XXXX XXXX";
		String interupt = "0001 0000 0000 0000"; //interrupt, print all registers
		String halt = "0000 0000 0000 0000"; //halt
		String[] instructions3 = {mov1, mov2, mov3, add, cmp, beq, interupt, halt};
		computer3.preLoad(instructions3);
		computer3.run();
		*/
		/*
		Computer computer3 = new Computer();
		String mov1 = "0010 0001 0001 0010"; //MOV R1 18 PC = 0
		String mov2 = "0010 0010 0001 0110"; //MOV R2 22 PC = 2
		String mov3 = "0010 0011 0000 0010"; //MOV R3 2 PC = 4
		String add = "1011 0001 0001 0011"; //ADD R1 R1 R3 PC = 6
		String cmp = "0100 0000 0001 0010";//CMP R1 R2 PC = 8
		String ble = "0101 1111 1111 1101";//Ble -6 should go to PC = 6 and PC = 10  Branch should happen twice
		String interupt = "0001 0000 0000 0000"; //interrupt, print all registers
		String halt = "0000 0000 0000 0000"; //halt
		String[] instructions3 = {mov1, mov2, mov3, add, cmp, ble, interupt, halt};
		computer3.preLoad(instructions3);
		computer3.run();
		*/
	}
}
