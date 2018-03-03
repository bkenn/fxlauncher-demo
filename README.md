# Gradle Application for FXLauncher with Custom Splash Screen

This is an example on how to create a TornadoFX application with a custom splash screen using the Gradle build system.

There are differences in the output of resources between gradle and maven that makes it necessary for you to roll your 
own tasks to correctly configure FXLauncher to display your custom splash screen. Luckily it is very easy to configure this.


### Configure LauncherUI
First create and configure your splash screen to your liking. Make sure to include the path to your implementation of `UIProvider` in the 
`resources/META-INF/services/fxlauncher.UIProvider` file so FXLauncher knows where to load the custom splash screen from. After you 
are satisfied with splash screen we create the gradle tasks to restructure the resources needed to copy into `mainui`'s `fxlauncher.jar`.
This is necessary because gradle generates classes and resources in a way you cannot simply copy the resources into a jar file. 
Take a look. You will find the class files in the directory structured like `build/classes/java/main` where we would like the directories 
`java/main` to be removed and the resources for our splash screen to reside in `classes` directory. So we must create two tasks to 
create such a directory structure.

 ```groovy
task("copyMeta", type: Copy) {
    dependsOn('jar')
    group = "deploy"
    from 'build/resources/main/services'
    into 'build/target/classes/META-INF/services'
}

task("copyAll", type: Copy) {
    dependsOn "copyMeta"
    group = "deploy"

    from('build/classes/java/main')
    from('build/resources/main/') {
        include '*.css'
    }

    into 'build/target/classes'
}
```

### Configure MainUI
Like a regular FXLauncher configuration you must provide FXLauncher with basic information about your application. 

```groovy

fxlauncher {
    applicationVendor 'Company Name'
    applicationVersion "1.0"
    applicationUrl 'http://localhost:8080/files'
    applicationMainClass 'com.github.bkenn.fxlauncherdemo.MyApp'
    acceptDowngrade false
    deployTarget 'briank@localhost:destDir'
}

```

We now need to make sure that the custom splash screen gets copied into `mainui`'s `fxlauncher.jar` and we would like to 
generate our final jar with only one gradle task call. I created the following task to copy the classes and resources 
into the `fxlauncher.jar` after gradle executes `generateApplicationManifest`.

```groovy
task("generateFXJar", type: Exec) {
    dependsOn('embedApplicationManifest', ':launcherui:copyAll')
    group = "fxlauncher"
    workingDir './build/fxlauncher'
    commandLine 'jar', 'uf', 'fxlauncher.jar', '-C', '../../../launcherui/build/target/classes', '.'
}
```

If you execute the `generateFXJar` gradle task from `mainui` module, you will see that  `launcherui` will create the resources we need 
in `build/target`, generate `mainui` jar and then copy the resources from `launcherui` into `mainui`'s `fxlauncher.jar`.

Congratulations! You are the proud owner of a TornadoFX application with a custom splash screen! 

If you would like to test out the results you can use the `demoserver` module to start up a spring boot application 
that will serve files from `${user.home)/fxlauncher`. Copy all resources from `mainui/build/fxlauncher` to `${user.home}/fxlauncher`, 
go back to `main/build/fxlauncher` and execute the following command: `java -jar fxlauncher.jar`. 
You should see your custom splash screen appearing.  

