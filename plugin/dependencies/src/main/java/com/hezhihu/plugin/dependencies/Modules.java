package com.hezhihu.plugin.dependencies;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Modules {

    Map<String,Group> mGroups = new HashMap<>();

    /**
     * 添加组 组件
     * @param group 组
     */
    public void addGroup(Group group){
        mGroups.put(group.group,group);
    }

    /**
     * 是否包含
     * @param dependencyGroup 依赖
     * @return boolean
     */
    public boolean containsKey(String dependencyGroup) {
        return mGroups.containsKey(dependencyGroup);
    }

    @Nullable
    public Group get(String dependencyGroup){
        return mGroups.get(dependencyGroup);
    }

    public static class Group{
        ///组
        String group;
        ///名称
        String id;

        String path;

        String version;

        Map<String,Module> mModules = new HashMap<>();

        /**
         * 添加区域模块
         * @param module module
         */
        public void addModule(Module module) {
            mModules.put(module.id,module);
        }

        public boolean containsKey(@NotNull String dependencyName) {
            return mModules.containsKey(dependencyName);
        }

        public Module get(@NotNull String dependencyName) {
            return mModules.get(dependencyName);
        }
    }

    public static class Module{
        boolean isApplication=false;
        String id;
        String path;

        public Module(String id, String path,boolean isApplication) {
            this.isApplication = isApplication;
            this.id = id;
            this.path = path;
        }

        @Override
        public String toString() {
            return "Module{" +
                    "isApplication=" + isApplication +
                    ", id='" + id + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }


}
