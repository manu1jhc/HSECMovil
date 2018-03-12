package com.pango.hsec.hsec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.Toast;

import java.lang.reflect.Field;

import layout.FragmentAprobaciones;
import layout.FragmentFichaPersonal;
import layout.FragmentMuro;
import layout.FragmentRegistroIO;
import layout.FragmentAvanzado;


public class MainActivity extends AppCompatActivity
        implements
        IActivity,
        FragmentMuro.OnFragmentInteractionListener,
        FragmentAprobaciones.OnFragmentInteractionListener,
        FragmentFichaPersonal.OnFragmentInteractionListener,
        FragmentRegistroIO.OnFragmentInteractionListener,
        FragmentAvanzado.OnFragmentInteractionListener
{
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    public static Context contextOfApplication;
    public static String jsonMuro="";

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public enum NavigationFragment{
        Muro,
        Aprobaciones,
        FichaPersonal,
        RegistroOI,
        Avanzado
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        contextOfApplication = getApplicationContext();


        ChangeFragment(NavigationFragment.Muro);
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_muro).setChecked(true);


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
                    Toast.makeText(this, "Press Back again to Exit.",
                            Toast.LENGTH_SHORT).show();
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
    //menu inferior
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_muro:
                    //setTitle("Antapaccay");
                    ChangeFragment(NavigationFragment.Muro);
                    //bottomNavigationView.setVisibility(View.GONE);
                    uncheckItemsMenu();
                    return true;
                case R.id.navigation_aprob:
                    uncheckItemsMenu();
                    //navigationView.getMenu().findItem(R.id.nav_imagenes).setChecked(true);
                    ClickMenuAprobaciones();
                    return true;
                case R.id.navigation_ficha:
                    GlobalVariables.isUserlogin=true;
                    GlobalVariables.barTitulo=true;

                    uncheckItemsMenu();
                    ClickMenuFicha();

                    //navigationView.getMenu().findItem(R.id.nav_videos).setChecked(true);
                    return true;
                case R.id.navigation_registro:
                    uncheckItemsMenu();
                    ClickMenuRegistro();
                    return true;
                case R.id.navigation_avanzado:
                    uncheckItemsMenu();
                    ClickMenuAvanzado();
                    return true;

            }
            return false;
        }

    };

    public void ClickMenuAprobaciones() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_aprob).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.Aprobaciones);

    }

    public void ClickMenuFicha() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_ficha).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.FichaPersonal);

    }

    public void ClickMenuRegistro() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_registro).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.RegistroOI);

    }
    public void ClickMenuAvanzado() {
        uncheckItemsMenu();
        bottomNavigationView.getMenu().findItem(R.id.navigation_avanzado).setChecked(true);
        bottomNavigationView.setVisibility(View.VISIBLE);
        ChangeFragment(NavigationFragment.Avanzado);

    }

    public void uncheckItemsMenu() {
        // limpiamos todos los seleccionados
        Menu menu = bottomNavigationView.getMenu();
        uncheckItems(menu);
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
            case Aprobaciones:    fragment = new FragmentAprobaciones(); break;
            case FichaPersonal: fragment = new FragmentFichaPersonal(); break;
            case RegistroOI: fragment = new FragmentRegistroIO(); break;
            case Avanzado: fragment = new FragmentAvanzado(); break;


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



}
