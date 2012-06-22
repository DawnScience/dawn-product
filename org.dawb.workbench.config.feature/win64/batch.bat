REM Script to run a workflow in batch

REM ##################################################################
REM # Please enter the paths required to run the workflow here

REM # Where your workspace is located (under the File-Switch Workspace menu in the UI)
set WORKSPACE=C:/users/<your name>/dawn_workspace

REM # The full path to where the model is located
set MODEL=%WORKSPACE%/workflows/examples/maths_example.moml

REM # This script assumes that 'HOSTTYPE' is set, change or set it here if not
HOSTTYPE=x86_64

##################################################################
# Nothing to do here...

./dawn -noSplash -application com.isencia.passerelle.workbench.model.launch -data %WORKSPACE% -consolelog -os linux -ws gtk -arch %HOSTTYPE% -vmargs -Dmodel=%MODEL%
