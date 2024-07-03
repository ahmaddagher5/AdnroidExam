package com.example.androidexam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ItemAdapter(private val items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var filteredItems: List<Item> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(filteredItems[position].imageUrl.isNullOrBlank()){
            holder.bind(filteredItems[position].text, filteredItems[position].imageResourceId)
        }else {
            filteredItems[position].imageUrl?.let { holder.bind(filteredItems[position].text, it) }
        }
    }

    override fun getItemCount(): Int = filteredItems.size

    fun filter(query: String?) {
        filteredItems = if (query.isNullOrEmpty()) {
            items
        } else {
            items.filter { it.text.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String, image: String) {
            textView.text = text
            //imageView.image = image

            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = fetchImage(image)
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
            }
        }
        fun bind(text: String, image: Int?) {
            textView.text = text
            if (image != null) {
                imageView.setImageResource(image)
            }
        }

        private suspend fun fetchImage(url: String): Bitmap? {
            return withContext(Dispatchers.IO) {
                try {
                    val urlConnection = URL(url).openConnection() as HttpURLConnection
                    urlConnection.doInput = true
                    urlConnection.connect()
                    val inputStream: InputStream = urlConnection.inputStream
                    BitmapFactory.decodeStream(inputStream)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }

}