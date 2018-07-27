package okmock.interceptor

import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import okmock.CallAction
import okmock.Mediator
import okmock.OKMockInterceptor

class XetrofitInterceptor : Interceptor, OKMockInterceptor {

    private lateinit var mediator: Mediator

    override fun onInitialize(mediator: Mediator) {
        this.mediator = mediator

    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!::mediator.isInitialized) {
            throw Exception(
                    "OKMock is not configured. Please initialize OKMock before adding the interceptor.")
        }
        val request = chain.request().newBuilder().build()
        val requestSentAt = System.currentTimeMillis()

        val requestModifiers = mediator.getCallAction(
                request.getOKMockMethodType(),
                request.url().url(),
                request.headers().toMultimap(),
                request.body().asByteArray()
        )

        val updatedRequest = when (requestModifiers) {
            is CallAction.GeneratedResponse -> request
            is CallAction.ModifyRequest -> request.applyModifiers(requestModifiers)
        }

        val loggedRequest = updatedRequest.newBuilder().build().let {
            mediator.logRequestCall(
                    it.getOKMockMethodType(),
                    it.url().url(),
                    it.headers().toMultimap(),
                    it.body().asByteArray()
            )
        }

        val response = request.createRetrofitResponse(chain, requestSentAt, requestModifiers)
        response.newBuilder().build().apply {
            mediator.logResponse(
                    loggedRequest,
                    code(),
                    headers().toMultimap(),
                    receivedResponseAtMillis(),
                    body()?.bytes()?: ByteArray(0)

            )
        }
        return chain.proceed(chain.request())
    }

    private fun RequestBody?.asByteArray(): ByteArray {
        if (this == null)
            return ByteArray(0)
        return Buffer().apply { writeTo(this) }.readByteArray()
    }

}
