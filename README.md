# flannelscript

Flannelscript is a strongly typed, interpreted programming language (WIP).

## Syntax

*Fizzbuzz in flannelscript:*

```
Int i = 1;

while (i :<= 100) <
    if (i % 3 := 0 & i % 5 := 0) <
        echo 'Fizzbuzz';
    >

    if (i % 3 := 0 & i % 5 != 0) <
        echo 'Fizz';
    >

    if (i % 5 := 0 & i % 3 != 0) <
        echo 'Buzz';
    >

    if (!(i % 3 := 0) & !(i % 5 := 0)) <
        echo i;
    >

    i = i + 1;
>

echo 'Done';
```

**Types:**

 - "Str" type: `Str str = 'Hello, world!';`
 - "Int" type: `Int int = 13;`
 - "Flt" type: `Flt flt = 6.8;`
 - "Bln" type: `Bln bln = true;`

**Output:**

 - "Asking": `Str input = ask 'What is your name?'`
 - "Echoing": `echo 'Hello, World!'`

**Functions:**

```
getHello[Str name] Str <
    return 'Hello there ' + name;
>
```

**Classes:**

```
Animal <
    Str name = 'Rover';

    getGreeting[] Str <
        return 'Hi, ' + name + '!';
    >
>
```

## Installation and Running

**Installation:**

```bash
git clone https://github.com/skunkmb/flannelscript
cd flannelscript/
```

**Running a file:**

```bash
cd src/main/java/
./rm-gen.sh && ./run.sh YOUR_FILE.flns
```
