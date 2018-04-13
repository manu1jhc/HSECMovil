package com.pango.hsec.hsec;

import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.View;
import com.pango.hsec.hsec.model.EstadisticaDetModel;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.ObsDetalleModel;
import com.pango.hsec.hsec.model.ObservacionModel;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.model.PlanMinModel;
import com.pango.hsec.hsec.model.PlanModel;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by Andre on 12/12/2017.
 */

public class GlobalVariables  {


    //variables de edicion Observaciones
    public static ObservacionModel Obserbacion= new ObservacionModel();
    public static ObsDetalleModel ObserbacionDetalle= new ObsDetalleModel();
    public static String ObserbacionFile;
    public static String ObserbacionPlan;
    public static List<GaleriaModel> listaGaleria =new ArrayList<>();
    public static List<GaleriaModel> listaArchivos =new ArrayList<>();

    public static ArrayList<PlanModel> Planes= new  ArrayList<>();
    public static boolean ObjectEditable=true;

    //public static String Urlbase2 = "entrada/getpaginated/";
    public static int con_status=0;
    public static int con_status_post=0;

    public static String token_auth="";

    public static boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static Stack<Fragment> fragmentStack= new Stack<Fragment>();

    public static void apilarFrag(Fragment fragment){
        if (GlobalVariables.fragmentStack.size()<=1) {
            GlobalVariables.fragmentStack.push(fragment);
        }else{
            GlobalVariables.fragmentStack.pop();
            GlobalVariables.fragmentStack.push(fragment);
        }
    }

    public static View view_fragment;
    public static boolean isFragment=false;
    public static int contpublic=1;
    public static int num_items=7;
    //public static String Url_base="https://app.antapaccay.com.pe/hsecweb/whsec_Service/api/";
    public static String Url_base="http://192.168.1.2/whsec_Servicedmz/api/";

    public static boolean flagUpSc=false;
   // public static boolean FDown=false;
   public static int count=5;
    public static boolean flag_up_toast=false;

    public static ArrayList<PublicacionModel> listaGlobal = new  ArrayList<PublicacionModel>();
    public static ArrayList<PersonaModel> lista_Personas=new ArrayList<>();

    public static ArrayList<PlanMinModel> listaPlanMin = new  ArrayList<PlanMinModel>();

    public static ArrayList<PublicacionModel> listaGlobalFiltro = new  ArrayList<PublicacionModel>();

    public static String json_user="";

    public static String nombre="";

    public static String[] obsDetListacab ={"CodObservacion","CodAreaHSEC","CodNivelRiesgo","ObservadoPor","Fecha","Hora","Gerencia","Superint","CodUbicacion","CodSubUbicacion","UbicacionEsp","Lugar","CodTipo"};
    public static String[] obsDetListIzq ={"Codigo","Area","Nivel de riesgo","Observado Por","Fecha","Hora","Gerencia","Superintendencia","Ubicacion","Sub Ubicación","Ubicación Específica","Lugar","Tipo"};

    public static String[] planDetCab={"CodAccion","NroDocReferencia","CodAreaHSEC", "CodNivelRiesgo","DesPlanAccion","FechaSolicitud","CodEstadoAccion","SolicitadoPor","CodActiRelacionada","CodReferencia", "CodTipoAccion","FecComprometidaInicial","FecComprometidaFinal"};
    public static String[] planDetIzq={"Código de acción", "Nro. doc. de referencia", "area","Nivel de riesgo", "Descripcion", "Fecha de solicitud", "Estado", "Solicitado por", "Actividad relacionada","Referencia", "Tipo de acción", "Fecha inicial","Fecha final" };

