package com.pango.hsec.hsec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.Maestro;

import java.util.List;

//public class IconAdapter {
//}




public class IconAdapter extends ArrayAdapter<Maestro>
{
    private Context context;

    List<Maestro> datos = null;

    public IconAdapter(Context context, List<Maestro> datos)
    {
        //se debe indicar el layout para el item que seleccionado (el que se muestra sobre el botón del botón)
        super(context, R.layout.spinner_selected_item, datos);
        this.context = context;
        this.datos = datos;
    }

    //este método establece el elemento seleccionado sobre el botón del spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_selected_item,null);
        }
        //((TextView) convertView.findViewById(R.id.texto)).setText(datos.get(position).Descripcion);
        ((ImageView) convertView.findViewById(R.id.icono)).setBackgroundResource(datos.get(position).Icon);

        return convertView;
    }

    //gestiona la lista usando el View Holder Pattern. Equivale a la típica implementación del getView
    //de un Adapter de un ListView ordinario
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.spinner_list_item, parent, false);
        }

        if (row.getTag() == null)
        {
            CheckHolder redSocialHolder = new CheckHolder();
            redSocialHolder.setIcono((ImageView) row.findViewById(R.id.icono));
            //redSocialHolder.setTextView((TextView) row.findViewById(R.id.texto));
            row.setTag(redSocialHolder);
        }

        //rellenamos el layout con los datos de la fila que se está procesando
        Maestro redSocial = datos.get(position);
        ((CheckHolder) row.getTag()).getIcono().setImageResource(redSocial.Icon);
        //((CheckHolder) row.getTag()).getTextView().setText(redSocial.Descripcion);

        return row;
    }

    /**
     * Holder para el Adapter del Spinner
     * @author danielme.com
     *
     */
    private static class CheckHolder
    {

        private ImageView icono;

        //private TextView textView;

        public ImageView getIcono()
        {
            return icono;
        }

        public void setIcono(ImageView icono)
        {
            this.icono = icono;
        }

//        public TextView getTextView()
//        {
//            return textView;
//        }
//
//        public void setTextView(TextView textView)
//        {
//            this.textView = textView;
//        }

    }
}