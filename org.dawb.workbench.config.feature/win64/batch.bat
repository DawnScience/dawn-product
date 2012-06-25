REM Script to run a workflow in batch

REM ##################################################################
REM # Please enter the paths required to run the workflow here

REM # Where your workspace is located (under the File-Switch Workspace menu in the UI)
set WORKSPACE=C:/Users/<your user name>/workspace

REM # The full path to where the model is located
set MODEL=%WORKSPACE%/workflows/examples/maths_example.moml


##################################################################
# Nothing to do here...

dawn -noSplash -application com.isencia.passerelle.workbench.model.launch -data %WORKSPACE% -consolelog -vmargs -Dorg.dawb.workbench.jmx.headless=true -Dcom.isencia.jmx.service.terminate=true -Dmodel=%MODEL%
