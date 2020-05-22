; ============================================================
;					CONSTANTES DEL SISTEMA
;
; Actualmente hay dos tipos de bloques:
;		- Pequeno: de 8 bytes ( 2 longs ) para poder contener
;							 los descriptores de arrays/strings
;		- Normal: de 256 bytes para uso de proposito general
; ============================================================

DMMBNUM						EQU 	100
DMMBNUMSMALL			EQU 	100 		
DMMBSIZE 					EQU 	256
DMMBSIZESMALL 		EQU 	8						; 2 long words. This is mainly used for descriptors.
METAINFOSIZE 			EQU 	8						; BYTES

*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
