package com.example.proyecto13.transacciones;

public class Transacciones
{
    // Nombre de la base de datos
    public static final String NameDatabase = "Proyecto13";
    // Tablas de la base de datos
    public static final String tablaPersonas = "personas";

    /* Transacciones de la base de datos PM1E11248 */
    public static final String CreateTBPersonas =
            "CREATE TABLE personas (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, apellido TEXT, numero TEXT, correo TEXT, direccion TEXT)";

    public static final String DropTablePersonas = "DROP TABLE IF EXISTS persoans";

    // Helpers
    public static final String Empty = "";
}