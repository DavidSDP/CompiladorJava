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
STREQUALS
; ============================================================
; INPUT		- A0.L -> Puntero a descriptor 1
;			- A1.L -> Puntero a descriptor 2
; OUTPUT	- D0.L -> #0 si falso, #1 si verdadero
; ============================================================
        	MOVE.L	(A0),D0
        	CMP.L	(A1),D0
        	BNE		.STRNOTEQ
        	SUBQ 	#1, D0
			MOVE.L	4(A0),A0
			MOVE.L	4(A1),A1
.LOOPSTR
        	CMP.W	(A0)+, (A1)+
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


*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
