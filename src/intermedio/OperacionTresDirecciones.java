/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intermedio;

/**
 *
 * @author jesus
 */
public enum OperacionTresDirecciones {
    COPIA,
    
    // Saltos
    GOTO,
    
    // Creacion de etiquetas
    ETIQUETA,
    
    
    // Operaciones relacionadas con funciones
    LLAMADA,
    PREAMBULO,
    
    // Operaciones aritmeticas
    SUMA,
    RESTA,
    PRODUCTO,
    DIVISION,
    
    // Operaciones relacionales
    GT,
    GTE,
    LT,
    LTE,
    EQ,
    NE,
    
    // Operaciones logicas
    AND,
    OR,
       
}
