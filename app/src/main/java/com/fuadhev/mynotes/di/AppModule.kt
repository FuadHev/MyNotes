package com.fuadhev.mynotes.di

import android.content.Context
import androidx.room.Room
import com.fuadhev.mynotes.repo.Repository
import com.fuadhev.mynotes.room.MyRoomDatabase
import com.fuadhev.mynotes.room.NotesDao
import com.fuadhev.mynotes.room.ToDosDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): MyRoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MyRoomDatabase::class.java,
            "note_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesRepository(notesDao: NotesDao,toDosDao: ToDosDao): Repository {
        return Repository(notesDao,toDosDao)
    }

    @Provides
    @Singleton
    fun provideNoinDao(db:MyRoomDatabase): NotesDao {
        return db.notesDao()
    }


    @Provides
    @Singleton
    fun provideTodoDao(db:MyRoomDatabase): ToDosDao {
        return db.toDosDao()
    }


    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context):Context{
        return context
    }
}