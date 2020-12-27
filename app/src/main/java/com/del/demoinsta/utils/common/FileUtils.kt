package com.del.demoinsta.utils.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.mindorks.paracamera.Camera
import com.mindorks.paracamera.Utils
import java.io.*

/**
 *
 *
 * After receiving the result from gallery/camera via onActivityResult
 * we use this class to tell Para Camera to store the image.
 */
@SuppressLint("LogNotTimber")
object FileUtils {

    private val TAG = FileUtils::class.simpleName + " : ";


    /**
     *
     * provide a dir (named "temp" ) from internal storage.
     * if exists, return or else create dir.
     *
     *
     * Triggered from application module.
     */
    fun getDirectory(context: Context, dirName: String): File {
        Log.d(TAG, "getDirectory: () ")
        //
        val file = File(context.filesDir.path + File.separator + dirName)
        if (!file.exists()) file.mkdir()
        return file
    }

    /**
     *
     * Returns the file saved file uri
     *
     *
     * Saved the original image
     *
     *
     * then again, picks the original image from File and scale down and save again.
     *
     * So, no loading big bitmaps into m/r. rather using a file.
     *
     * @param inputStream
     * @param directory
     * @param imageName
     * @param height
     */
    fun saveInputStreamToFile(inputStream: InputStream, directory: File, imageName: String, height: Int): File? {
        Log.d(TAG, "saveInputStreamToFile: () ")
        //
        val temp = File(directory.path + File.separator + "temp\$file\$for\$processing")
        //
        try {
            // load it from temp
            val fileOutputStream = FileOutputStream(temp)

            // File : the final output uri.
            // pathname gets it from directory.
            val final = File(directory.path + File.separator + imageName + ".${Camera.IMAGE_JPG}")

            // saving the original files
            try {
                //
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int = inputStream.read(buffer)
                //
                while (read != -1) {
                    fileOutputStream.write(buffer, 0, read)
                    read = inputStream.read(buffer)
                }
                //
                fileOutputStream.flush()

                // Now, again get the original and scale down and store.
                // saving in lib.
                // temp file decoded - scaled dowm,
                // final.path
                Utils.saveBitmap(Utils.decodeFile(temp, height), final.path, Camera.IMAGE_JPG, 80)
                //
                return final
            } finally {
                //
                fileOutputStream.close()
                temp.delete()
            }
        } finally {
            //
            inputStream.close()
        }
    }

    /**
     * Get the size of image with loading into m/r
     *
     */
    fun getImageSize(file: File): Pair<Int, Int>? {
        Log.d(TAG, "getImageSize: () ")
        //
        try {
            // Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(FileInputStream(file), null, o)
            return Pair(o.outWidth, o.outHeight)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
    }
}