package com.example.stickerapp.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.example.fifastickerapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var isSearching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListeners()
    }

    fun disableSearch() {
        isSearching = false
        setFabMenu()
        checkfIsSearching()
    }


    private fun setListeners() {
        button_menu.setOnClickListener {
            openMenuBottomSheet()
        }
        search_bar.setOnClickListener {
            search()
        }
    }

    private fun search() {
        updateStateSearch()
        checkfIsSearching()
    }

    private fun checkfIsSearching() {
        filter_lost_cards.isVisible = isSearching.not()
        team_progress_stickers.isVisible = isSearching.not()
        search_bar.isVisible = isSearching.not()
        search.isVisible = isSearching
    }

    private fun updateStateSearch() {
        isSearching = !isSearching
    }

    private fun setFabMenu() = with(button_menu){
        setImageResource(R.drawable.ic_menu)
        setOnClickListener { openMenuBottomSheet() }
    }

    private fun openMenuBottomSheet() {
        nav_host_fragment.findNavController().navigate(R.id.open_menu)
    }
}