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

    // Soporte de arrays
    DECLARAR_INDIRECCION,
    CARGAR_INDIRECCION,
    GUARDAR_INDIRECCION,

    // Carga de parametros para funciones. Solo tenemos parametros simples por ahora.
    PARAM,

    // Operaciones relacionadas con funciones
    LLAMADA,
    PREAMBULO,
    RETORNO,

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

    // Se utiliza para inicializar lo que sea necesario en las clases
    CLASE,

    // Instruccion especial usada para inicializar
    // htodo lo que tiene que ver con el inicio de la ejecución
    ENTRY_POINT,
    ;

}
