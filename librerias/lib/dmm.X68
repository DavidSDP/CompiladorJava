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
            MOVEM.L     D0/A0-A1,-(A7)
            LEA         HEAP_MEMORY, A0
            MOVE.L      #DMMBNUMSMALL-1, D0
            MOVE.L      #0, A1 ; Previous/Next
            ; First of all we're gonna place the small blocks.

.LOOP_SMALL
            ; Fill header info
            MOVE.L      A1, 0(A0)            ; Previous block
            MOVE.W      #1, 4(A0)            ; Free state
            MOVE.W      #DMMBSIZESMALL, 6(A0)     ; Content size

            ; Fill trailer info
            MOVE.L      #DMMBSIZESMALL+METAINFOSIZE, D1
            MOVE.W      #DMMBSIZESMALL, 0(A0, D1) ; Content size
            MOVE.W      #1, 2(A0, D1)        ; Free state
            MOVEA.L     A0, A1
            ADD.L       #DMMBSIZESMALL+METAINFOSIZE*2, A1
            MOVE.L      A1, 4(A0, D1)        ; Next block

            ; Prepare for next loop
            MOVE.L      A0, A1
            ADD.L       #DMMBSIZESMALL+METAINFOSIZE*2, A0
            DBRA.W      D0, .LOOP_SMALL

            MOVE.L      #DMMBNUM-1, D0
.LOOP
            ; Fill header info
            MOVE.L      A1, 0(A0)            ; Previous block
            MOVE.W      #1, 4(A0)            ; Free state
            MOVE.W      #DMMBSIZE, 6(A0)     ; Content size

            ; Fill trailer info
            MOVE.L      #DMMBSIZE+METAINFOSIZE, D1
            MOVE.W      #DMMBSIZE, 0(A0, D1) ; Content size
            MOVE.W      #1, 2(A0, D1)        ; Free state
            MOVEA.L     A0, A1
            ADD.L       #DMMBSIZE+METAINFOSIZE*2, A1
            MOVE.L      A1, 4(A0, D1)        ; Next block

            ; Prepare for next loop
            MOVE.L      A0, A1
            ADD.L       #DMMBSIZE+METAINFOSIZE*2, A0

            DBRA.W      D0, .LOOP
            MOVE.L      #0, -4(A0)
            MOVE.L      #HEAP_MEMORY, FREE_LIST
            MOVE.L      A1, FREE_LIST_TAIL
            MOVEM.L     (A7)+,D0/A0-A1
            RTS

; -----------------------------------------------------------------------------
DMMALLOCSMALL
; SEARCHES THE FIRST FREE SMALL  SLOT, ASSIGNS TO THE SPECIFIED OWNER
; AND RETURNS A POINTER TO THE CORRESPONDING DATA (SLOT ADDRESS + METAINFOSIZE)
; INPUT    - NONE
; OUTPUT   - A0   : POINTER TO THE DATA OR 0 IF NOT FOUND
; MODIFIES - NONE (ASIDE OF OUTPUT)
; -----------------------------------------------------------------------------
            MOVEM.L     D1/A1-A2,-(A7)
            MOVE.L      FREE_LIST, A0
            CMP #0, A0
            BEQ .END
            MOVE.L      #0, D1
            ; Remove the block from the free list
            MOVE.W      6(A0), D1                  ; Get the blocks size
            ADD.L       #METAINFOSIZE, D1
            MOVE.L      4(A0, D1), A1
            MOVE.L      A1, FREE_LIST

            ; Check if this is the last block and handle the trail
            ; if needed
            MOVE.L      FREE_LIST_TAIL, A2
            CMP A0, A2
            BNE .CHECK_NEXT_BLOCK
            ; Clear tail as we're getting the last block
            MOVE.L      #0, FREE_LIST_TAIL

.CHECK_NEXT_BLOCK:
            CMP #0, A1
            BEQ .UPDATE_BLOCK
            MOVE.L      #0, (A1) ; Put to 0 the next block's previous pointer

