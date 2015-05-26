package main

import "fmt"

func main() {
    fmt.Println(test())
    
    y := 1
        <error>y, _</error> := 10, 1
    fmt.Println(y)
}

func f1() int {return 1}
func f2() (int, int) {return 1, 2}

func test() int {
    x := f1()
    y, z := f2()

    <error>x</error> := f1()
    <error>y,   z</error>     := f2()

    fmt.Println(test21313())
    fmt.Println(Test2())

    x, a := f2() // Ok: `x` is reused and `a` is new
    b, x := f2() // Ok: `b` is new and `x` is reused

    return x + y + z + a + b // Just to avoid unused variable error
}

func test21313() (err int) {
    {
        err := 1
        return err
    }
    return 1
}

func bar() error {
    return nil
}

func Test2() (err error) {
    <error>err</error> := bar()
    if err := bar(); err != nil {
        return err
    } else {
        err := bar()
        return err
    }
    
    switch err := bar(); {  // missing switch expression means "true"
        case err != nil: return -err
        default: return err
    }
    
    var c chan int
    // todo: fix inspection
    //noinspection GoVarDeclaration
    select {
    case err, ok := (<-c):
        println(err)
        println(ok)
    }
    
    return err
}