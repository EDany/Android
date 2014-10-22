package com.royal.tudeuda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.royal.tudeuda.bean.Contacto;
import com.royal.tudeuda.manejador.ManejadorDeuda;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.content.ContentProviderResult;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements TabListener, 
															   OnPageChangeListener,
															   OnItemClickListener,
															   DrawerListener,
															   OnQueryTextListener,
															   OnActionExpandListener{
	
	private ViewPager pager;
	private DrawerLayout drawer;
	private ActionBarDrawerToggle toggle;
	private ListView lstDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		drawer.setDrawerListener(this);
		
		lstDrawer = (ListView)findViewById(R.id.menu_Drawer);
		lstDrawer.setOnItemClickListener(this);
		
		toggle = new ActionBarDrawerToggle(this, drawer, R.drawable.drawer, 
										   R.string.drawer_open, R.string.drawer_close);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		pager = (ViewPager)findViewById(R.id.pager_Local);
		pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		pager.setOnPageChangeListener(this);
		
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab tab = getSupportActionBar().newTab().setText(R.string.tab_deudas).setTabListener(this);
		Tab tab2 = getSupportActionBar().newTab().setText(R.string.tab_deudores).setTabListener(this);
		getSupportActionBar().addTab(tab);
		getSupportActionBar().addTab(tab2);
		
		AccesoContacto.getInstancia(obtenerContactos());
		ManejadorDeuda.getInstancia(this);
		verificarContactos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		MenuItem searchItem = menu.findItem(R.id.search);
		
		SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
		searchView.setOnQueryTextListener(this);
		
		MenuItemCompat.setOnActionExpandListener(searchItem, this);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		if (toggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onPostCreated(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		toggle.syncState();
	}
	
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		toggle.onConfigurationChanged(newConfig);
	}

	public Cursor obtenerContactos(){
		/*cContactos = managedQuery(ContactsContract.Contacts.CONTENT_URI,
								  new String[]{ContactsContract.Contacts.DISPLAY_NAME,
											   ContactsContract.CommonDataKinds.Phone.NUMBER},
								  ContactsContract.Contacts.IN_VISIBLE_GROUP, null,
								  ContactsContract.Contacts.DISPLAY_NAME+ " ASC");*/
		return getContentResolver().query(Data.CONTENT_URI, 
										  new String[]{Data._ID, Data.DISPLAY_NAME, 
													   Phone.NUMBER, Phone.TYPE}, 
										  Data.MIMETYPE+"='"+Phone.CONTENT_ITEM_TYPE
										  +"' AND "+Phone.NUMBER+" IS NOT NULL", null, 
										  Data.DISPLAY_NAME+" ASC");
	}
	
	public void verificarContactos(){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				HashMap<String, Contacto> lista = AccesoContacto.getInstancia(null).getContactoAgregado();
				if(lista.size()>0){
					for(int x = 0; x < lista.size(); x++){
						Contacto c = lista.get(String.valueOf(x));
						agregarContacto(c.getNombre(), c.getTelefono());
					}
				}
				AccesoContacto.getInstancia(null).cleanContactoAgregado();
			}}, 0, 2000);
	}
	
	@SuppressWarnings("unused")
	public void agregarContacto(String nombre, String telefono){
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsert = ops.size();
		
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
										.withValue(RawContacts.ACCOUNT_TYPE, null)
										.withValue(RawContacts.ACCOUNT_NAME, null).build());
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
										.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsert)
										.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
										.withValue(Phone.NUMBER, telefono)
										.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
										.withValue(Phone.TYPE, "1").build());
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
										.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsert)
										.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
										.withValue(StructuredName.DISPLAY_NAME, nombre).build());
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
										.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsert)
										.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
										.withValue(Email.DATA, null)
										.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
										.withValue(Email.TYPE, "2").build());
		
		try{
			ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}catch(RemoteException e){
			e.printStackTrace();
		}catch(OperationApplicationException e){
			e.printStackTrace();
		}
		
		//Actualizar cursor contactos
		AccesoContacto.getInstancia(null).setcContactos(obtenerContactos());
	}
	

	// Start Methods TabListener
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}
	
	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		pager.setCurrentItem(arg0.getPosition());		
	}
	
	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				
	}
	// End Methods TabListener
	
	// Start Methods OnPageChangeListener
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		getSupportActionBar().setSelectedNavigationItem(arg0);
	}
	// End Methods OnPageChangeListener

	// Start Method OnItemClickListener
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		
		drawer.closeDrawer(lstDrawer);
	}
	// End Method OnItemClickListener
	
	// Start Methods DrawerListener
	@Override
	public void onDrawerClosed(View arg0) {
		// TODO Auto-generated method stub
		getSupportActionBar().setTitle("Tu Deuda");
	}

	@Override
	public void onDrawerOpened(View arg0) {
		// TODO Auto-generated method stub
		getSupportActionBar().setTitle("Menu");
	}

	@Override
	public void onDrawerSlide(View arg0, float arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDrawerStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	// End Methods DrawerListener
	
	// Start Methods OnQueryTextChange
	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	// End Methods OnQueryTextChanged

	// Start Methods OnActionExpandListener
	@Override
	public boolean onMenuItemActionCollapse(MenuItem arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onMenuItemActionExpand(MenuItem arg0) {
		// TODO Auto-generated method stub
		return true;
	}
	// End Methods OnActionExpandListener
}
