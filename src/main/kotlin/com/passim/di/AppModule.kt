package com.passim.di

import org.koin.dsl.module
import com.passim.repository.RecordRepository
import com.passim.repository.ManagerRepository
import com.passim.service.LinkService
import com.passim.service.AdminService
import com.passim.service.CaptchaService

val appModule = module {
    // Repositories
    single { RecordRepository() }
    single { ManagerRepository() }
    
    // Services
    single { LinkService(get()) }
    single { AdminService(get(), get()) }
    single { CaptchaService() }
}