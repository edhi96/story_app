package tia.sarwoedhi.storyapp.ui.add_story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import tia.sarwoedhi.storyapp.R
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.databinding.ActivityAddStoryBinding
import tia.sarwoedhi.storyapp.ui.main.MainActivity
import tia.sarwoedhi.storyapp.utils.FileFunction
import tia.sarwoedhi.storyapp.utils.UiState
import tia.sarwoedhi.storyapp.utils.Utils
import java.io.File

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val addViewModel: AddStoryViewModel by viewModels()
    private lateinit var currentPhotoPath: String
    private var photoFile: File? = null
    private lateinit var imageMultipart: MultipartBody.Part
    private lateinit var description: RequestBody
    private lateinit var latitudeRequestBody: RequestBody
    private lateinit var longitudeRequestBody: RequestBody
    private var descriptionText = ""

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.add_story)

        binding.btnAddCamera.setOnClickListener { startTakePhoto() }
        binding.btnAddGallery.setOnClickListener { startGallery() }
        addStory()
        binding.btnUpload.setOnClickListener {
            addStory()
        }
        getLatestLocation()
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
                descriptionText = p0.toString()
                setMyButtonEnable(descriptionText)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.btnUpload.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                convertFile()
                if (photoFile != null && imageMultipart.body.contentType() != null) {
                    addViewModel.addStory(imageMultipart, description, latitude = latitudeRequestBody, longitude = longitudeRequestBody)

                    addViewModel.addState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                        .onEach { addResponse(it) }
                        .launchIn(lifecycleScope)
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
        val descriptionText = descriptionText
        description = descriptionText.toRequestBody("text/plain".toMediaType())
        latitudeRequestBody = latitude.toString().toRequestBody("text/plain".toMediaType())
        longitudeRequestBody = longitude.toString().toRequestBody("text/plain".toMediaType())
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
        val valid = description.isNotEmpty() && photoFile != null
        if (valid) convertFile()
        binding.btnUpload.isEnabled = valid
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            photoFile = myFile
            setMyButtonEnable(descriptionText)
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
            setMyButtonEnable(descriptionText)
            binding.ivPreview.setImageURI(selectedImg)
        }
    }

    private fun getLatestLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        latitude = it.latitude
                        longitude = it.longitude

                        Utils.getAddressName(applicationContext, it.latitude, it.longitude)
                            ?.let { addr ->
                                binding.locationEditText.setText(addr)
                            }
                    }
                }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLatestLocation()
            }
        }
}