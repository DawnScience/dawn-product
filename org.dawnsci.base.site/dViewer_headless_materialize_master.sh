#!/bin/bash

PRODUCT_NAME_VERSION="dawn master"
BUILD_LOG_FILE="build.log"

source dViewer_buckminster_init.sh $@
error=$?
[ "${BASH_SOURCE[0]}" != "${0}" ] && THIS_SOURCED=1 || THIS_SOURCED=0

while true; do
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
  #python ${BUILDER_WITH_COMMON_ARGS} --delete -Ddownload.location.common=public materialize dawnbase dawn master
  echo "Materializing ${PRODUCT_NAME_VERSION} component and its dependencies."
  python ${BUILDER_WITH_COMMON_ARGS} -Ddownload.location.common=public materialize ${PRODUCT_WORKSPACE_FOLDER} ${PRODUCT_NAME_VERSION}
  error=$?
  if [ ${error} -ne 0 ]; then
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
    git checkout ${PRODUCT_ID}
    error=$?
    if [ ${error} -ne 0 ]; then
      echo "Error (${error}): can not switch to ${PRODUCT_ID} branch"
      break
    fi
    cd ${cur_path}
  fi

  echo "Cleaning ${PRODUCT_NAME_VERSION} workspace."
  python ${BUILDER_WITH_COMMON_ARGS} clean
  error=$?
  if [ ${error} -ne 0 ]; then
    echo "Error (${error}): can not clean the workspace (by clean)"
    break
  fi
#  echo "Building ${PRODUCT_NAME_VERSION} workspace."
#  python ${BUILDER_WITH_COMMON_ARGS} build
#  error=$?
#  if [ ${error} -ne 0 ]; then
#    echo "Error (${error}): can not build the workspace"
#    break
#  fi
  echo "Cleaning ${PRODUCT_NAME_VERSION} previous buckminster output."
  python ${BUILDER_WITH_COMMON_ARGS} bmclean
  error=$?
  if [ ${error} -ne 0 ]; then
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
  echo "Building ${PRODUCT_NAME_VERSION} workspace and Eclipse product, then zip the product."
  python ${BUILDER_WITH_COMMON_ARGS} product.zip org.dawnsci.base.site linux32 linux64 >${BUILD_LOG_FILE} 2>&1
  error=$?
  if [ ${error} -ne 0 ]; then
    grep Error ${BUILD_LOG_FILE}
    echo "Error (${error}): can not build workspace and Eclipse product, thus can not zip the product"
    break
  fi
  break
done
cd ${CURRENT_PATH}
if [ ${error} -eq 0 ]; then
  echo "Product successfully built into "`pwd`"/products folder."
fi
[[ "${THIS_SOURCED}" -ne 0 ]] && return ${error} || exit ${error}
