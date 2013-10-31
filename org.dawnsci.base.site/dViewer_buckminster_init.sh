#!/bin/bash
#Input: output folder
#Output: CURRENT_PATH, PATH, BUILDER_WITH_COMMON_ARGS, PRODUCT_ID, PRODUCT_FOLDER, and installed buckminster

[ "${BASH_SOURCE[0]}" != "${0}" ] && THIS_SOURCED=1 || THIS_SOURCED=0
if [ -z "${1}" ] || [ ! -d "${1}" ]; then
  echo "Usage: ${0} <output folder>"
  echo "output folder: it must exist, and it must not be in git repository (it would interfere)."
  echo "The products folder in the output folder will contain the built products."
  [[ "${THIS_SOURCED}" -ne 0 ]] && return 1 || exit 1
fi
export PRODUCT_ID="dViewer"
BUCKMINSTER_URL="http://download.eclipse.org/tools/buckminster/headless-4.2/"
DIRECTOR_FILE="director_latest.zip"
#One can find mirrors on this page: http://www.eclipse.org/downloads/download.php?file=/tools/buckminster/products/director_latest.zip
DIRECTOR_URL1="http://mirror.ibcp.fr/pub/eclipse/tools/buckminster/products/director_latest.zip"
DIRECTOR_URL2="http://eclipse.ialto.com/tools/buckminster/products/director_latest.zip"
BUCKMINSTER_FOLDER="buckminster"
DIRECTOR_FOLDER="director"
BUILDER_FILE="dawn.py" #This could be "${PRODUCT_ID}.py" if it would be customised, but currently there is no need
UPDATE_BUILDER="" #Set this to anything to update the BUILDER_FILE
PYTHON_VERSION_REQUIRED="2.6"
export PRODUCT_FOLDER="dawnbase" #This could be "${PRODUCT_ID}_base" if the buckminster files would be customised as well
export CURRENT_PATH=`pwd`

is_equallessthan() {
  [ "$1" = "`echo -e "$1\n$2" | sort -V | head -n1`" ]
}

is_lessthan() {
  [ "$1" = "$2" ] && return 1 || is_equallessthan $1 $2
}

