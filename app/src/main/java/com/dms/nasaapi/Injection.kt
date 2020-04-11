package com.dms.nasaapi

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.dms.nasaapi.api.ApodApiService
import com.dms.nasaapi.api.EpicApiService
import com.dms.nasaapi.api.ImageApiLibraryService
import com.dms.nasaapi.api.MrpApiService
import com.dms.nasaapi.data.apod.ApodRepository
import com.dms.nasaapi.data.epic.EpicRepository
import com.dms.nasaapi.data.image_library.ImLRepository
import com.dms.nasaapi.data.mrp.MrpRepository
import com.dms.nasaapi.db.apod.ApodDAO
import com.dms.nasaapi.db.apod.ApodDatabase
import com.dms.nasaapi.db.epic.EpicDAO
import com.dms.nasaapi.db.epic.EpicDatabase
import com.dms.nasaapi.db.marsRoverPhotos.MrpDatabase
import com.dms.nasaapi.ui.apod.ApodViewModelFactory
import com.dms.nasaapi.ui.epic.EpicViewModelFactory
import com.dms.nasaapi.ui.image_and_video.ImLViewModelFactory
import com.dms.nasaapi.ui.mrp.MrpViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {


    private fun provideMrpRepository(context: Context): MrpRepository {
        return MrpRepository(MrpApiService.create(), MrpDatabase.getInstance(context).getMrpDao())
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return MrpViewModelFactory(provideMrpRepository(context))
    }

    private fun provideImLRepository(): ImLRepository {
        return ImLRepository(ImageApiLibraryService.create())
    }

    fun provideImLViewModelFactory(): ViewModelProvider.Factory {
        return ImLViewModelFactory(provideImLRepository())
    }

    private fun provideEpicDao(context: Context): EpicDAO {
        return EpicDatabase.getInstance(context).getEpicDao()
    }

    private fun provideEpicRepository(context: Context): EpicRepository {
        return EpicRepository(EpicApiService.create(), provideEpicDao(context))
    }

    fun provideEpicViewModelFactory(context: Context): ViewModelProvider.Factory {
        return EpicViewModelFactory(provideEpicRepository(context))
    }

    private fun provideApodDao(context: Context): ApodDAO {
        return ApodDatabase.getInstance(context).getApodDao()
    }

    private fun provideApodRepository(context: Context): ApodRepository {
        return ApodRepository(ApodApiService.create(), provideApodDao(context))
    }

    fun provideApodViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ApodViewModelFactory(provideApodRepository(context))
    }

}