 package com.example.andrey.mediaplayer;

 import android.annotation.TargetApi;
 import android.app.Activity;
 import android.content.Intent;
 import android.os.Build;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.Toast;

 import com.vk.sdk.VKAccessToken;
 import com.vk.sdk.VKCallback;
 import com.vk.sdk.VKScope;
 import com.vk.sdk.VKSdk;
 import com.vk.sdk.api.VKApi;
 import com.vk.sdk.api.VKApiConst;
 import com.vk.sdk.api.VKError;
 import com.vk.sdk.api.VKParameters;
 import com.vk.sdk.api.VKRequest;
 import com.vk.sdk.api.VKResponse;
 import java.util.Objects;

 /**
 * Created by Romanosov on 25.02.2016.
 */



public class Publish extends Activity {

     String title;
     EditText publish_text;
     Button wall_confirm;
     Button publish_cancel;
     private String[] scope = new String[]{VKScope.WALL, VKScope.STATUS};

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         VKSdk.initialize(this);
         super.onCreate(savedInstanceState);
         setContentView(R.layout.vk_publish);

         Intent intent = new Intent(this, MainActivity.class);

         title = intent.getStringExtra("title");

         VKSdk.login(this, scope);
     }



     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
             @Override
             public void onResult(VKAccessToken res) {
                 Toast.makeText(getApplicationContext(), "Успешная авторизация", Toast.LENGTH_SHORT).show();
                 publish_text = (EditText) findViewById(R.id.publish_text);
                 wall_confirm = (Button) findViewById(R.id.button_wall);
                 publish_cancel = (Button) findViewById(R.id.button_cancel);
                 String s = List.link;
                 s = s.replaceAll("(([:][0-9]+).+|[^\\/]+$)", "");
                 String text = "Сейчас играет радиостанция: " + List.nowMain
                         + "\nСсылка на станцию: " + s + "\nОпубликовано через mortum5Player";
                 publish_text.setText(text);
                 View.OnClickListener onClickListener = new View.OnClickListener() {
                     @TargetApi(Build.VERSION_CODES.KITKAT)
                     @Override
                     public void onClick(View v) {
                         switch (v.getId()) {

                             case R.id.button_wall:
                                 String post_it = publish_text.getText().toString();
                                 if (!Objects.equals(post_it, "")) {
                                     VKRequest request = VKApi.wall().post(VKParameters.from(VKApiConst.FRIENDS_ONLY, 1, VKApiConst.MESSAGE, post_it, VKApiConst.ATTACHMENTS, "photo72506440_406650660"));
                                     request.executeWithListener(new VKRequest.VKRequestListener() {
                                         @Override
                                         public void onComplete(VKResponse response) {
                                             super.onComplete(response);
                                             Toast.makeText(getApplicationContext(), "Запись опубликована.", Toast.LENGTH_LONG).show();
                                             publish_text.setText("");
                                         }
                                     });

                                     request.attempts = 10;
                                 }
                                 break;
                             case R.id.button_cancel:
                                 finish();
                                 break;
                         }
                     }
                 };

                 wall_confirm.setOnClickListener(onClickListener);
                 publish_cancel.setOnClickListener(onClickListener);

             }
             @Override
             public void onError(VKError error) {
                 finish();
                 Toast.makeText(getApplicationContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();

             }
         })) {
             super.onActivityResult(requestCode, resultCode, data);
         }
     }
 }