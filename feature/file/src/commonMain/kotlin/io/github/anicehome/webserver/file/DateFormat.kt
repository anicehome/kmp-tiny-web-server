package io.github.anicehome.webserver.file

import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun dateFormatted(publishDate: Long): String =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())
        .withZone(LocalTimeZone.current.toJavaZoneId()).format(Instant.fromEpochMilliseconds(publishDate).toJavaInstant())