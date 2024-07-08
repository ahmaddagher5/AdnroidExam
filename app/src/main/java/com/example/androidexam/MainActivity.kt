package com.example.androidexam

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import androidx.appcompat.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var floatingButton: FloatingActionButton

    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback
    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8,0,8,0)
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)


        // Set up ViewPager (carousel)
        val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3,
            R.drawable.image4)
        viewPager.adapter = ImageAdapter(this, images)
        val dotsImage = Array(images.size) { ImageView(this) }

        dotsImage.forEach {
            it.setImageResource(
                R.drawable.non_active_dot
            )
            tabLayout.addView(it,params)
        }
        pageChangeListener = object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                dotsImage.mapIndexed { index, imageView ->
                    if (position == index){
                        imageView.setImageResource(
                            R.drawable.active_dot
                        )
                    }else{
                        imageView.setImageResource(R.drawable.non_active_dot)
                    }
                }
                super.onPageSelected(position)
            }
        }
        viewPager.registerOnPageChangeCallback(pageChangeListener)

        // default first dot selected
        dotsImage[0].setImageResource(R.drawable.active_dot)
        // Set up RecyclerView (list view)
        val items: List<Item> = listOf(
            Item("Phone", "39393939",  R.drawable.phone),
            Item("Mobile", "39393939", R.drawable.mobile),
            Item("Fax",    "17171717", R.drawable.fax),
            Item("Email",  "example@example.com", R.drawable.email),
            Item("Website", "www.example.com", R.drawable.website),

            Item("Phone (Online)",  "39393939","https://img.icons8.com/?size=50&id=9659&format=png"),
            Item("Mobile (Online)", "39393939","https://img.icons8.com/?size=50&id=11409&format=png"),
            Item("Fax (Online)",  "17171717",  "https://img.icons8.com/?size=50&id=12527&format=png"),
            Item("Email (Online)",  "example@example.com","https://img.icons8.com/?size=50&id=12580&format=png"),
            Item("Website (Online)", "www.example.com","https://cdn-icons-png.freepik.com/256/1006/1006771.png"),
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
        floatingButton = findViewById(R.id.floatingButton)
        floatingButton.setOnClickListener {
            showStatisticsBottomSheet()
        }
    }
    private fun showStatisticsBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, null)

        val itemCountTextView: TextView = view.findViewById(R.id.item_count_text)
        val topCharactersTextView: TextView = view.findViewById(R.id.top_characters_text)

        // Sample data
        val items = listOf("apple", "banana", "cherry", "date", "elderberry", "fig", "grape")

        // Calculate statistics
        val itemCount = items.size
        val topCharacters = getTopCharactersMap(items)

        // Update UI
        itemCountTextView.text = "List 1 ($itemCount items)"
        topCharactersTextView.text = topCharacters.entries.take(3).joinToString("\n") { "${it.key} = ${it.value}" }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun getTopCharactersMap(items: List<String>): MutableMap<Char, Int> {
        val charFrequency = mutableMapOf<Char, Int>()

        // Count character frequencies
        items.forEach { item ->
            item.forEach { char ->
                charFrequency[char] = charFrequency.getOrDefault(char, 0) + 1
            }
        }

        // Sort the map by values in descending order and return a MutableMap
        return charFrequency.entries
            .sortedByDescending { it.value }
            .associate { it.toPair() }
            .toMutableMap()
    }
    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(pageChangeListener)
    }
}