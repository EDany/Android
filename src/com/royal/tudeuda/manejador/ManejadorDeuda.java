package com.royal.tudeuda.manejador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.royal.tudeuda.bean.Deuda;
import com.royal.tudeuda.db.DBHelper;
import com.royal.tudeuda.db.estructura.TablaDeuda;

public class ManejadorDeuda extends TablaDeuda {

	private DBHelper helper;
	private SQLiteDatabase db;
	
	private static ManejadorDeuda instancia;
	
	public static ManejadorDeuda getInstancia(Context contexto){
		if(instancia == null)
			instancia = new ManejadorDeuda(contexto);
		return instancia;
	}
	
	private ManejadorDeuda(Context contexto){
		helper = new DBHelper(contexto);
		db = helper.getWritableDatabase();
	}
	
	public ContentValues getContentValuesDeuda(String fecha, String descripcion, String deudor,
											   String telefonoDeudor, String moneda, 
											   Integer deuda, Integer contactoTipo){
		ContentValues valores = new ContentValues();
		valores.put(CN_FECHA, fecha);
		valores.put(CN_DESCRIPCION, descripcion);
		valores.put(CN_DEUDOR, deudor);
		valores.put(CN_TELEFONODEUDOR, telefonoDeudor);
		valores.put(CN_MONEDA, moneda);
		valores.put(CN_DEUDA, deuda);
		valores.put(CN_CONTACTOTIPO, contactoTipo);
		return valores;
	}
	
	public void insertar(String fecha, String descripcion, String deudor, String telefonoDeudor, 
						 String moneda, Integer deuda, Integer contactoTipo){
		db.insert(TABLE_DEUDA, null, getContentValuesDeuda(fecha, descripcion, deudor, 
														   telefonoDeudor, moneda, deuda,
														   contactoTipo));
	}
	
	public Cursor listarDeudas(int tipo){
		String[] columns = new String[]{CN_ID,CN_FECHA,CN_DESCRIPCION,CN_DEUDOR,
										CN_TELEFONODEUDOR,CN_MONEDA,CN_DEUDA};
		return db.query(TABLE_DEUDA, columns, CN_CONTACTOTIPO+"=?", new String[]{String.valueOf(tipo)},
						null, null, null);
	}
	
	public Cursor listarDeudas(String query){
		query = query.replace("'", "''");
		String[] columns = new String[]{CN_ID,CN_FECHA,CN_DESCRIPCION,CN_DEUDOR,
										CN_TELEFONODEUDOR,CN_MONEDA,CN_DEUDA,CN_CONTACTOTIPO};
		return db.query(TABLE_DEUDA, columns, CN_FECHA+" like '%?%' or "+CN_DESCRIPCION
											 +" like '%?%' or"+CN_DEUDOR
											 +" like '%?%' or"+CN_TELEFONODEUDOR+" like '%?%' or"
											 +CN_MONEDA+" like '%?%'", 
						new String[]{query, query, query, query, query}, null, null, null);
	}
	
	public void editar(Deuda deuda){
		ContentValues valores = new ContentValues();
		valores.put(CN_FECHA, deuda.getFecha());
		valores.put(CN_DESCRIPCION, deuda.getDescripcion());
		valores.put(CN_DEUDOR, deuda.getDeudor());
		valores.put(CN_TELEFONODEUDOR, deuda.getTelefonoDeudor());
		valores.put(CN_MONEDA, deuda.getMoneda());
		valores.put(CN_DEUDA, deuda.getDeuda());
		db.update(TABLE_DEUDA, valores, CN_ID+"=?", new String[]{deuda.getIdDeuda().toString()});
	}
	
	public void eliminar(Deuda deuda){
		db.delete(TABLE_DEUDA, CN_ID+"=?", new String[]{deuda.getIdDeuda().toString()});
	}
}
