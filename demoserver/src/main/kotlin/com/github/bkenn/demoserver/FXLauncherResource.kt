package com.github.bkenn.demoserver

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths

@Controller
@RequestMapping("/files")
class FXLauncherResource {

    final val home: String = System.getProperty("user.home")
    final val fxlauncherDir = Paths.get(home, "fxlauncher")

    init {
        Files.createDirectories(fxlauncherDir)
    }

    @GetMapping("/{name:.+}")
    fun getFile(@PathVariable name: String) : ResponseEntity<InputStreamResource> {

        val fullPath = fxlauncherDir.resolve(name)

        if (Files.notExists(fullPath)) return ResponseEntity.notFound().build()

        val file = fullPath.toFile()

        val mediaType = when(file.extension.contains("xml")) {
            true  -> MediaType.APPLICATION_XML
            false -> MediaType.APPLICATION_OCTET_STREAM
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=$name")
                .contentType(mediaType).contentLength(file.length())
                .body(InputStreamResource(FileInputStream(file)))
    }
}