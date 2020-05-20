package CodigoMaquina;

public enum OpCode {
	ADD,ADDA,ADDI,ADDQ,AND,ANDI,BRA,BEQ,BNE,BLT,BGT,BGE,BLE,BSR,BTST,CHK,CLR,
	CMP,DBRA,DBEQ,DBNE,DBLT,DBGT,DBGE,DBLE,DIVS,DIVU,EOR,JMP,JSR,LEA,LSL,LSR,
	MOVE,MOVEA,MOVEM,MOVEQ,MULS,MULU,NEG,NOT,OR,ORI,ROL,ROR,RTE,RTR,RTS,SEQ,
	SNE,SLT,SGT,SGE,SLE,SUB,SUBA,SUBI,SUBQ,SWAP,TRAP,TST;
	
	public String __() {
		return this.name();
	}
	
}
