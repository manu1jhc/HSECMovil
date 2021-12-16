package com.pango.hsec.hsec.adapter;

        import android.app.Activity;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.PopupWindow;
        import android.widget.TextView;

        import com.pango.hsec.hsec.GlobalVariables;
        import com.pango.hsec.hsec.R;
        import com.pango.hsec.hsec.model.CapCursoMinModel;

        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.concurrent.TimeUnit;

/**
 * Created by Andre on 12/03/2018.
 */

public class CapCursosAdapter extends ArrayAdapter<CapCursoMinModel> {

    private Activity context;
    private ArrayList<CapCursoMinModel> data = new ArrayList<CapCursoMinModel>();
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
   // FragmentPlanPendiente ActContent;
    View popupView;
    public PopupWindow popupWindow;

    public CapCursosAdapter( Activity context, ArrayList<CapCursoMinModel> data) {
        super(context, R.layout.item_curso, data);
        this.data = data;
        this.context = context;
       // this.ActContent=ActContent;
    }
   /* public void replace(PlanMinModel replacedata){
        data.get(indexOf(replacedata.CodAccion)).CodNivelRiesgo=replacedata.CodNivelRiesgo;
        data.get(indexOf(replacedata.CodAccion)).DesPlanAccion=replacedata.DesPlanAccion;
        data.set(indexOf(replacedata.CodAccion),replacedata);
        notifyDataSetChanged();
    }

    public void remove(int index){
        data.remove(index);
        notifyDataSetChanged();
    }

    public int indexOf(String value){
        for (int i=0;i<data.size();i++  ) {
            if(data.get(i).CodAccion.equals(value)) return i;
        }
        return 0;
    }*/
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        //ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View rowView = inflater.inflate(R.layout.item_curso, null, true);

        TextView Tema = rowView.findViewById(R.id.txt_tema);
        TextView fecha = rowView.findViewById(R.id.txt_fecha);
        ImageView recurrence=rowView.findViewById(R.id.im_recurrence);
        TextView Duracion = rowView.findViewById(R.id.txt_duracion);
        TextView Empresa = rowView.findViewById(R.id.txt_empresa);
        TextView Tipo = rowView.findViewById(R.id.txt_tipo);

        Tema.setText(GlobalVariables.getDescripcion(GlobalVariables.C_Tema,data.get(position).CodTema));
        Empresa.setText(GlobalVariables.getDescripcion(GlobalVariables.C_Empresa,data.get(position).Empresa));
        Tipo.setText(GlobalVariables.getDescripcion(GlobalVariables.C_Tipo,data.get(position).Tipo));

        fecha.setText(Obtenerfecha(data.get(position).Fecha));
        Duracion.setText(TimeDiffM(data.get(position).Duracion));
        if(data.get(position).Recurrence.equals("1"))
            recurrence.setVisibility(View.VISIBLE);

        return rowView;
    }
    public String TimeDiffM(int timediff){
        String diferencia="",dias="",horas="",min="";
        int minutes=timediff;
        int day = (int) TimeUnit.MINUTES.toDays(minutes);
        long hours = TimeUnit.MINUTES.toHours(minutes)- (day *24);
        long minute = TimeUnit.MINUTES.toMinutes(minutes)-(TimeUnit.MINUTES.toHours(minutes)* 60);
        if(day>0){
            dias=day +" Dias";
        }
        else if(hours>0){
            horas=hours +" Hora(s)";
        }
        else if(minute>0){
            min=minute +" Minutos";
        }

        diferencia=dias+horas+min;
        return diferencia;
    }

    public String Obtenerfecha(String tempcom_fecha) {

        String fecha="";
        try {
            fecha=formatoRender.format(formatoInicial.parse(tempcom_fecha));
        } catch (ParseException e) {
            e.printStackTrace();
            fecha=tempcom_fecha;
        }

        return fecha;

    }

}