    public static String[] busqueda_tipo={"Observaciones", "Inspecciones","Noticias"};
    public static String[] busqueda_mes={"-","Enero", "Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    public static String[] busqueda_anio;

    public static int  CodRol=3;
    public static boolean desdeBusqueda=false;
    public static String dniUser="";
    public static boolean barTitulo=true;

    public static  ArrayList<EstadisticaDetModel> dataAdicional = new ArrayList<>();

    //variables globales select
    //public static  ArrayList<Maestro> Tablas = new ArrayList<>();

    public static  ArrayList<Maestro> Area_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Tipo_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Tipo_obs2 = new ArrayList<>();

    public static  ArrayList<Maestro> Gerencia = new ArrayList<>();
    public static  ArrayList<Maestro> SuperIntendencia = new ArrayList<>();
    public static  ArrayList<Maestro> Contrata = new ArrayList<>();

    public static  ArrayList<Maestro> NivelRiesgo_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Ubicaciones_obs = new ArrayList<>();

    public static  ArrayList<Maestro> Ubicacion_obs = new ArrayList<>();
    public static  ArrayList<Maestro> SubUbicacion_obs = new ArrayList<>();
    public static  ArrayList<Maestro> UbicacionEspecifica_obs = new ArrayList<>();

    public static  ArrayList<Maestro> Tipo_insp = new ArrayList<>();

//plan de accion
    public static  ArrayList<Maestro> Referencia_Plan = new ArrayList<>();
    public static  ArrayList<Maestro> Tipo_Plan = new ArrayList<>();
    public static  ArrayList<Maestro> Estado_Plan = new ArrayList<>();

    public static  ArrayList<Maestro> Tablas = new ArrayList<>();

    //Obserbacion Detalle
    public static  ArrayList<Maestro> Actividad_obs = new ArrayList<>();
    public static  ArrayList<Maestro> HHA_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Acto_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Condicion_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Estado_obs = new ArrayList<>();
    //public static  ArrayList<Maestro> Actv_Relacionada = new ArrayList<>();
    public static  ArrayList<Maestro> Aspecto_Obs = new ArrayList<>();

    public static  ArrayList<Maestro> Error_obs = new ArrayList<>();

    public static  ArrayList<Maestro> Aspectos_Obs = new ArrayList<>();

    public static  ArrayList<Maestro> GestionRiesg_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Clasificacion_Obs = new ArrayList<>();
    public static  ArrayList<Maestro> CondicionComp_Obs = new ArrayList<>();

    public static  ArrayList<Maestro> TipoAutenticacion = new ArrayList<>();
    public static  ArrayList<Maestro> Sexo = new ArrayList<>();

    public static int paginacion=1;

    public static String getDescripcion(ArrayList<Maestro> Obj, String value){
        for (Maestro o : Obj  ) {
            if(o.CodTipo.equals(value)) return o.Descripcion;
        }
        return "";
    }

    public static int indexOf(ArrayList<Maestro> Obj, String value){
        for (int i=0;i<Obj.size();i++  ) {
            if(Obj.get(i).CodTipo.equals(value)) return i;
        }
        return 0;
    }

    public static  ArrayList<Maestro> SuperInt_Busq = new ArrayList<>();

    public static void loadObs_Detalles(){

        if(!Area_obs.isEmpty()) return;

        Aspectos_Obs.add(new Maestro("P001","EPP completos para la tarea"));
        Aspectos_Obs.add(new Maestro("P002","Orden y Limpieza"));
        Aspectos_Obs.add(new Maestro("P003","Estado de Herramientas"));
        Aspectos_Obs.add(new Maestro("P004","Materiales necesarios para la tarea"));
        Aspectos_Obs.add(new Maestro("P005","Estado de las instaaciones y/o estructuras"));
        Aspectos_Obs.add(new Maestro("P006","Análisis de Seguridad en e trabajo / Peligros identificados y controles existentes"));
        Aspectos_Obs.add(new Maestro("P007","Permiso(s) de Trabajo"));
        Aspectos_Obs.add(new Maestro("P008","Se cumplen las  restricciones o condiciones generales del PET"));

        GestionRiesg_obs.add(new Maestro("GESRIES1","Permiso de "));
        GestionRiesg_obs.add(new Maestro("GESRIES2","PET(Procedimiento escrito de trabajo)"));
        GestionRiesg_obs.add(new Maestro("GESRIES3","AST(Análisis de Seguridad en el trabajo)"));

        Clasificacion_Obs.add(new Maestro("CLASOBS1","Comportamiento seguro"));
        Clasificacion_Obs.add(new Maestro("CLASOBS2","Comportamiento de riesgo"));
        Clasificacion_Obs.add(new Maestro("CLASOBS3","Condición segura"));
        Clasificacion_Obs.add(new Maestro("CLASOBS4","Condición insegura"));

        CondicionComp_Obs.add(new Maestro("COMCON1","Competencias"));
        CondicionComp_Obs.add(new Maestro("COMCON2","Salud e Higiene"));
        CondicionComp_Obs.add(new Maestro("COMCON3","Posición de las personas"));
        CondicionComp_Obs.add(new Maestro("COMCON4","Valores de GLencore"));
        CondicionComp_Obs.add(new Maestro("COMCON5","Medio Ambiente"));
        CondicionComp_Obs.add(new Maestro("COMCON6","Orden y limpieza"));
        CondicionComp_Obs.add(new Maestro("COMCON7","Herramientas y equipos"));
        CondicionComp_Obs.add(new Maestro("COMCON8","Aptitud para el trabajo"));
        CondicionComp_Obs.add(new Maestro("COMCON10","Condiciones de Trabajo"));
        CondicionComp_Obs.add(new Maestro("COMCON11","Otro"));

        Referencia_Plan.add(new Maestro("01", "Observaciones"));
        Referencia_Plan.add(new Maestro("02", "Inspecciones"));
        Referencia_Plan.add(new Maestro("03","Incidentes"));
        Referencia_Plan.add(new Maestro("04","IPERC"));
        Referencia_Plan.add(new Maestro("05","Auditorias"));
        Referencia_Plan.add(new Maestro("06","Simulacros"));
        Referencia_Plan.add(new Maestro("07","Reuniones"));
        Referencia_Plan.add(new Maestro("08","Comites"));
        Referencia_Plan.add(new Maestro("09","Capacitaciones"));

        Estado_Plan.add(new Maestro("01","Pendiente"));
        Estado_Plan.add(new Maestro("02","Atendido"));
        Estado_Plan.add(new Maestro("03","En Proceso"));
        Estado_Plan.add(new Maestro("04","Observado"));
        Estado_Plan.add(new Maestro("05","Cerrado"));

        Tipo_obs.add(new Maestro("TO01", "Comportamiento"));
        Tipo_obs.add(new Maestro("TO02", "Condición"));

        NivelRiesgo_obs.add(new Maestro("BA", "Baja"));
        NivelRiesgo_obs.add(new Maestro("ME", "Media"));
        NivelRiesgo_obs.add(new Maestro("AL", "Alta"));

   //condicion sub estandar
        Condicion_obs.add(new Maestro("0027","Protección inadecuadas, defectuosa o inexistente"));
        Condicion_obs.add(new Maestro("0028","Paredes, techos, etc.  inestables"));
        Condicion_obs.add(new Maestro("0029","Caminos, pisos, superficies inadecuadas."));
        Condicion_obs.add(new Maestro("0030","Equipo de protección personal  inadecuado."));
        Condicion_obs.add(new Maestro("0031","Herramientas, Equipos, Materiales Defectuosos o sin calibración."));
        Condicion_obs.add(new Maestro("0032","Congestión o Acción Restringida"));
        Condicion_obs.add(new Maestro("0033","Alarmas, Sirenas, Sistemas de Advertencia Inadecuado  o defectuosos"));
        Condicion_obs.add(new Maestro("0034","Peligros de Incendio y Explosión"));
        Condicion_obs.add(new Maestro("0035","Limpieza y Orden deficientes"));
        Condicion_obs.add(new Maestro("0036","Exceso de Ruido"));
        Condicion_obs.add(new Maestro("0037","Exceso de Radiación"));
        Condicion_obs.add(new Maestro("0038","Temperaturas Extremas"));
        Condicion_obs.add(new Maestro("0039","Peligros ergonómicos"));
        Condicion_obs.add(new Maestro("0040","Excesiva o inadecuada iluminación"));
        Condicion_obs.add(new Maestro("0041","Ventilación Inadecuada"));
        Condicion_obs.add(new Maestro("0042","Condiciones Ambientales Peligrosas"));
        Condicion_obs.add(new Maestro("0043","Dispositivos de seguridad inadecuados / defectuosos"));
        Condicion_obs.add(new Maestro("0044","Sistemas y Equipos energizados"));
        Condicion_obs.add(new Maestro("0045","Productos químicos peligrosos"));
        Condicion_obs.add(new Maestro("0046","Altura desprotegida"));
        Condicion_obs.add(new Maestro("0047","Derrame"));
// acto sub estandar
        Acto_obs.add(new Maestro("0001","Operar equipos sin autorización"));
        Acto_obs.add(new Maestro("0002","Operar  equipo a velocidad inadecuada"));
        Acto_obs.add(new Maestro("0003","No Avisar"));
        Acto_obs.add(new Maestro("0004","No Advertir"));
        Acto_obs.add(new Maestro("0005","No Asegurar"));
        Acto_obs.add(new Maestro("0006","Desactivar Dispositivos de Seguridad"));
        Acto_obs.add(new Maestro("0007","Usar Equipos y Herramientas Defectuosos"));
        Acto_obs.add(new Maestro("0008","Uso inadecuado o no uso de EPP"));
        Acto_obs.add(new Maestro("0009","Cargar  Incorrectamente"));
        Acto_obs.add(new Maestro("0010","Ubicación Incorrecta"));
        Acto_obs.add(new Maestro("0011","Levantar Incorrectamente"));
        Acto_obs.add(new Maestro("0012","Posición Inadecuada para el Trabajo o la Tarea"));
        Acto_obs.add(new Maestro("0013","Dar mantenimiento a equipo en operación"));
        Acto_obs.add(new Maestro("0014","Jugar en el trabajo"));
        Acto_obs.add(new Maestro("0015","Usar equipo inadecuadamente"));
        Acto_obs.add(new Maestro("0016","Trabajo bajo la Influencia del Alcohol y/u otras Drogas"));
        Acto_obs.add(new Maestro("0017","Maniobra incorrecta"));
        Acto_obs.add(new Maestro("0018","Uso inapropiado de herramientas"));
        Acto_obs.add(new Maestro("0019","Evaluación de riesgos deficiente por parte del personal"));
        Acto_obs.add(new Maestro("0020","Control inadecuado de energía (bloqueo/etiquetado)"));
        Acto_obs.add(new Maestro("0021","Instrumentos mal interpretados / mal leídos"));
        Acto_obs.add(new Maestro("0022","Hechos de violencia"));
        Acto_obs.add(new Maestro("0023","Exponerse a la línea de fuego"));
        Acto_obs.add(new Maestro("0024","No uso de los 3 puntos de apoyo"));
        Acto_obs.add(new Maestro("0025","Intento por realizar tareas múltiples en forma simultánea"));


        TipoAutenticacion.add(new Maestro("B","Básico"));
        TipoAutenticacion.add(new Maestro("W","Windows"));

        Sexo.add(new Maestro("01","Masculino"));
        Sexo.add(new Maestro("02","Femenino"));

        Tablas.add(new Maestro("TCOM","Comites"));
        Tablas.add(new Maestro("THIG","Higiene"));
        Tablas.add(new Maestro("TINC","Incidentes"));
        Tablas.add(new Maestro("TINS","Inspecciones"));
        Tablas.add(new Maestro("TIPE","IPERC"));
        Tablas.add(new Maestro("TOBS","Observaciones"));
        Tablas.add(new Maestro("TREU","Reuniones"));
        Tablas.add(new Maestro("TSIM","Simulacro"));
        Tablas.add(new Maestro("TYOS","Yo Aseguro"));
        Tablas.add(new Maestro("TCAP","ActaAsistencia"));
        Tablas.add(new Maestro("TDETAINSP","DetalleInspeccion"));
        Tablas.add(new Maestro("TDIPEAFEC","DiasPerdidosAfectado"));
        Tablas.add(new Maestro("TEAU","EquipoAuditor"));
        Tablas.add(new Maestro("TEIN","EquipoInvestigacion"));
        Tablas.add(new Maestro("THALL","Hallazgos"));
        Tablas.add(new Maestro("TINVEAFEC","InvestigaAfectado"));
        Tablas.add(new Maestro("TSEC","SecuenciaEvento"));
        Tablas.add(new Maestro("TSIM","Simulacro"));
        Tablas.add(new Maestro("TTES","TestigoInvolucrado"));
        Tablas.add(new Maestro("OTROS","Otros"));

    }
    public static ArrayList<Maestro> loadUbicacion(String Tipo, int nivel){
        ArrayList<Maestro> Ubicaciones = new ArrayList<>();
        Ubicaciones.add(new Maestro(Tipo,"-  Seleccione  -"));
        for (Maestro item:Ubicaciones_obs ) {
            String Tipos[]=item.CodTipo.split("\\.");
            if(Tipos.length==nivel){
                if(nivel==3 && Tipo.split("\\.").length==2&& Tipos[0].equals(Tipo.split("\\.")[0]) && Tipos[1].equals(Tipo.split("\\.")[1]))
                    Ubicaciones.add(item);
                if(nivel==2 && Tipos[0].equals(Tipo))
                    Ubicaciones.add(item);
                else if(nivel==1) Ubicaciones.add(item);
            }
        }
        return Ubicaciones;
    }

    public static ArrayList<Maestro> loadSuperInt(String Tipo){
        ArrayList<Maestro> Super = new ArrayList<>();
        Super.add(new Maestro("","-  Seleccione  -"));
        for (Maestro item:SuperIntendencia ) {
            String Tipos[]=item.CodTipo.split("\\.");
            if(Tipos[0].equals(Tipo))
                    Super.add(item);
        }
        return Super;
    }


    public static String CodObs="";

    public static List<GaleriaModel> listaImgVid =new ArrayList<GaleriaModel>();

    public static int con_status_video=200;

    public static boolean flagObsFiltro=true;
    public static boolean istabs=false;
    public static boolean isUserlogin=false;
   // public static List<Maestro> listPlan=new ArrayList<>();

    //autenticacion
    public static String reemplazar(String cadena, String busqueda, String reemplazo) {
        return cadena.replaceAll(busqueda, reemplazo);
    }
    public static String reemplazarUnicode(String cadena) {
        String cadena1 = GlobalVariables.reemplazar(cadena, "\\\\u000a", "\n");
        String cadena2 = GlobalVariables.reemplazar(cadena1,"\\\\u000d", "\r");
        String cadena3 = GlobalVariables.reemplazar(cadena2,"\\\\u0009", "\t");
        return cadena3;
    }



  /*  public static void LoadData() {
        if(Ubicaciones_obs.isEmpty())GetMaestroLocal("UBIC");
        if(Gerencia.isEmpty())GetMaestroLocal("GERE");
        if(SuperIntendencia.isEmpty())GetMaestroLocal("SUPE");
        if(Contrata.isEmpty())GetMaestroLocal("PROV");
        //observacion
        if(HHA_obs.isEmpty())GetMaestroLocal("HHAR");
        if(Actividad_obs.isEmpty())GetMaestroLocal("ACTR");
        if(Error_obs.isEmpty())GetMaestroLocal("EROB");
        if(Estado_obs.isEmpty())GetMaestroLocal("ESOB");
        if(Tipo_obs2.isEmpty())GetMaestroLocal("TPOB");

        //inspecciones
        if(Aspecto_Obs.isEmpty())GetMaestroLocal("ASPO");
        if(Tipo_insp.isEmpty())GetMaestroLocal("TPIN");

        //Plan de Accion
        if(Area_obs.isEmpty())GetMaestroLocal("AREA");
        if(Tipo_Plan.isEmpty())GetMaestroLocal("TPAC");

        Ubicacion_obs=loadUbicacion("",1);
    }

    public  static void GetMaestroLocal(String Tipo){
        String data1 = Recuperar_data(Tipo);
        String url=Url_base+"Maestro/GetTipoMaestro/"+Tipo;
        Gson gson = new Gson();

        if(data1=="" || data1.contains("Count\":-1")){
            GlobalVariables Objeto= new GlobalVariables();
            final ActivityController obj = new ActivityController("get", url,Objeto);
            obj.execute(Tipo);
        }
        else {
            GetMaestroModel getMaestroModel = gson.fromJson(data1, GetMaestroModel.class);
            switch (Tipo){
                case "UBIC":
                    Ubicaciones_obs=getMaestroModel.Data;
                    break;
                case "GERE":
                    Gerencia=getMaestroModel.Data;
                    break;
                case "SUPE":
                    SuperIntendencia=getMaestroModel.Data;
                    break;
                case "PROV":
                    Contrata=getMaestroModel.Data;
                    break;
                //observacion
                case "HHAR":
                    HHA_obs=getMaestroModel.Data;
                    break;
                case "ACTR":
                    Actividad_obs=getMaestroModel.Data;
                    break;
                case "TPOB":
                    Tipo_obs2=getMaestroModel.Data;
                    break;
                case "ESOB":
                    Estado_obs=getMaestroModel.Data;
                    break;
                case "EROB":
                    Error_obs=getMaestroModel.Data;
                    break;
                //inspecciones
                case "ASPO":
                    Aspecto_Obs=getMaestroModel.Data;
                    break;
                case "TPIN":
                    Tipo_insp=getMaestroModel.Data;
                    break;
                //Plan de Accion
                case "AREA":
                    Area_obs=getMaestroModel.Data;
                    break;
                case "TPAC":
                    Tipo_Plan=getMaestroModel.Data;
                    break;
                *//*default:
                    break;*//*
            }
        }
    }

    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        GetMaestroModel getMaestroModel = gson.fromJson(data, GetMaestroModel.class);
        if (getMaestroModel.Count > 0){
            switch (Tipo) {
                case "UBIC":
                    Ubicaciones_obs = getMaestroModel.Data;
                    break;
                case "GERE":
                    Gerencia = getMaestroModel.Data;
                    break;
                case "SUPE":
                    SuperIntendencia = getMaestroModel.Data;
                    break;
                case "PROV":
                    Contrata = getMaestroModel.Data;
                    break;
                //observacion
                case "HHAR":
                    HHA_obs = getMaestroModel.Data;
                    break;
                case "ACTR":
                    Actividad_obs = getMaestroModel.Data;
                    break;
                case "TPOB":
                    Tipo_obs2 = getMaestroModel.Data;
                    break;
                case "ESOB":
                    Estado_obs = getMaestroModel.Data;
                    break;
                case "EROB":
                    Error_obs = getMaestroModel.Data;
                    break;
                //inspecciones
                case "ASPO":
                    Aspecto_Obs = getMaestroModel.Data;
                    break;
                case "TPIN":
                    Tipo_insp = getMaestroModel.Data;
                    break;
                //Plan de Accion
                case "AREA":
                    Area_obs = getMaestroModel.Data;
                    break;
                case "TPAC":
                    Tipo_Plan = getMaestroModel.Data;
                    break;
                *//*default:
                    break;*//*
            }
                Context applicationContext = MainActivity.getContextOfApplication();
                SharedPreferences VarMaestros = applicationContext.getSharedPreferences("HSEC_Maestros", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = VarMaestros.edit();
                editor.putString(Tipo, String.valueOf(data));
                editor.commit();
            }
            else  Toast.makeText(MainActivity.getContextOfApplication(),"Ocurrio un error al sicronizar datos",Toast.LENGTH_SHORT).show();
    }

    public static String Recuperar_data(String Variable) {
       // Context applicationContext = MainActivity.getContextOfApplication() ;
        SharedPreferences VarMaestros =  activity.getSharedPreferences("HSEC_Maestros", Context.MODE_PRIVATE);
        String ListaMaestro = VarMaestros.getString(Variable,"");
        return ListaMaestro;
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }*/
}
