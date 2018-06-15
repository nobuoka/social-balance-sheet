package info.vividcode.ktor.twitter.login.application

import info.vividcode.ktor.twitter.login.ClientCredential
import info.vividcode.ktor.twitter.login.TemporaryCredential
import info.vividcode.ktor.twitter.login.TemporaryCredentialStore
import info.vividcode.oauth.*
import info.vividcode.oauth.protocol.ParameterTransmission
import info.vividcode.oauth.protocol.PercentEncode
import info.vividcode.oauth.protocol.Signatures
import info.vividcode.whatwg.url.parseWwwFormUrlEncoded
import io.ktor.http.encodeURLQueryComponent
import kotlinx.coroutines.experimental.async
import okhttp3.Call
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.security.SecureRandom
import java.time.Clock
import kotlin.coroutines.experimental.CoroutineContext

class ObtainRedirectUrlService(private val env: Required) {
    interface Required {
        val oauth: OAuth
        val httpClient: Call.Factory
        val httpCallContext: CoroutineContext
        val temporaryCredentialStore: TemporaryCredentialStore
    }

    suspend fun obtainRedirectUrl(
        clientCredential: ClientCredential,
        twitterLoginCallbackAbsoluteUrl: String
    ): String {
        val unauthorizedRequest = Request.Builder()
            .method("POST", RequestBody.create(null, ByteArray(0)))
            .url("https://api.twitter.com/oauth/request_token")
            .build()
        val additionalProtocolParameters = listOf(ProtocolParameter.Callback(twitterLoginCallbackAbsoluteUrl))
        val authorizedRequest = authorize(unauthorizedRequest, clientCredential, additionalProtocolParameters)

        println("Async start")
        val temporaryCredential = TemporaryCredential("token", "secret")
        //async(env.httpCallContext) {

//            println("Async start 2")
//            try {
//                env.httpClient.newCall(authorizedRequest).execute()
//            } catch (exception: IOException) {
//                throw TwitterCallFailedException("Request couldn't be executed", exception)
//            }.use { response ->
//                if (!response.isSuccessful) {
//                    throw TwitterCallFailedException("Response not successful (status code : ${response.code()})")
//                }
//                val body = response.body()?.bytes()
//                if (body != null) {
//                    val pairs = parseWwwFormUrlEncoded(body).toMap()
//                    val oauthToken = pairs["oauth_token"]
//                    val oauthTokenSecret = pairs["oauth_token_secret"]
//                    if (oauthToken == null || oauthTokenSecret == null) {
//                        throw TwitterCallFailedException("Unexpected response content (response body : $body)")
//                    } else {
//                        TemporaryCredential(oauthToken, oauthTokenSecret)
//                    }
//                } else {
//                    throw TwitterCallFailedException("Not expected response content (response body is null)")
//                }
//            }.also {
//                println("Async finish 2")
//            }
        //}.await()
        println("Async finish")
        env.temporaryCredentialStore.saveTemporaryCredential(temporaryCredential)
        return "https://api.twitter.com/oauth/authenticate?oauth_token=${encodeURLQueryComponent(temporaryCredential.token)}"
    }

    private fun authorize(
        unauthorizedRequest: Request,
        clientCredential: ClientCredential,
        additionalProtocolParameters: List<ProtocolParameter<*>>
    ): Request {
        val httpRequest = HttpRequest(unauthorizedRequest.method(), unauthorizedRequest.url().url())

        val nextInt: (Int) -> Int = SecureRandom.getInstanceStrong()::nextInt
        val nonceGenerator = OAuthNonceGenerator(object : NextIntEnv {
            override val nextInt: (Int) -> Int = nextInt
        })
        val clock: Clock = Clock.systemDefaultZone()

        val protocolParams = OAuthProtocolParameters.createProtocolParametersExcludingSignature(
            clientCredential.identifier,
            null,
            OAuthProtocolParameters.Options.HmcSha1Signing(nonceGenerator.generateNonceString(), clock.instant()),
            additionalProtocolParameters
        )
        val secrets = "ddd&ddd"
        val signature = when (protocolParams.get(ProtocolParameter.SignatureMethod)) {
            is ProtocolParameter.SignatureMethod.HmacSha1 -> Signatures.makeSignatureWithHmacSha1(secrets, "testbasestring")
            is ProtocolParameter.SignatureMethod.Plaintext -> secrets
            null -> throw RuntimeException("No signature method specified")
        }
        val protocolParameters = ProtocolParameterSet.Builder().add(protocolParams).add(ProtocolParameter.Signature(signature)).build()

//        val protocolParameters = env.oauth.generateProtocolParametersSigningWithHmacSha1(
//            httpRequest,
//            clientCredentials = OAuthCredentials(clientCredential.identifier, clientCredential.sharedSecret),
//            temporaryOrTokenCredentials = null,
//            additionalProtocolParameters = additionalProtocolParameters
//        )
        val authorizationHeaderString = ParameterTransmission.getAuthorizationHeaderString(protocolParameters, "")
        return unauthorizedRequest.newBuilder().header("Authorization", authorizationHeaderString).build()
    }

}
