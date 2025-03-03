pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven (url ="https://storage.zego.im/maven" )
        maven ( url="https://www.jitpack.io")
        maven (url = "https://jitpack.io")
    }
}

rootProject.name = "ChatFlow"
include(":app")
 