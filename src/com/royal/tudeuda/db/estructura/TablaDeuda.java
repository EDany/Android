package com.royal.tudeuda.db.estructura;

public class TablaDeuda implements Column {

	public static String TABLE_DEUDA = "deuda";
	
	public static String CREATE_TABLE_DEUDA = "create table "+TABLE_DEUDA+" ("
			+CN_ID+" integer primary key autoincrement,"
			+CN_FECHA+" text not null,"
			+CN_DESCRIPCION+" text,"
			+CN_DEUDOR+" text not null,"
			+CN_TELEFONODEUDOR+" text,"
			+CN_MONEDA+" text not null,"
			+CN_CONTACTOTIPO+" integer not null,"
			+CN_DEUDA+" integer not null);";
}
