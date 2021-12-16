package com.pango.hsec.hsec.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

import com.pango.hsec.hsec.Capacitacion.AsistentesCurso;
import com.pango.hsec.hsec.R;
import com.pango.hsec.hsec.model.PersonaModel;

import java.util.List;



public class AsistenteAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public List<PersonaModel> items;

    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AsistentesCurso activity;
    private int activeSwipeR=1000;
    public AsistenteAdapter(AsistentesCurso activity,List<PersonaModel> personas, RecyclerView recyclerView,SwipeRefreshLayout swipeRefreshLayout) {
        items = personas;
        this.activity = activity;
        this.swipeRefreshLayout=swipeRefreshLayout;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if(lastVisibleItem<=activeSwipeR) {
                        activeSwipeR=lastVisibleItem;
                    }
                    if(lastVisibleItem>activeSwipeR)  swipeRefreshLayout.setEnabled( true );
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public void add(PersonaModel newdata){
        boolean pass=true;
        for (PersonaModel temp: items) {
            if(temp.CodPersona.equals(newdata.CodPersona))
                pass=false;
        }
        if(pass)
        {
            items.add(0,newdata);
            notifyDataSetChanged();
        }
        else Toast.makeText(activity, "La persona ya existe en la lista" , Toast.LENGTH_SHORT).show();
    }
    public void remove(int index){
        items.remove(index);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_responsable, parent, false);
            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            PersonaModel persona = (PersonaModel) items.get(position);
            ((ViewHolder) holder).Nombre.setText(persona.Nombres);
            ((ViewHolder) holder).Cargo.setText(persona.Cargo);
            ((ViewHolder) holder).Hora=items.get(position).Email;
            ((ViewHolder) holder).btn_Delete.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                                 AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity,android.R.style.Theme_Material_Dialog_Alert);
                                                                    alertDialog.setTitle("Eliminar asistente")
                                                                     .setMessage("Esta seguro de eliminar la asistencia del alumno "+ items.get(position).Nombres+"?")
                                                                     .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                         public void onClick(DialogInterface dialog, int which) {
                                                                             activity.DeleteObject("Capacitacion/DeleteAsistentente?CodPersonaA="+items.get(position).CodPersona,position+3);
                                                                         }
                                                                     })
                                                                     .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                                         public void onClick(DialogInterface dialog, int which) {
                                                                             // do nothing
                                                                         }
                                                                     })
                                                                     .setIcon(android.R.drawable.ic_dialog_alert)
                                                                     ; //.show()
                                                             alertDialog.show();
                                                         }
                                                     }
            );

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }



    //
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Nombre, DNI, Cargo;
        private ImageButton btn_Delete;
        public String Hora;
        public ViewHolder(View view) {
            super(view);

            Nombre = (TextView)view.findViewById(R.id.txt_tarea);
            Cargo = (TextView)view.findViewById(R.id.txt_cargo);
            // DNI = (TextView)view.findViewById(R.id.txt_adicional);
            btn_Delete= (ImageButton) view.findViewById(R.id.btn_delete);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Hora de ingreso: "+Hora  , Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}