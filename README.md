# 🎨 Shilpa-Kala

**Shilpa-Kala** is a native Android application designed to bridge the gap between traditional artistry and modern AI technology. Built with Kotlin and Jetpack Compose, this app provides tools for seasoned sculptors and digital sketching enthusiasts to bring their creative visions to life.

---

## 🚀 Getting Started

To run the **Shilpa-Kala** project locally and build it on your device, follow these steps:

### **Prerequisites**
* **Android Studio**: Ensure you have the latest version of Android Studio installed.
* **Android SDK**: API level 24 or higher is recommended.
* **Gemini API Key**: Essential for powering the Generative AI features within the app.

### **Installation Steps**
1.  **Clone the Repository**:
    ```bash
    git clone [https://github.com/your-username/shilpa-kala.git](https://github.com/your-username/shilpa-kala.git)
    ```
2.  **Open in Android Studio**:
    Launch Android Studio, select "Open an existing project", and navigate to the cloned directory.
3.  **Configure Environment**:
    Create a `local.properties` file in the root directory (if it doesn't already exist) and securely add your API key:
    ```properties
    GEMINI_API_KEY=your_api_key_here
    ```
4.  **Sync and Run**:
    Allow Gradle to sync all project dependencies, then click the **Run** button to deploy the app to your emulator or a connected Android device.

---

## 🛠 Core Features

* **Modern UI**: Built entirely using Jetpack Compose for a smooth, reactive, and intuitive user experience.
* **AI-Powered Inspiration**: Integrates Generative AI to provide intelligent prompts and artistic directions directly within the workspace.
* **Creative Tools**: Dedicated modules tailored for exploring both traditional and digital art techniques.

---

## 👨‍💻 Developed By

**Vishwachetan G Panchal**

---

> **Note:** Always keep your `GEMINI_API_KEY` private. Ensure that your `local.properties` file is included in your `.gitignore` to prevent exposing your credentials in public version control.
