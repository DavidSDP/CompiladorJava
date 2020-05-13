; ============================================================
;					LIBRERIA DE MACROS
; ============================================================


; ============================================================
; Every single param push handles its own load and save of the 
; stack pointer in order to keep all the things independent.
; ============================================================

PUSH_PARAM MACRO
			move.w 	STACK_TOP, A5
   			move.w 	\1, (A5)
    		add.w 	\2, A5
			move.w 	A5, STACK_TOP
    		ENDM

*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~