package com.example.test.core.webservice

import com.example.test.core.webservice.domain.UsersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module()
object WebServiceModule {

    @UsersClient
    @Singleton
    @Provides
    fun provideUsersApiClient(): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .build()

    @Singleton
    @UsersClient
    @Provides
    fun provideUsersApiRetrofitClient(@UsersClient client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Singleton
    @UsersClient
    @Provides
    fun provideUsersApi(@UsersClient retrofit: Retrofit): UsersApi =
        retrofit.create(UsersApi::class.java)
}

@InstallIn(SingletonComponent::class)
@Module
internal interface WebServiceModuleInternalBinds {

}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class UsersClient
