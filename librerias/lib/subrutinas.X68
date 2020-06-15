; ============================================================
;					LIBRERIA DE SUBRUTINAS
; ============================================================

; =============================================================================
; Adds the internal information needed for the activation block.
;
; Information added:
;   - Space to allow return a value if needed
;   - Access link (not yet)
;   - Last activation blocks address
;
; Finally, updates the current BP
;
;
;
;  Modified registers: D4, A5
; =============================================================================

UPDATE_BP
    		MOVE.L	STACK_TOP, A5				; Access link must be handled by every single function call
    		ADDQ.L	#2, A5						; Place the pointer on the address for the new BP
    		MOVE.L 	BP, D4						; Move BP to new AB
    		MOVE.L 	D4, (A5)
    		MOVE.L 	A5, BP
				ADDQ.L	#2, A5
				MOVE.L 	A5, STACK_TOP
    		RTS

RESTORE_BP
    	MOVE.L	BP, A4
			MOVE.L	(A4),BP
			RTS

; ============================================================
READ
; ============================================================
; INPUT		- NADA
; MODIFICA  - NADA
; OUTPUT	- A1.L: Direccion del Descriptor del String leido.
; ============================================================
            MOVEM.L D0-D1/A1-A2, -(A7)
            
            MOVE.B  #2, D0      ; Lectura de input (Task 2, TRAP 15)
            TRAP    #15         ; D1.W -> length
            MOVE.L  A1, A2      ; A2.L -> @ String
            
            MOVE.L  #8, D0
            JSR     DMMALLOC
            MOVE.L  A0, A1
            MOVE.W  D1, (A1)
            MOVE.W  #0, 2(A1)
            ADD.W   #1, D1
            MOVE.L  D1, D0
            JSR     DMMALLOC
            MOVE.L  A0, 4(A1)   ; A1 -> @ String Descriptor
            SUB.W   #1, D1
.READCNT
            MOVE.B  (A2)+, (A0)+
            DBRA.W  D1, .READCNT
            MOVE.B  #0, (A0)+
            MOVE.L  A1, A0
            MOVE.L  BP, A1
            SUB.L   #8, A1
            MOVE.L  A0, (A1)
            MOVEM.L (A7)+, D0-D1/A1-A2
            RTS
            
; ============================================================
STRWRITE
; ============================================================
; INPUT		- A0.L -> Puntero a descriptor de String
; MODIFICA  - NADA
; OUTPUT	- NADA
; ============================================================
            MOVEM.L D0-D1/A1, -(A7)
        	MOVE.W	(A0), D1    ; Tamanyo String
            
            MOVE.B  #0, D0
            MOVE.L  4(A0), A1
            TRAP    #15         ; Escritura por pantalla (Task 0, TRAP 15)
            
            MOVEM.L (A7)+, D0-D1/A1
            RTS
            
; ============================================================
STREQUALS
; ============================================================
; INPUT		- A0.L -> Puntero a descriptor 1
;			- A1.L -> Puntero a descriptor 2
; OUTPUT	- D0.L -> #0 si falso, #1 si verdadero
; ============================================================
        	MOVE.W	(A0),D0
        	CMP.W	(A1),D0
        	BNE		.STRNOTEQ
        	SUBQ 	#1, D0
			MOVE.L	4(A0),A0
			MOVE.L	4(A1),A1
.LOOPSTR
        	CMP.B	(A0)+, (A1)+
        	BNE 	.STRNOTEQ
        	DBRA.W 	D0, .LOOPSTR
        	BRA		.STREQUAL
.STRNOTEQ
        	CLR.L 	D0
        	BRA 	.STREND
.STREQUAL
        	MOVE.L 	#1, D0
.STREND 
			RTS

; ============================================================
DECREASEREF
; ============================================================
; INPUT		- A0.L -> Puntero a descriptor
; OUTPUT	- 
; ============================================================
                MOVE.W  2(A0), D0
                SUBQ.W  #1, D0
                CMP     #0, D0
                BNE     .UPDATE_REF
                JSR     DMMFREE
                MOVE.L  #0, A0
                BRA     .END
.UPDATE_REF:
                MOVE.W  D0, 2(A0)
.END:
                RTS

*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
