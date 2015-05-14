package com.Zeditude.APCompFinal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Splash extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		GameView gv = new GameView(this);
		gv.setGravity(Gravity.CENTER);
		gv.setNumColumns(8);
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.splashLayout);
		ll.addView(gv);
	}
}

class GameView extends GridView {

	Context context;
	Board board;

	public GameView(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub

		initilize();
	}

	public GameView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.context = context;
		// TODO Auto-generated constructor stub

		initilize();
	}

	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		// TODO Auto-generated constructor stub

		initilize();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub

		initilize();
	}

	public void initilize() {
		board = new Board(context, 8, 8);
		
		String[] pieces = new String[64];
		int index = 0;
		
		for(int r = 0; r < board.getRow(); r++){
			for(int c = 0; c < board.getCol(); c++){
				Piece p = board.getPiece(r, c);
				String s = "Y";
				
				switch (p.getType()) {
				case PAWN:
					s = "P";
					break;

				case KNIGHT:
					s = "K";
					break;

				case BISHOP:
					s = "B";
					break;

				case ROOK:
					s = "R";
					break;

				case QUEEN:
					s = "Q";
					break;

				case KING:
					s = "M";
					break;
				}
				
				pieces[index] = s;
				index++;
			}
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, 
				android.R.layout.simple_list_item_1, pieces);
 
		this.setAdapter(adapter);
 
		this.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				board.select((int)(position / 8), (int)(position % 8));
			}
		});
	}

}