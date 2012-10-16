/*
 ===========================================================================
 Copyright (c) 2012 Three Pillar Global Inc. http://threepillarglobal.com

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sub-license, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ===========================================================================
 */

package org.brickred.socialshare;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageGallery extends Activity {
	/** Called when the activity is first created. */
	private Cursor imagecursor, actualimagecursor;
	private int image_column_index, actual_image_column_index;
	public final static int DISPLAYWIDTH = 200;
	public final static int DISPLAYHEIGHT = 200;
	GridView imagegrid;
	private int count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		init_phone_image_grid();
	}

	private void init_phone_image_grid() {
		String[] img = { MediaStore.Images.Thumbnails._ID };
		imagecursor = managedQuery(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, img, null,
				null, MediaStore.Images.Thumbnails.IMAGE_ID + "");
		image_column_index = imagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
		count = imagecursor.getCount();

		imagegrid = (GridView) findViewById(R.id.GridView);
		imagegrid.setAdapter(new ImageAdapter(getApplicationContext()));

		imagegrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {
				System.gc();
				String[] proj = { MediaStore.Images.Media.DATA };
				actualimagecursor = managedQuery(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
						null, null, null);
				actual_image_column_index = actualimagecursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				actualimagecursor.moveToPosition(position);
				String i = actualimagecursor
						.getString(actual_image_column_index);

				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inJustDecodeBounds = true;

				Bitmap bmp = BitmapFactory.decodeFile(i, bmpFactoryOptions);
				int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
						/ (float) DISPLAYHEIGHT);
				int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
						/ (float) DISPLAYWIDTH);

				// If both of the ratios are greater than 1, one of the sides of
				// the image is greater than the screen
				if (heightRatio > 1 && widthRatio > 1) {
					if (heightRatio > widthRatio) {
						// Height ratio is larger, scale according to it
						bmpFactoryOptions.inSampleSize = heightRatio;
					} else {
						// Width ratio is larger, scale according to it
						bmpFactoryOptions.inSampleSize = widthRatio;
					}
				}

				// Decode it for real
				bmpFactoryOptions.inJustDecodeBounds = false;
				ShareButtonActivity.bitmap = BitmapFactory.decodeFile(i,
						bmpFactoryOptions);

				finish();
			}
		});
	}

	public static byte[] convertToByteArray(Bitmap bmp) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 0, bos);
		byte[] bitmapdata = bos.toByteArray();
		return bitmapdata;
	}

	public class ImageAdapter extends BaseAdapter {
		private final Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			System.gc();
			ImageView i = new ImageView(mContext.getApplicationContext());
			if (convertView == null) {
				imagecursor.moveToPosition(position);
				int id = imagecursor.getInt(image_column_index);
				i.setImageURI(Uri.withAppendedPath(
						MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, ""
								+ id));
				i.setScaleType(ImageView.ScaleType.CENTER_CROP);
				i.setLayoutParams(new GridView.LayoutParams(92, 92));
			} else {
				i = (ImageView) convertView;
			}
			return i;
		}
	}
}
