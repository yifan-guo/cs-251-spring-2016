package vandy.mooc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;

/**
 * This helper class encapsulates several static methods that are used
 * to download image files.
 */
public class DownloadUtils {
    /**
     * Used for debugging.
     */
    private final static String TAG = "DownloadUtils";

    /**
     * If you have access to a stable Internet connection for testing
     * purposes, feel free to change this variable to false so it
     * actually downloads the image from a remote server.
     */
    static final boolean DOWNLOAD_OFFLINE = false;

    /**
     * The resource that we write to the file system in offline
     * mode. 
     */
    static final int OFFLINE_TEST_IMAGE = R.raw.dougs;

    /**
     * The file name that we should use to store the image in offline
     * mode.
     */
    static final String OFFLINE_FILENAME = "dougs.jpg";

    /**
     * Download the image located at the provided Internet url using
     * the URL class, store it on the android file system using a
     * FileOutputStream, and return the path to the image file on
     * disk.
     *
     * @param context	the context in which to write the file.
     * @param url       the web url.
     *
     * @return          the absolute path to the downloaded image file on the file system.
     */
    public static Uri downloadImage(Context context,
                                    Uri url) {
        if (!isExternalStorageWritable()) {
            Log.d(TAG,
                  "external storage is not writable");
            return null;
        }

        // If we're offline, open the image in our resources.
        if (DOWNLOAD_OFFLINE) {
            // Get a stream from the image resource.
            try (InputStream inputStream =
                 context.getResources().openRawResource(OFFLINE_TEST_IMAGE)) {
                    // Create an output file and save the image into it.
                    return DownloadUtils.createDirectoryAndSaveFile
                        (context, inputStream, OFFLINE_FILENAME);
            } catch (Exception e) {
                Log.e(TAG,
                      "Exception getting resources."
                      + e.toString());
                return null;
            }
        }
        // Otherwise, download the file requested by the user.
        else {
            Log.d(TAG, "Inside DownloadUtils.java DownloadImage() method");
            // Download the contents at the URL, which should
            // reference an image.
            try (InputStream inputStream =
                 (InputStream) new URL(url.toString()).getContent()) {
                    // Create an output file and save the image into it.
                    return DownloadUtils.createDirectoryAndSaveFile
                        (context, inputStream, url.toString());
             } catch (Exception e) {
                Log.e(TAG,
                      "Exception while downloading. Returning null."
                      + e.toString());
                return null;
            }
        }
    }

    /**
     * Decode an InputStream into a Bitmap and store it in a file on
     * the device.
     *
     * @param context	   the context in which to write the file.
     * @param inputStream  the Input Stream.
     * @param fileName     name of the file.
     *
     * @return          the absolute path to the downloaded image file on the file system.
     */
    private static Uri createDirectoryAndSaveFile(Context context,
                                                  InputStream inputStream,
                                                  String fileName) {
        Log.d(TAG, "Inside createDirectoryAndSaveFile method");
        // Decode the InputStream into a Bitmap image.
        Bitmap imageToSave =
            BitmapFactory.decodeStream(inputStream);

        // Bail out of we get an invalid bitmap.
        if (imageToSave == null) {
            Log.d(TAG,  "Bailed out because of invalid bitmap");
            return null;
        }

        Log.d(TAG, "Creating new directory to save image");
        File directory =
            new File(Environment.getExternalStoragePublicDirectory
                     (Environment.DIRECTORY_DCIM)
                     + "/ImageDir");

        if (!directory.exists()) {
            File newDirectory =
                new File(directory.getAbsolutePath());
            newDirectory.mkdirs();
        }

        File file = new File(directory,
                             getTemporaryFilename(fileName));
        if (file.exists())
            file.delete();
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            imageToSave.compress(Bitmap.CompressFormat.JPEG,
                                 100,
                                 outputStream);
            outputStream.flush();
        } catch (Exception e) {
            // Indicate a failure.
            Log.d(TAG, "image failed to compress to outputStream");
            Log.d(TAG, e.toString());
            return null;
        }

        // Get the absolute path of the image.
        String absolutePathToImage = file.getAbsolutePath();

        // Provide metadata so the downloaded image is viewable in the
        // Gallery.
        ContentValues values =
            new ContentValues();
        values.put(Images.Media.TITLE,
                   fileName);
        values.put(Images.Media.DESCRIPTION,
                   fileName);
        values.put(Images.Media.DATE_TAKEN,
                   System.currentTimeMillis ());
        values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME,
                   file.getName().toLowerCase(Locale.US));
        values.put("_data",
                   absolutePathToImage);

        ContentResolver cr =
            context.getContentResolver();

        // Store the metadata for the image into the Gallery.
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                  values);

        Log.d(TAG,
              "absolute path to image file is "
              + absolutePathToImage);

        return Uri.parse(absolutePathToImage);
    }

    /**
     * This method checks if we can write image to external storage
     *
     * @return true if an image can be written, and false otherwise
     */
    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals
            (Environment.getExternalStorageState());
    }

    /**
     * Create a temporary filename to store the result of a download.
     *
     * @param url Name of the URL.
     * @return String containing the temporary filename.
     */
    static private String getTemporaryFilename(final String url) {
        // This is what you'd normally call to get a unique temporary
        // filename, but for testing purposes we always name the file
        // the same to avoid filling up student phones with numerous
        // files!
        //
        // return Base64.encodeToString((url.toString()
        //                              + System.currentTimeMillis()).getBytes(),
        //                              Base64.NO_WRAP);
        return Base64.encodeToString(url.getBytes(),
                                     Base64.NO_WRAP);
    }

    /**
     * Ensure this class is only used as a utility.
     */
    private DownloadUtils() {
        throw new AssertionError();
    }
}
