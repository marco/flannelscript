#!/usr/bin/env bash

javac ./mandrill/lexer/Lexer.java
/bin/bash ./parse.sh --direct "`java mandrill.lexer.Lexer $1`"
