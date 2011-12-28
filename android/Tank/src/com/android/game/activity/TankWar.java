package com.android.game.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.game.api2.Sprite.ActionType;
import com.android.game.view.GameView;

public class TankWar extends Activity {
	
	private GameView gameView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        gameView = (GameView) this.findViewById(R.id.gameView);
      
    }
  
    //各个按钮的单击事件
    public void playerAction(View target){
    	switch(target.getId()){
    	case R.id.btnUp:
    		gameView.playerAction(ActionType.UP);
    		break;
    	}
    }
}