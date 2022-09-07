package com.kudu.tagboard.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kudu.tagboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //btn group
        binding.btnGroup.setOnClickListener {
            Toast.makeText(this, "Groups clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, GroupsActivity::class.java))
        }

        //btn settings
        binding.btnSettings.setOnClickListener {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }

}