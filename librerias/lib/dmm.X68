; =============================================================================
; DYNAMIC MEMORY MANAGEMENT
; DYNAMIC MEMORY IS A SET OF DMMBNUM SLOTS OF DMMBSIZE BYTES EACH ONE.
; DMMBSIZE MUST BE POWER OF TWO. A SLOT HAS:
; - HEADER  - 3 WORD. Previous block, Free, ContentSize
; - DATA    - DMMBSIZE bytes. May contain vars and data
; - TRAILER - 3 WORD. Previous block, Free, ContentSize
; =============================================================================

; -----------------------------------------------------------------------------
DMMINIT:
; INITIALIZES THE DYNAMIC MEMORY TO ALL OWNED BY SYSTEM.
; INPUT    - NONE
; OUTPUT   - NONE
; MODIFIES - NONE
; -----------------------------------------------------------------------------
            MOVEM.L D0/A0-A1,-(A7)
            LEA     HEAP_MEMORY, A0
            MOVE.W  #DMMBNUM-1, D0
            MOVE.L  #0, A1 ; Previous/Next
.LOOP
            ; Fill header info
            MOVE.L  A1, 0(A0)            ; Previous block
            MOVE.W  #1, 4(A0)            ; Free state
            MOVE.W  #DMMBSIZE, 6(A0)     ; Content size

            ; Fill trailer info
            MOVE.L  #DMMBSIZE+METAINFOSIZE, D1
            MOVE.W  #DMMBSIZE, 0(A0, D1) ; Content size
            MOVE.W  #1, 2(A0, D1)        ; Free state
            MOVEA.L A0, A1
            ADD.L   #DMMBSIZE+METAINFOSIZE*2, A1
            MOVE.L  A1, 4(A0, D1)        ; Next block

            ; Prepare for next loop
            MOVE.L  A0, A1
            ADD.L   #DMMBSIZE+METAINFOSIZE*2, A0

            DBRA.W  D0,.LOOP
            MOVE.L  #0, -4(A0)
            MOVE.L  #HEAP_MEMORY, FREE_LIST
            MOVEM.L (A7)+,D0/A0-A1
            RTS

; -----------------------------------------------------------------------------
DMMFIRST
; SEARCHES THE FIRST MEMORY SLOT NOT OWNED BY SYSTEM AND OUTPUTS A POINTER
; TO THE CORRESPONDING DATA (SLOT ADDRESS+2)
; INPUT    - NONE
; OUTPUT   - A0   : POINTER TO THE DATA OR 0 IF NOT FOUND
; MODIFIES - NONE (ASIDE OF OUTPUT)
; -----------------------------------------------------------------------------
            LEA     HEAP_MEMORY,A0
DMMFRLOP    TST.W   (A0)+
            BNE     DMMFRFND
            ADD.L   #DMMBSIZE-2,A0
DMMFRCOM    CMP.L   #HEAP_MEMORY+DMMBSIZE*DMMBNUM-1,A0
            BLT     DMMFRLOP
            MOVE.L  #0,A0
DMMFRFND    RTS

; -----------------------------------------------------------------------------
DMMNEXT
; SEARCHES THE NEXT MEMORY SLOT NOT OWNED BY SYSTEM AND OUTPUTS A POINTER
; TO THE CORRESPONDING DATA (SLOT ADDRESS+2)
; INPUT    - A0   : POINTER WITHIN THE CURRENT SLOT
; OUTPUT   - A0   : POINTER TO THE DATA OR 0 IF NOT FOUND
; MODIFIES - NONE (ASIDE OF OUTPUT)
; -----------------------------------------------------------------------------
            MOVE.L  D0,-(A7)
            MOVE.L  A0,D0
            AND.L   #-DMMBSIZE,D0           ; TAKE ADVANTAGE OF ALIGNMENT
            ADD.L   #DMMBSIZE,D0
            MOVE.L  D0,A0
            MOVE.L  (A7)+,D0
            BRA     DMMFRCOM

; -----------------------------------------------------------------------------
DMMFRSTO
; SEARCHES THE FIRST MEMORY SLOT WITH THE SPECIFIED ID AND OUTPUTS A POINTER
; TO THE CORRESPONDING DATA (SLOT ADDRESS+6)
; OUTPUT   - A0   : POINTER TO THE DATA OR 0 IF NOT FOUND
; MODIFIES - NONE (ASIDE OF OUTPUT)
; -----------------------------------------------------------------------------
            LEA     HEAP_MEMORY,A0
