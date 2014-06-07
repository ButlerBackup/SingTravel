package dev.blacksheep.trif;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import dev.blacksheep.trif.classes.Utils;

public class PhotoContestActivity extends Activity {
	static final int REQUEST_TAKE_PHOTO = 11111;
	private String mCurrentPhotoPath = null;
	private Uri mCapturedImageURI = null;
	ImageView ivPhoto;
	TextView tvWordCount;
	EditText etPhotoMessage;
	Button bSend;
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	private long fileSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.photo_contest_activity);
		Calendar c = Calendar.getInstance();
		getActionBar().setTitle(String.format(Locale.US, "%tB", c) + "'s Contest!");
		ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
		ivPhoto.setTag(R.drawable.splash);
		etPhotoMessage = (EditText) findViewById(R.id.etPhotoMessage);
		tvWordCount = (TextView) findViewById(R.id.tvWordCount);
		if (new Utils(PhotoContestActivity.this).getPhotoDone()) {
			showPhotoAlreadyTaken();
		}
		etPhotoMessage.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String amt = String.valueOf(200 - s.length());
				tvWordCount.setText(amt + " chars left");
			}
		});
		bSend = (Button) findViewById(R.id.bSend);
		bSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Object tag = ivPhoto.getTag();
				int id = tag == null ? -1 : (int) tag;
				Toast.makeText(PhotoContestActivity.this, "" + id, Toast.LENGTH_SHORT).show();
				if (id == R.drawable.splash) {
					showNoPhotoDialog();
				} else {
					progressBar = new ProgressDialog(v.getContext());
					progressBar.setCancelable(false);
					progressBar.setCanceledOnTouchOutside(false);
					progressBar.setMessage("Uploading Photo..");
					progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressBar.setProgress(0);
					progressBar.setMax(100);
					progressBar.show();
					progressBarStatus = 0;
					fileSize = 0;

					new Thread(new Runnable() {
						public void run() {
							while (progressBarStatus < 100) {
								progressBarStatus = doSomeTasks();
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								progressBarHandler.post(new Runnable() {
									public void run() {
										progressBar.setProgress(progressBarStatus);
									}
								});
							}
							if (progressBarStatus >= 100) {
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								progressBar.dismiss();
								PhotoContestActivity.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										showUploadDoneDialog();
									}
								});
							}
						}
					}).start();
				}
			}
		});
	}

	public int doSomeTasks() {
		while (fileSize <= 1000000) {
			fileSize++;
			if (fileSize == 100000) {
				return 10;
			} else if (fileSize == 200000) {
				return 20;
			} else if (fileSize == 300000) {
				return 30;
			}
		}
		return 100;
	}

	private void showPhotoAlreadyTaken() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Entered already.");
		alert.setMessage("You've already entered into this month's contest!");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		alert.show();
	}

	private void showNoPhotoDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("But..");
		alert.setMessage("First, please take a #selfie! :D");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	private void showUploadDoneDialog() {
		new Utils(PhotoContestActivity.this).setCameraPhotoDone();
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Upload done!");
		alert.setMessage("Successfully uploaded photo! 100points will be added to your points-wallet if you're the winner!");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.photo_contest_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.push_down_in_opposite, R.anim.push_down_out_opposite);
			break;
		case R.id.menu_take_photo:
			takePhoto();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	public String getCurrentPhotoPath() {
		return mCurrentPhotoPath;
	}

	public void setCurrentPhotoPath(String mCurrentPhotoPath) {
		this.mCurrentPhotoPath = mCurrentPhotoPath;
	}

	public Uri getCapturedImageURI() {
		return mCapturedImageURI;
	}

	public void setCapturedImageURI(Uri mCapturedImageURI) {
		this.mCapturedImageURI = mCapturedImageURI;
	}

	private void takePhoto() {
		PackageManager packageManager = PhotoContestActivity.this.getPackageManager();
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
			Toast.makeText(PhotoContestActivity.this, "This device does not have a camera.", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		if (takePictureIntent.resolveActivity(PhotoContestActivity.this.getPackageManager()) != null) {

			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				ex.printStackTrace();
				Toast.makeText(PhotoContestActivity.this, "There was a problem saving the photo...", Toast.LENGTH_SHORT).show();
			}

			if (photoFile != null) {
				Uri fileUri = Uri.fromFile(photoFile);
				setCapturedImageURI(fileUri);
				setCurrentPhotoPath(fileUri.getPath());
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoContestActivity.this.getCapturedImageURI());
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
			addPhotoToGallery();
			setFullImageFromFilePath(getCurrentPhotoPath(), ivPhoto);
			ivPhoto.setTag(12345);
		} else {
			Toast.makeText(PhotoContestActivity.this, "Image Capture Failed", Toast.LENGTH_SHORT).show();
		}
	}

	protected File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);
		setCurrentPhotoPath("file:" + image.getAbsolutePath());
		return image;
	}

	protected void addPhotoToGallery() {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(PhotoContestActivity.this.getCurrentPhotoPath());
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		PhotoContestActivity.this.sendBroadcast(mediaScanIntent);
	}

	private void setFullImageFromFilePath(String imagePath, ImageView imageView) {
		int targetW = imageView.getWidth();
		int targetH = imageView.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
		imageView.setImageBitmap(bitmap);
	}
}
