/*
 * Copyright (c) 2010, Sony Ericsson Mobile Communication AB. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this 
 *      list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the Sony Ericsson Mobile Communication AB nor the names
 *      of its contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.coffee.util.activity;

import java.util.ArrayList;

import org.coffee.R;
import org.coffee.view.CoffeeGallery;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Test activity to display the list view
 */
public class CoffeeGalleryActivity extends Activity {

	/** The list view */
	private CoffeeGallery mListView;

	/**
	 * Small class that represents a contact
	 */
	private static class Contact {

		public Contact(final String name, final String number) {
		}

	}

	private MyAdapter adapter;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coffee_gallery);

		final ArrayList<Contact> contacts = createContactList(20);
		adapter = new MyAdapter(this, contacts);

		mListView = (CoffeeGallery) findViewById(R.id.my_list);
		mListView.setAdapter(adapter);

		// mListView.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(final AdapterView<?> parent, final View view,
		// final int position, final long id) {
		// final String message = "OnClick: " + contacts.get(position).mName;
		// Toast.makeText(TestActivity.this, message,
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		// mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
		// public boolean onItemLongClick(final AdapterView<?> parent, final
		// View view,
		// final int position, final long id) {
		// final String message = "OnLongClick: " +
		// contacts.get(position).mName;
		// Toast.makeText(TestActivity.this, message,
		// Toast.LENGTH_SHORT).show();
		// return true;
		// }
		// });
	}

	/**
	 * Creates a list of fake contacts
	 * 
	 * @param size
	 *            How many contacts to create
	 * @return A list of fake contacts
	 */
	private ArrayList<Contact> createContactList(final int size) {
		final ArrayList<Contact> contacts = new ArrayList<Contact>();
		for (int i = 0; i < size; i++) {
			contacts.add(new Contact("Contact Number " + i, "+46(0)"
					+ (int) (1000000 + 9000000 * Math.random())));
		}
		return contacts;
	}

	/**
	 * Adapter class to use for the list
	 */
	private static class MyAdapter extends BaseAdapter {

		/** Re-usable contact image drawable */
		private final Drawable contactImage;
		private Activity context;
		private ArrayList<Contact> contacts;

		/**
		 * Constructor
		 * 
		 * @param context
		 *            The context
		 * @param contacts
		 *            The list of contacts
		 */
		public MyAdapter(final Activity context,
				final ArrayList<Contact> contacts) {
			this.context = context;
			contactImage = context.getResources().getDrawable(
					R.drawable.contact_image);
			this.contacts = contacts;
		}

		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = LayoutInflater.from(context).inflate(R.layout.coffee_gallery_item,
						parent, false );
			}

			final TextView name = (TextView) view
					.findViewById(R.id.contact_name);
			if (position == 14) {
				name.setText("This is a long text that will make this box big. "
						+ "Really big. Bigger than all the other boxes. Biggest of them all.");
			} else {
				name.setText(position + "");
			}

			final TextView number = (TextView) view
					.findViewById(R.id.contact_number);
			number.setText(position + "");

			final ImageView photo = (ImageView) view
					.findViewById(R.id.contact_photo);
			photo.setImageDrawable(contactImage);
			return view;
		}

		public int getCount() {
			return contacts.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}
	}

	private int position = 0;

	public void click(View onclick) {
//		final ArrayList<Contact> contacts = createContactList(position+1);
//		adapter = new MyAdapter(this, contacts);
//		mListView.setAdapter(adapter);
		if (position++ > adapter.getCount()) {
			position = 0;
		}
		mListView.setSelection(position);
	}
}
