package tia.sarwoedhi.storyapp.ui.add_story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.databinding.ActivityAddStoryBinding
import tia.sarwoedhi.storyapp.ui.main.MainActivity
import tia.sarwoedhi.storyapp.utils.FileFunction
import tia.sarwoedhi.storyapp.utils.UiState
import java.io.File

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val addViewModel: AddStoryViewModel by viewModels()
    private lateinit var currentPhotoPath: String
    private var photoFile: File? = null
    private lateinit var imageMultipart: MultipartBody.Part
    private lateinit var description: RequestBody
    private var descriptionTeks = ""

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Story"

        binding.btnAddCamera.setOnClickListener { startTakePhoto() }
        binding.btnAddGallery.setOnClickListener { startGallery() }
        addStory()
        binding.btnUpload.setOnClickListener {
            addStory()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Do not get permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun addResponse(resource: UiState<BaseResponse>?) {
        when (resource) {
            is UiState.Loading -> {
                showLoading(true)
            }
            is UiState.Success -> {
                showLoading(false)
                Toast.makeText(this@AddStoryActivity, resource.data?.message, Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            is UiState.Error -> {
                showLoading(false)
                Toast.makeText(this, resource.error, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun addStory() {
        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                descriptionTeks = p0.toString()
                setMyButtonEnable(descriptionTeks)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.btnUpload.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                convertFile()
                if (photoFile != null && imageMultipart.body.contentType() != null) {
                    addViewModel.addStory(imageMultipart, description)
                        .observe(this@AddStoryActivity, ::addResponse)
                } else {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Please insert the picture",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
    }

    private fun convertFile() {
        val file = FileFunction.reduceFileImage(photoFile as File)
        val descriptionText = descriptionTeks
        description = descriptionText.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        FileFunction.createTempFile(this@AddStoryActivity).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "tia.sarwoedhi.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }


    private fun setMyButtonEnable(description: String) {
        convertFile()
        binding.btnUpload.isEnabled = description.isNotEmpty() && photoFile != null
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            photoFile = myFile
            setMyButtonEnable(descriptionTeks)
            val result = BitmapFactory.decodeFile(photoFile?.path)
            binding.ivPreview.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = FileFunction.uriToFile(selectedImg, this@AddStoryActivity)
            photoFile = myFile
            setMyButtonEnable(descriptionTeks)
            binding.ivPreview.setImageURI(selectedImg)
        }
    }

}