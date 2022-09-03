package com.example.stickerapp.presenter.sticker

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fifastickerapp.R
import com.example.stickerapp.domain.Team
import com.example.stickerapp.presenter.extensions.saveImage
import com.example.stickerapp.presenter.sticker.adapter.StickerItemAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.android.synthetic.main.fragment_stickers_page.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class StickersFragment : Fragment(R.layout.fragment_stickers_page) {
    companion object {
        private const val TEAM_BUNDLE = "team_bundle_arg"

        fun getBundle(teamPosition: Int) = bundleOf(TEAM_BUNDLE to teamPosition)
    }

    private lateinit var adapter: StickerItemAdapter
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { saveImageLocally(it) }
        }
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                selectImageFromGallery()
        }

    private val teamPosition by lazy {
        arguments?.getInt(TEAM_BUNDLE) ?: -1
    }
    private val viewModel: StickersViewModel by viewModel()
    private var stickerSelected: Pair<Int, Boolean> = 0 to false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectState()
        activity?.findViewById<ImageView>(R.id.filter_lost_cards)?.setOnClickListener {
            viewModel.event(StickersViewModel.StickersEvents.FilterLostCards)
        }

        viewModel.event(StickersViewModel.StickersEvents.GetTeam(teamPosition))
    }

    override fun onResume() {
        super.onResume()
        arguments?.let {
            updateName(it.getString("name") ?: return@let)
            updateProgress(it.getDouble("progress"))
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED).collectLatest {
                parseState(it)
            }
        }
    }

    private fun parseState(state: StickersViewModel.StickersState) = when (state) {
        is StickersViewModel.StickersState.FetchTeam -> updateTeam(state.team)
        StickersViewModel.StickersState.IDLE -> {}
        is StickersViewModel.StickersState.FetchStickers -> updateStickers(state.team)
        is StickersViewModel.StickersState.FilterStickers -> updateStickersFiltered(state.team)
    }

    private fun updateStickersFiltered(team: Team) {
        adapter.submitList(team.stickers.map { sticker ->
            sticker to team.images.find { sticker.first == it.first }?.second
        })
        updateProgress(Team.progressOfStickersCollection(team.stickers))
    }

    private fun updateTeam(team: Team) = with(team) {
        arguments?.putString("name", team.name)
        updateName(name)
        adapter = StickerItemAdapter(prefix, ::onStickerClicked, ::addImageFromGallery)
        stickers_recycler_view.adapter = adapter
        stickers_recycler_view.layoutManager = GridLayoutManager(requireContext(), 3)
        observeSticker()
    }

    private fun updateName(name: String) {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.title = name
    }

    private fun updateStickers(team: Team) {
        adapter.submitList(team.stickers.map { sticker ->
            sticker to team.images.find { sticker.first == it.first }?.second
        })
        updateProgress(
            Team.progressOfStickersCollection(team.stickers)
                .also { arguments?.putDouble("progress", it) })
    }

    private fun observeSticker() {
        viewModel.event(StickersViewModel.StickersEvents.GetStickers(teamPosition))
    }

    private fun updateProgress(progress: Double) {
        activity?.findViewById<CircularProgressIndicator>(R.id.team_progress_stickers)?.progress =
            progress.toInt()
    }

    private fun onStickerClicked(sticker: Pair<Int, Boolean>) {
        viewModel.event(StickersViewModel.StickersEvents.UpdateTeam(sticker))
    }

    private fun addImageFromGallery(sticker: Pair<Int, Boolean>) {
        stickerSelected = sticker
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        )
            selectImageFromGallery()
        else
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun saveImageLocally(uri: Uri) {
        activity?.saveImage(uri, "${stickerSelected.first}-${stickerSelected.second}") { thisUri ->
            viewModel.event(StickersViewModel.StickersEvents.UpdateImage(stickerSelected.first to thisUri.toString()))
        }
    }
}
