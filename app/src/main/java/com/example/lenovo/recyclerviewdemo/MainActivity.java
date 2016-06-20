package com.example.lenovo.recyclerviewdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView view;
    List<Content> list;
    RecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    Toolbar toolbar;
    String []image=new String[]{""};
    List<String> imageUrlList=new ArrayList<>();
    ProgressDialog mProgressDialog;
    Handler handler;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>=23){
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            int permsRequestCode = 200;
            ActivityCompat.requestPermissions( MainActivity.this, perms, permsRequestCode);
        }
        else{
            getImage();
        }


        handler=new Handler(){

            public void handleMessage(Message msg){

                switch (msg.what){
                    case 1:
                        mProgressDialog.dismiss();
                        initData();
                        initView();
                }
            }

        };
        showProgressDialog();
    }

    private void initView(){
        view=(RecyclerView)findViewById(R.id.recyclerView);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("My Title");
        toolbar.setSubtitle("Sub title");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add:
                        Content content=new Content();
                        content.setValue("add");
                        adapter.add(0,content);
                        break;
                    case R.id.remove:
                        adapter.remove(0);
                        break;
                }
                return true;
            }
        });
        adapter=new RecyclerAdapter(MainActivity.this, list);
        linearLayoutManager=new LinearLayoutManager(this);
        gridLayoutManager=new GridLayoutManager(this,4);
        view.setLayoutManager(linearLayoutManager);
        //view.setLayoutManager(gridLayoutManager);
        view.setAdapter(adapter);
        view.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemclickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, ""+ position, Toast.LENGTH_SHORT).show();
                adapter.remove(position);
            }
        });
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(view);

    }

    private void initData(){
        list=new ArrayList<Content>();

        for(int i=0;i<imageUrlList.size();i++){
            Content content=new Content();
            content.setValue("test");
           // content.setUrl("http://h.hiphotos.baidu.com/zhidao/pic/item/95eef01f3a292df5dfadcc35bf315c6034a873a7.jpg");
            content.setUrl(imageUrlList.get(i));
            list.add(content);
        }

    }

    private void getImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Cursor cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.MIME_TYPE+"=?or "+
                        MediaStore.Images.Media.MIME_TYPE+"=?",new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);
                if(cursor==null){
                    return;
                }
                int count=0;
                while(cursor.moveToNext()){
                    String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    imageUrlList.add(path);
                    count++;
                    mProgressDialog.setProgress(count);
                    Log.e("picture",path);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(1);
                cursor.close();
            }
        }).start();

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    getImage();
                }

                break;

        }

    }

    private void showProgressDialog() {
        mProgressDialog= new ProgressDialog(MainActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIcon(R.mipmap.ic_launcher);
        mProgressDialog.setTitle("test");
        mProgressDialog.setMessage("loading");
       // mProgressDialog.setCancelable(true);
        mProgressDialog.setMax(1000);
        mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "progressdialg show", Toast.LENGTH_SHORT).show();

            }
        });
        mProgressDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



}
