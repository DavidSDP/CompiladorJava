*-----------------------------------------------------------
*      Assembly template used when generating code
*
*
* The next registers are used in an "special" manner:
*   - A5: Used as an stack
*         to perform all the activation blocks creation
*
*-----------------------------------------------------------
    ORG    $1000

; Every single param push handles its own load and save of the 
; stack pointer in order to keep all the things independent.
PUSH_PARAM MACRO
		move.w STACK_TOP, A5
    move.w \1, (A5)
    add.w \2, A5
		move.w A5, STACK_TOP
    ENDM 

START:
; Ensure the start status is correct. That means,
; STACK_TOP is pointing to the top of the stack
; and the system is using user privileges.
    move.w #0, SR
    move.w #ACTIVATION_BLOCK_STACK, STACK_BASE
    move.w #ACTIVATION_BLOCK_STACK, STACK_TOP 
; Setup initial state of the activation block stack
		move.w STACK_TOP, A6
		addq.w #2, A6
		move.w A6, BP
    move.w A6, STACK_TOP


;{Generate Code here}
		
    SIMHALT             ; halt simulator    

************************************************************    
* Adds the internal information needed to the activation
* block.
* 
* Information added:
*   - Space to allow return a value if needed
*   - Access link (not yet)
*   - Last activation blocks address
* 
* Finally, updates the current BP
*
*
*	
*  Modified registers: D4, A5
************************************************************

update_bp:
		; Access link must be handled by every single function call
		move.w STACK_TOP, A5
		; Place the pointer on the address for the new BP
    add.w #2, A5
    ; Move BP to new AB
    move.w BP, D4
    move.w D4, (A5)
    move.w A5, BP
		move.w A5, STACK_TOP
    rts

restore_bp:
    move.w BP, A4
		move.w (A4), BP
		rts
    
STACK_BASE dc.w 0
STACK_TOP dc.w 0
BP dc.w 0

; Act as start of stack
ACTIVATION_BLOCK_STACK ds.w 0

; leave space between the activation blocks and the heap
    ORG $70000
MONTICULO_START ds.w 0

    END    START        ; last line of source


*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
