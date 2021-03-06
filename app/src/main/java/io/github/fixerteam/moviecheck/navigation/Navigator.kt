package io.github.fixerteam.moviecheck.navigation

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import io.github.fixerteam.moviecheck.R
import io.github.fixerteam.moviecheck.ui.base.mvp.BaseActivity
import io.github.fixerteam.moviecheck.ui.base.mvp.BaseFragment
import io.github.fixerteam.moviecheck.ui.detail.DetailActivity
import io.github.fixerteam.moviecheck.ui.detail.DetailFragment
import io.github.fixerteam.moviecheck.ui.main.MainActivity
import org.jetbrains.anko.startActivity

/**
 * Класс реализующий навигацию между экранами
 */
class Navigator {
  companion object {

    const val MOVIE_ID_EXTRA = "movie_id_extra"
    const val CURRENT_FRAGMENT = "currentFragment"

    fun showMovieDetail(context: Context?, idMovie: Int) {
      if (context is MainActivity) {
        context.startActivity<DetailActivity>(MOVIE_ID_EXTRA to idMovie)
      }
    }

    fun showMovieDetailFragment(context: Context?, idMovie: Int) {
      if (context is BaseActivity) {
        showFragment(context, DetailFragment.newInstance(idMovie), R.id.frame, false)
      }
    }

    fun showMovieDetailTwoPaneFragment(context: Context?, idMovie: Int) {
      if (context is BaseActivity) {
        showFragment(context, DetailFragment.newInstance(idMovie), R.id.detail_view, false)
      }
    }

    private fun showFragment(activity: BaseActivity, fragment: BaseFragment, @LayoutRes layout: Int,
        needBack: Boolean = true) {
      val fm = activity.supportFragmentManager
      if (! needBack) {
        clearScreenHistory(fm)
      }
      val backStateName = fragment.javaClass.name
      val fragmentPopped = if (fm.backStackEntryCount != 0) fm.popBackStackImmediate(backStateName, 0) else false

        if (needBack) {
          fm.beginTransaction()
              .replace(layout, fragment, backStateName)
              .addToBackStack(backStateName)
              .commit()
        } else {
          fm.beginTransaction()
              .replace(layout, fragment, backStateName)
              .commit()
        }

        activity.intent.putExtra(CURRENT_FRAGMENT, backStateName)

      fm.executePendingTransactions()
    }

    private fun clearScreenHistory(fm: FragmentManager) {
      try {
        while (fm.backStackEntryCount != 0) {
          fm.popBackStackImmediate()
        }
      } catch (e: IllegalStateException) {
        e.printStackTrace()
      }
      fm.executePendingTransactions()
    }
  }
}