package com.example.stickerapp.presenter.sticker.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.stickerapp.domain.Team
import com.example.stickerapp.presenter.sticker.StickersFragment

class StickersAdapter(fragment: Fragment, private val teams: List<Team>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = teams.size

    override fun createFragment(position: Int): Fragment = StickersFragment().apply {
        arguments = StickersFragment.getBundle(position+1)
    }
}