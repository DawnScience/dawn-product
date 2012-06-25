#!/bin/sh
# Script to run a workflow in batch

##################################################################
# Please enter the paths required to run the workflow here

# Where your workspace is located (under the File-Switch Workspace menu in the UI)
WORKSPACE=~/dawn_workspace

# The full path to where the model is located
MODEL=~/dawn_workspace/workflows/examples/maths_example.moml

# This script assumes that 'HOSTTYPE' is set, change or set it here if not
# HOSTTYPE=x86

##################################################################
# Nothing to do here...

./dawn -noSplash -application com.isencia.passerelle.workbench.model.launch -data $WORKSPACE -consolelog -os linux -ws gtk -arch $HOSTTYPE -vmargs -Dorg.dawb.workbench.jmx.headless=true -Dcom.isencia.jmx.service.terminate=true -Dmodel=$MODEL
