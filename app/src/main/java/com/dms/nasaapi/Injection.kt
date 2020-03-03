package com.dms.nasaapi

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.dms.nasaapi.api.ImageApiLibraryService
import com.dms.nasaapi.api.NasaApiService
import com.dms.nasaapi.data.image_library.ImLRepository
import com.dms.nasaapi.data.mrp.MrpRepository
import com.dms.nasaapi.db.marsRoverPhotos.MrpDatabase
import com.dms.nasaapi.db.marsRoverPhotos.MrpLocalCache
import com.dms.nasaapi.ui.image_and_video.ImLViewModelFactory
import com.dms.nasaapi.ui.mrp.MrpViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [MrpLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): MrpLocalCache {
        val database = MrpDatabase.getInstance(context)
        return MrpLocalCache(database.getMrpDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [MrpRepository] based on the [NasaApiService] and a
     * [MrpLocalCache]
     */
    private fun provideMrpRepository(context: Context): MrpRepository {
        return MrpRepository(
            NasaApiService.create(),
            provideCache(context)
        )
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return MrpViewModelFactory(
            provideMrpRepository(
                context
            )
        )
    }
    private fun provideImLRepository(): ImLRepository{
        return ImLRepository(ImageApiLibraryService.create())
    }
    fun provideImLViewModelFactory():ViewModelProvider.Factory{
        return ImLViewModelFactory(provideImLRepository())
    }
}