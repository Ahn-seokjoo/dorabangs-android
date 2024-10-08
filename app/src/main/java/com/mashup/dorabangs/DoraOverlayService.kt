package com.mashup.dorabangs

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.dorabangs.share.DoraSnackBarWithShareScreen
import com.mashup.dorabangs.domain.model.Link
import com.mashup.dorabangs.domain.usecase.posts.SaveLinkUseCase
import com.mashup.dorabangs.domain.usecase.user.GetIdFromLinkToReadLaterUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import javax.inject.Inject

@AndroidEntryPoint
class DoraOverlayService : Service(), LifecycleOwner, SavedStateRegistryOwner {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: ComposeView

    @Inject
    lateinit var saveLinkUseCase: SaveLinkUseCase

    @Inject
    lateinit var getFolderId: GetIdFromLinkToReadLaterUseCase

    private val _lifecycleRegistry = LifecycleRegistry(this)
    private val _savedStateRegistryController: SavedStateRegistryController =
        SavedStateRegistryController.create(this)
    override val savedStateRegistry: SavedStateRegistry =
        _savedStateRegistryController.savedStateRegistry
    override val lifecycle: Lifecycle = _lifecycleRegistry
    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(job + Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        _savedStateRegistryController.performAttach()
        _savedStateRegistryController.performRestore(null)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(URL).orEmpty()
        if (url.isNotBlank()) {
            val view = showOverlay(url)
            serviceScope.launch {
                val localFolderId = getFolderId.invoke()
                delay(3000L)
                if (localFolderId.isNotBlank()) {
                    windowManager.removeView(view)
                    saveLinkUseCase.invoke(
                        link = Link(
                            folderId = localFolderId,
                            url = url,
                        ),
                        fetchUpdate = true,
                    )
                }
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    private fun showOverlay(copiedUrl: String): ComposeView {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        overlayView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@DoraOverlayService)
            setViewTreeSavedStateRegistryOwner(this@DoraOverlayService)
            setContent {
                DoraSnackBarWithShareScreen(
                    onClick = {
                        windowManager.removeView(this)
                        job.cancel()
                        val encodedUrl = URLEncoder.encode(copiedUrl, "UTF-8")
                        val deepLinkUri = Uri.parse("linkit://linksave/$encodedUrl")
                        Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }.also {
                            startActivity(it)
                        }.run { stopSelf() }
                    },
                )
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT,
        )
        params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

        windowManager.addView(overlayView, params)
        return overlayView
    }

    companion object {
        private const val URL = "SHARED_URL"
    }
}
