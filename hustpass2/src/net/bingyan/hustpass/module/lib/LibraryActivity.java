package net.bingyan.hustpass.module.lib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Util;

public class LibraryActivity extends BaseActivity {
	String TAG = "LibraryActivity";
	EditText searchText;
	View searchBtn;

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
        setContentView(R.layout.activity_library_search);
        initView();
	}
	

	private void initView() {
		searchBtn = findViewById(R.id.library_search_btn);
		searchText = (EditText) findViewById(R.id.library_search_text);
		searchBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		if (arg0.getId() == R.id.library_search_btn) {
			String key = searchText.getText().toString().trim();
			if (Util.isEmpty(key))
				return;
			Intent intent = new Intent(this, LibraryListActivity.class);
			intent.putExtra("key", key);
			startActivity(intent);
		}
	}
}
