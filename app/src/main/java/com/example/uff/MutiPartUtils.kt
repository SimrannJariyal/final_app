// MultipartUtils.kt
import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

object MultipartUtils {

    /**
     * Creates a MultipartBody.Part for an image file to upload to the server.
     *
     * @param partName The name of the part (key in the form-data).
     * @param imageUri The URI of the image selected by the user.
     * @param context The application context used to access the file system.
     * @return MultipartBody.Part for Retrofit API requests.
     */
    fun createImageFilePart(partName: String, imageUri: Uri, context: Context): MultipartBody.Part {
        // Create a temp file in the cache directory
        val file = File(context.cacheDir, "temp_image.jpg")

        try {
            // Copy content from the URI to the temp file
            context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException("Failed to process the image file: ${e.message}")
        }

        // Prepare the file for multipart upload
        val requestBody = RequestBody.create("image/jpeg".toMediaType(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }
}
