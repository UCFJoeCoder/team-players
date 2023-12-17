package com.ucfjoe.teamplayers.domain.use_case.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ucfjoe.teamplayers.common.Resource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SendGameEmailUseCase @Inject constructor(
    private val context: Context
) {

    operator fun invoke(
        teamName: String,
        gameDateTime: LocalDateTime,
        uri: Uri
    ): Resource<Unit> {

        val dateTime = getReadableDateTime(gameDateTime)

        return sendEmail(
            subject = getSubject(teamName, dateTime),
            body = getBody(teamName, dateTime),
            attachmentMimeType = "text/csv",
            attachmentUri = uri
        )
    }

    private fun getReadableDateTime(gameDateTime: LocalDateTime): String {
        val dateFormatter = DateTimeFormatter.ofPattern("LLL d, yyyy hh:mm a")
        return gameDateTime.format(dateFormatter)
    }

    private fun getSubject(teamName: String, gameDateTime: String): String {
        return "$teamName - $gameDateTime"
    }

    private fun getBody(teamName: String, gameDateTime: String): String {
        return "Attached is the game details for the $teamName game held at $gameDateTime"
    }

    private fun sendEmail(
        subject: String,
        body: String? = null,
        attachmentUri: Uri? = null,
        attachmentMimeType: String? = null,
        sendToEmailAddress: String? = null
    ): Resource<Unit> {

        if (subject.isBlank()) {
            return Resource.Error("Subject must be provided.")
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK

            if (!sendToEmailAddress.isNullOrBlank()) {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(sendToEmailAddress))
            }

            putExtra(Intent.EXTRA_SUBJECT, subject)

            if (!body.isNullOrBlank()) {
                putExtra(Intent.EXTRA_TEXT, body)
            }

            if (attachmentUri != null && !attachmentMimeType.isNullOrBlank()) {
                type = attachmentMimeType
                putExtra(Intent.EXTRA_STREAM, attachmentUri)
            }
        }

        return try {
            context.startActivity(intent)
            Resource.Success(Unit)
        } catch (e: ActivityNotFoundException) {
            Resource.Error("Failed to find app to send email.")
        }
    }

}