.UPDATE_BLOCK:
            MOVE.L  #0, 0(A0)      ; Remove previous pointer
            MOVE.W  #0, 4(A0)      ; Set Free state to 0

            MOVE.W  #0, 2(A0, D1)  ; Set Free state to 0
            MOVE.L  #0, 4(A0, D1)  ; Remove pinter to next block
            ADD.L   #METAINFOSIZE, A0
.END        MOVEM.L  (A7)+, D1/A1-A2
            RTS

; -----------------------------------------------------------------------------
DMMALLOCNORMAL
; SEARCHES THE NEXT MEMORY SLOT WITH THE SPECIFIED ID AND OUTPUTS A POINTER
; TO THE CORRESPONDING DATA (SLOT ADDRESS+2)
; INPUT    - D0.W : ID TO SEARCH
;            A0   : POINTER WITHIN THE CURRENT SLOT
; OUTPUT   - A0   : POINTER TO THE DATA OR 0 IF NOT FOUND
; MODIFIES - NONE (ASIDE OF OUTPUT)
; -----------------------------------------------------------------------------
            MOVEM.L  D1-D2/A1-A2,-(A7)
            MOVE.L  FREE_LIST_TAIL, A0
            CMP #0, A0
            BEQ .END
						MOVEQ.L	#0, D1
						MOVE.W	6(A0), D1
						CMP #DMMBSIZE, D1
						BGT .CLEAR  ; The block size is too small. There's no block available
						; Remove the block from the tail of the free list
						MOVE.L (A0), FREE_LIST_TAIL	
						
						MOVE.L FREE_LIST, A2
						CMP A0, A2
						BNE .CHECK_PREVIOUS
						MOVE.L #0, FREE_LIST

.CHECK_PREVIOUS:
						MOVE.L	(A0), A1
            CMP #0, A1
            BEQ .UPDATE_BLOCK
						MOVEQ.L	#0, D2
						MOVE.W	6(A1), D2
						ADD.L		#METAINFOSIZE, D2
						MOVE.L	#0, 4(A1, D2)

.UPDATE_BLOCK:
            MOVE.L  #0, 0(A0)      ; Remove previous pointer
            MOVE.W  #0, 4(A0)      ; Set Free state to 0

						ADD.L		#METAINFOSIZE, D1
            MOVE.W  #0, 2(A0, D1)  ; Set Free state to 0
            MOVE.L  #0, 4(A0, D1)  ; Remove pinter to next block
            ADD.L   #METAINFOSIZE, A0
						BRA .END
.CLEAR			MOVE.L	#0, A0
.END        MOVEM.L	(A7)+, D1-D2/A1-A2
            RTS

; -----------------------------------------------------------------------------
DMMALLOC
; SEARCHES THE FIRST FREE (SYS OWNED) SLOT, ASSIGNS TO THE SPECIFIED OWNER
; AND RETURNS A POINTER TO THE CORRESPONDING DATA (SLOT ADDRESS + METAINFOSIZE)
; INPUT    - D0		: Block size requested
; OUTPUT   - A0   : POINTER TO THE DATA OR 0 IF NOT FREE SPACE
; MODIFIES - NONE (ASIDE OF OUTPUT)
; -----------------------------------------------------------------------------
            CMP #DMMBSIZESMALL, D0
            BGT .DMMALLOCNORMAL
            BSR DMMALLOCSMALL
            BRA .END
.DMMALLOCNORMAL
            BSR DMMALLOCNORMAL
.END
            RTS


