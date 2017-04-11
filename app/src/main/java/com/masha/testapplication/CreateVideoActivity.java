package com.masha.testapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.getExternalStorageState;

public class CreateVideoActivity extends AppCompatActivity implements View.OnClickListener {

    MediaRecorder recorder = new MediaRecorder();
    private File video;
    private File directory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_video);

        Button firstVideo = (Button) findViewById(R.id.first_video);
        Button secVideo = (Button) findViewById(R.id.second_video);
        Button post = (Button) findViewById(R.id.post);
        firstVideo.setOnClickListener(this);
        secVideo.setOnClickListener(this);
        post.setOnClickListener(this);

        createDirectory();
        // TODO: это строчка для отладки
        video = new File(directory.getPath() + "/" + "video_"
                + 1 + ".mp4");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_video:
                startRecord(1);
                break;
            case R.id.second_video:
                startRecord(2);
                break;
            case R.id.post:
                PostVideoTask postVideoTask = new PostVideoTask();
                postVideoTask.execute((Void) null);
                break;
        }
    }

    private void startRecord(int num) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //указываем явно путь для сохранения файла
        intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(num));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        if (resultCode == RESULT_OK) {
            if (intent == null) {
                Log.d("MyLogs", "Intent is null");
            } else {
                Log.d("MyLogs", "Video uri: " + intent.getData());
            }
        } else if (resultCode == RESULT_CANCELED) {
            //Если пользователь закроет экран через кнопку Back
            Log.d("MyLogs", "Canceled");
        }
    }

    private Uri generateFileUri(int num) {
        File file = null;
        switch (num) {
            case 1:
                video = new File(directory.getPath() + "/" + "video_"
                        + num + ".mp4");
                break;
            case 2:
                video = new File(directory.getPath() + "/" + "video_"
                        + num + ".mp4");
                break;
        }
        return Uri.fromFile(video);
    }

    private void createDirectory() {
        //проверяем что есть карта памяти и она доступна для записи
        if (getExternalStorageState().equals(MEDIA_MOUNTED)) {
            // задаем раздел и название папки
            directory = new File(getExternalFilesDir(
                    Environment.DIRECTORY_MOVIES), "MyVideos");
            //создаем указанный каталог
            if (!directory.mkdirs()) {
                Log.e("MyLogs", "Директория не создана");
            } else {
                Log.e("MyLogs", "Директория MyVideos создана");
            }
        }
    }

    private class PostVideoTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {

            Intent intent = getIntent();
            String access_token = intent.getStringExtra("токен");
            ServerHelper serverHelper = new ServerHelper(access_token);
            return serverHelper.postFile(video);
        }

        @Override
        protected void onPostExecute(final Integer code) {

            switch (code) {
                case 200:
                    //если все прошло успешно, то завершаем поток,
                    finish();
                    Log.e("MyLogs", "Файл успешно добавлен");

                    break;
                case 400: //неправильный запрос, синтаксическая ошибка
                    Log.e("MyLogs", "Ошибка отправки файла 400");
                    break;
                case 500: //ошибка на сервере
                    //выводим сообщение «Ошибка сервера!»
                    errorMassage();
                    break;
            }
        }
    }

    private void errorMassage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ошибка сервера!")
                .setMessage("Попробуйте снова.")
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
