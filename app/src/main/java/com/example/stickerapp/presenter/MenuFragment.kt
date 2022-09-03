package com.example.stickerapp.presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.fifastickerapp.R
import com.example.stickerapp.domain.usecases.GetLostStickersText
import com.example.stickerapp.presenter.extensions.shareText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MenuFragment : BottomSheetDialogFragment() {
    private val getLostStickersText: GetLostStickersText by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        share.setOnClickListener {
            shareText()
        }
        ic_search_team.setOnClickListener {
            searchTeam()
        }
    }

    private fun shareText() {
        viewLifecycleOwner.lifecycleScope.launch {
            context?.shareText(getLostStickersText.invoke(null))
        }
    }

    private fun searchTeam() {

    }
}