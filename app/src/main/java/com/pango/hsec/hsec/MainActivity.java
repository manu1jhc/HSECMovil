package com.pango.hsec.hsec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.pango.hsec.hsec.Busquedas.Busqueda;
import com.pango.hsec.hsec.Facilito.list_obsfacilito;
import com.pango.hsec.hsec.Ingresos.Inspecciones.AddInspeccion;

import java.lang.reflect.Field;

import layout.FragmentAprobaciones;
import layout.FragmentConfiguracion;
import layout.FragmentContactenos;
import layout.FragmentFichaPersonal;
import layout.FragmentInspecciones;
import layout.FragmentMuro;
import layout.FragmentObservaciones;
import layout.FragmentPlanPendiente;
import layout.FragmentRegistroIO;
import layout.FragmentAvanzado;


public class MainActivity extends AppCompatActivity
        implements
        IActivity,
        NavigationView.OnNavigationItemSelectedListener,
        FragmentMuro.OnFragmentInteractionListener,
        //FragmentAprobaciones.OnFragmentInteractionListener,
        FragmentFichaPersonal.OnFragmentInteractionListener,
        FragmentObservaciones.OnFragmentInteractionListener,
        FragmentInspecciones.OnFragmentInteractionListener,

        //FragmentRegistroIO.OnFragmentInteractionListener,
        FragmentAvanzado.OnFragmentInteractionListener,
        FragmentConfiguracion.OnFragmentInteractionListener,
        FragmentContactenos.OnFragmentInteractionListener,
        FragmentPlanPendiente.OnFragmentInteractionListener

