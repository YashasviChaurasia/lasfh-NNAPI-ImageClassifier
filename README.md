# lasfh

NNAPI CNN Classifier

This Android application demonstrates a convolutional neural network (CNN) classifier implemented using TensorFlow Lite. Using a pre-trained model, it allows users to select an image from their device's gallery and classify it into one of 1001 classes.

![Untitled](lasfh%207a86e207853a4b61928f2f02d16e094c/Untitled.png)

## Setup Instructions

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Build and run the application on an Android device or emulator.

## Usage

Upon launching the application, users will see a button labeled "Select Input Image." When clicked, this button prompts the user to choose an image from their device's gallery. Once an image is selected, the application processes it using the pre-trained model and displays the result, indicating the predicted class and the classification score.

## Components

### `MainActivity`

This is the main activity of the application. It initializes the view model and sets the content using the `MyApp` composable function.

### `MainViewModel`

This ViewModel class holds the application's business logic. It loads the TensorFlow Lite model, processes the selected image, and performs classification. The classification result is exposed as a `StateFlow` to observe changes in the UI.

### `MyApp`

A composable function that sets up the UI using Jetpack Compose. It initializes the navigation controller and displays the `RequestContentPermission` composable function.

### `RequestContentPermission`

A composable function responsible for requesting permission to access the device's storage and handling the image selection process. It utilizes the `ActivityResultContracts.GetContent` contract to launch the image picker.

### Utility Functions

- `classifyImage`: Takes a URI representing the selected image, processes it, and triggers the classification process.
- `resizeBitmap`: Resizes a given bitmap to a specified width and height.
- `toBitmap`: Converts a URI to a Bitmap object, handling compatibility with different Android versions.
- `loadModelFile`: Loads the TensorFlow Lite model file from the assets folder.

## Classify Image and Classify Functions

The `classifyImage` function and `classify` function within the `MainViewModel` class play a crucial role in the image classification process.

### `classifyImage` Function:

The `classifyImage` function orchestrates the image classification workflow. Here's an overview:

1. **Asynchronous Execution**: Leveraging coroutines, the function operates asynchronously to avoid blocking the main thread.
2. **Image Processing**: It converts the selected image URI into a Bitmap object, handling any exceptions that may arise during the process.
3. **Resizing**: The function resizes the image to a standard size of 224x224 pixels, ensuring compatibility with the neural network model.
4. **Classification**: After preprocessing the image, it calls the `classify` function to perform the actual classification.
5. **Result Handling**: Upon successful classification, the result is stored in the `_classificationResult` flow. Exceptions are caught, and the result is set to `null` if any errors occur.

### `classify` Function:

The `classify` function is responsible for running inference on the preprocessed image using the TensorFlow Lite model. Here's a breakdown:

1. **TensorFlow Lite Setup**: It prepares the input image by loading it into a `TensorImage` object compatible with TensorFlow Lite models.
2. **Input Buffer Preparation**: The function creates an input buffer of the required size to hold the image data in the format expected by the model. It then runs inference by passing the input buffer to the TensorFlow Lite interpreter.
3. **Output Processing**: After inference, the function analyzes the output logits vector to determine the predicted class. It identifies the index of the highest probability and extracts the corresponding class label and classification score.
4. **Result Presentation**: Constructing a human-readable string containing the predicted class and classification score, the function returns it to the `classifyImage` function for further handling.

Together, these functions streamline the image classification process, enabling the application to analyze input images and provide accurate classification results efficiently.

### Screenshots

![Untitled](lasfh%207a86e207853a4b61928f2f02d16e094c/Untitled%201.png)

![Untitled](lasfh%207a86e207853a4b61928f2f02d16e094c/Untitled%202.png)

![Untitled](lasfh%207a86e207853a4b61928f2f02d16e094c/Untitled%203.png)

## Dependencies

- AndroidX libraries for UI components and lifecycle management.
- NNAPI based TensorFlow Lite for model loading and inference.

This project utilizes a pre-trained TensorFlow Lite model for image classification and runs it using NNAPI. Credits to the creators and contributors of the model

Github link: https://github.com/YashasviChaurasia/lasfh