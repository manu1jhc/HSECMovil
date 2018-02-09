package com.pango.hsec.hsec;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.View;

import com.google.gson.Gson;
import com.pango.hsec.hsec.controller.ActivityController;
import com.pango.hsec.hsec.model.GaleriaModel;
import com.pango.hsec.hsec.model.GetMaestroModel;
import com.pango.hsec.hsec.model.Maestro;
import com.pango.hsec.hsec.model.PersonaModel;
import com.pango.hsec.hsec.model.PublicacionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by Andre on 12/12/2017.
 */

public class GlobalVariables implements IActivity {

    //public static String Urlbase2 = "entrada/getpaginated/";
    public  static int con_status=0;
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
    //public static String Url_base="http://servidorpango/whsec_Servicedmz/api/";
    public static String Url_base="http://192.168.1.2/whsec_Servicedmz/api/";

    public static boolean flagUpSc=false;
   // public static boolean FDown=false;
   public static int count=5;
    public static boolean flag_up_toast=false;

    public static ArrayList<PublicacionModel> listaGlobal = new  ArrayList<PublicacionModel>();
    public static ArrayList<PersonaModel> lista_Personas=new ArrayList<>();

    public static ArrayList<PublicacionModel> listaGlobalFiltro = new  ArrayList<PublicacionModel>();

    public static String  json_user="";

    //public static String[] obsDetcab={"CodObservacion","CodAreaHSEC","CodNivelRiesgo","ObservadoPor","Fecha","Hora","Gerencia","Superint","CodUbicacion","CodSubUbicacion","UbicacionEsp","Lugar","CodTipo"};
    //public static String[] obsDetIzq ={"Codigo","Area","Nivel de riesgo","Observado Por","Fecha","Hora","Gerencia","Superintendencia","Ubicacion","Sub Ubicación","Ubicación Específica","Lugar","Tipo"};

    public static String[] obsDetListacab ={"CodObservacion","CodAreaHSEC","CodNivelRiesgo","ObservadoPor","Fecha","Hora","Gerencia","Superint","CodUbicacion","CodSubUbicacion","UbicacionEsp","Lugar","CodTipo"};
    public static String[] obsDetListIzq ={"Codigo","Area","Nivel de riesgo","Observado Por","Fecha","Hora","Gerencia","Superintendencia","Ubicacion","Sub Ubicación","Ubicación Específica","Lugar","Tipo"};

    public static String[] planDetCab={"CodAccion","NroDocReferencia","CodAreaHSEC", "CodNivelRiesgo","DesPlanAccion","FechaSolicitud","CodEstadoAccion","CodSolicitadoPor","CodResponsable","CodActiRelacionada","CodReferencia", "CodTipoAccion","FecComprometidaInicial","FecComprometidaFinal"};
    public static String[] planDetIzq={"Código de acción", "Nro. doc. de referencia", "area","Nivel de riesgo", "Descripcion", "Fecha de solicitud", "Estado", "Solicitado por", "Responsable", "Actividad relacionada","Referencia", "Tipo de acción", "Fecha inicial","Fecha final" };

    public static String[] busqueda_tipo={"Observaciones", "Inspecciones","Noticias"};

    public static int  CodRol=3;



    //variables globales select

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

    //Obserbacion Detalle
    public static  ArrayList<Maestro> Actividad_obs = new ArrayList<>();
    public static  ArrayList<Maestro> HHA_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Acto_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Condicion_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Estado_obs = new ArrayList<>();
    public static  ArrayList<Maestro> Actv_Relacionada = new ArrayList<>();
    public static  ArrayList<Maestro> Aspecto_Obs = new ArrayList<>();
    public static  ArrayList<Maestro> Error_obs = new ArrayList<>();

    public static String TipoObservacion = "TO01";
    public static String getDescripcion(ArrayList<Maestro> Obj, String value){
        for (Maestro o : Obj  ) {
            if(o.CodTipo.equals(value)) return o.Descripcion;
        }
        return "";
    }
    public static  ArrayList<Maestro> SuperInt_Busq = new ArrayList<>();

    public static void LoadData() {
        if(!Gerencia.isEmpty()) return;

        GetMaestroLocal("UBIC");
        GetMaestroLocal("GERE");
        GetMaestroLocal("SUPE");
        GetMaestroLocal("PROV");
        Ubicacion_obs=loadUbicacion("",1);
    }

