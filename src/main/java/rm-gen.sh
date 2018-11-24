#!/usr/bin/env bash

find ./mandrill/parser -type f \
    -not -name '.gitignore' \
    -not -name 'ASTNode.java' \
    -not -name 'example.mandrill.lexed' \
    -not -name 'Parser.jjt' \
    -delete

find ./mandrill -type f -name '*.class' -delete
