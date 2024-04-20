package com.example.lasfh
//
//import android.graphics.Bitmap
//import android.graphics.ImageDecoder
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.provider.MediaStore
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.lasfh.ui.theme.LasfhTheme
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.Interpreter
//import org.tensorflow.lite.support.image.TensorImage
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import java.io.FileInputStream
//import java.io.IOException
//import java.nio.MappedByteBuffer
//import java.nio.channels.FileChannel
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            LasfhTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    RequestContentPermission()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun RequestContentPermission() {
//    var imageUri by remember {
//        mutableStateOf<Uri?>(null)
//    }
//    val context = LocalContext.current
//    val bitmap = remember {
//        mutableStateOf<Bitmap?>(null)
//    }
//    var classificationResult by remember { mutableStateOf<String?>(null) }
//
//    val launcher = rememberLauncherForActivityResult(contract =
//    ActivityResultContracts.GetContent()) { uri: Uri? ->
//        imageUri = uri
//    }
//    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//        Button(onClick = {
//            launcher.launch("image/*")
//        }) {
//            Text(text = "Select Input")
//        }
//
//        imageUri?.let {
//            if (Build.VERSION.SDK_INT < 28) {
//                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
//            } else {
//                val source = ImageDecoder.createSource(context.contentResolver, it)
//                bitmap.value = ImageDecoder.decodeBitmap(source)
//            }
//
//            bitmap.value?.let { btm ->
//                Image(bitmap = btm.asImageBitmap(),
//                    contentDescription = null,
//                    modifier = Modifier.size(400.dp))
//
//                Button(onClick = {
//                    classificationResult = classifyImage(context, btm)
//                }) {
//                    Text("Classify Image")
//                }
//
//                classificationResult?.let { result ->
//                    Text(text = "Classification Result: $result")
//                }
//            }
//        }
//    }
//}
//
//fun classifyImage(context: android.content.Context, image: Bitmap): String {
//    // Convert the input image to ARGB_8888 format
//    val argbBitmap = image.copy(Bitmap.Config.ARGB_8888, true)
//
//    // Load the TensorFlow Lite model
//    val interpreter = loadModelFile(context)?.let { Interpreter(it) }
//
//    // Preprocess the image
//    val tensorImage = TensorImage(DataType.UINT8)
//    tensorImage.load(argbBitmap)
//
//    // Create a container for the output
//    val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, NUM_CLASSES), DataType.FLOAT32)
//
//    // Run inference
//    if (interpreter != null) {
//        interpreter.run(tensorImage.buffer, outputBuffer.buffer)
//    }
//
//    // Postprocess the output
//    val resultsArray = outputBuffer.floatArray
//    val maxIndex = resultsArray.indices.maxByOrNull { resultsArray[it] } ?: 0
//    return "Class ${maxIndex + 1}"
//}
//
//
//fun loadModelFile(context: android.content.Context): MappedByteBuffer? {
//    try {
//        val modelFileDescriptor = context.assets.openFd("model.tflite")
//        val inputStream = FileInputStream(modelFileDescriptor.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = modelFileDescriptor.startOffset
//        val declaredLength = modelFileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    } catch (e: IOException) {
//        e.printStackTrace()
//        return null
//    }
//}
//
//
//const val NUM_CLASSES = 1000 // Number of classes in the model

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Pass the application context to MainViewModel
            MyApp(mainViewModel)
        }
    }
}


class MainViewModel(application: Application) : ViewModel() {
    val context = application.applicationContext
    private val interpreter: Interpreter by lazy {
        loadModelFile(context)?.let { Interpreter(it) } ?: throw IllegalStateException("Model could not be loaded")
    }


    private val _classificationResult = MutableStateFlow<String?>(null)
    val classificationResult: StateFlow<String?> = _classificationResult

    fun classifyImage(imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bitmap = imageUri.toBitmap(context.contentResolver)
                val resizedBitmap = resizeBitmap(bitmap, 224, 224) // Resize to 224x224
                val classificationResult = classify(resizedBitmap)
                _classificationResult.value = classificationResult
            } catch (e: Exception) {
                e.printStackTrace()
                _classificationResult.value = null
            }
        }
    }
    private fun classify(bitmap: Bitmap): String {
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        interpreter.run(tensorImage.buffer, inputBuffer.buffer)

        // Process the output logits-vector
        val outputBuffer = inputBuffer.floatArray
        val maxIndex = getMaxIndex(outputBuffer)
        return "Predicted class: $maxIndex"
    }

    private fun getMaxIndex(outputBuffer: FloatArray): Int {
        var maxIndex = 0
        var maxProbability = outputBuffer[0]

        for (i in 1 until outputBuffer.size) {
            if (outputBuffer[i] > maxProbability) {
                maxProbability = outputBuffer[i]
                maxIndex = i
            }
        }

        return maxIndex
    }



    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }



    companion object {
        private const val INPUT_SIZE = 224
        private const val INPUT_WIDTH = 224
        private const val INPUT_HEIGHT = 224

    }
}


@Composable
fun MyApp(mainViewModel: MainViewModel = viewModel()) {
    rememberNavController()
    Surface() {
        RequestContentPermission(mainViewModel)
    }
}

@Composable
fun RequestContentPermission(mainViewModel: MainViewModel) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var classificationResult by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = uri
            mainViewModel.classifyImage(uri)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.dog),
//            contentDescription = null,
//        )
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = "Select Input")
        }

        imageUri?.let { uri ->
            val bitmap = uri.toBitmap(LocalContext.current.contentResolver)
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(400.dp)
            )

            Button(onClick = {
                mainViewModel.classifyImage(uri)
            }) {
                Text("Classify Image")
            }

            val resultState by mainViewModel.classificationResult.collectAsState()
            resultState?.let { result ->
                Text(text = "Classification Result: $result")
            }
        }
    }
}


@Throws(IOException::class)
fun Uri.toBitmap(contentResolver: ContentResolver): Bitmap {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(contentResolver, this)
    } else {
        val source = ImageDecoder.createSource(contentResolver, this)
        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            decoder.isMutableRequired = true
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }
    }.copy(Bitmap.Config.ARGB_8888, true)
}




fun loadModelFile(context: Context): MappedByteBuffer? {
    return try {
        val modelFileDescriptor = context.assets.openFd("1.tflite")
        val inputStream = FileInputStream(modelFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = modelFileDescriptor.startOffset
        val declaredLength = modelFileDescriptor.declaredLength
        fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

