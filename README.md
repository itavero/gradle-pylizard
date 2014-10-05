# PyLizard Gradle Plugin
Adds tasks for running the [Lizard code complexity](http://www.lizard.ws/) tool from Gradle.

## Requirements
To use this plugin you need to have Python and Lizard installed.
```bash
# Install PIP if you don't have it already
python < <(curl -s https://bootstrap.pypa.io/get-pip.py)

# Install Lizard using PIP
pip install lizard
```

## Configuration
Just add the plugin to your `build.gradle` and your good to go.
If no directories or source sets are configured, the plugin adds the `src` directory by default.
However, if you do wish to configure Lizard, have a look at the example below.

```groovy
lizard {
    // The paths you wish to include
    // These are relative to your project folder
    includes = ["src"]
   
    // The paths you wish to exclude
    // These are relative to your project folder
    excludes = ["src/test/*"]
    
    // Simply add some SourceSet instances if you want
    sourceSets = [android.sourceSets.main, android.sourceSets.debug]
    
    // Set the number of threads Lizard may use
    // This defaults to the number of available processors found by the JVM
    numberOfThreads = 2
}
```

Please note, that I have not explicitly verified these examples, but I am pretty confident that they should work.

## Usage
The plug-in adds two tasks:
+ `lizardVersion` simply runs Lizard and shows you which version is installed.
+ `lizard` calls Lizard with arguments based on your Gradle configuration.

If your projects has a `check` task, the plug-in adds a dependency for the `lizard` task to it.

## Found a bug? Got a cool suggestion?
Let me know by creating an issue or, even better, fork this repository and DIY!
I'm always willing to accept a Pull-Request, if the code is written nicely and it is tested.