package com.music.player.presentation.welcomePage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.music.player.R
import com.music.player.common.CommonUtil.showToast
import com.music.player.common.PlayerInstance
import com.music.player.databinding.WelcomePageBinding
import com.music.player.domain.use_case.welcomeMusicList.WelcomePageViewModel
import com.music.player.presentation.adapter.PlaylistAdapter
import com.music.player.presentation.musicPlayer.MusicPlayer
import com.music.player.presentation.savedPlayList.SavedPlayList
import dagger.hilt.android.AndroidEntryPoint


//Activity for  Welcome Page
@AndroidEntryPoint
class WelcomePage : AppCompatActivity(), SearchView.OnQueryTextListener {

    private val STORAGE_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private lateinit var binding: WelcomePageBinding
    private val welcomePageViewModel: WelcomePageViewModel by viewModels()
    private lateinit var playListAdapter: PlaylistAdapter

    val STORAGE_PERMISSION_CODE = 0

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.commonView.welcomeToolbar).apply { title = "" }
        binding.commonView.pageTitle.text = resources.getString(R.string.library)

        //Check and request storage permission
        requestStoragePermissionInternal()
    }

    private fun showPlaylist() {
        welcomePageViewModel.getLocalAudios()
        welcomePageViewModel.audioPlayList.observe(this) { playlist ->
            if (playlist.isNullOrEmpty().not()) {
                binding.emptyState.root.isVisible = false
                playListAdapter = PlaylistAdapter(playlist) { track, position ->
                    //Navigate to music activity
                    PlayerInstance.getInstance().reset()
                    PlayerInstance.currentIndex = position
                    MusicPlayer.getStartIntent(this@WelcomePage, playlist)
                }
                binding.commonView.trackPlayList.adapter = playListAdapter
            }
        }
    }

    private fun requestStoragePermissionInternal() {
        if (ContextCompat.checkSelfPermission(
                this, STORAGE_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showPlaylist()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, STORAGE_PERMISSION)) {
            showPermissionDialog(
                resources.getString(R.string.permission_required),
                resources.getString(R.string.permission_popup_msg),
                resources.getString(R.string.permission_popup_ok)
            )
        } else {
            checkRequestPermissions()
        }

    }

    private fun checkRequestPermissions() {
        ActivityCompat.requestPermissions(
            this@WelcomePage, arrayOf(STORAGE_PERMISSION), STORAGE_PERMISSION_CODE
        )
    }

    private fun showPermissionDialog(dialogTitle: String, message: String, positiveBtnMsg: String) {
        AlertDialog.Builder(this).apply {
            setTitle(dialogTitle)
            setMessage(message)
            setCancelable(false)
            setPositiveButton(positiveBtnMsg) { dialog, _ ->
                if (positiveBtnMsg == "Settings") {
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        setData(Uri.fromParts("package", context.packageName, null))
                        startActivity(this)
                    }
                } else {
                    checkRequestPermissions()
                }
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.welcome_screen_menu, menu)
        (menu?.findItem(R.id.search_bar)?.actionView as SearchView).apply {
            setOnQueryTextListener(this@WelcomePage)
            queryHint = resources.getString(R.string.search_your_audio)
            setPadding(80, 0, 0, 0)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                startActivity(Intent(this@WelcomePage, SavedPlayList::class.java))
            }

            R.id.more_options -> showToast(resources.getString(R.string.welcome_menu_more_options))
        }

        return super.onOptionsItemSelected(item);

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPlaylist()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, STORAGE_PERMISSION)
                    .not()
            ) {
                showPermissionDialog(
                    resources.getString(R.string.permission_required),
                    resources.getString(R.string.permission_popup_msg),
                    resources.getString(R.string.permission_popup_setting)
                )
            } else {
                requestStoragePermissionInternal()
            }
        }

    }

    override fun onQueryTextSubmit(query: String): Boolean {
        playListAdapter?.searchFromList(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        playListAdapter?.searchFromList(newText)
        return true
    }

}