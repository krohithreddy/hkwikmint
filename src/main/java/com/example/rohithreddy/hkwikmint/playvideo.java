package com.example.rohithreddy.hkwikmint;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class playvideo extends AppCompatActivity {
    public static int button =0;
    UserSessionManager session;
    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    static final int READ_BLOCK_SIZE = 100;
    String phone ,starttime,endtime,routeid="";
    double longi,lati;
    File file;
    //mainl mainlobj;
    private SQLiteDatabase db;
    private Cursor d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);
        setTitle("         KWIKMINT   VIDEO ");

        Bundle b = getIntent().getExtras();
        phone = b.getString("phone");
        starttime = b.getString("start");
        longi = b.getDouble("lng");
        lati = b.getDouble("lat");
        db=openOrCreateDatabase("PKMDB", Context.MODE_PRIVATE, null);
        session = new UserSessionManager(getApplicationContext());
        videoView = (VideoView) findViewById(R.id.videoView);
        file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "d_vdr");
        if (mediaController == null) {
            mediaController = new MediaController(playvideo.this,false);
            videoView.setMediaController(mediaController);
        }
        try { // ID of video file.
            int id = this.getRawResIdByName("kwikmint");
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + id));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                int topContainerId = getResources().getIdentifier("mediacontroller_progress", "id", "android");
                SeekBar seekBarVideo = (SeekBar) mediaController.findViewById(topContainerId);
                videoView.seekTo(position);
                if (position == 0) {
                    videoView.start();
                }
                seekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() // dont allow forwarding and backwarding
                {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {seekBar.setEnabled(false);}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {seekBar.setEnabled(false);}
                });
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        // Re-Set the videoView that acts as the anchor for the MediaController
                        //   mediaController.setAnchorView(videoView);
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer vmp) {
                       /* View inflatedView = getLayoutInflater().inflate(R.layout.activity_mainl, null);
                        tb1 = inflatedView.findViewById(R.id.ro);
                        tb1.performClick();*/
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                        endtime = sdf.format(new Date());
                        System.out.println(endtime);
                        db.execSQL(" CREATE TABLE IF NOT EXISTS rid(route VARCHAR)");
                        d = db.rawQuery("SELECT * FROM rid", null);
                        d.moveToFirst();
                        if (!(d.moveToFirst()) || d.getCount() == 0){
                            System.out.println("rid is empty....bruhh");
                        }
                        else {
                            d.moveToFirst();
                            System.out.println(d.getString(0));
                            routeid=d.getString(1);
                        }
                        db=openOrCreateDatabase("PKMDB", Context.MODE_PRIVATE, null);
                        db.execSQL(" CREATE TABLE IF NOT EXISTS videotable(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "phonen VARCHAR,"+
                                "routeId VARCHAR,"+
                                "start VARCHAR," +
                                "end VARCHAR," +
                                "lat VARCHAR,lng VARCHAR); ");
                        db.execSQL("INSERT INTO videotable( phonen,routeId,start,end ,lat ,lng  )" +" VALUES('"+  phone +"','"+ routeid+"','"+ starttime +"','"+ endtime +"','"+  lati +"','"+  longi +"');");
                        session.startvideo();
                        Intent intent = new Intent();
                        button=1;
                        intent.setClass(playvideo.this, MainActivity.class);
                        startActivity(intent);

                    }
                });
            }
        });
    }
    public int getRawResIdByName(String resName) {
        String pkgName = this.getPackageName();
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        return resID;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        videoView.pause();
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);
    }
    @Override
    public void onBackPressed()// upon back press
    {
        videoView.pause();
        AlertDialog.Builder alertDialog  = new AlertDialog.Builder(playvideo.this );
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Video hasn't finished, it won't be counted as a view");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) { // On pressing Settings button
                Intent intent = new Intent(playvideo.this,
                        MainActivity.class);
                button=1;
                // mainlobj.button=1;    //no need of this ...... code goes through isattandancestarted in rmain.....
                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

                    endtime ="FS_AT--"+ sdf.format(new Date());
                    System.out.println(endtime);


                    db.execSQL(" CREATE TABLE IF NOT EXISTS rid(route VARCHAR)");
                    d = db.rawQuery("SELECT * FROM rid", null);
                    d.moveToFirst();
                    if (!(d.moveToFirst()) || d.getCount() == 0){
                        System.out.println("rid is empty....bruhh");
                    }
                    else {
                        d.moveToFirst();
                        System.out.println(d.getString(0));
                        routeid=d.getString(1);
                    }


                    db=openOrCreateDatabase("PKMDB", Context.MODE_PRIVATE, null);
                    db.execSQL(" CREATE TABLE IF NOT EXISTS videotable(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "phonen VARCHAR,"+
                            "routeId VARCHAR,"+
                            "start VARCHAR," +
                            "end VARCHAR," +
                            "lat VARCHAR,lng VARCHAR); ");
                    db.execSQL("INSERT INTO videotable( phonen,routeId,start,end ,lat ,lng  )" +" VALUES('"+  phone +"','"+ routeid+"','"+ starttime +"','"+ endtime +"','"+  lati +"','"+  longi +"');");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("going to mainactivity");
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { //on  pressing cancel button
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}