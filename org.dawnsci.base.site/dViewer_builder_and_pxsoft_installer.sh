#!/bin/bash
#Input: $1 as JRE_INSTALLER_PATH (default: current folder)
#Output: built and installed product into pxsoft

[ "${BASH_SOURCE[0]}" != "${0}" ] && THIS_SOURCED=1 || THIS_SOURCED=0
PRODUCT_ID="dViewer"
DAWN_PRODUCT_GIT_FOLDER="dawn-product.git"
DAWN_PRODUCT_GIT_URL="git@github.com:DawnScience/${DAWN_PRODUCT_GIT_FOLDER}"
BUILDER_FOLDER="vmaster"
PXSOFT_WRITER_HOST="scisoft8"
PRODUCT_PXSOFT_INSTALLER="dViewer_pxsoft_installer.sh"
if [ -z "${1}" ]; then
  export JRE_INSTALLER_PATH=`pwd`
else
  export JRE_INSTALLER_PATH="${1}"
fi

VERSION="v"`date +%Y%m%d`
CURRENT_PATH=`pwd`
WORKSPACE_PATH="${CURRENT_PATH}"
BUILDER_PATH="${WORKSPACE_PATH}/${BUILDER_FOLDER}"

#Give group write right to pxsoft members
umask g+w
error=0
while true; do
  #Checking if dViewer version is specified  
  if [[ "${DVIEWER_VERSION}" == "" ]]; then
    error=1
    printf "Error (${error}): DVIEWER_VERSION is not specified\n"
    break
  fi
  #Checking if git command is available and if yes, the output folder is not in git repository
  echo "Checking if output folder ${WORKSPACE_PATH} is not in git repository."
  (git --version >/dev/null)
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
  #Checking if zip is available
  (zip -h 1>/dev/null 2>&1)
  error=$?
  if [ ${error} -ne 0 ]; then
    printf "Error (${error}): zip does not exist\n"
    break
  fi
  error=0
  #Changing directory to workspace
  cd "${WORKSPACE_PATH}"
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not change to ${WORKSPACE_PATH} folder"
    break
  fi
  cur_path=`pwd`
  if [ -d "${DAWN_PRODUCT_GIT_FOLDER}/.git" ]; then
    echo "Updating ${DAWN_PRODUCT_GIT_FOLDER}."
    cd "${DAWN_PRODUCT_GIT_FOLDER}"
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not change to ${WORKSPACE_PATH}/${DAWN_PRODUCT_GIT_FOLDER} folder"
      break
    fi
    git pull
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not update ${DAWN_PRODUCT_GIT_FOLDER} by git pull"
      break
    fi
    cd ${cur_path}
  else
    echo "Downloading ${DAWN_PRODUCT_GIT_FOLDER}."
    git clone --branch master ${DAWN_PRODUCT_GIT_URL} ${DAWN_PRODUCT_GIT_FOLDER}
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not download ${DAWN_PRODUCT_GIT_FOLDER} by git clone"
      break
    fi
  fi
  PRODUCT_SITE_FOLDER="${DAWN_PRODUCT_GIT_FOLDER}/org.dawnsci.base.site"
  if [ ! -d ${PRODUCT_SITE_FOLDER} ]; then
    error=1
    echo "Error (${error}): ${PRODUCT_SITE_FOLDER} does not exist"
    break
  fi
  #Small hack to use dViewer product instead of Dawn product
  if [ ${PRODUCT_ID} == "dViewer" ]; then
    echo "Switching branch to ${PRODUCT_ID}."
    cd ${PRODUCT_SITE_FOLDER}
    git checkout ${PRODUCT_ID}
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not switch to ${PRODUCT_ID} branch"
      break
    fi
    cd ${cur_path}
  fi
  #Copying builder files to the builder folder
  if [ ! -d "${BUILDER_FOLDER}" ]; then
    echo "Creating ${BUILDER_FOLDER} folder."
    mkdir "${BUILDER_FOLDER}"
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not make ${BUILDER_FOLDER} folder"
      break
    fi
  fi
  cd ${BUILDER_FOLDER}
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not change to ${BUILDER_FOLDER} folder"
    break
  fi
  echo "Copying ${PRODUCT_ID}_*.sh."
  cp -pf "${cur_path}/${PRODUCT_SITE_FOLDER}/${PRODUCT_ID}_"*.sh ./
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not copy ${PRODUCT_ID}_*.sh to ${BUILDER_FOLDER} folder"
    break
  fi
  ./${PRODUCT_ID}_buckminster_materialize_master.sh .
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not materialize and build ${PRODUCT_ID}"
    break
  fi
  #Copy the compressed products to pxsoft and extract them
  source .get_pxsoft_target_root
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not query pxsoft target folder"
    break
  fi
  PRODUCT_TARGET_PATH="${pxsoft_target}/${PRODUCT_ID}/${VERSION}"
  PRODUCT_TARGET_INSTALLER_FOLDER_PATH="${pxsoft_target}/${PRODUCT_ID}/generic"
  PRODUCT_TARGET_INSTALLER_FILE_PATH="${PRODUCT_TARGET_INSTALLER_FOLDER_PATH}/${PRODUCT_PXSOFT_INSTALLER}"
  echo "Copying and calling the installer in pxsoft, this can take a while."
  ssh ${PXSOFT_WRITER_HOST} "bash --login -c \"cp -pf ${BUILDER_PATH}/${PRODUCT_PXSOFT_INSTALLER} ${PRODUCT_TARGET_INSTALLER_FOLDER_PATH}/; ${PRODUCT_TARGET_INSTALLER_FILE_PATH} ${BUILDER_PATH} ${PRODUCT_ID} ${VERSION}\""
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): the installer ${PRODUCT_TARGET_INSTALLER_PATH} in pxsoft failed"
    break
  fi
  echo "${PRODUCT_ID} is built and installed in ${PRODUCT_TARGET_PATH} succesfully."
  break
done
if [ ${error} -ne 0 ]; then
  echo "Error (${error}): pxsoft builder failed"
fi
cd ${CURRENT_PATH}
[[ "${THIS_SOURCED}" -ne 0 ]] && return ${error} || exit ${error}
