//package com.cristianboicu.githubclient.di
//
//import android.content.Context
//import androidx.room.Room
//import com.cristianboicu.githubclient.data.local.GhDao
//import com.cristianboicu.githubclient.data.local.GhDatabase
//import com.cristianboicu.githubclient.data.local.ILocalDataSource
//import com.cristianboicu.githubclient.data.local.LocalDataSource
//import com.cristianboicu.githubclient.data.remote.ApiService
//import com.cristianboicu.githubclient.data.remote.IRemoteDataSource
//import com.cristianboicu.githubclient.data.remote.RemoteDataSource
//import com.cristianboicu.githubclient.data.repository.DefaultRepository
//import com.cristianboicu.githubclient.data.repository.FakeAndroidDefaultRepository
//import com.cristianboicu.githubclient.data.repository.IDefaultRepository
//import dagger.Binds
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import dagger.hilt.testing.TestInstallIn
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import javax.inject.Singleton
//
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [AppModule::class]
//)
//abstract class TestAppModule {
//
//    @Binds
//    abstract fun bindLocalDataSource(
//        localDataSource: LocalDataSource,
//    ): ILocalDataSource
//
//    @Binds
//    abstract fun bindRemoteDataSource(
//        remoteDataSource: RemoteDataSource,
//    ): IRemoteDataSource
//
//    @Binds
//    abstract fun bindDefaultRepository(
//        defaultRepository: FakeAndroidDefaultRepository,
//    ): IDefaultRepository
//
//    @Binds
//    abstract fun bindRealDefaultRepository(
//        defaultRepository: DefaultRepository,
//    ): IDefaultRepository
//
//    companion object {
//        @Provides
//        fun provideDao(db: GhDatabase) = db.getGhDao()
//
//        @Provides
//        fun provideInMemoryDb(@ApplicationContext context: Context) =
//            Room.inMemoryDatabaseBuilder(
//                context,
//                GhDatabase::class.java
//            ).allowMainThreadQueries().build()
//
//        @Provides
//        fun provideLocalDataSource(dao: GhDao): LocalDataSource =
//            LocalDataSource(dao)
//
//        @Provides
//        fun provideRemoteDataSource(
//            apiService: ApiService,
//        ): RemoteDataSource = RemoteDataSource(apiService)
//
//        @Singleton
//        @Provides
//        fun provideDefaultRepository(
//        ): FakeAndroidDefaultRepository =
//            FakeAndroidDefaultRepository(mutableListOf(), mutableListOf(), null)
//
//        @Provides
//        fun provideRetrofit(): Retrofit {
//            return Retrofit.Builder()
//                .baseUrl("https://api.github.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//        }
//
//        @Provides
//        fun provideApiClient(retrofit: Retrofit): ApiService {
//            return retrofit.create(ApiService::class.java)
//        }
//    }
//}