error=0
while true; do
  #Checking if is_lessthan works
  if ! is_lessthan "1" "2"; then
    error=1
    echo "Error (${error}): can not compare versions (probably too old sort)"
    break
  fi
  #Checking python version
  python_version=`python -V 2>&1 | grep -oe "Python [[:digit:]\.]*" | grep -oe "[[:digit:]\.]*"`
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not determine version of python (probably too old grep)"
    break
  fi
  if is_lessthan "${python_version}" "${PYTHON_VERSION_REQUIRED}"; then
    error=1
    echo "Error (${error}): python version ${PYTHON_VERSION_REQUIRED} is required"
    break
  fi
  #Resolving output folder, because symlinks confuse the director and buckminster
  WORKSPACE_PATH=`readlink -e "${1}" 2>/dev/null`
  error=$?
  if [ ${error} -ne 0 ]; then
    WORKSPACE_PATH=`readlink -f "${1}"`
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not resolve ${1} (maybe too old readlink)"
      break
    fi
  fi
  #Changing directory to workspace
  cd "${WORKSPACE_PATH}"
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not change to ${WORKSPACE_PATH} folder"
    break
  fi
  #Checking if git command is available and if yes, the output folder is not in git repository
  git --version >/dev/null
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): git command is unavailable"
    break
  fi
  git status >/dev/null 2>&1
  error=$?
  if [ ${error} -eq 0 ]; then
    error=1
    echo "Error (${error}): output folder ${WORKSPACE_PATH} is in git repository"
    break
  fi
  error=0
  if [ ! -r "${DIRECTOR_FILE}" ]; then
    echo "Downloading ${DIRECTOR_FILE}."
    wget -O "${DIRECTOR_FILE}" "${DIRECTOR_URL1}"
    error=$?
    if [ ${error} -ne 0 ]; then
      wget -O "${DIRECTOR_FILE}" "${DIRECTOR_URL2}"
      error=$?
    fi
  fi
  if [ ! -r "${DIRECTOR_FILE}" ]; then
    echo "Error (${error}): ${WORKSPACE_PATH}/${DIRECTOR_FILE} does not exist and can not be downloaded"
    break
  fi
  if [ ! -d "${DIRECTOR_FOLDER}" ]; then
    echo "Extracting ${DIRECTOR_FILE}."
    unzip "${DIRECTOR_FILE}"
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not extract ${WORKSPACE_PATH}/${DIRECTOR_FILE}"
      break
    fi
  fi
  if [ ! -d "${BUCKMINSTER_FOLDER}" ]; then
    echo "Installing buckminster by director."
    "${DIRECTOR_FOLDER}/director" repositories "${BUCKMINSTER_URL}" -destination "${WORKSPACE_PATH}/${BUCKMINSTER_FOLDER}" -profile Buckminster -installIUs org.eclipse.buckminster.cmdline.product
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not install buckminster by director"
      break
    fi
    BUCKMINSTER_FEATURE="org.eclipse.buckminster.core.headless.feature"
    echo "Installing ${BUCKMINSTER_FEATURE}."
    "${BUCKMINSTER_FOLDER}/buckminster" install "${BUCKMINSTER_URL}" ${BUCKMINSTER_FEATURE}
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not install ${BUCKMINSTER_FEATURE}"
      break
    fi
    BUCKMINSTER_FEATURE="org.eclipse.buckminster.pde.headless.feature"
    echo "Installing ${BUCKMINSTER_FEATURE}."
    "${BUCKMINSTER_FOLDER}/buckminster" install "${BUCKMINSTER_URL}" ${BUCKMINSTER_FEATURE}
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not install ${BUCKMINSTER_FEATURE}"
      break
    fi
    BUCKMINSTER_FEATURE="org.eclipse.buckminster.git.headless.feature"
    echo "Installing ${BUCKMINSTER_FEATURE}."
    "${BUCKMINSTER_FOLDER}/buckminster" install "${BUCKMINSTER_URL}" ${BUCKMINSTER_FEATURE}
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not install ${BUCKMINSTER_FEATURE}"
      break
    fi
  fi
  if [ ! -r "${BUILDER_FILE}" ] || [ -n "${UPDATE_BUILDER}" ]; then
#    ${BUILDER_FILE} should be in git repository, and then this copying would work. Unfortunately it is in webserver.
#    echo "Copying ${BUILDER_FILE}."
#    cp -p ${CURRENT_PATH}/${BUILDER_FILE} ./
#    error=$?
#    if [ ${error} -ne 0 ]; then
#      echo "Error (${error}): can not copy ${CURRENT_PATH}/${BUILDER_FILE} to ${WORKSPACE_PATH}"
#      break
#    fi
    echo "Downloading ${BUILDER_FILE}."
    #http://www.opengda.org/documentation/manuals/Infrastructure_Guide/trunk/software_required/dawn_py.html
    wget --tries=1 --timeout=5 --no-cache "http://www.opengda.org/buckminster/software/${BUILDER_FILE}"
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): ${WORKSPACE_PATH}/${BUILDER_FILE} does not exist and can not be downloaded"
      break
    fi
  fi
  if [ ! -r "${WORKSPACE_PATH}/.keyring" ]; then
    echo "Creating .keyring file."
    touch ${WORKSPACE_PATH}/.keyring
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not create ${WORKSPACE_PATH}/.keyring file"
      break
    fi
  fi
  export BUILDER_WITH_COMMON_ARGS="${WORKSPACE_PATH}/${BUILDER_FILE} --keep-proxy --buckminster.root.prefix=${WORKSPACE_PATH} --log-level=DEBUG --workspace ${WORKSPACE_PATH}/${PRODUCT_FOLDER} --keyring ${WORKSPACE_PATH}/.keyring"
  export PATH=${WORKSPACE_PATH}/${BUCKMINSTER_FOLDER}:$PATH
  break
done
if [ ${error} -ne 0 ]; then
  cd ${CURRENT_PATH}
fi
[[ "${THIS_SOURCED}" -ne 0 ]] && return ${error} || exit ${error}
