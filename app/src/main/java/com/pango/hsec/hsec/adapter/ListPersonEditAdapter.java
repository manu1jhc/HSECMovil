package com.pango.hsec.hsec.adapter;

        import android.app.Activity;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.TextView;

        import com.pango.hsec.hsec.R;
        import com.pango.hsec.hsec.model.PersonaModel;

        import java.util.List;

public class ListPersonEditAdapter extends RecyclerView.Adapter<ListPersonEditAdapter.ViewHolder> {
    private Activity activity;
    private List<PersonaModel> items;

    public ListPersonEditAdapter(Activity activity, List<PersonaModel> items) {
        this.activity = activity;
        this.items = items;
    }
    public void add(PersonaModel newdata){
        items.add(newdata);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_responsable, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.Nombre.setText(items.get(position).Nombres);
       // viewHolder.DNI.setText(items.get(position).NroDocumento);
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
           // DNI = (TextView)view.findViewById(R.id.txt_adicional);
            Cargo = (TextView)view.findViewById(R.id.txt_cargo);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);
           // itemView.setOnClickListener(this);
        }
/*
        @Override
        public void onClick(View v) {
            if(items.get(idposition).Correlativo>0)
                Toast.makeText(v.getContext(), "Descargando...", Toast.LENGTH_SHORT).show();
        }*/
    }
}
