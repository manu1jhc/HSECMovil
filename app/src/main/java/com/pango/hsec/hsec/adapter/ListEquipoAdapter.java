package com.pango.hsec.hsec.adapter;

        import android.app.Activity;
        import android.graphics.Color;
        import androidx.recyclerview.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.pango.hsec.hsec.R;
        import com.pango.hsec.hsec.model.EquipoModel;

        import java.util.List;

public class ListEquipoAdapter extends RecyclerView.Adapter<ListEquipoAdapter.ViewHolder> {
    private Activity activity;
    private List<EquipoModel> items;
    private boolean ShowLider;

    public ListEquipoAdapter(Activity activity, List<EquipoModel> items,boolean ShowLider) {
        this.activity = activity;
        this.items = items;
        this.ShowLider=ShowLider;
    }
    public void add(EquipoModel newdata){
        boolean pass=true;
        for (EquipoModel temp: items) {
            if(temp.CodPersona.equals(newdata.CodPersona))
                pass=false;
        }
        if(pass)
        {
            items.add(newdata);
          //  notifyDataSetChanged();
        }
        else Toast.makeText(activity, "La persona ya existe en la lista" , Toast.LENGTH_SHORT).show();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_equipo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if(ShowLider)
        {
            String BackgrColor= "#FFFFFF";
            if(items.get(position).Lider.equals("1"))
                BackgrColor= "#BFE3DE";
            viewHolder.itemView.setBackgroundColor(Color.parseColor(BackgrColor));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(items.get(position).Lider.equals("0"))
                    {
                        for (EquipoModel item:items) item.Lider="0";
                        items.get(position).Lider="1";
                        notifyDataSetChanged();
                    }
                }
            });
        }
        viewHolder.Nombre.setText(items.get(position).Nombres);
        viewHolder.Cargo.setText(items.get(position).Cargo);
        viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         items.remove(position);
                                                         notifyItemRemoved(position);
                                                         notifyItemRangeChanged(position,items.size());
                                                     }
                                                 }
        );

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener
        private TextView Nombre, DNI, Cargo;
        private ImageButton btn_Delete;

        public ViewHolder(View view) {
            super(view);
            Nombre = (TextView)view.findViewById(R.id.txt_tarea);
            Cargo = (TextView)view.findViewById(R.id.txt_cargo);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
        }
    }
}
