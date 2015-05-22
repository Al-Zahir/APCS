package com.Zeditude.APCompFinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final Piece[] pieces;
	private final Piece selected;

	public ImageAdapter(Context context, Piece[] pieces, Piece selected) {
		this.context = context;
		this.pieces = pieces;
		this.selected = selected;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.adapter, null);

			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);
			
			ImageView imageView2 = (ImageView) gridView
					.findViewById(R.id.grid_item_image2);
			
			ImageView backGround = (ImageView) gridView
					.findViewById(R.id.grid_item_imageback);

			Piece p = pieces[position];

			switch (p.getType()) {
			case PAWN:
				if (p.getColor() == Team.WHITE)
					imageView.setImageResource(R.drawable.whitepawn);
				else
					imageView.setImageResource(R.drawable.blackpawn);
				break;

			case KNIGHT:
				if (p.getColor() == Team.WHITE)
					imageView.setImageResource(R.drawable.whiteknight);
				else
					imageView.setImageResource(R.drawable.blackknight);
				break;

			case BISHOP:
				if (p.getColor() == Team.WHITE)
					imageView.setImageResource(R.drawable.whitebishop);
				else
					imageView.setImageResource(R.drawable.blackbishop);
				break;

			case ROOK:
				if (p.getColor() == Team.WHITE)
					imageView.setImageResource(R.drawable.whiterook);
				else
					imageView.setImageResource(R.drawable.blackrook);
				break;

			case QUEEN:
				if (p.getColor() == Team.WHITE)
					imageView.setImageResource(R.drawable.whitequeen);
				else
					imageView.setImageResource(R.drawable.blackqueen);
				break;

			case KING:
				if (p.getColor() == Team.WHITE)
					imageView.setImageResource(R.drawable.whiteking);
				else
					imageView.setImageResource(R.drawable.blackking);
				break;
			}
			
			if(selected != null){
				if(selected.canMoveTo((int) (position / 8), (int) (position % 8)))
					imageView2.setImageResource(R.drawable.locate);
				else if(p == selected)
					imageView2.setImageResource(R.drawable.selected);
			}
			
			int row = (int) (position / 8);
			int col = (int) (position % 8);
			
			if(row % 2 == 0){
				if(col % 2 == 0)
					backGround.setImageResource(R.drawable.backblack);
				else
					backGround.setImageResource(R.drawable.backwhite);
			}else{
				if(col % 2 == 0)
					backGround.setImageResource(R.drawable.backwhite);
				else
					backGround.setImageResource(R.drawable.backblack);
			}

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return pieces.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}