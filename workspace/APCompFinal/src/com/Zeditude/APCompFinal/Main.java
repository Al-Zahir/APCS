package com.Zeditude.APCompFinal;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class Main extends Activity {
	GridView gridView;
	Board board;
	Team user = Team.WHITE;
	Team ai = Team.BLACK;

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

				if (p.isSelected())
					selected = p;
			}
		}

		gridView = (GridView) findViewById(R.id.gridView1);

		gridView.setAdapter(new ImageAdapter(this, pieces, selected));

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				int row = (int) (position / 8);
				int col = (int) (position % 8);
				board.select(row, col);
				reDraw();
			}
		});

		if (board.getTurn() == ai) {
			if (!board.isMate(ai)) {
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						board.aiTurn();
						reDraw();
						if (board.isInCheck(user)) {
							if (board.isMate(user))
								Toast.makeText(getApplicationContext(),
										"Check mate " + ai + " wins",
										Toast.LENGTH_LONG).show();
							else
								Toast.makeText(getApplicationContext(),
										user + " is in check",
										Toast.LENGTH_LONG).show();
						} else if (board.isStaleMate())
							Toast.makeText(getApplicationContext(),
									"Stalemate", Toast.LENGTH_LONG).show();
					}
				}, 1000);
			}
		}
	}
}
