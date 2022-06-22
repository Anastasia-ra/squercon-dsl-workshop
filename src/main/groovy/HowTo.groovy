import groovy.transform.TupleConstructor

// variable declarations & typing

String str = "I'm a string"
def str2 = "I'm a string as well"
def str3 = 123 as String

// strings

def str4 = 'I am what I am'
def str5 = "I know $str4"
def name = "Johnny"
def multiLine = """ Hello
$name
how
are 
you?
"""

def calculated = "Sum is ${3 + 3}"

// regexp

def matches = "pattern" ==~ /pat.*/

// lists/arrays

def list = [1, 2, 3]
list << 4
list.add(5)

def itsThere = 2 in list

// maps

def map = ['a': 1, 'b': 2, 'c': 3]
map['d'] = 4
map.put('d', 4)
map.d = 4

def bVal = map['b']
bVal = map.get('b')
bVal = map.b

// spread operators

class Car {
    String make
    String model
}

def cars = [
        new Car(make: 'Peugeot', model: '508'),
        new Car(make: 'Renault', model: 'Clio')]
def makes = cars*.make
assert makes == ['Peugeot', 'Renault']

// spread method args

int function(int x, int y, int z) {
    return x * y + z
}

def args = [4, 5, 6]

assert function(*args) == 26

args = [4, 5]
assert function(*args, 6) == 26

// spread list

def items = [4, 5]
def listOfItems = [1, 2, 3, *items, 6]
assert listOfItems == [1, 2, 3, 4, 5, 6]

// spread map

def m1 = [c: 3, d: 4]
def mapOfItems = [a: 1, b: 2, *: m1]
assert mapOfItems == [a: 1, b: 2, c: 3, d: 4]

// elvis operator

def val = null
def withValue = val ?: "default"
assert withValue == "default"

// range

def range = 0..5
def inRange = 4 in range

assert ('a'..'d').collect() == ['a', 'b', 'c', 'd']

// function declarations

int getLengthOf(String s) {
    return s.size()
}

def getLengthOf2(def s) {
    s.size()
}

def length = getLengthOf("hello")
def length2 = getLengthOf "hello"

def doubleParams(String s, Integer i) {
    return s * i
}

def multiplied = doubleParams "hello", 3

def funcWithClosure(Closure action) {
    action()
}

funcWithClosure({ -> println "I'm a closure" })
funcWithClosure {
    println "I'm a closure"
}

def funcWithNotOnlyClosure(String s, Closure action) {
    action()
}

funcWithNotOnlyClosure("hello") {
    println "I'm a closure"
}

// classes

class C {
    String str
    int i

    C(String str, int i) {
        this.str = str
        this.i = i
    }
}

def c = new C("hallo", 5)

class C2 {
    String str
    int i
}

def c2 = new C2(str: "hallo", i: 5)


// traits

trait FlyingAbility {
    String fly() { "I'm flying!" }
}

trait SpeakingAbility {
    String speak() { "I'm speaking!" }
}

class Duck implements FlyingAbility, SpeakingAbility {}

def d = new Duck()
assert d.fly() == "I'm flying!"
assert d.speak() == "I'm speaking!"

// Closures

int item

def cl1 = { item++ }

def cl2 = { -> item++ }

def cl3 = { println it }

def cl4 = { it -> println it }

def cl5 = { n -> println n }

def cl6 = { String x, int y ->
    println "hey ${x} the value is ${y}"
}

def cl7 = { reader ->
    def line = reader.readLine()
    line.trim()
}

cl5.call(123)
cl5(123)

// command chains

// equivalent to: turn(left).then(right)
turn left then right

// equivalent to: take(2.pills).of(chloroquinine).after(6.hours)
take 2.pills of chloroquinine after 6.hours

// equivalent to: paint(wall).with(red, green).and(yellow)
paint wall with red, green and yellow

// with named parameters too
// equivalent to: check(that: margarita).tastes(good)
check that: margarita tastes good

// with closures as parameters
// equivalent to: given({}).when({}).then({})
given {} when {} then {}


// command chains for the brave

show = { println it }
square_root = { Math.sqrt(it) }

def please(action) {
    [the: { what ->
        [of: { n -> action(what(n)) }]
    }]
}

// equivalent to: please(show).the(square_root).of(100)
please show the square_root of 100


// operator overloading

@TupleConstructor(includes = "name")
class Person {
    String name
    String spouseName

    def plus(Person otherPerson) {
        this.spouseName = otherPerson.name
        otherPerson.spouseName = this.name
    }
}

def alice = new Person("alice")
def bob = new Person("bob")

alice + bob // got married

assert alice.spouseName == "bob"
assert bob.spouseName == "alice"
