package com.example.my31_captureintent;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.my31_captureintent.COMMON.CommonMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "main:MainActivity";
    ImageView imageView;
    File imgFile = null;
    String imgFilePath = null;
    int reqPicCode = 1004;
    int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDangerousPermissions();
        // 사진업로드
        findViewById(R.id.btnUpload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String id = etIDMem.getText().toString();
//                String pw = etPWMem.getText().toString();
//                String name = etName.getText().toString();
//                String phone = etPhone.getText().toString();
//                String address = etAddress.getText().toString();

                // 보낼 파일 MultipartBody.Part 생성

                CommonMethod commonMethod = new CommonMethod();
//                MemberDTO dto = new MemberDTO() ;
//
//                dto.setId(etIDMem.getText().toString());
//                dto.setPw(etPWMem.getText().toString());
//                dto.setName( etName.getText().toString());
//                dto.setPhone(etPhone.getText().toString());
//                dto.setAddress(etAddress.getText().toString());
//                dto.setProfile(etAddress.getText().toString());

 //               commonMethod.setParams("param", dto);
                // 보낼 파일 MultipartBody.Part 생성
                // List<MultipartBody.Part> imgs = new ArrayList<>();
                RequestBody fileBody =null;
                MultipartBody.Part filePart =null;
                if(imgFile != null) {
                    fileBody = RequestBody.create(MediaType.parse("image/jpeg"), new File(imgFilePath));
                    filePart = MultipartBody.Part.createFormData("file", "test.jpg", fileBody);
                }
                // 데이터를 파라메터로 보낸다
                //commonMethod.setParams("param", dto);
                // 서버로 보내는 부분
                commonMethod.sendFile("anJoin", filePart, new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG, "onResponse: 보내기 성공 " );
                        finish();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onResponse: 보내기 실패 => " + t.getMessage() );
                    }
                });
            }
        });

        // 사진불러오기
        imageView = findViewById(R.id.imageView);
        findViewById(R.id.btnLoad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*.*");
                startActivityForResult(intent, REQUEST_CODE);
                imageView = findViewById(R.id.imageView);
            }
        });

        // 사진찍기
        findViewById(R.id.btnPic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 암묵적 인텐트 : 사진찍기(카메라창을 불러옴)
                Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 일단 이 인텐트가 사용가능한지 체크
                if (picIntent.resolveActivity(getPackageManager()) != null) {
                    imgFile = null;
                    // createFile 메소드를 이용하여 임시파일을 만듬
                    imgFile = createFile();

                    if(imgFile != null){
                        //API24 이상부터는 FileProvider를 제공해야함

                        Uri imgUir = FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".fileprovider",
                                imgFile);
                        // 만약에 API24 이상이면
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            picIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUir);
                        }else{
                            picIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(imgFile));
                        }

                        startActivityForResult(picIntent,reqPicCode);
                    }
                }

            }
        });
    }


    // 사진을 찍은 후 데이터 받는곳 : 정해져 있음


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
        if(requestCode == reqPicCode && resultCode == RESULT_OK){
            // 저장처리를 함
            Toast.makeText(this, "사진이 잘 찍힘", Toast.LENGTH_SHORT).show();

            setPic();
        }
    }
    // 사진 저장처리를 하는 곳
    private void setPic() {
        
        // 사진의 크기 가져오기
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 사진의 해상도를 1/8로 지정
        options.inSampleSize = 8;
        // 비트맵 이미지를 생성
        Bitmap bitmap = BitmapFactory.decodeFile(imgFilePath);
        // 이미지를 갤러리에 저장
        gelleryAddPic(bitmap);
        // 이미지를 이미지뷰에 세팅
        imageView.setImageBitmap(bitmap);
    }
    // 이미지를 갤러리에 저장
    private void gelleryAddPic(Bitmap bitmap) {
        FileOutputStream fos;
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){ // API29
            // 쓰기 객체
            ContentResolver resolver = getContentResolver();
            // 맵구조를 가진 ContentValuews : 파일정보를 저장함
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,
                    "Image_jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE,
                    "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator + "MyFolder");
            Uri imageUri = resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
            );
            try {
                fos = (FileOutputStream) resolver
                        .openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, fos);
                Objects.requireNonNull(fos);
                Toast.makeText(this, "fos 작업됨", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private File createFile() {
        // 파일 이름을 만들기 위해 시간값을 생성한다
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = "My" + timestamp;
        // 사진파일을 저장하기 위한 경로
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File curFile = null;
        try {
            // 임시파일을 생성함  1: 파일이름, 2: 확장자이름, 3: 경로
            curFile = File.createTempFile(imageFileName, ".jpg",
                    storageDir);
        } catch (Exception e) {
            e.getMessage();
        }
        // 스트링타입으로 임시파일이 있는 곳의 절대경로를 저장함
        imgFilePath = curFile.getAbsolutePath();

        Log.d(TAG, "createFile: " + imgFilePath);

        return curFile;
    }
        // 위험권한 : 실행시 허용여부를 다시 물어봄
        private void checkDangerousPermissions() {
            String[] permissions = {
                    // 위험권한 내용 : 메니페스트에 권한을 여기에 적음
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.ACCESS_MEDIA_LOCATION,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            int permissionCheck = PackageManager.PERMISSION_GRANTED;
            for (int i = 0; i < permissions.length; i++) {
                permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    break;
                }
            }

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this, permissions, 1);
                }
            }
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == 1) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
