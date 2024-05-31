package com.tourbuddy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tourbuddy.api.DestinationResponseItem
import com.tourbuddy.databinding.ActivityDetailBinding
import com.tourbuddy.ui.Main.MainActivity

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val destination = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<DestinationResponseItem>("key_destination", DestinationResponseItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<DestinationResponseItem>("key_destination")
        }

        Glide.with(this)
            .load(destination?.imageUrl)
            .into(binding.ivPhoto)

        val bundle = Bundle()
        bundle.putParcelable("key_destination", destination)

        val fragmentManager = supportFragmentManager
        val detailFragment = DetailFragment()
        detailFragment.arguments = bundle
        val fragment = fragmentManager.findFragmentByTag(DetailFragment::class.java.simpleName)
        if (fragment !is DetailFragment) {
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, detailFragment, DetailFragment::class.java.simpleName)
                .commit()
        }

        binding.buttonBack.setOnClickListener{
            startActivity(Intent(this@DetailActivity, MainActivity::class.java))
            finish()
        }

        //todo bookmark story
        binding.buttonBookmark.setOnClickListener{

        }
    }
}