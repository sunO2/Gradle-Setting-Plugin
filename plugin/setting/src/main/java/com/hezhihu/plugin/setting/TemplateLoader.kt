package com.hezhihu.plugin.setting

import freemarker.cache.ByteArrayTemplateLoader
import freemarker.template.Configuration

class TemplateLoader() {

    companion object{
        const val GRADLE = "gradle"
        const val MANIFEST = "manifest"
        const val STYLES = "styles"
        const val COLORS = "colors"
        const val IGNORE = "ignore"
        const val CONSUMER = "consumer"
        const val PROGUARD = "proguard"
    }

    var configuration: Configuration = Configuration(Configuration.getVersion())

    init {
        val byteArrayTemplateLoader = ByteArrayTemplateLoader()
        byteArrayTemplateLoader.putTemplate(GRADLE,TemplateLoader::class.java.getResource("/source/template/build.gradle.ftl").readBytes())
        byteArrayTemplateLoader.putTemplate(MANIFEST,TemplateLoader::class.java.getResource("/source/template/AndroidManifest.xml.ftl").readBytes())
        byteArrayTemplateLoader.putTemplate(COLORS,TemplateLoader::class.java.getResource("/source/template/colors.xml.ftl").readBytes())
        byteArrayTemplateLoader.putTemplate(STYLES,TemplateLoader::class.java.getResource("/source/template/styles.xml.ftl").readBytes())
        byteArrayTemplateLoader.putTemplate(IGNORE,TemplateLoader::class.java.getResource("/source/template/.gitignore.ftl").readBytes())
        byteArrayTemplateLoader.putTemplate(CONSUMER,TemplateLoader::class.java.getResource("/source/template/consumer-rules.pro.ftl").readBytes())
        byteArrayTemplateLoader.putTemplate(PROGUARD,TemplateLoader::class.java.getResource("/source/template/proguard-rules.pro.ftl").readBytes())
        configuration.templateLoader = byteArrayTemplateLoader
    }

}