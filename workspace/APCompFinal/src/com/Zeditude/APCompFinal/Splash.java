package com.Zeditude.APCompFinal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class Splash extends Activity {

	GridView gridView;
	Board board;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board);

		board = new Board(this, 8, 8);
		
		reDraw();
	}

	public void reDraw() {
		Piece[] pieces = new Piece[64];
		Piece selected = null;
		int index = 0;

		for (int r = 0; r < board.getRow(); r++) {
			for (int c = 0; c < board.getCol(); c++) {
				Piece p = board.getPiece(r, c);
				pieces[index] = p;
				index++;
				
				if(p.isSelected())
					selected = p;
			}
		}

		gridView = (GridView) findViewById(R.id.gridView1);

		gridView.setAdapter(new ImageAdapter(this, pieces, selected));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				board.select((int) (position / 8), (int) (position % 8));
				reDraw();

			}
		});
	}
}