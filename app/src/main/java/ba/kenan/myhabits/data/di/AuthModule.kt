package ba.kenan.myhabits.data.di

import ba.kenan.myhabits.data.repository.AuthRepositoryImpl
import ba.kenan.myhabits.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }
}
