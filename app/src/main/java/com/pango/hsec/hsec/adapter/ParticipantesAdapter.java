package com.pango.hsec.hsec.adapter;

        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;

        import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import android.text.Editable;
        import android.text.InputFilter;
        import android.text.TextWatcher;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.PopupWindow;
        import android.widget.ProgressBar;
        import android.widget.RadioGroup;
        import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.pango.hsec.hsec.Capacitacion.ParticipantesCurso;
        import com.pango.hsec.hsec.GlobalVariables;
        import com.pango.hsec.hsec.R;
        import com.pango.hsec.hsec.model.PersonaModel;
        import com.pango.hsec.hsec.utilitario.InputFilterMinMax;

        import org.apache.commons.lang3.StringUtils;

        import java.util.List;

        import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ParticipantesAdapter extends RecyclerView.Adapter {
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
    private int activeSwipeR=1000;
    private ParticipantesCurso activity;
    public ParticipantesAdapter(ParticipantesCurso activity,List<PersonaModel> personas, RecyclerView recyclerView,SwipeRefreshLayout swipeRefreshLayout) {
        items = personas;
        this.activity=activity;
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

    public void remove(int index){
        items.remove(index);
        notifyDataSetChanged();
    }

    public void update(String data){
        String[] datos= data.split("-");
        items.get(Integer.parseInt(datos[0])).Email=datos[1];
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
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
            ((ViewHolder) holder).Nota.setText(persona.Email);
            ((ViewHolder) holder).activity=activity;
            ((ViewHolder) holder).Posicion=position;

            String BackgrColor= "#FFFFFF";
            if(items.get(position).Estado.equals("E"))
                BackgrColor= "#CACFD2";
            ((ViewHolder) holder).itemView.setBackgroundColor(Color.parseColor(BackgrColor));

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
        private TextView Nombre, Nota, Cargo;
        ParticipantesCurso activity;
        int Position,Posicion;

        //popup
        LayoutInflater layoutInflater;
        View popupView;
        PopupWindow popupWindow;
        ImageView avatar;
        TextView txtNombres,txtCargo,txtDNI;
        EditText txtNota;
        SeekBar seekBar;
        ImageButton btnSiguiente,btnAnterior;
        boolean edittext=false;
        public ViewHolder(View view) {
            super(view);

            Nombre = (TextView)view.findViewById(R.id.tvName);
            Cargo = (TextView)view.findViewById(R.id.tvCargo);
            Nota = (TextView)view.findViewById(R.id.tvNota);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Position=Posicion;
                    if(activity.personasList.get(Position).Estado.equals("E")){
                        Toast.makeText(activity, "Participante no tiene asistencia al curso", Toast.LENGTH_LONG).show();
                        return;
                    }
                    layoutInflater =(LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
                    popupView = layoutInflater.inflate(R.layout.popup_notas, null);

                    popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, true);
                    popupWindow.showAtLocation(activity.list_Personas, Gravity.CENTER, 0, 0);
                    popupWindow.setFocusable(true);
                    popupWindow.update();
                    popupWindow.setBackgroundDrawable(new ColorDrawable()); //Color.TRANSPARENT
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            popupWindow.dismiss();
                        }
                    });
                    //ConstraintLayout constDissmiss=(ConstraintLayout) popupView.findViewById(R.id.constdismiss);
                    avatar=(ImageView) popupView.findViewById(R.id.img_avatar);
                    //CardView cardResult=(CardView) popupView.findViewById(R.id.card_title);
                    txtNombres=(TextView) popupView.findViewById(R.id.txt_nombres);
                    txtCargo=(TextView) popupView.findViewById(R.id.txt_cargo);
                    txtDNI=(TextView) popupView.findViewById(R.id.txt_dni);

                    txtNota=(EditText) popupView.findViewById(R.id.txt_nota);
                    txtNota.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "20")});
                    ImageButton btnClose=(ImageButton) popupView.findViewById(R.id.btn_close);
                    ImageButton btnIncrement=(ImageButton) popupView.findViewById(R.id.btn_increment);
                    ImageButton btnDecrement=(ImageButton) popupView.findViewById(R.id.btn_decrement);
                    btnAnterior=(ImageButton) popupView.findViewById(R.id.btn_ant);
                    btnSiguiente=(ImageButton) popupView.findViewById(R.id.btn_sig);
                    ImageButton btnDelete=(ImageButton) popupView.findViewById(R.id.btn_delete);
                    ImageButton btnSalvar=(ImageButton) popupView.findViewById(R.id.btn_save);
                    seekBar=(SeekBar)popupView.findViewById(R.id.seekBar2);

                    // set data
                    setdata(Position);
                    CheckPosition(Position);
                    //action button
                    btnClose.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            popupWindow.dismiss();
                        }
                    });

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                            // TODO Auto-generated method stub
                            if(!edittext)txtNota.setText(progress+"");
                            edittext=false;
                        }
                    });

                    //detect chabgues values
                    txtNota.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int st, int b, int c)
                        { }

                        @Override
                        public void beforeTextChanged(CharSequence s, int st, int c, int a)
                        { }

                        @Override
                        public void afterTextChanged(Editable s)
                        {   edittext=true;
                            String Nota=s.toString();
                            if(Nota.isEmpty())Nota="0";
                            seekBar.setProgress(Integer.parseInt(Nota));
                        }
                    });

                    btnIncrement.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            int Nota= StringUtils.isEmpty(txtNota.getText().toString())?0:Integer.parseInt(txtNota.getText().toString());
                            if(Nota<20){
                                Nota++;
                                seekBar.setProgress(Nota);
                                //txtNota.setText(Nota+"");
                            }
                        }
                    });

                    btnDecrement.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            int Nota= StringUtils.isEmpty(txtNota.getText().toString())?0:Integer.parseInt(txtNota.getText().toString());
                            if(Nota>0){
                                Nota--;
                                seekBar.setProgress(Nota);
                                //txtNota.setText(Nota+"");
                            }
                        }
                    });

                    btnAnterior.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Position--;
                            setdata(Position);
                            CheckPosition(Position);
                        }
                    });
                    btnSiguiente.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Position++;
                            if(activity.personasList.get(Position).Estado.equals("A")&& Position<activity.personasList.size()-1)
                             setdata(Position);
                            CheckPosition(Position);
                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity,android.R.style.Theme_Material_Dialog_Alert);
                            alertDialog.setTitle("Eliminar participante")
                                    .setMessage("Esta seguro de eliminar al participante "+ activity.personasList.get(Position).Nombres+"?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.DeleteObject(Position);
                                            popupWindow.dismiss();
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
                    });
                    btnSalvar.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            activity.updateNota(Position, txtNota.getText().toString());
                        }
                    });
                }
            });
        }

        public void setdata(int index){
            PersonaModel Alumno = activity.personasList.get(index);
            txtNombres.setText(Alumno.Nombres);
            txtNota.setText(Alumno.Email);
            edittext=false;
            if (Alumno.NroDocumento != null) {
                txtDNI.setText(Alumno.NroDocumento);
                String Url_img = GlobalVariables.Url_base + "media/getAvatar/" + Alumno.NroDocumento.replace("*","").replace(".","") + "/Carnet.jpg";
                Glide.with(activity)
                        .load(Url_img)
                        //.override(50, 50)
                        //.transform(new CircleTransform(ParticipantesCurso.this)) // applying the image transformer
                        .into(avatar);
            }

            if(StringUtils.isEmpty(Alumno.Cargo)) txtCargo.setVisibility(View.GONE);
            else {
                txtCargo.setVisibility(View.VISIBLE);
                txtCargo.setText(Alumno.Cargo);
            }
            int Nota= StringUtils.isEmpty(txtNota.getText())?0:Integer.parseInt(txtNota.getText().toString());
            seekBar.setProgress(Nota);
        }
        public void CheckPosition(int index){
            btnAnterior.setEnabled(true);
            btnSiguiente.setEnabled(true);
            if(index<=0) btnAnterior.setEnabled(false);
            if(activity.personasList.get(Position).Estado.equals("E")) btnSiguiente.setEnabled(false);
            if(index>=activity.personasList.size()-1) btnSiguiente.setEnabled(false);
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