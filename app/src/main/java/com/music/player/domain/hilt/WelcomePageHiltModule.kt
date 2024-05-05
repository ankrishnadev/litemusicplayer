package com.music.player.domain.hilt

import android.content.ContentResolver
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.music.player.data.repository.FavouriteSongRepository
import com.music.player.data.repository.MainRepositoryImpl
import com.music.player.data.viewmodel.MusicPlayerViewModel
import com.music.player.domain.db.MusicPlayerDB
import com.music.player.domain.repository.MainRepository
import com.music.player.domain.use_case.savedMusicList.SavedMusicListUseCase
import com.music.player.domain.use_case.savedMusicList.SavedMusicListViewModel
import com.music.player.domain.use_case.welcomeMusicList.FetchMusicListUseCase
import com.music.player.domain.use_case.welcomeMusicList.WelcomePageViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WelcomePageHiltModule {

    @Provides
    @Singleton
    fun providesApplication(@ApplicationContext context: Context): Context {
        return context.applicationContext
    }

    @Provides
    @Singleton
    fun providesContentResolver(context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun providesMainRepository(contentResolver: ContentResolver): MainRepository {
        return MainRepositoryImpl(contentResolver)
    }

    @Provides
    @Singleton
    fun providesDBMainRepository(context: Context): FavouriteSongRepository {
        return FavouriteSongRepository.getInstance(MusicPlayerDB.getInstance(context))
    }

    @Provides
    @Singleton
    fun providesFetchMusicListUseCase(repository: MainRepository): FetchMusicListUseCase {
        return FetchMusicListUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesSavedMusicListUseCase(repository: MainRepository): SavedMusicListUseCase {
        return SavedMusicListUseCase(repository)
    }

    @Provides
    fun providesSavedMusicListViewModel(
        activity: AppCompatActivity,
        savedMusicListUseCase: SavedMusicListUseCase
    ): ViewModel {
        return ViewModelProvider(activity)[SavedMusicListViewModel::class.java]
    }

    @Provides
    fun providesMusicPlayerViewModel(
        activity: AppCompatActivity,
        fetchMusicListUseCase: FetchMusicListUseCase
    ): ViewModel {
        return ViewModelProvider(activity)[MusicPlayerViewModel::class.java]
    }

    @Provides
    fun providesWelcomePageViewModel(
        activity: AppCompatActivity,
        fetchMusicListUseCase: FetchMusicListUseCase
    ): ViewModel {
        return ViewModelProvider(activity)[WelcomePageViewModel::class.java]
    }

}