DMMFLOOP    CMP.W   (A0)+,D0
            BEQ     DMMFFND
            ADD.L   #DMMBSIZE-2,A0
DMMFCOM     CMP.L   #HEAP_MEMORY+DMMBSIZE*DMMBNUM-1,A0
            BLT     DMMFLOOP
            MOVE.L  #0,A0
DMMFFND     RTS

; -----------------------------------------------------------------------------
DMMNEXTO
; SEARCHES THE NEXT MEMORY SLOT WITH THE SPECIFIED ID AND OUTPUTS A POINTER
; TO THE CORRESPONDING DATA (SLOT ADDRESS+2)
; INPUT    - D0.W : ID TO SEARCH
;            A0   : POINTER WITHIN THE CURRENT SLOT
; OUTPUT   - A0   : POINTER TO THE DATA OR 0 IF NOT FOUND
; MODIFIES - NONE (ASIDE OF OUTPUT)
; -----------------------------------------------------------------------------
            MOVE.L  D0,-(A7)
            MOVE.L  A0,D0
            AND.L   #-DMMBSIZE,D0           ; TAKE ADVANTAGE OF ALIGNMENT
            ADD.L   #DMMBSIZE,D0
            MOVE.L  D0,A0
            MOVE.L  (A7)+,D0
            BRA     DMMFCOM

; -----------------------------------------------------------------------------
DMMALLOC
; SEARCHES THE FIRST FREE (SYS OWNED) SLOT, ASSIGNS TO THE SPECIFIED OWNER
; AND RETURNS A POINTER TO THE CORRESPONDING DATA (SLOT ADDRESS + METAINFOSIZE)
; INPUT    - NONE
; OUTPUT   - A0   : POINTER TO THE DATA OR 0 IF NOT FREE SPACE
; MODIFIES - NONE (ASIDE OF OUTPUT)
; -----------------------------------------------------------------------------
            MOVEM.W  D0-D1/A1,-(A7)
            MOVE.L  FREE_LIST, A0
            CMP #0, A0
            BEQ .END
            ; Remove the block from the free list
            MOVE.L  #DMMBSIZE+METAINFOSIZE, D1
            MOVE.L  4(A0, D1), A1
            MOVE.L  A1, FREE_LIST
            CMP #0, 4(A0, D1)
            BEQ .UPDATE_REQUESTED_BLOCK
						MOVE.L	4(A0, D1), A1
						MOVE.L	#0, (A1) ; Put to 0 the next block's previous pointer
.UPDATE_REQUESTED_BLOCK:
            MOVE.L  #0, 0(A0)      ; Remove previous pointer
            MOVE.W  #0, 4(A0)      ; Set Free state to 0

            MOVE.W  #0, 2(A0, D1)  ; Set Free state to 0
            MOVE.L  #0, 4(A0, D1)  ; Remove pinter to next block
            ADD.L   #METAINFOSIZE, A0
.END        MOVEM.W  (A7)+,D0-D1/A1
            RTS

; -----------------------------------------------------------------------------
DMMFREE
; FREES THE SLOT TO WHICH THE PROVIDED POINTER BELONGS BY SETTING ITS OWNED TO
; SYSTEM (0)
; INPUT    - A0   : POINTER BELONGING TO THE SLOT TO FREE
; OUTPUT   - NONE
; MODIFIES - NONE
; -----------------------------------------------------------------------------
            MOVEM.L A0-A1,-(A7)
            SUB.L   #METAINFOSIZE, A0
            MOVE.L  FREE_LIST, A1

            CMP #0, A1
            BEQ .UPDATE_METAINFO
            ; If there's first block, update it's previous pointer to point
            ; to the recently freed block
            MOVE.L  A0, 0(A1)

            ; First handle the "left" metainfo block the released block
            ; Fix previous pointer for both, the first block of the free list
            ; and the recently released block
.UPDATE_METAINFO:
            MOVE.L  #0, 0(A0)
            MOVE.W  #1, 4(A0)

            ; Update the list start pointer
            MOVE.L  A0, FREE_LIST

            ; Now, fix the next pointer and the free flag for the trailing
            ; metainfo block
            ADD.L   #DMMBSIZE+METAINFOSIZE, A0
            MOVE.W  #1, 2(A0)
            MOVE.L  A1, 4(A0)

            MOVEM.L (A7)+,A0-A1
            RTS
            
*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
