package com.example.pruebretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.pruebretrofit.API.retrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var btnDetect:Button
    private lateinit var etDescription:EditText
    private lateinit var progressbar:ProgressBar

    var allLenguages = emptyList<Lenguage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initListener()
        getLenguages()
    }

    private fun initListener() {
        btnDetect.setOnClickListener {
            val text = etDescription.text.toString()
            if (text.isNotEmpty()){
                showloading()
                getTextLanguage(text)
            }
        }
    }


    private fun getTextLanguage(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = retrofitService.getTextLanguage(text)
            if (result.isSuccessful){
                checkResult(result.body())
            }else{
                showError()
            }
            cleanText()
            hideLoading()
        }
    }

    private fun cleanText() {
        etDescription.setText("")
    }

    private fun showloading() {
        progressbar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        runOnUiThread {
            progressbar.visibility = View.GONE
        }

    }

    private fun checkResult(detectionResponse: DetectionResponse?) {
        if (detectionResponse != null && !detectionResponse.data.detections.isNullOrEmpty()){
            val correctLanguages = detectionResponse.data.detections.filter { it.isReliable }
            if (correctLanguages.isNotEmpty()){

                val languageName = allLenguages.find { it.code == correctLanguages.first().language }

                if (languageName != null){
                    runOnUiThread{
                        Toast.makeText(this,"El lenguaje es ${languageName.name}", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun initView() {
        btnDetect =  findViewById(R.id.btnDetect)
        etDescription =  findViewById(R.id.etDescription)
        progressbar = findViewById(R.id.progressbar)
    }

    private fun getLenguages() {
        CoroutineScope(Dispatchers.IO).launch {
            val lenguages = retrofitService.getLenguages()

            if (lenguages.isSuccessful){
                allLenguages = lenguages.body() ?: emptyList()
                showSuccess()
            }else{
                showError()
            }
        }

    }

    private fun showSuccess() {
        runOnUiThread {
            Toast.makeText(this,"Peticion correcta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError() {
        runOnUiThread {
            Toast.makeText(this,"Error al hacer la llamada", Toast.LENGTH_SHORT).show()
        }
    }
}