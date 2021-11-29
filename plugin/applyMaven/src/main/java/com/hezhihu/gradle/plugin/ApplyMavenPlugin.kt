package com.hezhihu.gradle.plugin

import com.google.gson.JsonObject
import com.hezhihu.gradle.plugin.base.Framework
import com.hezhihu.gradle.plugin.base.appFrameworkFromFile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DslObject
import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.api.plugins.MavenRepositoryHandlerConvention
import org.gradle.api.tasks.Upload
import java.io.FileReader
import java.lang.Exception
import java.lang.IllegalArgumentException

/**
 * 上传 maven 组件功能
 */
class ApplyMavenPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        if(project.rootProject != project){
            throw IllegalArgumentException("请将Plugin 依赖在 rootProject 模块下")
        }

        appFrameworkFromFile(project.rootProject.file("dependencies.json")).app.framework.apply {
            val moduleInfoMap = toMap()
            project.subprojects{ subProject ->
                subProject.afterEvaluate{ projectAfter ->
                    if(projectAfter is DefaultProject){
                        moduleInfoMap[projectAfter.identityPath.path]?.apply {
                            projectAfter.group = group
                            projectAfter.version = version
                            projectAfter.convention.findPlugin(BasePluginConvention::class.java)?.apply {
                                archivesBaseName = id
                            }
                        }
                    }
                    println("mavenInfo: id:${projectAfter.name} group:${projectAfter.group} version:${projectAfter.version}")

                    println("添加 maven 插件功能 $projectAfter")
                    projectAfter.apply(hashMapOf("plugin" to "maven"))
                    projectAfter.tasks.getByName("uploadArchives"){ it ->
                        if(it is Upload){
                            try {
                                val maven = DslObject(it.repositories).convention.getPlugin(MavenRepositoryHandlerConvention::class.java)
                                val mavenDeploy = maven.mavenDeployer()
                                mavenDeploy.run {
                                    val repository = javaClass.getMethod("repository", Map::class.java)
                                        .invoke(this, mapOf("url" to "file://${project.rootProject.projectDir.path}/maven/m2"))
                                    repository.javaClass.getMethod("authentication", Map::class.java)
                                        .invoke(repository, mapOf("userName" to "","password" to ""))

                                    val snapshotRepository = javaClass.getMethod("snapshotRepository", Map::class.java)
                                        .invoke(this, mapOf("url" to "file://${project.rootProject.projectDir.path}/maven/m2"))
                                    snapshotRepository.javaClass.getMethod("authentication", Map::class.java)
                                        .invoke(snapshotRepository, mapOf("userName" to "","password" to ""))
                                }
                            }catch (e: Exception){
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun List<Framework>.toMap(): HashMap<String,ModuleVersionInfo>{
        val map = hashMapOf<String,ModuleVersionInfo>()
        forEach {
            val groupId = it.id
            val group = it.group
            val version = it.version
            map[":$groupId"] = ModuleVersionInfo(groupId,group,version)
            it.modules.forEach { module ->
                val moduleId = module.id
                map[":$groupId:$moduleId"] = ModuleVersionInfo(moduleId,group,version)
            }
        }
        return map
    }
}

class ModuleVersionInfo(val id: String,val group: String,val version: String){
    override fun toString(): String {
        return "ModuleVersionInfo(id='$id', group='$group', version='$version')"
    }
}