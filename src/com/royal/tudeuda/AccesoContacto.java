package com.royal.tudeuda;

import java.util.HashMap;

import com.royal.tudeuda.bean.Contacto;

import android.database.Cursor;

public class AccesoContacto {

	private Cursor cContactos;
	private HashMap<String, Contacto> contactoAgregado;
	private static AccesoContacto instancia;
	
	public static AccesoContacto getInstancia(Cursor cursor){
		if(instancia==null)
			instancia = new AccesoContacto(cursor);
		return instancia;
	}
	
	private AccesoContacto(Cursor cursor){
		this.cContactos = cursor;
		contactoAgregado = new HashMap<String, Contacto>();
	}

	public Cursor getcContactos() {
		return cContactos;
	}

	public void setcContactos(Cursor cContactos) {
		this.cContactos = cContactos;
	}

	public String[] nameAutoComplete(){
		String nameComplete = "";
		try{
			cContactos.moveToFirst();
			do{
				nameComplete = nameComplete+ " " + cContactos.getString(1);
			}while(cContactos.moveToNext());
			return nameComplete.split(" ");
		}catch(NullPointerException e){
			e.printStackTrace();
			return new String[]{};
		}
	}
	
	public void agregarContacto(Contacto contacto){
		contactoAgregado.put(String.valueOf(contactoAgregado.size()), contacto);
	}
	
	public HashMap<String, Contacto> getContactoAgregado(){
		return contactoAgregado;
	}
	
	public void cleanContactoAgregado(){
		contactoAgregado.clear();
	}
}
