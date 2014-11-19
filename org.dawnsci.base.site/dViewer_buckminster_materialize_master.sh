#!/bin/bash
#Input: JRE_INSTALLER_PATH
#Output: materialized and built product in products folder

PRODUCT_NAME_VERSION="dawn master"
MATERIALIZE_LOG_FILE="materialize.log"
BUILD_LOG_FILE="build.log"

source dViewer_buckminster_init.sh $@
error=$?
[ "${BASH_SOURCE[0]}" != "${0}" ] && THIS_SOURCED=1 || THIS_SOURCED=0

while true; do
  rm -rf ${MATERIALIZE_LOG_FILE}
  cur_path=`pwd`
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): ${PRODUCT_ID} buckminster initialization failed"
    break
  fi
  if [ -z "${BUILDER_WITH_COMMON_ARGS}" ]; then
    error=1
    echo "Error (${error}): BUILDER_WITH_COMMON_ARGS is not specified"
    break
  fi
  if [ -z "${JRE_INSTALLER_PATH}" ] || [ ! -d "${JRE_INSTALLER_PATH}" ]; then
    error=1
    echo "Error (${error}): JRE_INSTALLER_PATH is not specified or ${JRE_INSTALLER_PATH} does not exist"
    break
  fi
  #python ${BUILDER_WITH_COMMON_ARGS} --delete -Ddownload.location.common=public materialize dawnbase dawn master
  echo "Materializing ${PRODUCT_NAME_VERSION} component and its dependencies."
  python ${BUILDER_WITH_COMMON_ARGS} -Ddownload.location.common=public materialize ${PRODUCT_WORKSPACE_FOLDER} ${PRODUCT_NAME_VERSION} >>${MATERIALIZE_LOG_FILE} 2>&1
  error=$?
  if [ ${error} -ne 0 ]; then
    grep 'ERROR' -i ${MATERIALIZE_LOG_FILE}
    echo "Error (${error}): can not materialize component and its dependencies"
    break
  fi
  PRODUCT_SITE_FOLDER="${PRODUCT_WORKSPACE_FOLDER}_git/dawn-product.git/org.dawnsci.base.site"
  if [ ! -d ${PRODUCT_SITE_FOLDER} ]; then
    error=1
    echo "Error (${error}): ${PRODUCT_SITE_FOLDER} does not exist"
    break
  fi
  #Small hack to use dViewer product instead of Dawn product
  if [ ${PRODUCT_ID} == "dViewer" ]; then
    cd ${PRODUCT_SITE_FOLDER}
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not change to ${PRODUCT_SITE_FOLDER} folder"
      break
    fi
    git checkout ${PRODUCT_ID}
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not switch to ${PRODUCT_ID} branch"
      break
    fi
    cd "${cur_path}"
  fi
  #Updating the git repositories
  for d in "${PRODUCT_WORKSPACE_FOLDER}_git/"*.git; do
    if [ -d "${d}" ]; then
      echo "Updating ${d} by git pull."
      cd "${d}"
      error=$?
      if [ ${error} -ne 0 ]; then
        echo "Error (${error}): can not change to ${d} folder"
        break
      fi
      git pull
      error=$?
      if [ ${error} -ne 0 ]; then
        echo "Error (${error}): can not update the ${d} repository"
        break
      fi
      cd "${cur_path}"
    fi
  done
  if [ ${error} -ne 0 ]; then
    break
  fi
  PRODUCT_SITE_JRE_FOLDER="${PRODUCT_SITE_FOLDER}/jre-images/dawn-*/installed/"
  cd ${PRODUCT_SITE_JRE_FOLDER}
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not change to ${PRODUCT_SITE_JRE_FOLDER} folder"
    break
  fi
  echo "Creating jre symbolic links in ${PRODUCT_SITE_JRE_FOLDER} folder in ${PRODUCT_NAME_VERSION} workspace."
  #shopt for avoiding the loop if there is no such file
  shopt -s nullglob
  for f in "${JRE_INSTALLER_PATH}/"jre-*; do
    filename=`basename "${f}"`
    rm -f "${filename}"
    ln -s "${f}" "${filename}"
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not create symbolic link in ${PRODUCT_SITE_JRE_FOLDER} folder"
      break
    fi
  done
  if [ ${error} -ne 0 ]; then
    break
  fi
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not copy jre files to ${PRODUCT_SITE_JRE_FOLDER} folder"
    break
  fi
  cd "${cur_path}"
  echo "Cleaning ${PRODUCT_NAME_VERSION} workspace."
  python ${BUILDER_WITH_COMMON_ARGS} clean >>${MATERIALIZE_LOG_FILE} 2>&1
  error=$?
  if [ ${error} -ne 0 ]; then
    grep 'ERROR' -i ${MATERIALIZE_LOG_FILE}
    echo "Error (${error}): can not clean the workspace (by clean)"
    break
  fi
