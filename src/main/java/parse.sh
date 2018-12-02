#!/usr/bin/env bash

mkdir -p flannelscript/parser/generatednodes

(cd flannelscript/parser/generatednodes; jjtree ../Parser.jjt && mv ./Parser.jj ../Parser.jj)
(cd flannelscript/parser/; javacc ./Parser.jj)
javac flannelscript/parser/Parser.java

if [ $# -eq 0 ]
then
    java flannelscript.parser.Parser
else
    java flannelscript.parser.Parser "$@"
fi
