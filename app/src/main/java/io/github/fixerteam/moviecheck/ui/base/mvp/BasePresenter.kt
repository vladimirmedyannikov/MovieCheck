package io.github.fixerteam.moviecheck.ui.base.mvp

import rx.Subscription
import rx.subscriptions.CompositeSubscription
import kotlin.LazyThreadSafetyMode.NONE

abstract class BasePresenter<in T, V : BaseView<T>> {

  private var view: V? = null
  private val subscriptions by lazy(NONE) { CompositeSubscription() }

  fun attachView(view: V) {
    this.view = view
  }

  fun detachView() {
    view = null
    subscriptions.clear()
  }

  fun doIfViewReady(body: V.() -> Unit) {
    view?.apply {
      if (isReady()) {
        body.invoke(this)
      }
    }
  }

  fun addSubscription(subscription: Subscription) {
    subscriptions.add(subscription)
  }
}