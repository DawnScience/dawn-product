REM Script to run a workflow in batch

REM ##################################################################
REM # Please enter the paths required to run the workflow here

REM # Where your workspace is located (under the File-Switch Workspace menu in the UI)
set WORKSPACE=C:/Users/<your user name>/workspace

REM # The full path to where the model is located
set MODEL=%WORKSPACE%/workflows/examples/maths_example.moml


REM ##################################################################
REM # Nothing to do here...

dawn -noSplash -application com.isencia.passerelle.workbench.model.launch -data %WORKSPACE% -consolelog -vmargs -Dmodel=%MODEL%
