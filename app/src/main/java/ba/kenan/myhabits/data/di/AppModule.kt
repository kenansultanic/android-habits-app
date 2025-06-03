package ba.kenan.myhabits.data.di

import ba.kenan.myhabits.data.network.NetworkStatusProviderImpl
import ba.kenan.myhabits.data.repository.AuthRepositoryImpl
import ba.kenan.myhabits.data.repository.HabitRepositoryImpl
import ba.kenan.myhabits.data.repository.ProfileRepositoryImpl
import ba.kenan.myhabits.domain.network.NetworkStatusProvider
import ba.kenan.myhabits.domain.repository.AuthRepository
import ba.kenan.myhabits.domain.repository.HabitRepository
import ba.kenan.myhabits.domain.repository.ProfileRepository
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

    @Provides
    @Singleton
    fun provideProfileRepository(): ProfileRepository {
        return ProfileRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideHabitRepository(): HabitRepository {
        return HabitRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideNetworkStatusProvider(impl: NetworkStatusProviderImpl): NetworkStatusProvider = impl

}
