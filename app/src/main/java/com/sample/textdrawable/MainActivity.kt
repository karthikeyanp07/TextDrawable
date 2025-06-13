package com.sample.textdrawable

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.textdrawable.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private val mockUsers = listOf(
        User(1, "Alice Johnson", ""),
        User(2, "Bob Smith", ""),
        User(3, "Charlie Ray", ""),
        User(4, "Dana White", ""),
        User(5, "Ethan Brown", ""),
        User(6, "Nathan Brown Jille", ""),
        User(7, "Brown Taylor", ""),
        User(8, "Ross", ""),
        User(9, "Chapman Blue", ""),
        User(10, "Mark Wood", ""),
        User(11, "Maddy Green", ""),
        User(12, "Louis Phil", ""),
    )

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = UserAdapter(mockUsers)
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerView.adapter = adapter
    }
}