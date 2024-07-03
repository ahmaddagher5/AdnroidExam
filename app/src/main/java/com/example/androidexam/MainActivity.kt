package com.example.androidexam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import androidx.appcompat.widget.SearchView

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        // Set up ViewPager (carousel)
        val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3,
            R.drawable.image4)
        viewPager.adapter = ImageAdapter(this, images)

        // Set up RecyclerView (list view)
        val items: List<Item> = listOf(
            Item("Phone",  R.drawable.phone),
            Item("Mobile", R.drawable.mobile),
            Item("Fax",    R.drawable.fax),
            Item("Email",  R.drawable.email),
            Item("Website", R.drawable.website),

            Item("Phone (Online)",  "https://img.icons8.com/?size=50&id=9659&format=png"),
            Item("Mobile (Online)", "https://img.icons8.com/?size=50&id=11409&format=png"),
            Item("Fax (Online)",    "https://img.icons8.com/?size=50&id=12527&format=png"),
            Item("Email (Online)",  "https://img.icons8.com/?size=50&id=12580&format=png"),
            Item("Website (Online)","https://cdn-icons-png.freepik.com/256/1006/1006771.png"),
        )
        //items
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ItemAdapter(items)

        // Set up SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "Searching for $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter RecyclerView based on search query
                (recyclerView.adapter as ItemAdapter).filter(newText)
                return true
            }
        })
    }
}