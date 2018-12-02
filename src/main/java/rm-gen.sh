#!/usr/bin/env bash

find ./flannelscript/parser -type f \
    -not -name '.gitignore' \
    -not -name 'ASTNode.java' \
    -not -name 'example.flns.lexed' \
    -not -name 'Parser.jjt' \
    -delete

find ./flannelscript -type f -name '*.class' -delete
