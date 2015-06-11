package com._98ki.util;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.shape100.gym.Logger;

public final class CameraUtils {

	private static final Logger log = Logger.getLogger("CameraUtils");

	private static String filePath = null;

	private CameraUtils() {
		// can't be instantiated
	}

	public static boolean takePhoto(final Activity activity, final String dir, final String filename, final int cmd) {
		filePath = dir + filename;

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		final File cameraDir = new File(dir);
		if (!cameraDir.exists()) {
			return false;
		}

		final File file = new File(filePath);
		final Uri outputFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		try {
			activity.startActivityForResult(intent, cmd);

		} catch (final ActivityNotFoundException e) {
			return false;
		}
		return true;
	}


	
}
