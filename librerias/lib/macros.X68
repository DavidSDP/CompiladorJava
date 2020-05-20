; ============================================================
;					LIBRERIA DE MACROS
; ============================================================


; ============================================================
; Every single param push handles its own load and save of the 
; stack pointer in order to keep all the things independent.
; ============================================================

PUSH_PARAM MACRO
			MOVE.L 	STACK_TOP, A5
   		MOVE.L 	\1, (A5)
    	ADD.L 	\2, A5
			MOVE.L 	A5, STACK_TOP
    	ENDM

PUSH_DESCRIPTOR_PARAM MACRO
			MOVE.L 	STACK_TOP, A5
   		MOVE.L 	0(\1), (A5)
    	MOVE.L 	4(\1), 4(A5)
			ADDQ.L	#8, A5
			MOVE.L 	A5, STACK_TOP
    	ENDM
*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
