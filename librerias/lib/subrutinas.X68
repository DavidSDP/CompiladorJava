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
; MODIFICA  - -8(BP) con la @ del String descriptor
; OUTPUT	- NADA
; ============================================================
            MOVEM.L D0-D1/A0-A2, -(A7)
            
            MOVE.L  #8, D0
            JSR     DMMALLOC
            MOVE.L  A0, A2      ; A2.L -> @ String Descriptor
            
            MOVE.W  #0, 2(A2)   ; Save #0 references
            
            JSR     DMMALLOCNORMAL  ; A0.L -> @ String content
            MOVE.L  A0, A1      ; A1.L -> @ String content
            
            MOVE.B  #2, D0      ; Lectura de input (Task 2, TRAP 15)
            TRAP    #15         ; D1.W -> length (Limite 80)
            
            ADD.W   #1, D1
            MOVE.W  D1, (A2)    ; Save String size
            MOVE.L  A1, 4(A2)   ; Save String content Address (@)
            
            MOVE.L  BP, A1
            SUB.L   #8, A1
            MOVE.L  A2, (A1)    ; Retorno de String descriptor @
            
            MOVEM.L (A7)+, D0-D1/A0-A2
            RTS
            
; ============================================================
WRTSTRLN
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
WRITESTR
; ============================================================
; INPUT		- A0.L -> Puntero a descriptor de String
; MODIFICA  - NADA
; OUTPUT	- NADA
; ============================================================
            MOVEM.L D0-D1/A1, -(A7)
        	MOVE.W	(A0), D1    ; Tamanyo String
            
            MOVE.B  #14, D0
            MOVE.L  4(A0), A1
            TRAP    #15         ; Escritura por pantalla (Task 14, TRAP 15)
            
            MOVEM.L (A7)+, D0-D1/A1
            RTS
; ============================================================
WRITEINT
; ============================================================
; INPUT		- D0.W -> Valor entero
; MODIFICA  - NADA
; OUTPUT	- NADA
; ============================================================
            MOVEM.L D1-D2, -(A7)
            
            AND.L   #$0000FFFF, D0
            MOVE.L  D0, D1
            MOVE.B  #10, D2
            MOVE.W  #15, D0
            TRAP    #15         ; Escritura por pantalla (Task 15, TRAP 15)
            
            MOVEM.L (A7)+, D1-D2
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
                MOVE.L  A0, A1
                MOVE.L  4(A0), A0       ; Free the content
                JSR     DMMFREE
                MOVE.L  A1, A0          ; Free the descriptor
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
