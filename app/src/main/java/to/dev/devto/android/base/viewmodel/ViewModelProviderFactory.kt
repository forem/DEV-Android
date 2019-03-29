package to.dev.devto.android.base.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

abstract class ViewModelProviderFactory<V: BaseViewModel> : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return create() as T
    }

    protected abstract fun create(): V
}