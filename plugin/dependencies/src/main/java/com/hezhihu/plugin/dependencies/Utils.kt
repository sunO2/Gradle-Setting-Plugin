package com.hezhihu.plugin.dependencies

import com.android.aaptcompiler.android.isTruthy
import groovy.util.Node
import groovy.util.XmlParser
import java.io.File


/**
 * xml 转换到 Module 对应数据
 */
fun <T: File> T.xmlModule(): Modules{
    val moduleXml = XmlParser().parse(this)
    val dependenciesNode = moduleXml.children()
    val modules = Modules()
    dependenciesNode.forEach{ groupNode ->
        if(groupNode is Node){
            val moduleGroup = Modules.Group()
            moduleGroup.group = groupNode.attribute("group").toString()
            moduleGroup.id = groupNode.attribute("id").toString()
            moduleGroup.path = groupNode.attribute("path").toString()
            moduleGroup.version = groupNode.attribute("version").toString()
            modules.addGroup(moduleGroup)
            groupNode.children().forEach{ moduleNode ->
                if(moduleNode is Node) {
                    val moduleName = moduleNode.attribute("id")?.toString()?:""
                    val modulePath = moduleNode.attribute("path")?.toString()?:""
                    var application = moduleNode.attribute("application")
                    application = when (application) {
                        is Boolean -> {
                            application
                        }
                        is String -> {
                            application == "true"
                        }
                        else -> {
                            false
                        }
                    }
                    moduleGroup.addModule(Modules.Module(moduleName,modulePath,application))
                }
            }
        }
    }
    return modules
}