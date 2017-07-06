#!/bin/sh
cd ../build/classes
jar -cmf ../../BuildFiles/manifest.txt ../../agent.jar instr/*