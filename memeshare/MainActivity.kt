package com.tejasrafale1.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyLog.TAG
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    private val url = "https://meme-api.herokuapp.com/gimme"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
    fun loadMeme(){
        // Instantiate the RequestQueue.
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        //val queue = Volley.newRequestQueue(this)
        Log.d(TAG, "loadMeme: $url")


        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val url = response.getString("url")
                val memeImageView = findViewById<ImageView>(R.id.memeImageView)
                Glide.with(this).load(url).listener(object:RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }
                ).into(memeImageView)
            },
            {
                Toast.makeText(this,"There is Error In LOADING",Toast.LENGTH_LONG).show()
            }
        )

        MemeSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        // Add the request to the RequestQueue.
        //queue.add(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey Check Out This Meme \n $url")
        val chooser = Intent.createChooser(intent,"You Can Share This Meme Using ...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }

}