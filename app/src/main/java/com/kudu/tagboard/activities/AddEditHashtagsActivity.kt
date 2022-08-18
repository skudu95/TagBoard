package com.kudu.tagboard.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kudu.tagboard.databinding.ActivityAddEditHashtagsBinding

class AddEditHashtagsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditHashtagsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditHashtagsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}