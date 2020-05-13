; =============================================================================
;      Assembly template used when generating code
;
;
; The next registers are used in an "special" manner:
;   - A5: Used as an stack
;         to perform all the activation blocks creation
;
; =============================================================================
    		ORG    $1000
    		
    		INCLUDE "lib/sysconst.X68"							; LIBRERIA DE SUBRUTINAS
    		INCLUDE "lib/subrutinas.X68"						; LIBRERIA DE SUBRUTINAS
    		INCLUDE "lib/macros.X68"							; LIBRERIA DE MACROS
    		INCLUDE "lib/dmm.X68"								; GESTION DE MEMORIA DINAMICA

START		JSR 	DMMINIT									; Initialize dynamic memory

			MOVE.W	#0, SR                                	; Ensure the start status is correct. That means,
    		MOVE.W 	#ACTIVATION_BLOCK_STACK, STACK_BASE   	; STACK_TOP is pointing to the top of the stack
    		MOVE.W 	#ACTIVATION_BLOCK_STACK, STACK_TOP    	; and the system is using user privileges.

			MOVE.W 	STACK_TOP, A6							; Setup initial state of the activation block stack
			ADDQ.W 	#2, A6
			MOVE.W 	A6, BP
    		MOVE.W 	A6, STACK_TOP

			;{Generate Code here}
			
			SIMHALT											; halt simulator

STACK_BASE 				DC.W 	0
STACK_TOP 				DC.W 	0
BP 						DC.W	0

ACTIVATION_BLOCK_STACK 	DS.W 0								; Act as start of stack
			
			ORG 	$70000									; leave space between the activation blocks and the heap
            
            INCLUDE "lib/sysvars.X68"

    		END    	START        							; last line of source

*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
