package io.github.fixerteam.moviecheck.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.fixerteam.moviecheck.R
import io.github.fixerteam.moviecheck.ui.base.adapter.RecyclerAdapter
import io.github.fixerteam.moviecheck.ui.base.adapter.RecyclerHolder
import io.github.fixerteam.moviecheck.ui.base.mvp.BaseView
import io.github.fixerteam.moviecheck.util.hide
import io.github.fixerteam.moviecheck.util.show
import org.jetbrains.anko.find
import kotlin.LazyThreadSafetyMode.NONE

abstract class ListFragment<in T, out VH: RecyclerHolder<T>> : Fragment(), BaseView<T> {

  private var list: RecyclerView? = null
  private var messageLabel: TextView? = null
  private var progress: ContentLoadingProgressBar? = null

  private val adapter by lazy(NONE) {
    object : RecyclerAdapter<T, VH>() {
      override fun getViewHolder(parent: ViewGroup): VH = this@ListFragment.getViewHolder(parent)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_list, container, false)
    list = view.find<RecyclerView>(R.id.list)
    messageLabel = view.find<TextView>(R.id.message_label)
    progress = view.find<ContentLoadingProgressBar>(R.id.content_progress)
    return view
  }

  override fun isReady() = isAdded

  override fun showLoading(){
    progress?.show()
  }

  override fun hideLoading() {
    progress?.hide()
  }

  override fun showEmpty(message: String) {
    list?.hide()
    messageLabel?.show()
    messageLabel?.text = message
  }

  override fun showContent(content: List<T>) {
    adapter.setItems(content)
    list?.show()
    list?.layoutManager = LinearLayoutManager(context)
    list?.adapter = adapter
    messageLabel?.hide()
  }

  override fun showError(message: String) {
    list?.hide()
    messageLabel?.show()
    messageLabel?.text = message
  }

  abstract fun getViewHolder(parent: ViewGroup): VH
}