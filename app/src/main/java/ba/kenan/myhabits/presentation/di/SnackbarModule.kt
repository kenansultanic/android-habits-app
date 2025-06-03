package ba.kenan.myhabits.presentation.di

import ba.kenan.myhabits.presentation.utils.SnackbarController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SnackbarModule {

    @Provides
    @Singleton
    fun provideSnackbarController(): SnackbarController = SnackbarController()
}
