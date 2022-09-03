package com.example.stickerapp.presenter.sticker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.fifastickerapp.R
import com.example.stickerapp.domain.Team
import com.example.stickerapp.domain.usecases.GetAllTeamsUseCase
import com.example.stickerapp.presenter.MainActivity
import com.example.stickerapp.presenter.sticker.adapter.StickersAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home_sticker.*
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.inject

class HomeStickerFragment : Fragment(R.layout.fragment_home_sticker) {
    private var adapter: StickersAdapter? = null
    private val getAllTeamsUseCase: GetAllTeamsUseCase by inject()
    private lateinit var teams: List<Team>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllTeamsUseCase.invoke().takeWhile { adapter?.itemCount ?: 0 == 0 }.flowWithLifecycle(lifecycle).onEach {
            teams = it
            adapter = StickersAdapter(this@HomeStickerFragment, it)
            sticker_pager.adapter = adapter
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        setListeners()
    }

    private fun setListeners() {
        activity?.findViewById<TextInputEditText>(R.id.search)?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().isEmpty())
                    (activity as MainActivity).disableSearch()
                else
                    with(activity?.findViewById<FloatingActionButton>(R.id.button_menu) ?: return){
                        setImageResource(R.drawable.ic_search)
                        setOnClickListener {
                            filterTeam(p0.toString())
                        }
                    }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun filterTeam(name: String) {
        val teamPosition = teams.firstOrNull { it.name.contains(name, true) }?.position
        teamPosition?.let {
            sticker_pager.currentItem = it - 1
        }
        (activity as MainActivity).disableSearch()
    }
}