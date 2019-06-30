package com.example.wavrecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {


    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE_0 = "record_temp0.raw";
    private static final String AUDIO_RECORDER_TEMP_FILE_1 = "record_temp1.raw";
    private static final String AUDIO_RECORDER_TEMP_FILE_2 = "record_temp2.raw";
    private static final String AUDIO_RECORDER_TEMP_FILE_3 = "record_temp3.raw";

    private static final String AUDIO_WAV_TEMP_FILE = "temp.raw";

    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;

    private boolean isRecording = false;
    int i;

    private static String [] AUDIO_RECORDER_TEMP_LIST = {AUDIO_RECORDER_TEMP_FILE_0,AUDIO_RECORDER_TEMP_FILE_1,AUDIO_RECORDER_TEMP_FILE_2,AUDIO_RECORDER_TEMP_FILE_3};



    String lastFileName;

    public static final String TAG= MainActivity.class.getSimpleName();

    Button startRecord1, stopRecord1, play1,startRecord2, stopRecord2, play2,startRecord3, stopRecord3, play3, startRecord4, stopRecord4, play4, saveButton;

    List<RecordViews> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewList = new ArrayList<>();

        startRecord1 = findViewById(R.id.start_button1);
        stopRecord1 = findViewById(R.id.stop_button1);
        play1 = findViewById(R.id.play_button1);
        viewList.add(new RecordViews(startRecord1,stopRecord1,play1));

        startRecord2 = findViewById(R.id.start_button2);
        stopRecord2 = findViewById(R.id.stop_button2);
        play2 = findViewById(R.id.play_button2);
        viewList.add(new RecordViews(startRecord2,stopRecord2,play2));


        startRecord3 = findViewById(R.id.start_button3);
        stopRecord3 = findViewById(R.id.stop_button3);
        play3 = findViewById(R.id.play_button3);
        viewList.add(new RecordViews(startRecord3,stopRecord3,play3));


        startRecord4 = findViewById(R.id.start_button4);
        stopRecord4 = findViewById(R.id.stop_button4);
        play4 = findViewById(R.id.play_button4);
        viewList.add(new RecordViews(startRecord4,stopRecord4,play4));


        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyWaveFile(getFilename());
            }
        });

        if(checkPermission()) {
            initialization();

//            Log.d(TAG,"Got all permission");
        }
    }


    public void enableButton(int index){
//        Log.d(TAG,"index="+index);
        viewList.get(index).getStop().setEnabled(true);
        viewList.get(index).getStart().setEnabled(false);

        for (int i=0;i<4;i++){
            if(i!=index) {
                viewList.get(i).getStart().setEnabled(false);
                viewList.get(i).getStop().setEnabled(false);
            }
        }
    }


    public void enableAllStartButtons(){
        for(int i=0;i<viewList.size();i++){
            viewList.get(i).getStart().setEnabled(true);
            viewList.get(i).getStop().setEnabled(false);
        }
        saveButton.setEnabled(true);
    }


    public void initialization(){

        enableAllStartButtons();

        bufferSize = AudioRecord.getMinBufferSize(8000,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT);
        Log.d(TAG,"buffersize="+bufferSize);




            viewList.get(0).getStart().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    enableButton(0);

                    startRecording(0);
                }
            });

            viewList.get(0).getStop().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopRecording(0);
                    enableAllStartButtons();
                }
            });

            viewList.get(0).getPlay().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playLastFile(0);
                }
            });

            viewList.get(1).getStart().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    enableButton(1);

                    startRecording(1);
                }
            });

            viewList.get(1).getStop().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopRecording(1);
                    enableAllStartButtons();
                }
            });

            viewList.get(1).getPlay().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playLastFile(1);
                }
            });

            viewList.get(2).getStart().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    enableButton(2);

                    startRecording(2);
                }
            });

            viewList.get(2).getStop().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopRecording(2);
                    enableAllStartButtons();
                }
            });

            viewList.get(2).getPlay().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playLastFile(2);
                }
            });

            viewList.get(3).getStart().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    enableButton(3);

                    startRecording(3);
                }
            });

            viewList.get(3).getStop().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopRecording(3);
                    enableAllStartButtons();
                }
            });

            viewList.get(3).getPlay().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playLastFile(3);
                }
            });




    }

    private void startRecording(final int index){
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);

        int i = recorder.getState();
        if(i==1)
            recorder.startRecording();
        isRecording = true;



        recordingThread = new Thread(new Runnable() {

            @Override
            public void run() {

                byte data[] = new byte[bufferSize];
//                String filename = getTempFilename(index);
                String filename = getTempWavFile();
                FileOutputStream os = null;

                try {
                    os = new FileOutputStream(filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                int read = 0;

                if(null != os){
                    while(isRecording){
                        read = recorder.read(data, 0, bufferSize);

                        if(AudioRecord.ERROR_INVALID_OPERATION != read){
                            try {
                                os.write(data);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    try {
                        os.close();
                        FileInputStream in= new FileInputStream(filename);
                        String tempWav = getTempFilename(index);
                        FileOutputStream outWav = new FileOutputStream(tempWav);
                        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * 2/8;

                        Log.d(TAG,"inp totalDataLen="+in.getChannel().size()+36);
                        WriteWaveFileHeader(outWav,in.getChannel().size(),in.getChannel().size()+36,RECORDER_SAMPLERATE,2,byteRate);
                        while (in.read(data) != -1) {
                            outWav.write(data);
                        }
                        Log.d(TAG,"inp totalDataLen="+outWav.getChannel().size());


                        outWav.close();
                        in.close();

                        deleteTempWavFile();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"AudioRecorder Thread");

        recordingThread.start();
    }

    private void stopRecording(int index){
        if(null != recorder){
            isRecording = false;

            int i = recorder.getState();
            if(i==1)
                recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }


    }






    private void copyWaveFile(String outFilename){

        lastFileName = outFilename;
        FileInputStream in;
        FileOutputStream out;
        long totalDataLen;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 2,count=0;

        byte[] data = new byte[bufferSize];
        byte[] pad = new byte[20];
        byte[] convArr = getByteFromInt(0) ;
        for(int i=0;i<convArr.length;i++){
            pad[count] = convArr[i];
            count++;
        }

        String tempWavFile = getTempWavFile();



        String filepath = Environment.getExternalStorageDirectory().getPath();

        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        try {
            out = new FileOutputStream(tempWavFile);


            for(int i=0;i<AUDIO_RECORDER_TEMP_LIST.length;i++) {

                String fileName = file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_LIST[i];
                in = new FileInputStream(fileName);
                Log.d(TAG,"copyWaveFile inp file len="+in.getChannel().size());

                while (in.read(data) != -1) {

                    out.write(data);
                }

                convArr = getByteFromInt((int)out.getChannel().size()) ;
                    for (int j = 0; j < convArr.length; j++) {
                        pad[count] = convArr[j];
                        count++;
                    }

                Log.d(TAG,"copyWaveFile output file len="+out.getChannel().size());

                in.close();
            }

            out.close();

            in = new FileInputStream(tempWavFile);
            out = new FileOutputStream(outFilename);
            out.write(pad,0,20);
            while (in.read(data) != -1) {

                out.write(data);
            }
            out.close();
            in.close();
            deleteTempFiles();

            Toast.makeText(this,"Saved .wav file",Toast.LENGTH_SHORT).show();
            enablePlayButtons();

        } catch (FileNotFoundException e) {
            Toast.makeText(this,"Press Start to record files",Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void enablePlayButtons(){
        for (int i=0;i<viewList.size();i++){
            viewList.get(i).getPlay().setEnabled(true);
            viewList.get(i).getStart().setEnabled(true);

        }
        saveButton.setEnabled(true);
    }


    private void disablePlayButton(){
        for (int i=0;i<viewList.size();i++){
            viewList.get(i).getPlay().setEnabled(false);
            viewList.get(i).getStart().setEnabled(false);

        }
        saveButton.setEnabled(false);

    }



    public void playLastFile(int index){
        FileInputStream in = null;
        FileOutputStream out = null;
        FileDescriptor fd = null;
        int fileSize;
        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(lastFileName);
            Log.d(TAG,"playlastFile len="+in.getChannel().size());

            int fullAudioLen=0,durationOfAudio=0,headerPadding=0;
            int bufferOffset=0;
            byte[] pad = new byte[20];
            in.read(pad);

            String tempWavFile = getTempWavFile();
            out = new FileOutputStream(tempWavFile);
            while (in.read(data) != -1) {

                out.write(data);
            }
            fileSize =(int) out.getChannel().size();
            out.close();
            in.close();



            bufferOffset = GetLittleEndianIntegerFromByteArray(pad,(((index+1)*4)-1));
            if(index<3){
                durationOfAudio = GetLittleEndianIntegerFromByteArray(pad,(((index+2)*4)-1));
            }
            else{
                durationOfAudio = fileSize;
            }

            Log.d(TAG,"index="+index+" bufferOffset="+bufferOffset+" durationOfAudio="+durationOfAudio);



            in = new FileInputStream(tempWavFile);

            fd = in.getFD();
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(fd,bufferOffset,durationOfAudio);

            player.prepare();
            player.start();
            disablePlayButton();

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    deleteTempWavFile();
                    enablePlayButtons();
                }
            });

            in.close();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }






    byte[] getByteFromInt(int a){
        byte[] b = new byte[4];
        for(int i=0;i<b.length;i++){
            b[i]= (byte) (a >> (8*i) & 0xff);
        }
        return b;
    }


    private void deleteTempFiles() {

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File folder = new File(filepath,AUDIO_RECORDER_FOLDER);

        for(int i=0;i<AUDIO_RECORDER_TEMP_LIST.length;i++){
            File file = new File(folder.getAbsolutePath()+"/"+AUDIO_RECORDER_TEMP_LIST[i]);

            file.delete();

        }


    }

    int GetLittleEndianIntegerFromByteArray(byte[] data, int endIndex) {

        long a=0;

        for (int i=endIndex; i >= endIndex-3; --i)
            a = (a << 8) | (0xff & data[i]);


        return (int)a;
    }


    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {



        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = RECORDER_BPP; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);



        out.write(header, 0, 44);
    }



    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV);
    }

    private String getTempFilename(int index){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }


        File tempFile = new File(filepath,AUDIO_RECORDER_TEMP_LIST[index]);

        if(tempFile.exists())
            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_LIST[index]);
    }

    private String getTempWavFile(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        File tempFile = new File(filepath,AUDIO_WAV_TEMP_FILE);

        if(tempFile.exists())
            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_WAV_TEMP_FILE);
    }

    private void deleteTempWavFile(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        File tempFile = new File(file.getAbsolutePath(),AUDIO_WAV_TEMP_FILE);

        if(tempFile.exists())
            tempFile.delete();


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case 123:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    initialization();


                } else {
                    Toast.makeText(MainActivity.this,"Need both permissions to operate",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Toast.makeText(MainActivity.this,"Need both permissions to operate",Toast.LENGTH_LONG).show();


        }
    }



    public  boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);

            return false;
        }
        else {
            return true;
        }


    }
}
