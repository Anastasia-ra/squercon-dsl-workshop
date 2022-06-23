package at.squer.dslworkshop

abstract class SlackDslBaseScript extends Script {

    def run() {

        // global variables should be declared here

        scriptBody()
    }

    abstract void scriptBody()

    // global script methods/functions must be declared here

    def pause() {
        System.in.read()
    }
}

// classes must be declared here