{

    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView; // menu inferior
    private FragmentManager fragmentManager;
    public static Context contextOfApplication;
    public static String jsonObs="";
    public static String jsonInsp="";

    DrawerLayout drawerLayout;
    ImageView buscar;
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void menuLateral(View view) {

        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void lupaBuscar(View view) {
        Intent busquedas = new Intent(this,Busqueda.class);
        startActivity(busquedas);
    }


    public enum NavigationFragment{
        Muro,
        //Aprobaciones,
        FichaPersonal,
        //RegistroOI,
        Observaciones,
        Inspecciones,
        Avanzado,
        Configuracion,
        Contactenos,
        PlanPendiente
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        disableShiftMode(bottomNavigationView);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        buscar=findViewById(R.id.btn_buscar);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        contextOfApplication = getApplicationContext();

        ChangeFragment(NavigationFragment.Muro);
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if(!GlobalVariables.desdeBusqueda){
            ChangeFragment(NavigationFragment.Muro);
            uncheckItemsMenu();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);
            GlobalVariables.desdeBusqueda=false;
        }else{

            ChangeFragment(NavigationFragment.FichaPersonal);
            uncheckItemsMenu();
            bottomNavigationView.getMenu().findItem(R.id.navigation_ficha).setChecked(true);
        }





    }


    public static Context getContextOfApplication(){ return contextOfApplication; }
    //int  backpress=0;
    private Boolean exit = false;

    @Override
    public void onBackPressed() {

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        try {

            /*if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);

            }else */

            if (GlobalVariables.fragmentStack.size() == 2) {
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                GlobalVariables.fragmentStack.lastElement().onPause();
                ft.remove(GlobalVariables.fragmentStack.pop());
                GlobalVariables.fragmentStack.lastElement().onResume();
                uncheckItemsMenu();

                //Fragment fra=GlobalVariables.fragmentStack.lastElement();
                bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);

                ft.show(GlobalVariables.fragmentStack.lastElement());

                ft.commit();


                //bottomNavigationView.setVisibility(View.GONE);
                //toolbar.setVisibility(View.VISIBLE);


                // ChangeFragment(NavigationFragment.Inicio);
            }else {
                //super.onBackPressed();
                if (exit) {
                    GlobalVariables.fragmentStack.clear();
                    super.onBackPressed(); // finish activity
                } else {
                    Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 3 * 1000);

                }

            }
        }catch (Throwable e){
            Log.d("error_frag", e.getLocalizedMessage());
        }
    }








    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_reporte) {

            Toast.makeText(this, "nav_reporte",
                    Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_aprobaciones) {

            Toast.makeText(this, "nav_Aprobaciones", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_listaobs) {
            GlobalVariables.flagFacilito=true;
            Intent addObsReport = new Intent(this,list_obsfacilito.class);
            startActivity(addObsReport);

            Toast.makeText(this, "nav_listaobs", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_observacion) {

            GlobalVariables.ObjectEditable=false;
            Intent obserbacion_edit = new Intent(this,observacion_edit.class);
            obserbacion_edit.putExtra("codObs", "OBS000000XYZ");
            obserbacion_edit.putExtra("tipoObs","TO01");
            obserbacion_edit.putExtra("posTab", 0);
            startActivity(obserbacion_edit);

        } else if (id == R.id.nav_inspeccion) {
            GlobalVariables.ObjectEditable=false;
            Intent addInspeccion = new Intent(this,AddInspeccion.class);
            addInspeccion.putExtra("codObs","INSP000000XYZ");
            startActivity(addInspeccion);


            Toast.makeText(this, "nav_inspeccion", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_ficha) {
            buscar.setVisibility(View.INVISIBLE);

            GlobalVariables.isUserlogin=true;
            GlobalVariables.barTitulo=true;

            ClickMenuFicha();
            uncheckItemsMenu();
            bottomNavigationView.getMenu().findItem(R.id.navigation_ficha).setChecked(true);

            Toast.makeText(this, "nav_ficha",
                    Toast.LENGTH_SHORT).show();



        }else if (id == R.id.nav_pendientes){

            buscar.setVisibility(View.INVISIBLE);

            Menu menu = navigationView.getMenu();
            uncheckItems(menu);
            ClickPendientes();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);

            Toast.makeText(this, "nav_pendientes",
                    Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_Contactenos){
            buscar.setVisibility(View.INVISIBLE);

            Menu menu = navigationView.getMenu();
            uncheckItems(menu);
            ClickMenuContactenos();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);



            Toast.makeText(this, "nav_Contactenos",
                    Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_configuracion){
            buscar.setVisibility(View.INVISIBLE);
            Menu menu = navigationView.getMenu();
            uncheckItems(menu);

            ClickMenuConfiguracion();
            bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);


            Toast.makeText(this, "nav_configuracion",
                    Toast.LENGTH_SHORT).show();

        }else if(id == R.id.nav_cerrar){
            buscar.setVisibility(View.INVISIBLE);

            Save_status(false);
            Save_Datalogin("","");
            Intent intent=new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //menu inferior
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_muro:
                    //setTitle("Antapaccay");
                    buscar.setVisibility(View.VISIBLE);

                    ChangeFragment(NavigationFragment.Muro);
                    //bottomNavigationView.setVisibility(View.GONE);
                    uncheckItemsMenu();
                    return true;

                /*case R.id.navigation_aprob:
                    buscar.setVisibility(View.INVISIBLE);

                    uncheckItemsMenu();
                    //navigationView.getMenu().findItem(R.id.nav_imagenes).setChecked(true);
                    ClickMenuAprobaciones();
                    return true;
                    */
                case R.id.navigation_ficha:
                    buscar.setVisibility(View.INVISIBLE);

                    GlobalVariables.isUserlogin=true;
                    GlobalVariables.barTitulo=true;

                    uncheckItemsMenu();
                    ClickMenuFicha();

                    //navigationView.getMenu().findItem(R.id.nav_videos).setChecked(true);
                    return true;
            /*    case R.id.navigation_registro:
                    buscar.setVisibility(View.INVISIBLE);

                    uncheckItemsMenu();
                    ClickMenuRegistro();
                    return true;
                    */
                case R.id.navigation_observacion:
                    buscar.setVisibility(View.INVISIBLE);

                    uncheckItemsMenu();
                    //navigationView.getMenu().findItem(R.id.nav_imagenes).setChecked(true);
                    ClickMenuObservacion();
                    return true;

                case R.id.navigation_inspeccion:
                    buscar.setVisibility(View.INVISIBLE);

                    uncheckItemsMenu();
                    //navigationView.getMenu().findItem(R.id.nav_imagenes).setChecked(true);
                    ClickMenuInspeccion();
                    return true;


                case R.id.navigation_avanzado:
                    buscar.setVisibility(View.INVISIBLE);

                    uncheckItemsMenu();
                    ClickMenuAvanzado();
                    return true;

            }
            return false;
        }

    };


    public void ClickMenuObservacion() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_observacion).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.Observaciones);

    }
    public void ClickMenuInspeccion() {

        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_inspeccion).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.Inspecciones);
    }

    /*
        public void ClickMenuAprobaciones() {
            uncheckItemsMenu();
            bottomNavigationView.getMenu().findItem(R.id.navigation_aprob).setChecked(true);
            bottomNavigationView.setVisibility(View.VISIBLE);
            ChangeFragment(NavigationFragment.Aprobaciones);

        }
    */
    public void ClickMenuFicha() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_ficha).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        navigationView.getMenu().findItem(R.id.nav_ficha).setChecked(true);

        ChangeFragment(NavigationFragment.FichaPersonal);

    }
