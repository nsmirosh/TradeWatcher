package nick.mirosh.tradewatcher.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nick.mirosh.tradewatcher.repository.MainReposityImpl

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {

            return MainActivityViewModel(MainReposityImpl()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}