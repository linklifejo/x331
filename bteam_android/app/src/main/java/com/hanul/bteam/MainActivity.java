package com.hanul.bteam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
//    홈 :       HomeFragment      =>   home_frag
//    검색 :     SearchFragment      =>  search_frag
//    찜 :       WillHaveFragment  =>   willhave_frag
//    나의정보 :  MyInfoFragment =>   myinfo_frag
    public Bundle bundle;
    Toolbar toolbar;
    BottomNavigationView bNaviView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 액션바가 보이지 않게 하기 위하여
        // 먼저 theme에 가서 NoActionBar로 수정한다
        // 내가 만든 툴바를 액션바로 지정한다
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 기존 타이틀 글자가 안보이게 한다
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        // 처음 화면 초기화 : Fragment1로 초기화
        fragmentControl(new HomeFragment());
        // 네비게이션뷰를 찾아서 클릭이벤트를 달아준다
        bNaviView = findViewById(R.id.bottom_navi);
        bNaviView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.tab1:
                        fragmentControl(new HomeFragment());
                        break;
                    case R.id.tab2:
                        fragmentControl(new SearchFragment());
                        break;
                    case R.id.tab3:
                        fragmentControl(new WillHaveFragment());
                        break;
                    case R.id.tab4:
                        fragmentControl(new MyInfoFragment());
                        break;
                }
                return true;
            }
        });
    }
    public String getPathFromUri(Uri uri){

        Cursor cursor = getContentResolver().query(uri, null, null, null, null );

        cursor.moveToNext();

        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );

        cursor.close();

        return path;
    }


    //Path(파일경로) -> Uri
    public Uri getUirFromPath( String filePath) {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data = '" + filePath + "'", null, null);

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
    }
    public void fragmentControl(Fragment f) {

        getSupportFragmentManager().beginTransaction().replace(R.id.contain,f).commit();
    }
    public void fragmentControl(Fragment f, Bundle b) {
        bundle = b;
        getSupportFragmentManager().beginTransaction().replace(R.id.contain,f).commit();
    }

    // 위험권한
    private void checkDangerousPermissions() {
        String[] permissions = {
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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


    public  void BitmapConvertFile(Bitmap bitmap, String strFilePath)
    {
        // 파일 선언 -> 경로는 파라미터에서 받는다
        File file = new File(strFilePath);

        // OutputStream 선언 -> bitmap데이터를 OutputStream에 받아 File에 넣어주는 용도
        OutputStream out = null;
        try {
            // 파일 초기화
            file.createNewFile();

            // OutputStream에 출력될 Stream에 파일을 넣어준다
            out = new FileOutputStream(file);

            // bitmap 압축
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}