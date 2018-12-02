#!/usr/bin/env bash

javac ./flannelscript/lexer/Lexer.java
/bin/bash ./parse.sh --direct "`java flannelscript.lexer.Lexer $1`"
