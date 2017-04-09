package com.masha.testapplication;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreateVideoActivity extends AppCompatActivity implements View.OnClickListener {

    MediaRecorder recorder = new MediaRecorder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_video);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_video:

                break;
            case R.id.second_video:

                break;
            case R.id.post:

                break;
        }
    }

    private void startRecord() {
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setVideoEncodingBitRate(150000); // битрейт видео
        recorder.setAudioEncodingBitRate(8000); // битрейт аудио
        recorder.setAudioSamplingRate(8000); // частота дискретизации записи аудио
        recorder.setAudioChannels(1); // количество каналов записи аудио
        recorder.setVideoFrameRate(30); // фреймрейт записи видео
        recorder.setVideoSize(640, 480); // размер картинки
        recorder.setMaxDuration(0); // максимальная длительность записи
        recorder.setMaxFileSize(0); // максимальный размер файла

        recorder.setOutputFile(путь); //задаем имя файла для записи

        recorder.setPreviewDisplay(Surface); //задать превью

        recorder.start(); //включается превью и начинается запись
    }

    private void stopRecord() {
        recorder.stop();
        recorder.reset(); // возвращаемся к шагу, задающему setAudioSource()

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recorder.release();
    }
}
