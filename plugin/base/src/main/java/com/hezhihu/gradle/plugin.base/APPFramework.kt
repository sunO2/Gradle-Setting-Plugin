package com.hezhihu.gradle.plugin.base

import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
import java.io.FileReader

data class APPFramework(
    val app: App,
    var maven: JSONObject
)

data class App(
    val framework: List<Framework>,
    val host: Host
) {
    override fun toString(): String {
        return "App(framework=$framework, host=$host)"
    }
}

data class Framework(
    val dependencies: Dependencies?,
    val group: String,
    val id: String,
    val modules: List<Module>,
    val path: String,
    val version: String
) {
    override fun toString(): String {
        return "Framework(dependencies=$dependencies, group='$group', id='$id', modules=$modules, path='$path', version='$version')"
    }
}

data class Host(
    val path: String

) {
    override fun toString(): String {
        return "Host(path='$path')"
    }
}

data class Dependencies(
    val api: List<String>?,
    val implementation: List<String>?

) {
    override fun toString(): String {
        return "Dependencies(api=$api, implementation=$implementation)"
    }

    /**
     * 依赖库
     */
    fun dependencies(): Map<String,List<String>>{
        return mapOf(
            "api" to (api ?: arrayListOf()),
            "implementation" to (implementation ?: arrayListOf())
        )
    }
}

data class Module(
    val dependencies: Dependencies?,
    val id: String,
    val path: String

) {
    override fun toString(): String {
        return "Module(dependencies=$dependencies, id='$id', path='$path')"
    }
}

fun <T>T.appFrameworkFromFile(filePath: File): APPFramework{
    return FileReader(filePath).readText().run {
        Gson().fromJson(this, APPFramework::class.java).apply {
            maven = JSONObject(this@run).optJSONObject("maven")
            maven = maven ?: JSONObject()
        }
    }
}
