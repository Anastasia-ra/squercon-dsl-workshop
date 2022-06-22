@groovy.transform.BaseScript(at.squer.dslworkshop.SlackDslBaseScript)
package at.squer.dslworkshop

me = user 'Lukasz Juszczyk'
paul = user 'Paul Rohorzka'
lorenz = user 'Lorenz Müller'
philipp = user 'Philipp Stampfer'
thomas = user 'Thomas Pokorny'
anastasia = user 'Anastasia Rambaud'

rudl = group lorenz & philipp & paul
devs = group rudl & thomas & anastasia & me

send "Hey, good to see you!" to anastasia
send "Welcome to the DSL workshop, hackers." to devs
send "Czesc!" to me & thomas

pause()

read messages from anastasia

on messages from paul, act {
    if (it.contains "lunch") {
        send "On my way!!" to paul
    } else {
        send "Have you heard? ${paul.name} wrote '$it' to me" to thomas
    }
}

pause()

ignore messages from paul

pause()
