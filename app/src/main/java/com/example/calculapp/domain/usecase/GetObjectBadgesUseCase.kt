package com.example.calculapp.domain.usecase

import com.example.calculapp.domain.Repository
import javax.inject.Inject

class GetObjectBadgesUseCase @Inject constructor(private val repository: Repository){
    suspend operator fun invoke(apiKey: String, symbols: String) = repository.getObjectBadges(apiKey, symbols)
}