; -----------------------------------------------------------------------------
DMMFREESMALL
; FREES THE SLOT PLACING IT AT THE BEGINNING OF THE FREE LIST.
; INPUT    - A0   : POINTER BELONGING TO THE SLOT TO FREE
; OUTPUT   - NONE
; MODIFIES - NONE
; -----------------------------------------------------------------------------
            MOVEM.L D0/A0-A1, -(A7)
            SUB.L   #METAINFOSIZE, A0
            MOVE.L  FREE_LIST, A1

            CMP #0, A1
            BEQ .UPDATE_TAIL
            ; If there's first block, update it's previous pointer to point
            ; to the recently freed block
            MOVE.L  A0, 0(A1)
            BRA .UPDATE_METAINFO

.UPDATE_TAIL
            ; If it's updating the tail it means this is the only block in the
            ; list
            MOVE.L A0, FREE_LIST_TAIL

.UPDATE_METAINFO:
            ; First handle the "left" metainfo block the released block
            ; Fix previous pointer for both, the first block of the free list
            ; and the recently released block
            MOVE.L  #0, 0(A0)
            MOVE.W  #1, 4(A0)

            ; Update the list start pointer
            MOVE.L  A0, FREE_LIST

            ; Now, fix the next pointer and the free flag for the trailing
            ; metainfo block
            MOVEQ.L #0, D0
            MOVE.W  6(A0), D0
            ADDQ.L  #METAINFOSIZE, D0
            MOVE.W  #1, 2(A0, D0)
            MOVE.L  A1, 4(A0, D0)

            MOVEM.L (A7)+, D0/A0-A1
            RTS

; -----------------------------------------------------------------------------
DMMFREENORM
; FREES THE SLOT TO WHICH THE PROVIDED POINTER BELONGS BY SETTING ITS OWNED TO
; SYSTEM (0)
; INPUT    - A0   : POINTER BELONGING TO THE SLOT TO FREE
; OUTPUT   - NONE
; MODIFIES - NONE
; -----------------------------------------------------------------------------
            MOVEM.L D0/A0-A1,-(A7)
            SUB.L   #METAINFOSIZE, A0
            MOVE.L  FREE_LIST_TAIL, A1

            CMP #0, A1
            BEQ .UPDATE_HEAD
            ; If there's last block, update it's next pointer to point
            ; to the recently freed block
            MOVEQ.L #0, D0
            MOVE.W  6(A1), D0
            ADDQ.L  #METAINFOSIZE, D0
            MOVE.L  A0, 4(A1, D0)
            BRA .UPDATE_METAINFO

.UPDATE_HEAD
            ; If it's updating the head it means this is the only block in the
            ; list
            MOVE.L A0, FREE_LIST

.UPDATE_METAINFO:
            ; First handle the "left" metainfo block the released block
            ; Fix previous pointer for both, the first block of the free list
            ; and the recently released block
            MOVE.L  A1, 0(A0)
            MOVE.W  #1, 4(A0)

            ; Update the list start pointer
            MOVE.L  A0, FREE_LIST_TAIL

            MOVE.W  6(A0), D0
            ADDQ.L  #METAINFOSIZE, D0
            ; Now, fix the next pointer and the free flag for the trailing
            ; metainfo block
            MOVE.W  #1, 2(A0, D0)
            MOVE.L  #0, 4(A0, D0)

            MOVEM.L (A7)+,D0/A0-A1
            RTS

; -----------------------------------------------------------------------------
DMMFREE
; FREES THE SLOT TO WHICH THE PROVIDED POINTER BELONGS BY SETTING ITS OWNED TO
; SYSTEM (0)
; INPUT    - A0   : POINTER BELONGING TO THE SLOT TO FREE
; OUTPUT   - NONE
; MODIFIES - NONE
; -----------------------------------------------------------------------------
            MOVE.L      D0, -(A7)
            MOVEQ.L     #0, D0
            ; Get the block size
            MOVE.W      -2(A0), D0
            CMP #DMMBSIZESMALL, D0
            BGT .DMMFREENORM
            BSR DMMFREESMALL
            BRA .END
.DMMFREENORM
            BSR DMMFREENORM
.END
            MOVE.L (A7)+, D0
            RTS
            
*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
