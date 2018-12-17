package com.glovo.challenge.data.mock

import android.content.Context
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.mock.AndroidResources.asset
import okhttp3.mock.Behavior
import okhttp3.mock.MediaTypes
import okhttp3.mock.MockInterceptor
import javax.inject.Inject

class MockDataInterceptor @Inject constructor(
    context: Context
) : MockInterceptor() {

    init {
        behavior(Behavior.UNORDERED)

        addRule()
            .get()
            .path("/countries")
            .anyTimes()
            .respond(asset(context, "countries.json"), MediaTypes.MEDIATYPE_JSON)

        addRule()
            .get()
            .path("/cities")
            .anyTimes()
            .respond(asset(context, "cities.json"), MediaTypes.MEDIATYPE_JSON)

        addRule()
            .get()
            .pathMatches("/cities/(\\w+)".toPattern())
            .anyTimes()
            .answer {
                val cityCode = it.url().encodedPath().substringAfterLast("/")

                return@answer Response.Builder()
                    .code(200)
                    .body(
                        ResponseBody.create(
                            MediaTypes.MEDIATYPE_JSON,
                            asset(context, "cities-$cityCode.json").readBytes()
                        )
                    )
            }
    }

}
