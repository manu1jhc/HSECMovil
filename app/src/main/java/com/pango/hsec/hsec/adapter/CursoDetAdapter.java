package com.pango.hsec.hsec.adapter;

        import android.content.Context;
        import androidx.recyclerview.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import com.pango.hsec.hsec.GlobalVariables;
        import com.pango.hsec.hsec.R;
        import com.pango.hsec.hsec.Utils;
        import com.pango.hsec.hsec.model.CapCursoModel;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;

public class CursoDetAdapter extends RecyclerView.Adapter<CursoDetAdapter.CursodetViewHolder> {


    private Context context;
    CapCursoModel cursoModel;
    DateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
    DateFormat formatoRender = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");

    public CursoDetAdapter(Context context, CapCursoModel cursoModel) {
        this.cursoModel = cursoModel;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(CursoDetAdapter.CursodetViewHolder CursodetViewHolder, int position) {
        //EquipoModel em = equipoModel.get(i);
        //personaViewHolder.Nombre.setText(em.Nombres);
        //personaViewHolder.area.setText(em.Cargo);

        CursodetViewHolder.ladoIzquierdo.setText(GlobalVariables.cursoDetIzq[position]);
        CursodetViewHolder.ladoDerecho.setText(Utils.getCurso(cursoModel,GlobalVariables.cursoDetCab[position]));


    }



    @Override
    public CursoDetAdapter.CursodetViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.obslist, viewGroup, false);
        CursoDetAdapter.CursodetViewHolder nvh= new CursoDetAdapter.CursodetViewHolder(itemView);
        return nvh;

    }

    @Override
    public int getItemCount() {
        return GlobalVariables.cursoDetCab.length;
    }


    public static class CursodetViewHolder extends RecyclerView.ViewHolder {
        //definicion de las variables
        TextView ladoIzquierdo;
        TextView ladoDerecho;

        //uniendo con los layouts
        public CursodetViewHolder(View v) {
            super(v);
            ladoIzquierdo = (TextView)  v.findViewById(R.id.txcab);
            ladoDerecho = (TextView)  v.findViewById(R.id.txdet);
        }
    }
}
