package com.developbharat.yogsudhaar.domain.api

import com.developbharat.yogsudhaar.domain.api.dto.IsPoseCorrectInput
import com.developbharat.yogsudhaar.domain.api.dto.IsPoseCorrectResult
import retrofit2.http.Body
import retrofit2.http.POST

interface IRemoteSource {
    @POST("predictions/is-pose-correct")
    suspend fun isPoseCorrect(
        @Body() data: IsPoseCorrectInput,
    ): IsPoseCorrectResult
}