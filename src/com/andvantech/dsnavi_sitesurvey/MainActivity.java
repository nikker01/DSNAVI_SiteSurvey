package com.andvantech.dsnavi_sitesurvey;


import com.andvantech.dsnavi_sitesurvey.position.position_1F;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        
	        Button b = (Button)this.findViewById(R.id.buttonObj);
	       
	        b.setOnClickListener( new OnClickListener(){
	            public void onClick(View arg0) {
	                // TODO Auto-generated method stub
	                
	                // �إ� "����ɮ� Action" �� Intent
	                Intent intent = new Intent( Intent.ACTION_PICK );
	                
	                // �L�o�ɮ׮榡
	                intent.setType( "image/*" );
	                
	                // �إ� "�ɮ׿�ܾ�" �� Intent  (�ĤG�ӰѼ�: ��ܾ������D)
	                Intent destIntent = Intent.createChooser( intent, "����ɮ�" );
	                
	                // �������ɮ׿�ܾ� (�����B�z���G, �|Ĳ�o onActivityResult �ƥ�)
	                startActivityForResult( destIntent, 0 );
	            }
	        });
	    }
	    
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        
	        // TODO Auto-generated method stub
	        super.onActivityResult(requestCode, resultCode, data);
	        
	        // ������ɮ�
	        if ( resultCode == RESULT_OK )
	        {
	            // ��o�ɮת� Uri
	            Uri uri = data.getData();
	            if( uri != null )
	            {
	            	Log.v("URI",uri.toString());
	            	Intent intent = new Intent(MainActivity.this, position_1F.class);
	            	intent.putExtra("imageURI",uri.toString());
	            	startActivity(intent);
	                // �Q�� Uri ��� ImageView �Ϥ�
	                /*ImageView iv = (ImageView)this.findViewById(R.id.imageViewObj);
	                iv.setImageURI( uri );
	                
	                setTitle( uri.toString() );*/
	            }
	            else
	            {
	                setTitle("�L�Ī��ɮ׸��| !!");
	            }
	        }
	        else
	        {
	            setTitle("������ɮ� !!");
	        }
	    }
}