    public  static void GetMaestroLocal(String Tipo){
        String data1 = Recuperar_data(Tipo);
        String url=Url_base+"Maestro/GetTipoMaestro/"+Tipo;
        Gson gson = new Gson();

        if(data1==""){
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
                /*default:
                    break;*/
                }
        }
    }

    public static void loadObs_Detalles(){

        if(!Area_obs.isEmpty()) return;
        
        Referencia_Plan.add(new Maestro("01", "Observaciones"));
        Referencia_Plan.add(new Maestro("02", "Inspecciones"));

        Tipo_Plan.add(new Maestro("01","Correctiva"));
        Tipo_Plan.add(new Maestro("02","Preventiva"));

        Estado_Plan.add(new Maestro("01","Pendiente"));
        Estado_Plan.add(new Maestro("02","Atendido"));
        Estado_Plan.add(new Maestro("03","En Proceso"));
        Estado_Plan.add(new Maestro("04","Observado"));
        Estado_Plan.add(new Maestro("05","Cerrado"));

        Area_obs.add(new Maestro("001", "Seguridad"));
        Area_obs.add(new Maestro("002", "Salud Ocupacional"));
        Area_obs.add(new Maestro("004", "Comunidades"));

        Tipo_insp.add(new Maestro("1","Operativo"));
        Tipo_insp.add(new Maestro("2","Gerencial"));
        Tipo_insp.add(new Maestro("3","Comité"));


        Tipo_obs.add(new Maestro("TO01", "Comportamiento"));
        Tipo_obs.add(new Maestro("TO02", "Condición"));

        Tipo_obs2.add(new Maestro("TO01", "Comportamiento"));
        Tipo_obs2.add(new Maestro("TO02", "Condición"));
        Tipo_obs2.add(new Maestro("TO03", "Tarea"));
        Tipo_obs2.add(new Maestro("TO04", "Interacción de  Seguridad (IS)"));

        NivelRiesgo_obs.add(new Maestro("BA", "Baja"));
        NivelRiesgo_obs.add(new Maestro("ME", "Media"));
        NivelRiesgo_obs.add(new Maestro("AL", "Alta"));

        //Error comportamiento
        Error_obs.add(new Maestro("01","Ojos no en la tarea"));
        Error_obs.add(new Maestro("02","Mente no en la tarea"));
        Error_obs.add(new Maestro("03","Ubicarse en la linea de fuego"));
        Error_obs.add(new Maestro("04","Perder el equilibrio/traccion/agarre"));
        Error_obs.add(new Maestro("05","No aplica"));
        //Estado comportamiento
        Estado_obs.add(new Maestro("01","Prisa"));
        Estado_obs.add(new Maestro("02","Frustracion"));
        Estado_obs.add(new Maestro("03","Fatiga"));
        Estado_obs.add(new Maestro("04","Exceso de confianza"));
        Estado_obs.add(new Maestro("05","No aplica"));

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

        //HHA
        HHA_obs.add(new Maestro("14","N/A"));
        HHA_obs.add(new Maestro("01","Aislamiento y Bloqueo de energía"));
        HHA_obs.add(new Maestro("02","Trabajo en altura"));
        HHA_obs.add(new Maestro("03","Trabajo en Espacios confinados"));
        HHA_obs.add(new Maestro("04","Operación de Equipos móviles"));
        HHA_obs.add(new Maestro("05","Trabajo en zonas con riesgo de Falla del terreno (Incluye excavaciones)"));
        HHA_obs.add(new Maestro("06","Seguridad eléctrica"));
        HHA_obs.add(new Maestro("07","Respuesta ante emergencias"));
        HHA_obs.add(new Maestro("08","Izaje y Levantamiento de carga"));
        HHA_obs.add(new Maestro("09","Trabajo con riesgo de Incendio y explosión (Trabajo en Caliente)"));
        HHA_obs.add(new Maestro("10","Manipulación de Explosivos y Voladura"));
        HHA_obs.add(new Maestro("11","Uso y Mantenimiento de Neumaticos y Aros"));
        HHA_obs.add(new Maestro("12","Trabajo con riesgo de Irrupción"));
        HHA_obs.add(new Maestro("13","Manipulación de sustancias químicas"));
        HHA_obs.add(new Maestro("15","Actividades de Riesgo"));
        HHA_obs.add(new Maestro("16","Higiene Ocupacional"));
        HHA_obs.add(new Maestro("17","Aislamiento y Bloqueo"));
        HHA_obs.add(new Maestro("18","Izaje y levantamiento de Carga"));
        HHA_obs.add(new Maestro("19","Otros"));
        HHA_obs.add(new Maestro("20","Trabajo en medio acuático (Presa de Relaves)"));
        HHA_obs.add(new Maestro("21","Operación de herramientas rotativas"));
        //actividad
        Actividad_obs.add(new Maestro("0","Ninguna"));
        Actividad_obs.add(new Maestro("6","Acopio de residuos"));
        Actividad_obs.add(new Maestro("21","Almacenamiento"));
        Actividad_obs.add(new Maestro("26","Calibración de equipos"));
        Actividad_obs.add(new Maestro("20","Desarmado de estructuras"));
        Actividad_obs.add(new Maestro("38","Desarrollo de voladura"));
        Actividad_obs.add(new Maestro("39","Drenaje ó bombeo"));
        Actividad_obs.add(new Maestro("36","Entrenamiento y Capacitación"));
        Actividad_obs.add(new Maestro("10","Inspección"));
        Actividad_obs.add(new Maestro("23","Limpieza"));
        Actividad_obs.add(new Maestro("1","Manipulación de cables"));
        Actividad_obs.add(new Maestro("5","Manipulación de componentes"));
        Actividad_obs.add(new Maestro("25","Manipulación de explosivos"));
        Actividad_obs.add(new Maestro("27","Manipulación de Herramientas"));
        Actividad_obs.add(new Maestro("19","Manipulación de sustancias químicas"));
        Actividad_obs.add(new Maestro("7","Mantenimiento de equipos de planta"));
        Actividad_obs.add(new Maestro("17","Mantenimiento de equipos móviles"));
        Actividad_obs.add(new Maestro("16","Mantenimiento de llantas gigantes"));
        Actividad_obs.add(new Maestro("15","Mantenimiento de subestaciones eléctricas"));
        Actividad_obs.add(new Maestro("13","Mantenimiento de tolvas"));
        Actividad_obs.add(new Maestro("28","Monitoreo e instrumentación"));
        Actividad_obs.add(new Maestro("24","Movimiento de tierras"));
        Actividad_obs.add(new Maestro("40","Muestreria de ripios, rocas"));
        Actividad_obs.add(new Maestro("3","Operación de equipo auxiliar"));
        Actividad_obs.add(new Maestro("4","Operación de equipo de planta"));
        Actividad_obs.add(new Maestro("2","Operación de equipó pesado"));
        Actividad_obs.add(new Maestro("14","Operación de vehículos livianos"));
        Actividad_obs.add(new Maestro("22","Recepción y despacho de materiales"));
        Actividad_obs.add(new Maestro("9","Supervisión"));
        Actividad_obs.add(new Maestro("18","Topografía"));
        Actividad_obs.add(new Maestro("12","Trabajo de oficina"));
        Actividad_obs.add(new Maestro("8","Trabajo en cocina"));
        Actividad_obs.add(new Maestro("32","Trabajos con HDPE"));
        Actividad_obs.add(new Maestro("29","Trabajos con Obras Civiles"));
        Actividad_obs.add(new Maestro("30","Trabajos con obras mecánicas"));
        Actividad_obs.add(new Maestro("37","Trabajos de metal mecánica"));
        Actividad_obs.add(new Maestro("31","Trabajos Eléctricos"));
        Actividad_obs.add(new Maestro("11","Tránsito"));
        Actividad_obs.add(new Maestro("33","Transporte de Carga"));
        Actividad_obs.add(new Maestro("34","Transporte de Concentrado"));
        Actividad_obs.add(new Maestro("41","Transporte de personal"));
        Actividad_obs.add(new Maestro("35","Transporte de sustancias peligrosas"));

        //Actividad relacionada
        Actv_Relacionada.add(new Maestro("0","Ninguna"));
        Actv_Relacionada.add(new Maestro("6","Acopio de residuos"));
        Actv_Relacionada.add(new Maestro("21","Almacenamiento"));
        Actv_Relacionada.add(new Maestro("26","Calibración de equipos"));
        Actv_Relacionada.add(new Maestro("20","Desarmado de estructuras"));
        Actv_Relacionada.add(new Maestro("38","Desarrollo de voladura"));
        Actv_Relacionada.add(new Maestro("39","Drenaje ó bombeo"));
        Actv_Relacionada.add(new Maestro("36","Entrenamiento y Capacitación"));
        Actv_Relacionada.add(new Maestro("10","Inspección"));
        Actv_Relacionada.add(new Maestro("23","Limpieza"));
        Actv_Relacionada.add(new Maestro("1","Manipulación de cables"));
        Actv_Relacionada.add(new Maestro("5","Manipulación de componentes"));
        Actv_Relacionada.add(new Maestro("25","Manipulación de explosivos"));
        Actv_Relacionada.add(new Maestro("27","Manipulación de Herramientas"));
        Actv_Relacionada.add(new Maestro("19","Manipulación de sustancias químicas"));
        Actv_Relacionada.add(new Maestro("7","Mantenimiento de equipos de planta"));
        Actv_Relacionada.add(new Maestro("17","Mantenimiento de equipos móviles"));
        Actv_Relacionada.add(new Maestro("16","Mantenimiento de llantas gigantes"));
        Actv_Relacionada.add(new Maestro("15","Mantenimiento de subestaciones eléctricas"));
        Actv_Relacionada.add(new Maestro("13","Mantenimiento de tolvas"));
        Actv_Relacionada.add(new Maestro("28","Monitoreo e instrumentación"));
        Actv_Relacionada.add(new Maestro("24","Movimiento de tierras"));
        Actv_Relacionada.add(new Maestro("40","Muestreria de ripios, rocas"));
        Actv_Relacionada.add(new Maestro("3","Operación de equipo auxiliar"));
        Actv_Relacionada.add(new Maestro("4","Operación de equipo de planta"));
        Actv_Relacionada.add(new Maestro("2","Operación de equipó pesado"));
        Actv_Relacionada.add(new Maestro("14","Operación de vehículos livianos"));
        Actv_Relacionada.add(new Maestro("22","Recepción y despacho de materiales"));
        Actv_Relacionada.add(new Maestro("9","Supervisión"));
        Actv_Relacionada.add(new Maestro("18","Topografía"));
        Actv_Relacionada.add(new Maestro("12","Trabajo de oficina"));
        Actv_Relacionada.add(new Maestro("8","Trabajo en cocina"));
        Actv_Relacionada.add(new Maestro("32","Trabajos con HDPE"));
        Actv_Relacionada.add(new Maestro("29","Trabajos con Obras Civiles"));
        Actv_Relacionada.add(new Maestro("30","Trabajos con obras mecánicas"));
        Actv_Relacionada.add(new Maestro("37","Trabajos de metal mecánica"));
        Actv_Relacionada.add(new Maestro("31","Trabajos Eléctricos"));
        Actv_Relacionada.add(new Maestro("11","Tránsito"));
        Actv_Relacionada.add(new Maestro("33","Transporte de Carga"));
        Actv_Relacionada.add(new Maestro("34","Transporte de Concentrado"));
        Actv_Relacionada.add(new Maestro("41","Transporte de personal"));
        Actv_Relacionada.add(new Maestro("35","Transporte de sustancias peligrosas"));

        //aspectos observados
        Aspecto_Obs.add(new Maestro("01","EDIFICIOS Y PISOS"));
        Aspecto_Obs.add(new Maestro("02","COMEDORES Y SERVICIOS HIGIÉNICOS"));
        Aspecto_Obs.add(new Maestro("03","ERGONOMÍA / HIGIENE OCUPACIONAL"));
        Aspecto_Obs.add(new Maestro("04","EQUIPO DE PROTECCION PERSONAL"));
        Aspecto_Obs.add(new Maestro("05","ALMACENAMIENTO Y APILAMIENTO"));
        Aspecto_Obs.add(new Maestro("06","SEÑALIZACION Y CÓDIGO DE COLORES"));
        Aspecto_Obs.add(new Maestro("07","EQUIPAMIENTO DE RESPUESTA A EMERGENCIAS"));
        Aspecto_Obs.add(new Maestro("08","AST/ PERMISOS /PROCEDIMIENTOS"));
        Aspecto_Obs.add(new Maestro("09","HERRAMIENTAS DE MANO"));
        Aspecto_Obs.add(new Maestro("10","EQUIPO ELÉCTRICO PORTÁTIL"));
        Aspecto_Obs.add(new Maestro("11","RECIPIENTES A PRESIÓN"));
        Aspecto_Obs.add(new Maestro("12","VEHÍCULOS LIVIANOS /TRANSPORTE DE PERSONAL"));
        Aspecto_Obs.add(new Maestro("13","EQUIPO AUXILIAR Y PESADO"));
        Aspecto_Obs.add(new Maestro("14","TRABAJO EN ALTURA"));
        Aspecto_Obs.add(new Maestro("15","GUARDAS DE PROTECCIÓN"));
        Aspecto_Obs.add(new Maestro("16","SUSTANCIAS PELIGROSAS"));
        Aspecto_Obs.add(new Maestro("17","AISLAMIENTO Y BLOQUEO"));
        Aspecto_Obs.add(new Maestro("18","IZAJE O LEVANTAMIENTO"));
        Aspecto_Obs.add(new Maestro("19","GENERACION Y DISPOSICION DE RESIDUOS SOLIDOS"));
        Aspecto_Obs.add(new Maestro("20","SISTEMA DE DRENAJE"));
        Aspecto_Obs.add(new Maestro("21","MANTENIMIENTO"));
        Aspecto_Obs.add(new Maestro("22","DERRAMES"));
        Aspecto_Obs.add(new Maestro("23","USO DE RECURSOS"));
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

    public static List<GaleriaModel> listaGaleria =new ArrayList<GaleriaModel>();

    public static List<GaleriaModel> listaImgVid =new ArrayList<GaleriaModel>();

    public static int con_status_video=200;


    public static boolean flagObsFiltro=true;
    public static boolean istabs=false;

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

    @Override
    public void success(String data, String Tipo) {
        Gson gson = new Gson();
        GetMaestroModel getMaestroModel = gson.fromJson(data, GetMaestroModel.class);
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
                /*default:
                    break;*/
        }
        Context applicationContext = MainActivity.getContextOfApplication() ;
        SharedPreferences VarMaestros = applicationContext.getSharedPreferences("HSEC_Maestros", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = VarMaestros.edit();
        editor.putString(Tipo, String.valueOf(data));
        editor.commit();
    }

    public static String Recuperar_data(String Variable) {
        Context applicationContext = MainActivity.getContextOfApplication() ;
        SharedPreferences VarMaestros =  applicationContext.getSharedPreferences("HSEC_Maestros", Context.MODE_PRIVATE);
        String ListaMaestro = VarMaestros.getString(Variable,"");
        return ListaMaestro;
    }

    @Override
    public void successpost(String data, String Tipo) {

    }

    @Override
    public void error(String mensaje, String Tipo) {

    }

    public static void loadUbicaciones(){
        Ubicaciones_obs.add(new Maestro("01","Mantenimiento Mina"));
        Ubicaciones_obs.add(new Maestro("01.01","Nave de mantenimiento (TruckShop)"));
        Ubicaciones_obs.add(new Maestro("01.01.01","Taller de mantenimiento camiones"));
        Ubicaciones_obs.add(new Maestro("01.01.02","Taller de flota liviana"));
        Ubicaciones_obs.add(new Maestro("01.01.03","Almacén de componentes"));
        Ubicaciones_obs.add(new Maestro("01.01.04","Almacén de Backlogs"));
        Ubicaciones_obs.add(new Maestro("01.01.05","Comedor"));
        Ubicaciones_obs.add(new Maestro("01.01.06","Vestuario"));
        Ubicaciones_obs.add(new Maestro("01.01.07","Oficinas"));
        Ubicaciones_obs.add(new Maestro("01.01.08","Sala de Cambio de Guardia"));
        Ubicaciones_obs.add(new Maestro("01.01.09","Sala de Capacitación"));
        Ubicaciones_obs.add(new Maestro("01.02","Taller de enllante"));
        Ubicaciones_obs.add(new Maestro("01.02.01","Plataforma de enllante"));
        Ubicaciones_obs.add(new Maestro("01.02.02","Almacen de llantas"));
        Ubicaciones_obs.add(new Maestro("01.02.03","Oficinas"));
        Ubicaciones_obs.add(new Maestro("01.02.04","Sala de compresores"));
        Ubicaciones_obs.add(new Maestro("01.03","Plataforma de contratistas"));
        Ubicaciones_obs.add(new Maestro("01.03.01","Ferreyros"));
        Ubicaciones_obs.add(new Maestro("01.03.02","Komatsu"));
        Ubicaciones_obs.add(new Maestro("01.03.03","Detroit Diessel MTU"));
        Ubicaciones_obs.add(new Maestro("01.03.04","JP Ingenieria"));
        Ubicaciones_obs.add(new Maestro("01.03.05","Joy Global"));
        Ubicaciones_obs.add(new Maestro("01.03.06","Plataforma de lubricantes"));
        Ubicaciones_obs.add(new Maestro("01.04","Taller de flota liviana"));
        Ubicaciones_obs.add(new Maestro("01.04.01","Taller de mantenimiento de flota liviana"));
        Ubicaciones_obs.add(new Maestro("01.04.02","Oficinas"));
        Ubicaciones_obs.add(new Maestro("01.04.03","Wong y Cia"));
        Ubicaciones_obs.add(new Maestro("01.04.04","Almacenes"));
        Ubicaciones_obs.add(new Maestro("01.04.05","Parqueo de equipo"));
        Ubicaciones_obs.add(new Maestro("01.04.06","Lavadero de equipo pesado"));
        Ubicaciones_obs.add(new Maestro("01.05","Reparación de componentes eléctricos"));
        Ubicaciones_obs.add(new Maestro("01.05.01","Taller de reparacion de componentes y bombas"));
        Ubicaciones_obs.add(new Maestro("01.05.02","Oficinas"));
        Ubicaciones_obs.add(new Maestro("01.05.03","Almacenes"));
        Ubicaciones_obs.add(new Maestro("01.05.04","Xylem"));
        Ubicaciones_obs.add(new Maestro("01.06","Reparación de tolvas"));
        Ubicaciones_obs.add(new Maestro("01.06.01","Taller de soldadura"));
        Ubicaciones_obs.add(new Maestro("01.06.02","Reparacion de tolvas"));
        Ubicaciones_obs.add(new Maestro("01.06.03","Reparacion de componentes"));
        Ubicaciones_obs.add(new Maestro("01.06.04","Almacen de Ferreyros"));
        Ubicaciones_obs.add(new Maestro("01.06.05","Parqueo de gruas"));
        Ubicaciones_obs.add(new Maestro("01.06.06","Oficinas"));
        Ubicaciones_obs.add(new Maestro("01.07","Taller de palas y Perforadoras"));
        Ubicaciones_obs.add(new Maestro("01.07.01","Oficinas"));
        Ubicaciones_obs.add(new Maestro("01.07.02","Almacenes"));
        Ubicaciones_obs.add(new Maestro("01.07.03","Reparacion de cables"));
        Ubicaciones_obs.add(new Maestro("01.07.04","Plataforma de componetes"));
        Ubicaciones_obs.add(new Maestro("01.07.05","Taller de soldadura"));
        Ubicaciones_obs.add(new Maestro("01.07.06","Plataforma de Mantenimiento en Mina "));
        Ubicaciones_obs.add(new Maestro("01.08","Taller de distribución de energía"));
        Ubicaciones_obs.add(new Maestro("01.08.01","Oficinas"));
        Ubicaciones_obs.add(new Maestro("01.08.02","Almacenes"));
        Ubicaciones_obs.add(new Maestro("01.08.03","Comunicaciones"));
        Ubicaciones_obs.add(new Maestro("01.08.04","Taller Tisher"));
        Ubicaciones_obs.add(new Maestro("01.09","Sistemas de Potencia"));
        Ubicaciones_obs.add(new Maestro("01.09.01","Casa Fuerza"));
        Ubicaciones_obs.add(new Maestro("01.09.02","Sub Estacion 931"));
        Ubicaciones_obs.add(new Maestro("01.09.03","Sub Estación Tintaya Nueva"));
        Ubicaciones_obs.add(new Maestro("01.10","Taller de Flota Auxiliar"));
        Ubicaciones_obs.add(new Maestro("01.11","Plataforma de Lavado"));
        Ubicaciones_obs.add(new Maestro("01.12","Plataforma de armado de equipos"));
        Ubicaciones_obs.add(new Maestro("01.13","Plataforma de equipos en desuso"));
        Ubicaciones_obs.add(new Maestro("01.14","Reparación de cables eléctricos"));
        Ubicaciones_obs.add(new Maestro("01.14.01","Oficinas"));
        Ubicaciones_obs.add(new Maestro("01.14.02","Plataforma de reparación de cables"));
        Ubicaciones_obs.add(new Maestro("01.14.03","Taller SELIN"));
        Ubicaciones_obs.add(new Maestro("02","TI"));
        Ubicaciones_obs.add(new Maestro("02.01","Oficinas TI "));
        Ubicaciones_obs.add(new Maestro("02.01.01","2do piso, Of. Administrativas campamento 2"));
        Ubicaciones_obs.add(new Maestro("02.02","Sala de servidores"));
        Ubicaciones_obs.add(new Maestro("02.02.01","2do piso, Of. Administrativas campamento 2"));
        Ubicaciones_obs.add(new Maestro("02.03","Almacen TI"));
        Ubicaciones_obs.add(new Maestro("02.03.01","Costado de RRCC, campamento 3"));
        Ubicaciones_obs.add(new Maestro("02.04","Sala reuniones TI"));
        Ubicaciones_obs.add(new Maestro("02.04.01","2do piso, Of. Administrativas campamento 2"));
        Ubicaciones_obs.add(new Maestro("02.05","Cuarto del generador TI"));
        Ubicaciones_obs.add(new Maestro("02.05.01","Espaldas Of. Administrativas campamento 2"));
        Ubicaciones_obs.add(new Maestro("03","Medio Ambiente y Recursos Hidricos"));
        Ubicaciones_obs.add(new Maestro("03.01","Campamento 03"));
        Ubicaciones_obs.add(new Maestro("03.01.01","Taller Hidrogeología"));
        Ubicaciones_obs.add(new Maestro("03.01.02","Oficinas Recursos Hídricos & Hidrogeología"));
        Ubicaciones_obs.add(new Maestro("03.01.03","Planta Arenado"));
        Ubicaciones_obs.add(new Maestro("03.01.04","Almacén Equidrill"));
        Ubicaciones_obs.add(new Maestro("03.02","Antapaccay"));
        Ubicaciones_obs.add(new Maestro("03.02.01","Almacén Hidrogeología"));
        Ubicaciones_obs.add(new Maestro("03.03","Antapaccay – Tajo Sur"));
        Ubicaciones_obs.add(new Maestro("03.03.01","Pozos de bombeo"));
        Ubicaciones_obs.add(new Maestro("03.04","Antapaccay – Tajo Sur"));
        Ubicaciones_obs.add(new Maestro("03.04.01","Plataformas para perforación de pozos"));
        Ubicaciones_obs.add(new Maestro("03.05","Huinipampa"));
        Ubicaciones_obs.add(new Maestro("03.05.01","Campo de pozos de reposición"));
        Ubicaciones_obs.add(new Maestro("03.05.02","Vivero Invernadero Sach’a Wasi"));
        Ubicaciones_obs.add(new Maestro("03.06","Oficinas Superintendencia Medio Ambiente Operaciones"));
        Ubicaciones_obs.add(new Maestro("03.06.01","Campamento 3 (Ex Colegio)"));
        Ubicaciones_obs.add(new Maestro("03.07","Laboratorio de Preparación de Muestras Ambientales"));
        Ubicaciones_obs.add(new Maestro("03.07.01","Campamento 3 (Ex Colegio)"));
        Ubicaciones_obs.add(new Maestro("03.08","Almacén de medio Ambiente "));
        Ubicaciones_obs.add(new Maestro("03.08.01","Campamento 3 (Ex Colegio)"));
        Ubicaciones_obs.add(new Maestro("03.09","Almacén Temporal de Residuos Tintaya"));
        Ubicaciones_obs.add(new Maestro("03.09.01","Planta de Sulfuros (Este)"));
        Ubicaciones_obs.add(new Maestro("03.10","Almacén Temporal de Residuos Antapaccay"));
        Ubicaciones_obs.add(new Maestro("03.10.01","Botadero 28"));
        Ubicaciones_obs.add(new Maestro("03.11","Botadero 28"));
        Ubicaciones_obs.add(new Maestro("03.11.01","Cierre fase I"));
        Ubicaciones_obs.add(new Maestro("03.11.02","Cierre Fase II"));
        Ubicaciones_obs.add(new Maestro("03.11.03","Cierre Fase III"));
        Ubicaciones_obs.add(new Maestro("03.12","Botadero Central"));
        Ubicaciones_obs.add(new Maestro("03.12.01","Cierre Botadero Central"));
        Ubicaciones_obs.add(new Maestro("03.13"," Botadero 20"));
        Ubicaciones_obs.add(new Maestro("03.13.01","Cierre Fase I"));
        Ubicaciones_obs.add(new Maestro("03.14","Presa de relaves Huinipampa"));
        Ubicaciones_obs.add(new Maestro("03.14.01","Area 700"));
        Ubicaciones_obs.add(new Maestro("03.14.02","Area 400"));
        Ubicaciones_obs.add(new Maestro("03.14.03","Cierre Fase I"));
        Ubicaciones_obs.add(new Maestro("03.14.04","Cierre Fase II"));
        Ubicaciones_obs.add(new Maestro("03.14.05","Cierre Fase III"));
        Ubicaciones_obs.add(new Maestro("03.14.06","Cierre Fase IV"));
        Ubicaciones_obs.add(new Maestro("03.14.07"," Talud del Dique"));
        Ubicaciones_obs.add(new Maestro("03.14.08","Canal Sur"));
        Ubicaciones_obs.add(new Maestro("03.14.09","Canal Norte"));
        Ubicaciones_obs.add(new Maestro("03.15","Presa de Relaves Ccamacmayo"));
        Ubicaciones_obs.add(new Maestro("03.15.01","Sub Estación de Bombeo 1"));
        Ubicaciones_obs.add(new Maestro("03.15.02","Sub Estación de Bombeo 2"));
        Ubicaciones_obs.add(new Maestro("03.15.03","Dique Principal"));
        Ubicaciones_obs.add(new Maestro("03.15.04","Dique auxiliar"));
        Ubicaciones_obs.add(new Maestro("03.15.05","Dique 1"));
        Ubicaciones_obs.add(new Maestro("03.15.06","Dique 2"));
        Ubicaciones_obs.add(new Maestro("03.15.07","Área Prueba de Coberturas"));
        Ubicaciones_obs.add(new Maestro("03.15.08","Canal Roubinsky"));
        Ubicaciones_obs.add(new Maestro("03.15.09","Canal Shangrilla"));
        Ubicaciones_obs.add(new Maestro("03.15.10","Mirador Ccamacmayo"));
        Ubicaciones_obs.add(new Maestro("03.15.11","Cierre Fase I"));
        Ubicaciones_obs.add(new Maestro("03.15.12","Cierre Fase II"));
        Ubicaciones_obs.add(new Maestro("03.16","Plantas Piloto Procesos Tintaya"));
        Ubicaciones_obs.add(new Maestro("03.16.01","Planta Piloto Sulfuros"));
        Ubicaciones_obs.add(new Maestro("03.16.02","Planta Piloto Óxidos"));
        Ubicaciones_obs.add(new Maestro("04","General"));
        Ubicaciones_obs.add(new Maestro("04.01","Gerencia General"));
        Ubicaciones_obs.add(new Maestro("04.01.01","Oficina de Gerencia General"));
        Ubicaciones_obs.add(new Maestro("04.01.02","Sala de Reuniones de la Gerencia General"));
        Ubicaciones_obs.add(new Maestro("04.02","Recurso Humanos"));
        Ubicaciones_obs.add(new Maestro("04.02.01","Almacén"));
        Ubicaciones_obs.add(new Maestro("04.02.02","Cafetería"));
        Ubicaciones_obs.add(new Maestro("04.02.03","Oficinas de Administración de Personal"));
        Ubicaciones_obs.add(new Maestro("04.02.04","Oficinas de Desarrollo Organizacional"));
        Ubicaciones_obs.add(new Maestro("04.02.05","Oficinas de Bienestar  Social"));
        Ubicaciones_obs.add(new Maestro("04.02.06","Sala de Reuniones de  Desarrollo Organizacional"));
        Ubicaciones_obs.add(new Maestro("04.03","Comunicaciones"));
        Ubicaciones_obs.add(new Maestro("04.03.01","Oficina de comunicaciones"));
        Ubicaciones_obs.add(new Maestro("04.03.02","Oficina de Superintendencia de comunicaciones"));
        Ubicaciones_obs.add(new Maestro("04.03.03","Radio La Primera"));
        Ubicaciones_obs.add(new Maestro("04.03.04","Almacén de comunicaciones"));
        Ubicaciones_obs.add(new Maestro("04.04","Legal"));
        Ubicaciones_obs.add(new Maestro("04.04.01","Oficinas de legal"));
        Ubicaciones_obs.add(new Maestro("04.04.02","Archivo legal"));
        Ubicaciones_obs.add(new Maestro("04.05","Finanzas"));
        Ubicaciones_obs.add(new Maestro("04.05.01","Oficinas de finanzas"));
        Ubicaciones_obs.add(new Maestro("04.06","Planificación Estratégica"));
        Ubicaciones_obs.add(new Maestro("04.06.01","Oficinas de Planificación Estratégica"));
        Ubicaciones_obs.add(new Maestro("04.07","Control Estratégico"));
        Ubicaciones_obs.add(new Maestro("04.07.01","Oficinas de Control Estratégico"));
        Ubicaciones_obs.add(new Maestro("04.08","Negociación de tierras"));
        Ubicaciones_obs.add(new Maestro("04.08.01","Oficinas de  Negociación de tierras"));
        Ubicaciones_obs.add(new Maestro("04.09","Propiedades y Concesiones"));
        Ubicaciones_obs.add(new Maestro("04.09.01","Oficinas de  Negociación de tierras"));
        Ubicaciones_obs.add(new Maestro("05","Relaciones Comunitarias"));
        Ubicaciones_obs.add(new Maestro("05.01","Oficinas de Relaciones comunitarias"));
        Ubicaciones_obs.add(new Maestro("05.01.01","Oficina de Desarrollo Social"));
        Ubicaciones_obs.add(new Maestro("05.01.02","Sala de reuniones"));
        Ubicaciones_obs.add(new Maestro("06","Riesgos y Proteccion de Activos"));
        Ubicaciones_obs.add(new Maestro("06.01","Garita Principal"));
        Ubicaciones_obs.add(new Maestro("06.01.01","Oficina de seguridad Vial"));
        Ubicaciones_obs.add(new Maestro("06.01.02","Sala de capacitación"));
        Ubicaciones_obs.add(new Maestro("06.01.03","Oficina de identificaciones"));
        Ubicaciones_obs.add(new Maestro("06.01.04","Oficina de Protección interna"));
        Ubicaciones_obs.add(new Maestro("06.01.05","Oficina de emergencias"));
        Ubicaciones_obs.add(new Maestro("06.01.06","Oficinas  de Transportes Reyna"));
        Ubicaciones_obs.add(new Maestro("06.02","Oficinas de  Riesgos y Protección de Activos"));
        Ubicaciones_obs.add(new Maestro("06.02.01","Sala de reuniones"));
        Ubicaciones_obs.add(new Maestro("06.02.02","CCTV"));
        Ubicaciones_obs.add(new Maestro("06.02.03","Oficina de Riesgos"));
        Ubicaciones_obs.add(new Maestro("06.03","Oficinas de Protección Interna"));
        Ubicaciones_obs.add(new Maestro("06.03.01","Oficinas de Protección Interna"));
        Ubicaciones_obs.add(new Maestro("06.03.02","Sala de Reuniones"));
        Ubicaciones_obs.add(new Maestro("06.03.03","Alojamientos"));
        Ubicaciones_obs.add(new Maestro("06.03.04","Comedor"));
        Ubicaciones_obs.add(new Maestro("06.03.05","Cocina"));
        Ubicaciones_obs.add(new Maestro("06.04","Emergencias"));
        Ubicaciones_obs.add(new Maestro("06.04.01","Taller de recarga de extintores"));
        Ubicaciones_obs.add(new Maestro("06.04.02","Almacén"));
        Ubicaciones_obs.add(new Maestro("06.04.03","Taller de bomberos Tintaya"));
        Ubicaciones_obs.add(new Maestro("06.04.04","Taller de bomberos Antapaccay"));
        Ubicaciones_obs.add(new Maestro("06.04.05","Taller de bomberos Truck shop"));
        Ubicaciones_obs.add(new Maestro("07","Seguridad y Salud"));
        Ubicaciones_obs.add(new Maestro("07.01","Oficinas Main Office"));
        Ubicaciones_obs.add(new Maestro("07.01.01","Oficina de Seguridad"));
        Ubicaciones_obs.add(new Maestro("07.01.02","Cafetería"));
        Ubicaciones_obs.add(new Maestro("07.01.03","Almacén"));
        Ubicaciones_obs.add(new Maestro("07.01.04","Archivo"));
        Ubicaciones_obs.add(new Maestro("07.01.05","Aula de Inducción"));
        Ubicaciones_obs.add(new Maestro("07.01.06","Aula de capacitación"));
        Ubicaciones_obs.add(new Maestro("07.02","Centro Médico Tintaya"));
        Ubicaciones_obs.add(new Maestro("07.02.01","Atención Médica Especializada"));
        Ubicaciones_obs.add(new Maestro("07.02.02","Atención de Urgencias y Emergencias"));
        Ubicaciones_obs.add(new Maestro("07.02.03","Salud Ocupacional."));
        Ubicaciones_obs.add(new Maestro("07.02.04","Rayos X"));
        Ubicaciones_obs.add(new Maestro("07.02.05","Laboratorio"));
        Ubicaciones_obs.add(new Maestro("07.02.06","Higiene Ocupacional"));
        Ubicaciones_obs.add(new Maestro("07.03","Unidad de Atención Médica I – Molienda"));
        Ubicaciones_obs.add(new Maestro("07.03.01","Atención Médica Ambulatoria"));
        Ubicaciones_obs.add(new Maestro("07.03.02","Atención de Urgencia y Emergencias"));
        Ubicaciones_obs.add(new Maestro("07.03.03","Traslado de pacientes críticos – Ambulancia."));
        Ubicaciones_obs.add(new Maestro("07.03.04","Laboratorio de Higiene Ocupacional"));
        Ubicaciones_obs.add(new Maestro("07.04","Unidad de Atención Médica II – Chancadora"));
        Ubicaciones_obs.add(new Maestro("07.04.01","Atención Médica Ambulatoria"));
        Ubicaciones_obs.add(new Maestro("07.04.02","Atención de Urgencia y Emergencias"));
        Ubicaciones_obs.add(new Maestro("07.04.03","Traslado de pacientes críticos – Ambulancia."));
        Ubicaciones_obs.add(new Maestro("07.04.04","Laboratorio de Higiene Ocupacional"));
        Ubicaciones_obs.add(new Maestro("07.05","Unidad de Atención Médica III – Campamento 03"));
        Ubicaciones_obs.add(new Maestro("07.05.01","Atención Médica Ambulatoria"));
        Ubicaciones_obs.add(new Maestro("07.06","Tópico Coroccohuayco"));
        Ubicaciones_obs.add(new Maestro("07.06.01","Atención de Consultas Ambulatorias."));
        Ubicaciones_obs.add(new Maestro("07.06.02","Respuesta de Urgencias – Emergencia"));
        Ubicaciones_obs.add(new Maestro("07.07","Oficinas Nuevas"));
        Ubicaciones_obs.add(new Maestro("07.07.01","Almacén (Conteiner 1)"));
        Ubicaciones_obs.add(new Maestro("07.07.02","Oficinas externas (conteiner 2)"));
        Ubicaciones_obs.add(new Maestro("07.07.03","Sala de Reuniones"));
        Ubicaciones_obs.add(new Maestro("07.07.04","Cafeteria"));
        Ubicaciones_obs.add(new Maestro("07.07.05","Sala de copias"));
        Ubicaciones_obs.add(new Maestro("07.07.06","Oficinas"));
        Ubicaciones_obs.add(new Maestro("07.07.07","Estacionamiento"));
        Ubicaciones_obs.add(new Maestro("07.08","Sala de Inducción - Garita Principal"));
        Ubicaciones_obs.add(new Maestro("07.09","Sala de Inducción Ex-colegio"));
        Ubicaciones_obs.add(new Maestro("07.10","Sala de Capacitación SAFEWORK (Costado de Ingeniería)"));
        Ubicaciones_obs.add(new Maestro("08","Ingenieria Mina"));
        Ubicaciones_obs.add(new Maestro("08.01","Antapaccay"));
        Ubicaciones_obs.add(new Maestro("08.01.01","Oficinas Ingeniería Mina"));
        Ubicaciones_obs.add(new Maestro("08.01.02","Parqueo Oficinas Ingeniería Mina"));
        Ubicaciones_obs.add(new Maestro("08.01.03","Patio de conteneodres"));
        Ubicaciones_obs.add(new Maestro("08.01.04","Oficinas y Almacenes Geología Mina (Huisa)"));
        Ubicaciones_obs.add(new Maestro("08.01.05","Sala de logueo La Pampilla"));
        Ubicaciones_obs.add(new Maestro("08.01.06","Almacén Geotecnia (La Pampilla)"));
        Ubicaciones_obs.add(new Maestro("08.01.07","Laboratorio de muestrería "));
        Ubicaciones_obs.add(new Maestro("08.02","Campamento 3"));
        Ubicaciones_obs.add(new Maestro("08.02.01","Almacenes de testigos DDH"));
        Ubicaciones_obs.add(new Maestro("08.02.02","Laboratorio de Muestreria BH DDH"));
        Ubicaciones_obs.add(new Maestro("08.02.03","Laboratorio de Geotecnia"));
        Ubicaciones_obs.add(new Maestro("08.02.04","Sala de Logueo"));
        Ubicaciones_obs.add(new Maestro("09","Proyecto Coroccohuayco"));
        Ubicaciones_obs.add(new Maestro("09.01","COROCCOHUAYCO"));
        Ubicaciones_obs.add(new Maestro("09.01.01","Plataforma de Perforadoras"));
        Ubicaciones_obs.add(new Maestro("09.01.02","Posa de Lodos"));
        Ubicaciones_obs.add(new Maestro("09.01.03","Sala de Logueo"));
        Ubicaciones_obs.add(new Maestro("09.01.04","Taller de Mantenimiento"));
        Ubicaciones_obs.add(new Maestro("09.01.05","Almacén"));
        Ubicaciones_obs.add(new Maestro("09.01.06","Oficinas Coroccohuayco"));
        Ubicaciones_obs.add(new Maestro("09.01.07","Campamento"));
        Ubicaciones_obs.add(new Maestro("09.01.08","Comedor Coroccohuayco"));
        Ubicaciones_obs.add(new Maestro("09.01.09","PTAR Coroccohuayco"));
        Ubicaciones_obs.add(new Maestro("09.02","OFICINAS TINTAYA"));
        Ubicaciones_obs.add(new Maestro("09.02.01","oficinas Tintaya"));
        Ubicaciones_obs.add(new Maestro("09.02.02","sala de reuniones"));
        Ubicaciones_obs.add(new Maestro("09.02.03","almacén de testigos"));
        Ubicaciones_obs.add(new Maestro("10","Ingenieria y Servicios"));
        Ubicaciones_obs.add(new Maestro("10.01","ESTACIONES DE AGUA POTABLE"));
        Ubicaciones_obs.add(new Maestro("10.01.01","ESTACION PLANTA DE AGUA"));
        Ubicaciones_obs.add(new Maestro("10.01.02","ESTACION R-3"));
        Ubicaciones_obs.add(new Maestro("10.01.03","ESTACION BOMBEO TANQUES VERDES"));
        Ubicaciones_obs.add(new Maestro("10.01.04","ESTACION BOMBEO INTERMEDIO"));
        Ubicaciones_obs.add(new Maestro("10.01.05","ESTACION BOMBEO TANQUE CAMPAMENTO EMSA"));
        Ubicaciones_obs.add(new Maestro("10.01.06","ESTACION BOMBEO RIO SALADO"));
        Ubicaciones_obs.add(new Maestro("10.02","ESTACIONES DE AGUA RESIDUALES"));
        Ubicaciones_obs.add(new Maestro("10.02.01","ESTACION DE BOMBEO HOSPITAL"));
        Ubicaciones_obs.add(new Maestro("10.02.02","ESTACION DE BOMBEO TAMBOMACHAY"));
        Ubicaciones_obs.add(new Maestro("10.02.03","ESTACION DE BOMBEO TANQUE IMHOFF"));
        Ubicaciones_obs.add(new Maestro("10.02.04","ESTACION DE BOMBEO 100 HP"));
        Ubicaciones_obs.add(new Maestro("10.03","Planta de Tratamiento de Aguas Residuales"));
        Ubicaciones_obs.add(new Maestro("10.03.01","CAMPAMENTO TINTAYA"));
        Ubicaciones_obs.add(new Maestro("10.03.02","PLANTA CONCENTRADORA"));
        Ubicaciones_obs.add(new Maestro("10.03.03","CHANCADO PRIMARIO"));
        Ubicaciones_obs.add(new Maestro("10.04","PLANTA DE CONCRETO"));
        Ubicaciones_obs.add(new Maestro("10.04.01","Planta de Agregados Camacmayo"));
        Ubicaciones_obs.add(new Maestro("10.04.02","zona de despacho de concreto"));
        Ubicaciones_obs.add(new Maestro("10.05","Servicios operacionales"));
        Ubicaciones_obs.add(new Maestro("10.05.01","carretera Imata - Oscoyo"));
        Ubicaciones_obs.add(new Maestro("10.05.02","Carretera Oscoyo - KM 80"));
        Ubicaciones_obs.add(new Maestro("10.05.03","Carretera Km 80 - Garita Bravo 8"));
        Ubicaciones_obs.add(new Maestro("10.05.04","Carretera Truck Shop - Huinipampa"));
        Ubicaciones_obs.add(new Maestro("10.06","Ingeniería"));
        Ubicaciones_obs.add(new Maestro("10.06.01","Oficinas Ingeniería"));
        Ubicaciones_obs.add(new Maestro("10.07","Taller de Contratistas"));
        Ubicaciones_obs.add(new Maestro("11","Mina"));
        Ubicaciones_obs.add(new Maestro("11.01","Almacén de tolvas"));
        Ubicaciones_obs.add(new Maestro("11.02","Bahía Norte A"));
        Ubicaciones_obs.add(new Maestro("11.03","Bahía Norte B"));
        Ubicaciones_obs.add(new Maestro("11.04","Bahía Sur A"));
        Ubicaciones_obs.add(new Maestro("11.05","Botadero 1"));
        Ubicaciones_obs.add(new Maestro("11.06","Botadero 2"));
        Ubicaciones_obs.add(new Maestro("11.07","Botadero 3"));
        Ubicaciones_obs.add(new Maestro("11.08","Botadero 4"));
        Ubicaciones_obs.add(new Maestro("11.09","Botadero 5"));
        Ubicaciones_obs.add(new Maestro("11.10","Botadero 6"));
        Ubicaciones_obs.add(new Maestro("11.11","Botadero 7"));
        Ubicaciones_obs.add(new Maestro("11.12","Cancha de Nitrato"));
        Ubicaciones_obs.add(new Maestro("11.13","Cantera 2"));
        Ubicaciones_obs.add(new Maestro("11.14","Cantera 3"));
        Ubicaciones_obs.add(new Maestro("11.15","Carretera de Integracion"));
        Ubicaciones_obs.add(new Maestro("11.16","Chancadora Lastre"));
        Ubicaciones_obs.add(new Maestro("11.17","Chancadora Oxidos"));
        Ubicaciones_obs.add(new Maestro("11.18","Chancadora Sulfuros"));
        Ubicaciones_obs.add(new Maestro("11.19","Garzas de camión"));
        Ubicaciones_obs.add(new Maestro("11.20","Garzas de cisterna"));
        Ubicaciones_obs.add(new Maestro("11.21","Grifo Antapaccay"));
        Ubicaciones_obs.add(new Maestro("11.22","Grifo Sur"));
        Ubicaciones_obs.add(new Maestro("11.23","Megapozas"));
        Ubicaciones_obs.add(new Maestro("11.24","Mirador Alcamari"));
        Ubicaciones_obs.add(new Maestro("11.25","Mirador Antapaccay"));
        Ubicaciones_obs.add(new Maestro("11.26","Oficinas Mina"));
        Ubicaciones_obs.add(new Maestro("11.27","Operaciones Mina"));
        Ubicaciones_obs.add(new Maestro("11.28","Parqueo Norte"));
        Ubicaciones_obs.add(new Maestro("11.29","Parqueo Norte"));
        Ubicaciones_obs.add(new Maestro("11.30","Parqueo Nuevo"));
        Ubicaciones_obs.add(new Maestro("11.31","Parqueo Oficinas Mina"));
        Ubicaciones_obs.add(new Maestro("11.32","Parqueo Sur"));
        Ubicaciones_obs.add(new Maestro("11.33","Parqueo Transporte personal Campamento 3"));
        Ubicaciones_obs.add(new Maestro("11.34","Patio de Bombas"));
        Ubicaciones_obs.add(new Maestro("11.35","Patio de Cables"));
        Ubicaciones_obs.add(new Maestro("11.36","Planta Exsa"));
        Ubicaciones_obs.add(new Maestro("11.37","Polvorin"));
        Ubicaciones_obs.add(new Maestro("11.38","Pozas Intermedias"));
        Ubicaciones_obs.add(new Maestro("11.39","Sala de reparto de guardia Campamento 3"));
        Ubicaciones_obs.add(new Maestro("11.40","Stock Oxidos"));
        Ubicaciones_obs.add(new Maestro("11.41","Stock Sulfuros Permanente"));
        Ubicaciones_obs.add(new Maestro("11.42","Stock Sulfuros Temporal"));
        Ubicaciones_obs.add(new Maestro("11.43","Stock de Sulfuros baja ley"));
        Ubicaciones_obs.add(new Maestro("11.44","Stock de ripios"));
        Ubicaciones_obs.add(new Maestro("11.45","Super Carretera"));
        Ubicaciones_obs.add(new Maestro("11.46","Tajo Atalaya"));
        Ubicaciones_obs.add(new Maestro("11.47","Tajo Sur"));
        Ubicaciones_obs.add(new Maestro("11.48","Tajo Sur Fase 2"));
        Ubicaciones_obs.add(new Maestro("11.49","Tajo Sur Fase 3"));
        Ubicaciones_obs.add(new Maestro("11.50","Tajo Tintaya"));
        Ubicaciones_obs.add(new Maestro("11.51","Botadero 8"));
        Ubicaciones_obs.add(new Maestro("11.52","Botadero Troncal"));
        Ubicaciones_obs.add(new Maestro("11.53","Lavadero de camionetas"));
        Ubicaciones_obs.add(new Maestro("11.54","Mirador Tajo Sur"));
        Ubicaciones_obs.add(new Maestro("11.55","Tajo Norte"));
        Ubicaciones_obs.add(new Maestro("12","Procesos"));
        Ubicaciones_obs.add(new Maestro("12.01","210 - Chancado Primario"));
        Ubicaciones_obs.add(new Maestro("12.01.01","Chute de alimentacion CH-I"));
        Ubicaciones_obs.add(new Maestro("12.01.02","Romperocas"));
        Ubicaciones_obs.add(new Maestro("12.01.03","Grua pedestal Marine"));
        Ubicaciones_obs.add(new Maestro("12.01.04","Sala de Control CH-I"));
        Ubicaciones_obs.add(new Maestro("12.01.05","Sala de lubricacion chancadora"));
        Ubicaciones_obs.add(new Maestro("12.01.06","Chancadora"));
        Ubicaciones_obs.add(new Maestro("12.01.07","Camara de compensacion CH"));
        Ubicaciones_obs.add(new Maestro("12.01.08","Sistema de extraccion de polvo"));
        Ubicaciones_obs.add(new Maestro("12.01.09","Alimentador de Placas"));
        Ubicaciones_obs.add(new Maestro("12.02","220 - Transporte de Mineral Grueso"));
        Ubicaciones_obs.add(new Maestro("12.02.01","Faja CVB - 001 Correa de sacrificio"));
        Ubicaciones_obs.add(new Maestro("12.02.02","Polea cola de Faja CVB - 001"));
        Ubicaciones_obs.add(new Maestro("12.02.03","Motor Polea Motriz de Faja CVB - 001"));
        Ubicaciones_obs.add(new Maestro("12.02.04","Chute de Transferencia"));
        Ubicaciones_obs.add(new Maestro("12.02.05","Chutes de alimentacion CVB - 002"));
        Ubicaciones_obs.add(new Maestro("12.02.06","Faja CVB - 002 Correa de overland"));
        Ubicaciones_obs.add(new Maestro("12.02.07","Polea cola de Faja CVB - 002"));
        Ubicaciones_obs.add(new Maestro("12.02.08","Motores de la Faja CVB - 002"));
        Ubicaciones_obs.add(new Maestro("12.02.09","Polea de la cabeza Faja CVB - 002"));
        Ubicaciones_obs.add(new Maestro("12.02.10","Staker"));
        Ubicaciones_obs.add(new Maestro("12.02.11","Ruma de Mineral Grueso"));
        Ubicaciones_obs.add(new Maestro("12.03","240 - Recuperacion de Mineral Grueso"));
        Ubicaciones_obs.add(new Maestro("12.03.01","Alimentadores de mineral grueso(5,6,7,8)"));
        Ubicaciones_obs.add(new Maestro("12.03.02","Faja - CVB - 005  alimentacion molino SAG"));
        Ubicaciones_obs.add(new Maestro("12.03.03","Hopper de Bolas"));
        Ubicaciones_obs.add(new Maestro("12.03.04","Alimentadores de Bolas Molino SAG y Bolas"));
        Ubicaciones_obs.add(new Maestro("12.03.05","Faja - CVH - 001  Descarga de Bolas"));
        Ubicaciones_obs.add(new Maestro("12.03.06","Faja - CVB - 006  Transferencia de Bolas"));
        Ubicaciones_obs.add(new Maestro("12.03.07","Faja - CVB - 007  Alimentacion de Bolas"));
        Ubicaciones_obs.add(new Maestro("12.04","310 - Molienda SAG"));
        Ubicaciones_obs.add(new Maestro("12.04.01","Chute de Alimentacion Molino SAG"));
        Ubicaciones_obs.add(new Maestro("12.04.02","Coraza de Rotor y Estator Molino SAG"));
        Ubicaciones_obs.add(new Maestro("12.04.03","Interior del Molino SAG"));
        Ubicaciones_obs.add(new Maestro("12.04.04","Sistema de Frenos Molino SAG"));
        Ubicaciones_obs.add(new Maestro("12.04.05","Sistema de Lubricacion Molino SAG"));
        Ubicaciones_obs.add(new Maestro("12.04.06","Sistema de Refrigeracion Molino SAG"));
        Ubicaciones_obs.add(new Maestro("12.04.07","Transformadores del Molino SAG"));
        Ubicaciones_obs.add(new Maestro("12.04.08","Cicloconvertidores del Molino SAG"));
        Ubicaciones_obs.add(new Maestro("12.04.09","Trommel"));
        Ubicaciones_obs.add(new Maestro("12.04.10","Chute de Descarga"));
        Ubicaciones_obs.add(new Maestro("12.04.11","Zaranda Vibratoria"));
        Ubicaciones_obs.add(new Maestro("12.04.12","Chute Colector sult"));
        Ubicaciones_obs.add(new Maestro("12.04.13","Plataforma Externa e Interna del SAG"));
        Ubicaciones_obs.add(new Maestro("12.04.14","Cuarto de Control"));
        Ubicaciones_obs.add(new Maestro("12.04.15","Sala de Ingeniería DCS"));
        Ubicaciones_obs.add(new Maestro("12.04.16","Sala de Servidores DCS"));
        Ubicaciones_obs.add(new Maestro("12.04.17","Oficinas Control de Proceso"));
        Ubicaciones_obs.add(new Maestro("12.04.18","Oficinas de Metalurgia"));
        Ubicaciones_obs.add(new Maestro("12.04.19","Sala de servidores Control de Procesos"));
        Ubicaciones_obs.add(new Maestro("12.04.20","Otros"));
        Ubicaciones_obs.add(new Maestro("12.05","310 - 1  Molienda de Bolas"));
        Ubicaciones_obs.add(new Maestro("12.05.01","Interior del Molino de Bolas 01"));
        Ubicaciones_obs.add(new Maestro("12.05.02","Coraza de Rotor y Estator Molino Bolas 01"));
        Ubicaciones_obs.add(new Maestro("12.05.03","Transformadores del Molino Bolas 01"));
        Ubicaciones_obs.add(new Maestro("12.05.04","Cicloconvertidores del Molino Bolas 01"));
        Ubicaciones_obs.add(new Maestro("12.05.05","Sistema de Lubricacion Molino Bolas 01"));
        Ubicaciones_obs.add(new Maestro("12.05.06","Chute de Alimentacion Molino de Bolas 01"));
        Ubicaciones_obs.add(new Maestro("12.05.07","Chute de Descarga Molino de Bolas 01"));
        Ubicaciones_obs.add(new Maestro("12.05.08","Nido de Hidrociclones 01"));
        Ubicaciones_obs.add(new Maestro("12.05.09","Bomba de Alimentacion Hidrociclones 01"));
        Ubicaciones_obs.add(new Maestro("12.05.10","Sistema de Tuberias Molino de Bolas 01"));
        Ubicaciones_obs.add(new Maestro("12.05.11","Interior del Molino de Bolas 02"));
        Ubicaciones_obs.add(new Maestro("12.05.12","Coraza de Rotor y Estator Molino Bolas 02"));
        Ubicaciones_obs.add(new Maestro("12.05.13","Transformadores del Molino Bolas 02"));
        Ubicaciones_obs.add(new Maestro("12.05.14","Cicloconvertidores del Molino Bolas 02"));
        Ubicaciones_obs.add(new Maestro("12.05.15","Sistema de Lubricacion Molino Bolas 02"));
        Ubicaciones_obs.add(new Maestro("12.05.16","Chute de Alimentacion Molino de Bolas 02"));
        Ubicaciones_obs.add(new Maestro("12.05.17","Chute de Descarga Molino de Bolas 02"));
        Ubicaciones_obs.add(new Maestro("12.05.18","Nido de Hidrociclones 02"));
        Ubicaciones_obs.add(new Maestro("12.05.19","Bomba de Alimentacion Hidrociclones 02"));
        Ubicaciones_obs.add(new Maestro("12.05.20","Plataforma de Molino de Bolas"));
        Ubicaciones_obs.add(new Maestro("12.06","320 - Chancado de Pebbles"));
        Ubicaciones_obs.add(new Maestro("12.06.01","Faja - CVB - 0011  Colectora de Pebbles"));
        Ubicaciones_obs.add(new Maestro("12.06.02","Electroiman 3 Colector de Pebbles"));
        Ubicaciones_obs.add(new Maestro("12.06.03","Electroiman 4 Limpieza de Pebbles"));
        Ubicaciones_obs.add(new Maestro("12.06.04","Stock de Bolas Partidas y Desgastadas"));
        Ubicaciones_obs.add(new Maestro("12.06.05","Faja - CVB - 0011  Colectora de Pebbles"));
        Ubicaciones_obs.add(new Maestro("12.06.06","Faja - CVB - 0012  Alimentadora Stockpile Pebbles"));
        Ubicaciones_obs.add(new Maestro("12.06.07","Faja - CVB - 0013  Alimentadora de Pebbles a CH - 1"));
        Ubicaciones_obs.add(new Maestro("12.06.08","Faja - CVB - 0014  Alimentadora de Pebbles a CH - 2"));
        Ubicaciones_obs.add(new Maestro("12.06.09","Chancadora de Pebbles 01"));
        Ubicaciones_obs.add(new Maestro("12.06.10","Chancadora de Pebbles 02"));
        Ubicaciones_obs.add(new Maestro("12.06.11","Sistema de Lubricacion Chancadoras 1 y 2"));
        Ubicaciones_obs.add(new Maestro("12.06.12","Alimentadores de Pebbles Stockpile"));
        Ubicaciones_obs.add(new Maestro("12.06.13","Faja - CVB - 0015  Recirculacion de Pebbles"));
        Ubicaciones_obs.add(new Maestro("12.07","330 - Flotacion Roughert y Escavenger"));
        Ubicaciones_obs.add(new Maestro("12.07.01","Muestreadores de Pulpa"));
        Ubicaciones_obs.add(new Maestro("12.07.02","Cajon de Distribucion"));
        Ubicaciones_obs.add(new Maestro("12.07.03","Celdas de Flotacion Rougher y Scavenger N° 01  01@07"));
        Ubicaciones_obs.add(new Maestro("12.07.04","Celdas de Flotacion Rougher y Scavenger N° 02  09@15"));
        Ubicaciones_obs.add(new Maestro("12.07.05","Manhole de Descarga de FTR y FTA"));
        Ubicaciones_obs.add(new Maestro("12.07.06","Canaletas de Descarga de Relaves"));
        Ubicaciones_obs.add(new Maestro("12.07.07","Grua Torre"));
        Ubicaciones_obs.add(new Maestro("12.07.08","Losa Inferior de Celdas de Flotacion"));
        Ubicaciones_obs.add(new Maestro("12.07.09","Sala de Control Flotacion"));
        Ubicaciones_obs.add(new Maestro("12.07.10","Tanques de Dosificacion de Reactivos"));
        Ubicaciones_obs.add(new Maestro("12.07.11","Sistema de Compresion y Adicion de Aire"));
        Ubicaciones_obs.add(new Maestro("12.08","330 - 1 Remolienda Scavenger"));
        Ubicaciones_obs.add(new Maestro("12.08.01","Bomba de Alimentacion de Ciclones"));
        Ubicaciones_obs.add(new Maestro("12.08.02","Bateria de Ciclones"));
        Ubicaciones_obs.add(new Maestro("12.08.03","Bomba de Alimentacion del Molino de Remolienda"));
        Ubicaciones_obs.add(new Maestro("12.08.04","Bomba de Alimentacion Agua Fresca  Molino de Remolienda"));
        Ubicaciones_obs.add(new Maestro("12.08.05","Molino de Remolienda"));
        Ubicaciones_obs.add(new Maestro("12.08.06","Bombas de Producto de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.08.07","Cajones de Recirculacion"));
        Ubicaciones_obs.add(new Maestro("12.09","330 - 2 Remolienda Roughert"));
        Ubicaciones_obs.add(new Maestro("12.09.01","Bomba de Alimentacion de Ciclones"));
        Ubicaciones_obs.add(new Maestro("12.09.02","Bateria de Ciclones"));
        Ubicaciones_obs.add(new Maestro("12.09.03","Bomba de Alimentacion del Molino de Remolienda"));
        Ubicaciones_obs.add(new Maestro("12.09.04","Bomba de Alimentacion Agua Fresca  Molino de Remolienda"));
        Ubicaciones_obs.add(new Maestro("12.09.05","Molino de Remolienda"));
        Ubicaciones_obs.add(new Maestro("12.09.06","Bombas de Producto de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.09.07","Cajones de Recirculacion"));
        Ubicaciones_obs.add(new Maestro("12.10","330 - 3 Flotacion Cleaner"));
        Ubicaciones_obs.add(new Maestro("12.10.01","Celdas de Flotacion FTA 001@0012"));
        Ubicaciones_obs.add(new Maestro("12.10.02","Celdas de Flotacion FTA 0014@0026"));
        Ubicaciones_obs.add(new Maestro("12.11","340 - Espesamiento de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.11.01","Espesador de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.11.02","Dardo Cajon de Alimentacion de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.11.03","Pasarela de Acceso a Motor Rastras"));
        Ubicaciones_obs.add(new Maestro("12.11.04","Motor Rastras del Espesador"));
        Ubicaciones_obs.add(new Maestro("12.11.05","Valvulas Compuerta de Espesador"));
        Ubicaciones_obs.add(new Maestro("12.11.06","Tunel Ingreso Nivel Inferior del Espesador"));
        Ubicaciones_obs.add(new Maestro("12.11.07","Bomba de Rebose Espesador de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.11.08","Tanque de Rebose Espesador de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.11.09","Bomba de Descarga Espesador de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.12","350 - 420 Filtrado"));
        Ubicaciones_obs.add(new Maestro("12.12.01","Piscina de Contingencia Almacenamiento de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.12.02","Estanque de Almacenamiento de Concentrado N° 01"));
        Ubicaciones_obs.add(new Maestro("12.12.03","Estanque de Almacenamiento de Concentrado N° 02"));
        Ubicaciones_obs.add(new Maestro("12.12.04","Bomba de Alimentacion de Concentrado al Filtro"));
        Ubicaciones_obs.add(new Maestro("12.12.05","Filtro"));
        Ubicaciones_obs.add(new Maestro("12.12.06","Sistema de Lubricacion de Filtro"));
        Ubicaciones_obs.add(new Maestro("12.12.07","Sistema de Compresion y Adicion de Aire"));
        Ubicaciones_obs.add(new Maestro("12.12.08","Loza de Filtrado"));
        Ubicaciones_obs.add(new Maestro("12.13","420 - 430 Acopio de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.13.01","Faja - CVB - 0025  Descarga Torta Filtro de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.13.02","Faja - CVB - 0031  Alimentacion Pila de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.13.03","Zona Acceso a Ruma de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.13.04","Zona de Ruma de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.13.05","Zona Muestreo de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.14","510 - 520 Espesamiento de Relaves"));
        Ubicaciones_obs.add(new Maestro("12.14.01","Espesador de Relaves N° 01 y 02 - 60 y 61"));
        Ubicaciones_obs.add(new Maestro("12.14.02","Motores de Giro y Levante de Rastra"));
        Ubicaciones_obs.add(new Maestro("12.14.03","Valvulas de Descarga de Relaves"));
        Ubicaciones_obs.add(new Maestro("12.14.04","Valvulas de Recirculacion de Relaves"));
        Ubicaciones_obs.add(new Maestro("12.14.05","Tunel Nivel Inferior Espesador"));
        Ubicaciones_obs.add(new Maestro("12.14.06","Tuberias del Sistema de Aguas"));
        Ubicaciones_obs.add(new Maestro("12.14.07","Valvulas Dardo Alimentador de Relaves"));
        Ubicaciones_obs.add(new Maestro("12.14.08","Planta de Fluculante"));
        Ubicaciones_obs.add(new Maestro("12.14.09","Lineas de Descarga de Relaves"));
        Ubicaciones_obs.add(new Maestro("12.15","550 - Recuperacion de Agua"));
        Ubicaciones_obs.add(new Maestro("12.15.01","Bombas de Recuperacion de Agua de Proceso"));
        Ubicaciones_obs.add(new Maestro("12.15.02","Centina de Agua de Proceso"));
        Ubicaciones_obs.add(new Maestro("12.16","2130 - Agua Fresca y Agua de Rio Salado"));
        Ubicaciones_obs.add(new Maestro("12.16.01","Estacion de Bombeo Rio Salado"));
        Ubicaciones_obs.add(new Maestro("12.16.02","Cuarto de Control"));
        Ubicaciones_obs.add(new Maestro("12.16.03","Tuberia del Sistema de Bombeo"));
        Ubicaciones_obs.add(new Maestro("12.16.04","Relavera (Pit Tintaya)"));
        Ubicaciones_obs.add(new Maestro("12.17","391 - Planta de Cal"));
        Ubicaciones_obs.add(new Maestro("12.17.01","Zona de Descarga de Cal Hidratada"));
        Ubicaciones_obs.add(new Maestro("12.17.02","Tanque de Preparacion de Cal"));
        Ubicaciones_obs.add(new Maestro("12.17.03","Faja de Alimentacion de Cal"));
        Ubicaciones_obs.add(new Maestro("12.17.04","Interior Tanque de Almacenamiento"));
        Ubicaciones_obs.add(new Maestro("12.18","Planta de Reactivos"));
        Ubicaciones_obs.add(new Maestro("12.18.01","Tanques de Preparacion de Reactivos"));
        Ubicaciones_obs.add(new Maestro("12.18.02","Zona de Almacenamiento de Reactivos"));
        Ubicaciones_obs.add(new Maestro("12.18.03","Loza  Externa de Planta de Reactivos"));
        Ubicaciones_obs.add(new Maestro("12.19","Salas Electricas"));
        Ubicaciones_obs.add(new Maestro("12.19.01","Sala Electrica 110-ERR-0001"));
        Ubicaciones_obs.add(new Maestro("12.19.02","Sala Electrica 130-ERR-0001"));
        Ubicaciones_obs.add(new Maestro("12.19.03","Sala Electrica 210-ERR-0001"));
        Ubicaciones_obs.add(new Maestro("12.19.04","Sala Electrica 220-ERR-0001"));
        Ubicaciones_obs.add(new Maestro("12.19.05","Sala Electrica 310-ERR-0001"));
        Ubicaciones_obs.add(new Maestro("12.19.06","Sala Electrica 310-ERR-0002"));
        Ubicaciones_obs.add(new Maestro("12.19.07","Sala Electrica 310-ERR-0003"));
        Ubicaciones_obs.add(new Maestro("12.19.08","Sala Electrica 0320-ERR-0001"));
        Ubicaciones_obs.add(new Maestro("12.19.09","Sala Electrica 0330-ERR-0001"));
        Ubicaciones_obs.add(new Maestro("12.19.10","Sala Electrica 340-ERR-001"));
        Ubicaciones_obs.add(new Maestro("12.19.11","Sala Electrica 0391-ERR-001"));
        Ubicaciones_obs.add(new Maestro("12.19.12","Sala Electrica 420-ERR-0001"));
        Ubicaciones_obs.add(new Maestro("12.19.13","Sala Electrica 510-ERR-0001"));
        Ubicaciones_obs.add(new Maestro("12.19.14","Sala Electrica 0550-ERR-001"));
        Ubicaciones_obs.add(new Maestro("12.19.15","Sala Electrica 0931-ERR-0001"));
        Ubicaciones_obs.add(new Maestro("12.19.16","Sala Electrica 931-ERR-0002"));
        Ubicaciones_obs.add(new Maestro("12.19.17","Sala Electrica 2130-ERR-001"));
        Ubicaciones_obs.add(new Maestro("12.19.18","Sala Electrica 210-JRL-0003"));
        Ubicaciones_obs.add(new Maestro("12.19.19","Sub Estaciones Moviles"));
        Ubicaciones_obs.add(new Maestro("12.20","Talleres"));
        Ubicaciones_obs.add(new Maestro("12.20.01","Taller de Mantenimiento 01"));
        Ubicaciones_obs.add(new Maestro("12.20.02","Taller de Mantenimiento Chancado"));
        Ubicaciones_obs.add(new Maestro("12.20.03","Taller de Mantenimiento Molienda"));
        Ubicaciones_obs.add(new Maestro("12.20.04","Taller de mantenimiento Pebbles"));
        Ubicaciones_obs.add(new Maestro("12.20.05","Taller de Mantenimiento Flotacion"));
        Ubicaciones_obs.add(new Maestro("12.20.06","Taller de Mantenimiento Filtrado"));
        Ubicaciones_obs.add(new Maestro("12.20.07","Area 1700 contratistas"));
        Ubicaciones_obs.add(new Maestro("12.20.08","Taller de Mantenimiento Aguas y Relaves"));
        Ubicaciones_obs.add(new Maestro("12.20.09","Taller de Mantenimiento Tintaya"));
        Ubicaciones_obs.add(new Maestro("12.21","Main Office"));
        Ubicaciones_obs.add(new Maestro("12.21.01","Oficina"));
        Ubicaciones_obs.add(new Maestro("12.21.02","Modulo"));
        Ubicaciones_obs.add(new Maestro("12.21.03","Sala de Reuniones"));
        Ubicaciones_obs.add(new Maestro("12.21.04","Pasillos"));
        Ubicaciones_obs.add(new Maestro("12.21.05","Zona de Parqueo"));
        Ubicaciones_obs.add(new Maestro("12.22","Vias y Parqueos"));
        Ubicaciones_obs.add(new Maestro("12.22.01","Chancado"));
        Ubicaciones_obs.add(new Maestro("12.22.02","Overland Faja CVB - 002"));
        Ubicaciones_obs.add(new Maestro("12.22.03","Pila de Mineral Grueso"));
        Ubicaciones_obs.add(new Maestro("12.22.04","Alimentadores de Mineral Grueso y Bolas"));
        Ubicaciones_obs.add(new Maestro("12.22.05","Planta de Pebbles"));
        Ubicaciones_obs.add(new Maestro("12.22.06","Molienda"));
        Ubicaciones_obs.add(new Maestro("12.22.07","Flotacion"));
        Ubicaciones_obs.add(new Maestro("12.22.08","Remolienda"));
        Ubicaciones_obs.add(new Maestro("12.22.09","Filtrado"));
        Ubicaciones_obs.add(new Maestro("12.22.10","Acopio de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.22.11","Planta de Reactivos"));
        Ubicaciones_obs.add(new Maestro("12.22.12","Planta de Cal"));
        Ubicaciones_obs.add(new Maestro("12.22.13","Espesador de Relaves"));
        Ubicaciones_obs.add(new Maestro("12.22.14","Estacion de Bombeo Rio Salado"));
        Ubicaciones_obs.add(new Maestro("12.22.15","Patio de Almacenamiento"));
        Ubicaciones_obs.add(new Maestro("12.23","Pozas de Contingencia Agua de Proceso"));
        Ubicaciones_obs.add(new Maestro("12.23.01","Poza Chancado"));
        Ubicaciones_obs.add(new Maestro("12.23.02","Poza Molienda"));
        Ubicaciones_obs.add(new Maestro("12.23.03","Poza de Flotacion"));
        Ubicaciones_obs.add(new Maestro("12.23.04","Poza de Espezamiento de Concentrado"));
        Ubicaciones_obs.add(new Maestro("12.23.05","Poza Cielo"));
        Ubicaciones_obs.add(new Maestro("12.23.06","Poza Estacion Relevadora"));
        Ubicaciones_obs.add(new Maestro("12.24","Laboratorio"));
        Ubicaciones_obs.add(new Maestro("12.24.01","Laboratorio Quimico"));
        Ubicaciones_obs.add(new Maestro("12.24.02","Laboratorio Metalurgico"));
        Ubicaciones_obs.add(new Maestro("12.25","Sala Satelital"));
        Ubicaciones_obs.add(new Maestro("13","Administracion y Servicios"));
        Ubicaciones_obs.add(new Maestro("13.01","Campamento 2"));
        Ubicaciones_obs.add(new Maestro("13.01.01","Oficinas Administrativas"));
        Ubicaciones_obs.add(new Maestro("13.01.02","Casa de Huespedes"));
        Ubicaciones_obs.add(new Maestro("13.01.03","Tambo Machay"));
        Ubicaciones_obs.add(new Maestro("13.01.04","Comedor Tambomachay"));
        Ubicaciones_obs.add(new Maestro("13.01.05","Parqueo oficinas administrativas"));
        Ubicaciones_obs.add(new Maestro("13.01.06","Casas - campamento 2"));
        Ubicaciones_obs.add(new Maestro("13.01.07","Parqueo Tambomachay"));
        Ubicaciones_obs.add(new Maestro("13.02","Campamento 3"));
        Ubicaciones_obs.add(new Maestro("13.02.01","Parqueo de Buses"));
        Ubicaciones_obs.add(new Maestro("13.02.02","Oficinas Motorpool"));
        Ubicaciones_obs.add(new Maestro("13.02.03","Lavanderia"));
        Ubicaciones_obs.add(new Maestro("13.02.04","Mercantil"));
        Ubicaciones_obs.add(new Maestro("13.02.05","Zona Comercial"));
        Ubicaciones_obs.add(new Maestro("13.02.06","Cine Cusco"));
        Ubicaciones_obs.add(new Maestro("13.02.07","Comedor Mijunahuasi"));
        Ubicaciones_obs.add(new Maestro("13.02.08","Gimnasio"));
        Ubicaciones_obs.add(new Maestro("13.02.09","Edificios Sector 1"));
        Ubicaciones_obs.add(new Maestro("13.02.10","Edificios Sector 2"));
        Ubicaciones_obs.add(new Maestro("13.02.11","Ex Colegio"));
        Ubicaciones_obs.add(new Maestro("13.02.12","Taller de Edificaciones"));
        Ubicaciones_obs.add(new Maestro("13.02.13","Parqueo EMSA"));
        Ubicaciones_obs.add(new Maestro("13.02.14","Cancha de Futbol"));
        Ubicaciones_obs.add(new Maestro("13.02.15","Quincho 3"));
        Ubicaciones_obs.add(new Maestro("13.02.16","Parqueo RRHH"));
        Ubicaciones_obs.add(new Maestro("13.02.17","Terminal terrestre"));
        Ubicaciones_obs.add(new Maestro("13.03","Taller de Unidades Livianas (TAIR)"));
        Ubicaciones_obs.add(new Maestro("13.04","Tráfico"));
        Ubicaciones_obs.add(new Maestro("13.04.01","Ruta Arequipa - Matarani"));
        Ubicaciones_obs.add(new Maestro("13.04.02","Ruta Imata - Arequipa"));
        Ubicaciones_obs.add(new Maestro("13.04.03","Ruta Tintaya - Imata"));
        Ubicaciones_obs.add(new Maestro("13.04.04","Vía Interna Tintaya - Antapaccay"));
        Ubicaciones_obs.add(new Maestro("13.04.05","Rutas internas Antapaccay"));
        Ubicaciones_obs.add(new Maestro("13.05","Zona despacho concentrado Antapaccay"));
        Ubicaciones_obs.add(new Maestro("13.05.01","Oficinas de balanza Antapaccay"));
        Ubicaciones_obs.add(new Maestro("13.05.02","Playa de estacionamiento de camiones"));
        Ubicaciones_obs.add(new Maestro("13.06","Grifo Casa de Fuerza"));
        Ubicaciones_obs.add(new Maestro("13.06.01","Tanques de Almacenamiento"));
        Ubicaciones_obs.add(new Maestro("13.06.02","Area de recepcion de combustible"));
        Ubicaciones_obs.add(new Maestro("13.06.03","Sala de control"));
        Ubicaciones_obs.add(new Maestro("13.06.04","Isla de despacho de Camionetas"));
        Ubicaciones_obs.add(new Maestro("13.07","Grifo Antapaccay"));
        Ubicaciones_obs.add(new Maestro("13.07.01","Tanques de Almacenamiento"));
        Ubicaciones_obs.add(new Maestro("13.07.02","Area de recepcion de combustible"));
        Ubicaciones_obs.add(new Maestro("13.07.03","Sala de control"));
        Ubicaciones_obs.add(new Maestro("13.07.04","Isla de despacho de Camionetas"));
        Ubicaciones_obs.add(new Maestro("13.07.05","Isla de despacho para Equipo Pesado"));
        Ubicaciones_obs.add(new Maestro("13.08","Grifo Principal"));
        Ubicaciones_obs.add(new Maestro("13.08.01","Tanques de Almacenamiento"));
        Ubicaciones_obs.add(new Maestro("13.08.02","Area de recepcion de combustible"));
        Ubicaciones_obs.add(new Maestro("13.08.03","Sala de control"));
        Ubicaciones_obs.add(new Maestro("13.08.04","Isla de despacho de Camionetas"));
        Ubicaciones_obs.add(new Maestro("13.09","Estacion de Lubricantes"));
        Ubicaciones_obs.add(new Maestro("13.09.01","Tanques de Almacenamiento"));
        Ubicaciones_obs.add(new Maestro("13.09.02","Sala de control"));
        Ubicaciones_obs.add(new Maestro("13.09.03","Area de recepcion de combustible"));
        Ubicaciones_obs.add(new Maestro("13.10","Almacen Central"));
        Ubicaciones_obs.add(new Maestro("13.10.01","Oficinas de Almacen"));
        Ubicaciones_obs.add(new Maestro("13.10.02","Nave principal"));
        Ubicaciones_obs.add(new Maestro("13.10.03","Almacen de Gases Comprimidos"));
        Ubicaciones_obs.add(new Maestro("13.11","Almacen Patio Nro. 1"));
        Ubicaciones_obs.add(new Maestro("13.11.01","Area de Almacenamiento"));
        Ubicaciones_obs.add(new Maestro("13.12","Almacen Patio Nro. 2"));
        Ubicaciones_obs.add(new Maestro("13.12.01","Area de Almacenamiento"));
        Ubicaciones_obs.add(new Maestro("13.13","Almacen Patio Nro. 3"));
        Ubicaciones_obs.add(new Maestro("13.13.01","Area de Almacenamiento"));
        Ubicaciones_obs.add(new Maestro("13.14","Almacen Patio Nro. 12"));
        Ubicaciones_obs.add(new Maestro("13.14.01","Area de Almacenamiento Procesos"));
        Ubicaciones_obs.add(new Maestro("13.15","Almacen Patio Nro. 13"));
        Ubicaciones_obs.add(new Maestro("13.15.01","Area de Almacenamiento Truck Shop"));
        Ubicaciones_obs.add(new Maestro("13.15.02","Oficina despachos Almacen Truck Shop"));
        Ubicaciones_obs.add(new Maestro("13.15.03","Oficina Ferreyros"));
        Ubicaciones_obs.add(new Maestro("13.15.04","Oficina Komatsu"));
        Ubicaciones_obs.add(new Maestro("13.15.05","Almacen de Gases Comprimidos"));
        Ubicaciones_obs.add(new Maestro("13.16","Almacen Obsoletos"));
        Ubicaciones_obs.add(new Maestro("13.16.01","Area de Almacenamiento repuestos Ferreyros"));
        Ubicaciones_obs.add(new Maestro("13.17","Almacen Neumaticos Gigantes"));
        Ubicaciones_obs.add(new Maestro("13.17.01","Area de Almacenamiento de llantas Antapaccay"));
        Ubicaciones_obs.add(new Maestro("13.18","Planta de compostaje"));
        Ubicaciones_obs.add(new Maestro("13.18.01","Planta de compostaje"));
        Ubicaciones_obs.add(new Maestro("13.19","Zona Despacho Oxidos"));
        Ubicaciones_obs.add(new Maestro("13.19.01","Oficinas de Balanza Oxidos"));
        Ubicaciones_obs.add(new Maestro("13.19.02","Playa de Estacionamiento Camiones Oxidos"));
        Ubicaciones_obs.add(new Maestro("13.20","CREE - Espinar"));
        Ubicaciones_obs.add(new Maestro("13.21","Campamento EMSA"));
        Ubicaciones_obs.add(new Maestro("13.22","Fundación Tintaya"));
        Ubicaciones_obs.add(new Maestro("13.23","Matarani"));
        Ubicaciones_obs.add(new Maestro("13.23.01","Playa de camiones"));
        Ubicaciones_obs.add(new Maestro("13.23.02","Oficinas administrativas"));
        Ubicaciones_obs.add(new Maestro("13.23.03","Laboratorio"));
        Ubicaciones_obs.add(new Maestro("13.23.04","Lavadero camiones"));
        Ubicaciones_obs.add(new Maestro("13.23.05","Instalaciones anexas"));
        Ubicaciones_obs.add(new Maestro("13.24","Almacen Ferreyros"));
        Ubicaciones_obs.add(new Maestro("13.25","Aerodromo Espinar"));
        Ubicaciones_obs.add(new Maestro("14","Procesos Tintaya"));
        Ubicaciones_obs.add(new Maestro("14.01","Chancado Primario"));
        Ubicaciones_obs.add(new Maestro("14.01.01","Hopper de CH-I"));
        Ubicaciones_obs.add(new Maestro("14.01.02","Edificio de CH-I"));
        Ubicaciones_obs.add(new Maestro("14.01.03","Chancadora Primaria"));
        Ubicaciones_obs.add(new Maestro("14.01.04","Fajas Transportadoras"));
        Ubicaciones_obs.add(new Maestro("14.01.05","Ruma de Gruesos"));
        Ubicaciones_obs.add(new Maestro("14.01.06","Sala Eléctrica Chancado primario"));
        Ubicaciones_obs.add(new Maestro("14.02","Chancado Secundario"));
        Ubicaciones_obs.add(new Maestro("14.02.01","Chancadoras II y III"));
        Ubicaciones_obs.add(new Maestro("14.02.02","Edificio de CH-II"));
        Ubicaciones_obs.add(new Maestro("14.02.03","Torre de Transferencia"));
        Ubicaciones_obs.add(new Maestro("14.02.04","Taller mecánico"));
        Ubicaciones_obs.add(new Maestro("14.02.05","Fajas Transportadoras"));
        Ubicaciones_obs.add(new Maestro("14.02.06","Sala Eléctrica Chancado Secundario"));
        Ubicaciones_obs.add(new Maestro("14.03","Molienda"));
        Ubicaciones_obs.add(new Maestro("14.03.01","Ruma de Finos"));
        Ubicaciones_obs.add(new Maestro("14.03.02","Molino #1"));
        Ubicaciones_obs.add(new Maestro("14.03.03","Molino #2"));
        Ubicaciones_obs.add(new Maestro("14.03.04","Molino #3"));
        Ubicaciones_obs.add(new Maestro("14.03.05","Sala Eléctrica Molino 3"));
        Ubicaciones_obs.add(new Maestro("14.03.06","Patio para disposición de bolas"));
        Ubicaciones_obs.add(new Maestro("14.03.07","Taller Mantenimiento Mecánico"));
        Ubicaciones_obs.add(new Maestro("14.03.08","Sala Eléctrica 2do Piso"));
        Ubicaciones_obs.add(new Maestro("14.03.09","Sala Eléctrica 1er Piso"));
        Ubicaciones_obs.add(new Maestro("14.03.10","Sala DCS Molienda"));
        Ubicaciones_obs.add(new Maestro("14.03.11","Sala DCS Cuarto de Control"));
        Ubicaciones_obs.add(new Maestro("14.04","Flotación"));
        Ubicaciones_obs.add(new Maestro("14.04.01","Celdas Rougher"));
        Ubicaciones_obs.add(new Maestro("14.04.02","Celdas Scavenger"));
        Ubicaciones_obs.add(new Maestro("14.04.03","Celdas Cleaner"));
        Ubicaciones_obs.add(new Maestro("14.04.04","Celdas Columna"));
        Ubicaciones_obs.add(new Maestro("14.05","Planta de Cal"));
        Ubicaciones_obs.add(new Maestro("14.05.01","Tks de preparación de lechada cal"));
        Ubicaciones_obs.add(new Maestro("14.05.02","Almacen de Cal"));
        Ubicaciones_obs.add(new Maestro("14.06","Almacen de Concentrado"));
        Ubicaciones_obs.add(new Maestro("14.06.01","Espesador de Cu"));
        Ubicaciones_obs.add(new Maestro("14.06.02","Cocha L"));
        Ubicaciones_obs.add(new Maestro("14.06.03","Almacen de Concentrado"));
        Ubicaciones_obs.add(new Maestro("14.07","Filtrado"));
        Ubicaciones_obs.add(new Maestro("14.07.01","Filtros #1, #2, #3, #4"));
        Ubicaciones_obs.add(new Maestro("14.07.02","Sala de Control Filtros"));
        Ubicaciones_obs.add(new Maestro("14.08","Metalurgia"));
        Ubicaciones_obs.add(new Maestro("14.08.01","Almacén de Muestras"));
        Ubicaciones_obs.add(new Maestro("14.08.02","Comedor de Metalurgia"));
        Ubicaciones_obs.add(new Maestro("14.08.03","Lab. Microscopia"));
        Ubicaciones_obs.add(new Maestro("14.08.04","Lab. Molienda y Flotación"));
        Ubicaciones_obs.add(new Maestro("14.08.05","Preparación de muestras"));
        Ubicaciones_obs.add(new Maestro("14.08.06","Laboratorio Químico"));
        Ubicaciones_obs.add(new Maestro("14.09","Taller Eléctrico"));
        Ubicaciones_obs.add(new Maestro("14.09.01","Taller Eléctrico de Motores"));
        Ubicaciones_obs.add(new Maestro("14.09.02","Taller de Instrumentación"));
        Ubicaciones_obs.add(new Maestro("14.10","Oficinas Administrativas"));
        Ubicaciones_obs.add(new Maestro("14.10.01","Oficinas 2do, 3er piso"));
        Ubicaciones_obs.add(new Maestro("14.11","Espesadores de relaves"));
        Ubicaciones_obs.add(new Maestro("14.11.01","Espesador de relaves #01"));
        Ubicaciones_obs.add(new Maestro("14.11.02","Espesador de relaves #02"));
        Ubicaciones_obs.add(new Maestro("14.11.03","Túnel espesador de relaves"));
        Ubicaciones_obs.add(new Maestro("14.12","Área 100"));
        Ubicaciones_obs.add(new Maestro("14.12.01","Tren de Bombas"));
        Ubicaciones_obs.add(new Maestro("14.12.02","Bombas de sello"));
        Ubicaciones_obs.add(new Maestro("14.12.03","Tanque 01 relave"));
        Ubicaciones_obs.add(new Maestro("14.12.04","Sala Eléctrica Área 100"));
        Ubicaciones_obs.add(new Maestro("14.12.05","Sala Eléctrica Casa Bomba"));
        Ubicaciones_obs.add(new Maestro("14.13","Casa de Bombas Rio Salado"));
        Ubicaciones_obs.add(new Maestro("14.13.01","Casa de Bombas"));
        Ubicaciones_obs.add(new Maestro("14.13.02","Sala Eléctrica Rio Salado"));
        Ubicaciones_obs.add(new Maestro("14.14","Línea de Relave"));
        Ubicaciones_obs.add(new Maestro("14.14.01","Línea de Relave"));
        Ubicaciones_obs.add(new Maestro("14.15","Estación de Bombeo de Agua"));
        Ubicaciones_obs.add(new Maestro("14.15.01","Tanque rebombeo - Pit Tintaya"));
        Ubicaciones_obs.add(new Maestro("14.15.02","Sistema de rebombeo de agua - Pit Tintaya"));
        Ubicaciones_obs.add(new Maestro("14.15.03","Línea de rebombeo de agua"));
        Ubicaciones_obs.add(new Maestro("14.15.04","Sala Eléctrica Rebombeo de Agua"));
        Ubicaciones_obs.add(new Maestro("14.16","Planta de Floculante"));
        Ubicaciones_obs.add(new Maestro("14.16.01","Planta de Floculante"));
        Ubicaciones_obs.add(new Maestro("14.17","Poza de Contingencia"));
        Ubicaciones_obs.add(new Maestro("14.17.01","Poza de Contingencia"));
        Ubicaciones_obs.add(new Maestro("14.18","Sistema de Enfriamiento"));
        Ubicaciones_obs.add(new Maestro("14.18.01","Patio de Sistema de Enfriamiento"));
        Ubicaciones_obs.add(new Maestro("14.19","Compresores"));
        Ubicaciones_obs.add(new Maestro("14.19.01","Sala de Compresores"));
        Ubicaciones_obs.add(new Maestro("14.20","Agua de Proceso"));
        Ubicaciones_obs.add(new Maestro("14.20.01","Sub Estación Eléctrica 1"));
        Ubicaciones_obs.add(new Maestro("14.20.02","Sub Estación Eléctrica 2"));
        Ubicaciones_obs.add(new Maestro("14.20.03","Sub Estación Eléctrica 3"));
        Ubicaciones_obs.add(new Maestro("14.21","Sub estación"));
        Ubicaciones_obs.add(new Maestro("14.21.01","Bahías 8 y 9 "));
        Ubicaciones_obs.add(new Maestro("15","Vías y Accesos"));
        Ubicaciones_obs.add(new Maestro("15.01","Vía Antapaccay – Tintaya"));
        Ubicaciones_obs.add(new Maestro("15.01.01","Vía Mina – Truck Shop"));
        Ubicaciones_obs.add(new Maestro("15.01.02","Via Truck Shop- Planta Antapaccay"));
        Ubicaciones_obs.add(new Maestro("15.01.03","Vía Planta Antapaccay – Tajo Tintaya"));
        Ubicaciones_obs.add(new Maestro("15.01.04","Rampa óxidos- Hospital"));
        Ubicaciones_obs.add(new Maestro("15.02","Vía hacia Planta Tintaya"));
        Ubicaciones_obs.add(new Maestro("15.02.01","Vía las brujas"));
        Ubicaciones_obs.add(new Maestro("15.02.02","Vía Seguroc – Planta Tintaya"));
        Ubicaciones_obs.add(new Maestro("15.02.03","Vía Planta Tintaya – Almacén Central"));
        Ubicaciones_obs.add(new Maestro("15.02.04","Acceso rampa 25"));
        Ubicaciones_obs.add(new Maestro("15.02.05","Cruce Rampa 26"));
        Ubicaciones_obs.add(new Maestro("15.03","Vía San Martin"));
        Ubicaciones_obs.add(new Maestro("15.03.01","Cruce Ex Bahía Marquiri"));
        Ubicaciones_obs.add(new Maestro("15.03.02","Puente Antapaccay"));
        Ubicaciones_obs.add(new Maestro("15.03.03","Rampa 26"));
        Ubicaciones_obs.add(new Maestro("15.04","Nueva sub estación Tintaya"));
        Ubicaciones_obs.add(new Maestro("15.04.01","Bahías 8 y 9"));
        Ubicaciones_obs.add(new Maestro("16","Carretera Nacional"));
        Ubicaciones_obs.add(new Maestro("16.01"," Carretera  Antapaccay – Condoroma"));
        Ubicaciones_obs.add(new Maestro("16.02","Carretera Condoroma - Imata"));
        Ubicaciones_obs.add(new Maestro("16.03","Carretera  Imata – Arequipa"));
        Ubicaciones_obs.add(new Maestro("16.04","Carretera Arequipa – Matarani"));
        Ubicaciones_obs.add(new Maestro("16.05","Carretera Tintaya - Sicuani"));
        Ubicaciones_obs.add(new Maestro("16.06","Carretera Sicuani - Cusco"));
        Ubicaciones_obs.add(new Maestro("16.07","Carretera Tintaya - Espinar"));
        Ubicaciones_obs.add(new Maestro("17","Planta de Óxidos"));
        Ubicaciones_obs.add(new Maestro("17.01","Área Seca"));
        Ubicaciones_obs.add(new Maestro("17.01.01","Chancado Primario"));
        Ubicaciones_obs.add(new Maestro("17.01.02","Chancado Secundario"));
        Ubicaciones_obs.add(new Maestro("17.01.03","Tambor de curado"));
        Ubicaciones_obs.add(new Maestro("17.01.04","Pad de Lixiviación"));
        Ubicaciones_obs.add(new Maestro("17.02","Área Húmeda"));
        Ubicaciones_obs.add(new Maestro("17.02.01","Lixiviación por agitación"));
        Ubicaciones_obs.add(new Maestro("17.02.02","Tank Farm"));
        Ubicaciones_obs.add(new Maestro("17.02.03","Nave de SX"));
        Ubicaciones_obs.add(new Maestro("17.02.04","Nave de EW"));
        Ubicaciones_obs.add(new Maestro("17.02.05","Pozas"));
        Ubicaciones_obs.add(new Maestro("17.03","Taller Mecánico"));
        Ubicaciones_obs.add(new Maestro("17.04","Taller Eléctrico"));
        Ubicaciones_obs.add(new Maestro("17.05","Patio de Soldadura"));
        Ubicaciones_obs.add(new Maestro("17.06","Control Room"));
    }


}
