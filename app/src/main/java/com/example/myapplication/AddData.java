package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class AddData extends AppCompatActivity {
    Connection connection;
    private ImageView imageButton;
    String ConnectionResult = "";
    TextView BaseNameAdd;
    TextView GeografPositionAdd;
    String Img="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        configureBackButton();
        imageButton=findViewById(R.id.imageAdd);
    }
    public void onClickChooseImage(View view)
    {
        getImage();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && data!= null && data.getData()!= null)
        {
            if(resultCode==RESULT_OK)
            {
                Log.d("MyLog","Image URI : "+data.getData());
                imageButton.setImageURI(data.getData());
                Bitmap bitmap = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
                encodeImage(bitmap);

            }
        }
    }

    private void getImage()
    {
        Intent intentChooser= new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser,1);
    }

    private String encodeImage(Bitmap bitmap) {
        int prevW = 150;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Img= Base64.getEncoder().encodeToString(bytes);
            return Img;
        }
        return "";
    }
    private void configureBackButton() {
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void SetTextFromSql(View v) {
        BaseNameAdd = findViewById(R.id.BaseNameAdd);
        GeografPositionAdd = findViewById(R.id.GeografPositionAdd);
        String Base = BaseNameAdd.getText().toString();
        String Position = GeografPositionAdd.getText().toString();
        try {
            ConectionHellper conectionHellper = new ConectionHellper();
            connection = conectionHellper.connectionClass();
            if (BaseNameAdd.getText().length()==0 || GeografPositionAdd.getText().length()==0 )
            {
                Toast.makeText(this,"Не заполнены обязательные поля", Toast.LENGTH_LONG).show();
                return;
            }
            if (connection != null) {

                String query = "INSERT INTO Student (Name, Surname, Image) values ('" + Base + "','" + Position + ",'"+ Img +"')";
                Statement statement = connection.createStatement();
                statement.execute(query);
                Toast.makeText(this,"Успешно добавлено", Toast.LENGTH_LONG).show();
            } else {
                ConnectionResult = "Check Connection";
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}