##  We do not need pure building, because building the product includes pure building
##  echo "Building ${PRODUCT_NAME_VERSION} workspace."
##  python ${BUILDER_WITH_COMMON_ARGS} build
##  error=$?
##  if [ ${error} -ne 0 ]; then
##    echo "Error (${error}): can not build the workspace"
##    break
##  fi
  echo "Cleaning ${PRODUCT_NAME_VERSION} previous buckminster output."
  python ${BUILDER_WITH_COMMON_ARGS} bmclean >>${MATERIALIZE_LOG_FILE} 2>&1
  error=$?
  if [ ${error} -ne 0 ]; then
    grep 'ERROR' -i ${MATERIALIZE_LOG_FILE}
    echo "Error (${error}): can not clean previous buckminster output (by bmclean)"
    break
  fi
  #bmclean does not clean everything (why?), so better deleting the contents of output folder
  rm -rf output/*
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not clean previous buckminster output"
    break
  fi
  echo "Cleaning ${PRODUCT_NAME_VERSION} previous products."
  #There is no buckminster command for doing this, so better deleting the contents of products folder
  rm -rf products/*
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not clean previous buckminster products"
    break
  fi
  DVIEWER_JRE_PATH="dawnvanilla_git/dawn-product.git/org.dawnsci.base.site/jre-images/dawn-${DVIEWER_VERSION}/installed"
  if [[ ! -d "dawnvanilla_git/dawn-product.git/.git" ]; then
    error=1
    echo "Error (${error}): dawn-product.git does not exist"
    break
  fi
  mkdir -p "${DVIEWER_JRE_PATH}"
  JRE_RELPATH="../../../../../../.."
  ln -s "${JRE_RELPATH}/jre-7u45-linux-i586.tar.gz" "${DVIEWER_JRE_PATH}/jre-7u45-linux-i586.tar.gz"
  ln -s "${JRE_RELPATH}/jre-7u45-linux-x64.tar.gz" "${DVIEWER_JRE_PATH}/jre-7u45-linux-x64.tar.gz"
  echo "Building ${PRODUCT_NAME_VERSION} workspace and ${PRODUCT_ID} Eclipse based product(s), then zip the product(s), this will take time."
  python ${BUILDER_WITH_COMMON_ARGS} product.zip org.dawnsci.base.site linux32 linux64 >${BUILD_LOG_FILE} 2>&1
  error=$?
  if [ ${error} -ne 0 ]; then
    grep 'Error:' -i ${BUILD_LOG_FILE}
    echo "Error (${error}): can not build workspace and ${PRODUCT_ID} Eclipse based product(s), thus can not zip the product(s)"
    break
  fi
  echo "Product successfully built into "`pwd`"/products folder."
  break
done
if [ ${error} -ne 0 ]; then
  echo "Error (${error}): buckminster materializer and builder failed"
fi
cd ${CURRENT_PATH}
[[ "${THIS_SOURCED}" -ne 0 ]] && return ${error} || exit ${error}
