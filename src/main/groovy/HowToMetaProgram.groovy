// meta programming
// see http://docs.groovy-lang.org/docs/latest/html/documentation/xGroovyInterceptions.png.pagespeed.ic.52JMxy8wtl.webp

// extension functions

3.times { println "hello" }

Integer.metaClass.isEven = { -> delegate % 2 == 0 }

assert 6.isEven()
assert !7.isEven()

// virtual methods & properties

class MC implements GroovyInterceptable {

    def doSomething() {
        println "I did something"
    }

    @Override
    Object invokeMethod(String name, Object args) {
        "you called $name(${args})"
    }

    @Override
    void setProperty(String propertyName, Object newValue) {
        println "trying to set property $propertyName, but I will ignore it"
    }
}

def mc = new MC()

mc.doSomething()

println mc.doSomethingCompletelyDifferent()

mc.newProp = 123

// meta classes

class Foo {
    def bar() { "bar" }
}

def f = new Foo()

assert f.metaClass =~ /MetaClassImpl/

class MyFooMetaClass extends DelegatingMetaClass {
    MyFooMetaClass(MetaClass metaClass) { super(metaClass) }

    MyFooMetaClass(Class theClass) { super(theClass) }

    Object invokeMethod(Object object, String methodName, Object[] args) {
        def result = super.invokeMethod(object, methodName.toLowerCase(), args)
        result.toUpperCase();
    }
}


def metaClassObj = new MyFooMetaClass(Foo.metaClass)
metaClassObj.initialize()

Foo.metaClass = metaClassObj
def foo = new Foo()

assert foo.BAR() == "BAR" // the new metaclass routes .BAR() to .bar() and uppercases the result

