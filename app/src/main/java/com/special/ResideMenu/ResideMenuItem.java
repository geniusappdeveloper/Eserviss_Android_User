package com.special.ResideMenu;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.egnyt.eserviss.R;

/**
 * User: special
 * Date: 13-12-10
 * Time: 下午11:05
 * Mail: specialcyci@gmail.com
 */
public class ResideMenuItem extends LinearLayout{

    /** menu item  icon  */
    //private ImageView iv_icon;
    /** menu item  title */
   //private TextView tv_title;
    
    private Button menu_button;
    public static View view;
    
    public interface ClickableMenu_Item{
    	
    	void mi_Click(View v);
    }

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(final Context context, int icon, String title,final ClickableMenu_Item callback,final int tagId) 
    {
        super(context);
        initViews(context);
        //iv_icon.setImageResource(icon);
        //tv_title.setText(title);
        menu_button.setBackgroundResource(icon);
        menu_button.setText(title);
        
        menu_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(context, "Home",Toast.LENGTH_SHORT).show();
				v.setTag(tagId);
				//Log.i("", "ResideMenuItem setOnClickListener");
				callback.mi_Click(v);
			}
		});
    }

    private int initViews(Context context)
    {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view =  inflater.inflate(R.layout.residemenu_item, this);
        menu_button = (Button) findViewById(R.id.button_menu_item);
        //==============================My change April========================================
        Typeface roboto_condensed = Typeface.createFromAsset(context.getAssets(),"fonts/BebasNeue.otf");
        menu_button.setTypeface(roboto_condensed);

        //iv_icon = (ImageView) findViewById(R.id.iv_icon);
        //tv_title = (TextView) findViewById(R.id.tv_title);
        return R.id.button_menu_item;
    }
    

    /**
     * set the icon color;
     *
     * @param icon
     */
    public void setIcon(int icon){
        //iv_icon.setImageResource(icon);
        menu_button.setBackgroundResource(icon);
    }

    /**
     * set the title with resource
     * ;
     * @param title
     */
    public void setTitle(int title){
       // tv_title.setText(title);
       // menu_button.setBackgroundResource(icon);
        menu_button.setText(title);
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title){
        //tv_title.setText(title);
       // menu_button.setBackgroundResource(icon);
        menu_button.setText(title);
    }
    
    public View getView()
    {
    	return view;
    }
}
