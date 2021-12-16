package com.pango.hsec.hsec.Observaciones;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.PowerManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.pango.hsec.hsec.GlobalVariables;
import com.pango.hsec.hsec.R;

import java.io.IOException;

public class ActVidDet extends AppCompatActivity implements SurfaceHolder.Callback,MediaPlayer.OnPreparedListener,MediaController.MediaPlayerControl{

    String video;
    public int length=0;

    private VideoView surfaceView;
    private ConstraintLayout top, button,left, right;
    private LinearLayout video_layout;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private MediaController mediaController;
    private Handler handler = new Handler();
    public PowerManager.WakeLock wakelock;

    public ProgressDialog pDialog;
    private SeekBar seekbar;
    public int percenloadin;

    public boolean isListvideo;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        percenloadin=0;

        setContentView(R.layout.activity_act_vid_det);
        Bundle datos = this.getIntent().getExtras();
        position=datos.getInt("post");
        isListvideo=datos.getBoolean("isList");
        //obtener_estado();
        Registrar(0);
        isListvideo=true;

        if(isListvideo==true) {

            String video_data=datos.getString("urltemp");
            video=video_data; //GlobalVariables.Urlbase.substring(0, GlobalVariables.Urlbase.length() - 4)+video_data.replace(".",GlobalVariables.cal_sd_hd);//+video_data.replace(".",GlobalVariables.cal_sd_hd);

        }else{
            //video = GlobalVariables.Urlbase.substring(0, GlobalVariables.Urlbase.length() - 4) + GlobalVariables.listdetvid.get(position).getUrl_vid().replace(".",GlobalVariables.cal_sd_hd);
        }

        if(GlobalVariables.con_status_video==200) {
            surfaceView = (VideoView) findViewById(R.id.surfaceView);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mediaController != null) {
                        mediaController.show();
                    }
                    return false;
                }
            });


            top = (ConstraintLayout) findViewById(R.id.padingtop);
            button = (ConstraintLayout) findViewById(R.id.padingbutton);
            left = (ConstraintLayout) findViewById(R.id.padingleft);
            right = (ConstraintLayout) findViewById(R.id.padingright);
            video_layout  = (LinearLayout) findViewById(R.id.layout_horizontal);


            pDialog = new ProgressDialog(ActVidDet.this);
            pDialog.setMessage("Buffering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            final PowerManager pm = (PowerManager) getSystemService(getBaseContext().POWER_SERVICE);
            this.wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "etiqueta");
            surfaceView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mediaController != null) {
                        mediaController.show();
                    }
                    return false;
                }
            });


        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(ActVidDet.this).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Error en la Reproducción");
            alertDialog.setMessage("El video no se encuentra disponible");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.show();
        }


    }

    //////////////////////////////////////////////

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) //To fullscreen
        {
            if(mediaPlayer.getVideoWidth()>mediaPlayer.getVideoHeight()) { //fullscreen
                top.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                surfaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                video_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }
            else{
                top.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
                video_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                surfaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.34f));
            }
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            if(mediaPlayer.getVideoHeight()>mediaPlayer.getVideoWidth())//fullScrim
            {
                top.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                surfaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                video_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }
            else {
                top.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                video_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.34f));
                surfaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if(mediaPlayer !=null) mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer !=null){
            mediaPlayer.pause();
            Registrar(mediaPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (pDialog.isShowing()) pDialog.dismiss();
        mediaPlayer.seekTo( Integer.parseInt(Recuperar_data()));
        mediaPlayer.start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(surfaceView);
        if(getResources().getConfiguration().orientation==1) //orientacion Vertical
        {
            if(mediaPlayer.getVideoHeight()>mediaPlayer.getVideoWidth())//fullScrim
            {
                top.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                surfaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                video_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }
            else{
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                surfaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }
        }
        else{   //orientacion Horizonal
            if(mediaPlayer.getVideoWidth()>mediaPlayer.getVideoHeight()) { //fullscreen
                top.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                left.setVisibility(View.GONE);
                right.setVisibility(View.GONE);
                surfaceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                video_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }
            else{
                top.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                video_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }
        }
        handler.post(new Runnable() {

            public void run() {
                try{
                    mediaController.setEnabled(true);
                    mediaController.show();
                }
                catch (Exception e){
                    Toast.makeText(ActVidDet.this, "Ocurrio un error al cargar reporte facilito", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.wakelock.acquire();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Registrar(0);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wakelock.release();
                    }
                },3000);
            }
        });

        int topContainerId = getResources().getIdentifier("mediacontroller_progress", "id", "android");
        seekbar = (SeekBar) mediaController.findViewById(topContainerId);

        mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                percenloadin=percent;
            }
        });

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDisplay(surfaceHolder);
        try{
            mediaPlayer.setDataSource(video);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaController = new MediaController(this);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    public void Registrar(int mPosition) {
        SharedPreferences posicion_vid = this.getSharedPreferences("pos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = posicion_vid.edit();
        editor.putString("pos_url", String.valueOf(mPosition));

        editor.commit();
        //v.finish();
    }
    public String Recuperar_data() {

        SharedPreferences settings =  this.getSharedPreferences("pos", Context.MODE_PRIVATE);
        String dominio_user = settings.getString("pos_url","0");
        return dominio_user;
    }

    public void releaseMediaPlayer(){
        if(mediaPlayer!= null)
        {
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    @Override
    public void start() {
        mediaPlayer.start();
        this.wakelock.acquire();
    }

    @Override
    public void pause() {
        wakelock.release();
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return percenloadin;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }
/*
    public void obtener_estado(){
        SharedPreferences cal_vid =  this.getSharedPreferences("calidad", Context.MODE_PRIVATE);
        Boolean calvid = cal_vid.getBoolean("cal",false);

        if(calvid){
            GlobalVariables.cal_sd_hd=".";
        }else{
            GlobalVariables.cal_sd_hd="_SD.";
        }
    }
*/
}
