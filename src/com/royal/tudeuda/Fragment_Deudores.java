package com.royal.tudeuda;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.royal.tudeuda.bean.Contacto;
import com.royal.tudeuda.manejador.ManejadorDeuda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Fragment_Deudores extends Fragment implements OnClickListener{
	
	private SimpleCursorAdapter adapter;
	private ListView lstDeudores;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
								Bundle savedInstance){
		
		View rootView = inflater.inflate(R.layout.fragment_slide_page_list_deudores, container,
										 false);
		
		lstDeudores = (ListView)rootView.findViewById(R.id.lstDeudores);
		llenarLista();
		rootView.findViewById(R.id.agregar_Deudor).setOnClickListener(this);
		
		return rootView;
				
	}
	
	@SuppressWarnings({ "deprecation", "static-access" })
	private void llenarLista(){
		adapter = new SimpleCursorAdapter(getActivity(), 
									  	  R.layout.itemdeuda, ManejadorDeuda.getInstancia(null)
									  	  									.listarDeudas(1), 
									  	  new String[]{ManejadorDeuda.getInstancia(null).CN_FECHA, 
													   ManejadorDeuda.getInstancia(null).CN_DEUDOR, 
													   ManejadorDeuda.getInstancia(null).CN_DEUDA, 
													   ManejadorDeuda.getInstancia(null).CN_MONEDA}, 
										  new int[]{R.id.lblDate, R.id.lblDeudor, R.id.lblDeuda,
													R.id.lblMoneda});
		lstDeudores.setAdapter(adapter);
	}

	// OnClickListener
	@Override
	public void onClick(View v) {
		showDialogAdd(null);
	}
	
	// Dialog Add Deudor
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SuppressLint({ "InflateParams", "DefaultLocale" })
	private void showDialogAdd(Bundle savedInstance){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater factory = LayoutInflater.from(getActivity());
		final View entryView = factory.inflate(R.layout.deuda_agregar, null);
		
		final TextView dateAdd = (TextView)entryView.findViewById(R.id.dateAdd);
		dateAdd.setText(this.getDate());
		
		final CheckBox deudorAddedSave = 
				(CheckBox)entryView.findViewById(R.id.deudorAddedSave);
		
		final EditText telefonoAdd = (EditText)entryView.findViewById(R.id.telefonoAdd);
		
		final AutoCompleteTextView deudorAdd = 
				(AutoCompleteTextView)entryView.findViewById(R.id.deudorAdd);
		deudorAdd.setAdapter(new ArrayAdapter(getActivity(), 
											  android.R.layout.simple_dropdown_item_1line,
											  AccesoContacto.getInstancia(null)
											  				.nameAutoComplete()));
		deudorAdd.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				Cursor contacto = AccesoContacto.getInstancia(null).getcContactos();
				contacto.moveToFirst();
				boolean estado = false;
				do{
					if(contacto.getString(1)
							   .toLowerCase()
								.startsWith(deudorAdd.getText().toString().toLowerCase())){
						telefonoAdd.setText(contacto.getString(2));
						telefonoAdd.setEnabled(false);
						deudorAddedSave.setEnabled(false);
						estado = true;
						break;
					}
				}while(contacto.moveToNext());
				if(!deudorAdd.getText().toString().equals("") & !estado){
					telefonoAdd.setText("");
					telefonoAdd.setEnabled(true);
					deudorAddedSave.setEnabled(true);
				}else if(deudorAdd.getText().toString().equals("")){
					telefonoAdd.setText("");
					telefonoAdd.setEnabled(false);
					deudorAddedSave.setEnabled(false);
				}
			}});

		builder.setView(entryView);
		builder.setTitle("Agregar Deudor").setCancelable(false)
			   .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText descriptionAdd = 
								(EditText)entryView.findViewById(R.id.descriptionAdd);
						
						ToggleButton monedaAdd = 
								(ToggleButton)entryView.findViewById(R.id.monedaAdd);
						
						EditText deudaAdd = (EditText)entryView.findViewById(R.id.deudaAdd);
						
						ManejadorDeuda.getInstancia(null).insertar(dateAdd.getText().toString(), 
																	descriptionAdd.getText().toString(),
																	deudorAdd.getText().toString(), 
																	telefonoAdd.getText().toString(), 
																	(monedaAdd.isChecked())? "Q":"$", 
																	Integer.parseInt(deudaAdd.getText().toString()), 1);
						
						if(deudorAddedSave.isChecked() & deudorAddedSave.isEnabled())
							AccesoContacto.getInstancia(null)
										  .agregarContacto(new Contacto(deudorAdd.getText().toString(),
																		telefonoAdd.getText().toString()));
						llenarLista();
					}
			   })
			   
			   .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	@SuppressLint("SimpleDateFormat")
	private String getDate(){
		return new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
	}

}
