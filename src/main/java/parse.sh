#!/usr/bin/env bash

mkdir -p mandrill/parser/generatednodes

(cd mandrill/parser/generatednodes; jjtree ../Parser.jjt && mv ./Parser.jj ../Parser.jj)
(cd mandrill/parser/; javacc ./Parser.jj)
javac mandrill/parser/Parser.java

if [ $# -eq 0 ]
then
    java mandrill.parser.Parser
else
    java mandrill.parser.Parser $1
fi
