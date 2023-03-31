package com.hanul.bteam;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

// 4. 선언한 클래스와 xml 파일을 이용하여 화면을 adapter에서 생성한다
public class BoardrAdapter extends BaseAdapter {
    private static final String TAG = "main:SingerAdapter";
    // 메인에서 넘겨받을것 -> 생성자를 만든다.
    Context context;
    ArrayList<BoardDTO> dtos;

    // 화면을 붙이기 위한 객체 생성
    LayoutInflater inflater;
    AlertDialog dialog;

    public BoardrAdapter(Context context, ArrayList<BoardDTO> dtos) {
        this.context = context;
        this.dtos = dtos;

        this.inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //////////////////////////////////////////////////
    // 매소드를 만들때는 무조건 여기에 생성한다(adapter)
    // 하나의 dto 추가하기 (SingerDTO)
    public void addDto(BoardDTO dto){
        dtos.add(dto);
    }

    // dtos의 모든 내용 지우기
    public void removeDtos(){
        dtos.clear();
    }
    /////////////////////////////////////////////////


    // dtos에 저장된 dto 갯수
    @Override
    public int getCount() {
        return dtos.size();
    }

    // dtos에 특정위치에 있는 dto 가져오기(SingerDTO)
    @Override
    public Object getItem(int position) {
        return dtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 가장 중요함 : 화면을 생성하고 특정뷰에 대한 클릭이벤트를 만들수 있다
    // 만약 화면 5개를 생성한다면 getView가 5번 실행된다
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: " + position);

        BoardViewHolder viewHolder;

        // 캐시된 뷰가 없을 경우 새로 뷰홀더를 생성하고
        // 있으면 그 뷰를 재활용한다
        if(convertView == null){
            // 화면을 붙인다
            convertView = inflater.inflate(R.layout.boardview,
                    parent, false);

            viewHolder = new BoardViewHolder();
            // 붙인 화면과 아래에 생성한 뷰홀더를 연결한다
            viewHolder.ivImage = convertView.findViewById(R.id.ivImage);


            convertView.setTag(viewHolder);
        }else { // 캐시된 뷰가 있을 경우 이미 생성된 뷰홀더를 사용한다
            viewHolder = (BoardViewHolder) convertView.getTag();
        }

        // 선택한 dto 데이터 가져오기
        BoardDTO dto = dtos.get(position);
        int resImage = dto.getResId();

        // 화면에 데이터 연결하기
        viewHolder.ivImage.setImageResource(resImage);

        // 이미지 클릭시 이벤트 걸어주기
        viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,  "선택 : " + position
                        + "\n이름 : " + dtos.get(position).getMountin_name(), Toast.LENGTH_SHORT).show();

                // 이미지뷰를 동적으로 생성해서 해당 이미지 보여줌
                //popUpImg(dtos.get(position).getResId());
               // popUpImgXml(dtos.get(position));
            }
        });



        // 만들어진 뷰를 반환
        return convertView;
    }

    private void popUpImgXml(BoardDTO dto) {
        // 1. res에 xml파일을 만든다
        // 2. 그 xml파일을 inflate 시켜 setView 한다

        // 팝업창에 xml 붙이기
        LayoutInflater inflater = LayoutInflater.from(context); //(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupimg, null);
        // xml에 있는 리소스 찾기
        ImageView imageView = view.findViewById(R.id.imageView);


        // xml에 데이터 연결하기
        imageView.setImageResource(dto.getResId());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("이미지 띄우기").setView(view);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();

    }

    private void popUpImg(int resId) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(resId);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("이미지 띄우기").setView(imageView);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }


    // 3. xml 파일에서 사용된 모든 변수를 adapter에서 클래스로 선언한다
    public class BoardViewHolder{
        public ImageView ivImage;

    }
}
