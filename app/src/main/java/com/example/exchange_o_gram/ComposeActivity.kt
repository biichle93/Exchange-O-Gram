package com.example.exchange_o_gram

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ComposeActivity : AppCompatActivity() {

    val APP_TAG = "Exchangeogram"
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1
    val photoFileName = "photo_resized.jpg"
    var photoFile: File? = null
    lateinit var resizedFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)
        listeners()
    }
    companion object{
        const val TAG = "ComposeActivity"
    }
    fun listeners(){
        //grab references to things we're listening to
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnCamera = findViewById<Button>(R.id.btnCamera)
        val btnImageSearch = findViewById<Button>(R.id.btnCameraRoll)
        val btnSubmit = findViewById<Button>(R.id.btnProceed)

        //set listeners to the referenced entities
        btnCancel.setOnClickListener(){
            finish()
        }
        btnCamera.setOnClickListener(){
            onLaunchCamera()
        }
        btnSubmit.setOnClickListener(){
            if(photoFile != null){
                submitPost()
            }
        }
    }
    fun submitPost(){
        val description = findViewById<EditText>(R.id.etDescription).text.toString()
        val user = ParseUser.getCurrentUser()
        user.add("description", description)
        user.add("user", user)
        val post = Post()
        post.setUser(user)
        post.setDescription(description)
        post.setImage(ParseFile(resizedFile))
        post.saveInBackground{exception ->
            if(exception != null){
                Log.e(TAG, "error while posting: $exception")
            }else{
                Toast.makeText(this,"Posted Successfully", Toast.LENGTH_SHORT).show()
                val text = findViewById<EditText>(R.id.etDescription)
                text.setText("")
            }
        }
    }
    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                val resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 512)
                // Load the taken image into a preview
                // Configure byte output stream
                val bytes = ByteArrayOutputStream()
                // Compress the image further
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
                // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                resizedFile = getPhotoFileUri(photoFileName)
                resizedFile.createNewFile()
                val fos = FileOutputStream(resizedFile)
                fos.write(bytes.toByteArray())
                fos.close()

                val ivPreview = findViewById<ImageView>(R.id.ivPreview)
                ivPreview.setImageBitmap(rotateBitmapOrientation(resizedFile.path))
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun rotateBitmapOrientation(photoFilePath: String): Bitmap? {
        // Create and configure BitmapFactory
        val bounds = BitmapFactory.Options()
        bounds.inJustDecodeBounds = true
        BitmapFactory.decodeFile(photoFilePath, bounds)
        val opts = BitmapFactory.Options()
        val bm = BitmapFactory.decodeFile(photoFilePath, opts)
        // Read EXIF Data
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(photoFilePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientString = exif?.getAttribute(ExifInterface.TAG_ORIENTATION)
        val orientation =
            orientString?.toInt() ?: ExifInterface.ORIENTATION_ROTATE_90
        var rotationAngle = 0
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270
        // Rotate Bitmap
        val matrix = Matrix()
        matrix.postRotate(90f)
        // Return result
        return Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
    }

}