/*
    public void ClickMenuRegistro() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_registro).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.RegistroOI);

    }
    */
    public void ClickMenuAvanzado() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_avanzado).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.Avanzado);

    }

    private void ClickMenuConfiguracion() {
        uncheckItemsMenu();
        ChangeFragment(NavigationFragment.Configuracion);

    }

    private void ClickMenuContactenos() {
        uncheckItemsMenu();
        ChangeFragment(NavigationFragment.Contactenos);
    }

    public void ClickPendientes(){
        uncheckItemsMenu();
        ChangeFragment(NavigationFragment.PlanPendiente);
    }
    public void uncheckItemsMenu() {

        try {
            // limpiamos todos los seleccionados
            Menu menu = navigationView.getMenu();
            uncheckItems(menu);

            menu = bottomNavigationView.getMenu();
            uncheckItems(menu);
        }catch (Exception e){
            System.out.print(e);
        }

    }


    private void uncheckItems(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    }


    public void ChangeFragment(NavigationFragment value){
        Fragment fragment = null;
        switch (value) {
            case Muro:    fragment = new FragmentMuro(); break;
            //case Aprobaciones:    fragment = new FragmentAprobaciones(); break;
            case FichaPersonal: fragment = new FragmentFichaPersonal(); break;
            //case RegistroOI: fragment = new FragmentRegistroIO(); break;
            case Observaciones: fragment = new FragmentObservaciones(); break;

            case Inspecciones: fragment = new FragmentInspecciones(); break;

            case Avanzado: fragment = new FragmentAvanzado(); break;// configuracion
            case Configuracion: fragment = new FragmentConfiguracion(); break;
            case Contactenos: fragment = new FragmentContactenos(); break;
            case PlanPendiente: fragment = new FragmentPlanPendiente(); break;
        }
        if(fragment!=null&&GlobalVariables.fragmentStack.size()==0){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, fragment)

                    .commit();
            GlobalVariables.apilarFrag(fragment);

       }else if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, fragment)
                    .hide(GlobalVariables.fragmentStack.lastElement())
                    .commit();
            GlobalVariables.apilarFrag(fragment);
        }

    }


    @Override
    public void success(String data,String Tipo) {

    }

    @Override
    public void successpost(String data,String Tipo) {

    }

    @Override
    public void error(String mensaje,String Tipo) {

    }



    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }




    public void Save_status(boolean ischecked){
        SharedPreferences check_status = this.getSharedPreferences("checked", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_estado = check_status.edit();
        editor_estado.putBoolean("check", ischecked);
        editor_estado.commit();
    }
    public void Save_Datalogin(String user,String password){
        SharedPreferences user_login = this.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_user = user_login.edit();
        editor_user.putString("user", user);
        editor_user.putString("password",password);
        editor_user.commit();
    }



}
