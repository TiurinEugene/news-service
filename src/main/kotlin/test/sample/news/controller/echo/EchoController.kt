package test.sample.news.controller.echo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EchoController {

    @GetMapping("/echo")
    fun echo(message: String) = message
}
