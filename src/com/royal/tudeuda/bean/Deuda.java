package com.royal.tudeuda.bean;

public class Deuda {

	private Integer idDeuda;
	private String fecha;
	private String deudor;
	private String telefonoDeudor;
	private String descripcion;
	private String moneda;
	private Integer deuda;
	private Integer contactoTipo;
	
	public Deuda(){
		super();
	}

	public Deuda(Integer idDeuda, String fecha, String deudor,
			String telefonoDeudor, String descripcion, String moneda,
			Integer deuda, Integer contactoTipo) {
		super();
		this.idDeuda = idDeuda;
		this.fecha = fecha;
		this.deudor = deudor;
		this.telefonoDeudor = telefonoDeudor;
		this.descripcion = descripcion;
		this.moneda = moneda;
		this.deuda = deuda;
		this.contactoTipo = contactoTipo;
	}

	public Integer getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Integer idDeuda) {
		this.idDeuda = idDeuda;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getDeudor() {
		return deudor;
	}

	public void setDeudor(String deudor) {
		this.deudor = deudor;
	}

	public String getTelefonoDeudor() {
		return telefonoDeudor;
	}

	public void setTelefonoDeudor(String telefonoDeudor) {
		this.telefonoDeudor = telefonoDeudor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Integer getDeuda() {
		return deuda;
	}

	public void setDeuda(Integer deuda) {
		this.deuda = deuda;
	}

	public Integer getContactoTipo() {
		return contactoTipo;
	}

	public void setContactoTipo(Integer contactoTipo) {
		this.contactoTipo = contactoTipo;
	}
}
